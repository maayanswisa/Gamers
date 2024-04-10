package com.example.gamers.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gamers.R;
import com.example.gamers.dataModel.FavoriteGamesManager;
import com.example.gamers.dataModel.VideoGame;

public class GameDetailsFragment extends Fragment {

    public GameDetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // קבלת המידע מה־Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("Title");
            String genre = bundle.getString("Genre");
            String description = bundle.getString("Description");
            String platform = bundle.getString("Platform");
            String image = bundle.getString("Image");
            String publisher = bundle.getString("Publisher");
            String developer = bundle.getString("Developer");
            String releaseDate = bundle.getString("ReleaseDate");

            // תצוגת המידע באמצעות הרכיבים המתאימים
            TextView txtTitle = view.findViewById(R.id.txt_title);
            TextView txtGenre = view.findViewById(R.id.txt_genre);
            TextView txtPlatform = view.findViewById(R.id.txt_platform);
            TextView txtDescription = view.findViewById(R.id.txt_short_description);
            ImageView ivImage = view.findViewById(R.id.iv_thumbnail);
            TextView txtPublisher = view.findViewById(R.id.txt_publisher);
            TextView txtDeveloper = view.findViewById(R.id.txt_developer);
            TextView txtReleaseDate = view.findViewById(R.id.txt_release_date);

            txtTitle.setText(title);
            txtGenre.setText(genre);
            txtPlatform.setText(platform);
            txtDescription.setText(description);
            Glide.with(requireContext()).load(image).into(ivImage);
            txtPublisher.setText(publisher);
            txtDeveloper.setText(developer);
            txtReleaseDate.setText(releaseDate);
        }

        // קישור לכתובת URL של המשחק
        Button btnGameUrl = view.findViewById(R.id.btnOpenGameUrl);
        btnGameUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameUrl = bundle.getString("GameUrl");
                if (gameUrl != null && !gameUrl.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gameUrl));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(requireContext(), "No game available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnFavorite = view.findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                if (bundle != null) {
                    VideoGame clickedGame = new VideoGame();
                    clickedGame.setTitle(bundle.getString("Title"));
                    clickedGame.setGenre(bundle.getString("Genre"));
                    clickedGame.setShort_description(bundle.getString("Description"));
                    clickedGame.setPlatform(bundle.getString("Platform"));
                    clickedGame.setThumbnail(bundle.getString("Image"));
                    clickedGame.setPublisher(bundle.getString("Publisher"));
                    clickedGame.setDeveloper(bundle.getString("Developer"));
                    clickedGame.setRelease_date(bundle.getString("ReleaseDate"));
                    clickedGame.setGame_url(bundle.getString("GameUrl"));
                    clickedGame.setFreetogame_profile_url(bundle.getString("FreetogameProfileUrl"));

                    FavoriteGamesManager.getInstance(requireContext()).addFavoriteGame(clickedGame);
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_gameDetailsFragment_to_favoritesFragment);
                }
            }
        });


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // הגדר כאן לאיזה פרגמנט אתה רוצה לחזור
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_gameDetailsFragment_to_gamesListFragment);
            }
        });







    }




}
