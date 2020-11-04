import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;

public class ServerClientThread extends Thread {

    //private final ServerSocket mServerSocket;
    private Socket mClientSocket;
    private boolean isLoggedIn = false;
    public static final ArrayList<String> mEmails = new ArrayList<>();
    public static final Hashtable<String, String> users = new Hashtable<String, String>();
    private final Hashtable<String, String> tokens = new Hashtable<>();
    private String userName = "";
    private int nextToken = 0;
    public static final boolean DEBUG = true;

    public ServerClientThread(int port) throws IOException {
        //mServerSocket = new ServerSocket(port);
    }

    public ServerClientThread(Socket cS) throws IOException {
        //mServerSocket = new ServerSocket(SERVER_PORT);
        mClientSocket = cS;
    }

    public ServerClientThread() throws IOException {
        //mServerSocket = new ServerSocket(SERVER_PORT);
    }

    public void listen() throws IOException {
        boolean quit = false;
        //waitForConnection();

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
                    userName = message.getParam(EmailProtocolMessage.USERNAME_KEY);
                    response = login(message);
                    if (DEBUG) System.out.println("server: debug: logged the user in");
                }
                case EmailProtocolMessage.SENDEMAIL_COMMAND -> {
                    response = sendEmail(message);
                    if (DEBUG) System.out.println("server: debug: sent email");
                }
                case EmailProtocolMessage.RETRIVE_EMAIL_COMMAND -> {
                    response = retrieve(message);
                    if (DEBUG) System.out.println("server: debug: retrieved email");
                }
                case EmailProtocolMessage.LOGOUT_COMMAND -> {
                    response = logOut(message);
                    quit = true;
                    if (DEBUG) System.out.println("server: debug: logged user out");
                }
                default -> {
                    System.out.println("Error in command");
                    response = "ERROR";
                }
            }
            clientWriter.writeBytes(response + "\n");
            System.out.println("written response to user: " + response + "\n");
        }
        //mServerSocket.close();
        //listen();
    }

    private Socket waitForConnection() throws IOException {
        System.out.println("waiting for a connection");
        //mClientSocket = mServerSocket.accept();
        System.out.println("connected!");
        InetAddress clientAddress = mClientSocket.getInetAddress();
        System.out.println("client at: " + clientAddress.toString() + ":" + mClientSocket.getPort());
        return mClientSocket;
    }

    private boolean authenticate(EmailProtocolMessage msg) {
        String token;
        boolean ans;
        try {
            token = msg.getParam(EmailProtocolMessage.TOKEN_KEY);
            ans = tokens.get(userName).equals(token);
        } catch (NullPointerException exception) {
            if (DEBUG) System.out.println("authenticate:debug:exception caught");
            return false;
        }
        return ans;
    }

    private String login(EmailProtocolMessage msg) {
        String usrn = msg.getParam(EmailProtocolMessage.USERNAME_KEY);
        String pwd = msg.getParam(EmailProtocolMessage.PASSWORD_KEY);
        if (users.contains(usrn)) {
            if (users.get(usrn).equals(pwd)) {
                isLoggedIn = true;
                return login_ack(true, usrn);
            } else {
                return login_ack(false, usrn);
            }
        }
        users.put(usrn, pwd);
        isLoggedIn = true;
        return login_ack(true, usrn);
    }

    private String login_ack(boolean success, String username) {
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.LOGIN_ACK);
        if (success) {
            msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.OK_STATUS);
            msg.putParam(EmailProtocolMessage.TOKEN_KEY, String.valueOf(nextToken));
            tokens.put(username, String.valueOf(nextToken));
            nextToken += 1;
        } else {
            msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.FAILED_STATUS);
            msg.putParam(EmailProtocolMessage.TOKEN_KEY, "-1");
        }
        return msg.toString();
    }

    private String sendEmail(EmailProtocolMessage msg) {
        if (!isLoggedIn) {
            return "ERROR: User is not logged in";
        }
        boolean success = authenticate(msg);
        if (!success) {
            return sendEmail_ack(success);
        }
        mEmails.add(msg.getParam(EmailProtocolMessage.EMAIL_KEY));
        return sendEmail_ack(success);
    }

    private String sendEmail_ack(boolean success) {
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.SENDEMAIL_ACK);
        if (success) {
            msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.OK_STATUS);
        } else {
            msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.INVALID_TOKEN_STATUS);
        }
        return msg.toString();
    }

    private String retrieve(EmailProtocolMessage mg) {
        if (!isLoggedIn) {
            return "ERROR: User is not logged in";
        }
        if (!authenticate(mg)) {
            EmailProtocolMessage msg = new EmailProtocolMessage();
            msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.INVALID_TOKEN_STATUS);
            return msg.toString();
        }
        ArrayList<String> userEmails = new ArrayList<>();
        for (String email : mEmails) {
            String[] fields = email.split(";");
            for (String field : fields) {
                String[] keyValue = field.split(">");
                if (keyValue[0].equals("to")
                        && keyValue[1].equals(userName)) {
                    userEmails.add(email);
                    break;
                }
            }
        }
        String emails;
        if (userEmails.isEmpty()) {
            emails = "ZZZ";
        } else {
            emails = String.join("ZZZ", userEmails);
        }
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.EMAILS_KEY);
        msg.putParam(EmailProtocolMessage.EMAILS_KEY, emails);
        msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.OK_STATUS);
        if (DEBUG) System.out.println("server:retrieve:debug:" + msg.toString());
        return msg.toString();
    }

    private String logOut(EmailProtocolMessage msg) {
        if (!isLoggedIn) {
            return "ERROR: User is not logged in";
        }
        boolean success = authenticate(msg);
        if (!success) {
            return logOut_ack(success);
        }
        isLoggedIn = false;
        tokens.remove(userName);
        return logOut_ack(success);
    }

    private String logOut_ack(boolean success) {
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.LOGOUT_ACK);
        if (success) {
            msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.OK_STATUS);
        } else {
            msg.putParam(EmailProtocolMessage.STATUS_KEY, EmailProtocolMessage.INVALID_TOKEN_STATUS);
        }
        return msg.toString();
    }

    private static final int SERVER_PORT = 6789;

    public void run(){
        try{
            listen();
        }catch(Exception ex){
            System.out.println(ex);
        }//finally{
         //   System.out.println("Client -" + " exit!! ");
        //}
    }

}
