package id.ac.itn.moca.service;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.util.Log;

import java.util.Date;

import id.ac.itn.moca.util.NotificationBuilder;

public class UpcomingJobService extends JobService {

    private static final String TAG = "mocaSetting";
    private NotificationBuilder notifBuilder = new NotificationBuilder();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ");
        int notifId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notifBuilder.showNotification(getApplicationContext(), "Coba job service", "isi dari job service firebase", notifId);
        jobFinished(jobParameters,false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob: ");
        return true;
    }

}
