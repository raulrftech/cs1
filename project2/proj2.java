import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class proj2 {
    public static void main(String[] args) {
        Scanner globalScanner = new Scanner(System.in);
        Random rand = new Random();

        
    }
}

interface Password {
    default boolean createPassword(String attempt) {
        if (attempt.length() != 10) { System.out.println("Your password must be exactly 10 characters long that follow \n4 digits\n3 Uppercase letters\n3 lowercase letters"); }
        
        int numberCount = 0; int lcCount = 0; int ucCount = 0;
        for (int i = 0; i < attempt.length(); i++) {
            char c = attempt.charAt(i);
            if (Character.isDigit(c)) { numberCount++; }
            if (Character.isUpperCase(c))  { ucCount++; }
            if (Character.isLowerCase(c)) { lcCount++; }
        }
        return numberCount == 4 && lcCount == 3 && ucCount == 3;
    }
    
    default boolean confirmPassword(String made, String confirmation) { return made.equals(confirmation); }
}

interface TwoFactorAuthentication {
    default String generate2FACode(Random rand) {
        
        StringBuilder code = new StringBuilder(8);

        int runningTotal = 0;
        // format: U L N L U N U N
        char firstUpper = (char) (rand.nextInt(26) + 65); code.append(firstUpper); runningTotal += (int) firstUpper;
        char firstLower = (char) (rand.nextInt(26) + 97); code.append(firstLower); runningTotal += (int) firstLower;
        char firstNumber = (char) (rand.nextInt(10) + 48); code.append(firstNumber); runningTotal += (int) firstNumber;
        char secondLower = (char) (rand.nextInt(26) + 97); code.append(secondLower); runningTotal += (int) secondLower;
        char secondUpper = (char) (rand.nextInt(26) + 65); code.append(secondUpper); runningTotal += (int) secondUpper;
        char secondNumber = (char) (rand.nextInt(10) + 48); code.append(secondNumber); runningTotal += (int) secondNumber;
        char thirdUpper = (char) (rand.nextInt(26) + 65); code.append(thirdUpper); runningTotal += (int) thirdUpper;
        char lastChar = (char) (rand.nextInt(10) + 48); code.append(lastChar); runningTotal += (int) lastChar;

        return (runningTotal == 560) ? code.toString() : generate2FACode(rand);
    }
    default boolean verified2FA(Scanner sc, String retrieved2FA, int attempts) {
        String verified = "";
        while (attempts > 0 && !verified.equals(retrieved2FA)) {
            System.out.println(String.format("You have %d attempts remaining.", attempts));
            verified = sc.nextLine().trim();
            if (!verified.equals(retrieved2FA)) { attempts--; }
        }
        return (attempts > 0) && verified.equals(retrieved2FA);
    }
}

class FileHolder implements Password, TwoFactorAuthentication {
    String name; String username; String password; Boolean isLoggedIn;
    FileSystemManager fsm;

    private FileHolder(String name, String username) {
        this.name = name;
        this.username = username;
        this.password = null;
        this.isLoggedIn = false;
        this.fsm = null;
    }
    public static FileHolder createHolder(Scanner userInfo, Random rand) {
        String name = "";
        String username = "";

        System.out.println("Please enter your name, this will be used alongside your username within the command line");
        while (name.trim().isEmpty()) {
            name = userInfo.nextLine();
        }
        System.out.println("Please enter a username, this will be used alognside your name within the command line");
        while (username.trim().isEmpty()) {
            username = userInfo.nextLine();
        }
        FileHolder fh = new FileHolder(name, username);

        String validPassword = "";
        while (!fh.createPassword(validPassword)) {
            System.out.println("Please create a password consisting of 4 numbers, 3 Uppercase letters and 3 lowercase letters");
            validPassword = userInfo.nextLine().trim();
        }
        fh.password = validPassword;

        System.out.println("Please confirm your password. You only have one try or else you're going to have to restart");
        if (!fh.confirmPassword(fh.password, userInfo.nextLine().trim())) {
            System.out.println("You entered the password incorrectly. Please start over.");
            fh = null; return null; 
        }

        String retrieved2FA = fh.generate2FACode(rand);
        System.out.println(String.format("You are going to receive a verification code, please enter it exactly as it appears.%n%s", retrieved2FA));
        if (!fh.verified2FA(userInfo, retrieved2FA, 4)) {
            System.out.println("You failed verifying the 2FA, you are going to have to start over.");
            fh = null; return null;
        }

        return fh;
    }

    public String passwordCreation() {
        return null; 
    }

    @Override
    public String toString() {
        return String.format("Name: %s%nUsername: %s%nPassword: ****%s%nDisplay: %s@%s", this.name, this.username, this.password.substring(this.password.length() - 4), this.username, this.name);
    }
}

interface Fix_FileName {
    default String fixFileName(String name, int i) {
        if (i > name.length() - 1) { return ""; }
        String corrected = "";
        return name.charAt(i) != '/' && name.charAt(i) != '\\' && name.charAt(i) != ' ' ? (corrected + name.charAt(i) + fixFileName(name, i + 1)) : fixFileName(name, i + 1);
    }
}

