import java.util.Arrays;
class recursionPracice {
    // This file is to introduce me to recursion, will span through the course of the weekend form 10/22 to 10/26
    public static void main(String[] args) {
        int[][] grid = {{-5, -3}, {-9, -4}};
        System.out.println(maxOf(grid, 0, 0));

        int[] nums = {6, -2, 5, 4, -1, 3};
        System.out.println(mystery(nums, 0));

        int[] nums = {3, 34, 4, 12, 5, 2};
        System.out.println(canMakeSum(nums, 0, 9));
        System.out.println(canMakeSum(nums, 0, 30));
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
                // whenever 0 is runnign it checks if n is zero and it is so it returns
                // the calls that exist at that moment are with n=1,2,3 respectively, paused right before the sysoutprntnln statement that prints out Up n
            // Refinements
                // show(0) is on the stack too, as the top call. At that moment there are 4 calss, show3 at the bottom to show0 at the top.
                    // The top call is the newest and it finishes first
                // Each of the three paused calls is stopped on its recursive call line (show(n-1)) and it resumes at the next lin.
    }

    // Next step:
        // Write recursion yourself instead of tracing
        // Brief:
            // Write a recursive void method printUp(int n) that prints 1 up to n, each on its own line, with exactly one println in the method
            // Before any code, send the plan in a few sentences: base vase, recursive case and what order things happen in
                // Base Case: if (n <= 1) { Sysoutprntln n}
                // Recursive Case: printUp(n - 1)
                // Order of Occurences:
                    //  print statements are after recursive call so it prints current n, whenever recursive call is called it
    public static void printUp(int n) { if (n < 1) { return; } printUp(n-1); System.out.println(n); }
    // A call stack adds a new call only on top and removes a call only from the top. The call that is running right now is always the one on top.
        // When it finishes, its removed and control goes back to the call directly beneath it
    // if the method were swapped
        // control would not return to the 0, whenever 0 finishes, control returns to the call beneath it which would be 1
        // nothing is passed back either since the method is void, so no value and no original n travels up
        // each call only ever had its own n
        // when 1 gets control back it has no lines left after the recursive call so it just ends
        // then each call prior it also end, thus the way back is silent since all the printing happened on the way down
    // THe rule is:
        // code before the recurisve call runs on the way down and code after it runs on the way back up
            // THis is also why this method counted up and the swapped one counts down

    // Exercise 1:
        // Write a recursive method sumDigits(int n) that returns the sum of the digits of a non-negative integer, so sumDigits(1234) returns 10
        // Before writing code; plan:
            // Base case
            // Recursive case
            // How each moves toward the base case
    public static int sumDigits(int n) {
        // base case
        if (n < 10) { return n; }
        // recursive case
        return (n % 10) + sumDigits(n / 10);
    }
    // Each call handles one small peice and it trusts the recursive call to handle everything else correctly
    // You do not trace the whole chain while writing it, you only decide how this calls piece combines with what comes back

    // Exercise 2
    // Write a recursive method isPalindrome(String s) that returns true if s reads the same forward and backward so racecar is true and abca is false
    // Assume lowercase letters only, no loops and no reversing the string
    // You may use length, charAt and substring
    // Plan First:
        // base cases
        // recursive case
        // how each call moves toward a base case
        // inputs testing
    public static boolean isPalindrome(String s) {
        if (s == null || s.isEmpty() || s.length() < 2) { return true; }
        if (s.charAt(0) == s.charAt(s.length() - 1)) { return isPalindrome(s.substring(s.length() - (s.length() - 1), s.length() - 1)); } else { return false; }
        // When a method returns a value, that value takes the place of the call, right where the call was written
        // In the sumDIgits, the call sat inside an addition so the value that came back was used in the sum
        // The same thing happens with int x = Math.max(3, 4) which stores the 4
            // If you write Math.max(3, 4); alone ona line, the 4 comes back and nothing uses it so Java throws it away
            // It inst an error, a returned value is only kept if the call is used somewhere: returned, stored, tested or combined with something else
        // The caller does not check what it originally returned
                // A call hasnt returned anything until it runs a return instruction
                // THe caller is not comparing true against false or converting anything
                // It just runs its next instruction
                // In the fixed version that instruction is ireturn whihc takes whatever is on top fo the operand stack and returns exactly that unchanged
                // The value reaches the first call unchanged so a flase comes out as the same false
        // THere was something on the operand stack
            // In the first version of this, the false (the 0) did arrive on abca's operand stack, the next instruction was pop which removed it
            // Only after that did the call continue with goto 59, iconst_1 and ireturn which is the original return true solo line
            // So it wasnt nothing ot handle, the value was indeed there and the next instruction just threw it away since nothing handled it
    }

