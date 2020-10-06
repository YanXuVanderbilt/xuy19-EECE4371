import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpClient {
    private static final String HOST_ADDRESS = "127.0.0.1";
    private static final int PORT = 6789;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST_ADDRESS, PORT);
        DataOutputStream outToServer= new DataOutputStream(socket.getOutputStream());
        BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        EmailProtoclMessage message = new EmailProtoclMessage();
        message.putParam(EmailProtoclMessage.TYPE_KEY, EmailProtoclMessage.LOGOUT_COMMAND);

        boolean quit = false;

        System.out.println("Please enter a message to send");
        BufferedReader userBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String userInput = userBufferedReader.readLine();
        outToServer.writeBytes(userInput + "\n");

        String responseFromServer = serverBufferedReader.readLine();
        System.out.println("Response from server: " + responseFromServer);

        socket.close();
    }

    private static int login() {
        return 0;
    }

    private static int sendEmail() {
        return 0;
    }

    private static int retrieve() {
        return 0;
    }

    private static int logOut() {
        return 0;
    }
}
