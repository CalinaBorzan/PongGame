import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private GameFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField usernameField2;
    private JPasswordField passwordField2;
    private JButton loginButton;
    private JRadioButton singlePlayerButton;
    private JRadioButton twoPlayerButton;
    private ButtonGroup modeGroup;
    public LoginPanel(GameFrame frame) {
        this.frame = frame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(Color.blue);
        gbc.insets = new Insets(10, 10, 10, 10); // Adds padding between components
        gbc.anchor = GridBagConstraints.WEST; // Aligns components to the left

        // Initialize radio buttons and group
        singlePlayerButton = new JRadioButton("Single Player", true); // Initialized
        twoPlayerButton = new JRadioButton("Two Player"); // Initialized
        modeGroup = new ButtonGroup();
        modeGroup.add(singlePlayerButton);
        modeGroup.add(twoPlayerButton);

        // Initialize text fields and button
        usernameField = new JTextField(20); // Initialized
        passwordField = new JPasswordField(20); // Initialized
        usernameField2 = new JTextField(20); // Initialized
        passwordField2 = new JPasswordField(20); // Initialized
        loginButton = new JButton("Login"); // Initialized

        // Setup layout with initialized components
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);

        gbc.gridy++;
        add(usernameField, gbc);

        gbc.gridy++;
        add(new JLabel("Password:"), gbc);

        gbc.gridy++;
        add(passwordField, gbc);

        gbc.gridy++;
        add(singlePlayerButton, gbc);

        gbc.gridy++;
        add(twoPlayerButton, gbc);

        gbc.gridy++;
        add(new JLabel("Player 2 Username:"), gbc);

        gbc.gridy++;
        add(usernameField2, gbc);

        gbc.gridy++;
        add(new JLabel("Player 2 Password:"), gbc);

        gbc.gridy++;
        add(passwordField2, gbc);

        // Adjusting the grid position for the login button to be centered
        gbc.gridwidth = GridBagConstraints.REMAINDER; // This makes the component take the remainder of the line
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        gbc.gridy++; // Increment to place the button on the next row
        add(loginButton, gbc);

        // Set the initial state for Player 2 fields based on the mode
        updateLoginFieldsEnabledState();

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        twoPlayerButton.addActionListener(e -> updateLoginFieldsEnabledState());
        singlePlayerButton.addActionListener(e -> updateLoginFieldsEnabledState());
    }

    private void updateLoginFieldsEnabledState() {
        boolean isTwoPlayerMode = twoPlayerButton.isSelected();
        usernameField2.setEnabled(isTwoPlayerMode);
        passwordField2.setEnabled(isTwoPlayerMode);
    }

    private void performLogin() {
        String username1 = usernameField.getText();
        String password1 = new String(passwordField.getPassword());
        UserAccount user1 = UserAccount.AccountManager.login(username1, password1);

        // Check if single-player mode is selected
        if (singlePlayerButton.isSelected()) {
            if (user1 != null) {
                // Assuming the 'setUser' method and 'showModeSelection' can handle single-player logic
                frame.setUser(user1);
                frame.startGame(false, user1.getUsername(), "Robot"); // Pass "Robot" as the opponent name
            } else {
                JOptionPane.showMessageDialog(this, "Login failed for the player.");
            }
        } else {
            // Two-player mode
            String username2 = usernameField2.getText();
            String password2 = new String(passwordField2.getPassword());
            UserAccount user2 = UserAccount.AccountManager.login(username2, password2);

            if (user1 != null && user2 != null) {
                frame.startGame(true, user1.getUsername(), user2.getUsername()); // Proceed with both usernames
            } else {
                JOptionPane.showMessageDialog(this, "Login failed for one or both players.");
            }
        }
    }


}
