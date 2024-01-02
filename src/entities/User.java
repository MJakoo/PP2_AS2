package entities;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a user with a username and password.
 * Provides static methods for user registration and login, utilizing a file-based storage system.
 */
public class User {
    private String username;
    private String password;

    /**
     * Constructs a new User object with a specified username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters

    /**
     * Returns the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to be set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to be set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Registers a new user with a given username and password.
     * Registration is successful only if the username does not already exist.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @return true if registration is successful, false if the username already exists.
     */
    public static boolean register(String username, String password) {
        Map<String, String> users = loadUsers();
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        saveUsers(users);
        return true;
    }

    /**
     * Attempts to log in a user with the provided username and password.
     *
     * @param username The username of the user trying to log in.
     * @param password The password of the user.
     * @return true if login is successful, false otherwise.
     */
    public static boolean login(String username, String password) {
        Map<String, String> users = loadUsers();
        return password.equals(users.get(username));
    }

    /**
     * Loads users from a file into a Map.
     * The file format is expected to be 'username:password' with each user on a new line.
     *
     * @return A Map containing usernames as keys and passwords as values.
     */
    private static Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String username = parts[0].replaceAll("\"", ""); // Remove quotes
                String password = parts[1].replaceAll("\"", ""); // Remove quotes
                users.put(username, password);
            }
            System.out.println(users);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with loading the users!");
        }
        return users;
    }

    /**
     * Saves the current state of users to a file.
     * Each user is written in the format 'username:password' with each user on a new line.
     *
     * @param users A Map containing usernames as keys and passwords as values.
     */
    private static void saveUsers(Map<String, String> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/users.txt"))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                String username = "\"" + entry.getKey() + "\""; // Add quotes
                String password = "\"" + entry.getValue() + "\""; // Add quotes
                writer.write(username + ":" + password);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
