package projects.riteh.post_itpin_it.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import projects.riteh.post_itpin_it.adapter.PostAdapter;
import projects.riteh.post_itpin_it.adapter.PostViewModel;
import projects.riteh.post_itpin_it.model.Post;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class PostFragment extends PostFragments{
    private PostAdapter postAdapter;
    private PostViewModel mPostViewModel;

    PostFragment(int layoutID) {
        super(layoutID);
    }

    @Override
    protected void setAdapterView() {
        postAdapter = new PostAdapter(getActivity(), (MainActivity)getActivity());
        recyclerView.setAdapter(postAdapter);
        mPostViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        mPostViewModel.getUnpinnedPosts().observe(this, new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Post> posts) {
                postAdapter.setPosts(posts);
            }
        });
    }
}
