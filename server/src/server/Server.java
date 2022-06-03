package server;

import util.packages.ChatMessage;
import util.packages.ClientsPackage;
import util.packages.GamePackage;
import util.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javafx.util.Pair;
import util.packages.GameResult;

public class Server {
    static int lastClientId = 1;
    static ArrayList<EchoThread> threads = new ArrayList<>();
    static ArrayList<Game> games = new ArrayList<>();

    private static DatabaseController databaseController;

    public static void main(String[] args) {
        databaseController = new DatabaseController();
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(5000);
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
            threads.add(thread);
            thread.start();
            sendClientsUpdate();
        }
    }

    public static void clientLost(int id) {
        threads.removeIf(ob -> (ob.getClientId() == id));
        removeGame(id);
        sendClientsUpdate();
    }


    public static ArrayList<Pair<Integer, String>> getPlayers() {
        ArrayList<Pair<Integer, String>> array = new ArrayList<>();
        for (EchoThread thread : threads) {
            boolean flag = true;
            for (Game pair : games)
                if (pair.hasId(thread.getClientId()))
                    flag = false;
            if (flag) {
                array.add(new Pair<>(thread.getClientId(), thread.getClientName()));
            }
        }
        System.out.println("Clients Array: " + array);
        return array;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public static void addGame(int id1, int id2) {
        String name1 = "", name2 = "";
        for (Pair<Integer, String> player : getPlayers()) {
            if (player.getKey() == id1)
                name1 = player.getValue();
            else if (player.getKey() == id2)
                name2 = player.getValue();
        }
        games.add(new Game(id1, id2, name1, name2));
        sendClientsUpdate();
    }

    public static void removeGame(int id1) {
        games.removeIf(pair -> (pair.hasId(id1)));
        sendClientsUpdate();
    }

    public static void addSpectator(Game game) {
        for (Game game1 : games) {
            if (game1.getSender() == game.getSender() && game1.getReceiver() == game.getReceiver()) {
                game1.setSpectators(game.getSpectators());
            }
        }
        System.out.println("added speCTATOR 1111111");
        sendClientsUpdate();
    }

    public static void checkNameUnicity(String name, int id) {
        boolean duplicate = false;
        for (EchoThread echoThread : threads)
            if (echoThread.getClientName().equals(name) && echoThread.getClientId() != id)
                duplicate = true;
        if (duplicate) {
            threads.forEach(echoThread -> {
              if (echoThread.getClientId() == id)
                  echoThread.sendObject("TAKEN");
            });
        } else {
            databaseController.addPlayer(name);
        }
    }

    public static void sendToSpectators(GamePackage gamePackage) {
        games.forEach(game -> {
            if (game.hasId(gamePackage.getSender()))
                for (Integer spectator : game.getSpectators())
                    for (EchoThread echoThread : threads)
                        if (echoThread.getClientId() == spectator)
                            echoThread.sendObject(gamePackage);
        });
    }

    public static void sendChatMessage(ChatMessage message) {
        games.forEach(game -> {
            if (game.hasId(message.getGame().getSender())) {
                for (EchoThread echoThread : threads) {
                    for (Integer spectator : game.getSpectators())
                        if (echoThread.getClientId() == spectator)
                            echoThread.sendObject(message);
                    if (echoThread.getClientId() == game.getSender() | echoThread.getClientId() == game.getReceiver())
                        echoThread.sendObject(message);
                }
            }
        });
    }

    public static void saveGameResult(GameResult gameResult) {
        databaseController.saveGameResult(gameResult);
    }

    public static void sendLeaderboard(int id) {
        for (EchoThread echoThread : threads)
            if (echoThread.getClientId() == id)
                echoThread.sendObject(databaseController.getLeaderboard());
    }

    static void sendClientsUpdate() {
        for (EchoThread thread : threads) {
            ClientsPackage clientsPackage = new ClientsPackage(getPlayers(), games, thread.getClientName(), thread.getClientId());
            thread.sendObject(clientsPackage);
        }
    }

    public static void transferGamePackage(GamePackage gamePackage) {
        for (EchoThread thread : threads)
            if (thread.getClientId() == gamePackage.getReceiver())
                thread.sendObject(gamePackage);
    }

}
