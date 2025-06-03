package de.elia.systemclasses;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private static Connection connection;

    // Replace with your actual database credentials
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "minecraft ";
    private static final String USERNAME = "soul";
    private static final String PASSWORD = "34ka(edj3!\"@E32";

    public void connect() {
        try {
            String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            System.out.println("Connected to database!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }

    public ArrayList<String> getPunishmentType(String playerName) {
      connect();
      ArrayList<String> punishmentTypes = new ArrayList<>();

      try {
        String query = "SELECT punishmentType FROM Punishments WHERE LOWER(name) = LOWER(?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, playerName);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          try {
            String pType = resultSet.getString("punishmentType");
            punishmentTypes.add(pType);
            System.out.println("Punishment type: " + pType);
          } catch (IllegalArgumentException e) {
            System.err.println("Ungültiger Punishment Type: " + e.getMessage());
          }
        }
      } catch (SQLException e) {
        System.err.println("Error retrieving punishment type: " + e.getMessage());
      }

      return punishmentTypes;
    }

    public String getIP(String playerName) {
      connect();
      String ipaddr = null;

      try {
          String query =
            "SELECT ipaddr FROM altdetector_iptable\n" +
            "WHERE playerid = (\n" +
            "SELECT id FROM altdetector_playertable \n" +
            "WHERE name = \"" + playerName + "\"\n" +
            ")\n" +
            "ORDER BY date DESC\n" +
            "LIMIT 1;";
          PreparedStatement statement = connection.prepareStatement(query);
          ResultSet resultSet = statement.executeQuery();

          if (resultSet.next()) {
            ipaddr = resultSet.getString("ipaddr");
          }

          resultSet.close();
          statement.close();
      } catch (SQLException e) {
          System.err.println("Error retrieving ip address: " + e.getMessage());
      }

      return ipaddr;
    }

  public List<OfflinePlayer> getTeamPlayerList(String teamId) {
    connect();
    List<OfflinePlayer> players = new ArrayList<>();

    try {
      String query = "SELECT playerUUID FROM BetterTeams_Players WHERE teamID = \"" + teamId + "\"";
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        try {
          String uuidString = resultSet.getString("playerUUID");
          players.add(Bukkit.getOfflinePlayer(UUID.fromString(uuidString)));
        } catch (IllegalArgumentException e) {
          System.err.println("Ungültige UUID: " + e.getMessage());
        }
      }
    } catch (SQLException e) {
      System.err.println("Error retrieving player: " + e.getMessage());
    }
    return players;
  }

  public String getTeamID(String playerUUID) {
    connect();
      String teamID = null;

    try {
      String query = "SELECT teamID FROM BetterTeams_Players WHERE playerUUID = \"" + playerUUID + "\"";
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        teamID = resultSet.getString("teamID");
      }

      resultSet.close();
      statement.close();
    } catch (SQLException e) {
      System.err.println("Error retrieving team id: " + e.getMessage());
    }
    return teamID; // Spieler nicht gefunden
  }
}
