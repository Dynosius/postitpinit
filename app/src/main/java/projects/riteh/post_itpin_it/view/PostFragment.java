package projects.riteh.post_itpin_it.view;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayoutManager;
import projects.riteh.post_itpin_it.model.Post;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

@SuppressLint("ValidFragment")
public class PostFragment extends PostFragments implements Observer {
    private projects.riteh.post_itpin_it.adapter.PostAdapter postAdapter;

    PostFragment(int layoutID) {
        super(layoutID);
    }

    @Override
    protected void setAdapterView() {
        postAdapter = new projects.riteh.post_itpin_it.adapter.PostAdapter(getActivity(), (MainActivity)getActivity());
        recyclerView.setAdapter(postAdapter);
        // Here we define how we want to display post-its in the fragment (either linearly as rows or something else)
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));

    }

    @Override
    public void update(Observable o, Object arg) {
        postAdapter.setPosts((ArrayList<Post>) arg);
    }
}
