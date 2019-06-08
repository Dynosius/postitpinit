package projects.riteh.post_itpin_it.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import projects.riteh.post_itpin_it.model.Post;
import projects.riteh.post_itpin_it.service.PostService;

import java.util.ArrayList;
import java.util.Date;

public class PostViewModel extends ViewModel {
    private PostService mPostService;
    private MutableLiveData<ArrayList<Post>> pinnedPosts, unpinnedPosts, calendarPosts;
    public PostViewModel (){
        mPostService = PostService.getInstance();
        pinnedPosts = mPostService.getPinnedPosts();
        unpinnedPosts = mPostService.getUnpinnedPosts();
        calendarPosts = mPostService.getCalendarPosts();
    }

    public MutableLiveData<ArrayList<Post>> getCalendarPosts() {
        return calendarPosts;
    }

    public MutableLiveData<ArrayList<Post>> getPinnedPosts() {
        return pinnedPosts;
    }

    public MutableLiveData<ArrayList<Post>> getUnpinnedPosts() {
        return unpinnedPosts;
    }

    public void refreshPostsByDate(Date date){
        mPostService.refreshCalendarByDate(date);
    }
}
