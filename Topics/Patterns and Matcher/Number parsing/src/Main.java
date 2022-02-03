import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String n1 = "0|([+-]?[123456789]\\d*[\\.,]\\d*[123456789])";
        String n2 = "[+-]?[123456789]\\d*[\\.,]\\d";
        String n3 = "([+-]?0[\\.,]\\d*[123456789])|([+-]?0[\\.,]\\d)";
        String n4 = "[+-]?[123456789]\\d*";
        String number = scanner.nextLine();
        System.out.println(number.matches(n1) || number.matches(n2) ||
                number.matches(n3) || number.matches(n4) ? "YES" : "NO");
    }
}