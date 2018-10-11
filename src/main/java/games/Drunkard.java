package games;

import org.apache.commons.math3.util.MathArrays;

import java.util.stream.IntStream;

public class Drunkard {
    private static final Integer PLAYERS_COUNT = 2;
    private static final Integer RANKS_COUNT = Rank.values().length;
    private static final Integer CARDS_COUNT = RANKS_COUNT * Suit.values().length;
    private static final Integer PLAYER_CARDS_COUNT = CARDS_COUNT / PLAYERS_COUNT;

    private static int[] players = new int[PLAYERS_COUNT];
    private static int[][] playersCards = new int[PLAYERS_COUNT][CARDS_COUNT];
    private static int[] playersCardsBeginCursors = new int[PLAYERS_COUNT];
    private static int[] playersCardsEndCursors = new int[PLAYERS_COUNT];

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
        int[] deck = createDeck();
        divideDeck(deck);

        printCards();

        players = IntStream.range(0, PLAYERS_COUNT).toArray();

        while (getWinner() == null) {
            makeTurn();
        }

        System.out.println();
        System.out.printf("Player %d won the game", getPlayerNumber(getWinner()));
    }


    private static int[] createDeck() {
        int[] deck = IntStream.range(0, CARDS_COUNT).toArray();
        MathArrays.shuffle(deck);

        return deck;
    }

    private static void divideDeck(int[] deck) {
        for (Integer i = 0; i < PLAYERS_COUNT; i++) {
            System.arraycopy(deck, i * PLAYER_CARDS_COUNT, playersCards[i], 0, PLAYER_CARDS_COUNT);

            playersCardsEndCursors[i] = PLAYER_CARDS_COUNT;
        }
    }

    private static void makeTurn() {
        int[] cards = new int[PLAYERS_COUNT];

        for (Integer playerIndex : players) {
            cards[playerIndex] = getCardByPlayer(playerIndex);
        }

        Integer winner = compareCards(cards);

        if (winner == null) {
            for (Integer playerIndex : players) {
                addCardToPlayer(playerIndex, cards[playerIndex]);
            }
        } else {
            for (Integer card : cards) {
                addCardToPlayer(winner, card);
            }
        }

        for (Integer playerIndex : players) {
            System.out.printf("Player %d revealed %14s; ", getPlayerNumber(playerIndex), toString(cards[playerIndex]));
        }

        String turnResult = winner == null
                ? "Draw! "
                : String.format("Player %d won! ", getPlayerNumber(winner));

        System.out.printf("%-17s", turnResult);

        for (Integer playerIndex : players) {
            System.out.printf("Player %d has %2d cards; ", getPlayerNumber(playerIndex), cardsOwnedByPlayer(playerIndex));
        }
        System.out.println();
    }

    private static Integer getWinner() {
        for (Integer playerIndex : players) {
            if (cardsOwnedByPlayer(playerIndex).equals(CARDS_COUNT)) {
                return playerIndex;
            }
        }

        return null;
    }

    private static Integer compareCards(final int[] cards) {
        Integer higherCardIndex = 0;

        Integer drawCount = 0;

        for (Integer i = 0; i < cards.length; i++) {
            Integer card = cards[i];

            Integer result = comparePairOfCards(getCardRank(card), getCardRank(cards[higherCardIndex]));

            switch (result) {
                case 0:
                    drawCount++;
                    break;
                case 1:
                    higherCardIndex = i;
                    break;
                default:
                    break;
            }
        }

        if (drawCount == cards.length) {
            return null;
        }

        return higherCardIndex;
    }

    private static Integer comparePairOfCards(Rank card1, Rank card2) {
        if (card1 == Rank.ACE && card2 == Rank.SIX) {
            return -1;
        }

        if (card1 == Rank.SIX && card2 == Rank.ACE) {
            return 1;
        }

        return Integer.compare(card1.ordinal(), card2.ordinal());
    }

    private static Suit getCardSuit(final Integer cardNumber) {
        return Suit.values()[cardNumber / RANKS_COUNT];
    }

    private static Rank getCardRank(final Integer cardNumber) {
        return Rank.values()[cardNumber % RANKS_COUNT];
    }

    private static void printCards() {
        for (Integer i = 0; i < PLAYERS_COUNT; i++) {
            System.out.printf("Player %d cards:%n", getPlayerNumber(i));
            for (Integer j = 0; j < PLAYER_CARDS_COUNT; j++) {
                System.out.println(toString(playersCards[i][j]));
            }

            System.out.println();
        }
    }

    private static String toString(final Integer cardNumber) {
        return getCardRank(cardNumber) + " " + getCardSuit(cardNumber);
    }

    private static Integer cardsOwnedByPlayer(final Integer playerIndex) {
        Integer count = playersCardsEndCursors[playerIndex] - playersCardsBeginCursors[playerIndex];

        if (count < 0) {
            count += CARDS_COUNT;
        }

        return count;
    }

    private static Integer getCardByPlayer(final Integer playerIndex) {
        Integer currentIndex = playersCardsBeginCursors[playerIndex];
        Integer card = playersCards[playerIndex][currentIndex];
        playersCardsBeginCursors[playerIndex] = incrementIndex(currentIndex);

        return card;
    }

    private static void addCardToPlayer(final Integer playerNum, final Integer card) {
        Integer currentIndex = playersCardsEndCursors[playerNum];
        playersCards[playerNum][currentIndex] = card;
        Integer nextIndex = incrementIndex(currentIndex);
        playersCardsEndCursors[playerNum] = nextIndex;
    }


    private static Integer incrementIndex(Integer i) {
        return (i + 1) % CARDS_COUNT;
    }

    private static Integer getPlayerNumber(Integer playerIndex) {
        return playerIndex + 1;
    }
}