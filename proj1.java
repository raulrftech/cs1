import java.util.Scanner;

class proj1 {
    public static void main(String[] args) {
        // Code Submission (worth 60%)
        Scanner globalScanner = new Scanner(System.in);
        String[] possibleTopics = new String[] {
            "RGB Questionnaire",
            "Pet Test",
            "Math Test",
            "Intro to Comp Sci Test"
        };

        int totalRight = 0; int totalWrong = 0; int totalTestsTaken = 0;
        String sentinel = "";
        while (!sentinel.trim().equalsIgnoreCase("no")) {
            int[] results = topicChoice(globalScanner, possibleTopics);
            if (results.length == 3 ) {  totalRight += results[0]; totalWrong += results[1]; totalTestsTaken += results[2]; }
            System.out.println("Would you like to play again"); sentinel = globalScanner.nextLine().trim();
        }
        System.out.println(String.format("Thank you for taking %d tests. Throughout taking these tests you got a total of %d questions right and %d wrong.", totalTestsTaken, totalRight, totalWrong));
        globalScanner.close();
    }

    // Code Submission (worth 60%)
    public static int[] topicChoice(Scanner gs, String[] possibleTopics) {
        String retrievedChoice = "";
        int determinedChoice = 0;
        while (determinedChoice <= 0 || determinedChoice > 4) {
            System.out.println("Choose from the following topics below, enter the corresponding number.\n1. RGB Questionnaire (great if you're into UI\n2. Pet Test\n3. Math Test\n4. Intro to Comp Sci Test\nEnter exit to exit");
            retrievedChoice = gs.nextLine().trim();
            if (retrievedChoice.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                determinedChoice = Integer.parseInt(retrievedChoice);
                
            } catch (Exception e) { 
                if (!retrievedChoice.equalsIgnoreCase("exit")) { System.out.println(String.format("There was an incorrect input provided. Try again.", e.getLocalizedMessage())); }
                retrievedChoice = retrievedChoice.equalsIgnoreCase("exit") ? "exit" : "";
            }
        }
        return retrievedChoice.equalsIgnoreCase("exit") ? new int[] {} : startSection(determinedChoice, possibleTopics, gs);
        
    }
    public static String[] questionDeterminator(int topicNumber) {
        String[] questions = new String[5];

        if (topicNumber == 1) {
            questions[0] = "What is the RGB(hex) for red?";
            questions[1] = "What is the RGB(hex) for green?";
            questions[2] = "What is the RGB(hex) for blue?";
            questions[3] = "What is the RGB(hex) for white?";
            questions[4] = "What is the RGB(hex) for black?";
            return questions;
        } else if (topicNumber == 2) {
            questions[0] = "What household animal meows?";
            questions[1] = "What household animal barks?";
            questions[2] = "What animal slithers?";
            questions[3] = "What animal hops?";
            questions[4] = "What animal chirps?";
            return questions;
        } else if (topicNumber == 3) {
            questions[0] = "What is 2+2?";
            questions[1] = "what is 3 sqaured?";
            questions[2] = "What is the log of 100?";
            questions[3] = "What is the slope-intercept form?";
            questions[4] = "What is the derivative of 9x^3 + 34x^2 + 28? Do not include C in the answer."; // 18x^2 + 68x
            return questions;
        } else {
            // no else needed for option 4 since the guaranteed topicPossibilites range from 1-4
            questions[0] = "What do you think questions were stored in?";
            questions[1] = "Can int be null?";
            questions[2] = "Can a method return null?";
            questions[3] = "Can main be named something else?";
            questions[4] = "Can the file-class be named something else than the file name it sits in?";
            return questions;
        }
    }
    public static String[] answersDeterminator(int topicNumber) {
        String[] answers = new String[5];

        if (topicNumber == 1) {
            answers[0] = "2550000";
            answers[1] = "0025500";
            answers[2] = "0000255";
            answers[3] = "255255255";
            answers[4] = "000000";
            return answers;
        } else if (topicNumber == 2) {
            answers[0] = "cat";
            answers[1] = "dog";
            answers[2] = "snake";
            answers[3] = "rabbit";
            answers[4] = "bird";
            return answers;
        } else if (topicNumber == 3) {
            answers[0] = "4";
            answers[1] = "9";
            answers[2] = "2";
            answers[3] = "y=mx+b";
            answers[4] = "18x^2+68x";
            return answers;
        } else {
            answers[0] = "arrays";
            answers[1] = "no";
            answers[2] = "depends";
            answers[3] = "no";
            answers[4] = "no";
            return answers;
        }
    }
    public static int[] startSection(int returnValueFrom, String[] topics, Scanner gs) {
        String topic = topics[returnValueFrom - 1];
        System.out.println(String.format("You are now going to be asked 5 questions for the %s", topic));

        int rightAnswers = 0; int wrongAnswers = 0;
        String[] questionsForTopic = questionDeterminator(returnValueFrom);
        String[] answersForTopic = answersDeterminator(returnValueFrom);
        for (int pair = 0; pair < questionsForTopic.length; pair++) {
            System.out.println(questionsForTopic[pair]);
            if (gs.nextLine().trim().equalsIgnoreCase(answersForTopic[pair])) { rightAnswers++; } else { wrongAnswers++;}
        }
        System.out.println(String.format("You got %d correct answers and %d wrong answers", rightAnswers, wrongAnswers));
        return new int[] { rightAnswers, wrongAnswers, 1};
    }
    


    // Topic of this Project
    //      Creating a program that incorps user input into conditional logic
    //      Specifically, creating a quiz or questionnaire on a topic of interest
    //      The output a user receives from your program will differ based on responses
    // Project Reqs
    //      Input/Output
    //          ask user at least 5 mcq's. Each question myst clearly display the available answer choices
    //      Variables
    //          create vars to store data thatll be needed later in the program
    //      Strings
    //          Use 1D arrays to store questions, answer choices and user responses
    //      Conditionals
    //          Use if, else if, ternaries
    //      1D Arrays
    //          use 1F arrays to store questions, answer choices, responses and correct answers
    //      Loops
    //          use at least one for or while loop
    //      Methods
    //          create and call >=2 methods
    //      Score/Result
    //          calaculate and display final result
    //      Answer Feedback
    //          May tell the user whether an individual answer was correct or provide other feedback

    
    // Checkpoint (worth 10%)
    //      Brief Descriptions of focus and final res users get
    //          Itll be an mcq test on primitive types and last will be a fill in
    //          Total of 5 questions
    //              1-2: poor performance, practice will be suggested
    //              3-4: average performance, recommondation based on what answers were wrong
    //              5:   outstanding job, keep the roll
    //      List of quesitons and possible responses
    //      List of vars thatll be used
    //          string to capture input, Integer.parseInt() to verify the last question is a valid number, sentinel value for early exit 
    //      Outline of condiitonal statements and consequences
    //          if (Integer.parseInt(lastInput).equals(Integer.MAX_VALUE)) but this would req a try/catch since parseInt etc throw
    //          determining standing such as what to recommend
}

// Flowchart Submission (worth 10%)


// Explanation (worth 20%)