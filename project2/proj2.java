import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
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
    default String fixFileName(String name, int i, boolean isFile) {
        if (isFile) {
            if (i > name.length() - 1) { return ""; }
            String corrected = "";
            return name.charAt(i) != '/' && name.charAt(i) != '\\' && name.charAt(i) != ' ' ? (corrected + name.charAt(i) + fixFileName(name, i + 1, isFile)) : fixFileName(name, i + 1, isFile);
        } else {
            if (i > name.length() - 1) { return ""; }
            String corrected = "";
            return name.charAt(i) != '/' && name.charAt(i) != '\\' && name.charAt(i) != ' ' && name.charAt(i) != '.' ? (corrected + name.charAt(i) + fixFileName(name, i + 1, isFile)) : fixFileName(name, i + 1, isFile);
        }
        
    }
}

class Folder extends ContainingType implements Fix_FileName {
    TreeMap<Integer, ContainingType> contents = new TreeMap<>();
    public TreeMap<String, ContainingType> names = new TreeMap<>();

    public void ls(boolean isR) {
        HashMap<String, ArrayList<Folder>> lsR_Folders = new HashMap<>();
        if (!isR) {
            this.contents.forEach((id, instance) -> { System.out.println(String.format("%s(%.2fkb)", instance.name, instance.getSize(instance))); }); } else {
                this.ls(false);

                this.contents.forEach((id, instance) -> {
                    if (instance.getClass().getSimpleName() == "Folder") {
                        Folder casted = (Folder) instance;
                        lsR_Folders.computeIfAbsent(casted.getPath(), k -> new ArrayList<>()).add(casted);
                    }
                });

                lsR_Folders.forEach((path, container) -> {
                    System.out.println();
                    System.out.println(path);
                    container.forEach((obj) -> { obj.ls(true); });
                    System.out.println();
                });
        }
    }

    public Folder(String name, Folder parent) {
        super(name, parent);
    }

