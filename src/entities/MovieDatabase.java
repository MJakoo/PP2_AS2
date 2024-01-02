package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a database for storing and managing a collection of Movie objects.
 * Provides methods for adding, removing, and retrieving movies.
 */
public class MovieDatabase {
    private List<Movie> movies;

    /**
     * Constructs an empty MovieDatabase.
     */
    public MovieDatabase() {
        movies = new ArrayList<>();
    }

    /**
     * Adds a new movie to the database.
     * A movie is added only if there is no other movie with the same title already in the database.
     *
     * @param movie The Movie object to be added.
     * @return true if the movie was successfully added, false if a movie with the same title already exists.
     */
    public boolean addMovie(Movie movie) {
        if (movies.stream().noneMatch(m -> m.getTitle().equals(movie.getTitle()))) {
            movies.add(movie);
            return true;
        }
        return false;
    }

    /**
     * Removes a movie from the database based on its title.
     *
     * @param title The title of the movie to be removed.
     * @return true if the movie was successfully removed, false if no movie with the given title was found.
     */
    public boolean removeMovie(String title) {
        return movies.removeIf(m -> m.getTitle().equals(title));
    }

    /**
     * Retrieves a movie from the database based on its title.
     *
     * @param title The title of the movie to be retrieved.
     * @return An Optional containing the Movie if found, or an empty Optional if no movie with the given title exists.
     */
    public Optional<Movie> getMovie(String title) {
        return movies.stream().filter(m -> m.getTitle().equals(title)).findFirst();
    }

    /**
     * Retrieves a list of all movies in the database.
     *
     * @return A List of all the Movie objects in the database.
     */
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    /**
     * Returns a string representation of the entire movie database.
     * The string representation consists of a list of all movies in the database.
     *
     * @return A string representing all the movies in the database.
     */
    @Override
    public String toString() {
        return String.valueOf(getAllMovies());
    }
}