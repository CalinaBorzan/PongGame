import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RankingsPanel extends JPanel {
    private GameFrame frame;
    private JScrollPane scrollPane;
    private JList<String> rankingsList;
    private JButton backButton;
    // Updated constructor to correctly receive and assign the GameFrame
    public RankingsPanel(GameFrame frame) {
        this.frame = frame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.BLUE);

        // Assuming UserAccount.AccountManager.getAllAccountsSortedByScore() returns a sorted list
        List<UserAccount> sortedUsers = UserAccount.AccountManager.getAllAccountsSortedByScore();

        // Convert the sorted user list to display strings
        String[] userDisplayStrings = sortedUsers.stream()
                .map(user -> user.getUsername() + " - High Score: " + user.getHighScore())
                .toArray(String[]::new);

        // Create and set up the JList for displaying rankings
        rankingsList = new JList<>(userDisplayStrings);
        rankingsList.setFont(new Font("Arial", Font.BOLD, 14));
        rankingsList.setForeground(Color.WHITE);
        rankingsList.setBackground(Color.DARK_GRAY);

        // Add the JList to a JScrollPane
        scrollPane = new JScrollPane(rankingsList);
        add(scrollPane, BorderLayout.CENTER);

        // Create and add a back button
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showStartPanel();
            }
        });
        add(backButton, BorderLayout.SOUTH); // This line was missing in your code

        // Optionally, you can set margins or paddings if the button appears too close to the edges
        backButton.setMargin(new Insets(10,10,10,10));
    }
}
