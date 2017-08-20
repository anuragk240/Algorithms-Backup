import java.util.Scanner;

public class Change {
    private static int getChange(int m) {
        //write your code here
        int tens = 0, fives = 0, ones = 0;
        tens = m / 10;
        fives = m % 10 / 5;
        ones = m % 10 % 5;
        return tens + fives + ones;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        System.out.println(getChange(m));

    }
}

