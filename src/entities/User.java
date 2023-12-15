package entities;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private static final String DATABASE_FILE = "resources/users.txt";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static boolean register(String username, String password) {
        Map<String, String> users = loadUsers();
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        saveUsers(users);
        return true;
    }

    public static boolean login(String username, String password) {
        Map<String, String> users = loadUsers();
        return password.equals(users.get(username));
    }

    private static Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                users.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static void saveUsers(Map<String, String> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}