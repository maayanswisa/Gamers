package com.example.gamers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamers.R;
import com.example.gamers.dataModel.VideoGame;
import com.example.gamers.interfaces.RecyclerViewGameInterface;

import java.util.ArrayList;
import java.util.List;

public class AdapterGames extends RecyclerView.Adapter<AdapterGames.ViewHolder> implements Filterable {
    Context context;
    List<VideoGame> videoGameList; // List used for displaying data
    List<VideoGame> videoGameListAll; // Original list of all data

    RecyclerViewGameInterface itemClickListener;

    public AdapterGames(Context context, List<VideoGame> videoGameList, RecyclerViewGameInterface itemClickListener) {
        this.context = context;
        this.videoGameList = videoGameList;
        this.itemClickListener = itemClickListener;
        this.videoGameListAll = new ArrayList<>(videoGameList); // Create a copy of the list for all data
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_layout, parent, false);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.genre.setText(videoGameList.get(position).getGenre());
        holder.title.setText(videoGameList.get(position).getTitle());
        holder.short_description.setText(videoGameList.get(position).getShort_description());
        holder.platform.setText(videoGameList.get(position).getPlatform());

        Glide.with(context).load(videoGameList.get(position).getThumbnailUrl()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return videoGameList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String searchString = charSequence.toString().toLowerCase();

                if (searchString.isEmpty()) {
                    filterResults.values = videoGameListAll;
                } else {
                    List<VideoGame> filteredList = new ArrayList<>();
                    for (VideoGame game : videoGameListAll) { // Filter from the complete list
                        if (game.getTitle().toLowerCase().contains(searchString) ||
                                game.getShort_description().toLowerCase().contains(searchString) ||
                                game.getRelease_date().toLowerCase().contains(searchString)) {
                            filteredList.add(game);
                        }
                    }
                    filterResults.values = filteredList;
                }
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //noinspection unchecked
                videoGameList.clear();
                videoGameList.addAll((List<VideoGame>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RecyclerViewGameInterface itemClickListener;
        TextView short_description, genre, title, platform;
        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView, RecyclerViewGameInterface itemClickListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;

            short_description = itemView.findViewById(R.id.txt_short_description);
            genre = itemView.findViewById(R.id.txt_genre);
            title = itemView.findViewById(R.id.txt_title);
            platform = itemView.findViewById(R.id.txt_platform);
            thumbnail = itemView.findViewById(R.id.iv_thumbnail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(position);
                }
            }
        }
    }
}
