package panel;

import entities.Movie;
import entities.WatchListDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;


public class WatchListPanel extends JPanel {
    private JTable watchListTable;
    private DefaultTableModel watchListModel;
    private WatchListDatabase watchListDB;
    private String username;

    public WatchListPanel(WatchListDatabase watchListDB) {
        this.watchListDB = watchListDB;
        setLayout(new BorderLayout());
        initWatchListTable();
    }

    public void loadUserWatchList(String username) {
        this.username = username;
        populateWatchList(username);
    }
    private void initWatchListTable() {
        watchListTable = new JTable();
        watchListTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        watchListTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(watchListTable);
        add(scrollPane, BorderLayout.CENTER);
    }

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
//        List<String> userWatchList = watchListDB.getWatchlistForUser(username);
//        for (String movieTitle : userWatchList) {
//            watchListModel.addRow(new Object[]{movieTitle, "Remove"});
//        }

        watchListTable.setModel(watchListModel);

        watchListTable.getColumn("Remove").setCellRenderer(new ButtonRenderer());
        watchListTable.getColumn("Remove").setCellEditor(new ButtonEditor(new JCheckBox(), username, watchListDB));

    }


//    public void addToWatchList(Movie movie) {
//        List<String> userWatchList = watchListDB.getWatchlistForUser(username);
//        String movieTitle = movie.getTitle();
//        if (!userWatchList.contains(movieTitle)) {
//            watchListDB.addMovieToWatchlist(username, movieTitle);
//            refreshWatchList();
//        }
//    }


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
        private String username;
        private WatchListDatabase watchListDB;

        public ButtonEditor(JCheckBox checkBox, String username, WatchListDatabase watchListDB) {
            super(checkBox);
            this.username = username;
            this.watchListDB = watchListDB;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                int selectedRow = watchListTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = watchListTable.convertRowIndexToModel(selectedRow);
                    String movieTitle = (String) watchListTable.getModel().getValueAt(modelRow, 0);
                    watchListDB.removeFromWatchlist(username, movieTitle,watchListTable);
                    refreshWatchList();
                }
            });

        }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value == null ? "" : value.toString());
            return button;
        }
    }


    private void refreshWatchList() {
        watchListModel.setRowCount(0); // Clear existing rows
        List<String> userWatchList = watchListDB.getWatchlistForUser(username);
        for (String movieTitle : userWatchList) {
            watchListModel.addRow(new Object[]{movieTitle, "Remove"});
        }
    }
}