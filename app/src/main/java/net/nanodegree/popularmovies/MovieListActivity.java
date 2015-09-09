package net.nanodegree.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.nanodegree.popularmovies.fragments.MovieDetailFragment;
import net.nanodegree.popularmovies.fragments.MovieGridFragment;
import net.nanodegree.popularmovies.listeners.MovieCallbacks;
import net.nanodegree.popularmovies.listeners.MovieResultsListener;
import net.nanodegree.popularmovies.model.Movie;
import net.nanodegree.popularmovies.model.ParcelableMovie;
import net.nanodegree.popularmovies.tasks.MovieDbRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity
        implements MovieCallbacks, MovieResultsListener {

    private boolean mTwoPane;

    private final String DEFAULT_CRITERIA = "popularity.desc";
    private String currentCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        currentCriteria = prefs.getString("criteria", DEFAULT_CRITERIA);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((MovieGridFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.movie_list))
                    .setActivateOnItemClick(true);
        }

        loadMovies();

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_criteria, menu);
        return true;
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

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("criteria", currentCriteria);
        editor.apply();

        loadMovies();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(Movie movie) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", new ParcelableMovie(movie));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra("movie", new ParcelableMovie(movie));
            startActivity(detailIntent);
        }
    }

    private String getKeyFromResource() {

        String strKey = "";

        try {
            InputStream in = getResources().openRawResource(R.raw.themoviedbkey);
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

    public void onMoviesLoaded(ArrayList<Movie> movies) {

        ((MovieGridFragment) getSupportFragmentManager()
                .findFragmentById(R.id.movie_list))
                .setMovies(movies);
    }

    public void onMoviesLoadError() {
        //TODO: Handle error cases here
    }

    public void loadMovies() {

        try {
            new MovieDbRequest(this, getKeyFromResource()).execute(currentCriteria);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
