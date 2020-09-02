import java.util.*;

public class Notes {
    public static void main(String[] args) {
        // TODO: raw use of parameterized class "Dictionary" or "Hashtable"?
        Dictionary geek = new Hashtable();

        System.out.println("Welcome to the String Test Program!");
        System.out.println("This demonstrates how to input strings from the console.");

        // Define a scanner for user input

        Scanner userInput = new Scanner(System.in);

        System.out.println("Type in a line of text (a String) or \"quit\" to end:");

        while (userInput.hasNextLine()) {
            String line = userInput.nextLine();
            if (line.equals("quit")) {
                break;
            } else {
                System.out.println("You input: " + line);
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