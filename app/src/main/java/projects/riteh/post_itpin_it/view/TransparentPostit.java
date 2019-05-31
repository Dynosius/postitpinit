package projects.riteh.post_itpin_it.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import projects.riteh.post_itpin_it.R;

public class TransparentPostit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_postit);
        ImageView imw = findViewById(R.id.orange_object);
        System.out.println("Tu sam");

        imw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("**********");
            }
        });
    }
}
