package tictactoe;

import javafx.application.Platform;
import javafx.util.Pair;
import util.packages.ChatMessage;
import util.packages.ClientsPackage;
import util.packages.GamePackage;
import util.Game;
import util.packages.Leaderboard;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientController extends Thread{

    private final MainController mainController;
    private static ArrayList<Pair<Integer, String>> players;
    private static ArrayList<Game> games;
    private static Leaderboard leaderboard;
    private static int id;
    private static String name;
    private static Socket socket;
    ObjectInputStream oin;
    ObjectOutputStream oout;

    boolean connected = false;

    public ClientController(MainController controller) {
        this.mainController = controller;
    }

    public void run() {
        while (true) {
            if (connected) {
                try {
                    Object readObject = oin.readObject();
                    if (readObject instanceof ClientsPackage clientsPackage) {
                        players = clientsPackage.getPlayers();
                        games = clientsPackage.getGames();
                        id = clientsPackage.getId();
                        name = clientsPackage.getName();
                    } else if (readObject instanceof GamePackage gamePackage) {
                        System.out.println("got gamePackage: " + gamePackage.getMessage() + " " + gamePackage.getTurn());
                        switch (gamePackage.getMessage()) {
                            case "GAME_REQUEST":
                                mainController.showGameRequest(getClientName(gamePackage.getSender()));
                                break;
                            case "GAME_ACCEPT":
                                Platform.runLater(() -> {
                                    mainController.startOnlineGame(gamePackage.getReceiver(), gamePackage.getSender());
                                });
                                break;
                            case "GAME_DECLINE":
                                Platform.runLater(() -> {
                                    mainController.showGameDeclined();
                                });
                                break;
                            case "GAME_TURN":
                                mainController.getGamePackage(gamePackage);
                                break;
                            case "GAME_END":
                                mainController.closeOnlineGame();
                                break;
                            case "GAME_REMATCH":
                                Platform.runLater(() -> {
                                    mainController.gameRematch();
                                });
                                break;
                            case "GAME_SPECTATE":
                                Platform.runLater(() -> {
                                    mainController.spectatePackage(gamePackage);
                            });
                                break;
                        }
                    } else if (readObject instanceof ChatMessage message) {
                        mainController.getChatController().readMessage(message);
                    } else if (readObject instanceof String string) {
                        if (string.equals("TAKEN")) {
                            Platform.runLater(() -> {
                                mainController.nameTaken();
                            });
                        }
                    } else if (readObject instanceof Leaderboard table) {
                        leaderboard = table;
                        mainController.getOnlineMenuController().updateLeaderboard();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendGameRequest(String receiver) {
        int rec = -1;
        for (Pair<Integer, String> player : players)
            if (player.getValue() == receiver)
                rec = player.getKey();
        GamePackage gamePackage = new GamePackage(id, rec, "GAME_REQUEST");
        try {
            oout.reset();
            oout.writeObject(gamePackage);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameResponse(String requestSenderName, String response) {
        int requestSenderId = getClientId(requestSenderName);
        GamePackage gamePackage = new GamePackage(id, requestSenderId, response);
        try {
            oout.reset();
            oout.writeObject(gamePackage);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object object) {
        try {
            oout.reset();
            oout.writeObject(object);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer(String name, String ip) {
        try {
            socket = new Socket(ip, 5000);
            oin = new ObjectInputStream(socket.getInputStream());
            oout = new ObjectOutputStream(socket.getOutputStream());
            connected = true;
            ClientController.name = name;
            oout.reset();
            oout.writeObject(name);
            oout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromServer() {
        System.out.println("disconnecting from server");
        ClientController.name = "";
        connected = false;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Pair<Integer, String>> getPlayers() {
        return players;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public static int getClientId() {
        return id;
    }

    public static String getClientName() {
        return name;
    }

    public static int getClientId(String name) {
        for (Pair<Integer, String> player : players) {
            if (player.getValue() == name)
                return player.getKey();
        }
        return -1;
    }

    public static String getClientName(int id) {
        for (Pair<Integer, String> player : players) {
            if (player.getKey() == id)
                return player.getValue();
        }
        return "";
    }

    public static Game getGameFromId(int id) {
        for (Game game : games)
            if (game.hasId(id))
                return game;
        return null;
    }

    public static Leaderboard getLeaderboard() {
        return leaderboard;
    }

}
