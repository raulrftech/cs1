import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class proj2 {
    public static void main(String[] args) {
        Scanner globalScanner = new Scanner(System.in);
        Random rand = new Random();

        Folder main = new Folder("main", null);
        File notes = new File("notes.txt", main);
        File todo = new File("todo.txt", main);
        File data = new File("data.csv", main);
        File readme = new File("readme.md", main);
        Folder docs = new Folder("docs", main);
        Folder images = new Folder("images", main);
        Folder code = new Folder("code", main);

        File report = new File("report.txt", docs);
        File summary = new File("summary.txt", docs);
        Folder drafts = new Folder("drafts", docs);

        File captions = new File("captions.txt", images);
        File credits = new File("credits.txt", images);
        Folder thumbnails = new Folder("thumbnails", images);

        File app = new File("app.java", code);
        File util = new File("util.java", code);
        Folder tests = new Folder("tests", code);

        notes.body = "buy milk and eggs from the store then pick up the package at the post office before five and call the dentist to move the appointment to next week";
        todo.body = "finish the disk class tomorrow morning then write the commands for ls and cd and test every command with the sample folders before the check in meeting";
        data.body = "id name score 1 ana 90 2 ben 85 3 carla 77 4 dev 92 5 elena 88 6 felix 69 7 gina 95 8 hugo 81";
        readme.body = "this is the main folder of the simulator and it holds three folders named docs images and code plus a few loose files used for testing";
        report.body = "the report explains how the file system stores folders in a tree where every folder keeps its own map of names and each file keeps its text in a body";
        summary.body = "in short a folder can hold files and other folders and the size of a folder is the total size of everything inside it";
        captions.body = "a quiet street at sunrise with long shadows and a lone bicycle leaning against a blue wall near the corner of the old market";
        credits.body = "photos by the class members and edited on a laptop with free tools during the long weekend before the project was due";
        app.body = "start the program then read a line from the user and hand it to the file system manager until the user types exit";
        util.body = "helper methods live here such as one that cleans a name and one that checks that a size is a whole number that is not negative";

        main.addToContents(notes);
        main.addToContents(todo);
        main.addToContents(data);
        main.addToContents(readme);
        main.addToContents(docs);
        main.addToContents(images);
        main.addToContents(code);

        docs.addToContents(report);
        docs.addToContents(summary);
        docs.addToContents(drafts);

        images.addToContents(captions);
        images.addToContents(credits);
        images.addToContents(thumbnails);

        code.addToContents(app);
        code.addToContents(util);
        code.addToContents(tests);

        main.getContentsSummary(true, true);
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
    public void getContentsSummary(boolean isLS,  boolean isR) {
        HashMap<String, Folder> lsR_Folders = new HashMap<>();

        /* TODO: ls -R: (1) give every folder, including the starting one,
            a heading with its full path; (2) list each folder's subfolders as entries with sizes,
            not only as later headings; (3) replace the getSimpleName() == "Folder" check, also in getSize and addToContents.
        */
        if (!isLS) {
            contents.forEach((id, instance) -> System.out.println(String.format("ContainingType: %s%nID: %d%nName: %s%nParent: %s%nSize: %.2f%n", instance.getClass().getSimpleName(), id, instance.name, instance.parent.name, instance.getSize(instance)))); 
        } else {
            if (!isR) { contents.forEach((id, instance) -> System.out.println(String.format("%s(%.2f)", instance.name, instance.getSize(instance)))); } else {
                // this needs to recurse each folder within
                contents.forEach((id, instance) -> {
                    if (instance.getClass().getSimpleName() == "Folder") {
                        Folder casted = (Folder) instance;
                        lsR_Folders.put(casted.name, casted);
                    } else { System.out.println(String.format("%s(%.2f)", instance.name, instance.getSize(instance))); }
                });
                System.out.println();
                // {k: directoryName, v: directoryInstance}
                lsR_Folders.forEach((name, directory) -> {
                    System.out.println(name);
                    directory.getContentsSummary(true, true);
                    System.out.println();
                });
            }
        }
    }

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

    public String getPath() {
        StringBuilder pathBuilder = new StringBuilder();
        Folder refTo = this;
        while (refTo != null) {
            pathBuilder.insert(0, String.format("%s", refTo.parent == null ? "/" : String.format("%s", refTo == this ? String.format("%s", refTo.name) : String.format("%s/", refTo.name))));
            refTo = refTo.parent;
        }
        return pathBuilder.toString();
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
    public double getSize(ContainingType self) {
        double[] accumulatedSize = {0.0};

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

class Move extends SharedScanner implements Command {
    Scanner fsm_Scanner;
    public Move(Scanner fsm_Scanner) { super(fsm_Scanner); }


    @Override
    public String usage() {
        return "Format your command in the following way: move containingType_name destinationName";
    }

    @Override 
    public String description() {
        return "Moves a file or folder from a folder to another folder such as Folder/File A in Folder 12 move to Folder 15";
    }

    @Override
    public boolean accepts(int argumentCount) {

        return true;
    }

    @Override 
    public void execute(String[] arguments) {
        

    }
}

class Touch implements Command {
    // touch NAME SIZE_KB
    private final FileSystemManager ownedBy;
    public Touch(FileSystemManager ownedBy) { this.ownedBy = ownedBy; }

    @Override 
    public String usage() {
        return "touch fileName.fileType";
    }

    @Override 
    public String description() {
        return "Creates a file within the current directory, if a folder is trying to be created, utilize 'mkdir'";
    }

    @Override
    public boolean accepts(int argumentCount) {

        return true;
    }

    @Override
    public void execute(String[] arguments) {


    }
}

class MakeDirectory implements Command {
    private final FileSystemManager ownedBy;
    public MakeDirectory(FileSystemManager ownedBy) { this.ownedBy = ownedBy; }

    @Override 
    public String usage() { return "mkdir folderName"; }

    @Override
    public String description() { return "Creates a folder within the current directory."; }

    @Override
    public boolean accepts(int argumentCount) {

        return argumentCount == 1;
    }

    @Override
    public void execute(String[] arguments) {
        ContainingType newInstance = new Folder(arguments[0], null);
        // TODO: configure statement in case of invalid error being thrown
        this.ownedBy.getCurrentDirectory().addToContents(newInstance);
    }
}

class List implements Command {
    // ls, ls -R
    private final FileSystemManager ownedBy;
    public List(FileSystemManager ownedBy) { this.ownedBy = ownedBy; }

    @Override
    public String usage() { return "ls or ls -R"; }

    @Override
    public String description() {
        return "ls: lists all files and subfolders within the current directory.\nls -R Same as ls and also goes into every subfolder at every depth. Each folder gets a heading with its path";
    }

    @Override
    public boolean accepts(int argumentCount) {

        return argumentCount == 0 || argumentCount == 1;
    }

    @Override
    public void execute(String[] arguments) {
        // this method does what it says above
            // Lists all contents of the current subdirectory
            // Much like pwd but goes over the hm
                // Since the hm is {k: id, v: instance} we can print this
        boolean isFull = arguments.length == 1;
        if (isFull) { this.ownedBy.getCurrentDirectory().getContentsSummary(true, true); } else {
            this.ownedBy.getCurrentDirectory().getContentsSummary(true, false);
        }
    }
}

class CD extends SharedScanner implements Command{
    // cd NAME, cd .. , cd /
    public CD(Scanner fsm_Scanner) { super(fsm_Scanner); }

    @Override
    public String usage() {
        return "cd\ncd fileName\ncd /path";
    }

    @Override
    public String description() {
        return "cd: return to root directory\ncd NAME: changes the current directory into a child directory of the current directory\ncd /path/: changes the directory into the given path";
    }

    @Override
    public boolean accepts(int argumentCount) {

        return true;
    }

    @Override
    public void execute(String[] arguments) {

    }
}

class CurrentPath implements Command {
    // pwd
    private final FileSystemManager ownedBy;
    public CurrentPath(FileSystemManager ownedBy) { this.ownedBy = ownedBy; }

    @Override
    public String usage() {
        return "pwd";
    }

    @Override
    public String description() {
        return "Outputs the current directy path such as /users/user1/subLocation/folderName/";
    }

    @Override
    public boolean accepts(int argumentCount) {
        return argumentCount == 0;
    }

    @Override
    public void execute(String[] arguments) {
        System.out.println(this.ownedBy.getCurrentDirectory().getPath());
    }
}

class SizeCommand extends SharedScanner implements Command {
    // size, size NAME
    public SizeCommand(Scanner fsm_Scanner) { super(fsm_Scanner); }

    @Override
    public String usage() {
        return "size\nsize NAME";
    }

    @Override
    public String description() {
        return "Outputs the size of the current directory if just supplied with 'size', if inputting 'size NAME' returns the size of the folder or file";
    }

    @Override
    public boolean accepts(int argumentCount) {

        return true;
    }

    @Override
    public void execute(String[] arguments) {

    }
}

class FindCommand extends SharedScanner implements Command {
    // find NAME
    public FindCommand(Scanner fsm_Scanner) { super(fsm_Scanner); }

    @Override
    public String usage() {
        return "find NAME, name can be a folder's name or a file's name";
    }

    @Override
    public String description() {
        return "Finds the file or folder and returns the path";
    }

    @Override
    public boolean accepts(int argumentCount) {

        return true;
    }

    @Override
    public void execute(String[] arguments) {

    }
}

class RemoveCommand extends SharedScanner implements Command {
    // rm NAME
    public RemoveCommand(Scanner fsm_Scanner) { super(fsm_Scanner); }

    @Override
    public String usage() {
        return "rm fileName or folderName";
    }

    @Override
    public String description() {
        return "removes a file or folder, deleting a folder also deletes its descendants and reclaims their disk space";
    }

    @Override
    public boolean accepts(int argumentCount) {

        return true;
    }

    @Override
    public void execute(String[] arguments) {

    }
}

class DiskCommand extends SharedScanner implements Command {
    // disk
    public DiskCommand(Scanner fsm_Scanner) { super(fsm_Scanner); }

    @Override
    public String usage() {
        return "disk, can be used at any level within the system";
    }

    @Override
    public String description() {
        return "Displays the disk and its used vs available space";
    }

    @Override
    public boolean accepts(int argumentCount) {

        return true;
    }

    @Override
    public void execute(String[] arguments) {

    }
}

class HelpCommand extends SharedScanner implements Command {
    // help
    public HelpCommand(Scanner fsm_Scanner) { super(fsm_Scanner); }

    @Override
    public String usage() {

        return "help, can be used at any level within the system";
    }

    @Override
    public String description() {
        return "Displays each command's usage and description";
    }

    @Override
    public boolean accepts(int argumentCount) {
        
        return true;
    }

    @Override
    public void execute(String[] arguments) {

    }
}

class ExitCommand extends SharedScanner implements Command {
    // exit
    public ExitCommand(Scanner fsm_Scanner) { super(fsm_Scanner); }

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

abstract class SharedScanner {
    Scanner fsm_Scanner;

    public SharedScanner(Scanner fsm_Scanner) {
        this.fsm_Scanner = fsm_Scanner;
    }
}


class FileSystemManager implements TwoFactorAuthentication {
    String name;
    HashMap<String, Command> commands = new HashMap<>();
    private Folder currentDirectory;
    public FileSystemManager(String name) {
        this.name = name;
        this.currentDirectory = new Folder("root", null);

    }

    public FileHolder createFileHolder(Scanner userCreator, Random rand) {
        // add to static hm of users
        FileHolder newUser = FileHolder.createHolder(userCreator, rand);
        if (newUser != null) { newUser.fsm = this; }
        return newUser;
    }

    public void handleCommand(String userInput) {
        String[] args = userInput.trim().split(" ");
        String commandFrom = args[0];
        Command command = commands.get(commandFrom);
        if (command != null) {
            String[] finalArgs = Arrays.copyOfRange(args, 1, args.length);
            if (command.accepts(finalArgs.length)) { command.execute(finalArgs); } else {
                return; // TODO: proper error handling
            }
        }
    }

    public Folder getCurrentDirectory() { return this.currentDirectory; }

    public void login() {

    }

    public void logout() {

    }
}