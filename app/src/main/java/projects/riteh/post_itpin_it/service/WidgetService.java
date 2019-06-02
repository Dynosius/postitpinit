package projects.riteh.post_itpin_it.service;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.adapter.PostAdapter;
import projects.riteh.post_itpin_it.model.Post;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory {
        private Context context;
        private int appWidgetid;
        //private MutableLiveData<ArrayList<Post>> postitData;
        private String[] exampleData = {"one", "two", "three", "four"};
        //private PostService postService;
        //private ArrayList<Post> posts = new ArrayList<>();

        WidgetItemFactory (Context context, Intent intent) {
            this.context = context;
            this.appWidgetid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            //postService = PostService.getInstance();
        }

        @Override
        public void onCreate() {
            //connect to a data source
            //SystemClock.sleep(1000);
            //postitData = postService.getPinnedPosts();
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            //close data source
        }

        @Override
        public int getCount() {
            return exampleData.length;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            views.setTextViewText(R.id.widget_item_text, exampleData[position]);
            //SystemClock.sleep(500);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
