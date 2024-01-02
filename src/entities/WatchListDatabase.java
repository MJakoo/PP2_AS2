package entities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

/**
 * Manages a database of user watchlists, with functionality to load, save, add, and remove movies.
 * Each user's watchlist is stored as a list of movie titles.
 */
public class WatchListDatabase {
    private static final String WATCHLIST_FILE = "src/resources/watchlistdb.txt";
    final private Map<String, List<String>> userWatchlists;

    /**
     * Constructs a new WatchListDatabase and loads existing watchlists from a file.
     */
    public WatchListDatabase() {
        userWatchlists = new HashMap<>();
        loadWatchlists();
    }

    /**
     * Loads watchlists from a file. The file format is expected to be 'username==>movie1,,movie2' with each user on a new line.
     */
    private void loadWatchlists() {
        try (BufferedReader reader = new BufferedReader(new FileReader(WATCHLIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("==>");
                if (parts.length >= 2) {
                    String username = parts[0];
                    List<String> movies = new ArrayList<>();
                    if (!parts[1].isEmpty()) {
                        Collections.addAll(movies, parts[1].split(",,"));
                    }
                    userWatchlists.put(username, movies);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current state of all user watchlists to a file.
     * Each user's watchlist is written in the format 'username==>movie1,,movie2' with each user on a new line.
     */
    public void saveWatchlists() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WATCHLIST_FILE))) {
            for (Map.Entry<String, List<String>> entry : userWatchlists.entrySet()) {
                writer.write(entry.getKey() + "==>" + String.join(",,", entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a movie to a user's watchlist and saves the updated watchlist.
     * If the movie is already in the watchlist, it is not added again.
     *
     * @param username The username of the user.
     * @param movie The movie to be added to the watchlist.
     */
    public void addMovieToWatchlist(String username, String movie) {
        List<String> watchlist = userWatchlists.computeIfAbsent(username, k -> new ArrayList<>());
        if (!watchlist.contains(movie)) {
            watchlist.add(movie);
            saveWatchlists();
        }
    }

    /**
     * Removes a movie from a user's watchlist and updates the associated JTable view.
     * If the movie is found and removed, the watchlist is saved and the JTable is updated accordingly.
     *
     * @param username The username of the user.
     * @param movie The movie to be removed from the watchlist.
     * @param watchListTable The JTable displaying the watchlist.
     */
    public void removeFromWatchlist(String username, String movie, JTable watchListTable) {
        List<String> watchlist = userWatchlists.get(username);
        if (watchlist != null && watchlist.remove(movie)) {
            saveWatchlists();
            System.out.println("Delete clicked!");
            // Update JTable
            int rowIndex = watchListTable.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) watchListTable.getModel();
            if (rowIndex != -1 && rowIndex < model.getRowCount()) {
                model.removeRow(rowIndex);
            }
            if (rowIndex < model.getRowCount()) {
                watchListTable.setRowSelectionInterval(rowIndex, rowIndex);
            } else if (model.getRowCount() > 0) {
                watchListTable.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
            }
        }
    }

    /**
     * Retrieves the watchlist for a specific user.
     *
     * @param username The username of the user.
     * @return A List containing the movie titles in the user's watchlist.
     */
    public List<String> getWatchlistForUser(String username) {
        return userWatchlists.getOrDefault(username, new ArrayList<>());
    }

}
