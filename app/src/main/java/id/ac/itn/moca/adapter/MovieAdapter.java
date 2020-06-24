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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context mCtx;
    private List<Movie> movieList;

    public MovieAdapter(Context mCtx, List<Movie> movieList) {
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
        View view = LayoutInflater.from(mCtx).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie mov = getMovieList().get(position);
        Glide.with(mCtx).load(BuildConfig.MovieImgURL + "w185/" + mov.getPosterPath()).into(holder.ivPoster);
        holder.tvTitle.setText(mov.getTitle());
        holder.tvRelease.setText(mov.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPoster, ivShare;
        TextView tvTitle, tvRelease;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvRelease = itemView.findViewById(R.id.tvReleaseDate);
            ivShare = itemView.findViewById(R.id.ivShare);
            ivShare.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Movie mov = getMovieList().get(pos);
            if(view.getId()==R.id.ivShare){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,mov.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT,mov.getOverview());
                mCtx.startActivity(Intent.createChooser(intent,null));
                //Toast.makeText(mCtx,"Share Film " + getMovieList().get(pos).getTitle(),Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(mCtx, DetailActivity.class);
                Gson gson = new Gson();
                String mItem = gson.toJson(mov);
                intent.putExtra(DetailActivity.TYPE_ITEMS, "movie");
                intent.putExtra(DetailActivity.MOVIE_ITEMS, mItem);
                mCtx.startActivity(intent);
            }
        }
    }
}
