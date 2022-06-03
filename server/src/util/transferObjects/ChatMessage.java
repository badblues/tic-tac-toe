package util.transferObjects;

import util.Game;

import java.io.Serializable;

public class ChatMessage implements Serializable {

    Game game;
    String message;
    String sender;

    public ChatMessage(String sender, Game game, String message) {
        this.sender = sender;
        this.game = game;
        this.message = message;
    }

    public Game getGame() {
        return game;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

}
