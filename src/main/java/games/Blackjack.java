package games;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Blackjack {
    private static final Logger logger = LoggerFactory.getLogger(Blackjack.class);

    private static final int MAX_VALUE = 21;
    private static final int MAX_CARDS_COUNT = 8;

    private static final int INTERACTIVE_PLAYER_INDEX = 0;
    private static final int NON_INTERACTIVE_PLAYER_INDEX = 1;

    private static final int INTERACTIVE_HAND_VALUE_LIMIT = 20;
    private static final int NON_INTERACTIVE_HAND_VALUE_LIMIT = 16;

    private static final int DRAW_RESULT = -1;

    private static final int BET = 10;

    private static int[] deck;
    private static int cursor;

    private static int[] players = {INTERACTIVE_PLAYER_INDEX, NON_INTERACTIVE_PLAYER_INDEX};
    private static int[] playersBalances = {100, 100};
    private static int[][] playersCards;
    private static int[] playersCursors;

    public static void main(String... __) throws IOException {
        deck = CardUtils.getShuffledDeck();

        while (playersBalances[INTERACTIVE_PLAYER_INDEX] > 0 && playersBalances[NON_INTERACTIVE_PLAYER_INDEX] > 0) {
            initRound();

            int betSum = 0;
            for (int playerIndex : players) {
                playersBalances[playerIndex] -= BET;
                betSum += BET;
            }

            int handValue1 = playInteractiveRound(INTERACTIVE_PLAYER_INDEX);
            int handValue2 = playNonInteractiveRound(NON_INTERACTIVE_PLAYER_INDEX);

            logger.info("Your hand's value is {}. Other player hand's value is {}.", handValue1, handValue2);

            int winnerPlayerIndex = handValue1 == handValue2
                    ? DRAW_RESULT
                    : (handValue1 > handValue2 ? INTERACTIVE_PLAYER_INDEX : NON_INTERACTIVE_PLAYER_INDEX);

            for (int playerIndex : players) {
                if (winnerPlayerIndex == DRAW_RESULT) {
                    playersBalances[playerIndex] += BET;
                } else if (playerIndex == winnerPlayerIndex) {
                    playersBalances[playerIndex] += betSum;
                }
            }

            switch (winnerPlayerIndex) {
                case DRAW_RESULT:
                    logger.info("Draw");
                    break;
                case INTERACTIVE_PLAYER_INDEX:
                    logger.info("You won ${}", betSum - BET);
                    break;
                default:
                    logger.info("You lost ${}", BET);
                    break;
            }
        }

        String gameResultMessage = playersBalances[INTERACTIVE_PLAYER_INDEX] > 0
                ? "You won the game"
                : "You lost the game";

        logger.info(gameResultMessage);
    }

    private static void initRound() {
        logger.info(
                "Your balance is ${}. Other player's balance is ${}. Let's start a new round!",
                playersBalances[INTERACTIVE_PLAYER_INDEX],
                playersBalances[NON_INTERACTIVE_PLAYER_INDEX]
        );

        deck = CardUtils.getShuffledDeck();
        playersCards = new int[2][MAX_CARDS_COUNT];
        playersCursors = new int[]{0, 0};
        cursor = 0;
    }

    private static int playInteractiveRound(int playerIndex) throws IOException {
        int cardCount = 0;

        while (cardCount < 2 || (getHandSum(playerIndex) < INTERACTIVE_HAND_VALUE_LIMIT && confirm("Do you want to take a new card?"))) {
            int card = addCardToPlayer(playerIndex);
            cardCount++;

            logger.info("You got a {}", CardUtils.toString(card));
        }

        return getHandValue(playerIndex);
    }

    private static int playNonInteractiveRound(int playerIndex) {
        int cardCount = 0;

        while (cardCount < 2 || getHandSum(playerIndex) < NON_INTERACTIVE_HAND_VALUE_LIMIT) {
            if (cardCount >= 2) {
                logger.info("The player took a new card");
            }

            int card = addCardToPlayer(playerIndex);
            cardCount++;

            logger.info("The player got a {}", CardUtils.toString(card));
        }

        return getHandValue(playerIndex);
    }

    private static int addCardToPlayer(int player) {
        int card = deck[cursor];
        cursor++;

        int playerCursor = playersCursors[player];
        playersCards[player][playerCursor] = card;

        playersCursors[player]++;

        return card;
    }

    static int getHandSum(int player) {
        int sum = 0;
        for (int i = 0; i < playersCursors[player]; i++) {
            sum += getCardValue(playersCards[player][i]);
        }

        return sum;
    }

    static int getHandValue(int player) {
        int sum = getHandSum(player);

        return sum <= MAX_VALUE ? sum : 0;
    }

    static boolean confirm(String message) throws IOException {
        logger.info(message + " [y/n]");

        switch (Choice.getCharacterFromUser()) {
            case 'Y':
            case 'y':
                return true;
            default:
                return false;
        }
    }

    private static int getCardValue(int card) {
        switch (CardUtils.getRank(card)) {
            case JACK:
                return 2;
            case QUEEN:
                return 3;
            case KING:
                return 4;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            case TEN:
                return 10;
            case ACE:
                return 11;
            default:
                return 0;
        }
    }
}
