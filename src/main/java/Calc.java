import java.util.HashMap;
import java.util.Map;
import java.util.function.IntBinaryOperator;

public class Calc {

    /*
     * IntBinaryOperator에 덧셈 나눗셈 곱셈 세가지 연산 정하고
     * 연산자랑 map에 묶어놓고
     */

    public static int run(String formula) {
        Map<String, IntBinaryOperator> operManager
                = new HashMap<>();
        operManager.put("+", Integer::sum);
        operManager.put("-", (a, b) -> (a - b));
        operManager.put("*", (a, b) -> (a * b));

        String[] opers = formula.split(" ");
        int a = Integer.parseInt(opers[0]);
        int b = Integer.parseInt(opers[2]);

        IntBinaryOperator operation = operManager.get(opers[1]);

        return operation.applyAsInt(a, b);
    }
}
