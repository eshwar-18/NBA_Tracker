package helper;

public class PlayerStatsInfo {

    private int    gameId;
    private int    playerId;
    private String playerName;
    private int    points;
    private int    assists;
    private int    rebounds;
    private int    minutesPlayed;

    public PlayerStatsInfo() {}

    public PlayerStatsInfo(int gameId, int playerId, String playerName,
                           int points, int assists, int rebounds, int minutesPlayed) {
        this.gameId       = gameId;
        this.playerId     = playerId;
        this.playerName   = playerName;
        this.points       = points;
        this.assists      = assists;
        this.rebounds     = rebounds;
        this.minutesPlayed = minutesPlayed;
    }

    public int    getGameId()       { return gameId; }
    public int    getPlayerId()     { return playerId; }
    public String getPlayerName()   { return playerName; }
    public int    getPoints()       { return points; }
    public int    getAssists()      { return assists; }
    public int    getRebounds()     { return rebounds; }
    public int    getMinutesPlayed(){ return minutesPlayed; }

    public void setGameId(int gameId)             { this.gameId = gameId; }
    public void setPlayerId(int playerId)         { this.playerId = playerId; }
    public void setPlayerName(String playerName)  { this.playerName = playerName; }
    public void setPoints(int points)             { this.points = points; }
    public void setAssists(int assists)           { this.assists = assists; }
    public void setRebounds(int rebounds)         { this.rebounds = rebounds; }
    public void setMinutesPlayed(int minutesPlayed){ this.minutesPlayed = minutesPlayed; }
}
