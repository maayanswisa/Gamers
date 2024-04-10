package com.example.gamers.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamers.R;
import com.example.gamers.adapters.AdapterFavoriteGames;
import com.example.gamers.dataModel.FavoriteGamesManager;
import com.example.gamers.dataModel.VideoGame;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private List<VideoGame> favoriteGamesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Initialize the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerFavorite);

        // Set the layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        favoriteGamesList = FavoriteGamesManager.getInstance(requireContext()).getFavoriteGames();

        recyclerView.setAdapter(new AdapterFavoriteGames(requireContext(), favoriteGamesList));

        return view;
    }

}
