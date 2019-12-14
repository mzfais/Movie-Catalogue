package id.ac.itn.moca.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import id.ac.itn.moca.BuildConfig;
import id.ac.itn.moca.DetailActivity;
import id.ac.itn.moca.R;
import id.ac.itn.moca.model.TvShow;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    private Context mCtx;
    private List<TvShow> tvShowList;

    public TvShowAdapter(Context mCtx, List<TvShow> movieList) {
        this.mCtx = mCtx;
        this.tvShowList = movieList;
    }

    public List<TvShow> getTvShowList() {
        return tvShowList;
    }

    public void setTvShowList(List<TvShow> movieList) {
        this.tvShowList = movieList;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.now_playing_list_item, parent, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        TvShow tv = getTvShowList().get(position);
        Glide.with(mCtx).load(BuildConfig.MovieImgURL + "w185/" + tv.getPosterPath()).into(holder.ivPoster);
        holder.tvTitle.setText(tv.getOriginalName());
    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    public class TvShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPoster;
        TextView tvTitle;

        public TvShowViewHolder(@NonNull View itemView) {
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
                String mItem = gson.toJson(getTvShowList().get(pos));
                intent.putExtra(DetailActivity.TYPE_ITEMS, "tv");
                intent.putExtra(DetailActivity.MOVIE_ITEMS, mItem);
                mCtx.startActivity(intent);
        }
    }
}
