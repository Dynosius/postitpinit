package projects.riteh.post_itpin_it.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.model.Post;
import projects.riteh.post_itpin_it.service.PostService;
import projects.riteh.post_itpin_it.view.MainActivity;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    // MEMBERS
    private final LayoutInflater mInflater;
    private MainActivity activity;
    private Context context;
    private PostService postService;
    private ArrayList<Post> posts = new ArrayList<>();

    public PostAdapter(Context context, MainActivity activity) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
        postService = PostService.getInstance();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.postit_view, viewGroup, false);
        return new PostViewHolder(itemView);
    }
    // ViewHolder represents each single item bound
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position){
        if (posts != null){
            Post current = posts.get(position);
            holder.postItemView.setText(current.getPostText());
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(ArrayList<Post> arg) {
        this.posts = arg;
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
        // On click we send an event to MainActivity that should fire up the post-it screen and allow edits
        public void onClick(View v) {
            activity.setCurrentState(MainActivity.PostStates.EDIT_POST_MODE);
            activity.setSelectedPost(posts.get(getLayoutPosition()));
            activity.editPostIt();
        }

        @Override
        public boolean onLongClick(View v) {
            final Post post = posts.get(getLayoutPosition());
            //activity.getNotificationManagerCompat().cancel(post.getFirestore_id());     // Delete notification
            postService.deletePost(post);
            Snackbar snackbar = Snackbar.make(v, "Message deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Recreates the post (albeit with a new firebase_id and timestamp)
                            postService.createPost(post);
                        }
                    });
            snackbar.show();
            return true;
        }
    }
}
