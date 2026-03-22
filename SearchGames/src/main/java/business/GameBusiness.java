package business;

import helper.GameInfo;
import persistence.Game_CRUD;
import java.util.ArrayList;

public class GameBusiness {

    public ArrayList<GameInfo> searchByDate(String date) {
        return Game_CRUD.searchByDate(date);
    }

    public ArrayList<GameInfo> searchByTeam(String teamName) {
        return Game_CRUD.searchByTeam(teamName);
    }

    public ArrayList<GameInfo> searchByPlayer(String playerName) {
        return Game_CRUD.searchByPlayer(playerName);
    }
}
