package es.ucm.stalos.nonogramas.android;

import android.content.Context;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

/**
 * Class in charge of manage the notifications of the application.
 * If there are more notification's types than ReminderNotification
 * we can add here its setup taking the respective arguments
 */
public class NotificationManager {
    /**
     * Cancel all works enqueued
     * @param context A Context for on-demand initialization.
     */
    public static void CancelAllWorks(Context context)
    {
        WorkManager.getInstance(context).cancelAllWork();
    }

    /**
     * Cancel a current workManager task enqueued
     *
     * @param context A Context for on-demand initialization.
     * @param workTag Tag of the work to cancel
     */
    public static void CancelSomeWork(Context context, String workTag) {
        WorkManager.getInstance(context).cancelAllWorkByTag(workTag);
    }

    /**
     * Set up a reminder notification
     *
     * @param context        A Context for on-demand initialization.
     * @param workTag        Tag of the work
     * @param timeUnitType   Time-format to schedule the
     *                       push notification
     * @param timeUnitOffset Offset-Time related with
     *                       the _timeUnitType. Amount
     *                       of time from now to show
     *                       the notification
     */
    public static void SetUpReminderNotification(Context context, String workTag,
                                                 TimeUnit timeUnitType, int timeUnitOffset) {
        // Cancel previous workTag
        CancelSomeWork(context, workTag);
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(timeUnitOffset, timeUnitType)
                .addTag(workTag)
                .build();

        WorkManager.getInstance(context).beginUniqueWork(workTag,
                ExistingWorkPolicy.REPLACE, notificationWork).enqueue();
    }

}
