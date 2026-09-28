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
        while (!sentinel.trim().equalsIgnoreCase("no") || !sentinel.trim().equalsIgnoreCase("n")) {
            Integer results = topicChoice(globalScanner, possibleTopics);
            if (results.equals(null) ) {
                totalRight += results;
                totalWrong += (5 - results);
                totalTestsTaken++;
            }
            System.out.println("Would you like to play again"); sentinel = globalScanner.nextLine().trim();
        }
        System.out.println(String.format("Thank you for taking %d tests. Throughout taking these tests you got a total of %d questions right and %d wrong.", totalTestsTaken, totalRight, totalWrong));
        globalScanner.close();
    }

    // Code Submission (worth 60%)
    public static Integer topicChoice(Scanner gs, String[] possibleTopics) {
        String retrievedChoice = "";
        int determinedChoice = 0;
        while (determinedChoice <= 0 || determinedChoice > 4) {
            System.out.println("Choose from the following topics below, enter the corresponding number.\n1. RGB Questionnaire (great if you're into UI\n2. Pet Test\n3. Math Test\n4. Intro to Comp Sci Test\nEnter exit to exit");
            retrievedChoice = gs.nextLine().trim();
            try {
                determinedChoice = Integer.parseInt(retrievedChoice);
                
            } catch (Exception e) { 
                if (!retrievedChoice.equalsIgnoreCase("exit")) { System.out.println("There was an incorrect input provided. Try again."); }
                retrievedChoice = retrievedChoice.equalsIgnoreCase("exit") ? "exit" : "";
            }
        }
        return retrievedChoice.equalsIgnoreCase("exit") ? null : startSection(determinedChoice, possibleTopics, gs);
        
    }
    public static String[] questionDeterminator(int topicNumber) {
        String[] questions = new String[5];

        if (topicNumber == 1) {
            questions[0] = "What is the RGB(hex to dec, csv) for red?";
            questions[1] = "What is the RGB(hex to dec, csv) for green?";
            questions[2] = "What is the RGB(hex to dec, csv) for blue?";
            questions[3] = "What is the RGB(hex to dec, csv) for white?";
            questions[4] = "What is the RGB(hex to dec, csv) for black?";
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
            questions[1] = "what is 3 squared?";
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
            answers[0] = "A. 0, 0, 0";
            answers[1] = "B. 0, 255, 0";
            answers[2] = "C. 255, 0, 0";
            answers[3] = "D. 255, 255, 255";
            answers[4] = "E. 0, 0, 255";
            return answers;
        } else if (topicNumber == 2) {
            answers[0] = "A. Cat";
            answers[1] = "B. Dog";
            answers[2] = "C. Snake";
            answers[3] = "D. Bunny";
            answers[4] = "E. Bird";
            return answers;
        } else if (topicNumber == 3) {
            answers[0] = "A. 9";
            answers[1] = "B. 4";
            answers[2] = "C. y = mx + b";
            answers[3] = "D. 2";
            answers[4] = "E. 27x^2 + 68x";
            return answers;
        } else {
            answers[0] = "A. no, only if its a public top-level class";
            answers[1] = "B. yes";
            answers[2] = "C. no";
            answers[3] = "D. no, main cannot me named something else.";
            answers[4] = "E. arrays";
            return answers;
        }
    }
    public static Integer startSection(int returnValueFrom, String[] topics, Scanner gs) {
        String topic = topics[returnValueFrom - 1];
        System.out.println(String.format("You are now going to be asked 5 questions for the %s.", topic));

        int rightAnswers = 0; int wrongAnswers = 0;
        String[] questionsForTopic = questionDeterminator(returnValueFrom);
        String[] answersForTopic = answersDeterminator(returnValueFrom);
        for (int pair = 0; pair < questionsForTopic.length; pair++) {
            System.out.println(questionsForTopic[pair]);
            for (String answerChoice : answersForTopic) { System.out.println(answerChoice); }
            if (gs.nextLine().trim().substring(0,1).equalsIgnoreCase(answersForTopic[pair].substring(0, 1).toLowerCase())) { rightAnswers++; } else { wrongAnswers++;}
        }
        System.out.println(String.format("You got %d correct answers and %d wrong answers", rightAnswers, wrongAnswers));
        return rightAnswers;
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