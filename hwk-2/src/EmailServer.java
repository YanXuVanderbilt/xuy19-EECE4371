import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    private ServerSocket mServerSocket;
    private Socket mClientSocket;

    public TcpServer(int port) throws IOException {
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

            EmailProtoclMessage message = new EmailProtoclMessage(clientMessage);

            switch (message.getParam(EmailProtoclMessage.TYPE_KEY)) {
                case EmailProtoclMessage.LOGIN_COMMAND:
                    login();
                    break;
                case EmailProtoclMessage.SENDEMAIL_COMMAND:
                    sendEmail();
                    break;
                case EmailProtoclMessage.RETRIVE_EMAIL_COMMAND:
                    retrieve();
                    break;
                case EmailProtoclMessage.LOGOUT_COMMAND:
                    logOut()
                    break;
                default:
            }

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

    }

    private int sendEmail() {

    }

    private int retrieve() {

    }

    private int logOut() {

    }
}
