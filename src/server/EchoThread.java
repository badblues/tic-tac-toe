package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class EchoThread extends Thread {

    private Socket socket;
    private final int id;

    public EchoThread(Socket clientSocket, int id) {
        socket = clientSocket;
        this.id = id;
    }

    public void run() {
        while(true) {
            try {
                InputStream inputStream = socket.getInputStream();
                DataInputStream din = new DataInputStream(inputStream);

            } catch (IOException e) {
                break;
            }
        }
    }

}
