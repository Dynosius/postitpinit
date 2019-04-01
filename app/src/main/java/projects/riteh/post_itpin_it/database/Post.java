package projects.riteh.post_itpin_it.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Post {

    // ID, isReminder, isImportant, postText
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private boolean isReminder;
    private boolean isImportant;

    @NonNull
    private String postText;

    public int getId() {
        return id;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public boolean isImportant() {
        return isImportant;
    }

    @NonNull
    public String getPostText() {
        return postText;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setPostText(@NonNull String postText) {
        this.postText = postText;
    }

    public void setId(int id) {
        this.id = id;
    }
}
