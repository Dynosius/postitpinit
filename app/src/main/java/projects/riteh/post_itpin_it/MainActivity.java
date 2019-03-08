package projects.riteh.post_itpin_it;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button displayButton = findViewById(R.id.openOverlayBtn);
        final RelativeLayout overlay = findViewById(R.id.overlay_layout);
        final RelativeLayout background_overlay = findViewById(R.id.pozadinski_layout);


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
            }
        });
    }


}
