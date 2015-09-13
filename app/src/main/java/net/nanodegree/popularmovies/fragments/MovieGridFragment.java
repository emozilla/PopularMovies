package net.nanodegree.popularmovies.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.adapters.GridPostersAdapter;
import net.nanodegree.popularmovies.listeners.MovieCallbacks;
import net.nanodegree.popularmovies.model.Movie;
import net.nanodegree.popularmovies.support.GridFragment;

import java.util.ArrayList;

/**
 * A list fragment representing a list of Movies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MovieDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link MovieCallbacks}
 * interface.
 */
public class MovieGridFragment extends GridFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private MovieCallbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;


    /**
     * A dummy implementation of the {@link MovieCallbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static MovieCallbacks sDummyCallbacks = new MovieCallbacks() {
        @Override
        public void onMovieSelected(Movie m) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof MovieCallbacks)) {
            throw new IllegalStateException("Activity must implement fragment's MovieCallbacks.");
        }

        mCallbacks = (MovieCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onGridItemClick(GridView gridView, View view, int position, long id) {
        super.onGridItemClick(gridView, view, position, id);
        mCallbacks.onMovieSelected((Movie) ((ArrayAdapter)getListAdapter()).getItem(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getGridView().setChoiceMode(activateOnItemClick
                ? GridView.CHOICE_MODE_SINGLE
                : GridView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {

        if (position == ListView.INVALID_POSITION) {
            getGridView().setItemChecked(mActivatedPosition, false);
        } else {
            getGridView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public void setMovies(ArrayList<Movie> movies) {

        if (movies != null)
            setListAdapter(new GridPostersAdapter(getActivity(), R.layout.movie_grid_item, movies));
    }
}