package com.example.gamers.interfaces;



import com.example.gamers.dataModel.VideoGame;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonGames {

    @GET("api/games")
    Call<List<VideoGame>> getGames();
}
