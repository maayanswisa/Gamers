package com.example.gamers.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.gamers.R;
import com.example.gamers.adapters.AdapterGames;
import com.example.gamers.dataModel.VideoGame;
import com.example.gamers.interfaces.JsonGames;
import com.example.gamers.interfaces.RecyclerViewGameInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GamesListFragment extends Fragment implements RecyclerViewGameInterface {

    private RecyclerView recyclerView;
    private List<VideoGame> videoGameListAll;
    private List<VideoGame> videoGameList;

    private Spinner spinner_cat;
    private Spinner spinner_plat;
    private Spinner spinner_developer;

    private int selectedCatPosition = 0;
    private int selectedPlatPosition = 0;
    private int selectedDeveloperPosition = 0;

    // Declare adapter globally
    private AdapterGames adapterGames;

    public GamesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_games_list, container, false);
        // טעינת האנימציה
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        // החלת האנימציה על ה-view הראשי של ה-fragment
        view.startAnimation(fadeInAnimation);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videoGameListAll = new ArrayList<>();
        videoGameList = new ArrayList<>();

        spinner_plat = view.findViewById(R.id.spinner_plat);
        spinner_developer = view.findViewById(R.id.spinner_developer);
        recyclerView = view.findViewById(R.id.recyclerViewGames);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // הגדר כאן לאיזה פרגמנט אתה רוצה לחזור
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_gamesListFragment_to_mainFragment);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        loadData();
        setUpSearchButton(); // Add this line
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.freetogame.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonGames jsonGames = retrofit.create(JsonGames.class);
        Call<List<VideoGame>> call = jsonGames.getGames();
        call.enqueue(new Callback<List<VideoGame>>() {
            @Override
            public void onResponse(Call<List<VideoGame>> call, Response<List<VideoGame>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                videoGameListAll = response.body();
                videoGameList.addAll(videoGameListAll); // Populate the list with all games initially

                // Initialize and set the adapter for RecyclerView here, after list is populated
                adapterGames = new AdapterGames(getContext(), videoGameList, GamesListFragment.this);
                recyclerView.setAdapter(adapterGames);

                setUpSpinners();
                setUpSearchButton();
            }

            @Override
            public void onFailure(Call<List<VideoGame>> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpSpinners() {
        // Set up each spinner with unique options and "All" as the default option
        setUpCategorySpinner();
        setUpPlatformSpinner();
        setUpDeveloperSpinner();
    }

    private void setUpCategorySpinner() {
        // Extract unique categories
        Set<String> categories = new HashSet<>();
        for (VideoGame game : videoGameListAll) {
            categories.add(game.getGenre());
        }

        // Convert the Set to a List for the ArrayAdapter
        List<String> categoryList = new ArrayList<>(categories);
        categoryList.add(0, "All");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        spinner_cat = requireView().findViewById(R.id.spinner_cat);
        spinner_cat.setAdapter(categoryAdapter);

        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterGamesList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setUpPlatformSpinner() {
        // Extract unique platforms
        Set<String> platforms = new HashSet<>();
        for (VideoGame game : videoGameListAll) {
            platforms.add(game.getPlatform());
        }

        // Convert the Set to a List for the ArrayAdapter
        List<String> platformList = new ArrayList<>(platforms);
        platformList.add(0, "All");
        ArrayAdapter<String> platformsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, platformList);
        spinner_plat = requireView().findViewById(R.id.spinner_plat);
        spinner_plat.setAdapter(platformsAdapter);

        spinner_plat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterGamesList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setUpDeveloperSpinner() {
        // Extract unique developers
        Set<String> developers = new HashSet<>();
        for (VideoGame game : videoGameListAll) {
            developers.add(game.getDeveloper());
        }

        // Convert the Set to a List for the ArrayAdapter
        List<String> developerList = new ArrayList<>(developers);
        developerList.add(0, "All");
        ArrayAdapter<String> developersAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, developerList);
        spinner_developer = requireView().findViewById(R.id.spinner_developer);
        spinner_developer.setAdapter(developersAdapter);

        spinner_developer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterGamesList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterGamesList() {
        String selectedCategory = spinner_cat.getSelectedItem().toString();
        String selectedPlatform = spinner_plat.getSelectedItem().toString();
        String selectedDeveloper = spinner_developer.getSelectedItem().toString();

        videoGameList.clear();
        for (VideoGame game : videoGameListAll) {
            boolean matchesCategory = selectedCategory.equals("All") || game.getGenre().equals(selectedCategory);
            boolean matchesPlatform = selectedPlatform.equals("All") || game.getPlatform().equals(selectedPlatform);
            boolean matchesDeveloper = selectedDeveloper.equals("All") || game.getDeveloper().equals(selectedDeveloper);

            if (matchesCategory && matchesPlatform && matchesDeveloper) {
                videoGameList.add(game);
            }
        }

        adapterGames.notifyDataSetChanged();
    }

    private void setUpSearchButton() {
        Button searchButton = requireView().findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();

            }
        });
    }

    private void performSearch() {
        EditText editTextSearch = requireView().findViewById(R.id.editTextSearch);
        String searchText = editTextSearch.getText().toString().toLowerCase().trim();

        List<VideoGame> filteredGames = new ArrayList<>();
        Set<String> addedTitles = new HashSet<>(); // רשימה של כל הכותרות שכבר נוספו

        for (VideoGame game : videoGameListAll) {
            String title = game.getTitle().toLowerCase();
            String description = game.getShort_description().toLowerCase();
            String releaseDate = game.getRelease_date().toLowerCase();

            // בדיקה האם המשחק מתאים לחיפוש לפי שם, תקציר, או תאריך
            boolean matchesSearch = title.contains(searchText) || description.contains(searchText) || releaseDate.contains(searchText);

            // בדיקה האם המשחק כבר נוסף לרשימה ואם הוא מתאים לחיפוש
            if (!addedTitles.contains(title) && matchesSearch) {
                filteredGames.add(game);
                addedTitles.add(title); // הוספת הכותרת לרשימת הכותרות שכבר נוספו
            }
        }

        videoGameList.clear();
        videoGameList.addAll(filteredGames);
        adapterGames.notifyDataSetChanged();


    }


    @Override
    public void onItemClick(int position) {
        VideoGame clickedGame = videoGameList.get(position);

        // בניית ה־Bundle עם פרטי המשחק
        Bundle bundle = new Bundle();
        bundle.putString("Title", clickedGame.getTitle());
        bundle.putString("Genre", clickedGame.getGenre());
        bundle.putString("Description", clickedGame.getShort_description());
        bundle.putString("Platform", clickedGame.getPlatform());
        bundle.putString("Image", clickedGame.getThumbnailUrl());
        bundle.putString("Publisher", clickedGame.getPublisher());
        bundle.putString("Developer", clickedGame.getDeveloper());
        bundle.putString("ReleaseDate", clickedGame.getRelease_date());
        bundle.putString("GameUrl", clickedGame.getGame_url());
        bundle.putString("FreetogameProfileUrl", clickedGame.getFreetogame_profile_url());

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_gamesListFragment_to_gameDetailsFragment, bundle);
    }



}
