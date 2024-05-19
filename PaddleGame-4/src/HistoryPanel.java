import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HistoryPanel extends JPanel {
    private JTextArea historyTextArea;
    private JButton backButton;
    private GameFrame frame;

    public HistoryPanel(UserAccount user, GameFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());

        historyTextArea = new JTextArea(10, 30);
        historyTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyTextArea);

        populateHistory(user); // Populate text area with the user's game history

        backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.showStartPanel());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);

        c.gridy = 1;
        c.weighty = 0.0;
        add(backButton, c);
    }

    private void populateHistory(UserAccount user) {
        historyTextArea.setText(""); // Clear previous text
        for (GameRecord record : user.getGameHistory()) {

            if (user.getUsername().equals(record.getPlayer1Name())) {
                historyTextArea.append("Score: " + record.getPlayer1Score() +

                        ", Time: " + record.getElapsedTimeInSeconds() + " seconds\n");
            } else if (user.getUsername().equals(record.getPlayer2Name())) {
                historyTextArea.append("Score: " + record.getPlayer2Score() +

                        ", Time: " + record.getElapsedTimeInSeconds() + " seconds\n");
            }
        }
    }
}
