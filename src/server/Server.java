package server;

import util.packages.GamePackage;
import util.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static final int PORT = 4999;
    static int lastClientId =1;
    static ArrayList<EchoThread> threadList = new ArrayList<>();
    static ArrayList<Game> games = new ArrayList<>();

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
        removeGame(id);
        sendClientsUpdate();
    }


    public static ArrayList<Integer> getClients() {
        ArrayList<Integer> array = new ArrayList<>();
        for (EchoThread thread : threadList) {
            boolean flag = true;
            for(Game pair : games)
                if (pair.hasId(thread.getClientId()))
                    flag = false;
            if (flag)
                array.add(thread.getClientId());
        }
        System.out.println("Clients Array: " + array);
        return array;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public static void addGame(int id1, int id2) {
        games.add(new Game(id1, id2));
        sendClientsUpdate();
    }

    public static void removeGame(int id1) {
        games.removeIf(pair -> (pair.hasId(id1)));
        sendClientsUpdate();
    }

    public static void addSpectator(Game game) {
        for (Game game1 : games) {
            if (game1.getId1() == game.getId1() && game1.getId2() == game.getId2()) {
                game1.setSpectators(game.getSpectators());
            }
        }
        System.out.println("added speCTATOR 1111111");
        sendClientsUpdate();
    }

    public static void sendToSpectators(GamePackage gamePackage) {
        games.forEach(game -> {
            if (game.hasId(gamePackage.getSender()))
                for (Integer spectator : game.getSpectators())
                    for (EchoThread echoThread : threadList)
                        if (echoThread.getClientId() == spectator)
                            echoThread.sendGamePackage(gamePackage);
        });
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
