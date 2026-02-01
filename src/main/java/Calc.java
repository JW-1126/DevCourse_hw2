import java.util.function.IntBinaryOperator;

public class Calc {

    public static int run(String formula) {

        //괄호 제거 : 단순 formula.substring방식은 불가 -> 남아있는 괄호들이 이후 depth연산에 오류 발생
        if (isWrappedByParenthesis(formula)) {
            return run(formula.substring(1, formula.length() - 1));
        }

        // base case : 숫자인 경우 그대로 return
        if (isNumber(formula)) {
            return Integer.parseInt(formula);
        }

        // 상위 연산자 탐색
        int opIndex = findOperatorIndex(formula);
        // 단항 연산자 처리 : 상위 연산자가 존재하지 않는 경우 해당 항의 부호 반대로 return
        if (opIndex == -1 && formula.startsWith("-")) {
            return -run(formula.substring(1));
        }
        // 상위 연산자 존재 시 기준으로 좌우 분할
        String left = formula.substring(0, opIndex).trim();
        String right = formula.substring(opIndex + 1).trim();
        String operator = String.valueOf(formula.charAt(opIndex));

        // Divide & Conquer
        int leftVal = run(left);
        int rightVal = run(right);
        return function(operator).applyAsInt(leftVal, rightVal);
    }

    public static IntBinaryOperator function(String operator) {
        if (operator.equals("+")) {
            return Integer::sum;
        }
        if (operator.equals("-")) {
            return (a, b) -> a - b;
        }
        return (a, b) -> a * b;
    }

    private static boolean isNumber(String formula) {
        return formula.matches("\\d+");
    }

    // 식 전체가 유일 괄호로 싸여져 있는지 검사
    private static boolean isWrappedByParenthesis(String s) {
        if (!s.startsWith("(") || !s.endsWith(")")) {
            return false;
        }

        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                depth++;
            }
            if (c == ')') {
                depth--;
            }
            // 깊이가 0 즉 괄호가 여닫혔을 때 마지막 인덱스가 아니라면 내부에 괄호가 더 존재한다는 뜻
            if (depth == 0 && i < s.length() - 1) {
                return false;
            }
        }

        return true;
    }

    // 상위 연산자의 index return
    private static int findOperatorIndex(String s) {
        int multiplyIndex = -1;
        int depth = 0;
        // 문자열 역순 탐색 ( 좌 -> 우 연산 순서 고려 )
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == ')') {
                depth++;
            }
            if (c == '(') {
                depth--;
            }
            // 괄호 외부에 존재하는 연산자들에 대해
            // +, - 의 경우 즉시 return ( 연산자 우선 순위 고려 )
            if (depth == 0 && (c == '+' || (c == '-' && s.charAt(i + 1) == ' '))) {
                return i;
            }
            // * 의 경우 index 보관
            if (depth == 0 && c == '*') {
                multiplyIndex = i;
            }
        }
        // +, - 상위 연산자가 존재하지 않는 경우 * index return
        return multiplyIndex;
    }

}
