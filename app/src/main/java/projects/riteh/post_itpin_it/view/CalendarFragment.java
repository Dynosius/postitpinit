package projects.riteh.post_itpin_it.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.adapter.CalendarAdapter;
import projects.riteh.post_itpin_it.adapter.PostViewModel;
import projects.riteh.post_itpin_it.model.Post;
import projects.riteh.post_itpin_it.service.PostService;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {

    private CalendarView simpleCalendarView;
    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;
    private PostViewModel mPostViewModel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        calendarAdapter = new CalendarAdapter(getContext(), (MainActivity)getActivity());
        recyclerView = view.findViewById(R.id.calendarRecyclerView);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.backgroundColor, null));
        simpleCalendarView = view.findViewById(R.id.calendarView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(calendarAdapter);

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                mPostViewModel.refreshPostsByDate(date);
            }
        });
        mPostViewModel.getCalendarPosts().observe(this, new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Post> posts) {
                calendarAdapter.setPosts(posts);
            }
        });


        return view;
    }
}
