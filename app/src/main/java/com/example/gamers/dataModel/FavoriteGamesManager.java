package com.example.gamers.dataModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteGamesManager {

    private static final String SHARED_PREF_NAME = "favorite_games";
    private static final String KEY_FAVORITE_GAMES = "favorite_games_list";
    private static FavoriteGamesManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private FavoriteGamesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized FavoriteGamesManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoriteGamesManager(context);
        }
        return instance;
    }

    public void addFavoriteGame(VideoGame game) {
        List<VideoGame> favoriteGames = getFavoriteGames();
        if (!favoriteGames.contains(game)) { // בדיקה אם המשחק כבר קיים ברשימה
            favoriteGames.add(game);
            saveFavoriteGames(favoriteGames);
        } else {
            Log.d("FavoriteGamesManager", "Game already exists in favorites: " + game.getTitle());
        }
    }

    public void removeFavoriteGame(VideoGame game) {
        List<VideoGame> favoriteGames = getFavoriteGames();
        if (favoriteGames != null) {
            // בדיקה האם המשחק נמצא ברשימת המועדפים
            if (favoriteGames.remove(game)) {
                // אם המשחק נמצא ונמחק, עדכון של SharedPreferences
                saveFavoriteGames(favoriteGames);
            } else {
                Log.d("FavoriteGamesManager", "Failed to remove game: " + game.getTitle());
            }
        }
    }



    public List<VideoGame> getFavoriteGames() {
        String jsonFavorites = sharedPreferences.getString(KEY_FAVORITE_GAMES, null);
        Type type = new TypeToken<List<VideoGame>>() {}.getType();
        List<VideoGame> favoriteGames = gson.fromJson(jsonFavorites, type);
        if (favoriteGames == null) {
            favoriteGames = new ArrayList<>();
        }
        return favoriteGames;
    }

    private void saveFavoriteGames(List<VideoGame> favoriteGames) {
        String jsonFavorites = gson.toJson(favoriteGames);
        sharedPreferences.edit().putString(KEY_FAVORITE_GAMES, jsonFavorites).apply();
    }
}
