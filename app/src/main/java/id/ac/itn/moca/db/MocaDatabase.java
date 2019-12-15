package id.ac.itn.moca.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        FavouriteDao favouriteDao;

        private PopulateDbAsyncTask(MocaDatabase db){
            favouriteDao = db.favDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
