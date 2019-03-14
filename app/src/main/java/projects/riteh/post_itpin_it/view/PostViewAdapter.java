package projects.riteh.post_itpin_it.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.database.Post;
import projects.riteh.post_itpin_it.database.Repository;

import java.io.IOError;
import java.util.List;

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.PostViewHolder> {

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView postItemView;
        private PostViewHolder(View itemView) {
            super(itemView);
            postItemView = itemView.findViewById(R.id.textView);
            postItemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            try{
                repo.delete(repo.getmAllPosts().getValue().get(getLayoutPosition()));  // Attempts to delete the i-th
                // element in the LiveData list from DB
            }
            catch(IOError e){
                System.out.print(e);
            }
        }
    }
    // MEMBERS
    private final LayoutInflater mInflater;
    private List<Post> mPosts; // Cache
    private Repository repo;
    public PostViewAdapter(Context context, PostViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        repo = viewModel.getmRepository();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, viewGroup, false);
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

    public void setPosts(List<Post> posts){
        mPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if (mPosts != null) return mPosts.size();
        else return 0;
    }


}
