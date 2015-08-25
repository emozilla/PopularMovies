package net.nanodegree.popularmovies.tasks;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.nanodegree.popularmovies.listeners.MovieResultsListener;
import net.nanodegree.popularmovies.model.Movie;
import net.nanodegree.popularmovies.model.MovieDbResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by antonio on 13/07/15.
 */
public class MovieDbRequest extends AsyncTask<String, Void, ArrayList<Movie>> {

    private MovieResultsListener callback;

    public MovieDbRequest(MovieResultsListener callback) {
        this.callback = callback;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... args) {

        ArrayList<Movie> list = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(args[0]);

            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ObjectMapper mapper = new ObjectMapper();
                MovieDbResult result = mapper.readValue(br, MovieDbResult.class);
                list = result.results;
            }

        } catch (Exception e) {
            callback.onMoviesLoadError();
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        try {
            if (callback != null)
                callback.onMoviesLoaded(movies);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}