package helper;

public class GameInfo {
    private int    gameId;
    private String gameDate;
    private String homeTeamName;
    private String awayTeamName;

    public GameInfo() {}

    public GameInfo(int gameId, String gameDate, String homeTeamName, String awayTeamName) {
        this.gameId       = gameId;
        this.gameDate     = gameDate;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
    }

    public int    getGameId()       { return gameId; }
    public String getGameDate()     { return gameDate; }
    public String getHomeTeamName() { return homeTeamName; }
    public String getAwayTeamName() { return awayTeamName; }
}