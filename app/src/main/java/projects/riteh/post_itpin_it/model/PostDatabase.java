package projects.riteh.post_itpin_it.model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Post.class}, version = 1)
public abstract class PostDatabase extends RoomDatabase {

    private static volatile PostDatabase INSTANCE;

    public abstract PostDAO postDAO();

    static PostDatabase getAppDatabase(final Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PostDatabase.class, "post-database")
                    .addCallback(sRoomDatabaseCallback).build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{
        private final PostDAO postDao;

        PopulateDbAsync(PostDatabase db){
            postDao = db.postDAO();
        }
        // Here we can implement certain actions that we may wish to occur after opening the application
        @Override
        protected Void doInBackground(final Void... voids) {
            return null;
        }
    }
    public static void destroyInstance(){
        INSTANCE = null;
    }
}
