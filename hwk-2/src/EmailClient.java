import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class EmailClient {
    private static final String HOST_ADDRESS = "127.0.0.1";
    //private static final String HOST_ADDRESS = "10.66.56.222";
    private static final int PORT = 6789;

    public static void main(String[] args) throws IOException {
        //login();
        //sendEmail();
        EmailProtocolMessage msg = new EmailProtocolMessage("a;b;c;d");
        Socket socket = new Socket(HOST_ADDRESS, PORT);
        DataOutputStream outToServer= new DataOutputStream(socket.getOutputStream());
        BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        EmailProtocolMessage message = new EmailProtocolMessage();
        message.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.LOGOUT_COMMAND);

        boolean quit = false;

        System.out.println("Please enter a message to send");
        BufferedReader userBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String userInput = userBufferedReader.readLine();
        outToServer.writeBytes("type=log_in&username=yanxu" + "\n");
        outToServer.writeBytes("type=send_email&email=to>jbeck;from>yanxu;body>give me extra credit" + "\n");


        String responseFromServer = serverBufferedReader.readLine();
        System.out.println("Response from server: " + responseFromServer);



        //socket.close();
    }

    private static int login() {
        String usrName;
        Scanner in = new Scanner(System.in);

        System.out.print("Please provide your username: ");
        usrName = in.nextLine();
        System.out.print(usrName);
        return 0;
    }

    private static int sendEmail() {
        String recipient;
        String body;
        Scanner in = new Scanner(System.in);
        System.out.print("Who are you sending to? ");
        recipient = in.nextLine();
        System.out.println("Please enter the body of your email:");
        body = in.nextLine();
        System.out.print(recipient+body);
        return 0;
    }

    private static int retrieve() {
        return 0;
    }

    private static int logOut() {
        return 0;
    }
}
