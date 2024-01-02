package panel;

import entities.Movie;
import entities.WatchListDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * A panel for managing and displaying a user's movie watchlist.
 * It allows users to view, add, and remove movies from their watchlist.
 */
public class WatchListPanel extends JPanel {
    private JTable watchListTable;
    private DefaultTableModel watchListModel;
    private WatchListDatabase watchListDB;
    private String username;
    private JLabel emptyLabel;

    /**
     * Constructs a new WatchListPanel.
     * @param watchListDB The WatchListDatabase to interact with.
     */
    public WatchListPanel(WatchListDatabase watchListDB) {
        this.watchListDB = watchListDB;
        setLayout(new BorderLayout());

        emptyLabel = new JLabel("Your watchlist is empty.");
        emptyLabel.setHorizontalAlignment(JLabel.CENTER);
        emptyLabel.setVisible(false); // Initially invisible
        add(emptyLabel, BorderLayout.NORTH);

        initWatchListTable();
    }

    /**
     * Loads the watchlist for a specified user and populates the panel.
     * @param username The username whose watchlist is to be loaded.
     */
    public void loadUserWatchList(String username) {
        this.username = username;
        populateWatchList(username);
    }

    /**
     * Initializes the watchlist table component.
     */
    private void initWatchListTable() {
        watchListTable = new JTable();
        watchListTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        watchListTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(watchListTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Populates the watchlist with movies from the specified user's watchlist.
     * @param username The username whose watchlist is to be populated.
     */
    private void populateWatchList(String username) {
        watchListModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only the "Remove" column is clickable
            }
        };

        watchListModel.addColumn("Movie");
        watchListModel.addColumn("Remove");

        // Retrieve the watchlist for the specific user
        List<String> userWatchList = watchListDB.getWatchlistForUser(username);
        for (String movieTitle : userWatchList) {
            watchListModel.addRow(new Object[]{movieTitle, "Remove"});
        }

        watchListTable.setModel(watchListModel);

        watchListTable.getColumn("Remove").setCellRenderer(new ButtonRenderer());
        watchListTable.getColumn("Remove").setCellEditor(new ButtonEditor(new JCheckBox(), username, watchListDB));

    }

    /**
     * Adds a movie to the user's watchlist.
     * @param movie The movie to add to the watchlist.
     */
    public void addToWatchList(Movie movie) {
        List<String> userWatchList = watchListDB.getWatchlistForUser(username);
        String movieTitle = movie.getTitle();
        if (!userWatchList.contains(movieTitle)) {
            watchListDB.addMovieToWatchlist(username, movieTitle);
            refreshWatchList();
        }
    }

    /**
     * Renders a button in a table cell.
     * This class extends JButton and implements TableCellRenderer, allowing it to display a button in a JTable.
     */
    class ButtonRenderer extends JButton implements TableCellRenderer {

        /**
         * Constructs a ButtonRenderer setting the button as opaque.
         */
        public ButtonRenderer() {
            setOpaque(true);
        }

        /**
         * Returns the component used for drawing the cell. This method is called each time a cell in a column
         * using this renderer needs to be rendered.
         *
         * @param table The JTable that is asking the renderer to draw.
         * @param value The value of the cell to be rendered.
         * @param isSelected True if the cell is selected.
         * @param hasFocus True if the cell has focus.
         * @param row The row index of the cell being drawn.
         * @param column The column index of the cell being drawn.
         * @return The component used for drawing the cell.
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : "Remove");
            return this;
        }
    }

    /**
     * Handles interactions with a button placed within a table cell.
     * This class extends DefaultCellEditor and is used to handle the click events of buttons in a JTable.
     */
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String username;
        private WatchListDatabase watchListDB;

        /**
         * Constructs a ButtonEditor with the specified checkbox, username, and watchlist database.
         *
         * @param checkBox The checkbox component that provides the look-and-feel.
         * @param username The username of the user interacting with the watchlist.
         * @param watchListDB The database handling the watchlist data.
         */
        public ButtonEditor(JCheckBox checkBox, String username, WatchListDatabase watchListDB) {
            super(checkBox);
            this.username = username;
            this.watchListDB = watchListDB;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                if (watchListTable.isEditing()) {
                    watchListTable.getCellEditor().stopCellEditing();
                }
                int selectedRow = watchListTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = watchListTable.convertRowIndexToModel(selectedRow);
                    String movieTitle = (String) watchListTable.getModel().getValueAt(modelRow, 0);
                    watchListDB.removeFromWatchlist(username, movieTitle,watchListTable);
                    watchListTable.revalidate();
                    watchListTable.repaint();
                    refreshWatchList();
                }
            });

        }

        /**
         * Returns the component used for editing the value of a cell.
         *
         * @param table The JTable that is asking the editor to edit.
         * @param value The value of the cell to be edited.
         * @param isSelected True if the cell is to be rendered with selection highlighting.
         * @param row The row index of the cell being edited.
         * @param column The column index of the cell being edited.
         * @return The component for editing.
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value == null ? "" : value.toString());
            return button;
        }
    }

    /**
     * Refreshes the watchlist to reflect any changes.
     */
    private void refreshWatchList() {
        watchListModel.setRowCount(0); // Clear existing rows
        List<String> userWatchList = watchListDB.getWatchlistForUser(username);

        if (userWatchList.isEmpty()) {
            // Optional: Provide feedback for an empty watchlist
            // e.g., show a label with text "Your watchlist is empty."
        } else {
            for (String movieTitle : userWatchList) {
                watchListModel.addRow(new Object[]{movieTitle, "Remove"});
            }
        }
        watchListTable.revalidate();
        watchListTable.repaint();
    }
}