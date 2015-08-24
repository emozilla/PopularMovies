package net.nanodegree.popularmovies.listeners;

import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.results.ResultList;

/**
 * Created by antonio on 13/07/15.
 */
public interface MovieResultsListener {
    public void onMoviesLoaded(ResultList<MovieBasic> movies);
}
