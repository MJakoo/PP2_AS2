package entities;

/**
 * Represents a movie with details such as title, director, release year, and running time.
 * This class provides methods to access and modify movie properties.
 */

public class Movie {
    private String title;
    private String director;
    private int releaseYear;
    private int runningTime;

    /**
     * Constructs a new Movie object.
     *
     * @param title The title of the movie.
     * @param director The name of the movie's director.
     * @param releaseYear The year in which the movie was released.
     * @param runningTime The running time of the movie in minutes.
     */

    public Movie(String title, String director, int releaseYear, int runningTime) {

        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.runningTime = runningTime;
    }

    /**
     * Returns the title of the movie.
     *
     * @return The movie title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the movie.
     *
     * @param title The title to set for the movie.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the name of the director of the movie.
     *
     * @return The director's name.
     */
    public String getDirector() {
        return director;
    }

    /**
     * Sets the director of the movie.
     *
     * @param director The name of the director to set for the movie.
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Returns the release year of the movie.
     *
     * @return The year in which the movie was released.
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the release year of the movie.
     *
     * @param releaseYear The release year to set for the movie.
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Returns the running time of the movie.
     *
     * @return The running time of the movie in minutes.
     */
    public int getRunningTime() {
        return runningTime;
    }

    /**
     * Sets the running time of the movie.
     *
     * @param runningTime The running time to set for the movie, in minutes.
     */
    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }


    /**
     * Returns a string representation of the movie.
     * Format: [Title] ([Release Year]), Directed by [Director], Running time: [Running Time] minutes
     *
     * @return A string representing the movie details.
     */
    @Override
    public String toString() {
        return title + " (" + releaseYear + "), Directed by " + director + ", Running time: " + runningTime + " minutes";
    }

}