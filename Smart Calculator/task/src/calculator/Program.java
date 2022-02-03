package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {
    private Scanner scanner = new Scanner(System.in);
    private Map<String, BigInteger> map = new HashMap<>();


    public void start() {
        while (true) {
            String line = scanner.nextLine();
            Pattern pattern = Pattern.compile("/[A-Za-z]+");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                if (line.equals("/exit")) {
                    System.out.println("Bye!");
                    break;
                } else if (line.equals("/help")) {
                    System.out.println("The program calculates the sum and subtraction of numbers");
                } else {
                    System.out.println("Unknown command");
                    continue;
                }
            }

            if (line.contains("=")) {
                String[] array = line.split("=");
                if (array.length > 2) {
                    System.out.println("Invalid assignment");
                    continue;
                }
                array[0] = array[0].replaceAll("\\s", "");
                array[1] = array[1].replaceAll("\\s", "");
                if (!array[0].matches("[A-Za-z]+")) {
                    System.out.println("Invalid identifier");
                    continue;
                }
                if (array[1].matches("-*\\d+")) {
                    map.put(array[0], new BigInteger(array[1]));
                    continue;
                } else if (array[1].matches("[A-Za-z]+")) {
                    if (map.containsKey(array[1])) {
                        map.put(array[0], map.get(array[1]));
                    } else {
                        System.out.println("Unknown variable");
                        continue;
                    }
                } else {
                    System.out.println("Invalid assignment");
                    continue;
                }

                continue;
            }


            if (line.matches("[A-Za-z]+\\s*")) {
                if (map.containsKey(line)) {
                    System.out.println(map.get(line));
                    continue;
                } else {
                    System.out.println("Unknown variable");
                    continue;
                }
            }

            if (line.contains("(") || line.contains(")")) {
                int count = 0;
                String[] array = line.split("");

                for (int i = 0; i < array.length; i++) {
                    if (array[i].equals("(")) {
                        count++;
                    }
                    if (array[i].equals(")")) {
                        count--;
                    }
                }
                if (count != 0) {
                    System.out.println("Invalid expression");
                    continue;
                }
            }

            if (line.matches(".*\\*{2,}.*") || line.matches(".*/{2,}.*")) {
                System.out.println("Invalid expression");
                continue;
            }

            if (!line.isEmpty()) {
                makeStack(line);
            }

        }
    }

    public void makeStack(String line) {
        Deque<String> deque = new ArrayDeque<>();
        String entering = line.replaceAll(" ", "");
        String regex = String.format("(?<=%1$s)|(?=%1$s)", "[ \\-\\+\\*/\\(\\)\\^]");
        Pattern pattern = Pattern.compile("\\-{2}");
        Pattern pattern1 = Pattern.compile("\\+{2}");
        Pattern pattern2 = Pattern.compile("\\+-|\\-\\+");
        Matcher matcher = pattern.matcher(entering);
        Matcher matcher1 = pattern1.matcher(entering);
        Matcher matcher2 = pattern2.matcher(entering);

        while (matcher.find() || matcher1.find() || matcher2.find()) {
            entering = entering.replaceAll("\\-{2}", "+");
            entering = entering.replaceAll("\\+{2}", "+");
            entering = entering.replaceAll("\\+-|\\-\\+", "-");
        }
        String result = "";
        String[] array = entering.split(regex);

        for (int i = 0; i < array.length; i++) {
            if (array[i].matches("[\\d]+")) {
                result += array[i] + " ";
            } else if (array[i].matches("[A-Za-z]+")) {
                if (map.containsKey(array[i])) {
                    result += map.get(array[i]) + " ";
                } else {
                    System.out.println("Invalid expression");
                    return;
                }
            } else if (array[i].matches("[ \\-\\+\\*/\\(\\)\\^]")) {
                if ((i == 0 && array[i].equals("-")) || (array[i].equals("-") && array[i - 1].equals("("))) {
                    result += array[i];
                } else if (array[i].equals("(")) {
                    deque.push(array[i]);
                } else if (array[i].equals(")")) {
                    while (!deque.peek().equals("(") && deque.size() > 0) {
                        result += deque.poll() + " ";
                    }
                    deque.poll();
                } else if (deque.isEmpty() ||(deque.size() > 0 && deque.peek().equals("("))) {
                    deque.push(array[i]);
                } else if (deque.size() > 0 && (priority(deque.peek()) < priority(array[i]))) {
                    deque.push(array[i]);
                } else {
                    while (deque.size() > 0 && (priority(deque.peek()) >= priority(array[i]))) {
                        result += deque.poll() + " ";
                    }
                    deque.push(array[i]);
                }
            }
        }
        while (!deque.isEmpty()) {
            result += deque.poll() + " ";
        }
        calculate(result);

    }

    private void calculate(String postFix) {
        Deque<String> deque = new ArrayDeque<>();
        String[] array = postFix.split(" ");
        for (int i = 0; i < array.length; i++) {
            if (array[i].matches("-*\\d+")) {
                deque.push(array[i]);
            } else {
                BigInteger action = procedures(deque.poll(), deque.poll(), array[i]);
                deque.push(String.valueOf(action));
            }
        }
        System.out.println(deque.peek());
    }

    private BigInteger procedures(String a, String b, String operation) {
        switch (operation) {
            case "+":
                return new BigInteger(b).add(new BigInteger(a));
            case "-":
                return new BigInteger(b).subtract(new BigInteger(a));
            case "*":
                return new BigInteger(b).multiply(new BigInteger(a));
            case "/":
                return new BigInteger(b).divide(new BigInteger(a));
            case "^":
                return new BigInteger(b).pow(Integer.valueOf(a));
            default:
                return null;
        }
    }


    private int priority(String a) {
        if (a == null) {
            return 0;
        } else if (a.equals("^")) {
            return 4;
        } else if (a.equals("*") || (a.equals("/"))) {
            return 3;
        } else if (a.equals("+") || a.equals("-")) {
            return 2;
        } else if (a.equals("(") || a.equals(")")) {
            return 1;
        } else {
            return 0;
        }
    }
}
