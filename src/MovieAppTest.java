import entities.Movie;
import entities.MovieDatabase;
import entities.User;
import entities.WatchListDatabase;
import loader.CSVLoader;
import org.junit.Test;
import panel.LoginFrame;

import static org.junit.Assert.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Test suite for Movie Application.
 * This class contains various unit tests that cover the functionality of the Movie Application,
 * including tests for the Movie, User, MovieDatabase, and WatchListDatabase classes.
 * It ensures that each component of the application functions as expected.
 */
public class MovieAppTest {

    /**
     * Tests the initialization of the Movie Application.
     * Verifies that the MovieDatabase is correctly loaded and the LoginFrame is displayed.
     */
    @Test
    public void testAppInitialization() {
        MovieApp.main(new String[]{});

        // 1. Test if the MovieDatabase is loaded correctly
        MovieDatabase movieDatabase = CSVLoader.loadMoviesFromCSV();
        assertNotNull("Movie database should be loaded", movieDatabase);

        // 2. Test if the LoginFrame is displayed
        boolean loginFrameVisible = false;
        Frame[] frames = JFrame.getFrames();
        for (Frame frame : frames) {
            if (frame instanceof LoginFrame && frame.isVisible()) {
                loginFrameVisible = true;
                break;
            }
        }
        assertTrue("Login frame should be visible", loginFrameVisible);
    }

    /**
     * Tests the creation of a Movie object.
     * Ensures that the Movie object is created with the correct title, director, release year, and running time.
     */
    @Test
    public void testMovieCreation() {
        Movie movie = new Movie("Inception", "Christopher Nolan", 2010, 148);
        assertEquals("Inception", movie.getTitle());
        assertEquals("Christopher Nolan", movie.getDirector());
        assertEquals(2010, movie.getReleaseYear());
        assertEquals(148, movie.getRunningTime());
    }

    /**
     * Tests the creation of a Movie object with an invalid year.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void createMovie_InvalidYear_ShouldThrowException() {
        new Movie("Test Movie", "Test Director", -1, 120);
    }

    /**
     * Tests successful login functionality for the User class.
     * Assumes "admin" with "admin" is a valid user.
     */
    @Test
    public void testUserLogin() {
        // Assuming "user123" with "password" is a valid user
        assertTrue(User.login("admin", "admin"));
        // Assuming "invalidUser" with any password is invalid
        assertFalse(User.login("invalidUser", "password"));
    }

    /**
     * Tests User creation with an empty password.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUserCreation_EmptyPassword_ShouldThrowException() {
        new User("username", "");
    }

    /**
     * Tests User creation with a null username.
     * Expects an IllegalArgumentException to be thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUserCreation_NullUsername_ShouldThrowException() {
        new User(null, "password123");
    }

    /**
     * Tests adding a new movie to the MovieDatabase.
     * Verifies the movie is added and can be retrieved.
     */
    @Test
    public void testMovieDatabase() {
        MovieDatabase db = new MovieDatabase();
        Movie movie = new Movie("The Matrix", "Lana Wachowski", 1999, 136);
        db.addMovie(movie);
        assertEquals(movie, db.getMovie("The Matrix").orElse(null));
    }

    /**
     * Tests the removal of a movie from the MovieDatabase.
     * Verifies that the movie is successfully removed and no longer retrievable.
     */
    @Test
    public void testRemoveMovie() {
        MovieDatabase db = new MovieDatabase();
        Movie movie = new Movie("Test Movie", "Test Director", 2000, 150);
        db.addMovie(movie);
        assertTrue(db.removeMovie("Test Movie"));
        assertFalse(db.getMovie("Test Movie").isPresent());
    }

    /**
     * Tests adding a duplicate movie to the MovieDatabase.
     * Verifies that duplicate movies cannot be added.
     */
    @Test
    public void testDuplicateMovieAddition() {
        MovieDatabase db = new MovieDatabase();
        Movie movie1 = new Movie("Duplicate", "Director", 2000, 150);
        Movie movie2 = new Movie("Duplicate", "Director", 2000, 150);
        db.addMovie(movie1);
        assertFalse("Should not add duplicate movie", db.addMovie(movie2));
    }

    /**
     * Tests adding a movie to a user's watchlist in WatchListDatabase.
     * Verifies that the movie is added to the user's watchlist.
     */
    @Test
    public void testWatchListDatabase() {
        WatchListDatabase watchListDB = new WatchListDatabase();
        watchListDB.addMovieToWatchlist("admin", "Inception");
        assertTrue(watchListDB.getWatchlistForUser("admin").contains("Inception"));
    }

    /**
     * Tests removing a movie from a user's watchlist in WatchListDatabase.
     * Verifies that the movie is removed from the watchlist.
     */
    @Test
    public void testRemoveFromWatchlist() {
        WatchListDatabase watchListDB = new WatchListDatabase();
        watchListDB.addMovieToWatchlist("user123", "Inception");

        // Create a dummy JTable with a model and a row
        DefaultTableModel model = new DefaultTableModel(
                new Object[][] {{"Inception", "Remove"}},
                new String[] {"Movie", "Action"}
        );
        JTable dummyTable = new JTable(model);

        // Simulate a row selection
        if (model.getRowCount() > 0) {
            dummyTable.setRowSelectionInterval(0, 0);
        }

        // Call the removeFromWatchlist method with the dummy JTable
        watchListDB.removeFromWatchlist("user123", "Inception", dummyTable);

        // Check if the movie is removed from the watchlist
        assertFalse("Inception should be removed from the watchlist",
                watchListDB.getWatchlistForUser("user123").contains("Inception"));
    }

    /**
     * Tests adding a duplicate movie to a user's watchlist in WatchListDatabase.
     * Verifies that duplicate movies cannot be added to the watchlist.
     */

    @Test
    public void testAddDuplicateToWatchlist() {
        WatchListDatabase watchListDB = new WatchListDatabase();
        watchListDB.addMovieToWatchlist("user123", "Inception");

        // First check: The movie should be added successfully
        assertTrue("Inception should be in the watchlist",
                watchListDB.getWatchlistForUser("user123").contains("Inception"));

        // Try adding the same movie again
        watchListDB.addMovieToWatchlist("user123", "Inception");

        // Check the number of occurrences of the movie in the watchlist
        long count = watchListDB.getWatchlistForUser("user123").stream()
                .filter(title -> title.equals("Inception"))
                .count();

        // Assert that the movie only appears once, indicating no duplicates were added
        assertEquals("There should be no duplicates in the watchlist", 1, count);
    }

    /**
     * Tests the functionality of CSVLoader to load movies into the MovieDatabase.
     * Verifies that the MovieDatabase is not empty after loading.
     */
    @Test
    public void testCSVLoader() {
        MovieDatabase movieDatabase = CSVLoader.loadMoviesFromCSV();
        assertNotNull(movieDatabase);
    }

    /**
     * Tests CSVLoader with a valid file.
     * Verifies that the data is correctly loaded from the CSV file into the MovieDatabase.
     */
    @Test
    public void testCSVLoaderFileValidation() {
        MovieDatabase movieDatabase = CSVLoader.loadMoviesFromCSV();
        assertTrue("Movie database should have movies loaded", !movieDatabase.getAllMovies().isEmpty());
    }

}
