import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountCreationPanel extends JPanel {
    private GameFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton createAccountButton;

    public AccountCreationPanel(GameFrame frame) {
        this.frame = frame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        createAccountButton = new JButton("Create Account");
        this.setSize(1000, 800);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });
        setBackground(Color.blue);
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
        add(createAccountButton, gbc);
    }

    private void createAccount() {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (UserAccount.AccountManager.createAccount(username, password)) {
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                frame.showLoginPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Account creation failed. Username may already exist.");
            }
        }

    }
