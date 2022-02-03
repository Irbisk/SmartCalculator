import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = Integer.parseInt(scanner.nextLine());
        String line = scanner.nextLine();
        String symbol = "[A-Za-z]";

        String regexp = "(^|.*[^A-Za-z])";
        for (int i = 0; i < size; i++) {
            regexp += symbol;
        }
        regexp += "[^A-Za-z].*";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }


        // write your code here
    }
}