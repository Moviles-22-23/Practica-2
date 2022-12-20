package es.ucm.stalos.nonogramas.android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import es.ucm.stalos.nonogramas.R;

/**
 * Notification created in order to remind the player that the game exists
 */
public class ReminderNotification {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ReminderNotification(Context context, String newChannel, int notificationID,
                                String notificationName) {
        _channelID = newChannel;
        _notificationID = notificationID;
        _notificationName = notificationName;
        _msgs = new ArrayList<>();

        // 1. Set intent to open the activity when the app is foreground
        Intent startGameIntent = new Intent(context, MainActivity.class);
        startGameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent launchGame = PendingIntent.getActivity(context, 0, startGameIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 2. Construction of the channel
        createNotificationChannel(context);

        // 3. Setting up the notification
        _builder = new NotificationCompat.Builder(context, _channelID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(context.getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(ContextCompat.getColor(context, R.color.purple_200))
                .setContentIntent(launchGame)
                .setAutoCancel(true);

        notificationManager = NotificationManagerCompat.from(context);
    }

    /**
     * Create the NotificationChannel, but only on API 26+ because
     * the NotificationChannel class is new and not in the support library
     * Register the channel with the system; you can't change the importance
     * or other notification behaviors after this
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            System.out.println("Creation of channels is not available in your current" +
                    "android version");
            return;
        }

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(_channelID, _notificationName, importance);
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

        // X. Chosen a random message from messages data
        if(_msgs.size() > 0)
        {
            int rnd = ThreadLocalRandom.current().nextInt(0, _msgs.size());
            msg = _msgs.get(rnd);
        }

        _builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg));

        notificationManager.notify(_notificationID, _builder.build());
    }

    //--------------------ANDROID-STUFF----------------------//
    private NotificationCompat.Builder _builder;
    private NotificationManagerCompat notificationManager;

    //------------------------------------------------------//
    /**
     * ID of the channel
     */
    private String _channelID;
    /**
     * Name of the notification
     */
    private String _notificationName;
    /**
     * ID of the notification
     */
    private int _notificationID;
    /**
     * Contains different messages for the push-notification
     */
    public List<String> _msgs;
}
