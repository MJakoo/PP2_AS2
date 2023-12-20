package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDatabase {
    private List<Movie> movies;

    public MovieDatabase() {
        movies = new ArrayList<>();
    }

    public boolean addMovie(Movie movie) {
        if (movies.stream().noneMatch(m -> m.getTitle().equals(movie.getTitle()))) {
            movies.add(movie);
            return true;
        }
        return false;
    }

    public boolean removeMovie(String title) {
        return movies.removeIf(m -> m.getTitle().equals(title));
    }

    public Optional<Movie> getMovie(String title) {
        return movies.stream().filter(m -> m.getTitle().equals(title)).findFirst();
    }

    public List<Movie>  getAllMovies() {
        return new ArrayList<>(movies);
    }


    @Override
    public String toString() {
        return String.valueOf(getAllMovies());
    }
}