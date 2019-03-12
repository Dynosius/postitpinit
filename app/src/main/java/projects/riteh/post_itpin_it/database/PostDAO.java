package projects.riteh.post_itpin_it.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface PostDAO {

    @Query("SELECT * FROM Post")
    LiveData<List<Post>> getAll();

    @Query("SELECT COUNT(*) FROM Post")
    int countUsers();

    @Insert
    void insertAll(Post... posts);

    @Delete
    void delete(Post post);

    @Update
    void update(Post post);
}
