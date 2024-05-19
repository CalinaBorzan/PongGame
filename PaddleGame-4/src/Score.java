import java.awt.*;

public class Score extends Rectangle {

    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    int player1;
    int player2;
    String player1Name;
    String player2Name;

    Score(int GAME_WIDTH, int GAME_HEIGHT, String player1Name, String player2Name) {
        Score.GAME_WIDTH = GAME_WIDTH;
        Score.GAME_HEIGHT = GAME_HEIGHT;
        this.player1Name=player1Name;
        this.player2Name=player2Name;
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Consolas",Font.PLAIN,40));

        g.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);

        // Draw Player 1 score and name
        // In Score.draw(Graphics g) method
        g.drawString(player1Name + ": " + String.valueOf(player1/10) + String.valueOf(player1%10), (GAME_WIDTH/2)-400, 50);
        g.drawString(player2Name + ": " + String.valueOf(player2/10) + String.valueOf(player2%10), (GAME_WIDTH/2)+50, 50);

    }
}
