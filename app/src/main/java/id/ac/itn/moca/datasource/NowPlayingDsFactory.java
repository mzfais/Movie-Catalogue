package id.ac.itn.moca.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import id.ac.itn.moca.model.Movie;

public class NowPlayingDsFactory extends DataSource.Factory {
    private MutableLiveData<NowPlayingDs> nowPlayingLiveDataSource = new MutableLiveData<>();

    @NonNull
    @Override
    public DataSource create() {
        NowPlayingDs nowPlayingDS = new NowPlayingDs();
        nowPlayingLiveDataSource.postValue(nowPlayingDS);
        return nowPlayingDS;
    }

    public MutableLiveData<NowPlayingDs> getNowPlayingLiveDataSource() {
        return nowPlayingLiveDataSource;
    }
}
