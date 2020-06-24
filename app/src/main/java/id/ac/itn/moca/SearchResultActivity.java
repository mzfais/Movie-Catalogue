package id.ac.itn.moca;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import id.ac.itn.moca.adapter.SearchResultAdapter;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.NetworkState;
import id.ac.itn.moca.viewmodel.SearchMovieViewModel;
import id.ac.itn.moca.viewmodel.SearchViewModelFactory;

public class SearchResultActivity extends AppCompatActivity {
    private static final String TAG = "SearchResultActivity";
    SearchResultAdapter adapter;
    SearchMovieViewModel viewModel;
    //private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        RecyclerView rvMovie = findViewById(R.id.rvMovieList);
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchResultAdapter(this);
        //searchText = findViewById(R.id.et_search);
        String keyword = getIntent().getStringExtra("KEYWORD");
        if (keyword == null) {
            keyword = "";
        }
        viewModel = new ViewModelProvider(this, new SearchViewModelFactory(keyword)).get(SearchMovieViewModel.class);
        initViewModel();
        rvMovie.setAdapter(adapter);
    }

    void initViewModel() {
        viewModel.searchMoviePagedList.observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                    adapter.submitList(movies);
                    Log.d(TAG, "onChanged:" + movies.size());
            }
        });
        viewModel.getNetworkStateLiveData().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                adapter.setNetworkState(networkState);
                Log.d(TAG, "onChanged: " + networkState.getMsg());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            final SearchView searchView = (SearchView) menu.findItem(R.id.mn_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    viewModel.searchMovie(query);
                    //initViewModel();
                    hideKeyboard();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void hideKeyboard() {
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) this.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(
                    Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception ex) {
            Log.d(TAG, "hideKeyboard: " + ex.getMessage());
        }
    }

}
