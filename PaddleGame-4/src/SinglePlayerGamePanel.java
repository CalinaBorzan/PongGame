
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SinglePlayerGamePanel extends JPanel implements Runnable{
    private boolean gameover=false;
    private long elapsedTimeInSeconds;
    long startTime=0;
    private Image backgroundImage;
    private boolean restartRequested = false;
    private GameFrame gameframe;
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    SinglePlayerPaddle paddle2;
    protected Ball ball;
    Score score;
    String playerName;

    SinglePlayerGamePanel(GameFrame gameframe,String playerName) {
        this.gameframe = gameframe;
        this.playerName=playerName;
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT, this.playerName, "Bot");
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);
        setBackground(Color.BLACK);
        gameThread = new Thread(this);
        gameThread.setDaemon(true);  // Make the thread a daemon thread
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddle2 = new SinglePlayerPaddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }
    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
        if (gameover) {
            g.setColor(Color.white);
            g.setFont(new Font("Consolas", Font.PLAIN, 40));
            g.drawString("Game Over! Player " + (score.player1 == 3 ? "1" : "2") + " wins!", GAME_WIDTH / 4, GAME_HEIGHT / 2);


        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);
        setBackground(Color.BLACK);
    }

    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
        int centerX = GAME_WIDTH / 2;
        int lowerY = GAME_HEIGHT - 50;
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        String timeString = "Time: " + elapsedTime + " seconds";
        int stringWidth = g.getFontMetrics().stringWidth(timeString);
        int drawX = centerX - stringWidth / 2;

        g.drawString(timeString, drawX, lowerY);
        if (gameover) {
            g.setColor(Color.white);
            g.setFont(new Font("Consolas", Font.PLAIN, 40));
            g.drawString("Game Over! Player " + (score.player1 == 3 ? "1" : "2") + " wins!", GAME_WIDTH / 4, GAME_HEIGHT / 2);
            g.drawString("Press Space to Restart", GAME_WIDTH / 4, GAME_HEIGHT / 2 + 50);
            g.drawString("Total Time: " + elapsedTime + " seconds", GAME_WIDTH / 4, GAME_HEIGHT / 2 + 100);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    public void move() {
        paddle1.move();
        paddle2.moveTowards((int) ball.getY());
        ball.move();
    }
    public void checkCollision() {
        if (restartRequested) {
            gameframe.showStartPanel();
            restartRequested = false;
        }
        if (ball.intersects(paddle1) || ball.intersects(paddle2)) {
            SoundPlayer.playHitSound();}
        //bounce ball off top & bottom window edges
        if(ball.y <=0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }
        //bounce ball off paddles
        if(ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; //optional for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++; //optional for more difficulty
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; //optional for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++; //optional for more difficulty
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        //stops paddles at window edges
        if(paddle1.y<=0)
            paddle1.y=0;
        if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
        if(paddle2.y<=0)
            paddle2.y=0;
        if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
        //give a player 1 point and creates new paddles & ball
        if(ball.x <=0) {
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2: "+score.player2);
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: "+score.player1);
        }
    }
    public void checkGameOver() {
        // Calculate the elapsed time at game over
        elapsedTimeInSeconds = (System.currentTimeMillis() - startTime) / 1000;

        // Create a game record; assume "Bot" or another identifier for the second player
        GameRecord gameRecord = new GameRecord(playerName, score.player1, "Bot", score.player2, elapsedTimeInSeconds);

        // Fetch user account from the account manager
        UserAccount playerAccount = UserAccount.AccountManager.getAccountByUsername(playerName);

        // Update the player's game history if the account exists
        if (playerAccount != null) {
            playerAccount.updateGameHistory(gameRecord);
            UserAccount.AccountManager.saveAccounts(); // Save updates
        }

        // Show game over message
        displayGameOverMessage();
    }

    private void displayGameOverMessage() {
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score.player1 + " - Bot score: " + score.player2,
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void run() {
        //game loop
        long lastTime = System.nanoTime();
        double amountOfTicks =40.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true) {
            long now = System.nanoTime();
            delta += (now -lastTime)/ns;
            lastTime = now;
            if(!gameover)
            {if(delta >=1) {
                move();
                checkCollision();
                repaint();
                delta--;
                if (score.player1 == 10|| score.player2 == 10) {
                    // End the game
                    gameover=true;
                    System.out.println("Game Over!");
                    checkGameOver();
                    break;
                    // You can choose to handle the game over scenario differently
                }
            }

            }
        }}
    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (gameover && e.getKeyCode() == KeyEvent.VK_SPACE) {
                restartGame();
            } else {
                paddle1.keyPressed(e);
            }
        }


        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);

        }
    }
    private void restartGame() {
        gameover = false;
        newPaddles();
        newBall();
        score.player1 = 0;
        score.player2 = 0;
        startTime = System.currentTimeMillis();
        gameThread = new Thread(this);
        gameThread.start(); // Restart the game thread
    }

    public void startGame() {
        // Implement game initialization logic here
        Thread gameThread = new Thread(this);
        gameThread.start();
        startTime = System.currentTimeMillis();
    }


}