package loader;

import entities.Movie;
import entities.MovieDatabase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to load movies from a CSV file into a MovieDatabase
 * and to delete movies by title from the CSV file.
 */
public class CSVLoader {
    static String DB_PATH = "src/resources/moviedb.csv";

    /**
     * Loads movies from a CSV file and adds them to a MovieDatabase.
     * The CSV file is expected to have specific columns for title, director, release year, and running time.
     *
     * @return A MovieDatabase populated with movies from the CSV file.
     */
    public static MovieDatabase loadMoviesFromCSV() {
        MovieDatabase movieDatabase = new MovieDatabase();

        try (BufferedReader br = new BufferedReader(new FileReader(DB_PATH))) {
            String line;
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Split by commas not enclosed in quotes
                String title = values[1]; // or the appropriate index
                String director = values[6]; // or the appropriate index
                int releaseYear = Integer.parseInt(values[2].trim()); // or the appropriate index
                int runningTime = parseRunningTime(values[3]); // Custom method to parse running time

                Movie movie = new Movie(title, director, releaseYear, runningTime);
                movieDatabase.addMovie(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieDatabase;
    }

    /**
     * Parses the running time from a string, extracting numerical value.
     *
     * @param runningTimeStr The string representation of the running time.
     * @return The running time as an integer. Returns 0 if parsing fails.
     */
    private static int parseRunningTime(String runningTimeStr) {
        try {
            return Integer.parseInt(runningTimeStr.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Deletes a movie from the CSV file based on its title.
     *
     * @param titleToDelete The title of the movie to delete.
     * @return true if the movie was successfully deleted, false otherwise.
     */
    public boolean deleteMovieByTitle(String titleToDelete) {
        List<String> lines = new ArrayList<>();
        boolean movieDeleted = false;

        try (BufferedReader br = new BufferedReader(new FileReader(DB_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                String title = values[1].trim(); // Assuming title is in the second column (index 1)

                if (!title.equalsIgnoreCase(titleToDelete)) {
                    lines.add(line);
                } else {
                    movieDeleted = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (movieDeleted) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB_PATH))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                movieDeleted = false;
            }
        }

        return movieDeleted;
    }

    /**
     * The main method to demonstrate the loading of movies from a CSV file.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        MovieDatabase movieDatabase = loadMoviesFromCSV();
        System.out.println(movieDatabase);
    }
}
