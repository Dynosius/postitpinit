package projects.riteh.post_itpin_it.adapter;

import android.content.Context;
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
import java.util.Date;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private LayoutInflater mInflater;
    private PostService mPostService;
    private MainActivity activity;
    private ArrayList<Post> posts;

    public CalendarAdapter(Context context, MainActivity activity){
        mInflater = LayoutInflater.from(context);
        mPostService = PostService.getInstance();
        this.activity = activity;
        posts = new ArrayList<>();
    }
    @Override
    public CalendarAdapter.CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.postit_view, parent, false);
        return new CalendarAdapter.CalendarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        if (posts != null){
            Post current = posts.get(position);
            holder.postItemView.setText(current.getPostText());
        }
    }

    public void refreshPosts(Date date) {
        this.posts = mPostService.getPostsByDate(date);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private final TextView postItemView;
        protected CalendarViewHolder(View itemView) {
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
