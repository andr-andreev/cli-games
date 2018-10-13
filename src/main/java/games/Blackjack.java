package games;

import java.io.IOException;

public class Blackjack {
    private static final int MAX_VALUE = 21;
    private static final int MAX_CARDS_COUNT = 8;

    private static final int INTERACTIVE_PLAYER_INDEX = 0;
    private static final int NON_INTERACTIVE_PLAYER_INDEX = 1;
    private static final int NON_INTERACTIVE_HAND_VALUE_LIMIT = 16;

    private static final int BET = 10;

    private static int[] deck;
    private static int cursor;

    private static int[][] playersCards;
    private static int[] playersCursors;

    private static int[] playersBalances = {100, 100};

    public static void main(String... __) {
        deck = CardUtils.getShuffledDeck();

        while (getPlayerBalance(INTERACTIVE_PLAYER_INDEX) > 0 && getPlayerBalance(NON_INTERACTIVE_PLAYER_INDEX) > 0) {
            initRound();

            int betSum = 0;
            for (int playerIndex : new int[]{INTERACTIVE_PLAYER_INDEX, NON_INTERACTIVE_PLAYER_INDEX}) {
                playersBalances[playerIndex] -= BET;
                betSum += BET;
            }

            playInteractiveRound(INTERACTIVE_PLAYER_INDEX);
            playNonInteractiveRound(NON_INTERACTIVE_PLAYER_INDEX);

            int handValue1 = getHandValue(INTERACTIVE_PLAYER_INDEX);
            int handValue2 = getHandValue(NON_INTERACTIVE_PLAYER_INDEX);

            System.out.printf("Your hand's value is %d. Other player hand's value is %d.%n", handValue1, handValue2);

            int winnerPlayerIndex = handValue1 > handValue2 ? 0 : 1;

            playersBalances[winnerPlayerIndex] += betSum;

            if (winnerPlayerIndex == 0) {
                System.out.printf("You won $%d.%n%n", betSum - BET);
            } else {
                System.out.printf("You lost $%d.%n%n", BET);
            }
        }

        String gameResultMessage = getPlayerBalance(INTERACTIVE_PLAYER_INDEX) > 0
                ? "You won the game."
                : "You lost the game.";

        System.out.println(gameResultMessage);
    }

    private static void initRound() {
        System.out.printf(
                "Your balance is $%d. Other player's balance is $%d. Let's start a new round!%n",
                getPlayerBalance(INTERACTIVE_PLAYER_INDEX),
                getPlayerBalance(NON_INTERACTIVE_PLAYER_INDEX)
        );

        deck = CardUtils.getShuffledDeck();
        playersCards = new int[2][MAX_CARDS_COUNT];
        playersCursors = new int[]{0, 0};
        cursor = 0;
    }

    private static void playInteractiveRound(int playerIndex) {
        int[] cards = new int[]{addCardToPlayer(playerIndex), addCardToPlayer(playerIndex)};

        for (int card : cards) {
            printCardForInteractivePlayer(card);
        }

        while (getHandSum(playerIndex) < MAX_VALUE) {
            if (confirm("Do you want to take a new card?")) {
                int card = addCardToPlayer(playerIndex);
                printCardForInteractivePlayer(card);
            } else {
                break;
            }
        }
    }

    private static void playNonInteractiveRound(int playerIndex) {
        int[] cards = new int[]{addCardToPlayer(playerIndex), addCardToPlayer(playerIndex)};

        for (int card : cards) {
            printCardForNonInteractivePlayer(card);
        }

        while (getHandSum(playerIndex) < NON_INTERACTIVE_HAND_VALUE_LIMIT) {
            System.out.print("The player took a new card. ");
            int card = addCardToPlayer(playerIndex);

            printCardForNonInteractivePlayer(card);
        }
    }

    private static void printCardForInteractivePlayer(int card) {
        System.out.printf("You got a %s%n", CardUtils.toString(card));
    }

    private static void printCardForNonInteractivePlayer(int card) {
        System.out.printf("The player got a %s%n", CardUtils.toString(card));
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

        return sum <= 21 ? sum : 0;
    }

    static boolean confirm(String message) {
        System.out.println(message + " [y/n]");

        try {
            switch (Character.toUpperCase(Choice.getCharacterFromUser())) {
                case 'Y':
                    return true;
                default:
                    return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static int getPlayerBalance(int playerIndex) {
        return playersBalances[playerIndex];
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
