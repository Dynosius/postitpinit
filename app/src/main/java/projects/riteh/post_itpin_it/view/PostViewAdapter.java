package projects.riteh.post_itpin_it.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.TextView;
import projects.riteh.post_itpin_it.MainActivity;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.database.Post;
import projects.riteh.post_itpin_it.database.Repository;
import java.io.IOError;
import java.util.List;

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.PostViewHolder>{
    // MEMBERS
    private final LayoutInflater mInflater;
    private List<Post> mPosts; // Cache
    private Repository repo;
    private MainActivity activity;

    public PostViewAdapter(Context context, PostViewModel viewModel, MainActivity activity) {
        mInflater = LayoutInflater.from(context);
        repo = viewModel.getmRepository();
        this.activity = activity;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.mainscreen_postit, viewGroup, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position){ // ViewHolder represents each single item bound
        if(mPosts != null){                                            // from LiveData<List<Post>>
            Post current = mPosts.get(position);
            holder.postItemView.setText(current.getPostText());
        }
        else{
            holder.postItemView.setText("Nothing");
        }
    }
    @Override
    public int getItemCount(){
        if (mPosts != null) return mPosts.size();
        else return 0;
    }

    public void setPosts(List<Post> posts){
        mPosts = posts;
        notifyDataSetChanged();
    }
    // HOLDER CLASS
    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private final TextView postItemView;
    private PostViewHolder(View itemView) {
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
                activity.editPostIt(repo.getmAllPosts().getValue().get(getLayoutPosition()));
            }
            catch(IOError er){
                System.out.print(er);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            try{
                repo.delete(repo.getmAllPosts().getValue().get(getLayoutPosition()));  // Attempts to delete the i-th
                // element in the LiveData list from DB
                Snackbar snackbar = Snackbar.make(v, "Message deleted", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } catch(IOError e){
                System.out.print(e);
            }
            return true;
        }
    }
}
