import java.awt.*;
import java.io.Serializable;

public class SinglePlayerPaddle extends Paddle {

    SinglePlayerPaddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT, id);
    }

    // Override move method to implement AI for single-player mode

    public void moveTowards(int moveToY) {
        int centerY = y + height / 2;

        if (Math.abs(centerY - moveToY) > speed) {
            if (centerY > moveToY) {
                y -= speed;
            }
            if (centerY < moveToY) {
                y += speed;
            }
        }
    }

    // Override paint method to use a different color for the computer-controlled paddle

    public void paint(Graphics g) {
        g.setColor(Color.GREEN);  // Use a different color for the computer-controlled paddle
        g.fillRect(x, y, width, height);
    }
}


