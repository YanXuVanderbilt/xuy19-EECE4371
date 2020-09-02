import java.util.*;

public class Notes {
    public static void main(String[] args) {
        // TODO: raw use of parameterized class "Dictionary" or "Hashtable"?
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

        // put() method
        //geek.put("123", "Code");
        //geek.put("456", "Program");

        // elements() method :
        //for (Enumeration i = geek.elements(); i.hasMoreElements();)
        //{
        //    System.out.println("Value in Dictionary : " + i.nextElement());
        //}

        // get() method :
        //System.out.println("\nValue at key = 6 : " + geek.get("6"));

        // isEmpty() method :
        //System.out.println("\nThere is no key-value pair : " + geek.isEmpty() + "\n");

        // keys() method :
        //for (Enumeration k = geek.keys(); k.hasMoreElements();)
        //{
        //    System.out.println("Keys in Dictionary : " + k.nextElement());
        //}

        // remove() method :
        //System.out.println("\nRemove : " + geek.remove("123"));

        //System.out.println("\nSize of Dictionary : " + geek.size());

        //Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        //System.out.println("Enter key or key-note pair");

        //String userName = myObj.nextLine();  // Read user input
        //System.out.println("Note is: " + userName);  // Output user input

        // Print out welcome message

    }
}