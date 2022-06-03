package server;

import util.packages.GameResult;
import util.packages.Leaderboard;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseController {

    Statement statement;
    String tablename;

    public DatabaseController() {
        try {
            Properties props = new Properties();
            FileReader reader = new FileReader("server/resources/db.properties");
            props.load(reader);
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            tablename = props.getProperty("tablename");
            Connection connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlayer(String name) {
        try {
            statement.executeUpdate("USE tictactoe");
            ResultSet resultSet = statement.executeQuery("SELECT EXISTS(SELECT * FROM " + tablename + " WHERE player_name = '" + name + "');");
            boolean newName = true;
            if (resultSet.next())
                if (resultSet.getString(1).equals("1"))
                    newName = false;
            if (newName) {
                statement.executeUpdate("insert into " + tablename + " values ('" + name + "', 0, 0, 0, 0);");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveGameResult(GameResult gameResult) {
        savePlayerResult(gameResult.getName1(), gameResult.getResult1());
        savePlayerResult(gameResult.getName2(), gameResult.getResult2());
    }

    private void savePlayerResult(String name, String result) {
        try {
            statement.executeUpdate("USE tictactoe");
            ResultSet resultSet = statement.executeQuery("SELECT * from " + tablename + " WHERE player_name = '" + name + "';");
            if (resultSet.next()) {
                int games = resultSet.getInt(2);
                int wins = resultSet.getInt(3);
                int loses = resultSet.getInt(4);
                games++;
                if (result.equals("WIN"))
                    wins++;
                else if (result.equals("LOSE"))
                    loses++;
                String str = "UPDATE " + tablename + " SET games = " + games + ", wins = " + wins + ", loses = " + loses +
                        ", winrate = " + (float)wins/(float)games * 100 + " WHERE player_name = '" + name + "';";
                statement.executeUpdate(str);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Leaderboard getLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        try {
            statement.executeUpdate("USE tictactoe");
            ResultSet resultSet = statement.executeQuery("SELECT * from " + tablename + ";" );
            while(resultSet.next()) {
                leaderboard.addPlayerRow(resultSet.getString(1));
                leaderboard.addGamesRow(resultSet.getInt(2));
                leaderboard.addWinsRow(resultSet.getInt(3));
                leaderboard.addLosesRow(resultSet.getInt(4));
                leaderboard.addWinrateRow(resultSet.getFloat(5));
                leaderboard.setRowsNumber(leaderboard.getRowsNumber() + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return leaderboard;
    }
}
