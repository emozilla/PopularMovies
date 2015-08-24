package net.nanodegree.popularmovies.listeners;

import com.omertron.themoviedbapi.model.movie.MovieBasic;

/**
 * Created by antonio on 23/08/15.
 */
public interface FragmentInteractionListener {
    public void showNoConnectivity();
    public void showMovie(MovieBasic movie);
    public void showPosters(boolean reload);
}