    public boolean addToContents(ContainingType appending) {
        // this is case-sensitive already since different letters have different Unix values
        // chek for spaces, \ or / in name
        boolean isFile = appending instanceof File;
        String fixedName = fixFileName(appending.name, 0, isFile);
        if (fixedName.isEmpty()) { return false; } else { appending.name = fixedName; }
        if (names.containsKey(appending.name)) { return false; }
        if (appending.name.charAt(0) == '.') { return false; }
        // . occurence; no more than 1, check if folder, if so, it cannot have a period
        int periodCounter = 0;
        for (char c: appending.name.toCharArray()) { if (c=='.') {
            periodCounter++;
            if (appending instanceof Folder) { return false; } else {
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
            this.pathBuilder(refTo, pathBuilder);
            refTo = refTo.parent;
        }
        return pathBuilder.toString();
    }

    public String getPath_File(String name, TreeMap<String, ContainingType> hm) {
        // this method searches within currentDirectory, returns null if not found
        StringBuilder sb = new StringBuilder();
        ContainingType possibleMatch = hm.get(name);
        if (possibleMatch == null || possibleMatch instanceof Folder) { return null; } else {
            while (possibleMatch != null) {
                this.pathBuilder(possibleMatch, sb);
                possibleMatch = possibleMatch.parent;
            }
            return sb.toString();

            // TODO: finds a doc, should only find files
        }
    }

    public ArrayList<String> findChild(Folder cd, String name) {
        ArrayList<String> matchedPaths = new ArrayList<>();

        // since main is the root, the getPath_File method searches as it goes level by leve
            // root -> 1 -> 2 -> 3
        // Paths are the thing that searches through parents in order to get full path
        // an iteration really isnt needed well might be to see if the cd has any folders
        // Rules
            // Call method on cd
            // iterate to find folders, if so, call method with that
        matchedPaths.add(getPath_File(name, cd.names));
        for (Map.Entry<String, ContainingType> pair: cd.names.entrySet()) {
            if (pair.getValue().getClass().getSimpleName() == "Folder") { cd = (Folder) pair.getValue(); matchedPaths.addAll(findChild(cd, name)); }
        }
        matchedPaths.removeIf(Objects::isNull);
        return matchedPaths;
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
        return accumulatedSize[0] / 1000;
    }

    public void pathBuilder(ContainingType refTo, StringBuilder sb) {
        // Boolean Checks
            // if parent is null -> /
                // Else
                    // refTo is equal to compareTo which would be the parent
                        // just name or else name/
        // since refTo incrementation is basically like a linked list with reftTo = refTo.parent
            // each time this method is called refTo is never equal to the parent
        //sb.insert(0, String.format("%s", refTo.parent == null ? "/" : String.format("%s", refTo.parent != null ? String.format("%s", refTo.name) : String.format("%s/", refTo.name))));
        
        // refTo = refTo.next
        sb.insert(0, String.format("%s", refTo.parent != null ? String.format("/%s", refTo.name) : ""));
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
        if (isFull) { this.ownedBy.getCurrentDirectory().ls(true); } else {
            this.ownedBy.getCurrentDirectory().ls(false);
        }
    }
}

class CD implements Command{
    // cd NAME, cd .. , cd /
    private final FileSystemManager ownedBy;
    public CD(FileSystemManager ownedBy) { this.ownedBy = ownedBy; }

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

        return argumentCount == 1;
    }

    @Override
    public void execute(String[] arguments) {
        String command = arguments[0].trim();

        switch (command) {
            case "..":
                if (this.ownedBy.getCurrentDirectory().parent != null) {
                    this.ownedBy.changeCD(this.ownedBy.getCurrentDirectory().parent); 
                } else { System.out.println("You are at the upper-most directory already."); }
                break;
            case "/":
                Folder cd = this.ownedBy.getCurrentDirectory();
                while (cd.parent != null) {
                    cd = cd.parent;
                }
                this.ownedBy.changeCD(cd);
                break;
            default:
                Folder changeTo_Directory = this.ownedBy.nameExists(command);
                if (changeTo_Directory != null) {
                    this.ownedBy.changeCD(changeTo_Directory);
                } else { System.out.println("No matching directory found, please try again."); }
        }
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

class SizeCommand implements Command {
    // size, size NAME
    private final FileSystemManager ownedBy;
    public SizeCommand(FileSystemManager ownedBy) { this.ownedBy = ownedBy; }

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

        return argumentCount == 0 || argumentCount == 1;
    }

    @Override
    public void execute(String[] arguments) {
        // since reg would be len 0 and direct would be len 1
        if (arguments.length == 0) { 
            Folder cd = this.ownedBy.getCurrentDirectory();
            System.out.println(cd != null ? cd.getSize(cd) : "No folder found");
        } else {
            ContainingType obj = this.ownedBy.getObject(arguments[0]);
            System.out.println(obj != null ? obj.getSize(obj) : "No file/folder found");
        }
    }
}

class FindCommand implements Command {
    // find NAME
    private final FileSystemManager ownedBy;
    public FindCommand(FileSystemManager ownedBy) { this.ownedBy = ownedBy; }

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

        return argumentCount == 1;
    }

    @Override
    public void execute(String[] arguments) {
        Folder cd = this.ownedBy.getCurrentDirectory();
        ArrayList<String> matches = cd.findChild(cd, arguments[0]);
        System.out.println(String.format("Found %d matches", matches.size()));
        for (String match: matches) { System.out.println(match); }
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

class HelpCommand implements Command {
    // help
    private final FileSystemManager ownedBy;
    public HelpCommand(FileSystemManager ownedBy) { this.ownedBy = ownedBy; }

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
        
        return argumentCount == 0;
    }

    @Override
    public void execute(String[] arguments) {
        for (Map.Entry<String, Command> pair : this.ownedBy.commands.entrySet()) {
            System.out.println(String.format("Command: %s%nUsage:    %s", pair.getKey(), pair.getValue().usage()));
        }
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
    Folder root;
    HashMap<String, Command> commands = new HashMap<>();
    //HashMap<Integer, ContainingType> createdContents = new HashMap<>();
    private Folder currentDirectory;
    public FileSystemManager(String name) {
        this.name = name;
        this.root = new Folder("root", null);
        this.currentDirectory = root;
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

    public Folder nameExists(String name) { 
        ContainingType found = this.currentDirectory.names.get(name);
        if (found instanceof File || found == null) { return null; } else {
            Folder casted = (Folder) found;
            return casted;
        }
    }
    public void changeCD(Folder to) { this.currentDirectory = to; }
    // create file
    // create folder

    // get file/folder from String name
    public ContainingType getObject(String fromName) { return this.currentDirectory.names.get(fromName); }


    public Folder getCurrentDirectory() { return this.currentDirectory; }

    public void login() {

    }

    public void logout() {

    }
}

class Disk {
    Memory disk_head;
    double maxSize;
    FileSystemManager fsm;

    private Disk(Memory disk_head, double maxSize, FileSystemManager fsm) {
        this.disk_head = disk_head;
        this.maxSize = maxSize;
        this.fsm = fsm;
    }

    public static Disk createDisk(Scanner proppedScanner, FileSystemManager fsm) {
        // Set Up - Disk Size
        System.out.println("What size would you like your disk to be? You can enter any number between 1.00 - 1000.00 kb, inclusive. Default is 100 kb.");
        double size = 0;
        try {
            double temp = Integer.parseInt(proppedScanner.nextLine().trim());
            size = temp < 0 || temp > 1000 ? 100 : temp;
            System.out.println(String.format("Set size to %.2f kilobytes", size));
        } catch (Exception e) {
            System.out.println("Wrong input was found, defaulting disk size to 100 kilobytes.");
            size = 100;
        }

        // Set Up - First File
        System.out.println("You are now going to create your first file, the name and file extension is required such as \"testFile.txt\"");
        String name = proppedScanner.nextLine().trim();
        File newFile = new File(name, null);

        // Write - To First File

        // Set Up - First Folder
        System.out.println("Would you like to create a new folder? Enter y for yes or n for no");
        boolean decision = proppedScanner.nextLine().trim().equalsIgnoreCase("y");
        if (decision) {
            System.out.println("Please enter a name for the new file. Please keep in mind, any \\ or / or . are not allowed");
            String folderName = proppedScanner.nextLine().trim();
            Folder newFolder = new Folder(folderName, fsm.root);
        } else {
            System.out.println("You chose not to create a new folder, the only 'folder' on the system is the root folder. You can continue.");
            newFile.parent = fsm.root;
        }

        Memory m1 = new Memory(null);




        return null;
    }
}

class Memory {
    Set<File> files;
    Set<String> containingText;
    Memory prev;
    Memory next;
    double size;

    public Memory(Memory prev) {
        this.prev = prev;
        this.files = new HashSet<>();
        this.containingText = new HashSet<>();
        this.next = null;
        this.size = 0;
    }

    public static Memory createNode(Memory prev, File withFile) {
        // this is going to be the failable method for creating a node
        // this is going to check current size of the disk so i was thinking of including the prop to a Disk instance
        // has to check the size of the file trying to get added and also if it has been included already which will utilize a different method
    }
}