package projects.riteh.post_itpin_it;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import projects.riteh.post_itpin_it.database.Post;
import projects.riteh.post_itpin_it.view.PostViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public enum PostStates{
        EDIT_POST_MODE, CREATE_POST_MODE
    }
    private int[] tabIcons = {
            R.drawable.star,
            R.drawable.note,
            R.drawable.reminder
    };
    private PendingIntent pendingIntent;
    private Intent intent;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private Tab1Fragment tab1;
    private ViewPager viewPager;
    private PostViewModel mPostViewModel;
    private InputMethodManager imm;
    private TextInputEditText editedPostitNote;
    private ImageButton confirmButton;
    private Button displayButton;
    private RelativeLayout overlay;
    private LinearLayout background_overlay;
    private CheckBox reminderCheckBox;
    private PostStates currentState = PostStates.CREATE_POST_MODE;
    private Post selectedPost;
    private List<Post> pinnedPosts;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationManager notificationManager;
    private final String CHANNEL_ID = "testID";
    private final String GROUP_NAME = "Testing";
    private boolean keyboardShown = false;

    public NotificationManagerCompat getNotificationManagerCompat() {
        return notificationManagerCompat;
    }

    public List<Post> getPinnedPosts() {
        return pinnedPosts;
    }
    public void setPinnedPosts(List<Post> posts){
        this.pinnedPosts = posts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        intentInit();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        editedPostitNote = findViewById(R.id.textInputEditedTextPostitNote);
        displayButton = findViewById(R.id.openOverlayBtn);
        overlay = findViewById(R.id.overlay_layout);
        background_overlay = findViewById(R.id.pozadinski_layout);
        reminderCheckBox = findViewById(R.id.reminderCheckBox);

        pinnedPosts = new ArrayList<>();
        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tab1 = new Tab1Fragment();
        adapter.addFragment(tab1, "Tab 1");
        adapter.addFragment(new Tab2Fragment(), "Tab 2");
        adapter.addFragment(new Tab3Fragment(), "Tab 3");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);


        // Represents the NEW POST button
        displayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearPostIt();
                keyboardShown = true;
                currentState = PostStates.CREATE_POST_MODE;
                overlay.setVisibility(View.VISIBLE);
                background_overlay.setAlpha(0.2f);
            }
        });

        // This is what fires off when you click outside of the post area
        overlay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(currentState.equals(PostStates.CREATE_POST_MODE)){
                    Post post = new Post();
                    post.setPostText(editedPostitNote.getText().toString());
                    post.setReminder(reminderCheckBox.isChecked());
                    mPostViewModel.insert(post);

                } else if(currentState.equals(PostStates.EDIT_POST_MODE)){
                    selectedPost.setPostText(editedPostitNote.getText().toString());
                    selectedPost.setReminder(reminderCheckBox.isChecked());
                    mPostViewModel.update(selectedPost);
                }
                overlay.setVisibility(View.INVISIBLE);
                background_overlay.setAlpha(1);

                editedPostitNote.setText("");
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                keyboardShown = false;
            }
        });

        // Represents the TextView on the NEW POST screen/overlay
        editedPostitNote.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(!keyboardShown){
                    imm.showSoftInput(v, 0);
                    keyboardShown = true;
                }
                else{
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    keyboardShown = false;
                }

            }
        });
    }

    // Initialization methods and notification channels

    // This enables opening of the application with clicking on the notification
    private void intentInit() {
        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }
    // Method for creating a notification
    // TODO: Maybe create a specific class with its own interface for different types of reminders (event, general reminder)
    public void createNotification(Post post){
        Notification notification = notificationBuilder.setContentTitle("Reminder")
                .setContentText(post.getPostText())
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        notificationManagerCompat.notify(post.getId(), notification);
    }
    public void cancelNotifications(){
        notificationManagerCompat.cancelAll();
    }

    /**
     * Clears the display on the screen
     *
     */
    private void clearPostIt() {
        editedPostitNote.setText("");
        reminderCheckBox.setChecked(false);
    }

    public void setCurrentState(PostStates currentState) {
        this.currentState = currentState;
    }

    /**
     * Takes a Post object as a parameter. Opens the post-it editor overlay and passes it the information from the Post
     * parameter.
     * @param post
     */
    public void editPostIt(Post post){
        overlay.setVisibility(View.VISIBLE);
        background_overlay.setAlpha(0.2f);
        editedPostitNote.setText(post.getPostText());
        reminderCheckBox.setChecked(post.isReminder());
    }

    /***
     * Creates a notification channel to handle notification requests and organize them within the Android OS
     */
    private void createNotificationChannel(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("0", "Default channel"
                    , NotificationManager.IMPORTANCE_HIGH));
        }
        // create notification builder, in case it's on android 7 or lower the channel gets ignored
        notificationBuilder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.baseline_date_range)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);
    }
    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }
}