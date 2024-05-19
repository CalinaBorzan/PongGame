import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class LoginPanelWithAction extends JPanel {
    private GameFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Consumer<UserAccount> postLoginAction;

    public LoginPanelWithAction(GameFrame frame, Consumer<UserAccount> postLoginAction) {
        this.frame = frame;
        this.postLoginAction = postLoginAction;
        initializeUI();
        setBackground(Color.blue);
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        add(new JLabel("Username:"), gbc);
        add(usernameField, gbc);
        add(new JLabel("Password:"), gbc);
        add(passwordField, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(loginButton, gbc);

        loginButton.addActionListener(this::performLogin);
    }

    private void performLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        UserAccount user = UserAccount.AccountManager.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.setUser(user);
            if (frame.isViewingRankingsAfterLogin()) {
                frame.showRankingsPanel();
            } else if (frame.isViewingHistoryAfterLogin()) {
                frame.showHistoryPanel(user);
            } else if(frame.isViewingFriendsAfterLogin()){
                frame.showFriendsPanel();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