    // Exercise 3
    // An array cannot be cut with substring so progress has to be tracked a different way
        // Thats the idea of a seocnd piece of info that I wanted in my first printUp plan and this exercise needs it
    // Write a recursive method countEven(int[] nums, int i) that returns how many even numbers are in nums from index i to the end
    // countEven(nums, 0) counts the whole array.
    // Do not use loops
    // Plan First:
        // Base Case
        // Recursive cas
        // How each call moves toward the base case and inputs to test
    public static int countEven(int[] nums, int i) {
        if (nums.length == 0 || nums.length <= i) { return 0; }
        if (nums[i] % 2 == 0) {return (i - (i - 1)) + countEven(nums, ++i); } else { return countEven(nums, ++i);}
        // Information goes down in the arguments: nums and the next i tell the next call where to look
        // Information comes back up in the return value: the coutn so far
        // The operand stack is how the JVM moves both and the off case isnt nothing, that call adds 0 of its own but it still returns whatever came back from below
    }

    // Exercise 4
    // Progress now comes from halving a window defined by two indexes, instead of moving one step.
    // Write a recursive method contains(int[] sorted, int target, int lo, int hi) that returns true if target appears in sorted between index lo and hi, inclusive
    // The array is sorted ascending and contains(nums, t, 0, nums.length -1) searches the whole array
    // No loops and each call must discard half of the remaining window rather than scanning one element at a time
    // Plan First:
        // Base Case or Cases
            // this would be if lo == target or hi == target or if target < lo and lo is currently at index 0 and also checking if mid is target as well
        // Recursive Cases
            // these are going to be moving towards lo or towards high
        // How the window shrinks each call and why it always ends
            // a cut will either be towards one greater than mid or one lesser than mid
                // high would become mid - 1 if target is lesser than mid and lo would become mid + 1 if mid is greater
            // this always ends because it either cuts towards the left and right such as cutting to left would cut whole array into 0.5, to the right/left next would be 0.25, next 0.125 etc
        // Inputs you would test
            //
    public static boolean contains(int[] sorted, int target, int low, int high) {
        if ( high < low || sorted[low] > target || sorted[high] < target) { return false; }

        int mid = (low + high) / 2; // low:0, high = 9 -> 9 / 2 = 4
        if (sorted[low] == target || sorted[mid] == target || sorted[high] == target) { return true; }
        
        if (target < sorted[mid]) { return contains(sorted, target, low, mid - 1); } else { return contains(sorted, target, mid + 1, high); }
        // After k halvings, n(0.5^k) elements remain and the search is done when that drops to about 1
        // This gives k - log2(n), rounded up
        // For the 205 indices array, log2 is 7.68 so 8 calls and for 1 million it would be 20 where basic iteration would need a max of size n.
    }

    // For every new recursive method, ask these before running anything
        // What is the smallest input (empty, zero, one element) and does the method handle it?
        // Does every call move toward a base case?
        // Which inputs take different paths: found, absent, first, last?

