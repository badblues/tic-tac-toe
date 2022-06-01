package server;

import server.packages.GamePackage;
import util.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static final int PORT = 4999;
    static int lastClientId =1;
    static ArrayList<EchoThread> threadList = new ArrayList<>();
    static ArrayList<Game> gamesList = new ArrayList<>();

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
            System.out.println("new connect");
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
        for (EchoThread thread : threadList) {
            boolean flag = true;
            for(Game pair : gamesList)
                if (pair.hasId(thread.getClientId()))
                    flag = false;
            if (flag)
                array.add(thread.getClientId());
        }
        System.out.println("Clients Array: " + array);
        return array;
    }

    public static void addGame(int id1, int id2) {
        gamesList.add(new Game(id1, id2));
        sendClientsUpdate();
        System.out.println("game list: " + gamesList);
    }

    public static void removeGame(int id1, int ignoredId2) {
        gamesList.removeIf(pair -> (pair.hasId(id1)));
        sendClientsUpdate();
        System.out.println("game list: " + gamesList);
    }

    private static void sendClientsUpdate() {
        for (EchoThread thread : threadList) {
            thread.updateClientsList();
        }
    }

    public static void transferGamePackage(GamePackage gamePackage) {
        for (EchoThread thread : threadList)
            if (thread.getClientId() == gamePackage.getReceiver())
                thread.sendGamePackage(gamePackage);
    }

}
