package net.nanodegree.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.model.Cast;
import net.nanodegree.popularmovies.model.Movie;

import java.util.ArrayList;

/**
 * Created by antonio on 10/09/15.
 */
public class MovieCastAdapter extends ArrayAdapter {

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private final String PROFILE_RESOLUTION = "w45";

    private Context context;
    private int layout;
    private ArrayList data;

    public MovieCastAdapter(Context context, int layout, ArrayList data) {
        super(context, layout, data);
        this.layout = layout;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CastHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            holder = new CastHolder();
            holder.profileImage = (ImageView)convertView.findViewById(R.id.cast_image);
           // holder.name = (TextView)convertView.findViewById(R.id.cast_name);
           // holder.character = (TextView)convertView.findViewById(R.id.cast_character);

            convertView.setTag(holder);
        }
        else {
            holder = (CastHolder) convertView.getTag();
        }

        Cast cast = (Cast) data.get(position);

        if (cast != null) {

            Picasso.with(this.context).load(IMAGE_BASE_URL + PROFILE_RESOLUTION + cast.profileImage)
                    .error(R.drawable.ic_profile_fallback).fit().into(holder.profileImage);

         //   holder.name.setText(cast.name);
         //   holder.character.setText(cast.character);

        }

        return convertView;
    }

    static class CastHolder
    {
        ImageView profileImage;
       // TextView name;
       // TextView character;
    }
}
