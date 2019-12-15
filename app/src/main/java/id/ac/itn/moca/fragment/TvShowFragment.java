package id.ac.itn.moca.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import id.ac.itn.moca.R;
import id.ac.itn.moca.adapter.MoviePagedAdapter;
import id.ac.itn.moca.adapter.TvPagedAdapter;
import id.ac.itn.moca.model.NetworkState;
import id.ac.itn.moca.model.TvShow;
import id.ac.itn.moca.viewmodel.TvShowViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TvShowFragment";
    public static final String FIRST_LOAD = "false";
    private RecyclerView rvMovie;
    private TvPagedAdapter adapter;
    private TvShowViewModel viewModel;
    SwipeRefreshLayout srl;

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rvMovieList);
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
         adapter = new TvPagedAdapter(getActivity());
        srl = view.findViewById(R.id.swipeTv);
        srl.setOnRefreshListener(this);
        Boolean firstLoad;
        try {
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
        viewModel = ViewModelProviders.of(getActivity()).get(TvShowViewModel.class);
        viewModel.tvShowPagedList.observe(getActivity(), new Observer<PagedList<TvShow>>() {
            @Override
            public void onChanged(PagedList<TvShow> tvs) {
                if (tvs != null) {
                    adapter.submitList(tvs);
                 }
            }
        });
        viewModel.getNetworkStateLiveData().observe(getActivity(), new Observer<NetworkState>() {
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