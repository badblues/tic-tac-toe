package server;

import server.packages.ClientsPackage;
import server.packages.GamePackage;
import util.Game;

import java.io.*;
import java.net.Socket;

public class EchoThread extends Thread {

    private final int id;
    private final Socket socket;
    ObjectOutputStream oout;
    ObjectInputStream oin;


    public EchoThread(Socket clientSocket, int id) {
        socket = clientSocket;
        this.id = id;
        try {
            oout = new ObjectOutputStream(socket.getOutputStream());
            oin = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Object readObject = oin.readObject();
                if (readObject instanceof GamePackage gamePackage) {
                    switch(gamePackage.getMessage()) {
                        case "GAME_ACCEPT":
                            Server.addGame(gamePackage.getSender(), gamePackage.getReceiver());
                            break;
                        case "GAME_END":
                            Server.removeGame(gamePackage.getSender(), gamePackage.getReceiver());
                            break;
                        case "GAME_TURN":
                            Server.sendToSpectators(gamePackage);
                    }
                    System.out.println("got game Package:" + gamePackage.getMessage());
                    Server.transferGamePackage(gamePackage);
                } else if (readObject instanceof Game game) {
                    Server.addSpectator(game);
                }
            } catch (IOException e) {
                lostConnection();
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateClientsList() {
        System.out.println("sent clients update");
        ClientsPackage clientsPackage = new ClientsPackage(Server.getClients(), Server.getGames(), id);
        try {
            oout.reset();
            oout.writeObject(clientsPackage);
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
            System.out.println("sent gamePackage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return id;
    }

    private void lostConnection() {
        Server.clientLost(id);
    }

}
