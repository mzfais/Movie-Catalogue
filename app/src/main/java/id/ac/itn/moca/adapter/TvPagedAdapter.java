package id.ac.itn.moca.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import id.ac.itn.moca.BuildConfig;
import id.ac.itn.moca.DetailActivity;
import id.ac.itn.moca.R;
import id.ac.itn.moca.model.Movie;
import id.ac.itn.moca.model.NetworkState;
import id.ac.itn.moca.model.TvShow;

public class TvPagedAdapter extends PagedListAdapter<TvShow, RecyclerView.ViewHolder> {
    private static final String TAG = "TvPagedAdapter";
    private Context mCtx;
    public static final int NETWORK_VIEW_TYPE = 0;
    public static final int MOVIE_VIEW_TYPE = 1;
    private NetworkState mNetworkState;

    public TvPagedAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    private static DiffUtil.ItemCallback<TvShow> DIFF_CALLBACK = new DiffUtil.ItemCallback<TvShow>() {
        @Override
        public boolean areItemsTheSame(@NonNull TvShow oldItem, @NonNull TvShow newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull TvShow oldItem, @NonNull TvShow newItem) {
            return oldItem == newItem;
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == MOVIE_VIEW_TYPE) {
            view = inflater.inflate(R.layout.movie_list_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_loading, parent, false);
            return new NetworkStateItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder movieViewHolder = (ItemViewHolder) holder;
            TvShow mov = getItem(position);
            movieViewHolder.bind(mov);
        } else {
            NetworkStateItemViewHolder networkStateItemViewHolder = (NetworkStateItemViewHolder) holder;
            networkStateItemViewHolder.bind(mNetworkState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return NETWORK_VIEW_TYPE;
        } else {
            return MOVIE_VIEW_TYPE;
        }
    }

    private boolean hasExtraRow() {
        return mNetworkState != null && mNetworkState != NetworkState.LOADED;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow()?1:0);
    }

    public void setNetworkState(NetworkState newNetworkState) {
        Log.d(TAG, "setNetworkState: " + newNetworkState.getMsg());
        NetworkState previousState = this.mNetworkState;
        boolean previousExtraRow = hasExtraRow();
        this.mNetworkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    public void clear() {
        if (getItemCount() > 0) {
            getCurrentList().clear();
            notifyDataSetChanged();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPoster, ivShare;
        TextView tvTitle, tvRelease;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvRelease = itemView.findViewById(R.id.tvReleaseDate);
            ivShare = itemView.findViewById(R.id.ivShare);
            ivShare.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(TvShow movie) {
            Glide.with(mCtx).load(BuildConfig.MovieImgURL + "w185/" + movie.getPosterPath()).into(ivPoster);
            tvTitle.setText(movie.getName());
            tvRelease.setText(movie.getFirstAirDate());
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            TvShow mov = getItem(pos);
            if (view.getId() == R.id.ivShare) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, mov.getOriginalName());
                intent.putExtra(Intent.EXTRA_TEXT, mov.getOverview());
                mCtx.startActivity(Intent.createChooser(intent, null));
            }else{
                Intent intent = new Intent(mCtx, DetailActivity.class);
                Gson gson = new Gson();
                String mItem = gson.toJson(mov);
                intent.putExtra(DetailActivity.TYPE_ITEMS, "tv");
                intent.putExtra(DetailActivity.MOVIE_ITEMS, mItem);
                mCtx.startActivity(intent);
            }
        }
    }

    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;
        TextView errorMsg;

        public NetworkStateItemViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            errorMsg = itemView.findViewById(R.id.error_msg_item);
            //errorMsg.setVisibility(View.INVISIBLE);
        }

        public void bind(NetworkState networkState) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }

            String packageName = mCtx.getPackageName();
            if (networkState != null && networkState == NetworkState.FAIL) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(mCtx.getResources().getIdentifier(networkState.getMsg(),"string",packageName));
                Log.d(TAG, "bind: error tampil");
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(mCtx.getResources().getIdentifier(networkState.getMsg(),"string",packageName));
            } else {
                errorMsg.setVisibility(View.GONE);
            }

        }
    }
}
