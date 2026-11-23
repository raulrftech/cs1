import java.util.Arrays;

public class StackFrame {
    public String methodName;
    public String[] arguments;
    public StackFrame next;

    public StackFrame(String methodName, String[] arguments) {
        if (methodName == null || methodName.isBlank()) { throw new IllegalArgumentException(methodName == null ? "Method name not provided" : "Method name is blank"); }
        this.methodName = methodName;
        this.arguments = arguments == null || arguments.length == 0 ? new String[] {} : Arrays.copyOf(arguments, arguments.length);
        this.next = null;
    }

    public String displayName() {
        if (this.arguments.length == 0) { return this.methodName; }
        String joined = String.join(", ", this.arguments);
        return String.format("%s(%s)", this.methodName, joined);
    }

    @Override
    public String toString() {
        return String.format("Name: %s%nArgs: %s%nNext: %s", this.methodName, Arrays.toString(this.arguments), this.next == null ? "Null" : this.next.methodName);
    }
}