package projects.riteh.post_itpin_it.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.adapter.CalendarAdapter;
import projects.riteh.post_itpin_it.service.PostService;

import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {

    private CalendarView simpleCalendarView;
    private PostService postService;
    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        calendarAdapter = new CalendarAdapter(getContext(), (MainActivity)getActivity());
        recyclerView = view.findViewById(R.id.calendarRecyclerView);
        simpleCalendarView = view.findViewById(R.id.calendarView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(calendarAdapter);
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                postService = PostService.getInstance();
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                calendarAdapter.refreshPosts(date);
            }
        });
        return view;
    }
}
