package net.nanodegree.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.model.Cast;
import net.nanodegree.popularmovies.model.Review;
import net.nanodegree.popularmovies.model.Trailer;

import java.util.ArrayList;

/**
 * Created by antonio on 11/09/15.
 */
public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.CastHolder> {

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private final String PROFILE_RESOLUTION = "w92";

    private ArrayList<Cast> cast;
    private Context context;

    public static class CastHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public CastHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.cast_image);
            name = (TextView) itemView.findViewById(R.id.cast_name);
        }
    }

    public MovieCastAdapter(ArrayList<Cast> list, Context context) {
        this.context = context;
        this.cast = list;
    }

    @Override
    public CastHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_item, parent, false);

        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(CastHolder holder, int position) {

        Cast c = cast.get(position);

        Picasso.with(this.context).load(IMAGE_BASE_URL + PROFILE_RESOLUTION + c.profileImage)
                .error(R.drawable.ic_profile_fallback).fit().into(holder.image);


        holder.name.setText(c.name);
    }

    @Override
    public int getItemCount() {
        return cast.size();
    }
}