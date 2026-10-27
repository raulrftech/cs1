import java.util.Random;
import java.util.Scanner;

public class proj2 {
    public static void main(String[] args) {
        Scanner globalScanner = new Scanner(System.in);
        FileSystemManager fsm = new FileSystemManager("Foreman Technologies");
        FileHolder fh = fsm.createFileHolder(globalScanner);
        System.out.println(fh);
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
    default String generate2FACode() {
        Random rand = new Random();
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

        return (runningTotal == 560) ? code.toString() : generate2FACode();
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
    public static FileHolder createHolder(Scanner userInfo) {
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

        String retrieved2FA = fh.generate2FACode();
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

class FileSystemManager implements TwoFactorAuthentication {
    String name;
    public FileSystemManager(String name) { this.name = name; }

    public FileHolder createFileHolder(Scanner userCreator) {
        // add to static hm of users
        FileHolder newUser = FileHolder.createHolder(userCreator);
        if (newUser != null) { newUser.fsm = this; }
        return newUser;
    }


    public void login() {

    }

    public void logout() {

    }
}