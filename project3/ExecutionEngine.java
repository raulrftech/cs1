import java.util.ArrayList;
import java.util.List;

public class ExecutionEngine {
    // Milestone 1: Uncomment this field after creating CallStack.java.
    // private CallStack stack;
    private boolean purgeOldest = false;

    public ExecutionEngine(int maxDepth) {
        // Milestone 1: Create the stack with the supplied capacity.
        // stack = new CallStack(maxDepth);
    }

    public List<String> execute(List<String> instructions) {
        List<String> trace = new ArrayList<>();

        // Milestone 1: Process valid commands in order.
        // - Skip blank lines and lines whose trimmed text starts with #.
        // - Log "OVERFLOW POLICY: STRICT" for SET_OVERFLOW_POLICY STRICT.
        // - CALL method or CALL method(arg1, arg2): push a frame and log "CALL "
        //   followed by its display name. Trim each argument. Argument lists are
        //   well formed, arguments may contain spaces, and () means no arguments.
        // - RETURN [value]: pop a frame and log "RETURN " followed by its display
        //   name. If the input supplies a one-token value, append " => value".
        // - PRINT_STACK: add stack.describe() to the trace.

        // Milestone 3: Collect the trace and handle errors.
        // - Add output lines to trace in order and return the list.
        // - Catch StackOverflowException and StackUnderflowException. Add
        //   "ERROR: " followed by the exception message and stop processing.
        // - For a malformed command, add "ERROR: Invalid instruction: <line>"
        //   and stop processing.

        // Milestone 4: Configure the stack and apply the overflow policy.
        // - If present, SET_MAX_STACK_DEPTH n comes first. Create a stack with
        //   the supplied positive capacity and log "MAX STACK DEPTH: n".
        // - Apply the supplied SET_OVERFLOW_POLICY and log "OVERFLOW POLICY: <policy>".
        //   The input supplies one valid policy command before stack operations.
        // - In STRICT mode, let push throw on overflow. The failed push leaves
        //   the stack unchanged.
        // - In PURGE_OLDEST mode, remove the bottom frame before pushing onto a
        //   full stack. Log "STACK FULL: Purging oldest frame: <name>" before the CALL.
        throw new UnsupportedOperationException("TODO: execute");
    }
}