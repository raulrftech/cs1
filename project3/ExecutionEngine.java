import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.print.attribute.HashAttributeSet;

public class ExecutionEngine {
    // Milestone 1: Uncomment this field after creating CallStack.java.
    private CallStack stack;
    private boolean purgeOldest = false;

    public ExecutionEngine(int maxDepth) {
        // Milestone 1: Create the stack with the supplied capacity.
        stack = new CallStack(maxDepth);
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
        
        // execute receives a list of text lines which are the lines which are the lines of commands.txt
            // For each line it does two things, it changes the stack if the command calls for it and it adds one line of text to a list called trace
            // At the end it returns that trace list
        // Heres the specs worked example, one row per input line
        //  Input Line                      What engine does to stack                           Trace line it adds
        //  Set_OVERFLOW_POLICY STRICT      nothing yet                                         OVERRFLOW POLICY: STRICT
        //  CALL main                       makes a main frame and pushes it                    CALL main
        //  CALL add(5, 7)                  makes an add frame with args 5 and 6 and pushes     CALL add(5, 7)
        //  PRINT_STACK                     nothing                                             TOP -> add(5, 7) -> main -> null
        //  RETURN 12                       pops the top frame                                  RETURN add(5,7) => 12
        //  PRINT_STACK                      nothing                                             TOP -> main -> null
        //  RETURN                          pops the top frame                                  RETURN main
        // Every line goes through the same steps, first clean it up because the file indents so spaces at the ends have to be trimmed
            // then skip it if its blank or starts with a # otherwise work out which command it is and do the row from the table
        // The table shows what each line has to do and building it in code is your work
        // Start with the first part
        // Before the engine decides which command a line is, what does it have to do to each line to get ready?
            // Think about going through the list and cleaning each line, and about which lines dont count at all
        // TODO: REMMINDER
            // for each line in the instructions, it needs to be transformed into an array and then the command is the first index of this array
            // such as RETURN, CALL, PRINT_STACK
        for (int i = 0; i < instructions.size(); i++) {
            String s = instructions.get(i).trim();
            s = s.replaceFirst(" ", "#");
            String[] split = s.split("#");
            //System.out.println(Arrays.toString(split));

            switch (split[0]) {
                case "SET_OVERFLOW_POLICY":
                    split[1] = split[1].trim();

                    
                    break;
                case "SET_MAX_STACK_DEPTH":
                    split[1] = split[1].trim();
                    break;
                case "CALL":
                    String[] method = split[1].trim().split("[()]");
                    String methodName = method[0];
                    String[] methodArgs = method.length > 1 ? Arrays.copyOfRange(method, 1, method.length)[0].split("\\s*,\\s*") : new String[] {};
                    
                    for (int j = 0; j < methodArgs.length; j++) { methodArgs[j] = methodArgs[j].trim(); }

                    StackFrame sf = new StackFrame(methodName, methodArgs);
                    stack.push(sf);
                    String toTrace = String.format("%s %s", split[0], sf.displayName());
                    trace.add(toTrace);

                    System.out.println(String.format("%s%n%s -- Args Length: %d --- toTrace: [ %s ]", Arrays.toString(method), Arrays.toString(methodArgs), methodArgs.length, toTrace));
                    break;
                case "RETURN":
                    split[1] = split[1].trim();
                    break;
                case "PRINT_STACK":
                    trace.add(stack.describe());
                    break;
                default:
                    break;
            }
        }


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