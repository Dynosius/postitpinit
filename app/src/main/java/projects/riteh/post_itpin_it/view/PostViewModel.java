package projects.riteh.post_itpin_it.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import projects.riteh.post_itpin_it.database.Post;
import projects.riteh.post_itpin_it.database.Repository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<Post>> mAllPosts;

    public PostViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllPosts = mRepository.getmAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }

    public void insert(Post post) {
        mRepository.insert(post);
    }
}
