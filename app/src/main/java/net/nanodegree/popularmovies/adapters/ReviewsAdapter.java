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
import net.nanodegree.popularmovies.model.Review;
import net.nanodegree.popularmovies.model.Trailer;

import java.util.ArrayList;

/**
 * Created by antonio on 11/09/15.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private ArrayList<Review> reviews;

    public static class ReviewHolder extends RecyclerView.ViewHolder {

        TextView author;
        TextView content;

        public ReviewHolder(View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    public ReviewsAdapter(ArrayList<Review> list) {
        this.reviews = list;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        ReviewHolder review = new ReviewHolder(view);
        return review;
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {

        Review review = reviews.get(position);

        holder.author.setText(review.author);
        holder.content.setText(review.content);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
