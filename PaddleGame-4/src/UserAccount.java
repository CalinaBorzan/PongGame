import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserAccount {
    private static final long serialVersionUID = 1L;
    private List<String> history;
    private String username;
    private List<GameRecord> gameHistory = new ArrayList<>();
    private List<String> friends = new ArrayList<>();
    private int gamesWon;
    private String password;
    private int highScore;
    private long bestTime;

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.highScore = 0;
        this.bestTime = Long.MAX_VALUE;
        this.history = new ArrayList<>();
        this.gameHistory = new ArrayList<>();
    }
    public void updateGameHistory(GameRecord newRecord) {
        this.gameHistory.add(newRecord);

        // Check and update the high score for player 1
        if (newRecord.getPlayer1Score() > this.highScore && this.username.equals(newRecord.getPlayer1Name())) {
            this.highScore = newRecord.getPlayer1Score();
        }

        // Check and update the high score for player 2
        if (newRecord.getPlayer2Score() > this.highScore && this.username.equals(newRecord.getPlayer2Name())) {
            this.highScore = newRecord.getPlayer2Score();
        }

        // Check and update the best time for the current game
        if (newRecord.getElapsedTimeInSeconds() < this.bestTime) {
            this.bestTime = newRecord.getElapsedTimeInSeconds();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getHighScore() {
        return highScore;
    }

    public long getBestTime() {
        return bestTime;
    }

    public List<GameRecord> getGameHistory() {
        return gameHistory;
    }

    public void addGameRecord(GameRecord record) {
        gameHistory.add(record);
    }
    // Setters
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }
    public void addFriend(String friendUsername) {
        if (!friends.contains(friendUsername)) {
            friends.add(friendUsername);
        }
    }

    public List<String> getFriends() {
        return new ArrayList<>(friends);
    }

    public void incrementGamesWon() {
        gamesWon++;
    }

    public int getGamesWon() {
        return gamesWon;
    }
    public void addHistoryEntry(String entry) {
        history.add(entry);
    }

    public static class AccountManager {
        private static Map<String, UserAccount> accounts = new HashMap<>();
        private static final String DATA_FILE = "data.txt";

        static {
            loadAccounts(); // Load accounts from file when the class is initialized
        }
        public static List<UserAccount> getAllAccountsSortedByScore() {
            // Return a new list to avoid modifying the original map
            return accounts.values().stream()
                    .sorted(Comparator.comparingInt(UserAccount::getHighScore).reversed())
                    .collect(Collectors.toList());
        }

        public static boolean createAccount(String username, String password) {
            // Validate inputs
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                System.out.println("Invalid username or password.");
                return false; // Invalid input
            }

            // Check if username already exists
            if (accounts.containsKey(username)) {
                System.out.println("Username already exists.");
                return false; // Username already exists
            }

            // Create new account and add it to the map
            UserAccount newAccount = new UserAccount(username, password);
            accounts.put(username, newAccount);

            // Save the updated accounts list to the file
            saveAccounts();
            return true;
        }



        public static void saveAccounts() {
            System.out.println("Saving accounts to file...");
            try (PrintWriter out = new PrintWriter(new FileWriter(DATA_FILE))) {
                for (UserAccount account : accounts.values()) {
                    String accountInfo = String.join(",", account.getUsername(), account.getPassword(),
                            String.valueOf(account.getHighScore()), String.valueOf(account.getBestTime()));
                    out.println(accountInfo);
                    for (GameRecord record : account.getGameHistory()) {
                        out.println("--GameRecordStart--");
                        out.println(record.getPlayer1Score() + "," + record.getPlayer2Score() + "," + record.getElapsedTimeInSeconds());
                        out.println("--GameRecordEnd--");
                    }
                    out.println("--AccountEnd--");
                }
            } catch (IOException e) {
                System.err.println("Failed to save accounts: " + e.getMessage());
                e.printStackTrace();
            }
        }


        public static void loadAccounts() {
            try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
                String line;
                UserAccount currentAccount = null;

                while ((line = br.readLine()) != null) {
                    if (line.trim().equals("--AccountEnd--")) {
                        if (currentAccount != null) {
                            accounts.put(currentAccount.getUsername(), currentAccount);
                            currentAccount = null;
                        }
                    } else if (line.trim().equals("--GameRecordStart--")) {
                        line = br.readLine(); // Read the next line which should have game record details
                        if (line != null && currentAccount != null) {
                            String[] parts = line.split(",");
                            if (parts.length == 3) { // Ensure there are three parts for player1Score, player2Score, elapsedTimeInSeconds
                                int player1Score = Integer.parseInt(parts[0].trim());
                                int player2Score = Integer.parseInt(parts[1].trim());
                                long elapsedTimeInSeconds = Long.parseLong(parts[2].trim());
                                GameRecord record = new GameRecord(currentAccount.getUsername(), player1Score, currentAccount.getUsername(), player2Score, elapsedTimeInSeconds);
                                currentAccount.addGameRecord(record);
                            }
                            br.readLine(); // Skip the "--GameRecordEnd--"
                        }
                    } else if (currentAccount == null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 4) { // Check to ensure data is adequate
                            String username = parts[0].trim();
                            String password = parts[1].trim();
                            int highScore = Integer.parseInt(parts[2].trim());
                            long bestTime = Long.parseLong(parts[3].trim());
                            currentAccount = new UserAccount(username, password);
                            currentAccount.setHighScore(highScore);
                            currentAccount.setBestTime(bestTime);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading accounts: " + e.getMessage());
            }
        }


        public static UserAccount login(String username, String password) {
            UserAccount account = accounts.get(username.trim());
            if (account != null && account.getPassword().equals(password.trim())) {
                return account;
            }
            return null;  // Return null if credentials are incorrect
        }


        public static void updateHistory(String username, String entry) {
            UserAccount account = accounts.get(username);
            if (account != null) {
                account.addHistoryEntry(entry);
                saveAccounts(); // Save updated history
            }
        }


        public static UserAccount getAccountByUsername(String username) {
            return accounts.getOrDefault(username, null); // Return null if user does not exist
        }

        public static List<UserAccount> getAllAccounts() {
            return new ArrayList<>(accounts.values());
        }
    }


}

