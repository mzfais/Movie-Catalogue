package id.ac.itn.moca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;

import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.TvShow;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    public static final String MOVIE_ITEMS = "movie_items";
    public static final String TYPE_ITEMS = "movie";
    private String type;
    Toolbar toolbar;
    ImageView imTop, imPoster;
    TextView tvJudul, tvRating, tvRelease, tvOverview;
    RatingBar ratingBar;
    Movie mov;
    TvShow tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.detail_toolbar);
        tvJudul = findViewById(R.id.tvJudul);
        tvRating = findViewById(R.id.tvRating);
        tvRelease = findViewById(R.id.tvRelease);
        tvOverview = findViewById(R.id.tvSinopsis);
        ratingBar = findViewById(R.id.ratingBar);
        imTop = findViewById(R.id.topImage);
        imPoster = findViewById(R.id.imgPoster);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String data = getIntent().getStringExtra(MOVIE_ITEMS);
        type = getIntent().getStringExtra(TYPE_ITEMS);
        Log.d(TAG, "onCreate: " + type);
        if (type != null) {
            if (type.equals("movie")) {
                if (data != null) {
                    Gson gson = new Gson();
                    mov = gson.fromJson(data, Movie.class);
                }
                if (mov != null) {
                    getSupportActionBar().setTitle(mov.getOriginalTitle());
                    Glide.with(this).load(BuildConfig.MovieImgURL + "w300/" + mov.getBackdropPath()).into(imTop);
                    Glide.with(this).load(BuildConfig.MovieImgURL + "w92/" + mov.getPosterPath()).into(imPoster);
                    tvJudul.setText(mov.getOriginalTitle());
                    ratingBar.setRating(Float.parseFloat(String.valueOf(mov.getVoteAverage())) * 5 / 10);
                    String review = mov.getVoteCount() + " " + getString(R.string.reviews_text);
                    tvRating.setText(review);
                    tvRelease.setText("Release Date: " + mov.getReleaseDate());
                    tvOverview.setText(mov.getOverview());
                }
            } else {
                if (data != null) {
                    Gson gson = new Gson();
                    tv = gson.fromJson(data, TvShow.class);
                }
                if (tv != null) {
                    getSupportActionBar().setTitle(tv.getOriginalName());
                    Glide.with(this).load(BuildConfig.MovieImgURL + "w300/" + tv.getBackdropPath()).into(imTop);
                    Glide.with(this).load(BuildConfig.MovieImgURL + "w92/" + tv.getPosterPath()).into(imPoster);
                    tvJudul.setText(tv.getOriginalName());
                    ratingBar.setRating(Float.parseFloat(String.valueOf(tv.getVoteAverage())) * 5 / 10);
                    String review = tv.getVoteCount() + " " + getString(R.string.reviews_text);
                    tvRating.setText(review);
                    tvRelease.setText("First Air Date: " + tv.getFirstAirDate());
                    tvOverview.setText(tv.getOverview());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.mn_share) {
            if (type.equals("movie")) {
                if (mov != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, mov.getTitle()) ;
                    intent.putExtra(Intent.EXTRA_TEXT, mov.getOverview());
                    startActivity(Intent.createChooser(intent, null));
                }
            } else {
                if (tv != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, tv.getName());
                    intent.putExtra(Intent.EXTRA_TEXT, tv.getOverview());
                    startActivity(Intent.createChooser(intent, null));
                }
            }

        }
        return false;
    }

}
