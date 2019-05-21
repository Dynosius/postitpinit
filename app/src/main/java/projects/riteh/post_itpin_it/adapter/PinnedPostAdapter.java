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

public class PinnedPostAdapter extends RecyclerView.Adapter<PinnedPostAdapter.PostViewHolder> {

    private LayoutInflater mInflater;
    private MainActivity activity;
    private PostService mPostService;
    private ArrayList<Post> posts = new ArrayList<>();
    public PinnedPostAdapter(Context context, MainActivity activity){
        mInflater = LayoutInflater.from(context);
        this.activity = activity;
        mPostService = PostService.getInstance();
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null){
            Post current = posts.get(position);
            holder.postItemView.setText(current.getPostText());
        }
    }
    public PinnedPostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.postit_view, viewGroup, false);
        return new PinnedPostAdapter.PostViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void createNotifications(ArrayList<Post> posts) {
        activity.cancelNotifications();
        for (Post post : posts){
            activity.createNotification(post);
        }
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
        // Selects post with this action and opnes edit dialog
        @Override
        public void onClick(View v) {
            activity.setCurrentState(MainActivity.PostStates.EDIT_POST_MODE);
            activity.setSelectedPost(posts.get(getLayoutPosition()));
            activity.editPostIt();
        }
        // Delete with this action
        @Override
        public boolean onLongClick(View v) {
            final Post post = posts.get(getLayoutPosition());
            mPostService.deletePost(post);
            Snackbar snackbar = Snackbar.make(v, "Message deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Recreates the post (albeit with a new firebase_id and timestamp)
                            mPostService.createPost(post);
                        }
                    });
            snackbar.show();
            return true;
        }
    }
}
