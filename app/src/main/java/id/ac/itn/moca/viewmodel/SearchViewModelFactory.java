package id.ac.itn.moca.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    String keyword = "";

    public SearchViewModelFactory(String filter) {
        this.keyword = filter;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchMovieViewModel(this.keyword);
    }
}
