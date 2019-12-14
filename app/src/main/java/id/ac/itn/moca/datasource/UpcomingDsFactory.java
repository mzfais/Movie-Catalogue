package id.ac.itn.moca.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import id.ac.itn.moca.model.Movie;

public class UpcomingDsFactory extends DataSource.Factory {
    private MutableLiveData<UpcomingDs> upComingLiveDataSource = new MutableLiveData<>();

    @NonNull
    @Override
    public DataSource create() {
        UpcomingDs upcomingDs = new UpcomingDs();
        upComingLiveDataSource.postValue(upcomingDs);
        return upcomingDs;
    }

    public MutableLiveData<UpcomingDs> getUpComingLiveDataSource() {
        return upComingLiveDataSource;
    }
}
