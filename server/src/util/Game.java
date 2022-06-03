package util;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private int sender;
    private int receiver;
    private String senderName;
    private String receiverName;
    private ArrayList<Integer> spectators = new ArrayList<>();

    public Game(int senderId, int receiverId) {
        this.sender = senderId;
        this.receiver = receiverId;
    }

    public Game(int senderId, int receiverId, String senderName, String receiverName)
    {
        this.sender = senderId;
        this.receiver = receiverId;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setSpectators(ArrayList<Integer> spectators) {
        this.spectators = spectators;
    }

    public ArrayList<Integer> getSpectators() {
        return spectators;
    }

    public boolean hasId(int id) {
        return (id == sender | id == receiver);
    }

    public String getString() {
        return senderName + " vs " + receiverName;
    }

}