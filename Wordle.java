import java.util.Random;

public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        In in = new In(filename);
        String[] words = in.readAllStrings();
        in.close();
        return words;
    }

    // Choose a random secret word from the dictionary.
    // Hint: Pick a random index between 0 and dict.length (not including) using
    // Math.random()
    public static String chooseSecretWord(String[] dict) {
        Random random = new Random();
        return dict[random.nextInt(dict.length)];
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
        return secret.indexOf(c) != -1;
    }

    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[][][] attemptToGuessAndResults, int attempt) {
        for (int i = 0; i < secret.length(); i++) {
            char chr = guess.charAt(i);
            if (secret.indexOf(chr) != 1) {
                if (secret.charAt(i) == chr) {
                    attemptToGuessAndResults[attempt][1][i] = 'G';
                } else {
                    attemptToGuessAndResults[attempt][1][i] = 'Y';
                }
            } else {
                attemptToGuessAndResults[attempt][1][i] = '_';
            }
        }
    }


    public static void storeGuess(String guess, char[][][] attemptToGuessAndResults, int attempt) {
        for (int i = 0; i < guess.length(); i++) {
            attemptToGuessAndResults[attempt][0][i] = guess.charAt(i);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][][] attemptToGuessAndResults, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < attemptToGuessAndResults[row][0].length; col++) {
                System.out.print(attemptToGuessAndResults[row][0][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < attemptToGuessAndResults[row][1].length; col++) {
                System.out.print(attemptToGuessAndResults[row][1][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        for (int i = 0; i < resultRow.length; i++) {
            if (resultRow[i] != 'G') {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllChars(String guessInput) {
        for (int i = 0; i < guessInput.length(); i++) {
            char chr = guessInput.charAt(i);
            if (!((chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z'))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Prepare 2D arrays for guesses and results
        char[][][] attemptToGuessAndResults = new char[6][2][5];

        // Prepare to read from the standart input 
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            In in = new In();            
            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                
                guess = in.readLine();
                if (guess.length() != WORD_LENGTH || !isAllChars(guess)) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            // Store guess and compute feedback
            storeGuess(guess, attemptToGuessAndResults, attempt);
            computeFeedback(secret, guess, attemptToGuessAndResults, attempt);

            // Print board
            printBoard(attemptToGuessAndResults, attempt);

            // Check win
            if (isAllGreen(attemptToGuessAndResults[attempt][1])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            // ... follow the assignment examples for how the printing should look like
            System.out.printf("Sorry, you did not guess the word.%nThe secret word was: %s%n", secret);
        }

        inp.close();
    }
}
