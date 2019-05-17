package projects.riteh.post_itpin_it.view;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.controller.PostsViewModel;
import projects.riteh.post_itpin_it.model.Post;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 4413;

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
    private PostsViewModel mPostsViewModel;
    private InputMethodManager imm;
    private TextInputEditText editedPostitNote;
    private Button displayButton;
    private RelativeLayout overlay;
    private LinearLayout background_overlay;
    private CheckBox reminderCheckBox;
    private PostStates currentState = PostStates.CREATE_POST_MODE;
    private Post selectedPost;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManagerCompat;
    private View postitLayout;
    private NotificationManager notificationManager;
    private final String CHANNEL_ID = "testID";
    private final String GROUP_NAME = "Testing";
    private boolean keyboardShown = false;
    // Firebase
    private List<AuthUI.IdpConfig> providers;
    private Button btn_signinout;

    public NotificationManagerCompat getNotificationManagerCompat() {
        return notificationManagerCompat;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        createNotificationChannel();
        intentInit();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        editedPostitNote = findViewById(R.id.textInputEditedTextPostitNote);
        displayButton = findViewById(R.id.openOverlayBtn);
        overlay = findViewById(R.id.overlay_layout);
        background_overlay = findViewById(R.id.pozadinski_layout);
        reminderCheckBox = findViewById(R.id.reminderCheckBox);
        postitLayout = findViewById(R.id.postItLayout);

        btn_signinout = findViewById(R.id.signinout);
        mPostsViewModel = ViewModelProviders.of(this).get(PostsViewModel.class);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tab1 = new Tab1Fragment(R.layout.fragment_one);
        adapter.addFragment(tab1, "Tab 1");
        adapter.addFragment(new Tab2Fragment(R.layout.fragment_two), "Tab 2");
        adapter.addFragment(new Tab3Fragment(), "Tab 3");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        providers = Arrays.asList(
                //new AuthUI.IdpConfig.EmailBuilder().build(),
                //new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        /**
         * Firebase login initialization
         */
        showSignInOptions();
        // Represents the NEW POST button
        displayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearPostIt();
                keyboardShown = true;
                currentState = PostStates.CREATE_POST_MODE;
                //overlay.setVisibility(View.VISIBLE);
                background_overlay.setAlpha(0.2f);
                spinShowPostIt();

            }
        });

        // This is what fires off when you click outside of the post area
        overlay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(currentState.equals(PostStates.CREATE_POST_MODE)){
                    Post post = new Post();
                    post.setPostText(editedPostitNote.getText().toString());
                    post.setReminder(reminderCheckBox.isChecked());
                    // Insert into room database
                    mPostsViewModel.insert(post);
                    // Insert into firebase database
                    addPostToFirebase(post);
                    Toast.makeText(getApplicationContext(), "Post added", Toast.LENGTH_SHORT).show();

                } else if(currentState.equals(PostStates.EDIT_POST_MODE)){
                    selectedPost.setPostText(editedPostitNote.getText().toString());
                    selectedPost.setReminder(reminderCheckBox.isChecked());
                    mPostsViewModel.update(selectedPost);
                    // Update firebase database
                    // TODO

                    Toast.makeText(getApplicationContext(), "Post updated", Toast.LENGTH_SHORT).show();
                }
                background_overlay.setAlpha(1);
                spinHidePostIt();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                editedPostitNote.setText("");
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                keyboardShown = false;
            }
        });

        // Represents the TextView on the NEW POST screen/overlay
        editedPostitNote.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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

    /**
     * Inserts data from post object to firestore database
     * @param post
     */
    private void addPostToFirebase(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("posts").document();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        post.setFirestore_id(ref.getId());
        post.setUser_id(userId);
        ref.set(post);
    }

    private void updatePostAtFirebase(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //db.collection("posts").add(post);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){

                //Get user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Show email on toast
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                // Set Button signout
                btn_signinout.setEnabled(true);
                // Handles log in and log out with button
                btn_signinout.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        AuthUI.getInstance()
                                .signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        btn_signinout.setEnabled(false);
                                        showSignInOptions();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            else
            {
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * Displays sign in options (Google Firebase UI)
     */
    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(),
        REQUEST_CODE);
    }

    // Initialization methods and notification channels


    // This initiates an intent to open the MainActivity with clicking on the notification
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
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        notificationManagerCompat.notify(post.getId(), notification);
    }

    /**
     * Destroys all current instances of notifications. This is used to refresh notifications once they have been updated
     */
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
        postitLayout.setRotation(-360.0f);
    }

    public void setCurrentState(PostStates currentState) {
        this.currentState = currentState;
    }

    /**
     * Takes a Post object as a parameter. Opens the post-it editor overlay and passes it the information from the Post
     * parameter.
     * @param post This is a post it object reference
     */
    public void editPostIt(Post post){
        overlay.setVisibility(View.VISIBLE);
        background_overlay.setAlpha(0.2f);
        spinShowPostIt();
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

    /**
     * Sets current post to the one passed as an argument. This is used for purposes of having a reference to the object
     * in the MainActivity context
     * @param selectedPost
     */
    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }

    /**
     * Method to animate the post it with spinning it
     */
    private void spinShowPostIt() {
        final View overlay = findViewById(R.id.overlay_layout);
        overlay.setVisibility(View.VISIBLE);
        postitLayout.setVisibility(View.VISIBLE);
        //System.out.println(postitLayout.getRotation());
        postitLayout.animate()
             //   .scaleXBy(0.5f)
             //   .scaleYBy(0.5f)
                .alpha(1.0f)
                .rotation(360.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        postitLayout.setVisibility(View.VISIBLE);
                        overlay.setVisibility(View.VISIBLE);
                    }
                });
        //System.out.println(postitLayout.getRotation());
    }

    /**
     * Method to animate post it and hide it afterwards
     */
    private void spinHidePostIt() {
        final View overlay = findViewById(R.id.overlay_layout);
        postitLayout.animate()
         //       .scaleXBy(0.0f)
         //       .scaleYBy(0.0f)
                .alpha(0.0f)
                .rotation(-360.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        postitLayout.setVisibility(View.INVISIBLE);
                        overlay.setVisibility(View.GONE);
                        background_overlay.setAlpha(1);
                    }
                });
    }
}