package net.nanodegree.popularmovies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.nanodegree.popularmovies.R;
import net.nanodegree.popularmovies.listeners.FragmentInteractionListener;

public class NoConnectivityFragment extends Fragment {

    private FragmentInteractionListener interactionListener;

    public NoConnectivityFragment() {
    }

    public void setInteractionListener(FragmentInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_no_connectivity, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.showPosters();
            }
        });

        return view;
    }
}
