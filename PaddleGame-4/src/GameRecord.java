public class GameRecord {
    private String player1Name;
    private String player2Name;
    private int player1Score;
    private int player2Score;
    private long elapsedTimeInSeconds;

    public GameRecord(String player1Name, int player1Score, String player2Name, int player2Score, long elapsedTimeInSeconds) {
        this.player1Name = player1Name;
        this.player1Score = player1Score;
        this.player2Name = player2Name;
        this.player2Score = player2Score;
        this.elapsedTimeInSeconds = elapsedTimeInSeconds;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public long getElapsedTimeInSeconds() {
        return elapsedTimeInSeconds;
    }
}
