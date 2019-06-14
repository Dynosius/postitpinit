package projects.riteh.post_itpin_it.view;

import android.app.ActionBar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.model.Post;
import projects.riteh.post_itpin_it.service.PostService;

public class TransparentPostit extends AppCompatActivity {
    private CheckBox reminderCheckBox;
    private TextInputEditText editedPostitNote;
    private PostService postService;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_postit);

        ImageView postit = findViewById(R.id.orange_object);
        editedPostitNote = findViewById(R.id.textInputEditedTextPostitNote);
        reminderCheckBox = findViewById(R.id.reminderCheckBox);
        confirmButton = findViewById(R.id.button);
        postService = PostService.getInstance();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Post post = new Post();
                post.setPostText(editedPostitNote.getText().toString());
                post.setReminder(reminderCheckBox.isChecked());
                postService.createPost(post);
                finish();
            }
        });
    }
}
