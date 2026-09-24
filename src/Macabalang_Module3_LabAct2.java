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

    static String toPostfix(String exp, boolean forPrefix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder out = new StringBuilder();

        for (char c : exp.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                out.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    out.append(stack.pop());
                }
                stack.pop(); // remove '('
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && isOperator(stack.peek())) {
                    int top = precedence(stack.peek());
                    int cur = precedence(c);
                    boolean shouldPop = forPrefix ? top > cur : top >= cur;
                    if (!shouldPop) break;
                    out.append(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            out.append(stack.pop());
        }
        return out.toString();
    }

    static String toPrefix(String exp) {
        StringBuilder rev = new StringBuilder();
        for (int i = exp.length() - 1; i >= 0; i--) {
            char c = exp.charAt(i);
            if (c == '(') rev.append(')');
            else if (c == ')') rev.append('(');
            else rev.append(c);
        }
        String postfix = toPostfix(rev.toString(), true);
        return new StringBuilder(postfix).reverse().toString();
    }

    public static void main(String[] args) {
        String infix = "((A+(B*(C/A)))-(B/C))";

        System.out.println("Expression: " + infix);
        System.out.println("Postfix: " + toPostfix(infix, false));
        System.out.println("Prefix:  " + toPrefix(infix));
    }
}
