package id.ac.itn.moca.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.TvShow;

public class TvShowDsFactory extends DataSource.Factory {
    private MutableLiveData<TvShowDs> tvShowLiveDataSource = new MutableLiveData<>();

    @NonNull
    @Override
    public DataSource create() {
        TvShowDs tvShowDs = new TvShowDs();
        tvShowLiveDataSource.postValue(tvShowDs);
        return tvShowDs;
    }

    public MutableLiveData<TvShowDs> getTvShowLiveDataSource() {
        return tvShowLiveDataSource;
    }
}
