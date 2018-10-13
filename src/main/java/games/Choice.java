package games;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class Choice {
    private static final Logger logger = LoggerFactory.getLogger(Choice.class);

    static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String... __) throws IOException {
        logger.info(
                "Please select a game:\n" +
                        "1. Slot machine\n" +
                        "2. Drunkard\n" +
                        "3. Blackjack\n"
        );

        switch (getCharacterFromUser()) {
            case '1':
                Slot.main();
                break;
            case '2':
                Drunkard.main();
                break;
            case '3':
                Blackjack.main();
                break;
            default:
                logger.info("Invalid selection");
        }
    }

    static char getCharacterFromUser() throws IOException {
        byte[] input = new byte[1 + LINE_SEPARATOR.length()];
        if (System.in.read(input) != input.length) {
            throw new RuntimeException("Invalid input");
        }

        return (char) input[0];
    }
}
