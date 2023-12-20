//
// Gokje.java
//
// Created by Guus Kamphuis (1045891)
// 20-12-2023
//
//

import javax.swing.*;
import java.util.*;

public class Gokje {
    public static final String WINDOW_TITLE = "Gokje";

    public static final int MAX_GUESSES = 3;

    public enum Difficulty {
        Easy,
        Medium,
        Hard,
        Insane
    }

    public static final Map<Difficulty, Integer> numberRanges = new HashMap<>() {{
        put(Difficulty.Easy,   10);
        put(Difficulty.Medium, 20);
        put(Difficulty.Hard,   50);
        put(Difficulty.Insane, 100);
    }};

    public static int playerScore = 0;
    public static int computerScore = 0;

    public static void main(String[] args) {
        int input = getSelectionInput("Welcome to Gokje, please select a gamemode to start\n\nPlayer score: %s\nComputer score: %s".formatted(playerScore, computerScore), new String[]{ "Player guesses", "Computer guesses", "Help" });

        switch (input) {
            case 0:
                startPlayer();
                break;

            case 1:
                startComputer();
                break;

            case 2:
                showHelp();
                break;

            default:
                return;
        }

        main(args);
    }

    public static void startPlayer() {
        Difficulty selectedDifficulty = GetDifficulty();
        int number = generateNumber(selectedDifficulty);
        int tries = 0;

        while (true) {
            int guess = getNumberInput("Guess a number! %s %s remaining".formatted(MAX_GUESSES - tries, MAX_GUESSES - tries == 1 ? "try" : "tries"));

            if (guess == number) {
                showMessage("Congrats! You guessed the number", JOptionPane.INFORMATION_MESSAGE);
                playerScore++;
                return;
            }

            tries++;

            if (tries == MAX_GUESSES) {
                break;
            }

            showMessage("That's the wrong number! go a little %s".formatted(guess < number ? "higher" : "lower"), JOptionPane.INFORMATION_MESSAGE);
        }

        showMessage("You're out of guesses! the number was %s".formatted(number), JOptionPane.INFORMATION_MESSAGE);
        computerScore++;
    }

    public static void startComputer() {
        Difficulty selectedDifficulty = generateDifficulty();
        int upperBound = numberRanges.get(selectedDifficulty);
        int number = getNumberInput("The computer has selected the %s difficulty! choose a number between 1 and %s (inclusive)".formatted(selectedDifficulty.name(), upperBound));
        int tries = 0;
        Random random = new Random();

        while (true) {
            int guess = random.nextInt(1, upperBound + 1);

            if (guess == number) {
                showMessage("The computer guessed the correct number!", JOptionPane.INFORMATION_MESSAGE);
                computerScore++;
                return;
            }

            tries++;

            showMessage("The computer guessed %s! it has %s %s remaining".formatted(guess, MAX_GUESSES - tries, MAX_GUESSES - tries == 1 ? "try" : "tries"), JOptionPane.INFORMATION_MESSAGE);

            if (tries == MAX_GUESSES) {
                break;
            }
        }

        showMessage("The computer is out of guesses!", JOptionPane.INFORMATION_MESSAGE);
        playerScore++;
    }

    public static void showHelp() {
        showMessage("""
                <html>
                    <b>Player guesses</b>
                    <br>
                    The computer generates a number and the player has to guess it in %s tries
                    <br>
                    If the player guesses the number they score 1 point, if they don't the computer scores 1 point.
                    
                    <br>
                    <br>
                    
                    <b>Computer guesses</b>
                    <br>
                    The player chooses a number and the computer has to guess it in %s tries
                    <br>
                    If the computer guesses the number it scores 1 point, if it doesn't the player scores 1 point.
                    
                    <br>
                    <br>
                    
                    <b>Difficulties</b>
                    <br>
                    <b>Easy</b> 1-10
                    <br>
                    <b>Medium</b> 1-20
                    <br>
                    <b>Hard</b> 1-50
                    <br>
                    <b>Insane</b> 1-100
                </html>
                """.formatted(MAX_GUESSES, MAX_GUESSES),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static int generateNumber(Difficulty difficulty) {
        return new Random().nextInt(1, numberRanges.get(difficulty) + 1);
    }

    public static Difficulty generateDifficulty() {
        return Difficulty.values()[new Random().nextInt(Difficulty.values().length)];
    }

    public static Difficulty GetDifficulty() {
        Object selection = getDropdownInput("Select difficulty", new String[]{ Difficulty.Easy.name(), Difficulty.Medium.name(), Difficulty.Hard.name(), Difficulty.Insane.name() });
        return Difficulty.valueOf(selection.toString());
    }

    public static int getNumberInput(String message) {
        String input = JOptionPane.showInputDialog(null, message, WINDOW_TITLE, JOptionPane.QUESTION_MESSAGE);

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showMessage("\"%s\" is not a valid number".formatted(input), JOptionPane.ERROR_MESSAGE);
            return getNumberInput(message);
        }
    }

    public static int getSelectionInput(String message, String[] options) {
        return JOptionPane.showOptionDialog(null, message, WINDOW_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
    }

    public static Object getDropdownInput(String message, String[] options) {
        return JOptionPane.showInputDialog(null, message, WINDOW_TITLE, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    public static void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(null, message, WINDOW_TITLE, messageType);
    }
}