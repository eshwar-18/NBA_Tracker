package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.PlayerStatsInfo;

public class PlayerStats_CRUD {

    private static final String BASE_SELECT =
        "SELECT ps.game_id, ps.player_id, p.name, ps.points, ps.assists, ps.rebounds, ps.minutes_played " +
        "FROM PlayerStats ps " +
        "JOIN Players p ON ps.player_id = p.player_id ";

    // Static mapping from game_id -> { homeTeamName (lower), awayTeamName (lower) }
    // Mirrors the Games + Teams data in searchgames_db so team-name searches work here too
    private static final Map<Integer, String[]> GAME_TEAMS = new HashMap<>();
    static {
        GAME_TEAMS.put(101, new String[]{"bucks",    "nets"});
        GAME_TEAMS.put(102, new String[]{"nets",     "bulls"});
        GAME_TEAMS.put(103, new String[]{"warriors", "heat"});
        GAME_TEAMS.put(104, new String[]{"nets",     "celtics"});
    }

    public static ArrayList<PlayerStatsInfo> getStatsForGame(int gameId) {
        ArrayList<PlayerStatsInfo> stats = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE ps.game_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) stats.add(mapRow(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }

    public static PlayerStatsInfo getPlayerStatsInGame(int gameId, int playerId) {
        String sql = BASE_SELECT + "WHERE ps.game_id = ? AND ps.player_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            ps.setInt(2, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static ArrayList<PlayerStatsInfo> getStatsForPlayer(int playerId) {
        ArrayList<PlayerStatsInfo> stats = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE ps.player_id = ? ORDER BY ps.game_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) stats.add(mapRow(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }

    public static ArrayList<PlayerStatsInfo> getStatsForPlayerName(String playerName) {
        ArrayList<PlayerStatsInfo> stats = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE LOWER(p.name) LIKE LOWER(?) ORDER BY ps.game_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + playerName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) stats.add(mapRow(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }

    /**
     * Returns all player stats for games whose home/away team names match the
     * given strings (partial, case-insensitive). Either parameter may be blank
     * to match any team in that slot.
     */
    public static ArrayList<PlayerStatsInfo> getStatsForTeams(String homeTeam, String awayTeam) {
        ArrayList<PlayerStatsInfo> stats = new ArrayList<>();

        String home = (homeTeam != null) ? homeTeam.trim().toLowerCase() : "";
        String away = (awayTeam != null) ? awayTeam.trim().toLowerCase() : "";

        // Resolve matching game IDs using the static map (avoids cross-DB join)
        List<Integer> matchingIds = new ArrayList<>();
        for (Map.Entry<Integer, String[]> entry : GAME_TEAMS.entrySet()) {
            String gh = entry.getValue()[0]; // already lowercase
            String ga = entry.getValue()[1];
            boolean homeMatch = home.isEmpty() || gh.contains(home) || home.contains(gh);
            boolean awayMatch = away.isEmpty() || ga.contains(away) || away.contains(ga);
            if (homeMatch && awayMatch) matchingIds.add(entry.getKey());
        }

        if (matchingIds.isEmpty()) return stats;

        // Build safe IN clause from the hardcoded integer IDs (no user input in SQL)
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < matchingIds.size(); i++) {
            if (i > 0) inClause.append(",");
            inClause.append(matchingIds.get(i));
        }

        String sql = BASE_SELECT +
            "WHERE ps.game_id IN (" + inClause + ") ORDER BY ps.game_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) stats.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }

    private static PlayerStatsInfo mapRow(ResultSet rs) throws Exception {
        return new PlayerStatsInfo(
            rs.getInt("game_id"),
            rs.getInt("player_id"),
            rs.getString("name"),
            rs.getInt("points"),
            rs.getInt("assists"),
            rs.getInt("rebounds"),
            rs.getInt("minutes_played")
        );
    }

    public static ArrayList<helper.GameInfo> getAllGames() {
        ArrayList<helper.GameInfo> games = new ArrayList<>();
        String sql = "SELECT game_id, game_date, home_team_name, away_team_name " +
                     "FROM Games ORDER BY game_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                games.add(new helper.GameInfo(
                    rs.getInt("game_id"),
                    rs.getDate("game_date") != null ? rs.getDate("game_date").toString() : "",
                    rs.getString("home_team_name"),
                    rs.getString("away_team_name")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return games;
    }
}
