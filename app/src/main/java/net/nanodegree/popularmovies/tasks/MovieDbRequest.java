package net.nanodegree.popularmovies.tasks;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.nanodegree.popularmovies.listeners.MovieResultsListener;
import net.nanodegree.popularmovies.model.Movie;
import net.nanodegree.popularmovies.model.MovieDbResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by antonio on 13/07/15.
 */
public class MovieDbRequest extends AsyncTask<String, Void, ArrayList<Movie>> {

    private MovieResultsListener callback;

    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private String apiKey = null;

    public MovieDbRequest(MovieResultsListener callback, String apiKey) {
        this.callback = callback;
        this.apiKey = apiKey;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... args) {

        ArrayList<Movie> list = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(buildQueryURL(args[0]));

            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ObjectMapper mapper = new ObjectMapper();
                /* FIX: Due to impossibility to map genre ids */
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

    public String buildQueryURL(String criteria){

        String url = BASE_URL +
                "primary_release_year="  + Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) + "&" +
                "language=en" + "&" +
                "sort_by=" + criteria + "&" +
                "api_key=" + apiKey;

        return  url;
    }
}