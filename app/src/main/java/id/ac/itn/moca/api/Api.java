package id.ac.itn.moca.api;

import androidx.annotation.Nullable;

import id.ac.itn.moca.model.MovieList;
import id.ac.itn.moca.model.TvShowList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    //https://api.themoviedb.org/3/discover/movie?api_key=api_key&language=en-US
    @GET("discover/movie")
    Call<MovieList> getMovieList(@Query("api_key") String key, @Query("language") String lang);

    //https://api.themoviedb.org/3/discover/tv?api_key=api_key&language=en-US
    @GET("discover/tv")
    Call<TvShowList> getTvShowList(@Query("api_key") String key, @Query("language") String lang, @Query("page") int page);

    //https://api.themoviedb.org/3/movie/now_playing?api_key=api_key&language=en-US
    @GET("movie/now_playing")
    Call<MovieList> getNowPlayingList(@Query("api_key") String key, @Query("language") String lang, @Query("page") int page);

    //https://api.themoviedb.org/3/movie/upcoming?api_key=api_key&language=en-US
    @GET("movie/upcoming")
    Call<MovieList> getUpcomingList(@Query("api_key") String key, @Query("language") String lang, @Query("page") int page);

    //https://api.themoviedb.org/3/search/movie?query=keyword&api_key=api_key&language=en-US
    @GET("search/movie")
    Call<MovieList> getSearchResult(@Query("query") String keyword, @Query("api_key") String key, @Query("language") String lang, @Query("page") int page);
}