    // Exercise 5
    // The progress now runs in two dimensions so the column wraps to the next row
    // The base case also simply cannot return 0
    // Write a recursive method maxOf(int[][] grid, int row, int col) that returns the largest value forom cell (row, col) to the end of the grid, reading left to right, row by row
    // maxOf(grid, 0, 0) covers the whole grid
    // Every row has the same length and the grid has at least one cell
    // Do not use loops
    // Plan First
        // Base Case or Cases
            // since this has to deal with size inputs they have to suffic the grids size
        // Recursive Cases
        // Inputs you would test
    public static int maxOf(int[][] grid, int row, int col) {
        if (row >= grid.length || row < 0 || col < 0 || col > grid[row].length - 1) { return Integer.MIN_VALUE; }

        return Math.max(grid[row][col], maxOf(grid, (col + 1) > grid[row].length - 1  ? row + 1 : row, (col + 1) > grid[row].length - 1 ? 0 : col + 1));
    }

    // Exercise 6 - Starting Traces
    // Difficulty goes down from previous exercise, one array, one recursive call, no ternaries.
    // The new part is that you trace it with nothing running. Here is the method's body
    public static int mystery(int[] a, int i) {
        if (i >= a.length) { return 0; }
        if (a[i] < 0) { return mystery(a, i + 1); }
        return a[i] - mystery(a, i + 1);
    }
    // Trace it and do three things:
        // Using the array of {6, -2, 5, 4, -1, 3} and i=0
        // Note the calls going down (i and a[i] for each)
            // 6 > 0 so it returns 6 - mystery(a, 1) -> mystery(a, 2) -> 5 - mystery(a, 3) -> 4 - mystery(a, 4) -> mystery(a, 5) -> 3 - mystery(a, 6)
        // The value each call returns on the way back up
            // 6 - 5 - 4 - 3 - 0
        // The final printed number
            // 2
    
    // Exercise 7: cold trace, difficulty goes up slightly from previous exercise, the shape is the same, one array and one recursive call but this method prints before and after the call and keeps a local variable in each call
    // Method body:
    public static int mystery2(int[] a, int i) {
        if (i >= a.length) { return 0; }
        System.out.println("down " + a[i]);
        int rest = mystery2(a, i + 1);
        System.out.println("up " + a[i] + " rest " + rest);
        return rest * 2 + a[i];
    }
    // Trace it and do the same things
    // Using the array {3, 1, 4} and i=0
    // Note the calls going down
        // print donw 3
        // rest = mystery(a, 1)
            // print down 1
            // rest = mystery(a, 2)
                // print down 4
                // rest = mystery(a, 3)
                    // return 0
                    // print up 4 rest 0
                    // return 4
                // print up 1 rest 4
                // return 9
            // print up 3 rest 9
            // return 21
    // The final printed number
        // 21

    // Exercise 8, difficulty going up from previous
    // A value now travels both ways: acc is passed down as an argument and changes on the way and r comes back up as a return value
    // Method body:
    public static int mystery3(int[] a, int i, int acc) {
        if (i >= a.length) { return acc; }
        System.out.println("down " + i + " acc " + acc);
        int r = mystery3(a, i + 1, acc + a[i]);
        System.out.println("up " + i + " r " + r);
        return r + a[i];
    }
    // Trace it and do the same things
    // Using the array { 2, 5, 1} and i=0 and acc=0
    // Note the calls going down
        // print down 0 acc 0
        // r = mystery(a, 1, 2)
            // print down 1 acc 2
            // r = mystery(a, 2, 7)
                // print down 2 acc 7
                // r = mystery(a, 3, 8)
                // return 8
            // print up 2 r 8
            // return 8
        // print up 1 r 9
        // return 14
        // print up 0 r 14
        // return 14 + 2 = 16
    // The final printed number
        // 16

    // Exercise 9, difficulty rises noticeably, this is a different shape from the single chain of calls so far
    // Arrays only, no loops and nothing is modified
    // Write canMakeSum(int[] nums, int i, int target)
    // It returns true if some choice of elements from nums[i] to the end, each used at most once, adds up to target
    // All elements are positive, target is at least 0 and a target of 0 counts as true
    // canMakeSum(nums, 0, target) covers the whole array
    public static boolean canMakeSum(int[] nums, int i, int target) {
        // will implement solution following acc setup in proj2

    }
}