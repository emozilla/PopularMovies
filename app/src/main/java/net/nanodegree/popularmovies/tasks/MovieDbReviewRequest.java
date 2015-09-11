package net.nanodegree.popularmovies.tasks;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.nanodegree.popularmovies.listeners.MovieDetailsListener;
import net.nanodegree.popularmovies.model.MovieDbReviewResult;
import net.nanodegree.popularmovies.model.Review;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by antonio on 10/09/15.
 */
public class MovieDbReviewRequest extends AsyncTask<Integer, Void, ArrayList<Review>> {

    private MovieDetailsListener callback;

    private final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private String apiKey = null;

    public MovieDbReviewRequest(MovieDetailsListener callback, String apiKey) {
        this.callback = callback;
        this.apiKey = apiKey;
    }

    @Override
    protected ArrayList<Review> doInBackground(Integer... args) {

        ArrayList<Review> list = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(buildQueryURL(args[0]));

            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ObjectMapper mapper = new ObjectMapper();
                MovieDbReviewResult result = mapper.readValue(br, MovieDbReviewResult.class);
                list = result.results;
            }

        } catch (Exception e) {
            callback.onReviewsLoadError();
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        try {
            if (callback != null)
                callback.onReviewsLoaded(reviews);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String buildQueryURL(Integer movieId){

        String url = BASE_URL +
                movieId.toString() +
                "/reviews?" +
                "api_key=" + apiKey;

        return  url;
    }
}