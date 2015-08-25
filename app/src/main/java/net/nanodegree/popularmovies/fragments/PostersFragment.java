package net.nanodegree.popularmovies.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import net.nanodegree.popularmovies.MovieActivity;
import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.adapters.GridPostersAdapter;
import net.nanodegree.popularmovies.listeners.FragmentInteractionListener;
import net.nanodegree.popularmovies.listeners.MovieResultsListener;
import net.nanodegree.popularmovies.model.Movie;
import net.nanodegree.popularmovies.model.ParcelableMovie;
import net.nanodegree.popularmovies.tasks.MovieDbRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class PostersFragment extends Fragment implements MovieResultsListener {

    private ProgressDialog progress;
    private FragmentInteractionListener interactionListener;

    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private String apiKey = null;
    private String currentCriteria = "popularity.desc";

    private boolean hasData = false;

    public PostersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
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

        inflater.inflate(R.menu.menu_posters, menu);
    }

    @Override
    public void onMoviesLoaded(ArrayList<Movie> movies) {

        if (progress != null)
            progress.dismiss();

        if (movies != null) {
            GridView posters = (GridView) getActivity().findViewById(R.id.posters);
            GridPostersAdapter adapter = new GridPostersAdapter(getActivity(),
                    R.layout.grid_item_layout, movies);
            posters.setAdapter(adapter);

            final ArrayList<Movie> movieList = movies;
            posters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), MovieActivity.class);
                    intent.putExtra("movie", new ParcelableMovie(movieList.get(position)));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onMoviesLoadError() {
        interactionListener.showNoConnectivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sort_popular) {
            currentCriteria = "popularity.desc";
        }
        else if (id == R.id.sort_rating) {
            currentCriteria = "vote_average.desc";
        }

        doMovieSearch();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            doMovieSearch();
        }
    }

    public void setInteractionListener(FragmentInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public String buildQueryURL(){

        if (apiKey == null)
            apiKey = getKeyFromResource(getActivity());

        String url = BASE_URL +
                "primary_release_year="  + Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) + "&" +
                "language=en" + "&" +
                "sort_by=" + currentCriteria + "&" +
                "api_key=" + apiKey;

        return  url;
    }

    private static String getKeyFromResource(Context context) {

        String strKey = "";

        try {
            InputStream in = context.getResources().openRawResource(R.raw.themoviedbkey);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();

            while ((strKey = r.readLine()) != null) {
                total.append(strKey);
            }

            strKey = total.toString();
            strKey = strKey.trim();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return strKey;
    }

    public void doMovieSearch() {

        progress = ProgressDialog.show(getActivity(), getString(R.string.progress_title), getString(R.string.progress_text), true);

        try {
            new MovieDbRequest(this).execute(buildQueryURL());
        }
        catch (Exception e) {
            interactionListener.showNoConnectivity();
        }
    }
}