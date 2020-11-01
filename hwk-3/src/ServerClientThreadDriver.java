import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClientThreadDriver {
    public static void main(String[] args) throws IOException {
        final int SERVER_PORT = 6789;
        final ServerSocket mServerSocket = new ServerSocket(SERVER_PORT);

        while (true) {
            Socket client = mServerSocket.accept();
            System.out.println("accepted");
            ServerClientThread thread = new ServerClientThread(client);
            thread.start();
        }
    }
}
