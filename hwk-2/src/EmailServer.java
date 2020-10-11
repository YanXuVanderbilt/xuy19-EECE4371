import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class EmailServer {
    private final ServerSocket mServerSocket;
    private Socket mClientSocket;
    private boolean isLoggedIn = false;
    private final ArrayList<String> mEmails = new ArrayList<>();

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
            if (clientMessage == null) {
                continue;
            }
            System.out.println("received message: " + clientMessage);

            EmailProtocolMessage message = new EmailProtocolMessage(clientMessage);
            String response;


            switch (message.getParam(EmailProtocolMessage.TYPE_KEY)) {
                case EmailProtocolMessage.LOGIN_COMMAND -> {
                    response = login();
                    System.out.println("server: debug: logged the user in");
                }
                case EmailProtocolMessage.SENDEMAIL_COMMAND -> {
                    response = sendEmail(message);
                    System.out.println("server: debug: sent email");
                }
                case EmailProtocolMessage.RETRIVE_EMAIL_COMMAND -> {
                    response = retrieve();
                    System.out.println("server: debug: retrieved email");
                }
                case EmailProtocolMessage.LOGOUT_COMMAND -> {
                    response = logOut();
                    System.out.println("server: debug: logged user out");
                }
                default -> {
                    System.out.println("Error in command");
                    response = "ERROR";
                }
            }

            if (clientMessage.equals("quit")) {
                quit = true;
            }

            clientWriter.writeBytes(response + "\n");
            System.out.println("written response to user: " + response + "\n");
        }
    }

    private void waitForConnection() throws IOException {
        System.out.println("waiting for a connection");
        mClientSocket = mServerSocket.accept();
        System.out.println("connected!");
        InetAddress clientAddress = mClientSocket.getInetAddress();
        System.out.println("client at: " + clientAddress.toString() + ":" + mClientSocket.getPort());
    }

    private String login() {
        isLoggedIn = true;
        return login_ack();
    }

    private static String login_ack() {
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.LOGIN_ACK);
        msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.OK_STATUS);
        return msg.toString();
    }

    private String sendEmail(EmailProtocolMessage msg) {
        if (!isLoggedIn) {
            return "server:sendEmail:debug:ERRRO: User is not logged in";
        }
        mEmails.add(msg.getParam(EmailProtocolMessage.EMAIL_KEY));
        return "server:sendEmail:debug:Email sent";
    }

    private String retrieve() {
        if (!isLoggedIn) {
            return "server:retrieve:debug:ERROR: User is not logged in";
        }
        String emails = String.join("ZZZ", mEmails);
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.EMAILS_KEY);
        msg.putParam(EmailProtocolMessage.EMAILS_KEY, emails);
        System.out.println("server:retrieve:debug:" + msg.toString());
        return msg.toString();
    }

    private String logOut() {
        if (!isLoggedIn) {
            return "server:logOut:debug:ERRRO: User is not logged in";
        }
        isLoggedIn = false;
        return "server:logOut:debug:logged user out";
    }

    private static final int SERVER_PORT = 6789;

    public static void main(String argv[]) throws IOException {
        EmailServer emailServer = new EmailServer(SERVER_PORT);
        emailServer.listen();
    }
}
