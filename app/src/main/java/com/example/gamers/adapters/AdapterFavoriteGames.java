package com.example.gamers.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamers.R;
import com.example.gamers.dataModel.FavoriteGamesManager;
import com.example.gamers.dataModel.VideoGame;


import java.util.List;

public class AdapterFavoriteGames extends RecyclerView.Adapter<AdapterFavoriteGames.GameViewHolder> {

    private Context context;
    private List<VideoGame> favoriteGamesList;

    public AdapterFavoriteGames(Context context, List<VideoGame> favoriteGamesList) {
        this.context = context;
        this.favoriteGamesList = favoriteGamesList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_layout, parent, false);
        return new GameViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        VideoGame game = favoriteGamesList.get(position);
        holder.bind(game);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // יצירת Bundle עם פרטי המשחק
                Bundle bundle = new Bundle();
                bundle.putString("Title", game.getTitle());
                bundle.putString("Genre", game.getGenre());
                bundle.putString("Description", game.getShort_description());
                bundle.putString("Platform", game.getPlatform());
                bundle.putString("Image", game.getThumbnailUrl());
                bundle.putString("Publisher", game.getPublisher());
                bundle.putString("Developer", game.getDeveloper());
                bundle.putString("ReleaseDate", game.getRelease_date());
                bundle.putString("GameUrl", game.getGame_url());
                bundle.putString("FreetogameProfileUrl", game.getFreetogame_profile_url());

                // שליחת ה-Bundle ל־GameDetailsFragment ופתיחתו
                Navigation.findNavController(v).navigate(R.id.action_favoritesFragment_to_gameDetailsFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteGamesList.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        private TextView gameTitleTextView;
        private TextView gameGenreTextView;
        private ImageView gameImageView;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitleTextView = itemView.findViewById(R.id.txt_title);
            gameGenreTextView = itemView.findViewById(R.id.txt_genre);
            gameImageView = itemView.findViewById(R.id.iv_thumbnail);
        }

        public void bind(VideoGame videoGame) {
            gameTitleTextView.setText(videoGame.getTitle());
            gameGenreTextView.setText(videoGame.getGenre());
            // טעינת התמונה מה-URL בעזרת Glide
            Glide.with(context)
                    .load(videoGame.getThumbnailUrl())
                    .into(gameImageView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        VideoGame gameToRemove = favoriteGamesList.get(position);
                        // הסרת המשחק מרשימת המועדפים במחלקת הניהול
                        FavoriteGamesManager.getInstance(context).removeFavoriteGame(gameToRemove);
                        // הסרת הפריט מרשימת המועדפים בתצוגה ועדכון התצוגה
                        favoriteGamesList.remove(position);
                        notifyItemRemoved(position);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}

