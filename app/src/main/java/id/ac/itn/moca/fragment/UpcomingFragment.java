package id.ac.itn.moca.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import id.ac.itn.moca.R;
import id.ac.itn.moca.adapter.MoviePagedAdapter;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.NetworkState;
import id.ac.itn.moca.viewmodel.UpcomingViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NowPlayingFragment";
    public static final String FIRST_LOAD = "false";
    private MoviePagedAdapter adapter;
    private UpcomingViewModel viewModel;
    SwipeRefreshLayout srl;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvMovie = view.findViewById(R.id.rvMovieList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                if (viewType == MoviePagedAdapter.MOVIE_VIEW_TYPE) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });
        rvMovie.setLayoutManager(gridLayoutManager);
        adapter = new MoviePagedAdapter(getActivity());
        srl = view.findViewById(R.id.swipeUp);
        srl.setOnRefreshListener(this);
        boolean firstLoad;
        try {
            assert getArguments() != null;
            firstLoad = getArguments().getBoolean(FIRST_LOAD);
        } catch (Exception ex) {
            firstLoad = false;
        }
        load_data(firstLoad);
        rvMovie.setAdapter(adapter);
    }

    private void load_data(Boolean firstLoad) {
        if (firstLoad) {
            Log.d(TAG, "load_data: firstload");
        } else {
            Log.d(TAG, "load_data: not firstload");
        }
        viewModel = new ViewModelProvider(requireActivity()).get(UpcomingViewModel.class);
        viewModel.upComingPagedList.observe(getViewLifecycleOwner(), new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                if (movies != null) {
                    adapter.submitList(movies);
                }
            }
        });
        viewModel.getNetworkStateLiveData().observe(getViewLifecycleOwner(), new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                adapter.setNetworkState(networkState);
                Log.d(TAG, "onChanged: " + networkState.getMsg());
            }
        });

    }

    @Override
    public void onRefresh() {
        srl.setRefreshing(true);
        viewModel.Refresh();
        srl.setRefreshing(false);
    }
}
