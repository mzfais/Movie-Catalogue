package id.ac.itn.moca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.system.Os;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.ac.itn.moca.adapter.NowPlayingAdapter;
import id.ac.itn.moca.fragment.NowPlayingFragment;
import id.ac.itn.moca.fragment.TvShowFragment;
import id.ac.itn.moca.fragment.UpcomingFragment;

public class ShowAllActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String APP_STATUS = "null";
    private static final String FIRST_LOAD = "true";
    public static final String PAGE = "now";
    private String fActive = "now";
    private Boolean firstLoad = true;
    private Boolean upfirstLoad = true;
    private Boolean tvfirstLoad = true;
    Toolbar toolbar;
    final FragmentManager fm = getSupportFragmentManager();
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.show_all_activity_title));
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            String page = getIntent().getStringExtra(PAGE);
            if (page.equals("now")) {
                //loadFragment(new NowPlayingFragment(), page);
                updateNavigationBarState(R.id.bottom_nav_now);
            } else if (page.equals("up")) {
                //loadFragment(new UpcomingFragment(), page);
                updateNavigationBarState(R.id.bottom_nav_upcoming);
            } else {
                //loadFragment(new TvShowFragment(), page);
                updateNavigationBarState(R.id.bottom_nav_tvs);
            }
        } else {
/*
            String status = savedInstanceState.getString(FIRST_LOAD);
            firstLoad = Boolean.parseBoolean(status);
            Log.d(TAG, "onCreate: now playing firstload: " + status);
*/
            if (savedInstanceState.getString(APP_STATUS).equals("now")) {
                loadFragment(new NowPlayingFragment(), "now");
                Log.d(TAG, "onCreate: meeting aktif");
            } else if (savedInstanceState.getString(APP_STATUS).equals("up")) {
                loadFragment(new UpcomingFragment(), "up");
                Log.d(TAG, "onCreate: upcoming aktif");
            } else {
                loadFragment(new TvShowFragment(), "tv");
                Log.d(TAG, "onCreate: tvshow aktif");
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment = null;
                    String tag = "now";
                    switch (menuItem.getItemId()) {
                        case R.id.bottom_nav_now:
                            fragment = new NowPlayingFragment();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(NowPlayingFragment.FIRST_LOAD, firstLoad);
                            fragment.setArguments(bundle);
                            tag = "now";
                            firstLoad = false;
                            break;
                        case R.id.bottom_nav_upcoming:
                            fragment = new UpcomingFragment();
                            Bundle upbundle = new Bundle();
                            upbundle.putBoolean(UpcomingFragment.FIRST_LOAD, upfirstLoad);
                            fragment.setArguments(upbundle);
                            tag = "up";
                            upfirstLoad = false;
                            break;
                        case R.id.bottom_nav_tvs:
                            fragment = new TvShowFragment();
                            Bundle tvbundle = new Bundle();
                            tvbundle.putBoolean(TvShowFragment.FIRST_LOAD, tvfirstLoad);
                            fragment.setArguments(tvbundle);
                            tvfirstLoad = false;
                            tag = "tv";
                            break;
                    }
                    //active = fragment;
                    return loadFragment(fragment, tag);
                    //return false;
                }
            };

    private boolean loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
            fActive = tag;
            Log.d(TAG, "loadFragment");
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FIRST_LOAD, String.valueOf(firstLoad));
        outState.putString(APP_STATUS, fActive);
    }

    private void updateNavigationBarState(int actionId) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            navigation.setSelectedItemId(actionId);
        } else {
            Menu menu = navigation.getMenu();
            View view;
            for (int i = 0, size = menu.size(); i < size; i++) {
                MenuItem item = menu.getItem(i);
                if (item.getItemId() == actionId) {
                    item.setChecked(true);
                    view = navigation.findViewById(actionId);
                    view.performClick();
                    break;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: kembali");
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
