package com.theslof;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final ArrayList<String> words = new ArrayList<String>();
    private static final String FILE_NAME = "wordlist";
    private static final int MENU_ITEMS = 2;
    private static final String MENU_STRING = "--- Hangman ---\n" +
            "(1) Play a new game\n" +
            "(0) Quit\n" +
            "\nPlease select an option: ";
    private static final String[] HANGMAN = new String[]{"|------\n",
            "|/\n",
            "|\n",
            "|\n",
            "|\n",
            "|\n",
            "|__________\n"};
    private static final String[] HANGMAN_FAIL = new String[]{"|------\n",
            "|/    |\n",
            "|     O\n",
            "|    /|\\\n",
            "|     |\n",
            "|    / \\\n",
            "|__________\n"};
    private static final int MAX_TRIES = 5;

    public static void main(String[] args) {
        //Parse wordlist file into ArrayList
        FileReader wordfile = null;
        BufferedReader buffer = null;
        try {
            wordfile = new FileReader(FILE_NAME);
            buffer = new BufferedReader(wordfile);

            String currentLine;

            while ((currentLine = buffer.readLine()) != null) {
                words.add(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null)
                    buffer.close();
                if (wordfile != null)
                    wordfile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (words.isEmpty()) {
            System.out.println("No words in word list, exiting!");
            return;
        }

        System.out.println("Read " + words.size() + " words from file!");


        //Main menu loop
        while (true) {
            int menuChoice = -1;

            do {
                //Print menu
                System.out.print(MENU_STRING);

                menuChoice = readInt();
            } while (menuChoice < 0 && menuChoice >= MENU_ITEMS);

            switch (menuChoice) {
                case 0:
                    System.out.println("Thank you for playing!");
                    return;
                case 1:
                    startGame();
                    break;
                default:
            }
        }
    }

    private static void startGame() {
        Random rand = new Random();
        int i = rand.nextInt(words.size());
        int length = words.get(i).length();

        char[] solution = words.get(i).toUpperCase().toCharArray();
        char[] word = new char[length];
        String letters = "";
        int tries = 0;

        for (int n = 0; n < words.get(i).length(); n++) {
            word[n] = '_';
        }

        while (tries < MAX_TRIES && !isSolved(word)) {
            printBoard(tries, word, letters);
//            System.out.print("\nSolution: ");
//            printWord(solution);
            System.out.print("\n\nPlease guess a letter: ");

            String guess = "";
            boolean waiting = true;
            do {
                String temp = readLetter();

                for (char c : temp.toCharArray()) {
                    String s = Character.toString(c);
                    if (!letters.contains(s)) {
                        letters += s;
                        guess += s;
                        waiting = false;
                    }

                }
            } while (waiting);

            for (char c : guess.toCharArray()) {
                boolean validGuess = false;

                for (i = 0; i < length; i++) {
                    if (solution[i] == c) {
                        word[i] = c;
                        validGuess = true;
                    }
                }
                if (!validGuess) {
                    tries++;
                }
            }
        }

        printBoard(tries, word, letters);

        if (tries >= MAX_TRIES) {
            System.out.println("\n\nYou lost!\nThe correct word was: " + new String(solution) + "\n");
            return;
        }

        System.out.println("\n\nYou won!\n");
    }

    private static void printBoard(int tries, char[] word, String letters) {
        System.out.print("\n\n" + HANGMAN[0]);
        for (int i = 1; i < HANGMAN.length; i++) {
            if (i <= tries)
                System.out.print(HANGMAN_FAIL[i]);
            else
                System.out.print(HANGMAN[i]);
        }
        printWord(letters.toCharArray());
        System.out.print("\n\n");
        printWord(word);
    }

    private static boolean isSolved(char[] word) {
        for (char c : word) {
            if (c == '_')
                return false;
        }
        return true;
    }

    private static void printWord(char[] word) {
        for (char c : word) {
            if (!(c >= 'A' && c <= 'Z')) {
                if (c != '_')
                    c = ' ';
            }
            System.out.print(c + " ");
        }
    }

    private static int readInt() {
        Scanner input = new Scanner(System.in);

        int i = 0;
        boolean waiting = true;

        do {
            try {
                i = input.nextInt();
                waiting = false;
            } catch (InputMismatchException e) {
                input.next();
            }
        } while (waiting);

        return i;
    }

//    private static char readUppercaseLetter() {
//        Scanner input = new Scanner(System.in);
//
//        char c = ' ';
//        boolean waiting = true;
//
//        do {
//            try {
//                c = input.next().charAt(0);
//                waiting = false;
//            } catch (InputMismatchException e) {
//                input.next();
//            }
//        } while (waiting || (c < 'A' || c > 'Z'));
//
//        return c;
//    }

    private static String readLetter() {
        Scanner input = new Scanner(System.in);

        String s = "";
        boolean waiting = true;

        do {
            try {
                s = input.next().toUpperCase();
                waiting = false;
            } catch (InputMismatchException e) {
                input.next();
            }
            if (s.length() <= 0) {
                waiting = true;
            } else if (s.length() == 1 && (s.charAt(0) < 'A' || s.charAt(0) > 'Z')) {
                waiting = true;
            }
        } while (waiting);

        return s;
    }
}
