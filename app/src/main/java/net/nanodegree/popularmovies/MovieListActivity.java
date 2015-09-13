package net.nanodegree.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.nanodegree.popularmovies.fragments.MovieDetailFragment;
import net.nanodegree.popularmovies.fragments.MovieGridFragment;
import net.nanodegree.popularmovies.listeners.MovieCallbacks;
import net.nanodegree.popularmovies.listeners.MovieResultsListener;
import net.nanodegree.popularmovies.misc.Utils;
import net.nanodegree.popularmovies.model.Cast;
import net.nanodegree.popularmovies.model.Movie;
import net.nanodegree.popularmovies.model.parcelable.ParcelableCast;
import net.nanodegree.popularmovies.model.parcelable.ParcelableMovie;
import net.nanodegree.popularmovies.provider.MovieContract;
import net.nanodegree.popularmovies.tasks.MovieDbRequest;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, MovieCallbacks, MovieResultsListener, View.OnClickListener  {

    private boolean mTwoPane;

    private final String DEFAULT_CRITERIA = "popularity.desc";
    private static final int FAVORITES_LOADER = 0;

    private String currentCriteria;
    private ArrayList<Movie> movies;

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

        if (savedInstanceState == null)
            loadMovies();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if ((movies != null) && (movies.size() > 0)) {
            ArrayList parcelableMovies = new ArrayList<ParcelableMovie>();
            for (Movie m : movies)
                parcelableMovies.add(new ParcelableMovie(m));
            savedInstanceState.putParcelableArrayList("movies", parcelableMovies);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("movies")) {

            ArrayList<ParcelableMovie> movieBundle = savedInstanceState.getParcelableArrayList("movies");

            movies = new ArrayList<Movie>();

            for (Movie m : movieBundle)
                movies.add(m);

            onMoviesLoaded(movies);
        }
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
        else if (id == R.id.sort_favorites) {
            currentCriteria = "favorites";
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, MovieContract.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        switch (cursorLoader.getId()) {

            case FAVORITES_LOADER:

                ArrayList<Movie> movies = new ArrayList();

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    try {
                        movies.add(Movie.fromCursor(cursor));
                    } catch (Exception e) { e.printStackTrace(); };

                    cursor.moveToNext();
                }

                if (currentCriteria.equals("favorites"))
                    onMoviesLoaded(movies);

            break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {}

    public void onMoviesLoaded(ArrayList<Movie> movies) {

        this.movies = movies;

        ((MovieGridFragment) getSupportFragmentManager()
                .findFragmentById(R.id.movie_list))
                .setMovies(movies);
    }

    public void onMoviesLoadError() {

        Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_connectivity), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), this)
                .setActionTextColor(Color.RED)
                .show();

    }

    @Override
    public void onClick(View view) {
        loadMovies();
    }


    public void loadMovies() {

        try {

            if (currentCriteria.equals("favorites")) {
                getSupportLoaderManager().initLoader(FAVORITES_LOADER, null, this);
           }
            else {
                new MovieDbRequest(this, Utils.getKeyFromResource(this)).execute(currentCriteria);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
