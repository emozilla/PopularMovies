package net.nanodegree.popularmovies.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.enumeration.SortBy;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.results.ResultList;

import net.nanodegree.popularmovies.listeners.MovieResultsListener;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by antonio on 13/07/15.
 */
public class MovieDbRequest extends AsyncTask<ArrayList<String>, Void, ResultList<MovieBasic>> {

    private MovieResultsListener callback;
    private TheMovieDbApi instance;

    public MovieDbRequest(MovieResultsListener callback, TheMovieDbApi instance) {
        this.instance = instance;
        this.callback = callback;
    }

    @Override
    protected ResultList<MovieBasic> doInBackground(ArrayList<String>... args) {

        ResultList<MovieBasic> list = null;
        Discover params = new Discover();
        params.year(Calendar.getInstance().get(Calendar.YEAR));
        ArrayList arguments = args[0];

        if (arguments.get(0).equals("popularity")) {

            if (arguments.get(1).equals("true"))
                params.sortBy(SortBy.POPULARITY_DESC);
            else
                params.sortBy(SortBy.POPULARITY_ASC);
        }
        else if (arguments.get(0).equals("rating")) {

            if (arguments.get(1).equals("true"))
                params.sortBy(SortBy.VOTE_AVERAGE_DESC);
            else
                params.sortBy(SortBy.VOTE_AVERAGE_ASC);
        }

        try {
             list = instance.getDiscoverMovies(params);
        } catch (Exception e) {
            list = null;
        }

        return list;
    }

    @Override
    protected void onPostExecute(ResultList<MovieBasic> movies) {
        try {
            if (callback != null)
                callback.onMoviesLoaded(movies);
        }catch (Exception e) {
            Log.e("POPULAR_MOVIES", "Error en onPostExecute");
            e.printStackTrace();
        }
    }
}
