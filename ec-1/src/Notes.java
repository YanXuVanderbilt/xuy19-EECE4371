import java.util.*;

public class Notes {
    public static void main(String[] args) {
        Map<String, String> notes = new Hashtable<String, String>();
        String prompt = "There are three operations available";
        String options = "(1) Type in a key-note pair with \"@\" as separator: e.g. reminder1 @ Put eggs in fridge\n" +
                "(2) Type in a key to retrieve notes: e.g. reminder\n" +
                "(3) Type quit to exit: e.g. quit";

        System.out.println("Welcome to the Notes program!");
        System.out.println("This is a command-line program that helps you store and retrieve your notes.");

        // Define a scanner for user input

        Scanner userInput = new Scanner(System.in);

        //System.out.println("Type in a line of text (a String) or \"quit\" to end:");
        System.out.println(prompt);
        System.out.println(options);

        while (userInput.hasNextLine()) {
            String line = userInput.nextLine();
            if (line.equals("quit")) {
                break;
            } else {
                String[] subStrings = line.split("@", 0);
                if (subStrings.length == 2) {
                    // storing a note
                    String key = subStrings[0].trim();
                    String note = subStrings[1].trim();
                    notes.put(key, note);
                    System.out.println("Your note has been saved!\n");
                } else if (subStrings.length == 1) {
                    String key = subStrings[0].trim();
                    if (notes.containsKey(key)) {
                        String note = notes.get(key);
                        System.out.println("Your note is:");
                        System.out.println(note + "\n");
                    } else {
                        System.out.println("Note not found.\n");
                    }
                } else {
                    System.out.println("Illegal input.\n");
                }

                System.out.println(options);
            }
        }

        System.out.println("bye!");
        userInput.close();

    }
}