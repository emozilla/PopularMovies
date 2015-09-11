package net.nanodegree.popularmovies.tasks;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.nanodegree.popularmovies.listeners.MovieDetailsListener;
import net.nanodegree.popularmovies.model.MovieDbTrailerResult;
import net.nanodegree.popularmovies.model.Trailer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by antonio on 10/09/15.
 */
public class MovieDbTrailerRequest extends AsyncTask<Integer, Void, ArrayList<Trailer>> {

    private MovieDetailsListener callback;

    private final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private String apiKey = null;

    public MovieDbTrailerRequest(MovieDetailsListener callback, String apiKey) {
        this.callback = callback;
        this.apiKey = apiKey;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(Integer... args) {

        ArrayList<Trailer> list = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(buildQueryURL(args[0]));

            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ObjectMapper mapper = new ObjectMapper();
                MovieDbTrailerResult result = mapper.readValue(br, MovieDbTrailerResult.class);
                list = result.results;
            }

        } catch (Exception e) {
            callback.onTrailersLoadError();
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        try {
            if (callback != null)
                callback.onTrailersLoaded(trailers);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String buildQueryURL(Integer movieId){

        String url = BASE_URL +
                movieId.toString() +
                "/videos?" +
                "api_key=" + apiKey;

        System.out.println(url);

        return  url;
    }

}
