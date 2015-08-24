package net.nanodegree.popularmovies.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.results.ResultList;

import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.adapters.GridPostersAdapter;
import net.nanodegree.popularmovies.listeners.FragmentInteractionListener;
import net.nanodegree.popularmovies.listeners.MovieResultsListener;
import net.nanodegree.popularmovies.tasks.MovieDbRequest;

import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

public class PostersFragment extends Fragment implements MovieResultsListener {

    private ProgressDialog progress;
    private TheMovieDbApi instance;
    private FragmentInteractionListener interactionListener;
    private boolean sortDescending = true;
    private String criteria = "popularity";
    private ResultList<MovieBasic> movies;

    public PostersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState==null) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_movie_posters, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies, menu);
    }

    @Override
    public void onMoviesLoaded(ResultList<MovieBasic> movies) {

        if (progress != null)
            progress.dismiss();

        if (movies != null) {
            this.movies = movies;
            GridView posters = (GridView) getActivity().findViewById(R.id.posters);
            GridPostersAdapter adapter = new GridPostersAdapter(getActivity(), R.layout.grid_item_layout, new ArrayList(movies.getResults()));
            posters.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sort_order) {

            if (sortDescending) {
                sortDescending = false;
                item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_drop_up_white_36dp));
            }
            else {
                sortDescending = true;
                item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_drop_down_white_36dp));
            }
        }
        else if (id == R.id.sort_popular) {
            criteria = "popularity";
        }
        else if (id == R.id.sort_rating) {
            criteria = "rating";
        }

        doMovieSearch();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void setInteractionListener(FragmentInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    private void setApiInstance() {
        try {
            instance = new TheMovieDbApi("21a32bdbe87dae42d59a35ccf8c4cc9d", new DefaultHttpClient());
        } catch (MovieDbException e) {
            Log.e("POPULAR_MOVIES", e.getMessage());
        }
    }

    public void doMovieSearch() {

        if (instance == null)
            setApiInstance();

        progress = ProgressDialog.show(getActivity(), getString(R.string.progress_title), getString(R.string.progress_text), true);

        ArrayList<String> arguments = new ArrayList<String>();
        arguments.add(criteria);
        arguments.add(Boolean.toString(sortDescending));

        try {
            new MovieDbRequest(this, instance).execute(arguments);
        }
        catch (Exception e) {
            interactionListener.showNoConnectivity();
        }
    }
}