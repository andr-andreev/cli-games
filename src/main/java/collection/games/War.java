package collection.games;

import collection.CardUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

public class War {
    private static final Logger logger = LoggerFactory.getLogger(War.class);

    private static final int PLAYERS_COUNT = 3;
    private static final int PLAYER_CARDS_COUNT = CardUtils.CARDS_COUNT / PLAYERS_COUNT;
    private static final int DRAW_RESULT = -1;

    private static final int[][] playersCards = new int[PLAYERS_COUNT][CardUtils.CARDS_COUNT];
    private static final int[] playersCardsBeginCursors = new int[PLAYERS_COUNT];
    private static final int[] playersCardsEndCursors = new int[PLAYERS_COUNT];
    private static final int[] playersCardCounts = new int[PLAYERS_COUNT];
    private static int[] activePlayers = new int[PLAYERS_COUNT];

    public static void main(String... __) {
        divideDeck(CardUtils.getShuffledDeck());

        printCards();

        for (int i = 0; i < activePlayers.length; i++) {
            activePlayers[i] = i;
        }

        while (activePlayers.length > 1) {
            makeTurn();
        }

        logger.info("Player {} won the game", getPlayerNumber(activePlayers[0]));
    }

    private static void divideDeck(final int[] deck) {
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            System.arraycopy(deck, i * PLAYER_CARDS_COUNT, playersCards[i], 0, PLAYER_CARDS_COUNT);

            playersCardsEndCursors[i] = PLAYER_CARDS_COUNT;
            playersCardCounts[i] = PLAYER_CARDS_COUNT;
        }
    }

    private static void makeTurn() {
        int[] cards = new int[PLAYERS_COUNT];

        for (int playerIndex : activePlayers) {
            int playerCard = getCardByPlayer(playerIndex);
            cards[playerIndex] = playerCard;
            logger.info("Player {} revealed a {}", getPlayerNumber(playerIndex), CardUtils.toString(playerCard));
        }

        int higherCardPlayerIndex = compareCards(cards);

        for (int playerIndex : activePlayers) {
            addCardToPlayer(higherCardPlayerIndex == DRAW_RESULT ? playerIndex : higherCardPlayerIndex, cards[playerIndex]);
        }

        String turnResult = higherCardPlayerIndex == DRAW_RESULT
                ? "Draw!"
                : String.format("Player %d won!", getPlayerNumber(higherCardPlayerIndex));

        logger.info("{}", turnResult);

        for (int playerIndex : activePlayers) {
            logger.info("Player {} has {} cards", getPlayerNumber(playerIndex), getPlayerCardCount(playerIndex));
        }

        activePlayers = Arrays.stream(activePlayers)
                .filter(playerIndex -> getPlayerCardCount(playerIndex) > 0)
                .toArray();
    }

    private static int compareCards(final int[] cards) {
        int higherCardPlayerIndex = activePlayers[0];
        int drawCount = 0;

        for (int playerIndex : activePlayers) {
            int card = cards[playerIndex];

            int result = comparePairOfCards(CardUtils.getRank(card), CardUtils.getRank(cards[higherCardPlayerIndex]));

            switch (result) {
                case 0:
                    drawCount++;
                    break;
                case 1:
                    higherCardPlayerIndex = playerIndex;
                    break;
                default:
                    break;
            }
        }

        if (drawCount == activePlayers.length) {
            return DRAW_RESULT;
        }

        return higherCardPlayerIndex;
    }

    private static int comparePairOfCards(final CardUtils.Rank card1, final CardUtils.Rank card2) {
        if ((card1 == CardUtils.Rank.ACE && card2 == CardUtils.Rank.SIX) || (card1 == CardUtils.Rank.SIX && card2 == CardUtils.Rank.ACE)) {
            return card1 == CardUtils.Rank.ACE ? -1 : 1;
        }

        return Integer.compare(card1.ordinal(), card2.ordinal());
    }

    private static void printCards() {
        for (int i = 0; i < activePlayers.length; i++) {
            logger.info("Player {} cards:", getPlayerNumber(i));
            for (int j = 0; j < getPlayerCardCount(activePlayers[i]); j++) {
                logger.info(CardUtils.toString(playersCards[i][j]));
            }
        }
    }

    private static int getPlayerCardCount(final int playerIndex) {
        return playersCardCounts[playerIndex];
    }

    private static int getCardByPlayer(final int playerIndex) {
        int currentIndex = playersCardsBeginCursors[playerIndex];
        int card = playersCards[playerIndex][currentIndex];

        playersCardsBeginCursors[playerIndex] = incrementIndex(currentIndex);
        playersCardCounts[playerIndex]--;

        return card;
    }

    private static void addCardToPlayer(final int playerIndex, final int card) {
        int currentIndex = playersCardsEndCursors[playerIndex];
        playersCards[playerIndex][currentIndex] = card;

        playersCardsEndCursors[playerIndex] = incrementIndex(currentIndex);
        playersCardCounts[playerIndex]++;
    }


    private static int incrementIndex(final int i) {
        return (i + 1) % CardUtils.CARDS_COUNT;
    }

    private static int getPlayerNumber(final int playerIndex) {
        return playerIndex + 1;
    }
}