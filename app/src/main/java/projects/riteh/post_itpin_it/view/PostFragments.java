package projects.riteh.post_itpin_it.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import projects.riteh.post_itpin_it.R;

public abstract class PostFragments extends Fragment {
    private int layoutId;
    protected View view;
    RecyclerView recyclerView;

    PostFragments(int layoutID){
        this.layoutId = layoutID;
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layoutId, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.backgroundColor, null));
        // Here we define how we want to display post-its in the fragment (either linearly as rows or something else)
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        setAdapterView();
        return view;
    }

    protected abstract void setAdapterView();
}
