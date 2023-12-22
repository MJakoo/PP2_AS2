package panel;

import entities.Movie;
import entities.MovieDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrowseMoviesPanel extends JPanel {
    private JTable moviesTable;
//    private MainFrame mainFrame;
    String csvFilePath = "src/resources/moviedb.csv";
    private WatchListPanel watchListPanel; // Reference to the WatchListPanel to add movies to


    // Load movies from CSV file
    private List<Movie> currentDisplayedMovies;
    private MovieDatabase movieDatabase;
    // Other member variables...

    public BrowseMoviesPanel(MovieDatabase movieDatabase, WatchListPanel watchListPanel, MainFrame mainFrame) {
        this.movieDatabase = movieDatabase;
        this.currentDisplayedMovies = new ArrayList<>(movieDatabase.getAllMovies());
        this.watchListPanel = watchListPanel;
//        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initMoviesTable();
        populateMovies();JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // "Add New Movie" button
        JButton addMovieButton = new JButton("Add New Movie");
        addMovieButton.addActionListener(e -> addNewMovie());
        buttonPanel.add(addMovieButton); // Add to the button panel

        // "Search" button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> showSearchDialog());
        buttonPanel.add(searchButton); // Add to the button panel

        JButton resetButton = new JButton("Reset View");
        resetButton.addActionListener(e -> refreshMovieList());
        buttonPanel.add(resetButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        add(logoutButton, BorderLayout.PAGE_END); // Add the button to the panel

        add(buttonPanel, BorderLayout.PAGE_START);

        addMovieButton.addActionListener(e -> addNewMovie());
    }

    private void logout() {
        new LoginFrame(); // Open the login window
//        mainFrame.dispose(); // Close the main application window
        System.out.println("Main Frame Closed, Logged Out of the Session");

    }

    private void addNewMovie() {
        JTextField titleField = new JTextField(20);
        JTextField directorField = new JTextField(20);
        JTextField yearField = new JTextField(4);
        JTextField runningTimeField = new JTextField(4);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Director:"));
        panel.add(directorField);
        panel.add(new JLabel("Release Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Running Time (minutes):"));
        panel.add(runningTimeField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Movie Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String director = directorField.getText();
            int releaseYear = Integer.parseInt(yearField.getText());
            int runningTime = Integer.parseInt(runningTimeField.getText());
            Movie newMovie = new Movie(title, director, releaseYear, runningTime);
            addMovieToDatabaseAndCSV(newMovie);
        }
    }

    private void addMovieToDatabaseAndCSV(Movie movie) {
        if (movieDatabase.addMovie(movie)) {
            appendMovieToCSV(movie, csvFilePath);
            refreshMovieList();
        } else {
            JOptionPane.showMessageDialog(this, "Movie already exists in the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void appendMovieToCSV(Movie movie, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write("," + movie.getTitle() + "," + movie.getReleaseYear() + "," + movie.getRunningTime() + ", genre, 0," + movie.getDirector()) ;
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void performSearch(String searchText) {
        String lowerCaseSearchText = searchText.toLowerCase();
        List<Movie> filteredMovies = movieDatabase.getAllMovies().stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(lowerCaseSearchText)
                        || movie.getDirector().toLowerCase().contains(lowerCaseSearchText))
                .collect(Collectors.toList());

        updateMovieTable(filteredMovies);
    }

    private void updateMovieTable(List<Movie> movies) {
        currentDisplayedMovies = movies; // Update the current displayed list
        DefaultTableModel model = (DefaultTableModel) moviesTable.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Movie movie : currentDisplayedMovies) {
            model.addRow(new Object[]{movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getRunningTime(), "Add"});
        }
    }
    private void showSearchDialog() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter search query:", "Search Movies", JOptionPane.PLAIN_MESSAGE);
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            performSearch(searchQuery.trim());
        }
    }
    private void initMoviesTable() {
        moviesTable = new JTable();
        moviesTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        moviesTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(moviesTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    private void populateMovies() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow only the "Add to Watch List" column to be clickable
                return column == 4;
            }

        };

        // Add columns to model
        model.addColumn("Title");
        model.addColumn("Director");
        model.addColumn("Year");
        model.addColumn("Runtime");

        // Now apply the model to your JTable
        moviesTable.setModel(model);
        model.addColumn("Add to Watch List");

        // Populate the model with movies and a placeholder for the button
        for (Movie movie : movieDatabase.getAllMovies()) {
            model.addRow(new Object[]{movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getRunningTime(), "Add"});
        }

        moviesTable.setModel(model);

        // Set custom renderer and editor for the "Add to Watch List" button
        moviesTable.getColumn("Add to Watch List").setCellRenderer(new ButtonRenderer());
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox(), moviesTable, movieDatabase, watchListPanel);
        moviesTable.getColumn("Add to Watch List").setCellEditor(buttonEditor);

    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, JTable table, MovieDatabase movieDatabase, WatchListPanel watchListPanel) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                int modelRow = table.convertRowIndexToModel(table.getEditingRow());
                if (modelRow >= 0 && modelRow < currentDisplayedMovies.size()) {
                    Movie selectedMovie = currentDisplayedMovies.get(modelRow);
                    watchListPanel.addToWatchList(selectedMovie);
                }
                fireEditingStopped(); // Notify that editing is stopped
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {

                System.out.println(label + " clicked!");
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }


    private void refreshMovieList() {
        DefaultTableModel model = (DefaultTableModel) moviesTable.getModel();
        model.setRowCount(0); // Clear the existing rows

        // Populate the model with movies and a placeholder for the button
        for (Movie movie : movieDatabase.getAllMovies()) {
            model.addRow(new Object[]{movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getRunningTime(), "Add"});
        }

        // Notify the table that the data has changed
        model.fireTableDataChanged();
    }

}