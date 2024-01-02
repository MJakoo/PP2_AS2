package panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import entities.Movie;
import entities.MovieDatabase;
import loader.CSVLoader;

/**
 * A JPanel for browsing, adding, deleting, and searching movies in a movie database.
 * It includes features for interacting with the movies list and updating movie data.
 */
public class BrowseMoviesPanel extends JPanel {
    private JTable moviesTable;
    private final MainFrame mainFrame;

    //  HashMap used for sorting
    final private Map<Integer, Boolean> sortOrderMap = new HashMap<>();
    String csvFilePath = "src/resources/moviedb.csv";
    private final WatchListPanel watchListPanel; // Reference to the WatchListPanel to add movies to


    // Load movies from CSV file
    private List<Movie> currentDisplayedMovies;
    private final MovieDatabase movieDatabase;
    private final CSVLoader csvLoader;

    /**
     * Constructs the BrowseMoviesPanel.
     * @param movieDatabase The MovieDatabase instance to be used.
     * @param watchListPanel The WatchListPanel instance for managing watch lists.
     * @param mainFrame The MainFrame instance for main application window management.
     */
    public BrowseMoviesPanel(MovieDatabase movieDatabase, WatchListPanel watchListPanel, MainFrame mainFrame) {
        this.movieDatabase = movieDatabase;
        this.currentDisplayedMovies = new ArrayList<>(movieDatabase.getAllMovies());
        this.watchListPanel = watchListPanel;
        this.mainFrame = mainFrame;
        csvLoader = new CSVLoader();
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

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        contextMenu.add(deleteMenuItem);

        // Add a mouse listener to the moviesTable to show the context menu
        moviesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int row = moviesTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < moviesTable.getRowCount()) {
                        // Select the clicked row
                        moviesTable.setRowSelectionInterval(row, row);
                        // Show the context menu at the mouse position
                        contextMenu.show(moviesTable, e.getX(), e.getY());
                    }
                }
            }
        });

        // Add an action listener to the "Delete" menu item
        deleteMenuItem.addActionListener(e -> {
            int selectedRow = moviesTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < currentDisplayedMovies.size()) {
                Movie selectedMovie = currentDisplayedMovies.get(selectedRow);
                deleteMovie(selectedMovie);
            }
        });


//      True For Ascending, False for Descending
        sortOrderMap.put(0, true); // Title
        sortOrderMap.put(1, true); // Director
        sortOrderMap.put(2, true); // Year
        sortOrderMap.put(3, true); // Running Time

        // Add the buttonPanel to the BrowseMoviesPanel
        add(buttonPanel, BorderLayout.PAGE_START);

        addMovieButton.addActionListener(e -> addNewMovie());
    }

