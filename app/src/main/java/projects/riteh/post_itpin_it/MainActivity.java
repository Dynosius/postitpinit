package projects.riteh.post_itpin_it;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private int[] tabIcons = {
            R.drawable.star,
            R.drawable.note,
            R.drawable.reminder
    };

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button displayButton = findViewById(R.id.openOverlayBtn);
        final RelativeLayout overlay = findViewById(R.id.overlay_layout);
        final LinearLayout background_overlay = findViewById(R.id.pozadinski_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Tab 1");
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
            }
        });
    }
}