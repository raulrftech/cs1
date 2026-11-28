import java.util.ArrayList;
import java.util.Arrays;

public class project3 {
    public static void main(String[] args) {
        CallStack cs = new CallStack(3);
        StackFrame sf1 = new StackFrame("main", new String[] {});
        StackFrame sf2 = new StackFrame("add", new String[] { "5", "7" });

        ExecutionEngine ee = new ExecutionEngine(5);
        ArrayList<String> sample = new ArrayList<>(Arrays.asList("SET_OVERFLOW_POLICY STRICT", "CALL main", "CALL add(5, 7)", "PRINT_STACK", "RETURN 12", "PRINT_STACK", "RETURN", "    ", "#", ""));
        System.out.println(ee.execute(sample));

    }
}