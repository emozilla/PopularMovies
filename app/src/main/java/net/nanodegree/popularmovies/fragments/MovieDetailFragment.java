package net.nanodegree.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.model.Movie;


public class MovieDetailFragment extends Fragment {

    private Movie movie;

    public MovieDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("movie")) {
            movie = getArguments().getParcelable("movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (movie != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movie.originalTitle);
        }

        return rootView;
    }
}
