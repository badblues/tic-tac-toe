package server;

import tictactoe.Controller;

import java.io.IOException;
import java.net.Socket;

public class ClientController extends Thread{


    private static Socket socket;
    private final Controller controller;

    public ClientController(Controller controller) {
        this.controller = controller;
        try {
            socket = new Socket("localhost", Hub.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

    }

}
