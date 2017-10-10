package com.example.android.newsapp;

/**
 * Created by kevincurtis on 15/07/2017.
 */

public class CurrentNews {

    private String title;
    private String section;
    private String url;

    public CurrentNews(String title, String section, String url) {
        this.title = title;
        this.section = section;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getUrl() {
        return url;
    }

}