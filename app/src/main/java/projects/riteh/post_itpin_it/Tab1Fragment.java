package projects.riteh.post_itpin_it;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import projects.riteh.post_itpin_it.database.Post;
import projects.riteh.post_itpin_it.view.PostViewAdapter;
import projects.riteh.post_itpin_it.view.PostViewModel;

import java.util.List;

public class Tab1Fragment extends Fragment {
    private PostViewModel mPostViewModel;
    private PostViewAdapter postAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_one, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        postAdapter = new PostViewAdapter(getActivity(), mPostViewModel, (MainActivity)getActivity());
        recyclerView.setAdapter(postAdapter);
        // Here we define how we want to display post-its in the fragment (either linearly as rows or something else)
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        mPostViewModel.getAllPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                postAdapter.setPosts(posts);
            }
        });
        return view;
    }
}