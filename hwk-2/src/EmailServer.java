import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EmailServer {
    private ServerSocket mServerSocket;
    private Socket mClientSocket;

    public EmailServer(int port) throws IOException {
        mServerSocket = new ServerSocket(port);
    }

    public void listen() throws IOException {
        boolean quit = false;
        waitForConnection();

        while (!quit) {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            DataOutputStream clientWriter = new DataOutputStream(mClientSocket.getOutputStream());

            String clientMessage = clientReader.readLine();
            System.out.println("received message: " + clientMessage);

            /*
            EmailProtocolMessage message = new EmailProtocolMessage(clientMessage);

            switch (message.getParam(EmailProtocolMessage.TYPE_KEY)) {
                case EmailProtocolMessage.LOGIN_COMMAND:
                    login();
                    break;
                case EmailProtocolMessage.SENDEMAIL_COMMAND:
                    sendEmail();
                    break;
                case EmailProtocolMessage.RETRIVE_EMAIL_COMMAND:
                    retrieve();
                    break;
                case EmailProtocolMessage.LOGOUT_COMMAND:
                    logOut();
                    break;
                default:
            }
            */
            if (clientMessage.equals("quit")) {
                quit = true;
            }

            String response = "You sent me " + clientMessage.length() + " characters\n";
            clientWriter.writeBytes(response);

            System.out.println("");
        }
    }

    private void waitForConnection() throws IOException {
        System.out.println("waiting for a connection");
        mClientSocket = mServerSocket.accept();
        System.out.println("connected!");
        InetAddress clientAddress = mClientSocket.getInetAddress();
        System.out.println("client at: " + clientAddress.toString() + ":" + mClientSocket.getPort());
    }

    private int login() {
        return 0;
    }

    private int sendEmail() {
        return 0;
    }

    private int retrieve() {
        return 0;
    }

    private int logOut() {
        return 0;
    }

    private static final int SERVER_PORT = 6789;

    public static void main(String argv[]) throws IOException {
        EmailServer emailServer = new EmailServer(SERVER_PORT);
        emailServer.listen();
    }
}
