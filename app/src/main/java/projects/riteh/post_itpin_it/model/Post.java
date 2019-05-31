package projects.riteh.post_itpin_it.model;

import android.support.annotation.NonNull;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Post {
    public static final String FIELD_REMINDER = "isReminder";
    public static final String FIELD_IMPORTANT = "isImportant";
    public static final String FIELD_FIRESTORE_ID = "firestoreId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_POST_TEXT = "postText";

    private @ServerTimestamp Date timestamp;
    private boolean isReminder;
    private boolean isImportant;
    private String firestore_id;
    private String user_id;
    private String postText;

    public Post (){}

    public Post(Date timestamp, boolean isReminder, String postText, String user_id, String firestore_id){
        this.timestamp = timestamp;
        this.isReminder = isReminder;
        this.postText = postText;
        this.user_id = user_id;
        this.firestore_id = firestore_id;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getFirestore_id() {
        return firestore_id;
    }

    public void setFirestore_id(String firestore_id) {
        this.firestore_id = firestore_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @NonNull
    public String getPostText() {
        return postText;
    }

    public void setPostText(@NonNull String postText) {
        this.postText = postText;
    }
}
