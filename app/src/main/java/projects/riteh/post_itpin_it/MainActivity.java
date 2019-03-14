package projects.riteh.post_itpin_it;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import projects.riteh.post_itpin_it.database.Post;
import projects.riteh.post_itpin_it.view.PostViewAdapter;
import projects.riteh.post_itpin_it.view.PostViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private int[] tabIcons = {
            R.drawable.star,
            R.drawable.note,
            R.drawable.reminder
    };

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PostViewModel mPostViewModel;
    private PostViewAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextInputEditText editedPostitNote = findViewById(R.id.textInputEditedTextPostitNote);
        final ImageButton confirmButton = findViewById(R.id.imageButtonConfirm);
        final Button displayButton = findViewById(R.id.openOverlayBtn);
        final RelativeLayout overlay = findViewById(R.id.overlay_layout);
        final LinearLayout background_overlay = findViewById(R.id.pozadinski_layout);
        final CheckBox reminderCheckBox = findViewById(R.id.reminderCheckBox);

        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        Tab1Fragment tab1 = new Tab1Fragment();
        adapter.addFragment(tab1, "Tab 1");
        adapter.addFragment(new Tab2Fragment(), "Tab 2");
        adapter.addFragment(new Tab3Fragment(), "Tab 3");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        displayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                overlay.setVisibility(View.VISIBLE);
                background_overlay.setAlpha(0.2f);
            }
        });

        overlay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                overlay.setVisibility(View.INVISIBLE);
                background_overlay.setAlpha(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Post post = new Post();
                post.setPostText(editedPostitNote.getText().toString());
                post.setReminder(reminderCheckBox.isChecked());
                mPostViewModel.insert(post);

                overlay.setVisibility(View.INVISIBLE);
                background_overlay.setAlpha(1);
                editedPostitNote.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        postAdapter = new PostViewAdapter(this, mPostViewModel);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPostViewModel.getAllPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                postAdapter.setPosts(posts);
            }
        });
    }
}