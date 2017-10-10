package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by kevincurtis on 15/07/2017.
 */

public class CurrentNewsLoader extends AsyncTaskLoader<List<CurrentNews>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = CurrentNewsLoader.class.getName();

    /**
     * Query URL
     */
    private String url;

    public CurrentNewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<CurrentNews> loadInBackground() {
        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of articles.
        List<CurrentNews> articles = Queries.fetchArticlesData(url);
        return articles;
    }
}
