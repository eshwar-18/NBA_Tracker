package persistence;

import helper.GameInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Game_CRUD {

    // JOIN Teams so every result carries human-readable team names
    private static final String BASE_SELECT =
        "SELECT g.game_id, g.game_date, g.location, " +
        "ht.name AS home_team_name, at.name AS away_team_name, " +
        "g.home_score, g.away_score, g.status " +
        "FROM Games g " +
        "JOIN Teams ht ON g.home_team_id = ht.team_id " +
        "JOIN Teams at ON g.away_team_id = at.team_id ";

    public static ArrayList<GameInfo> searchByDate(String date) {
        ArrayList<GameInfo> games = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE g.game_date = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) games.add(mapRow(rs));
            }
        } catch (Exception e) {
            System.out.println("Game_CRUD.searchByDate error: " + e);
        }
        return games;
    }

    // teamName — partial, case-insensitive match against either home or away team
    public static ArrayList<GameInfo> searchByTeam(String teamName) {
        ArrayList<GameInfo> games = new ArrayList<>();
        String sql = BASE_SELECT +
            "WHERE LOWER(ht.name) LIKE LOWER(?) OR LOWER(at.name) LIKE LOWER(?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = "%" + teamName + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) games.add(mapRow(rs));
            }
        } catch (Exception e) {
            System.out.println("Game_CRUD.searchByTeam error: " + e);
        }
        return games;
    }

    // playerName — partial, case-insensitive match against player name
    public static ArrayList<GameInfo> searchByPlayer(String playerName) {
        ArrayList<GameInfo> games = new ArrayList<>();
        String sql =
            "SELECT g.game_id, g.game_date, g.location, " +
            "ht.name AS home_team_name, at.name AS away_team_name, " +
            "g.home_score, g.away_score, g.status " +
            "FROM Games g " +
            "JOIN Teams ht ON g.home_team_id = ht.team_id " +
            "JOIN Teams at ON g.away_team_id = at.team_id " +
            "JOIN PlayerGameAppearances pga ON g.game_id = pga.game_id " +
            "JOIN Players p ON pga.player_id = p.player_id " +
            "WHERE LOWER(p.name) LIKE LOWER(?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + playerName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) games.add(mapRow(rs));
            }
        } catch (Exception e) {
            System.out.println("Game_CRUD.searchByPlayer error: " + e);
        }
        return games;
    }

    private static GameInfo mapRow(ResultSet rs) throws Exception {
        return new GameInfo(
            rs.getInt("game_id"),
            rs.getDate("game_date") != null ? rs.getDate("game_date").toString() : "",
            rs.getString("location"),
            rs.getString("home_team_name"),
            rs.getString("away_team_name"),
            rs.getInt("home_score"),
            rs.getInt("away_score"),
            rs.getString("status")
        );
    }
}
