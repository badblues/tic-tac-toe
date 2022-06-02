package server;

import javafx.application.Platform;
import util.packages.ClientsPackage;
import util.packages.GamePackage;
import tictactoe.MainController;
import util.Game;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientController extends Thread{

    private final MainController mainController;
    private static ArrayList<Integer> clients;
    private static ArrayList<Game> games;
    private static int id;
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
                        clients = clientsPackage.getClients();
                        games = clientsPackage.getGames();
                        id = clientsPackage.getId();
                    } else if (readObject instanceof GamePackage gamePackage) {
                        System.out.println("got gamePackage: " + gamePackage.getMessage());
                        switch (gamePackage.getMessage()) {
                            case "GAME_REQUEST":
                                mainController.showGameRequest(gamePackage.getSender());
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

    public void sendGameRequest(int receiver) {
        GamePackage gamePackage = new GamePackage(id, receiver, "GAME_REQUEST");
        try {
            oout.reset();
            oout.writeObject(gamePackage);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameResponse(int requestSenderId, String response) {
        GamePackage gamePackage = new GamePackage(id, requestSenderId, response);
        try {
            oout.reset();
            oout.writeObject(gamePackage);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGamePackage(GamePackage gamePackage) {
        try {
            oout.reset();
            oout.writeObject(gamePackage);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameSpectator(Game game) {
        try {
            oout.reset();
            oout.writeObject(game);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", Server.PORT);
            oin = new ObjectInputStream(socket.getInputStream());
            oout = new ObjectOutputStream(socket.getOutputStream());
            connected = true;
            System.out.println("connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromServer() {
        System.out.println("disconnecting from server");
        connected = false;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Integer> getClients() {
        return clients;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public static int getClientId() {
        return id;
    }

}
