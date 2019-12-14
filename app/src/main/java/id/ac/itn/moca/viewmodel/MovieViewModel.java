package id.ac.itn.moca.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import id.ac.itn.moca.BuildConfig;
import id.ac.itn.moca.api.ApiClient;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.MovieList;
import id.ac.itn.moca.model.TvShow;
import id.ac.itn.moca.model.TvShowList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {
    private static final String TAG = "MovieViewModel";
    MutableLiveData<List<Movie>> movieList;
    MutableLiveData<MovieList> upcomingList;
    MutableLiveData<TvShowList> tvShowList;
    MutableLiveData<MovieList> moviesNowPlaying;

    public LiveData<List<Movie>> getMovieList() {
        if (movieList == null) {
            movieList = new MutableLiveData<>();
            setMovieList();
        }
        return movieList;
    }

    private void setMovieList() {
        ApiClient.getInstance().getApi().getMovieList(BuildConfig.MovieAPIKey, "en-US")
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if (response.body().getTotalResults() > 0) {
                            movieList.postValue(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public LiveData<MovieList> getMoviesNowPlaying() {
        if (moviesNowPlaying == null || moviesNowPlaying.getValue().getResults() == null) {
            moviesNowPlaying = new MutableLiveData<>();
            setMoviesNowPlaying();
        }
        return moviesNowPlaying;
    }

    private void setMoviesNowPlaying() {
        ApiClient.getInstance().getApi().getNowPlayingList(BuildConfig.MovieAPIKey, "en-US",1)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if (response.body().getTotalResults() > 0) {
                            moviesNowPlaying.postValue(response.body());
                            Log.d(TAG, "onResponse: nowplaying: " + response.body().getTotalResults());
                        } else {
                            Throwable t = new Throwable("Tidak ada data yang bisa ditampilkan");
                            moviesNowPlaying.postValue(new MovieList(t));
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        moviesNowPlaying.postValue(new MovieList(t));
                    }
                });
    }

    public LiveData<MovieList> getUpcomingList() {
        if (upcomingList == null) {
            upcomingList = new MutableLiveData<>();
            setUpcomingList();
        }
        return upcomingList;
    }

    private void setUpcomingList() {
        ApiClient.getInstance().getApi().getUpcomingList(BuildConfig.MovieAPIKey, "en-US",1)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if (response.body().getTotalResults() > 0) {
                            upcomingList.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        upcomingList.postValue(new MovieList(t));
                    }
                });
    }

    public LiveData<TvShowList> getTvShowList() {
        if (tvShowList == null) {
            tvShowList = new MutableLiveData<>();
            setTvShowList();
        }
        return tvShowList;
    }

    private void setTvShowList() {
        ApiClient.getInstance().getApi().getTvShowList(BuildConfig.MovieAPIKey, "en-US",1)
                .enqueue(new Callback<TvShowList>() {
                    @Override
                    public void onResponse(Call<TvShowList> call, Response<TvShowList> response) {
                        if (response.body().getTotalResults() > 0) {
                            tvShowList.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TvShowList> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        tvShowList.postValue(new TvShowList(t));
                    }
                });
    }

    public void Refresh() {
        if (moviesNowPlaying.getValue() != null) {
            setMoviesNowPlaying();
        }
        if (upcomingList.getValue() != null) {
            setUpcomingList();
        }
        if (tvShowList.getValue() != null) {
            setTvShowList();
        }
    }
}
