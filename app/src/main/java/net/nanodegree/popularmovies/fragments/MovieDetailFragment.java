package net.nanodegree.popularmovies.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.nanodegree.popularmovies.MovieDetailActivity;
import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.adapters.MovieCastAdapter;
import net.nanodegree.popularmovies.adapters.TrailersAdapter;
import net.nanodegree.popularmovies.listeners.MovieDetailsListener;
import net.nanodegree.popularmovies.listeners.TrailerListener;
import net.nanodegree.popularmovies.misc.Utils;
import net.nanodegree.popularmovies.model.Cast;
import net.nanodegree.popularmovies.model.Review;
import net.nanodegree.popularmovies.model.Trailer;
import net.nanodegree.popularmovies.model.parcelable.ParcelableCast;
import net.nanodegree.popularmovies.model.parcelable.ParcelableMovie;
import net.nanodegree.popularmovies.model.parcelable.ParcelableReview;
import net.nanodegree.popularmovies.model.parcelable.ParcelableTrailer;
import net.nanodegree.popularmovies.provider.MovieContract;
import net.nanodegree.popularmovies.tasks.MovieDbCastRequest;
import net.nanodegree.popularmovies.tasks.MovieDbReviewRequest;
import net.nanodegree.popularmovies.tasks.MovieDbTrailerRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MovieDetailFragment extends Fragment implements
        MovieDetailsListener, TrailerListener, LoaderManager.LoaderCallbacks<Cursor> {

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final int FAVORITE_STATUS_LOADER = 1;


    private boolean favorite;

    private ParcelableMovie movie;
    private ArrayList<Cast> castList;
    private ArrayList<Trailer> trailersList;
    private ArrayList<Review> reviewsList;

    private MovieDbCastRequest castRequest;
    private MovieDbTrailerRequest trailerRequest;
    private MovieDbReviewRequest reviewRequest;

    private Menu mMenu;
    private ShareActionProvider mShareActionProvider;

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

                getActivity().getSupportLoaderManager().initLoader(FAVORITE_STATUS_LOADER, null, this);
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

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.movie_detail_toolbar);

        if (getActivity() instanceof MovieDetailActivity) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else {

            if (movie != null) {
                toolbar.setTitle(movie.title);
                toolbar.setTitleTextColor(Color.WHITE);
            }
        }


        if (savedInstanceState != null) {
            retrieveInstanceState(savedInstanceState);
        }

        setHasOptionsMenu(true);

        populate(rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.movie_details_share, menu);

        mMenu = menu;

        // Set up ShareActionProvider's default share intent
        MenuItem shareItem = menu.findItem(R.id.movie_detail_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getDefaultShareIntent());

        if ((trailersList != null) && (trailersList.size() > 0)) {
            shareItem.setVisible(true);
        }

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
        outState.putBoolean("favorite", favorite);
    }

    private void retrieveInstanceState(Bundle savedInstanceState) {

        // If instance state already loaded, return
        if (movie != null)
            return;

        movie = savedInstanceState.getParcelable("movie");
        favorite = savedInstanceState.getBoolean("favorite");

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

        if (getActivity().getSupportLoaderManager().hasRunningLoaders())
            getActivity().getLoaderManager().destroyLoader(FAVORITE_STATUS_LOADER);
    }

    @Override
    public void onCastLoaded(ArrayList<Cast> cast) {

        if ((cast != null) && (cast.size() > 0)) {

            this.castList = cast;

            MovieCastAdapter adapter = new MovieCastAdapter(castList, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_cast)).setHasFixedSize(false);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_cast)).setLayoutManager(mLayoutManager);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_cast)).setAdapter(adapter);
            getView().findViewById(R.id.movie_detail_cast_container).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCastLoadError() {}

    @Override
    public void onTrailersLoaded(ArrayList<Trailer> trailers) {

        if ((trailers != null) && (trailers.size() > 0)) {

            this.trailersList = trailers;

            //Show share trailer menu
            if (mMenu != null) {
                mMenu.findItem(R.id.movie_detail_share).setVisible(true);
                mShareActionProvider.setShareIntent(getDefaultShareIntent());
            }

            TrailersAdapter adapter = new TrailersAdapter(trailersList, getActivity());
            adapter.setCallback(this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_trailers)).setHasFixedSize(false);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_trailers)).setLayoutManager(mLayoutManager);
            ((RecyclerView) getView().findViewById(R.id.movie_detail_trailers)).setAdapter(adapter);
            getView().findViewById(R.id.movie_detail_trailers_container).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrailersLoadError() {}

    @Override
    public void onReviewsLoaded(ArrayList<Review> reviews) {

        if ((reviews != null) && (reviews.size() > 0)) {

            this.reviewsList = reviews;

            LinearLayout reviewsContainer = (LinearLayout) getView().findViewById(R.id.movie_detail_reviews_container);

            final int count = reviewsList.size();

            for (int i = 0; i < count; i++) {

                Review review = reviewsList.get(i);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.review_item, null, false);
                ((TextView)v.findViewById(R.id.review_author)).setText(review.author);
                ((TextView)v.findViewById(R.id.review_content)).setText(review.content);
                reviewsContainer.addView(v);
            }

            reviewsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReviewsLoadError() {}

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String selectionClause ="_id = ?";
        String[] selectionArgs = { movie.id.toString() };

        return new CursorLoader(getActivity(), MovieContract.CONTENT_URI, null, selectionClause, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        switch (cursorLoader.getId()) {

            case FAVORITE_STATUS_LOADER:

                FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.movie_detail_favorite);

                if (fab != null) {

                    if (cursor.getCount() == 0) {
                        fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_outline_white_18dp));
                        favorite = false;
                    } else {
                        fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_white_18dp));
                        favorite = true;
                    }
                }

                break;
        }

        cursor.close();
        getActivity().getSupportLoaderManager().destroyLoader(FAVORITE_STATUS_LOADER);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {}

    private Intent getDefaultShareIntent() {

        Trailer trailer = new Trailer();

        if (trailersList != null)
            trailer = trailersList.get(0);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(R.string.watch) + " " + movie.title + " - " +trailer.name);
        intent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + trailer.key);
        return intent;
    }

    private void populate(View rootView) {

        if (getActivity() instanceof MovieDetailActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(movie.title);
        }

        ((TextView) rootView.findViewById(R.id.movie_detail_overview)).setText(movie.overview);

        ((RatingBar) rootView.findViewById(R.id.movie_detail_ratingbar))
                .setRating(movie.vote_average / 2);
        ((TextView) rootView.findViewById(R.id.movie_detail_vote_average))
                .setText(Float.toString(movie.vote_average));
        ((TextView) rootView.findViewById(R.id.movie_detail_vote_count))
                .setText(Integer.toString(movie.vote_count) + " " + getString(R.string.votes));
        ((TextView) rootView.findViewById(R.id.movie_detail_release_date))
                .setText(new SimpleDateFormat("MMMM yyyy", Locale.US).format(movie.release));

        Picasso.with(getActivity()).load(IMAGE_BASE_URL + "w185" + movie.poster)
                .error(R.drawable.ic_poster_fallback).fit()
                .into(((ImageView) rootView.findViewById(R.id.movie_detail_poster)));

        Picasso.with(getActivity()).load(IMAGE_BASE_URL + "w342" + movie.backdrop)
                .error(R.drawable.ic_backdrop_fallback).fit()
                .into(((ImageView) rootView.findViewById(R.id.movie_detail_backdrop)));


        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.movie_detail_favorite);

        if (fab.getDrawable() == null) {

            if (favorite)
                fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_white_18dp));
            else
                fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_outline_white_18dp));
        }

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (favorite) {
                    String selectionClause = "_id = ?";
                    String[] selectionArgs = {movie.id.toString()};
                    getActivity().getContentResolver().delete(MovieContract.CONTENT_URI, selectionClause, selectionArgs);
                    fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_outline_white_18dp));
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.movie_detail_action_favorite_delete), Snackbar.LENGTH_LONG).show();
                    favorite = false;

                } else {
                    getActivity().getContentResolver().insert(MovieContract.CONTENT_URI, movie.getContentValues());
                    fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_white_18dp));
                    Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.movie_detail_action_favorite_add), Snackbar.LENGTH_LONG).show();
                    favorite = true;
                }
            }
        });

        if ((castList != null) && (castList.size() > 0)) {

            MovieCastAdapter adapter = new MovieCastAdapter(castList, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_cast)).setHasFixedSize(false);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_cast)).setLayoutManager(mLayoutManager);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_cast)).setAdapter(adapter);
            rootView.findViewById(R.id.movie_detail_cast_container).setVisibility(View.VISIBLE);
        }

        if ((trailersList != null) && (trailersList.size() > 0)) {

            TrailersAdapter adapter = new TrailersAdapter(trailersList, getActivity());
            adapter.setCallback(this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_trailers)).setHasFixedSize(false);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_trailers)).setLayoutManager(mLayoutManager);
            ((RecyclerView) rootView.findViewById(R.id.movie_detail_trailers)).setAdapter(adapter);
            rootView.findViewById(R.id.movie_detail_trailers_container).setVisibility(View.VISIBLE);
        }

        if ((reviewsList != null) && (reviewsList.size() > 0)) {

            LinearLayout reviewsContainer = (LinearLayout) rootView.findViewById(R.id.movie_detail_reviews_container);

            final int count = reviewsList.size();

            for (int i = 0; i < count; i++) {

                Review review = reviewsList.get(i);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.review_item, null, false);
                ((TextView)v.findViewById(R.id.review_author)).setText(review.author);
                ((TextView)v.findViewById(R.id.review_content)).setText(review.content);
                reviewsContainer.addView(v);
            }

            reviewsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrailerSelected(int position) {
        watchTrailer(trailersList.get(position).key);
    }

    private  void watchTrailer(String id) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            intent.putExtra("force_fullscreen",true);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+id));
            startActivity(intent);
        }
    }
}