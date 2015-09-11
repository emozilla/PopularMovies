package net.nanodegree.popularmovies.listeners;

import net.nanodegree.popularmovies.model.Cast;
import net.nanodegree.popularmovies.model.Review;
import net.nanodegree.popularmovies.model.Trailer;

import java.util.ArrayList;

/**
 * Created by antonio on 10/09/15.
 */
public interface MovieDetailsListener {
    public void onCastLoaded(ArrayList<Cast> cast);
    public void onCastLoadError();
    public void onTrailersLoaded(ArrayList<Trailer> trailers);
    public void onTrailersLoadError();
    public void onReviewsLoaded(ArrayList<Review> reviews);
    public void onReviewsLoadError();
}
