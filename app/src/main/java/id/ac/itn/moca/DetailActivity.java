package id.ac.itn.moca;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Objects;

import id.ac.itn.moca.db.FavouriteRepository;
import id.ac.itn.moca.model.Favourite;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.TvShow;
import id.ac.itn.moca.viewmodel.FavouriteViewModel;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    public static final String MOVIE_ITEMS = "movie_items";
    public static final String TYPE_ITEMS = "movie";
    private boolean isChecked = false;
    private String type;

    Toolbar toolbar;
    ImageView imTop, imPoster;
    TextView tvJudul, tvRating, tvRelease, tvOverview;
    RatingBar ratingBar;
    Movie mov;
    TvShow tv;
    Favourite fav;
    FavouriteRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        repository = new FavouriteRepository(getApplication());
        toolbar = findViewById(R.id.detail_toolbar);
        tvJudul = findViewById(R.id.tvJudul);
        tvRating = findViewById(R.id.tvRating);
        tvRelease = findViewById(R.id.tvRelease);
        tvOverview = findViewById(R.id.tvSinopsis);
        ratingBar = findViewById(R.id.ratingBar);
        imTop = findViewById(R.id.topImage);
        imPoster = findViewById(R.id.imgPoster);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String data = getIntent().getStringExtra(MOVIE_ITEMS);
        type = getIntent().getStringExtra(TYPE_ITEMS);
        Log.d(TAG, "onCreate: " + type);
        if (type != null) {
            // cek apakah movie berasal dari favourite
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
                    FavouriteViewModel viewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
                    viewModel.getFavItem(mov.getId()).observe(this, new Observer<Favourite>() {
                        @Override
                        public void onChanged(Favourite favourite) {
                            if (favourite != null) {
                                fav = favourite;
                                Log.d(TAG, "onOptionsItemSelected: ini favorit " + fav.getId());
                                isChecked = true;
                            }
                        }
                    });
                }
            } else if (type.equals("tv")) {
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
            } else {
                if (data != null) {
                    Gson gson = new Gson();
                    fav = gson.fromJson(data, Favourite.class);
                }
                if (fav != null) {
                    Log.d(TAG, "onCreate: " + fav.getId());
                    getSupportActionBar().setTitle(fav.getOriginalTitle());
                    Glide.with(this).load(BuildConfig.MovieImgURL + "w300/" + fav.getBackdropPath()).into(imTop);
                    Glide.with(this).load(BuildConfig.MovieImgURL + "w92/" + fav.getPosterPath()).into(imPoster);
                    tvJudul.setText(fav.getOriginalTitle());
                    ratingBar.setRating(Float.parseFloat(String.valueOf(fav.getVoteAverage())) * 5 / 10);
                    String review = fav.getVoteCount() + " " + getString(R.string.reviews_text);
                    tvRating.setText(review);
                    tvRelease.setText("Release Date: " + fav.getReleaseDate());
                    tvOverview.setText(fav.getOverview());
                    isChecked = true;
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.mn_fav);
        setFavStatus(item);
        if (type.equals("tv")) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
        Log.d(TAG, "onPrepareOptionsMenu: " + isChecked);
        return super.onPrepareOptionsMenu(menu);
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
                    intent.putExtra(Intent.EXTRA_SUBJECT, mov.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, mov.getOverview());
                    startActivity(Intent.createChooser(intent, null));
                }
            } else if (type.equals("tv")) {
                if (tv != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, tv.getName());
                    intent.putExtra(Intent.EXTRA_TEXT, tv.getOverview());
                    startActivity(Intent.createChooser(intent, null));
                }
            } else {
                if (fav != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, fav.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, fav.getOverview());
                    startActivity(Intent.createChooser(intent, null));
                }
            }

        } else if (item.getItemId() == R.id.mn_fav) {
            isChecked = !item.isChecked();
            addtoFavourite(isChecked);
            setFavStatus(item);
            return true;
        }
        return false;
    }

    private void addtoFavourite(Boolean status) {
        Log.d(TAG, "addtoFavourite: " + type);
        if (type.equals("movie")) {
            if (status) {
                Favourite favourite = new Favourite();
                favourite.setMovieId(mov.getId());
                favourite.setOriginalTitle(mov.getOriginalTitle());
                favourite.setTitle(mov.getTitle());
                favourite.setAdult(mov.isAdult());
                favourite.setOverview(mov.getOverview());
                favourite.setBackdropPath(mov.getBackdropPath());
                favourite.setOriginalLanguage(mov.getOriginalLanguage());
                favourite.setPosterPath(mov.getPosterPath());
                favourite.setReleaseDate(mov.getReleaseDate());
                favourite.setVideo(mov.isVideo());
                favourite.setVoteAverage(Double.parseDouble(String.valueOf(mov.getVoteAverage())));
                favourite.setVoteCount(mov.getVoteCount());
                repository.insert(favourite);
                Toast.makeText(this, "Film telah ditambahkan ke daftar favorit Anda", Toast.LENGTH_SHORT).show();
            } else {
                if (fav != null) {
                    repository.delete(fav);
                    Toast.makeText(this, "Film telah dihapus dari daftar favorit Anda", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (type.equals("favourite")) {
            if (status) {
                repository.insert(fav);
                Toast.makeText(this, getResources().getString(R.string.msg_add_to_fav), Toast.LENGTH_SHORT).show();
            } else {
                repository.delete(fav);
                Toast.makeText(this, getResources().getString(R.string.msg_del_from_fav), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setFavStatus(MenuItem item) {
        item.setChecked(isChecked);
        item.setIcon(isChecked ? R.drawable.ic_favorite_on : R.drawable.ic_favorite_border);
    }

}