class Folder extends ContainingType implements Fix_FileName {
    TreeMap<Integer, ContainingType> contents = new TreeMap<>();
    private TreeMap<String, ContainingType> names = new TreeMap<>();
    public void getContentsSummary() { contents.forEach((id, instance) -> System.out.println(String.format("ContainingType: %s%nID: %d%nName: %s%nParent: %s%nSize: %d%n", instance.getClass().getSimpleName(), id, instance.name, instance.parent.name, instance.getSize(instance)))); }

    public Folder(String name, Folder parent) {
        super(name, parent);
    }

    public boolean addToContents(ContainingType appending) {
        // this is case-sensitive already since different letters have different Unix values
        // chek for spaces, \ or / in name
        String fixedName = fixFileName(appending.name, 0);
        if (fixedName.isEmpty()) { return false; } else { appending.name = fixedName; }
        if (names.containsKey(appending.name)) { return false; }
        if (appending.name.charAt(0) == '.') { return false; }
        // . occurence; no more than 1, check if folder, if so, it cannot have a period
        int periodCounter = 0;
        for (char c: appending.name.toCharArray()) { if (c=='.') {
            periodCounter++;
            if (appending.getClass().getSimpleName() == "Folder") { return false; } else {
                if (periodCounter > 1 ) { return false; } else { continue; }
            }
        }}
        names.put(appending.name, appending);
        contents.putIfAbsent(appending.id, appending); appending.parent = this; return true;
        // TODO: if exists in another folder already, prompt to move, refer to line 146-148 on brainstorming.txt
    }
}
class File extends ContainingType {
    String body; // TODO: implement history record
    String type;
    public File(String name, Folder parent) {
        super(name, parent);
        this.body = "";
        this.type = name.substring(name.indexOf(".") >= 0 ? name.indexOf(".") + 1 : 0);
    }
}

abstract class UUID_Config {
    static ArrayList<Integer> used_UUIDS = new ArrayList<>();

    int getUUID() {
        Random rand = new Random();
        StringBuilder uuid_String = new StringBuilder(10);

        // 1st Position
        uuid_String.append(1); 
        // 2nd and 3rd Position
        int twoDigit = rand.nextInt(100);
        if (twoDigit < 10) { uuid_String.append(0); uuid_String.append(twoDigit); } else { uuid_String.append(twoDigit); }
        // 4th Position
        uuid_String.append(0);
        // 5th to 7th Position
        int threeDigit = 1;
        while (threeDigit % 7 != 0 || threeDigit == 0) { threeDigit = rand.nextInt(887); }
        if (threeDigit < 10) {
            uuid_String.append(0); uuid_String.append(0); uuid_String.append(threeDigit);
        } else if (threeDigit < 100) {
            uuid_String.append(0); uuid_String.append(threeDigit);
        } else { uuid_String.append(threeDigit); }
        // 8th Position
        uuid_String.append(4);
        // Last 2 Positions
        int lastTwo = 1;
        while (lastTwo % 8 != 0 || lastTwo == 0) { lastTwo = rand.nextInt(100); }
        if (lastTwo < 10) { uuid_String.append(0); uuid_String.append(lastTwo); } else { uuid_String.append(lastTwo); }

        int newUUID = Integer.parseInt(uuid_String.toString());
        if (used_UUIDS.contains(newUUID)) { return getUUID(); } else { used_UUIDS.add(newUUID); return newUUID; }
        // There is a maximum of 151,200 unique UUIDs
    }
}

abstract class ContainingType extends UUID_Config {
    String name; Folder parent; Double size; int id;
    public ContainingType(String name, Folder parent) { this.name = name; this.parent = parent; this.size = 0.00; this.id = getUUID(); }

    // get size, recursion if folder, if file checks if body has anything, if not then 0, otherwise, strip then count with .length()
    public int getSize(ContainingType self) {
        int[] accumulatedSize = {0};

        if (self.getClass().getSimpleName() == "Folder") {
            Folder casted = (Folder) self;
            for (Map.Entry<Integer, ContainingType> pair: casted.contents.entrySet()) {
                accumulatedSize[0] += getSize(pair.getValue());
            }
        } else {
            // TODO: look at required implementation of Disk in proj desc
            File casted = (File) self;
            Arrays.asList(casted.body.split(" ")).forEach((String s) -> accumulatedSize[0] += s.length());
        }
        return accumulatedSize[0];
    }


}

interface Command {
    // returns a String, the text showing how to type the command
    String usage();
    // returns a String, short line saying what the command does
    String description();
    // returns boolean, whether that many arguments is valid for this command
    boolean accepts(int argumentCount);
    // does the work
    void execute(String[] arguments);
}

class Move implements Command {
    Scanner fsm_Scanner;
    public Move(Scanner fsm_Scanner) { this.fsm_Scanner = fsm_Scanner; }


    @Override
    public String usage() {

        return null;
    }

    @Override 
    public String description() {

        return null;
    }

    @Override
    public boolean accepts(int argumentCount) {

        return true;
    }

    @Override 
    public void execute(String[] arguments) {
        

    }
}


class FileSystemManager implements TwoFactorAuthentication {
    String name;
    public FileSystemManager(String name) { this.name = name; }

    public FileHolder createFileHolder(Scanner userCreator, Random rand) {
        // add to static hm of users
        FileHolder newUser = FileHolder.createHolder(userCreator, rand);
        if (newUser != null) { newUser.fsm = this; }
        return newUser;
    }


    public void login() {

    }

    public void logout() {

    }
}