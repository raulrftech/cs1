public class CallStack {
    private StackFrame head;
    private int size;
    private final int maxDepth;

    public CallStack(int maxDepth) {
        this.head = null;
        this.size = 0;
        this.maxDepth = maxDepth;
    }

    public void push(StackFrame frame) {
        // puts a frame on top
        frame.next = this.head;
        this.head = frame;
        this.size += 1;
    }

    public StackFrame pop() {
        // removes and returns the top frame
        StackFrame returning = this.head;
        this.head = this.head.next; this.size -= 1;
        returning.next = null; return returning;
    }

    public StackFrame peek() {
        // returns the top frame without removing it
        return this.head;
    }

    public boolean isEmpty() {
        // Whether there are no frames
        return this.size == 0;
    }
    
    public int size() {
        // the current number of frames
        return this.size;
    }

    public int capacity() {
        // the max depth
        return this.maxDepth;
    }

    public StackFrame get(int index) {
        // returns the frame at a position, with 0 being the top
        // TODO: the error message is off by one for non-empty stacks
        if (index < 0 || index > (size - 1)) { throw new IndexOutOfBoundsException(String.format("The desired index(%d) is out of bounds. There are only %d available indices.", index, this.size - 1 < 0 ? this.size : this.size - 1)); }
        if (index == 0) { return this.head; }
        StackFrame ph = this.head; int counter = 0;
        while (ph.next != null && counter != index) {
            ph = ph.next;
            counter += 1;
        }
        return ph;
    }

    public String describe() {
        // builds the TOP -> ... -> null test, using ll traversal, fuck O^2
        StringBuilder sb = new StringBuilder();
        StackFrame ph = this.head;
        sb.append("TOP -> ");
        while (ph != null) {
            sb.append(String.format("%s -> ", ph.displayName()));
            ph = ph.next;
        }
        sb.append("null");
        return sb.toString();
    }

    public StackFrame removeBottom() {
        // removes and returns the oldest frame, at the bototm
        return null;
    }
}