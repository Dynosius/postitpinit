package projects.riteh.post_itpin_it.service;

import android.app.*;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import projects.riteh.post_itpin_it.R;
import projects.riteh.post_itpin_it.view.MainActivity;

public class NotifyService extends Service {
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        //Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        createNotificationChannel();
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

    private void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("0", "Default channel"
                    , NotificationManager.IMPORTANCE_HIGH));
        }
        // create notification builder, in case it's on android 7 or lower the channel gets ignored
        notificationBuilder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.baseline_date_range)
                .setPriority(NotificationCompat.PRIORITY_MAX);
                //.setContentIntent(pendingIntent);
    }
}
