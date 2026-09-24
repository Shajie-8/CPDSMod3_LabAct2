import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Macabalang_Module3_LabAct2 {

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    static int precedence(char c) {
        if (c == '+' || c == '-') return 1;
        if (c == '*' || c == '/') return 2;
        return 0;
    }

    static String join(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    static String stackToString(Stack<Character> stack) {
        if (stack.isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (char c : stack) sb.append(c);
        return sb.toString();
    }

    static void printHeader() {
        System.out.printf("%-8s %-42s %-10s %s%n", "Symbol", "Action", "Stack", "Output");
        System.out.println("=".repeat(80));
    }

    static void printRow(String symbol, String action, Stack<Character> stack, StringBuilder out) {
        System.out.printf("%-8s %-42s %-10s %s%n", symbol, action, stackToString(stack), out);
    }

    static String convert(String exp, boolean forPrefix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder out = new StringBuilder();
        printHeader();

        for (char c : exp.toCharArray()) {
            if (c == ' ') continue;
            String action;

            if (Character.isLetterOrDigit(c)) {
                out.append(c);
                action = "Add to output";

            } else if (c == '(') {
                stack.push(c);
                action = "Push to stack";

            } else if (c == ')') {
                List<Character> popped = new ArrayList<>();
                while (!stack.isEmpty() && stack.peek() != '(') {
                    char op = stack.pop();
                    popped.add(op);
                    out.append(op);
                }
                stack.pop(); // remove '('
                action = popped.isEmpty() ? "Remove (" : "Pop " + join(popped) + ", then remove (";

            } else { // operator
                List<Character> popped = new ArrayList<>();
                while (!stack.isEmpty() && isOperator(stack.peek())) {
                    int top = precedence(stack.peek());
                    int cur = precedence(c);
                    boolean shouldPop = forPrefix ? top > cur : top >= cur;
                    if (!shouldPop) break;
                    char op = stack.pop();
                    popped.add(op);
                    out.append(op);
                }
                boolean hasOperatorBelow = !stack.isEmpty() && isOperator(stack.peek());
                stack.push(c);

                if (!popped.isEmpty()) {
                    action = "Pop " + join(popped) + ", then push " + c;
                } else if (hasOperatorBelow) {
                    action = "Push because " + c + " has higher precedence";
                } else {
                    action = "Push to stack";
                }
            }

            printRow(String.valueOf(c), action, stack, out);
        }

        List<Character> rest = new ArrayList<>();
        while (!stack.isEmpty()) {
            char op = stack.pop();
            rest.add(op);
            out.append(op);
        }
        printRow("End", rest.isEmpty() ? "Nothing left to pop" : "Pop " + join(rest), stack, out);

        return out.toString();
    }

    static String reverseAndSwap(String exp) {
        StringBuilder rev = new StringBuilder();
        for (int i = exp.length() - 1; i >= 0; i--) {
            char c = exp.charAt(i);
            if (c == '(') rev.append(')');
            else if (c == ')') rev.append('(');
            else rev.append(c);
        }
        return rev.toString();
    }

    public static void main(String[] args) {
        String infix = "((A+(B*(C/A)))-(B/C))";

        System.out.println("Infix: " + infix);
        System.out.println("\nConversion to Postfix Notation\n");
        String postfix = convert(infix, false);
        System.out.println("Postfix: " + postfix);
        System.out.println("\nConversion to Prefix Notation\n");
        String reversed = reverseAndSwap(infix);
        System.out.println("Reversed expression (parentheses swapped): " + reversed);
        String temp = convert(reversed, true);
        String prefix = new StringBuilder(temp).reverse().toString();
        System.out.println("Reverse the output: " + temp + " -> " + prefix);
        System.out.println("Prefix: " + prefix);
    }
}

