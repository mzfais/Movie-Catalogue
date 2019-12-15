package id.ac.itn.moca.util;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import id.ac.itn.moca.R;
import id.ac.itn.moca.service.AlarmReceiver;
import id.ac.itn.moca.service.UpcomingJobService;

public class AppSetting extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appsetting);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.action_setting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new MyPreferenceFragment())
                .commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public static class MyPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
        private static final String TAG = "mocaSetting";
        private static final String DISPATCHER_TAG = "upcomingmoviejob";
        String daily_reminder, reminder_upcoming, setting_locale;
        private AlarmReceiver alarmReceiver = new AlarmReceiver();
        private FirebaseJobDispatcher dispatcher;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.app_settings, rootKey);
            //reminder_upcoming = findPreference(this.getResources().getString(R.string.key_upcoming_reminder));
            reminder_upcoming = this.getResources().getString(R.string.key_upcoming_reminder);
            daily_reminder = this.getResources().getString(R.string.key_daily_reminder);
            setting_locale = this.getResources().getString(R.string.key_setting_locale);
            Objects.requireNonNull(findPreference(reminder_upcoming)).setOnPreferenceChangeListener(this);
            Objects.requireNonNull(findPreference(daily_reminder)).setOnPreferenceChangeListener(this);
            Objects.requireNonNull(findPreference(setting_locale)).setOnPreferenceClickListener(this);
            dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(Objects.requireNonNull(getActivity())));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            boolean isOn = (boolean) newValue;
            if (key.equals(daily_reminder)) {
                if (isOn) {
                    alarmReceiver.setRepeatingAlarm(Objects.requireNonNull(getActivity()), AlarmReceiver.TYPE_REPEATING, "07:00", getResources().getString(R.string.label_alarm_daily_reminder));
                } else {
                    alarmReceiver.cancelAlarm(Objects.requireNonNull(getActivity()), AlarmReceiver.TYPE_REPEATING);
                }
                return true;
            }
            if (key.equals(reminder_upcoming)) {
                if (isOn) {
                    startUpcomingJob();
                } else {
                    dispatcher.cancel(DISPATCHER_TAG);
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            if (key.equals(setting_locale)) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                return true;
            }

            return false;
        }

        private void startUpcomingJob() {
            int minutesToStart = 59;
            int hoursToStart = 7;
            Calendar calendar = Calendar.getInstance();
            int minute = calendar.get(Calendar.MINUTE);
            long startM = TimeUnit.MINUTES.toSeconds(minutesToStart - minute);//TimeUnit.MINUTES.toMillis(minutesToStart - minute); // 40 in the example
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            long startH = TimeUnit.HOURS.toSeconds((hoursToStart - hour) % 24);
            int seconds = calendar.get(Calendar.SECOND);//TimeUnit.HOURS.toMillis((hoursToStart - hour) % 24); // 1 in the example
            Log.i(TAG, "hitungwaktu: hour: " + hour + ", minute: " + minute);
            Log.i(TAG, "hitungwaktu: hour: " + startH + ", minute: " + startM);
            long startMs = startH + startM + seconds;//TimeUnit.MINUTES.toSeconds((minutesToStart - (minute))) + TimeUnit.HOURS.toSeconds(((hoursToStart - hour) % 24));
            if (startMs < 0) {
                Log.d(TAG, "jadwal pada hari berikutnya...");
                startMs += TimeUnit.DAYS.toSeconds(1);
            }
            long endMs = startMs + 1;//TimeUnit.MINUTES.toSeconds(1);
            int start = Math.toIntExact(startMs);
            int end = Math.toIntExact(endMs);

            Log.i(TAG, "hitungwaktu: startMs: " + start + ", endMs: " + end);
            Log.d(TAG, "hitungwaktu: hasil startMs: " + convertSecondsToHMmSs(start));

            Job synJob = dispatcher.newJobBuilder()
                    .setService(UpcomingJobService.class)
                    .setTag(DISPATCHER_TAG)
                    .setRecurring(true)
                    .setReplaceCurrent(true)
                    .setLifetime(Lifetime.FOREVER)
                    .setTrigger(Trigger.executionWindow(start, end))
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .build();
            dispatcher.mustSchedule(synJob);
        }

        private String convertSecondsToHMmSs(long seconds) {
            long s = seconds % 60;
            long m = (seconds / 60) % 60;
            long h = (seconds / (60 * 60)) % 24;
            return String.format("%d:%02d:%02d", h, m, s);
        }
    }
}
