package id.ac.itn.moca.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import id.ac.itn.moca.model.Favourite;

@Database(entities = {Favourite.class},version = 1)
public abstract class MocaDatabase extends RoomDatabase {
    private static MocaDatabase instance;
    public abstract FavouriteDao favDao();
    public static synchronized MocaDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MocaDatabase.class, "moca_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
