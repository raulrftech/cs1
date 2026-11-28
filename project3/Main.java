import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final int DEFAULT_STACK_DEPTH = 10;
    public static final String INPUT_FILE = "commands.txt";
    public static final String OUTPUT_FILE = "execution.txt";

    public static void main(String[] args) {
        if (args.length == 0) {
            runFromConsole();
        } else if (args.length == 1 && args[0].equals("--file")) {
            runFromFile();
        } else {
            System.err.println("Usage: java Main [--file]");
        }
    }

    private static void runFromConsole() {
        List<String> instructions = new ArrayList<>();
        Scanner console = new Scanner(System.in);
        while (console.hasNextLine()) {
            String line = console.nextLine();
            if (line.trim().equals("END")) {
                break;
            }
            instructions.add(line);
        }
        
        // Milestone 1: Uncomment the engine calls and remove the command echo
        // once execute can process valid commands.
        ExecutionEngine engine = new ExecutionEngine(DEFAULT_STACK_DEPTH);
        printTrace(engine.execute(instructions));
    }

    private static void runFromFile() {
        // Milestone 5: Read the commands, run the engine, and save the trace.
        System.err.println("TODO: Implement file mode in Milestone 5.");
    }

    private static void printTrace(List<String> trace) {
        for (String line : trace) {
            System.out.println(line);
        }
    }
}