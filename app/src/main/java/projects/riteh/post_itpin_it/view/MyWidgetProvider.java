package projects.riteh.post_itpin_it.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.service.WidgetService;

public class MyWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context,
                MyWidgetProvider.class);
        // Gets all ids from our specific widget
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent configIntent = new Intent(context, TransparentPostit.class);

            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

            remoteViews.setOnClickPendingIntent(R.id.button, configPendingIntent);
            remoteViews.setRemoteAdapter(R.id.widget_stack_view, serviceIntent);
            remoteViews.setEmptyView(R.id.widget_stack_view, R.id.widget_empty_stack_view);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }
    }
}