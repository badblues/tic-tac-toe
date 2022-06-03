package tictactoe.controller;

import javafx.application.Platform;
import tictactoe.data.ClientData;
import util.packages.ChatMessage;
import util.packages.ClientsPackage;
import util.packages.GamePackage;
import util.packages.Leaderboard;

import java.io.*;
import java.net.Socket;

public class ClientController extends Thread{

    private static final ClientData clientData = ClientData.getInstance();
    private final MainController mainController;
    private static Socket socket;
    ObjectInputStream oin;
    static ObjectOutputStream oout;

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
                        clientData.setPlayers(clientsPackage.getPlayers());
                        clientData.setGames(clientsPackage.getGames());
                        clientData.setId(clientsPackage.getId());
                        clientData.setName(clientsPackage.getName());
                    } else if (readObject instanceof GamePackage gamePackage) {
                        switch (gamePackage.getMessage()) {
                            case "GAME_REQUEST" ->
                                    mainController.getOnlineMenuController().showGameRequestAlert(clientData.getClientName(gamePackage.getSender()));
                            case "GAME_ACCEPT" -> Platform.runLater(() -> mainController.startOnlineGame(gamePackage.getReceiver(), gamePackage.getSender()));
                            case "GAME_DECLINE" -> Platform.runLater(mainController::showGameDeclined);
                            case "GAME_TURN" -> Platform.runLater(() -> mainController.getGameController().nextTurnOnlineGame(gamePackage));
                            case "GAME_END" -> Platform.runLater(mainController::closeOnlineGame);
                            case "GAME_REMATCH" -> Platform.runLater(mainController::gameRematch);
                            case "GAME_SPECTATE" -> Platform.runLater(() -> mainController.getGameController().readBoard(gamePackage));
                        }
                    } else if (readObject instanceof ChatMessage message) {
                        mainController.getChatController().readMessage(message);
                    } else if (readObject instanceof Leaderboard table) {
                        clientData.setLeaderboard(table);
                        mainController.getOnlineMenuController().updateLeaderboard();
                    } else if (readObject instanceof String string) {
                        if (string.equals("TAKEN")) {
                            Platform.runLater(mainController::nameTaken);
                        }
                    }
                } catch (IOException ignored) {
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

    public boolean connectToServer(String name, String ip) {
        boolean result = true;
        try {
            socket = new Socket(ip, 5000);
            oin = new ObjectInputStream(socket.getInputStream());
            oout = new ObjectOutputStream(socket.getOutputStream());
            connected = true;
            clientData.setName(name);
            oout.reset();
            oout.writeObject(name);
            oout.flush();
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    public void disconnectFromServer() {
        clientData.setName("");
        connected = false;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendObject(Object object) {
        try {
            oout.reset();
            oout.writeObject(object);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendGameRequest(String receiver) {
        int receiverId = clientData.getClientId(receiver);
        GamePackage gamePackage = new GamePackage(clientData.getThisClientId(), receiverId, "GAME_REQUEST");
        try {
            oout.reset();
            oout.writeObject(gamePackage);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameResponse(String requestSenderName, String response) {
        int requestSenderId = clientData.getClientId(requestSenderName);
        GamePackage gamePackage = new GamePackage(clientData.getThisClientId(), requestSenderId, response);
        try {
            oout.reset();
            oout.writeObject(gamePackage);
            oout.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }



}
