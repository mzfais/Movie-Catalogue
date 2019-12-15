package id.ac.itn.moca.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.ac.itn.moca.R;
import id.ac.itn.moca.adapter.FavouriteAdapter;
import id.ac.itn.moca.model.Favourite;
import id.ac.itn.moca.viewmodel.FavouriteViewModel;

public class FavouriteFragment extends Fragment {
    private static final String TAG = "FavouriteFragment";
    private FavouriteViewModel favViewModel;
    private RecyclerView rvMovie;
    private FavouriteAdapter adapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rvMovieList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvMovie.setLayoutManager(gridLayoutManager);
        adapter = new FavouriteAdapter(getActivity());
        rvMovie.setAdapter(adapter);
        favViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favViewModel.getAllFavourites().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(List<Favourite> favourites) {
                adapter.setFavouriteList(favourites);
                Log.d(TAG, "onChanged: " + favourites.size());
            }
        });
    }
}
