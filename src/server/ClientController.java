package server;

import javafx.application.Platform;
import server.packages.ClientsPackage;
import server.packages.GamePackage;
import tictactoe.MainController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientController extends Thread{

    private final MainController mainController;
    private static ArrayList<Integer> clients;
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
                        id = clientsPackage.getId();
                        mainController.clientsUpdate();
                    } else if (readObject instanceof GamePackage gamePackage) {
                        System.out.println("got gamePackage: " + gamePackage.getMessage());
                        if (gamePackage.getMessage().equals("GAME_REQUEST")) {
                            mainController.showGameRequest(gamePackage.getSender());
                        } else if (gamePackage.getMessage().equals("GAME_ACCEPT")) {
                            Platform.runLater(() -> {
                                mainController.startOnlineGame(gamePackage.getReceiver(), gamePackage.getSender());
                            });
                        } else if (gamePackage.getMessage().equals("GAME_DECLINE")) {
                            mainController.showGameDeclined();
                        } else if (gamePackage.getMessage().equals("GAME_TURN")) {
                            mainController.getGamePackage(gamePackage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
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


    public void connectToServer() {
        try {
            socket = new Socket("localhost", Hub.PORT);
            oin = new ObjectInputStream(socket.getInputStream());
            oout = new ObjectOutputStream(socket.getOutputStream());
            connected = true;
            System.out.println("connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromServer() {
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

    public static int getClientId() {
        return id;
    }

}
