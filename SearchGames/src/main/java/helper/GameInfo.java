package helper;

public class GameInfo {

    private int    gameId;
    private String gameDate;
    private String location;
    private String homeTeamName;
    private String awayTeamName;
    private int    homeScore;
    private int    awayScore;
    private String status;

    public GameInfo() {}

    public GameInfo(int gameId, String gameDate, String location,
                    String homeTeamName, String awayTeamName,
                    int homeScore, int awayScore, String status) {
        this.gameId       = gameId;
        this.gameDate     = gameDate;
        this.location     = location;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.homeScore    = homeScore;
        this.awayScore    = awayScore;
        this.status       = status;
    }

    public int    getGameId()       { return gameId; }
    public String getGameDate()     { return gameDate; }
    public String getLocation()     { return location; }
    public String getHomeTeamName() { return homeTeamName; }
    public String getAwayTeamName() { return awayTeamName; }
    public int    getHomeScore()    { return homeScore; }
    public int    getAwayScore()    { return awayScore; }
    public String getStatus()       { return status; }

    public void setGameId(int gameId)               { this.gameId = gameId; }
    public void setGameDate(String gameDate)         { this.gameDate = gameDate; }
    public void setLocation(String location)         { this.location = location; }
    public void setHomeTeamName(String homeTeamName) { this.homeTeamName = homeTeamName; }
    public void setAwayTeamName(String awayTeamName) { this.awayTeamName = awayTeamName; }
    public void setHomeScore(int homeScore)          { this.homeScore = homeScore; }
    public void setAwayScore(int awayScore)          { this.awayScore = awayScore; }
    public void setStatus(String status)             { this.status = status; }
}
