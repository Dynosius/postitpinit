package projects.riteh.post_itpin_it.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.model.Post;

public class WidgetService extends RemoteViewsService {

    private ArrayList<Post> posts = new ArrayList<>();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory {
        private Context context;
        private int appWidgetid;
        private PostService postService;
        //private String[] exampleData = {"one", "two", "three", "four"};

        WidgetItemFactory (final Context context, Intent intent) {
            this.context = context;
            this.appWidgetid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            //connect to a data source
            //SystemClock.sleep(1000);
            postService = PostService.getInstance();
        }

        @Override
        public void onDataSetChanged() {
            //TODO: Put arraylist elements on each postit in widget and display them
            posts = postService.getPinnedPostsArray();
        }

        @Override
        public void onDestroy() {
            //close data source
            postService = null;
        }

        @Override
        public int getCount() {
            return posts.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            try{
                views.setTextViewText(R.id.widget_item_text, posts.get(position).getPostText());
            } catch (Exception e){
                System.err.println("Some error " + e.getMessage());
            }

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
