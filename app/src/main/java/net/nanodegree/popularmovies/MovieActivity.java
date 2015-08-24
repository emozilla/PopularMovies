package net.nanodegree.popularmovies;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.nanodegree.popularmovies.model.Movie;
import net.nanodegree.popularmovies.model.ParcelableMovie;


public class MovieActivity extends ActionBarActivity {

    private ParcelableMovie movie;
    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);
        ImageView poster = (ImageView) findViewById(R.id.poster);
        ImageView backdrop = (ImageView) findViewById(R.id.backdrop);

        movie = (ParcelableMovie) getIntent().getParcelableExtra("movie");

        if (movie != null) {

            getSupportActionBar().setTitle(movie.title);

            Picasso.with(this).load(IMAGE_BASE_URL + "w185" + movie.poster)
                    .fit().error(R.drawable.ic_error_fallback).into(poster);

            Picasso.with(this).load(IMAGE_BASE_URL + "w342" + movie.backdrop)
                    .fit().error(R.drawable.ic_error_fallback).into(backdrop);
        }

    }
}
