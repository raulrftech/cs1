public class project3 {
    public static void main(String[] args) {
        CallStack cs = new CallStack(3);
        StackFrame sf1 = new StackFrame("main", new String[] {});
        StackFrame sf2 = new StackFrame("add", new String[] { "5", "7" });
        
        System.out.println(cs.size()); System.out.println(cs.isEmpty()); System.out.println(cs.peek()); cs.push(sf1); cs.push(sf2);
        System.out.println(cs.describe());
    }
}