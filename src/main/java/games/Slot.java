package games;

public class Slot {
    private static final int SIZE = 7;

    private int balance;
    private int bet;
    private int prize;

    private int drum1 = 0;
    private int drum2 = 0;
    private int drum3 = 0;

    public static void main(String... __) {
        Slot game = new Slot(100, 10, 1000);

        game.play();
    }

    public Slot(final int balance, final int bet, final int prize) {
        this.balance = balance;
        this.bet = bet;
        this.prize = prize;
    }

    public void play() {
        while (getBalance() > 0) {
            System.out.println(String.format("Your balance is $%d. Your bet is $%d.", getBalance(), bet));
            spin();

            System.out.println("Spin result:");
            System.out.println(String.format("The 1st drum - %d, the 2nd drum - %d, the 3rd drum - %d", drum1, drum2, drum3));

            int result = getSpinResult();
            modifyBalance(result);

            System.out.println(
                    String.format(
                            "You %s $%d. Your balance is: $%d.\n",
                            result > 0 ? "won" : "lost",
                            Math.abs(result),
                            getBalance()
                    )
            );
        }
    }

    private int getBalance() {
        return balance > 0 ? balance : 0;
    }

    private void modifyBalance(final int amount) {
        balance += amount;
    }

    private void spin() {
        drum1 = getNextValue(drum1);
        drum2 = getNextValue(drum2);
        drum3 = getNextValue(drum3);
    }

    private int getNextValue(final int currentValue) {
        return (currentValue + getMovementValue()) % SIZE;
    }

    private int getMovementValue() {
        return (int) Math.round(Math.random() * 100);
    }

    private int getSpinResult() {
        boolean isWinningCombination = drum1 == drum2 && drum1 == drum3;

        return isWinningCombination ? prize : -bet;
    }
}