package panel;

import entities.MovieDatabase;
import entities.WatchListDatabase;
import loader.CSVLoader;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(String username) {
        setTitle("Movie App");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load databases
        MovieDatabase movieDatabase = CSVLoader.loadMoviesFromCSV("src/resources/moviedb.csv");
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
