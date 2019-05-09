package projects.riteh.post_itpin_it.controller;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.TextView;
import projects.riteh.post_itpin_it.view.MainActivity;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.model.Post;
import projects.riteh.post_itpin_it.model.Repository;
import java.io.IOError;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    // MEMBERS
    private final LayoutInflater mInflater;
    private List<Post> mPosts; // Cache
    private Repository repo;
    private MainActivity activity;
    private Post post;
    private boolean isPinnedView;

    public PostAdapter(Context context, PostsViewModel viewModel, MainActivity activity, boolean isPinned) {
        mInflater = LayoutInflater.from(context);
        repo = viewModel.getmRepository();
        this.activity = activity;
        this.isPinnedView = isPinned;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.postit_view, viewGroup, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position){ // ViewHolder represents each single item bound
        if(mPosts != null){                                            // from LiveData<List<Post>>
            Post current = mPosts.get(position);
            holder.postItemView.setText(current.getPostText());
        }
        else{
            holder.postItemView.setText("Nothing");
        }
    }
    @Override
    public int getItemCount() {
        if (mPosts != null) return mPosts.size();
        else return 0;
    }

    /***
     * Override this in specialized class
     * @param posts
     */
    public void setPosts(List<Post> posts){
        mPosts = posts;
        if (isPinnedView){
            ArrayList<Post> pinnedPostList = new ArrayList<>();
            activity.cancelNotifications();
            for (Post postIter:posts) {
                if(postIter.isReminder()){
                    pinnedPostList.add(postIter);
                    activity.createNotification(postIter);
                }
            }
        }

        notifyDataSetChanged();
    }
    // HOLDER CLASS
    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private final TextView postItemView;
    protected PostViewHolder(View itemView) {
        super(itemView);
        postItemView = itemView.findViewById(R.id.textView);
        postItemView.setOnClickListener(this);
        postItemView.setOnLongClickListener(this);
    }
        @Override
        public void onClick(View v) {
            activity.setCurrentState(MainActivity.PostStates.EDIT_POST_MODE);
            activity.setSelectedPost(mPosts.get(getLayoutPosition()));
            try{
                // Sets post text once it's opened in edit mode
                // ZASTO TU RADIM QUERY
                activity.editPostIt(mPosts.get(getLayoutPosition()));
            }
            catch(IOError er){
                System.out.print(er);
            }
        }
        // TODO: Maybe change this up to not get all posts but only required ones
        @Override
        public boolean onLongClick(View v) {
            try{
                post = mPosts.get(getLayoutPosition());
                activity.getNotificationManagerCompat().cancel(post.getId());   // Delete the notification
                repo.delete(post);  // Attempts to delete the i-th element in the LiveData list from DB
                Snackbar snackbar = Snackbar.make(v, "Message deleted", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                repo.insert(post);
                            }
                        });
                snackbar.show();
            } catch(IOError e){
                System.out.print(e);
            }
            return true;
        }
    }
}
