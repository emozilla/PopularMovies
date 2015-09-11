package net.nanodegree.popularmovies.tasks;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.nanodegree.popularmovies.listeners.MovieDetailsListener;
import net.nanodegree.popularmovies.model.Cast;
import net.nanodegree.popularmovies.model.MovieDbCastResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by antonio on 10/09/15.
 */
public class MovieDbCastRequest extends AsyncTask<Integer, Void, ArrayList<Cast>> {

    private MovieDetailsListener callback;

    private final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private String apiKey = null;

    public MovieDbCastRequest(MovieDetailsListener callback, String apiKey) {
        this.callback = callback;
        this.apiKey = apiKey;
    }

    @Override
    protected ArrayList<Cast> doInBackground(Integer... args) {

        ArrayList<Cast> list = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(buildQueryURL(args[0]));

            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ObjectMapper mapper = new ObjectMapper();
                MovieDbCastResult result = mapper.readValue(br, MovieDbCastResult.class);
                list = result.cast;
            }

        } catch (Exception e) {
            callback.onCastLoadError();
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Cast> cast) {
        try {
            if (callback != null)
                callback.onCastLoaded(cast);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String buildQueryURL(Integer movieId){

        String url = BASE_URL +
                movieId.toString() +
                "/casts?" +
                "api_key=" + apiKey;

        return  url;
    }
}