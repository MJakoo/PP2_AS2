package entities;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatchListDatabase {
    private static final String WATCHLIST_FILE = "src/resources/watchlistdb.txt";
    private Map<String, List<String>> userWatchlists;

    public WatchListDatabase() {
        userWatchlists = new HashMap<>();
        loadWatchlists();
    }

    private void loadWatchlists() {
        try (BufferedReader reader = new BufferedReader(new FileReader(WATCHLIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0];
                    List<String> movies = new ArrayList<>();
                    if (!parts[1].isEmpty()) {
                        for (String movieTitle : parts[1].split(",,")) {
                            movies.add(movieTitle);
                        }
                    }
                    userWatchlists.put(username, movies);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWatchlists() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WATCHLIST_FILE))) {
            for (Map.Entry<String, List<String>> entry : userWatchlists.entrySet()) {
                writer.write(entry.getKey() + ":" + String.join(",,", entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMovieToWatchlist(String username, String movie) {
        List<String> watchlist = userWatchlists.computeIfAbsent(username, k -> new ArrayList<>());
        if (!watchlist.contains(movie)) {
            watchlist.add(movie);
            saveWatchlists();
        }
    }


}
