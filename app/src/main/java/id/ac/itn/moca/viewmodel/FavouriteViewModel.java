package id.ac.itn.moca.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.itn.moca.db.FavouriteRepository;
import id.ac.itn.moca.model.Favourite;

public class FavouriteViewModel extends AndroidViewModel {
    private FavouriteRepository repository;
    private LiveData<List<Favourite>> allFavs;
    private LiveData<Favourite> favItem;

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavouriteRepository(application);
        allFavs = repository.getAllFavourites();
    }

    public void insert(Favourite fav){
        repository.insert(fav);
    }

    public void delete(Favourite fav){
        repository.delete(fav);
    }

    public LiveData<List<Favourite>> getAllFavourites(){
        return allFavs;
    }

    public LiveData<Favourite> getFavItem(int mov_id){
        return repository.getItemFavourite(mov_id);
    }

}
