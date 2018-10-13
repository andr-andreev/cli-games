package games;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slot {
    private static final Logger logger = LoggerFactory.getLogger(Slot.class);

    private static final int SIZE = 7;

    public static void main(String... __) {
        int balance = 100;
        int bet = 10;
        int prize = 1000;

        int drum1 = 0;
        int drum2 = 0;
        int drum3 = 0;

        while (balance > 0) {
            logger.info("Your balance is ${}. Your bet is ${}.", balance, bet);

            drum1 = getNextValue(drum1);
            drum2 = getNextValue(drum2);
            drum3 = getNextValue(drum3);

            logger.info("A result of the spin: the 1st drum - {}, the 2nd drum - {}, the 3rd drum - {}", drum1, drum2, drum3);

            int result = isWinningCombination(drum1, drum2, drum3) ? prize : -bet;
            balance += result;

            logger.info(
                    "You {} ${}. Your balance is: ${}.",
                    result > 0 ? "won" : "lost",
                    Math.abs(result),
                    balance > 0 ? balance : 0
            );
        }
    }

    private static boolean isWinningCombination(int drum1, int drum2, int drum3) {
        return drum1 == drum2 && drum1 == drum3;
    }

    private static int getNextValue(final int currentValue) {
        return (currentValue + getMovementValue()) % SIZE;
    }

    private static int getMovementValue() {
        return (int) Math.round(Math.random() * 100);
    }
}