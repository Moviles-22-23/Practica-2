package es.ucm.stalos.nonogramas.android;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import es.ucm.stalos.nonogramas.R;

public class PushNotification {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public PushNotification(AppCompatActivity context, String newChannel, int notificationID,
                            String notificationName) {
        _channelID = newChannel;
        _notificationID = notificationID;
        _name = notificationName;
        _msgs = new ArrayList<>();

        // 1. Set intent to open the activity whe the app is foreground
        Intent startGameIntent = new Intent(context, context.getClass());
        startGameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(startGameIntent);
        PendingIntent launchGame = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 2. Construction of the channel
        createNotificationChannel(context);

        // 3. Setting up the notification
        _builder = new NotificationCompat.Builder(context, _channelID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(context.getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(ContextCompat.getColor(context, R.color.purple_200))
                .setContentIntent(launchGame);

        notificationManager = NotificationManagerCompat.from(context);
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * Create the NotificationChannel, but only on API 26+ because
     * the NotificationChannel class is new and not in the support library
     * Register the channel with the system; you can't change the importance
     * or other notification behaviors after this
     */
    private void createNotificationChannel(AppCompatActivity context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            System.out.println("Creation of channels is not available in your current" +
                    "android version");
            return;
        }

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(_channelID, _name, importance);
        channel.setDescription("");

        NotificationManager notificationManager =
                context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Displays the notification
     */
    public void showNotification() {
        String msg = "";

        if(_msgs.size() > 0)
        {
            int rnd = ThreadLocalRandom.current().nextInt(0, _msgs.size());
            msg = _msgs.get(rnd);
        }

        _builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg));
        Date currentTime = Calendar.getInstance().getTime();
        System.out.println(currentTime.toString());
//        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() +
//                        60 * 1000, );

        notificationManager.notify(_notificationID, _builder.build());
    }

    private NotificationCompat.Builder _builder;
    private NotificationManagerCompat notificationManager;
    private String _channelID;
    private String _name;
    private int _notificationID;
    private AlarmManager alarmMgr;

    /**
     * Contains different messages for the push-notification
     */
    public List<String> _msgs;
}
