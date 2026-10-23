class recursionPracice {
    // This file is to introduce me to recursion, will span through the course of the weekend form 10/22 to 10/26
    public static void main(String[] args) {
        show(3);
    }
    
    // A revursive method solves a problem by calling itself on a smaller version of the same problem, until the problem is small enough to answer directly
    // The three parts every recursive method needs:
        // Base Case: the smallest input, answered directly with no further recursive call
        // Recursive Case: shrink the input, call the method on the smaller input and combine that result with your own piece
        // Progress: every call must move strictly closer to the base case
    // Example
    public static int sumTo(int n) {
        if (n == 0) { return 0; } // base case
        return n + sumTo(n - 1); // recursive case

        // How It Runs
            // Each call gets its own copy of its variables and sits on the call stack waiting. For sumTo(3):
                // sumTo(3) waits on 3 + sumTo(2)
                // sumTo(2) waits on 2 + sumTo(1)
                // sumTo(1) waits on 1 + sumTo(0)
                // sumTo(0) returns o, which starts the unwinding: 1+0=1 then 2+1=3 then 3+3=6
    }
    // The deepest call finishes first. The answer is built on the way back up not on the way down
    // Writing one
        // Assume the clal on the smaller input already returns the right answer and aks how to combine it with your piece. Dont trace the whole chain in your head while you write
    // Where it Fits
        // Recursion fits data that contains smaller copies of itself: folders within folders, nested JSON, trees, and merge sort's halves
        // Compilers, file tools and graph search all lean on it
        // For a flat sequence, a loop is isually simpler and does not grow the stack
    // The Classic Bugs
        // A missing or unreachable base case
            // sumTo(-1) never hits 0 so Java throws StackOverflowError
        // No progress such as calling sumTO(n) from inside sumTo(n)
        // Calling the method but throwing its return value away
        // Repeated work
            // Naive Fibo recomputes the same values over and over and its cost explodes
    
    // Your first step:
        // In your notebook, trace sumTo(4) call by call, in the same style as the sumTo(3) trace above and send what each call waits on and what each returns
        // sumTo(4) waits on 4 + sumTo(3)
        // sumTo(3) waits on 3 + sumTo(2)
        // sumTo(2) waits on 2 + sumTo(1)
        // sumTo(1) waits on 1 + sumTo(0)
        // sumTo(0) returns o, which starts the unwinding: 1+0=1 then 2+1=3 then 3+3=6 then 4+6=10
    // Next step, the difficulty is a small step up. The method now prints instead of returning a value and a void method's base case is just return
    public static void show(int n) {
        if (n == 0) { return; }
        System.out.println(String.format("Down %d", n));
        show(n-1);
        System.out.println(String.format("Up %d", n));

        // Before running antyhing, write down what show(3) prints, line by line and in order. Then say why the up lines come out in the order they do
            // show(3) would first print Down 3 then call again with 2 then print down 2 then call again with 1 and print down 1
            // Since nothing is being counted or tracked, the most recent n which is 1 would propagate to the last sysoutprntln so itll print up 1
        // Correction:
            // The wrong idea was that one n is shared and the most recent value wins
            // There is no shared n. Each call has its own separate n and the call stakc is what tracks them. Each paused call remembers its own n and the exact line it stopped at
            // after it hits the base case, control goes back to show(1) which resumes on the line right after its call. Since it still has n = 1 it print up 1 and finishes
                // same thing for the other cases
        
        // The rule is that code before the recursive call runs on the way down and code after it runs on the way back up and never in reverse order
            // sumTo(n) worked the same way, its n + waited until the inner call returned so the adding happened on the way back up
        
        // Next Step:
            // Draw the call stack at the moment show(0) is running. List every call that exists at that moment with its n and the line its paused on.
    }
}