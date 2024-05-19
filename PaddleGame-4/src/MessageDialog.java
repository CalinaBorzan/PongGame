import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

public class MessageDialog extends JDialog {
    private JTextArea messageHistoryArea;
    private JTextField newMessageField;
    private JButton sendButton;
    private String friendUsername;
    private JFrame parentFrame;

    private String currentUserUsername; // Class member to hold the current user's username

    public MessageDialog(JFrame parentFrame, String friendUsername, String currentUserUsername) {
        super(parentFrame, "Chat with " + friendUsername, true);
        this.friendUsername = friendUsername;
        this.parentFrame = parentFrame;
        this.currentUserUsername = currentUserUsername; // Assign the passed username to the class member
        initializeUI();
    }


    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        messageHistoryArea = new JTextArea(10, 30);
        messageHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageHistoryArea); // Add scroll pane for the text area
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        newMessageField = new JTextField();
        bottomPanel.add(newMessageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Call to load the chat history
        loadMessageHistory(currentUserUsername, friendUsername); // Ensure this method is correctly implemented

        setupListeners();

        setSize(400, 300); // Set the dialog size
        setLocationRelativeTo(parentFrame); // Center the dialog relative to the parent frame
    }


    private void setupListeners() {
        sendButton.addActionListener(e -> sendMessage());
        // You can also add a listener to the newMessageField for the Enter key to send a message
        newMessageField.addActionListener(e -> sendMessage());
    }

    private void loadMessageHistory(String currentUserUsername, String friendUsername) {
        String chatFileName = generateFileName(currentUserUsername, friendUsername);
        File file = new File(chatFileName);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    messageHistoryArea.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void sendMessage() {
        String message = newMessageField.getText().trim();
        if (!message.isEmpty()) {
            // Append to message history and clear the input field
            messageHistoryArea.append(currentUserUsername + ": " + message + "\n");
            newMessageField.setText("");

            // Save the message with both the current user and the friend involved
            saveMessage(currentUserUsername, friendUsername, message);
        }
    }



    private String generateFileName(String user1, String user2) {
        String[] users = {user1, user2};
        Arrays.sort(users); // Sort usernames alphabetically
        return "messages/" + users[0] + "_" + users[1] + "_chat.txt";
    }

    private void saveMessage(String senderUsername, String receiverUsername, String message) {
        String chatFileName = generateFileName(senderUsername, receiverUsername);
        File file = new File(chatFileName);
        file.getParentFile().mkdirs();
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            // Save message with sender's username prefixed
            out.println(senderUsername + ": " + message);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to send message.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }



}
