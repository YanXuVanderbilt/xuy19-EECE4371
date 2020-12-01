import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientDriver {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(EmailClient.HOST_ADDRESS, EmailClient.PORT);
        DataOutputStream outToServer= new DataOutputStream(socket.getOutputStream());
        BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        EmailProtocolMessage message = new EmailProtocolMessage();
        message.putParam(EmailProtocolMessage.TYPE_KEY, EmailProtocolMessage.LOGOUT_COMMAND);

        boolean quit = false;

        EmailClient client = new EmailClient();

        outToServer.writeBytes(client.login() + "\n");
        String login_ack = serverBufferedReader.readLine();
        if (EmailClient.DEBUG) System.out.println(login_ack);
        EmailProtocolMessage login_msg = new EmailProtocolMessage(login_ack);
        if (!EmailClient.authenticate(login_msg)) {
            System.out.println("Authentication failed");
            return;
        } else {
            client.token = login_msg.getParam(EmailProtocolMessage.TOKEN_KEY);
        }
        if (!EmailClient.login_ack(login_ack)) {
            System.out.println("No response from server. Login failed.\n");
            return;
        }
        while (!quit) {
            int choice = EmailClient.displayMenu();
            switch (choice) {
                case 1 -> outToServer.writeBytes(client.sendEmail() + "\n");
                case 2 -> outToServer.writeBytes(client.retrieve() + "\n");
                case 3 -> outToServer.writeBytes(client.logOut() + "\n");
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
            if (EmailClient.DEBUG) System.out.println("Response from server: " + responseFromServer);
            EmailProtocolMessage msg = new EmailProtocolMessage(responseFromServer);
            if (!EmailClient.authenticate(msg)) {
                System.out.println("Authentication failed");
            } else {
                switch (choice) {
                    case 1 -> {
                        if (!EmailClient.sendEmail_ack(responseFromServer)) {
                            System.out.println("No response from server. Send email failed.\n");
                            return;
                        }
                    }
                    case 2 -> EmailClient.displayEmails(responseFromServer);
                    case 3 -> {
                        if (!EmailClient.logOut_ack(responseFromServer)) {
                            System.out.println("No response from server. Log out failed.\n");
                            return;
                        }
                    }
                }
            }
        }

        socket.close();
    }
}
