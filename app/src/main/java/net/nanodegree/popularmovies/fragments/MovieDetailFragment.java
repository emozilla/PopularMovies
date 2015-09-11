package net.nanodegree.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.adapters.MovieCastAdapter;
import net.nanodegree.popularmovies.adapters.ReviewsAdapter;
import net.nanodegree.popularmovies.adapters.TrailersAdapter;
import net.nanodegree.popularmovies.listeners.MovieDetailsListener;
import net.nanodegree.popularmovies.model.Cast;
import net.nanodegree.popularmovies.model.parcelable.ParcelableCast;
import net.nanodegree.popularmovies.model.parcelable.ParcelableMovie;
import net.nanodegree.popularmovies.model.parcelable.ParcelableReview;
import net.nanodegree.popularmovies.model.parcelable.ParcelableTrailer;
import net.nanodegree.popularmovies.model.Review;
import net.nanodegree.popularmovies.model.Trailer;
import net.nanodegree.popularmovies.tasks.MovieDbCastRequest;
import net.nanodegree.popularmovies.misc.Utils;
import net.nanodegree.popularmovies.tasks.MovieDbReviewRequest;
import net.nanodegree.popularmovies.tasks.MovieDbTrailerRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MovieDetailFragment extends Fragment implements MovieDetailsListener {

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private ParcelableMovie movie;
    private ArrayList<Cast> castList;
    private ArrayList<Trailer> trailersList;
    private ArrayList<Review> reviewsList;

    private MovieDbCastRequest castRequest;
    private MovieDbTrailerRequest trailerRequest;
    private MovieDbReviewRequest reviewRequest;

    public MovieDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            retrieveInstanceState(savedInstanceState);
        }
        else {

            if (getArguments().containsKey("movie")) {

                movie = getArguments().getParcelable("movie");

                castRequest = new MovieDbCastRequest(this, Utils.getKeyFromResource(getActivity()));
                castRequest.execute(new Integer(movie.id));

                trailerRequest = new MovieDbTrailerRequest(this, Utils.getKeyFromResource(getActivity()));
                trailerRequest.execute(new Integer(movie.id));

                reviewRequest = new MovieDbReviewRequest(this, Utils.getKeyFromResource(getActivity()));
                reviewRequest.execute(new Integer(movie.id));
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            retrieveInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        /* The best way to do this is in the activity i think*/
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.movie_detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* *********************************************** */

        if (savedInstanceState != null) {
            retrieveInstanceState(savedInstanceState);
        }

        populate(rootView);

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if ((castList != null) && (castList.size() > 0)) {
            ArrayList parcelableCasts = new ArrayList<ParcelableCast>();
            for (Cast castObject : castList)
                parcelableCasts.add(new ParcelableCast(castObject));
            outState.putParcelableArrayList("cast", parcelableCasts);
        }

        if ((trailersList != null) && (trailersList.size() > 0)) {
            ArrayList parcelableTrailers = new ArrayList<ParcelableTrailer>();
            for (Trailer trailerObject : trailersList)
                parcelableTrailers.add(new ParcelableTrailer(trailerObject));
            outState.putParcelableArrayList("trailers", parcelableTrailers);
        }

        if ((reviewsList != null) && (reviewsList.size() > 0)) {

            ArrayList parcelableReviews = new ArrayList<ParcelableReview>();
            for (Review reviewObject : reviewsList)
                parcelableReviews.add(new ParcelableReview(reviewObject));
            outState.putParcelableArrayList("reviews", parcelableReviews);
        }

        outState.putParcelable("movie", movie);
    }

    private void retrieveInstanceState(Bundle savedInstanceState) {

        if (movie != null)
            return;

        movie = savedInstanceState.getParcelable("movie");

        if (savedInstanceState.containsKey("cast")) {

            ArrayList<ParcelableCast> castBundle = savedInstanceState.getParcelableArrayList("cast");

            castList = new ArrayList<Cast>();

            for (Cast castObject : castBundle)
                castList.add(castObject);
        }

        if (savedInstanceState.containsKey("trailers")) {

            ArrayList<ParcelableTrailer> trailersBundle = savedInstanceState.getParcelableArrayList("trailers");

            trailersList = new ArrayList<Trailer>();

            for (Trailer trailerObject : trailersBundle)
                trailersList.add(trailerObject);
        }

        if (savedInstanceState.containsKey("reviews")) {

            ArrayList<ParcelableReview> reviewsBundle = savedInstanceState.getParcelableArrayList("reviews");

            reviewsList = new ArrayList<Review>();

            for (Review reviewsObject : reviewsBundle)
                reviewsList.add(reviewsObject);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (castRequest != null)
            castRequest.cancel(true);

        if (trailerRequest != null)
            trailerRequest.cancel(true);

        if (reviewRequest != null)
            reviewRequest.cancel(true);
    }

    @Override
    public void onCastLoaded(ArrayList<Cast> cast) {

        if ((cast != null) && (cast.size() > 0)) {
            this.castList = cast;
            MovieCastAdapter adapter = new MovieCastAdapter(getActivity(), R.layout.cast_list_item, cast);
            ((ListView) getView().findViewById(R.id.movie_detail_cast)).setAdapter(adapter);
            getView().findViewById(R.id.movie_detail_cast).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCastLoadError() {
        getView().findViewById(R.id.movie_detail_cast).setVisibility(View.GONE);
    }

    @Override
    public void onTrailersLoaded(ArrayList<Trailer> trailers) {

        if ((trailers != null) && (trailers.size() > 0)) {

            this.trailersList = trailers;

            TrailersAdapter adapter = new TrailersAdapter(trailersList, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            ((RecyclerView) getView().findViewById(R.id.movie_detail_trailers)).setHasFixedSize(false);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_trailers)).setLayoutManager(mLayoutManager);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_trailers)).setAdapter(adapter);
            getView().findViewById(R.id.movie_detail_trailers).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrailersLoadError() {
        getView().findViewById(R.id.movie_detail_trailers).setVisibility(View.GONE);
    }

    @Override
    public void onReviewsLoaded(ArrayList<Review> reviews) {

        if ((reviews != null) && (reviews.size() > 0)) {

            this.reviewsList = reviews;

            ReviewsAdapter adapter = new ReviewsAdapter(reviewsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            ((RecyclerView) getView().findViewById(R.id.movie_detail_reviews)).setHasFixedSize(false);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_reviews)).setLayoutManager(mLayoutManager);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_reviews)).setAdapter(adapter);
            getView().findViewById(R.id.movie_detail_reviews).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReviewsLoadError() {
        getView().findViewById(R.id.movie_detail_reviews).setVisibility(View.GONE);
    }


    private void populate(View rootView) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(movie.title);

        ((TextView) rootView.findViewById(R.id.movie_detail_overview)).setText(movie.overview);

        ((RatingBar) rootView.findViewById(R.id.movie_detail_ratingbar))
                .setRating(movie.vote_average / 2);
        ((TextView) rootView.findViewById(R.id.movie_detail_vote_average))
                .setText(Float.toString(movie.vote_average / 2));
        ((TextView) rootView.findViewById(R.id.movie_detail_vote_count))
                .setText(Integer.toString(movie.vote_count));
        ((TextView) rootView.findViewById(R.id.movie_detail_release_date))
                .setText(new SimpleDateFormat("MMMM yyyy", Locale.US).format(movie.release));

        Picasso.with(getActivity()).load(IMAGE_BASE_URL + "w185" + movie.poster)
                .error(R.drawable.ic_poster_fallback).fit()
                .into(((ImageView) rootView.findViewById(R.id.movie_detail_poster)));

        Picasso.with(getActivity()).load(IMAGE_BASE_URL + "w342" + movie.backdrop)
                .error(R.drawable.ic_backdrop_fallback).fit()
                .into(((ImageView) rootView.findViewById(R.id.movie_detail_backdrop)));

        if ((castList != null) && (castList.size() > 0)) {
            MovieCastAdapter adapter = new MovieCastAdapter(getActivity(), R.layout.cast_list_item, castList);
            ((ListView) rootView.findViewById(R.id.movie_detail_cast)).setAdapter(adapter);
            rootView.findViewById(R.id.movie_detail_cast).setVisibility(View.VISIBLE);
        }
        else {
            rootView.findViewById(R.id.movie_detail_cast).setVisibility(View.GONE);
        }

        if ((trailersList != null) && (trailersList.size() > 0)) {

            TrailersAdapter adapter = new TrailersAdapter(trailersList, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_trailers)).setHasFixedSize(false);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_trailers)).setLayoutManager(mLayoutManager);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_trailers)).setAdapter(adapter);
            rootView.findViewById(R.id.movie_detail_trailers).setVisibility(View.VISIBLE);
        }
        else {
            rootView.findViewById(R.id.movie_detail_trailers).setVisibility(View.GONE);
        }

        if ((reviewsList != null) && (reviewsList.size() > 0)) {

            ReviewsAdapter adapter = new ReviewsAdapter(reviewsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_reviews)).setHasFixedSize(false);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_reviews)).setLayoutManager(mLayoutManager);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_reviews)).setAdapter(adapter);
            rootView.findViewById(R.id.movie_detail_reviews).setVisibility(View.VISIBLE);
        }
        else {
            rootView.findViewById(R.id.movie_detail_reviews).setVisibility(View.GONE);
        }
    }
}
