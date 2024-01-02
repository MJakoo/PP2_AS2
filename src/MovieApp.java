import panel.LoginFrame;
import entities.MovieDatabase;
import loader.CSVLoader;

/**
 * Main class for the Movie Application.
 * This class loads the movie database from a CSV file and initializes the login interface.
 */
public class MovieApp {

    /**
     * The entry point of the application.
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Load the movie database from a CSV file.
        MovieDatabase movieDatabase = CSVLoader.loadMoviesFromCSV();

        // Print the movie database to the console (for debugging or verification).
        System.out.println(movieDatabase.toString());

        // Open the login window.
        new LoginFrame();

    }
}