import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Morton on 1/28/16.
 */
public class Server implements Runnable {
    public final static int DEFAULT_PORT = 10000;

    ServerSocket ss;
    Socket socket;
    int code;
    boolean onSystemExit;

    public Server() {
        try {
            ss = new ServerSocket(Server.DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onSystemExit = false;
        code = renewCode();
    }

    public int renewCode() {
        Random random = new Random();
        return random.nextInt(9999) + 1;
    }

    public void run() {
        try {
            while (!onSystemExit) {
                socket = ss.accept();
                System.out.println("Incoming transmission detected. Incoming IP address: " +
                        socket.getInetAddress().toString().substring(1));

            }
        } catch (InterruptedIOException e1) {
            onSystemExit = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
