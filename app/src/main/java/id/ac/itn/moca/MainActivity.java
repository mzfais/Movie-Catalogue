package id.ac.itn.moca;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import id.ac.itn.moca.adapter.NowPlayingAdapter;
import id.ac.itn.moca.adapter.TvShowAdapter;
import id.ac.itn.moca.adapter.UpcomingAdapter;
import id.ac.itn.moca.model.MovieList;
import id.ac.itn.moca.model.TvShowList;
import id.ac.itn.moca.util.AppSetting;
import id.ac.itn.moca.viewmodel.MovieViewModel;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    RecyclerView rvNowPlaying, rvUpcoming, rvTvShow;
    NowPlayingAdapter nowPlayingAdapter;
    UpcomingAdapter upcomingAdapter;
    TvShowAdapter tvShowAdapter;
    MovieViewModel viewModel;
    ProgressBar progressBar, upcomingPb, tvPb;
    TextView npMsg, upMsg, tvshowMsg, tvAllNow, tvAllUp, tvAllTV;
    private EditText searchText;
    SwipeRefreshLayout swp;
    ImageView ivSetting;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = findViewById(R.id.et_search);
        swp = findViewById(R.id.swipe);
        swp.setOnRefreshListener(this);
        progressBar = findViewById(R.id.nowPlayingProgress);
        progressBar.setVisibility(View.VISIBLE);
        npMsg = findViewById(R.id.npMsg);
        npMsg.setVisibility(View.INVISIBLE);
        upMsg = findViewById(R.id.upMsg);
        upMsg.setVisibility(View.INVISIBLE);
        tvshowMsg = findViewById(R.id.tvshowMsg);
        tvshowMsg.setVisibility(View.INVISIBLE);
        upcomingPb = findViewById(R.id.upComingProgress);
        tvPb = findViewById(R.id.tvShowProgress);
        tvAllNow = findViewById(R.id.tvAllNow);
        tvAllUp = findViewById(R.id.tvAllUpcoming);
        tvAllTV = findViewById(R.id.tvAllTV);
        tvAllNow.setOnClickListener(this);
        tvAllUp.setOnClickListener(this);
        tvAllTV.setOnClickListener(this);
        rvNowPlaying = findViewById(R.id.rvNowPlaying);
        rvUpcoming = findViewById(R.id.rvUpcoming);
        rvTvShow = findViewById(R.id.rvTVShow);
        ivSetting = findViewById(R.id.ivSetting);
        ivSetting.setOnClickListener(this);
        rvNowPlaying.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvUpcoming.setLayoutManager(new LinearLayoutManager(this));
        rvTvShow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getMoviesNowPlaying().observe(this, new Observer<MovieList>() {
            @Override
            public void onChanged(MovieList movieList) {
                if (movieList.getResults() == null) {
                    npMsg.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onChanged: data gagal diupload");
                } else {
                    nowPlayingAdapter = new NowPlayingAdapter(MainActivity.this, movieList.getResults());
                    nowPlayingAdapter.notifyDataSetChanged();
                    rvNowPlaying.setAdapter(nowPlayingAdapter);
                    npMsg.setVisibility(View.GONE);
                    Log.d(TAG, "onChanged: now playing diupdate");
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getUpcomingList().observe(this, new Observer<MovieList>() {
            @Override
            public void onChanged(MovieList movies) {
                if (movies.getResults() == null) {
                    upMsg.setVisibility(View.VISIBLE);
                } else {
                    upcomingAdapter = new UpcomingAdapter(MainActivity.this, movies.getResults());
                    upcomingAdapter.notifyDataSetChanged();
                    rvUpcoming.setAdapter(upcomingAdapter);
                    upMsg.setVisibility(View.GONE);
                }
                upcomingPb.setVisibility(View.GONE);
            }
        });

        viewModel.getTvShowList().observe(this, new Observer<TvShowList>() {
            @Override
            public void onChanged(TvShowList tvShows) {
                if (tvShows.getResults() == null) {
                    tvshowMsg.setVisibility(View.VISIBLE);
                } else {
                    tvShowAdapter = new TvShowAdapter(MainActivity.this, tvShows.getResults());
                    tvShowAdapter.notifyDataSetChanged();
                    rvTvShow.setAdapter(tvShowAdapter);
                    tvshowMsg.setVisibility(View.GONE);
                }
                tvPb.setVisibility(View.GONE);
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra("KEYWORD", String.valueOf(textView.getText()));
                    searchText.setText("");
                    startActivity(intent);
                }
                return true;
            }
        });
        //PreferenceManager.setDefaultValues(this, R.xml.app_settings, false);
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //Boolean switchPref = sharedPref.getBoolean(AppSetting.REMINDER_UPCOMING, false);
    }

    @Override
    public void onRefresh() {
        swp.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        upcomingPb.setVisibility(View.VISIBLE);
        tvPb.setVisibility(View.VISIBLE);
        viewModel.Refresh();
        swp.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        String page = "now";
        switch (view.getId()) {
            case R.id.tvAllNow:
                page = "now";
                break;
            case R.id.tvAllUpcoming:
                page = "up";
                break;
            case R.id.tvAllTV:
                page = "tv";
                break;
            case R.id.ivSetting:
                Intent setting = new Intent(MainActivity.this,AppSetting.class);
                startActivity(setting);
                return;
        }
        Intent intent = new Intent(MainActivity.this, ShowAllActivity.class);
        intent.putExtra(ShowAllActivity.PAGE, page);
        startActivity(intent);
    }

}
