package net.nanodegree.popularmovies.listeners;

import net.nanodegree.popularmovies.model.Movie;
import java.util.ArrayList;

/**
 * Created by antonio on 13/07/15.
 */
public interface MovieResultsListener {
    public void onMoviesLoaded(ArrayList<Movie> movies);
    public void onMoviesLoadError();
}
