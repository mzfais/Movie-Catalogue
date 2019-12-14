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
import id.ac.itn.moca.datasource.UpcomingDs;
import id.ac.itn.moca.datasource.UpcomingDsFactory;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.NetworkState;

public class UpcomingViewModel extends ViewModel {
    public LiveData<PagedList<Movie>> upComingPagedList;
    public LiveData<UpcomingDs> livedataSource;
    private LiveData<NetworkState> networkStateLiveData;
    private static final String TAG = "UpcomingViewModel";

    public UpcomingViewModel() {

        UpcomingDsFactory upcomingDsFactory = new UpcomingDsFactory();
        livedataSource = upcomingDsFactory.getUpComingLiveDataSource();
        networkStateLiveData = Transformations.switchMap(upcomingDsFactory.getUpComingLiveDataSource(), new Function<UpcomingDs, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(UpcomingDs input) {
                return input.getNetworkState();
            }
        });
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(NowPlayingDs.PAGE_SIZE)
                        .build();
        upComingPagedList = new LivePagedListBuilder(upcomingDsFactory, config).build();
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
