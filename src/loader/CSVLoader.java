package loader;

import entities.Movie;
import entities.MovieDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVLoader {

    public static MovieDatabase loadMoviesFromCSV(String filePath) {
        MovieDatabase movieDatabase = new MovieDatabase();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Split by commas not enclosed in quotes
                // Assuming the columns match your Movie constructor
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

    private static int parseRunningTime(String runningTimeStr) {
        try {
            return Integer.parseInt(runningTimeStr.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        String moviedb = "src/resources/moviedb.csv";
        String watchlistdb = "src/resources/watchlistdb.txt";
        MovieDatabase movieDatabase = loadMoviesFromCSV(moviedb);
        System.out.println(movieDatabase.toString());
    }
}
