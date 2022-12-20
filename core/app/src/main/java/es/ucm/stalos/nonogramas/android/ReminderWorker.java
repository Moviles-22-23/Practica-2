package es.ucm.stalos.nonogramas.android;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import es.ucm.stalos.nonogramas.R;

/**
 * Worker related with the ReminderNotification.
 * It is made to only make ReminderNotifications
 */
public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _pushNotification = new ReminderNotification(context, "unique_channel",
                    123456, "reminder");
            _pushNotification._msgs.add(context.getResources().getString(R.string.msg_1));
            _pushNotification._msgs.add(context.getResources().getString(R.string.msg_2));
            _pushNotification._msgs.add(context.getResources().getString(R.string.msg_3));
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            System.out.println("You can't create notification for this device API level.\n" +
                    "API required: Oreo 26");
            return Result.failure();
        }

        _pushNotification.showNotification();
        
        return Result.success();
    }

    private ReminderNotification _pushNotification;
}
