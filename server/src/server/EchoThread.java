package server;

import util.transferObjects.ChatMessage;
import util.transferObjects.GamePackage;
import util.Game;
import util.transferObjects.GameResult;

import java.io.*;
import java.net.Socket;

public class EchoThread extends Thread {

    private final int id;
    private String name = "unnamed";
    ObjectOutputStream oout;
    ObjectInputStream oin;


    public EchoThread(Socket clientSocket, int id) {
        this.id = id;
        try {
            oout = new ObjectOutputStream(clientSocket.getOutputStream());
            oin = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Object readObject = oin.readObject();
                if (readObject instanceof GamePackage gamePackage) {
                    switch (gamePackage.getMessage()) {
                        case "GAME_ACCEPT" -> Server.addGame(gamePackage.getSender(), gamePackage.getReceiver());
                        case "GAME_END" -> {
                            Server.sendToSpectators(gamePackage);
                            Server.removeGame(gamePackage.getSender());
                        }
                        case "GAME_TURN" -> {
                            gamePackage.setMessage("GAME_SPECTATE");
                            Server.sendToSpectators(gamePackage);
                            gamePackage.setMessage("GAME_TURN");
                        }
                        case "GAME_REMATCH" -> {
                            gamePackage.setMessage("GAME_SPECTATE");
                            Server.sendToSpectators(gamePackage);
                            gamePackage.setMessage("GAME_REMATCH");
                        }
                    }
                    Server.transferGamePackage(gamePackage);
                } else if (readObject instanceof Game game) {
                    Server.addSpectator(game);
                } else if (readObject instanceof ChatMessage message) {
                    Server.sendChatMessage(message);
                } else if (readObject instanceof GameResult gameResult) {
                    Server.saveGameResult(gameResult);
                } else if (readObject instanceof String string) {
                    if (string.equals("LEADERBOARD")) {
                        Server.sendLeaderboard(id);
                    } else {
                        this.name = string;
                        Server.checkNameUnicity(name, id);
                        Server.sendClientsUpdate();
                    }
                }
            } catch (IOException e) {
                lostConnection();
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendObject(Object object) {
        try {
            oout.reset();
            oout.writeObject(object);
            oout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return id;
    }

    public String getClientName() {
        return name;
    }

    private void lostConnection() {
        Server.clientLost(id);
    }

}
