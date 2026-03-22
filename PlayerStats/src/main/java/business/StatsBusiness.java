package business;

import java.util.ArrayList;

import helper.PlayerStatsInfo;
import persistence.PlayerStats_CRUD;

public class StatsBusiness {

    public ArrayList<PlayerStatsInfo> getStatsForGame(int gameId) {
        return PlayerStats_CRUD.getStatsForGame(gameId);
    }

    public PlayerStatsInfo getPlayerStatsInGame(int gameId, int playerId) {
        return PlayerStats_CRUD.getPlayerStatsInGame(gameId, playerId);
    }

    public ArrayList<PlayerStatsInfo> getStatsForPlayer(int playerId) {
        return PlayerStats_CRUD.getStatsForPlayer(playerId);
    }

    public ArrayList<PlayerStatsInfo> getStatsForPlayerName(String playerName) {
        return PlayerStats_CRUD.getStatsForPlayerName(playerName);
    }

    public ArrayList<PlayerStatsInfo> getStatsForTeams(String homeTeam, String awayTeam) {
        return PlayerStats_CRUD.getStatsForTeams(homeTeam, awayTeam);
    }

    public ArrayList<helper.GameInfo> getAllGames() {
        return PlayerStats_CRUD.getAllGames();
    }
}
