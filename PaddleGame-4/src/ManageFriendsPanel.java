import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
public class ManageFriendsPanel extends JPanel {
    private GameFrame frame;
    private JTextField searchField;
    private static final String file = "message.txt";
    private JList<String> searchResultsList, friendsList;
    private JButton searchButton, addButton, backButton;
    private DefaultListModel<String> searchResultsModel, friendsModel;
    private JTextArea messageHistoryArea;
    public ManageFriendsPanel(GameFrame frame) {
        this.frame = frame;
        initializeUI();
        loadFriendsFromFile(); // Load friends at initialization
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10)); // Add some spacing

        // Search panel setup
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Search results setup
        searchResultsModel = new DefaultListModel<>();
        searchResultsList = new JList<>(searchResultsModel);
        JScrollPane searchScrollPane = new JScrollPane(searchResultsList);
        add(searchScrollPane, BorderLayout.CENTER);
        friendsModel = new DefaultListModel<>();
        friendsList = new JList<>(friendsModel);
        JScrollPane friendsScrollPane = new JScrollPane(friendsList);
        // Buttons setup
        addButton = new JButton("Add Friend");

        backButton = new JButton("Back");
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(addButton);

        buttonsPanel.add(backButton);
        add(buttonsPanel, BorderLayout.SOUTH);

        setupListeners();
    }
    private void setupListeners() {
        searchButton.addActionListener(e -> searchForUser(searchField.getText()));

        friendsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double-click
                    openMessageDialog(friendsList.getSelectedValue());
                }
            }
        });

        searchResultsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double-click
                    String selectedValue = searchResultsList.getSelectedValue();
                    String username = selectedValue.split(" - ")[0]; // Assuming your format is "username - details"
                    openMessageDialog(username);
                }
            }
        });

        addButton.addActionListener(e -> addSelectedUserAsFriend());

        backButton.addActionListener(e -> frame.showStartPanel());

    }
    void loadFriendsFromFile() {
        // Assuming a method exists to get the current user's username
        String currentUser = frame.getCurrentUser().getUsername();
        List<String> friendsUsernames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming the first CSV value is the username and the last one is the friends list
                String[] parts = line.split(",");
                String username = parts[0];
                if (username.equals(currentUser)) {
                    // Assuming the friends list is the last part and is semicolon-separated
                    Collections.addAll(friendsUsernames, parts[parts.length - 1].split(";"));
                    break; // Found the current user, no need to continue reading
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update the friends list UI
        friendsModel.clear();
        for (String friendUsername : friendsUsernames) {
            // Add each friend to the model, possibly with additional details like high scores if available
            friendsModel.addElement(friendUsername);
        }
    }

    private void searchForUser(String username) {
        searchResultsModel.clear(); // Clear previous search results

        List<UserAccount> allUsers = UserAccount.AccountManager.getAllAccounts();
        System.out.println("Searching for: " + username + ", Total users: " + allUsers.size()); // Debug print

        for (UserAccount user : allUsers) {
            System.out.println("Checking user: " + user.getUsername()); // Debug print
            if (user.getUsername().toLowerCase().contains(username.toLowerCase().trim())) {
                String friendStatus = frame.getCurrentUser().getFriends().contains(user.getUsername()) ? " (Friend)" : "";
                searchResultsModel.addElement(user.getUsername() + " - High Score: " + user.getHighScore() + friendStatus);
                System.out.println("User added to results: " + user.getUsername()); // Debug print
            }
        }

        if (searchResultsModel.isEmpty()) {
            searchResultsModel.addElement("No users found matching: " + username);
            System.out.println("No users found matching: " + username); // Debug print
        }
    }


    private void addSelectedUserAsFriend() {
        String selectedValue = searchResultsList.getSelectedValue();
        if (selectedValue == null || selectedValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user from the search results to add.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedUsername = selectedValue.split(" - ")[0];
        UserAccount selectedUser = UserAccount.AccountManager.getAccountByUsername(selectedUsername);
        if (selectedUser != null && !frame.getCurrentUser().getFriends().contains(selectedUsername)) {
            frame.getCurrentUser().addFriend(selectedUsername);
            UserAccount.AccountManager.saveAccounts();
            updateFriendsList();
            JOptionPane.showMessageDialog(this, "Added " + selectedUsername + " as a friend.");
            searchForUser(searchField.getText()); // Refresh with current search to update friend status
        } else {
            JOptionPane.showMessageDialog(this, selectedUsername + " is already a friend.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        loadFriendsFromFile();
    }

    private void openMessageDialog(String username) {
        if (username != null && !username.isEmpty()) {
            // Fetch the current user's username. Ensure this method call is valid and exists in your frame class.
            String currentUserUsername = frame.getCurrentUser().getUsername();
            // Pass both the friend's username and the current user's username to the dialog
            MessageDialog dialog = new MessageDialog(frame, username, currentUserUsername);
            dialog.setVisible(true);
        }
    }


    void updateFriendsList() {
        friendsModel.clear();
        List<String> friendsUsernames = frame.getCurrentUser().getFriends();
        for (String friendUsername : friendsUsernames) {
            UserAccount friend = UserAccount.AccountManager.getAccountByUsername(friendUsername);
            if (friend != null) {
                friendsModel.addElement(friend.getUsername() + " - High Score: " + friend.getHighScore() + " ✔"); // Added check mark
            }
        }
    }
}
