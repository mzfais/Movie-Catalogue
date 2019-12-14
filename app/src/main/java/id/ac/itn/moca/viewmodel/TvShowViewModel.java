package id.ac.itn.moca.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import id.ac.itn.moca.datasource.NowPlayingDs;
import id.ac.itn.moca.datasource.TvShowDs;
import id.ac.itn.moca.datasource.TvShowDsFactory;
import id.ac.itn.moca.datasource.UpcomingDsFactory;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.NetworkState;
import id.ac.itn.moca.model.TvShow;

public class TvShowViewModel extends ViewModel {
    public LiveData<PagedList<TvShow>> tvShowPagedList;
    public LiveData<TvShowDs> livedataSource;
    private LiveData<NetworkState> networkStateLiveData;
    private static final String TAG = "TvShowViewModel";

    public TvShowViewModel() {
        TvShowDsFactory tvShowDsFactory = new TvShowDsFactory();
        livedataSource = tvShowDsFactory.getTvShowLiveDataSource();
        networkStateLiveData = Transformations.switchMap(tvShowDsFactory.getTvShowLiveDataSource(), new Function<TvShowDs, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(TvShowDs input) {
                return input.getNetworkState();
            }
        });
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(NowPlayingDs.PAGE_SIZE)
                        .build();
        tvShowPagedList = new LivePagedListBuilder(tvShowDsFactory, config).build();
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

    public void Refresh() {
        if (livedataSource.getValue() != null) {
            livedataSource.getValue().invalidate();
        }
    }
}
