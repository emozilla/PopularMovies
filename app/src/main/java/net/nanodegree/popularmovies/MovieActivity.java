package net.nanodegree.popularmovies;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.nanodegree.popularmovies.model.ParcelableMovie;

import java.text.SimpleDateFormat;

public class MovieActivity extends ActionBarActivity {

    private ParcelableMovie movie;
    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView poster = (ImageView) findViewById(R.id.poster);
        ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
        TextView plot_synopsis = (TextView) findViewById(R.id.plot_synopsis);
        TextView rating = (TextView) findViewById(R.id.rating);
        RatingBar bar = (RatingBar) findViewById(R.id.ratingBar);
        TextView release = (TextView) findViewById(R.id.release_date);

        movie = (ParcelableMovie) getIntent().getParcelableExtra("movie");

        if (movie != null) {

            getSupportActionBar().setTitle(movie.title);
            plot_synopsis.setText(movie.overview);

            bar.setRating(movie.vote_average/2);
            rating.setText(Float.toString(movie.vote_average));

            release.setText(new SimpleDateFormat("MMM dd yyyy").format(movie.release));

            Picasso.with(this).load(IMAGE_BASE_URL + "w92" + movie.poster)
                    .error(R.drawable.ic_poster_fallback).fit().into(poster);

            Picasso.with(this).load(IMAGE_BASE_URL + "w342" + movie.backdrop)
                    .error(R.drawable.ic_backdrop_fallback).fit().into(backdrop);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}