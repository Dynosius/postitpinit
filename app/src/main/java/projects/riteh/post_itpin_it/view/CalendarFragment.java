package projects.riteh.post_itpin_it.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import projects.riteh.post_itpin_it.R;

public class CalendarFragment extends Fragment {

    private CalendarView simpleCalendarView;

    public CalendarFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        simpleCalendarView = view.findViewById(R.id.calendarView);
        return view;
    }
}
