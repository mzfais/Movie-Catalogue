package id.ac.itn.moca.datasource;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import id.ac.itn.moca.BuildConfig;
import id.ac.itn.moca.api.ApiClient;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.MovieList;
import id.ac.itn.moca.model.NetworkState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingDs extends PageKeyedDataSource<Integer, Movie> {
    private static final String TAG = "UpcomingDs";
    public static final int PAGE_SIZE = 10;
    public static final int FIRST_PAGE = 1;
    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);
        ApiClient.getInstance().getApi().getUpcomingList(BuildConfig.MovieAPIKey, "en-US", FIRST_PAGE)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
                            networkState.postValue(NetworkState.LOADED);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        networkState.postValue(NetworkState.FAIL);
                        Log.d(TAG, "onFailure: loadinitial " + t.getMessage());
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);
        ApiClient.getInstance().getApi().getUpcomingList(BuildConfig.MovieAPIKey, "en-US", params.key)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if (response.isSuccessful()) {
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getResults(), key);
                            networkState.postValue(NetworkState.LOADED);
                            Log.d(TAG, "onResponse: loadBefore " + key);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        networkState.postValue(NetworkState.FAIL);
                        Log.d(TAG, "onFailure: loadbefore " + t.getMessage());
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);
        ApiClient.getInstance().getApi().getUpcomingList(BuildConfig.MovieAPIKey, "en-US", params.key)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getTotalPages() >= params.key) {
                                networkState.postValue(NetworkState.LOADED);
                                callback.onResult(response.body().getResults(), params.key + 1);
                                Log.d(TAG, "onResponse: loadAfter " + params.key + 1);
                            } else {
                                networkState.postValue(NetworkState.ENDOFLIST);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        networkState.postValue(NetworkState.FAIL);
                        Log.d(TAG, "onFailure: loadafter " + t.getMessage());
                    }
                });
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }
}
