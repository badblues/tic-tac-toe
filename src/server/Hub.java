package server;

import server.packages.GamePackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Hub {

    static final int PORT = 4999;
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
            sendClientsUpdate();
        }
    }

    public static void clientLost(int id) {
        threadList.removeIf(ob -> (ob.getClientId() == id));
        sendClientsUpdate();
    }


    public static ArrayList<Integer> getClients() {
        ArrayList<Integer> array = new ArrayList<>();
        for (EchoThread thread : threadList)
            array.add(thread.getClientId());
        return array;
    }

    private static void sendClientsUpdate() {
        for (EchoThread thread : threadList)
            thread.updateClientsList();
    }

    public static void transferGamePackage(GamePackage gamePackage) {
        for (EchoThread thread : threadList)
            if (thread.getClientId() == gamePackage.getReceiver())
                thread.sendGamePackage(gamePackage);
    }

}
