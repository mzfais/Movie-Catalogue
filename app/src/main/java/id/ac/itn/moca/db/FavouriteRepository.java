package id.ac.itn.moca.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.itn.moca.model.Favourite;

public class FavouriteRepository {
    private FavouriteDao favDao;
    LiveData<List<Favourite>> allFavs;

    public FavouriteRepository(Application application){
        MocaDatabase mocaDatabase = MocaDatabase.getInstance(application);
        favDao = mocaDatabase.favDao();
        allFavs = favDao.getAllFavourite();
    }

    public void insert(Favourite fav){
        new InsertFavouriteSync(favDao).execute(fav);
    }

    public void delete(Favourite fav){
        new DeleteFavouriteSync(favDao).execute(fav);
    }

    public LiveData<List<Favourite>> getAllFavourites(){
        return allFavs;
    }

    private static class InsertFavouriteSync extends AsyncTask<Favourite,Void,Void>{
        FavouriteDao favouriteDao;

        public InsertFavouriteSync(FavouriteDao favouriteDao) {
            this.favouriteDao = favouriteDao;
        }

        @Override
        protected Void doInBackground(Favourite... favourite) {
            favouriteDao.insert(favourite[0]);
            return null;
        }
    }

    private static class DeleteFavouriteSync extends AsyncTask<Favourite,Void,Void>{
        FavouriteDao favouriteDao;

        public DeleteFavouriteSync(FavouriteDao favouriteDao) {
            this.favouriteDao = favouriteDao;
        }

        @Override
        protected Void doInBackground(Favourite... favourite) {
            favouriteDao.delete(favourite[0]);
            return null;
        }
    }
}
