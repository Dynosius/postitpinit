package projects.riteh.post_itpin_it.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import projects.riteh.post_itpin_it.R;

public abstract class PostFragments extends Fragment {
    private int layoutId;
    protected View view;
    RecyclerView recyclerView;

    PostFragments(int layoutID){
        this.layoutId = layoutID;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layoutId, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        setAdapterView();
        return view;
    }

    protected abstract void setAdapterView();
}
