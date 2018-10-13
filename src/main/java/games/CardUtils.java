package games;

import org.apache.commons.math3.util.MathArrays;

class CardUtils {
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

    static final int RANKS_COUNT = Rank.values().length;
    static final int CARDS_COUNT = RANKS_COUNT * Suit.values().length;

    static int[] getShuffledDeck() {
        int[] deck = createRange(0, CardUtils.CARDS_COUNT);
        MathArrays.shuffle(deck);

        return deck;
    }

    static Suit getSuit(final int cardNumber) {
        return Suit.values()[cardNumber / RANKS_COUNT];
    }

    static Rank getRank(final int cardNumber) {
        return Rank.values()[cardNumber % RANKS_COUNT];
    }

    static String toString(final int cardNumber) {
        return getRank(cardNumber) + " " + getSuit(cardNumber);
    }

    private static int[] createRange(final int start, final int end) {
        int[] result = new int[end - start];

        for (int i = 0; i < end - start; i++) {
            result[i] = start + i;
        }

        return result;
    }
}
