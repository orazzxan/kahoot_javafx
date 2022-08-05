package com.example.project3f;

import java.util.*;
import java.io.*;

public class Quiz {
    private static String name;
    private static ArrayList<Question> questions = new ArrayList<>();

    Quiz() {}

    public static void setName(String fileName) {
        name = fileName;
    }

    public String getName() {
        return name;
    }

    public static void addQuestion(Question addQuestion) {
        questions.add(addQuestion);
    }

    public static ArrayList<Question> loadFromFile(String fileName) throws FileNotFoundException {

        try {
            File file = new File(fileName);
            Scanner input = new Scanner(file);

            while(input.hasNext()) {
                String description = input.nextLine();

                if(description.equals("")) {
                    continue;
                }else if(!description.contains("{blank}")) {
                    Question question = new Test();
                    ArrayList<String> list = new ArrayList<>();
                    question.setDescription(description);

                    while(input.hasNext()) {
                        String variant = input.nextLine();

                        if(variant.equals("")) {
                            break;
                        }

                        list.add(variant);
                    }

                    String[] variants = new String[list.size()];
                    list.toArray(variants);

                    ((Test)question).setOptions(variants);
                    question.setAnswer(((Test)question).getAnswerLabel());

                    questions.add(question);

                }else {
                    Question question = new FillIn();
                    question.setDescription(description);
                    question.setAnswer(input.nextLine());

                    questions.add(question);

                }
            }
        }catch(FileNotFoundException ex) {
            System.out.println("Such a file does not exist!\n");
            System.exit(0);
        }
        Collections.shuffle(questions);
        return questions;
    }

    @Override
    public String toString() {
        return "WELCOME TO \"" + getName() + "\" QUIZ!";
    }

    public void start() {
        Scanner input = new Scanner(System.in);
        int count = 0;

        System.out.println("\n=====================================================================\n");
        System.out.println(toString());
        System.out.println("\n-------------------------------------------------------------------\n");
        for(Question q : questions) {
            if(q instanceof Test) {
                Test test = ((Test)q);
                System.out.println(test.toString());
                System.out.println("- - - - - - - - - - - - - -");
                System.out.print("Enter the correct choice: ");
                String myAnswer = input.next();
                int widamsizdik = 1;
                while(true) {
                    if(!myAnswer.matches("[A-Z]")) {
                        System.out.print("Invalid choice! Try again (Ex: A, B, ...): ");
                        myAnswer = input.next();
                    }else {
                        String check = "";
                        for(int i = 0; i < test.getNumOfOptions(); i++) {
                            check += test.getLabelAt(i);
                        }

                        if(check.contains(myAnswer)) {
                            break;
                        }else if(widamsizdik == 3) {
                            System.out.println("EMAEEE duristap jaza koiw");
                            myAnswer = input.next();
                            widamsizdik = 1;
                        }
                        else {
                            System.out.print("Invalid choice! Try again (Ex: A, B, ...): ");
                            myAnswer = input.next();
                            widamsizdik++;
                        }
                    }
                }

                if(myAnswer.equals(test.getAnswer())) {
                    count++;
                    System.out.println("Correct!\n");
                    System.out.println("-------------------------------------------------------------------\n");
                }else {
                    System.out.println("Incorrect!\n");
                    System.out.println("-------------------------------------------------------------------\n");
                }
            }else {
                FillIn fillIn = ((FillIn)q);
                System.out.println(fillIn.toString());
                System.out.println("- - - - - - - - - - - - - -");
                System.out.print("Type your answer: ");
                String myAnswer = input.next();

                if(myAnswer.equalsIgnoreCase(fillIn.getAnswer())) {
                    count++;
                    System.out.println("Correct!\n");
                    System.out.println("-------------------------------------------------------------------\n");
                }else {
                    System.out.println("Incorrect!\n");
                    System.out.println("-------------------------------------------------------------------\n");
                }
            }
        }

        float result = (count * 100) / questions.size();
        System.out.printf("Correct Answers: %d/%d (%.1f%%)", count, questions.size(), (float)result);
        System.out.println();

    }

}
