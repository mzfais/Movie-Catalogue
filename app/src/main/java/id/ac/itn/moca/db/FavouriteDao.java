package id.ac.itn.moca.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import id.ac.itn.moca.model.Favourite;

@Dao // data access object
public interface FavouriteDao {

    @Insert
    void insert(Favourite fav);

    @Delete
    void delete(Favourite fav);

    @Query("SELECT * FROM movie_favourite ORDER BY id DESC")
    LiveData<List<Favourite>> getAllFavourite();

    @Query("SELECT * FROM movie_favourite WHERE MovieId = :mov_id")
    LiveData<Favourite> getItemFavourite(int mov_id);
}
