package games;

import java.io.IOException;

public class Choice {
    public static void main(String... __) throws IOException {
        System.out.println("Please select a game:");
        System.out.println("1. Slot machine");
        System.out.println("2. Drunkard");

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
