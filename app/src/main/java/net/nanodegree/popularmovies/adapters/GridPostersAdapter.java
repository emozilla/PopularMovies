package net.nanodegree.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.model.Movie;

import java.util.ArrayList;

/**
 * Created by antonio on 14/07/15.
 */
public class GridPostersAdapter extends ArrayAdapter {

    private final String BASE_URL = "http://image.tmdb.org/t/p/";

    private Context context;
    private int layout;
    private ArrayList data;

    public GridPostersAdapter(Context context, int layout, ArrayList data) {
        super(context, layout, data);
        this.layout = layout;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            holder = new MovieHolder();
            holder.moviePoster = (ImageView)convertView.findViewById(R.id.poster);
            convertView.setTag(holder);
        }
        else {
            holder = (MovieHolder) convertView.getTag();
        }

        Picasso.with(this.context).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
                .fit().error(R.drawable.ic_error_fallback).into(holder.moviePoster);

        Movie movie = (Movie) data.get(position);

        if (movie != null)
            Picasso.with(this.context).load(BASE_URL + "w185" + movie.poster)
                    .fit().error(R.drawable.ic_error_fallback).into(holder.moviePoster);

        return convertView;
    }

    static class MovieHolder
    {
        ImageView moviePoster;
    }
}