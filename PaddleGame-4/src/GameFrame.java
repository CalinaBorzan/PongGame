import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private boolean viewingHistoryAfterLogin = false;
   private boolean ViewingFriendsAfterLogin=false;
    StartPanel startPanel;
    private LoginPanel loginPanel;

    GamePanel gamePanel;
    SinglePlayerGamePanel gamePanelSingle;
    HistoryPanel historyPanel;
    RankingsPanel rankingPanel;
    ManageFriendsPanel friendsPanel;
    private boolean viewingRankingsAfterLogin=false;

    GameFrame() {
        startPanel = new StartPanel(this);
        this.add(startPanel);
        this.setTitle("Paddle Game");
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 800);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        loginPanel = new LoginPanel(this);
    }

    public void showAccountCreationPanel() {
        this.remove(startPanel);
        this.add(new AccountCreationPanel(this));
        this.setLocationRelativeTo(null);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }


    public void startGame(boolean twoPlayerMode, String player1Name, String player2Name) {
        if (twoPlayerMode) {
            gamePanel = new GamePanel(this, player1Name, player2Name);
            this.remove(loginPanel);
            this.add(gamePanel);
            this.setResizable(true);  // Allow resizing for the game panel
            this.pack();  // Adjust the size of the window
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            this.revalidate();
            this.repaint();
            setBackground(Color.BLUE);
            gamePanel.requestFocusInWindow();
            gamePanel.startGame(); // Start the game
        } else {

            gamePanelSingle = new SinglePlayerGamePanel(this, player1Name);
            this.remove(loginPanel);
            this.add(gamePanelSingle);
            this.setResizable(true);  // Allow resizing for the game panel
            this.pack();  // Adjust the size of the window
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            this.revalidate();
            this.repaint();
            setBackground(Color.BLUE);
            gamePanelSingle.requestFocusInWindow();
            gamePanelSingle.startGame(); // Start the game
        }
    }

    private UserAccount currentUser;

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public void setUser(UserAccount user) {
        this.currentUser = user;
        if (viewingHistoryAfterLogin) {
            showHistoryPanel(user);
            viewingHistoryAfterLogin = false;
        } else if (viewingRankingsAfterLogin) {
            showRankingsPanel();
            viewingRankingsAfterLogin = false;
        } else if (ViewingFriendsAfterLogin) { // Make sure this condition is correctly checked
            showFriendsPanel();
            ViewingFriendsAfterLogin = false; // Reset the flag
        }
    }

    public void showHistoryPanel(UserAccount user) {
        if (currentUser != null) {
            historyPanel = new HistoryPanel(currentUser, this); // Pass the GameFrame reference
            this.getContentPane().removeAll();
            this.getContentPane().add(historyPanel);
            historyPanel.setPreferredSize(new Dimension(1000, 1000)); // Set your desired size
            this.revalidate();
            this.setLocationRelativeTo(null);
            this.repaint();
        } else {
            System.out.println("No user is logged in.");
        }
    }

    public void showStartPanel() {
        // Check if gamePanel is not null and has been added before trying to remove it
        if (gamePanel != null) {
            getContentPane().remove(gamePanel);
        }

        this.revalidate();

        startPanel = new StartPanel(this); // Consider if you need to reinitialize every time
        getContentPane().removeAll(); // Remove all components
        getContentPane().add(startPanel);
        this.setLocationRelativeTo(null);
        setResizable(true);
        pack();
        revalidate();
        repaint();
        startPanel.requestFocusInWindow();
    }

    public void showLoginPanel() {
        this.getContentPane().removeAll(); // Remove the current panel
        this.getContentPane().add(loginPanel); // Add the LoginPanel
        this.setLocationRelativeTo(null);
        updateAndRepaintFrame();
    }

    private void updateAndRepaintFrame() {
        this.revalidate();
        this.repaint();
        this.pack();
    }

    void changePanel(JPanel newPanel) {
        this.getContentPane().removeAll(); // Remove current panel
        this.getContentPane().add(newPanel); // Add the new panel
        this.pack(); // Adapt the JFrame size to fit the contents (the newly added panel)
        this.setSize(800, 500);
        this.setLocationRelativeTo(null); // Center the window
        this.revalidate(); // Re-validate the component hierarchy
        this.repaint(); // Repaint the frame
    }

    public void showRankingsPanel() {
        if (rankingPanel == null) {
            rankingPanel = new RankingsPanel(this); // Initialize the rankings panel if it hasn't been already
        }
            rankingPanel.setPreferredSize(new Dimension(600, 600)); // Example size
            getContentPane().removeAll();
            getContentPane().add(rankingPanel);
            pack(); // Adjust frame size based on content
            setLocationRelativeTo(null); // Center the window
            revalidate();
            repaint();

    }
    public void showFriendsPanel() {
        if (friendsPanel == null) {
            friendsPanel = new ManageFriendsPanel(this);
        }
        friendsPanel.loadFriendsFromFile();
        friendsPanel.setPreferredSize(new Dimension(600, 600)); // Example size
        getContentPane().removeAll();
        getContentPane().add(friendsPanel);
        friendsPanel.updateFriendsList(); // Ensure the friends list is updated
        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    public boolean isViewingRankingsAfterLogin() {
        return viewingRankingsAfterLogin;
    }

    public boolean isViewingHistoryAfterLogin() {
        return viewingHistoryAfterLogin;
    }
    public boolean isViewingFriendsAfterLogin() {
        return ViewingFriendsAfterLogin;
    }
    public void setViewingFriendsAfterLogin (boolean viewingFriendsAfterLogin) {
        this.ViewingFriendsAfterLogin = viewingFriendsAfterLogin;
    }
    public void setViewingRankingsAfterLogin(boolean viewingRankingsAfterLogin) {
        this.viewingRankingsAfterLogin = viewingRankingsAfterLogin;
    }

    public void setViewingHistoryAfterLogin(boolean viewingHistoryAfterLogin) {
        this.viewingHistoryAfterLogin = viewingHistoryAfterLogin;
    }


}


