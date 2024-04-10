package com.example.gamers.dataModel;

import java.util.Objects;

public class VideoGame {
    private String id;
    private String title;
    private String thumbnail;
    private String short_description;
    private String genre;
    private String platform;
    private String game_url;
    private String publisher;
    private String developer;
    private String release_date;
    private String freetogame_profile_url;




    public String getShort_description() {
        return short_description;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public String getGame_url() {
        return game_url;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getFreetogame_profile_url() {
        return freetogame_profile_url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailUrl() {
        return thumbnail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setGame_url(String game_url) {
        this.game_url = game_url;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setFreetogame_profile_url(String freetogame_profile_url) {
        this.freetogame_profile_url = freetogame_profile_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGame videoGame = (VideoGame) o;
        return Objects.equals(title, videoGame.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

}