//    This was for Opening the Watchlist in other Window:
//    private void openWatchList() {
//        // Assuming WatchListPanel is a JPanel
//        JFrame watchListFrame = new JFrame("WatchList");
//        watchListFrame.setContentPane(watchListPanel); // Set the WatchListPanel as the content pane
//        watchListFrame.setSize(400, 300); // Set the size of the frame
//        watchListFrame.setVisible(true); // Make the frame visible
//    }

    /**
     * Logs out the current user and closes the main application window.
     */
    private void logout() {
        new LoginFrame(); // Open the login window
        mainFrame.dispose(); // Close the main application window
        System.out.println("Main Frame Closed, Logged Out of the Session");

    }

    /**
     * Opens a dialog to add a new movie to the database.
     */
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
            try {
                String title = titleField.getText();
                String director = directorField.getText();
                int releaseYear = Integer.parseInt(yearField.getText());
                int runningTime = Integer.parseInt(runningTimeField.getText());
                Movie newMovie = new Movie(title, director, releaseYear, runningTime);
                addMovieToDatabaseAndCSV(newMovie);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Adds a movie to the database and updates the CSV file.
     * @param movie The movie to add.
     */
    private void addMovieToDatabaseAndCSV(Movie movie) {
        if (movieDatabase.addMovie(movie)) {
            appendMovieToCSV(movie, csvFilePath);
            refreshMovieList();
        } else {
            JOptionPane.showMessageDialog(this, "Movie already exists in the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a selected movie from the database and updates the CSV file.
     * @param movie The movie to delete.
     */
    private void deleteMovie(Movie movie) {
        // Remove the movie from your data source (e.g., movieDatabase)
        boolean deletedFromDatabase = movieDatabase.removeMovie(movie.getTitle());

        if (deletedFromDatabase) {
            // Remove the movie from the currentDisplayedMovies list
            currentDisplayedMovies.remove(movie);

            // Refresh the movie list or table
            refreshMovieList();

            // Delete the movie from the CSV file using CSVLoader
            boolean deletedFromFile = csvLoader.deleteMovieByTitle(movie.getTitle());

            if (!deletedFromFile) {
                JOptionPane.showMessageDialog(this, "Failed to delete the movie from the CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete the movie from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Appends a new movie to the CSV file.
     * @param movie The movie to append.
     * @param filePath The file path of the CSV file.
     */
    private void appendMovieToCSV(Movie movie, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write("," + movie.getTitle() + "," + movie.getReleaseYear() + "," + movie.getRunningTime() + ", genre, 0," + movie.getDirector()) ;
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a search for movies based on a search text and an optional year filter.(Uses Stream API)
     * @param searchText The text to search for.
     * @param filterYear The year to filter the movies, can be null.
     */
    private void performSearch(String searchText, Integer filterYear) {
        String lowerCaseSearchText = searchText.toLowerCase();
        List<Movie> filteredMovies = movieDatabase.getAllMovies().stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(lowerCaseSearchText)
                        || movie.getDirector().toLowerCase().contains(lowerCaseSearchText))
                .filter(movie -> filterYear == null || movie.getReleaseYear() == filterYear)
                .collect(Collectors.toList());

        updateMovieTable(filteredMovies);
        currentDisplayedMovies = filteredMovies;
    }

    /**
     * Sorts the movie table based on the specified column index.
     * @param columnIndex The index of the column to sort by.
     */
    private void sortTableByColumn(int columnIndex) {
        boolean isAscending = sortOrderMap.getOrDefault(columnIndex, true);
        Stream<Movie> movieStream = currentDisplayedMovies.stream();

        Comparator<Movie> comparator = switch (columnIndex) {
            case 0 -> // "Title" column
                    Comparator.comparing(Movie::getTitle);
            case 1 -> // "Director" column
                    Comparator.comparing(Movie::getDirector);
            case 2 -> // "Year" column
                    Comparator.comparing(Movie::getReleaseYear);
            case 3 -> // "Running Time" column
                    Comparator.comparing(Movie::getRunningTime);
            default -> throw new IllegalArgumentException("Invalid column index");
        };

        if (!isAscending) {
            comparator = comparator.reversed();
        }

        currentDisplayedMovies = movieStream.sorted(comparator).collect(Collectors.toList());
        updateMovieTable(currentDisplayedMovies);

        // Toggle the sort order for next time
        sortOrderMap.put(columnIndex, !isAscending);
    }

    /**
     * Updates the movie table with a new list of movies.
     * @param movies The list of movies to display.
     */
    private void updateMovieTable(List<Movie> movies) {
        DefaultTableModel model = (DefaultTableModel) moviesTable.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Movie movie : movies) {
            model.addRow(new Object[]{movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getRunningTime(), "Add"});
        }
    }

    /**
     * Shows a dialog to search for movies.
     */
    private void showSearchDialog() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter search query:", "Search Movies", JOptionPane.PLAIN_MESSAGE);
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Example: Ask for a year to filter by (this can be skipped or made optional)
            String yearString = JOptionPane.showInputDialog(this, "Enter year (optional):", "Filter by Year", JOptionPane.PLAIN_MESSAGE);
            Integer yearFilter;
            try {
                yearFilter = yearString != null && !yearString.trim().isEmpty() ? Integer.parseInt(yearString.trim()) : null;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid year format", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if the year format is invalid
            }

            performSearch(searchQuery.trim(), yearFilter);
        }
    }

    /**
     * Initializes the movie table with column headers and sets up event handlers.
     */
    private void initMoviesTable() {
        moviesTable = new JTable();
        moviesTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        moviesTable.setFillsViewportHeight(true);
        JTableHeader header = moviesTable.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = header.columnAtPoint(e.getPoint());
                sortTableByColumn(columnIndex);
            }
        });

        JScrollPane scrollPane = new JScrollPane(moviesTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Populates the movie table with movie data from the database.
     */
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
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox(), moviesTable, watchListPanel);
        moviesTable.getColumn("Add to Watch List").setCellEditor(buttonEditor);

    }

    /**
     * ButtonRenderer for rendering a button in a table cell.
     * This class is responsible for displaying a button in the JTable.
     */
    static class ButtonRenderer extends JButton implements TableCellRenderer {

        /**
         * Constructs a ButtonRenderer.
         */
        public ButtonRenderer() {
            setOpaque(true);
        }

        /**
         * Returns the table cell renderer component.
         * @param table The JTable that is asking the renderer to draw.
         * @param value The value of the cell to be rendered.
         * @param isSelected True if the cell is to be rendered with selection highlighting.
         * @param hasFocus If true, render cell appropriately.
         * @param row The row index of the cell being drawn.
         * @param column The column index of the cell being drawn.
         * @return The component used for drawing the cell.
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
     * ButtonEditor for handling button clicks in a table cell.
     * This class manages the functionality when a button within a table cell is clicked.
     */
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        /**
         * Constructs a ButtonEditor.
         * @param checkBox A checkbox that provides the look-and-feel for the editor.
         * @param table The table that this editor will work on.
         * @param watchListPanel The WatchListPanel to interact with.
         */
        public ButtonEditor(JCheckBox checkBox, JTable table, WatchListPanel watchListPanel) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                try {
                    int viewRow = table.getEditingRow(); // Get the view row index
                    if (viewRow >= 0) {
                        int modelRow = table.convertRowIndexToModel(viewRow); // Convert to model row index
                        if (modelRow >= 0 && modelRow < currentDisplayedMovies.size()) {
                            Movie selectedMovie = currentDisplayedMovies.get(modelRow);
                            watchListPanel.addToWatchList(selectedMovie);
                        }
                    }
                } catch (Exception ex){
                    ex.printStackTrace(); // For debugging
                    JOptionPane.showMessageDialog(table, "Error occurred while processing your request.", "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    fireEditingStopped();
                }

            });
        }

        /**
         * Returns the component used for editing the value of a cell.
         * @param table The JTable that is asking the editor to edit.
         * @param value The value of the cell to be edited.
         * @param isSelected True if the cell is to be rendered with selection highlighting.
         * @param row The row index of the cell being edited.
         * @param column The column index of the cell being edited.
         * @return The component for editing.
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        /**
         * Returns the value contained in the editor.
         * @return The value contained in the editor.
         */
        public Object getCellEditorValue() {
            if (isPushed) {

                System.out.println(label + " clicked!");
            }
            isPushed = false;
            return label;
        }

        /**
         * Tells the editor to stop editing and accept any partially edited value as the value of the editor.
         * @return true if editing was stopped.
         */
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    /**
     * Updates the current displayed movies list from the movie database.
     */
    private void updateCurrentDisplayedMovies() {
        currentDisplayedMovies = new ArrayList<>(movieDatabase.getAllMovies());
    }

    /**
     * Refreshes the movie list in the table.
     */
    private void refreshMovieList() {
        DefaultTableModel model = (DefaultTableModel) moviesTable.getModel();
        model.setRowCount(0); // Clear the existing rows

        // Populate the model with movies and a placeholder for the button
        for (Movie movie : movieDatabase.getAllMovies()) {
            model.addRow(new Object[]{movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getRunningTime(), "Add"});
        }

        // Notify the table that the data has changed
        model.fireTableDataChanged();
        updateCurrentDisplayedMovies();
    }

}