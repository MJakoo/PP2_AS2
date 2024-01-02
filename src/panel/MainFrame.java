package panel;

import entities.MovieDatabase;
import entities.WatchListDatabase;
import loader.CSVLoader;

import javax.swing.*;
import java.awt.*;

/**
 * The main application window for the movie application.
 * This frame hosts different panels for browsing movies and managing watchlists.
 */
public class MainFrame extends JFrame {

    /**
     * Constructs the MainFrame which serves as the primary interface for the user.
     * It sets up the movie browsing and watchlist panels.
     *
     * @param username The username of the currently logged-in user.
     */
    public MainFrame(String username) {
        setTitle("Movie App");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load databases
        MovieDatabase movieDatabase = CSVLoader.loadMoviesFromCSV();
        WatchListDatabase watchListDatabase = new WatchListDatabase();

        // Create panels
        WatchListPanel watchListPanel = new WatchListPanel(watchListDatabase);
        watchListPanel.loadUserWatchList(username);
        BrowseMoviesPanel browseMoviesPanel = new BrowseMoviesPanel(movieDatabase, watchListPanel, this);

        // Add panels to this frame
        add(browseMoviesPanel, BorderLayout.CENTER);
        add(watchListPanel, BorderLayout.EAST);

        setVisible(true);
    }
}
