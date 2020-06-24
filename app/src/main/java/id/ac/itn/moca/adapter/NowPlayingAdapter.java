package id.ac.itn.moca.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import id.ac.itn.moca.BuildConfig;
import id.ac.itn.moca.DetailActivity;
import id.ac.itn.moca.R;
import id.ac.itn.moca.model.Movie;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.MovieViewHolder> {
    private Context mCtx;
    private List<Movie> movieList;


    public NowPlayingAdapter(Context mCtx, List<Movie> movieList) {
        this.mCtx = mCtx;
        this.movieList = movieList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.now_playing_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie mov = getMovieList().get(position);
        Glide.with(mCtx).load(BuildConfig.MovieImgURL + "w185/" + mov.getPosterPath()).into(holder.ivPoster);
        holder.tvTitle.setText(mov.getTitle());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPoster;
        TextView tvTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Intent intent = new Intent(mCtx, DetailActivity.class);
            Gson gson = new Gson();
            String mItem = gson.toJson(getMovieList().get(pos));
            intent.putExtra(DetailActivity.TYPE_ITEMS, "movie");
            intent.putExtra(DetailActivity.MOVIE_ITEMS, mItem);
            mCtx.startActivity(intent);
        }

    }


}
