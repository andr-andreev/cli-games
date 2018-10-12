package games;

import java.io.IOException;

public class Choice {
    public static void main(String... __) throws IOException {
        System.out.println(
                "Please select a game:\n" +
                        "1. Slot machine\n" +
                        "2. Drunkard\n"
        );

        switch (System.in.read()) {
            case '1':
                Slot.main();
                break;
            case '2':
                Drunkard.main();
                break;
            default:
                System.out.println("Invalid selection");
        }
    }
}
