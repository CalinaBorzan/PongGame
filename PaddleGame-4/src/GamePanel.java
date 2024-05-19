
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.function.Consumer;

public class GamePanel extends JPanel implements Runnable{
    private boolean gameover=false;
    long startTime;
    private boolean restartRequested = false;
    private long elapsedTimeInSeconds;
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
    Paddle paddle2;
    protected Ball ball;
    Score score;
    private String player1Name;
    private String player2Name;
    public GamePanel(GameFrame gameFrame, String player1Name, String player2Name) {
        this.gameframe = gameFrame;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        newPaddles();
        newBall();
        // Initialize score with player names passed to the constructor
        System.out.println("Player 1 Name: " + player1Name); // Debug print
        System.out.println("Player 2 Name: " + player2Name);
        score = new Score(GAME_WIDTH, GAME_HEIGHT, player1Name, player2Name);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        setBackground(Color.blue);
        gameThread = new Thread(this);
        gameThread.setDaemon(true);  // Make the thread a daemon thread
        gameThread.start();}

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        setBackground(Color.blue);
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
    public void checkGameOver() {
        // Calculate the elapsed time in seconds at game over
        elapsedTimeInSeconds = (System.currentTimeMillis() - startTime) / 1000;

        // Create a game record with the final scores and elapsed time
        GameRecord gameRecord = new GameRecord(player1Name, score.player1, player2Name, score.player2, elapsedTimeInSeconds);

        // Fetch user accounts from the account manager
        UserAccount playerOne = UserAccount.AccountManager.getAccountByUsername(player1Name);
        UserAccount playerTwo = UserAccount.AccountManager.getAccountByUsername(player2Name);

        // Update each player's game history if they exist
        if (playerOne != null) {
            playerOne.updateGameHistory(gameRecord);
        }
        if (playerTwo != null) {
            playerTwo.updateGameHistory(gameRecord);
        }

        // Save the updated accounts to data.txt
        UserAccount.AccountManager.saveAccounts();

        // Show game over message or screen
        displayGameOverMessage();
    }

    private void displayGameOverMessage() {
        JOptionPane.showMessageDialog(this, "Game Over! Player 1: " + score.player1 + " - Player 2: " + score.player2,
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public void move() {
        paddle1.move();
        paddle2.move();
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
            ball.xVelocity++; //for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++; //for more difficulty
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++; // for more difficulty
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
    public void run() {
        // Initialize lastTime to the current system time in nanoseconds
        long lastTime = System.nanoTime();

        // Set the number of game updates to occur per second
        double amountOfTicks = 40.0;

        // Calculate the number of nanoseconds per update (tick)
        double ns = 1000000000 / amountOfTicks;

        // Delta is used to accumulate the time difference between each loop iteration
        double delta = 0;

        // Start the game loop
        while (true) {
            // Capture the current time in nanoseconds
            long now = System.nanoTime();

            // Accumulate the time passed since the last update in delta
            delta += (now - lastTime) / ns;

            // Update lastTime for the next loop iteration
            lastTime = now;

            // Check if it's time to update the game (delta >= 1)
            if (!gameover) {
                if (delta >= 1) {
                    // Update the positions of game elements (paddles and ball)
                    move();

                    // Check for any collisions between game elements
                    checkCollision();

                    // Redraw the game panel with updated positions
                    repaint();

                    // Reset delta after an update and render cycle
                    delta--;

                    // Check if a player has reached the winning score
                    if (score.player1 == 10 || score.player2 == 10) {
                        // Set gameover to true, ending the game loop
                        gameover = true;

                        // Log "Game Over" to the console for debugging
                        System.out.println("Game Over!");

                        // Update game history and save account data if a user is logged in
                        checkGameOver();

                        // Exit the loop
                        break;
                    }
                }
            }
        }
    }

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (gameover && e.getKeyCode() == KeyEvent.VK_SPACE) {
                restartGame();
            } else {
                paddle1.keyPressed(e);
                paddle2.keyPressed(e);
            }}

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
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
        Thread gameThread = new Thread(this);
        gameThread.start();
        startTime = System.currentTimeMillis();
    }
}

class StartPanel extends JPanel {

    GameFrame frame;

    StartPanel(GameFrame frame) {
        this.frame = frame;

        JButton startButton = new JButton("Press me to Start");
        JButton createAccountButton = new JButton("Create Account");
        JButton loginButton = new JButton("Start Game");
        JButton historyButton = new JButton("View History");
        JButton viewRankingsButton = new JButton("View Rankings");
        JButton manageFriendsButton = new JButton("Manage Friends");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showLoginPanel();
            }
        });

        historyButton.addActionListener(e -> {
            frame.setViewingHistoryAfterLogin(true); // Indicate that the user wants to view history after login
            frame.setViewingRankingsAfterLogin(false); // Ensure this is false
            frame.setViewingFriendsAfterLogin(false);
            Consumer<UserAccount> afterLogin = user -> frame.showHistoryPanel(user);
            frame.changePanel(new LoginPanelWithAction(frame, afterLogin));
        });

        viewRankingsButton.addActionListener(e -> {
            frame.setViewingRankingsAfterLogin(true); // Indicate that the user wants to view rankings after login
            frame.setViewingHistoryAfterLogin(false); // Ensure this is false
            frame.setViewingFriendsAfterLogin(false);
            Consumer<UserAccount> afterLogin = user -> frame.showRankingsPanel();
            frame.changePanel(new LoginPanelWithAction(frame, afterLogin));
        });


        manageFriendsButton.addActionListener(e -> {
            frame.setViewingFriendsAfterLogin(true); // Correct the capitalization here
            frame.setViewingRankingsAfterLogin(false);
            frame.setViewingHistoryAfterLogin(false);
            Consumer<UserAccount> afterLogin = user -> frame.showFriendsPanel();
            frame.changePanel(new LoginPanelWithAction(frame, afterLogin));
        });


        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showAccountCreationPanel();
            }
        });
        setBackground(Color.blue);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 30, 30, 30);

        add(createAccountButton, gbc);
        gbc.gridy = 1;
        add(loginButton, gbc);
        gbc.gridy = 2;
        add(historyButton, gbc);
        gbc.gridy = 3;
        add(viewRankingsButton, gbc);
        gbc.gridy = 4;
        add(manageFriendsButton, gbc);
        setFocusable(true);

    }
}
