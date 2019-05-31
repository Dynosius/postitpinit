package projects.riteh.post_itpin_it.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import projects.riteh.post_itpin_it.adapter.PinnedPostAdapter;
import projects.riteh.post_itpin_it.adapter.PostViewModel;
import projects.riteh.post_itpin_it.model.Post;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class PinnedPostFragment extends PostFragments{
    private PinnedPostAdapter postAdapter;
    private PostViewModel mPostViewModel;

    PinnedPostFragment(int layoutID) {
        super(layoutID);
    }

    @Override
    protected void setAdapterView() {
        postAdapter = new PinnedPostAdapter(getActivity(), (MainActivity)getActivity());
        recyclerView.setAdapter(postAdapter);
        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        mPostViewModel.getPinnedPosts().observe(this, new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Post> posts) {
                postAdapter.setPosts(posts);
                postAdapter.createNotifications(posts);
            }
        });
    }
}