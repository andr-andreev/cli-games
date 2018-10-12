package games;

import org.apache.commons.math3.util.MathArrays;

import java.util.Arrays;

public class Drunkard {
    private static final int PLAYERS_COUNT = 3;
    private static final int RANKS_COUNT = Rank.values().length;
    private static final int CARDS_COUNT = RANKS_COUNT * Suit.values().length;
    private static final int PLAYER_CARDS_COUNT = CARDS_COUNT / PLAYERS_COUNT;
    private static final int DRAW_RESULT = -1;

    private static int[][] playersCards = new int[PLAYERS_COUNT][CARDS_COUNT];
    private static int[] playersCardsBeginCursors = new int[PLAYERS_COUNT];
    private static int[] playersCardsEndCursors = new int[PLAYERS_COUNT];
    private static int[] playersCardCounts = new int[PLAYERS_COUNT];
    private static int[] activePlayers = new int[PLAYERS_COUNT];

    enum Suit {
        SPADES,
        HEARTS,
        CLUBS,
        DIAMONDS,
    }

    enum Rank {
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE,
    }

    public static void main(String... __) {
        divideDeck(createDeck());

        printCards();

        activePlayers = createRange(0, PLAYERS_COUNT);

        while (activePlayers.length > 1) {
            makeTurn();
        }

        System.out.printf("%nPlayer %d won the game.", getPlayerNumber(activePlayers[0]));
    }


    private static int[] createDeck() {
        int[] deck = createRange(0, CARDS_COUNT);
        MathArrays.shuffle(deck);

        return deck;
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
            System.out.printf("Player %d revealed %14s; ", getPlayerNumber(playerIndex), toString(playerCard));
        }

        int higherCardPlayerIndex = compareCards(cards);

        for (int playerIndex : activePlayers) {
            addCardToPlayer(higherCardPlayerIndex == DRAW_RESULT ? playerIndex : higherCardPlayerIndex, cards[playerIndex]);
        }

        String turnResult = higherCardPlayerIndex == DRAW_RESULT
                ? "Draw! "
                : String.format("Player %d won!", getPlayerNumber(higherCardPlayerIndex));

        System.out.printf("%-14s", turnResult);

        for (int playerIndex : activePlayers) {
            System.out.printf("Player %d has %2d cards; ", getPlayerNumber(playerIndex), getPlayerCardCount(playerIndex));
        }

        activePlayers = Arrays.stream(activePlayers)
                .filter(playerIndex -> getPlayerCardCount(playerIndex) > 0)
                .toArray();

        System.out.println();
    }

    private static int compareCards(final int[] cards) {
        int higherCardPlayerIndex = activePlayers[0];
        int drawCount = 0;

        for (int playerIndex : activePlayers) {
            int card = cards[playerIndex];

            int result = comparePairOfCards(getCardRank(card), getCardRank(cards[higherCardPlayerIndex]));

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

    private static int comparePairOfCards(final Rank card1, final Rank card2) {
        if ((card1 == Rank.ACE && card2 == Rank.SIX) || (card1 == Rank.SIX && card2 == Rank.ACE)) {
            return card1 == Rank.ACE ? -1 : 1;
        }

        return Integer.compare(card1.ordinal(), card2.ordinal());
    }

    private static Suit getCardSuit(final int cardNumber) {
        return Suit.values()[cardNumber / RANKS_COUNT];
    }

    private static Rank getCardRank(final int cardNumber) {
        return Rank.values()[cardNumber % RANKS_COUNT];
    }

    private static void printCards() {
        for (int i = 0; i < activePlayers.length; i++) {
            System.out.printf("Player %d cards:%n", getPlayerNumber(i));
            for (int j = 0; j < getPlayerCardCount(activePlayers[i]); j++) {
                System.out.println(toString(playersCards[i][j]));
            }

            System.out.println();
        }
    }

    private static String toString(final int cardNumber) {
        return getCardRank(cardNumber) + " " + getCardSuit(cardNumber);
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
        return (i + 1) % CARDS_COUNT;
    }

    private static int getPlayerNumber(final int playerIndex) {
        return playerIndex + 1;
    }

    private static int[] createRange(final int start, final int end) {
        int[] result = new int[end - start];

        for (int i = 0; i < end - start; i++) {
            result[i] = start + i;
        }

        return result;
    }
}