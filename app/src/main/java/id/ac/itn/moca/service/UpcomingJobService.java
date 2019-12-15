package id.ac.itn.moca.service;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.ac.itn.moca.BuildConfig;
import id.ac.itn.moca.R;
import id.ac.itn.moca.api.ApiClient;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.MovieList;
import id.ac.itn.moca.util.NotificationBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingJobService extends JobService {

    private static final String TAG = "mocaSetting";
    private NotificationBuilder notifBuilder = new NotificationBuilder();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ");
        getUpcomingMovie(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob: ");
        return true;
    }

    private void getUpcomingMovie(final JobParameters job) {
        ApiClient.getInstance().getApi().getUpcomingList(BuildConfig.MovieAPIKey, "en-US",1)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if(response.code()==200){
                            try {
                                MovieList movieList = response.body();
                                for (int i = 0; i < movieList.getResults().size(); i++) {

                                    Movie movieItems = movieList.getResults().get(i);
                                    String title = movieItems.getOriginalTitle();
                                    String release = movieItems.getReleaseDate();
                                    String message = String.format(getResources().getString(R.string.label_release_reminder), title);
                                    Date current = Calendar.getInstance().getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                    String now = sdf.format(current);
                                    Log.d(TAG, "strdate: " + release);
                                    Log.d(TAG, "now: " + now);
                                    if (now.equals(release)) {
                                        int notifId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                                        notifBuilder.showNotification(getApplicationContext(), title, message, notifId);
                                    }
                                }

                                // ketika proses selesai, maka perlu dipanggil jobFinished dengan parameter false;
                                jobFinished(job, false);
                            }catch (Exception e) {
                                // ketika terjadi error, maka jobFinished diset dengan parameter true. Yang artinya job perlu di reschedule
                                jobFinished(job, true);
                                Log.d(TAG, "onResponse: error: " + e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

    }
}
