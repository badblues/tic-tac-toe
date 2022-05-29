package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Hub {

    static final int PORT = 5000;
    static int lastClientId =1;
    static ArrayList<EchoThread> threadList = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                assert serverSocket != null : "Not connected";
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            EchoThread thread = new EchoThread(socket, lastClientId++);
            threadList.add(thread);
            thread.start();
            System.out.println("new client connected");
        }
    }

}
