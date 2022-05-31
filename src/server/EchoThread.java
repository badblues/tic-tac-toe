package server;

import server.packages.ClientsPackage;
import server.packages.GamePackage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
                    System.out.println("got game Package");
                    Hub.transferGamePackage(gamePackage);
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
        ClientsPackage clientsPackage = new ClientsPackage(Hub.getClients(), id);
        try {
            oout.reset();
            oout.writeObject(clientsPackage);
            oout.flush();
            System.out.println("sent update" + clientsPackage.getClients().toString());
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
        Hub.clientLost(id);
    }

}
