import javax.management.NotificationEmitter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class EmailClient {
    public static final String HOST_ADDRESS = "127.0.0.1";
    //private static final String HOST_ADDRESS = "10.66.56.222";
    public static final int PORT = 6789;
    private String userName = "";
    private String password = "";
    public static boolean DEBUG = false;
    private static final BufferedReader userBufferedReader = new BufferedReader(new InputStreamReader(System.in));


    public EmailClient() throws IOException {
    }

    public static int displayMenu() throws IOException {
        System.out.println("1. Send Mail");
        System.out.println("2. Read Mail");
        System.out.println("3. Exit");
        //System.out.flush();
        //Scanner in = new Scanner(System.in);
        String userInput = userBufferedReader.readLine();
        //String userInput = in.nextLine();
        //System.out.println(userInput);
        //in.close();
        switch (userInput) {
            case "1" -> {
                if (DEBUG) System.out.println("client:displayMenu:debug:send mail");
                return 1;
            }
            case "2" -> {
                if (DEBUG) System.out.println("client:displayMenu:debug:read mail");
                return 2;
            }
            case "3" -> {
                if (DEBUG) System.out.println("client:displayMenu:debug:exit");
                return 3;
            }
            default -> {
                System.out.println("please enter 1, 2, or 3");
                return displayMenu();
            }
        }
    }
/*
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST_ADDRESS, PORT);
        DataOutputStream outToServer= new DataOutputStream(socket.getOutputStream());
        BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        EmailProtocolMessage message = new EmailProtocolMessage();
        message.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.LOGOUT_COMMAND);

        boolean quit = false;

        outToServer.writeBytes(login() + "\n");
        String login_ack = serverBufferedReader.readLine();
        if (DEBUG) System.out.println(login_ack);
        if (!login_ack(login_ack)) {
            System.out.println("No response from server. Login failed.\n");
            return;
        }
        while (!quit) {
            int choice = displayMenu();
            switch (choice) {
                case 1 -> outToServer.writeBytes(sendEmail() + "\n");
                case 2 -> outToServer.writeBytes(retrieve() + "\n");
                case 3 -> outToServer.writeBytes(logOut() + "\n");
                default -> {
                    System.out.println("Something is very wrong here. Quitting.\n");
                    quit = true;
                }
            }
            if (choice == 3) {
                quit = true;
            }


            //BufferedReader userBufferedReader = new BufferedReader(new InputStreamReader(System.in));
            //String userInput = userBufferedReader.readLine();
            //outToServer.writeBytes("type=log_in&username=yanxu" + "\n");
            //outToServer.writeBytes("type=send_email&email=to>jbeck;from>yanxu;body>give me extra credit" + "\n");
            //outToServer.writeBytes(userInput+"\n");

            String responseFromServer = serverBufferedReader.readLine();
            if (DEBUG) System.out.println("Response from server: " + responseFromServer);

            switch (choice) {
                case 1 -> {
                    if (!sendEmail_ack(responseFromServer)) {
                        System.out.println("No response from server. Send email failed.\n");
                        return;
                    }
                }
                case 2 -> displayEmails(responseFromServer);
                case 3 -> {
                    if (!logOut_ack(responseFromServer)) {
                        System.out.println("No response from server. Log out failed.\n");
                        return;
                    }
                }
            }
        }

        socket.close();
    }
    */

    public String login() throws IOException {
        String usrName;
        String pwd;
       // Scanner in = new Scanner(System.in);

        System.out.println("Please provide your username and password: ");
        System.out.println("username: ");
        usrName = userBufferedReader.readLine();
        System.out.println("password: ");
        pwd = userBufferedReader.readLine();
        //usrName = in.nextLine();
        userName = usrName;
        password = pwd;
        EmailProtocolMessage login_msg = new EmailProtocolMessage();
        login_msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.LOGIN_COMMAND);
        login_msg.putParam(EmailProtocolMessage.PASSWORD_KEY, password);
        login_msg.putParam(EmailProtocolMessage.USERNAME_KEY, userName);
        //in.close();
        return login_msg.toString();
    }

    public String sendEmail() throws IOException {
        if (userName.equals("")) {
            return ""; // not logged in
        }
        String recipient;
        String body;
        //Scanner in = new Scanner(System.in);
        System.out.print("Who are you sending to? ");
        //recipient =
        recipient = userBufferedReader.readLine();
        System.out.println("Please enter the body of your email:");
        //body = in.nextLine();
        body = userBufferedReader.readLine();
        //System.out.print(recipient+body);
        String email = "to>" + recipient + ";from>" + userName + ";body>" + body;
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.SENDEMAIL_COMMAND);
        msg.putParam(EmailProtocolMessage.EMAIL_KEY, email);
        //in.close();
        return msg.toString();
    }

    public static String retrieve() {
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.RETRIVE_EMAIL_COMMAND);
        return msg.toString();
    }

    private static void displayEmail(String in) {
        String[] fields = in.split(";");
        for (String field : fields) {
            String[] keyValue = field.split(">");
            if (!keyValue[0].equals("to")) {
                System.out.println(field);
            }
        }
        System.out.println();
    }

    public static void displayEmails(String in) {
        EmailProtocolMessage msg = new EmailProtocolMessage(in);
        String emails_str = msg.getParam(EmailProtocolMessage.EMAILS_KEY);
        String[] emails = emails_str.split("ZZZ");
        System.out.println();
        for (String email : emails) {
            displayEmail(email);
        }
        if (DEBUG) System.out.println("client:displayEmails:debug:exited successfully");
    }

    public static String logOut() {
        EmailProtocolMessage msg = new EmailProtocolMessage();
        msg.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.LOGOUT_COMMAND);
        return msg.toString();
    }

    public static boolean login_ack(String ack) {
        EmailProtocolMessage msg = new EmailProtocolMessage(ack);
        //System.out.println("client:login_ack:debug:" + msg.getParam(EmailProtocolMessage.STATUS_KEY));
        return msg.getParam(EmailProtocolMessage.TYPE_KEY).equals(EmailProtocolMessage.LOGIN_ACK) &&
                msg.getParam(EmailProtocolMessage.STATUS_KEY).equals(EmailProtocolMessage.OK_STATUS);
    }

    public static boolean sendEmail_ack(String ack) {
        EmailProtocolMessage msg = new EmailProtocolMessage(ack);
        //System.out.println("client:login_ack:debug:" + msg.getParam(EmailProtocolMessage.STATUS_KEY));
        return msg.getParam(EmailProtocolMessage.TYPE_KEY).equals(EmailProtocolMessage.SENDEMAIL_ACK) &&
                msg.getParam(EmailProtocolMessage.STATUS_KEY).equals(EmailProtocolMessage.OK_STATUS);
    }

    public static boolean logOut_ack(String ack) {
        EmailProtocolMessage msg = new EmailProtocolMessage(ack);
        //System.out.println("client:login_ack:debug:" + msg.getParam(EmailProtocolMessage.STATUS_KEY));
        return msg.getParam(EmailProtocolMessage.TYPE_KEY).equals(EmailProtocolMessage.LOGOUT_ACK) &&
                msg.getParam(EmailProtocolMessage.STATUS_KEY).equals(EmailProtocolMessage.OK_STATUS);
    }

}
