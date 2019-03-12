package projects.riteh.post_itpin_it;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity /*implements View.OnTouchListener*/{

    private int[] tabIcons = {
            R.drawable.star,
            R.drawable.note,
            R.drawable.reminder
    };

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    int windowwidth;
    int windowheight;
    private ImageView mImageView;
    private RelativeLayout mRrootLayout;
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextInputEditText editedPostitNote = findViewById(R.id.textInputEditedTextPostitNote);
        final ImageButton confirmButton = findViewById(R.id.imageButtonConfirm);
        final Button displayButton = findViewById(R.id.openOverlayBtn);
        final RelativeLayout overlay = findViewById(R.id.overlay_layout);
        final LinearLayout background_overlay = findViewById(R.id.pozadinski_layout);

    /*    mRrootLayout = overlay;
        mImageView = (ImageView) mRrootLayout.findViewById(R.id.orange_object);
        mImageView.setOnTouchListener(this);
        mRrootLayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = mRrootLayout.getWidth();
                windowheight = mRrootLayout.getHeight();
            }
        });*/

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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                overlay.setVisibility(View.INVISIBLE);
                background_overlay.setAlpha(1);
                editedPostitNote.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }
/*
    private boolean isOutReported = false;

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        // Check if the image view is out of the parent view and report it if it is.
        // Only report once the image goes out and don't stack toasts.
        if (isOut(view)) {
            if (!isOutReported) {
                isOutReported = true;
                Toast.makeText(this, "OUT", Toast.LENGTH_SHORT).show();
            }
        } else {
            isOutReported = false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // _xDelta and _yDelta record how far inside the view we have touched. These
                // values are used to compute new margins when the view is moved.
                _xDelta = X - view.getLeft();
                _yDelta = Y - view.getTop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // Do nothing
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                // Image is centered to start, but we need to unhitch it to move it around.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                    lp.removeRule(RelativeLayout.CENTER_VERTICAL);
                } else {
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                    lp.addRule(RelativeLayout.CENTER_VERTICAL, 0);
                }
                lp.leftMargin = X - _xDelta;
                lp.topMargin = Y - _yDelta;
                // Negative margins here ensure that we can move off the screen to the right
                // and on the bottom. Comment these lines out and you will see that
                // the image will be hemmed in on the right and bottom and will actually shrink.
                lp.rightMargin = view.getWidth() - lp.leftMargin - windowwidth;
                lp.bottomMargin = view.getHeight() - lp.topMargin - windowheight;
                view.setLayoutParams(lp);
                break;
        }
        // invalidate is redundant if layout params are set or not needed if they are not set.
//        mRrootLayout.invalidate();
        return true;
    }

    private boolean isOut(View view) {
        // Check to see if the view is out of bounds by calculating how many pixels
        // of the view must be out of bounds to and checking that at least that many
        // pixels are out.
        float percentageOut = 0.50f;
        int viewPctWidth = (int) (view.getWidth() * percentageOut);
        int viewPctHeight = (int) (view.getHeight() * percentageOut);

        return ((-view.getLeft() >= viewPctWidth) ||
                (view.getRight() - windowwidth) > viewPctWidth ||
                (-view.getTop() >= viewPctHeight) ||
                (view.getBottom() - windowheight) > viewPctHeight);
    }*/
}