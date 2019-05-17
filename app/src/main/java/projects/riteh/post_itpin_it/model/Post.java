package projects.riteh.post_itpin_it.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity
public class Post {

    // ID, isReminder, isImportant, postText
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private boolean isReminder;
    private boolean isImportant;
    private String firestore_id;
    private String user_id;

    @NonNull
    private String postText;

    public Post (){}

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

    public void setFirestore_id(String firestore_id) {
        this.firestore_id = firestore_id;
    }

    public String getFirestore_id() {
        return firestore_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
