package projects.riteh.post_itpin_it.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.controller.PostsViewModel;

public abstract class PostFragments extends Fragment {
    PostsViewModel mPostsViewModel;
    private int layoutId;
    protected View view;
    RecyclerView recyclerView;

    PostFragments(int layoutID){
        this.layoutId = layoutID;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layoutId, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        mPostsViewModel = ViewModelProviders.of(this).get(PostsViewModel.class);
        setAdapterView();
        return view;
    }

    protected abstract void setAdapterView();
}
