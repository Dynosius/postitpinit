package projects.riteh.post_itpin_it.controller;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import projects.riteh.post_itpin_it.model.Post;
import projects.riteh.post_itpin_it.model.Repository;

import java.util.ArrayList;
import java.util.List;
// This class is actually controller??
public class PostsViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<Post>> mAllPosts, mPinnedPosts, mUnpinnedPosts;

    public PostsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllPosts = mRepository.getmAllPosts();
        mPinnedPosts = mRepository.getAllPinned();
        mUnpinnedPosts = mRepository.getAllUnpinned();

    }

    public LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }
    public LiveData<List<Post>> getAllPinned() { return mPinnedPosts; }
    public LiveData<List<Post>> getAllUnpinned() { return mUnpinnedPosts; }

    public void insert(Post post) {
        mRepository.insert(post);
    }
    public void update(Post post) { mRepository.update(post);
    }
    public Repository getmRepository() {
        return mRepository;
    }


}
