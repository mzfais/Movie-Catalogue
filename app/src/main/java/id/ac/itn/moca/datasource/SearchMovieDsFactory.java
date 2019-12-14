package id.ac.itn.moca.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import id.ac.itn.moca.model.Movie;

public class SearchMovieDsFactory extends DataSource.Factory {
    private static final String TAG = "SearchMovieDsFactory";
    private MutableLiveData<SearchMovieDs> searchMovieLiveDataSource = new MutableLiveData<>();
    private String keyword = "";

    public SearchMovieDsFactory(String filter) {
        this.keyword = filter;
    }

    @NonNull
    @Override
    public DataSource create() {
        SearchMovieDs searchMovieDs = new SearchMovieDs(this.keyword);
        searchMovieLiveDataSource.postValue(searchMovieDs);
        Log.d(TAG, "create: " + this.keyword);
        return searchMovieDs;
    }

    public MutableLiveData<SearchMovieDs> getSearchMovieLiveDataSource() {
        Log.d(TAG, "getSearchMovieLiveDataSource: return data");
        return searchMovieLiveDataSource;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
