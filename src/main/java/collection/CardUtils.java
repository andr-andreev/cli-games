package collection;

import org.apache.commons.math3.util.MathArrays;

public class CardUtils {
    public enum Suit {
        SPADES,
        HEARTS,
        CLUBS,
        DIAMONDS,
    }

    public enum Rank {
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

    public static final int RANKS_COUNT = Rank.values().length;
    public static final int CARDS_COUNT = RANKS_COUNT * Suit.values().length;

    public static int[] getShuffledDeck() {
        int[] deck = createRange(0, CardUtils.CARDS_COUNT);
        MathArrays.shuffle(deck);

        return deck;
    }

    public static Suit getSuit(final int cardNumber) {
        return Suit.values()[cardNumber / RANKS_COUNT];
    }

    public static Rank getRank(final int cardNumber) {
        return Rank.values()[cardNumber % RANKS_COUNT];
    }

    public static String toString(final int cardNumber) {
        return getRank(cardNumber) + " of " + getSuit(cardNumber);
    }

    private static int[] createRange(final int start, final int end) {
        int[] result = new int[end - start];

        for (int i = 0; i < end - start; i++) {
            result[i] = start + i;
        }

        return result;
    }
}
