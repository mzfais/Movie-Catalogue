package id.ac.itn.moca.viewmodel;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import id.ac.itn.moca.datasource.NowPlayingDs;
import id.ac.itn.moca.datasource.NowPlayingDsFactory;
import id.ac.itn.moca.datasource.SearchMovieDs;
import id.ac.itn.moca.datasource.SearchMovieDsFactory;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.NetworkState;

public class SearchMovieViewModel extends ViewModel {
    public LiveData<PagedList<Movie>> searchMoviePagedList;
    private LiveData<SearchMovieDs> livedataSource;
    private static final String TAG = "SearchMovieViewModel";
    private LiveData<NetworkState> networkStateLiveData;
    private SearchMovieDsFactory searchMovieDsFactory;
    private String filter = "";

    public SearchMovieViewModel(String keywords) {
        //this.searchMovie(keywords);
        searchMovieDsFactory = new SearchMovieDsFactory(keywords);
        livedataSource = searchMovieDsFactory.getSearchMovieLiveDataSource();
        networkStateLiveData = Transformations.switchMap(searchMovieDsFactory.getSearchMovieLiveDataSource(), new Function<SearchMovieDs, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(SearchMovieDs input) {
                return input.getNetworkState();
            }
        });
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(NowPlayingDs.PAGE_SIZE)
                        .build();
        searchMoviePagedList = new LivePagedListBuilder(searchMovieDsFactory, config).build();
    }

    public void searchMovie(String keywords) {
        searchMovieDsFactory.setKeyword(keywords);
        this.Refresh();
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
