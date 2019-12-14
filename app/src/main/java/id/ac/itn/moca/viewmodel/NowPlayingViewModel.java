package id.ac.itn.moca.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import id.ac.itn.moca.datasource.NowPlayingDs;
import id.ac.itn.moca.datasource.NowPlayingDsFactory;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.NetworkState;

public class NowPlayingViewModel extends ViewModel {
    public LiveData<PagedList<Movie>> nowPlayingPagedList;
    public LiveData<NowPlayingDs> livedataSource;
    private LiveData<NetworkState> networkStateLiveData;
    private static final String TAG = "NowPlayingViewModel";

    public NowPlayingViewModel() {

        NowPlayingDsFactory nowPlayingDsFactory = new NowPlayingDsFactory();
        livedataSource = nowPlayingDsFactory.getNowPlayingLiveDataSource();
        networkStateLiveData = Transformations.switchMap(nowPlayingDsFactory.getNowPlayingLiveDataSource(), new Function<NowPlayingDs, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(NowPlayingDs input) {
                return input.getNetworkState();
            }
        });
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(NowPlayingDs.PAGE_SIZE)
                        .build();
        nowPlayingPagedList = new LivePagedListBuilder(nowPlayingDsFactory, config).build();
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
