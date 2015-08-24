package net.nanodegree.popularmovies;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.omertron.themoviedbapi.model.movie.MovieBasic;

import net.nanodegree.popularmovies.fragments.PostersFragment;
import net.nanodegree.popularmovies.fragments.NoConnectivityFragment;
import net.nanodegree.popularmovies.listeners.FragmentInteractionListener;

public class MainActivity extends ActionBarActivity implements FragmentInteractionListener {

    private PostersFragment posters;
    private NoConnectivityFragment ncFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posters = new PostersFragment();
        posters.setInteractionListener(this);

        ncFragment = new NoConnectivityFragment();
        ncFragment.setInteractionListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, posters, "POSTERS")
                .commit();
    }

    @Override
    public void showNoConnectivity() {

        if (getSupportFragmentManager().findFragmentByTag("CONNECTIVITY") == null)
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ncFragment, "CONNECTIVITY")
                .commit();
    }

    @Override
    public void showMovie(MovieBasic movie) {

    }

    @Override
    public void showPosters(boolean reload) {

        getSupportFragmentManager()
                .beginTransaction()
                .remove(ncFragment)
                .commit();

        if (reload)
            posters.doMovieSearch();
    }

}