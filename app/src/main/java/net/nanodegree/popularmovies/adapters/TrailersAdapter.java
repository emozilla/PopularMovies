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
import net.nanodegree.popularmovies.listeners.TrailerListener;
import net.nanodegree.popularmovies.model.Trailer;

import java.util.ArrayList;

/**
 * Created by antonio on 11/09/15.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerHolder> {

    private final String IMAGE_BASE_URL = "http://img.youtube.com/vi/";

    private ArrayList<Trailer> trailers;
    private static TrailerListener callback;
    private Context context;

    public static class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnail;
        TextView  title;

        public TrailerHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.trailer_title);
            thumbnail = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (callback != null)
                callback.onTrailerSelected(getAdapterPosition(), v);
        }
    }

    public void setCallback(TrailerListener listener) {
        this.callback = listener;
    }

    public TrailersAdapter(ArrayList<Trailer> list, Context context) {
        this.context = context;
        this.trailers = list;
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);

        TrailerHolder trailer = new TrailerHolder(view);
        return trailer;
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {

        Trailer trailer = trailers.get(position);
        holder.title.setText(trailer.name);

        Picasso.with(context).load(IMAGE_BASE_URL + trailer.key + "/hqdefault.jpg")
                .error(R.drawable.ic_backdrop_fallback).fit().into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
