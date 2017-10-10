package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.newsapp.R.id.loading_indicator;


/**
 * Created by kevincurtis on 15/07/2017.
 */

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<CurrentNews>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    /**
     * URL for article data from the Guardian API
     */
    private static final String GUARDIAN_API_REQUEST_URL =
            "https://content.guardianapis.com/search?api-key=test&show-tags=contributor&q=";


    private static final int NEWS_LOADER_ID = 1;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(loading_indicator)
    ProgressBar loadingIndicator;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.list)
    ListView newsListView;
    String builtURL = "";

    private CurrentNewsAdapter adapter;

    /**
     * TextView that is displayed when the list is empty
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Find a reference to the {@link ListView} in the layout
        newsListView.setEmptyView(emptyView);
        // Create a new adapter that takes an empty list of articles as input
        adapter = new CurrentNewsAdapter(this, new ArrayList<CurrentNews>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                CurrentNews currentArticle = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                // Create a new intent to view URI
                Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(webIntent);
            }
        });

        //call helper method for network connectivity
        if (checkNetwork()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            emptyView.setText(R.string.no_connection);
        }

        //get the searchview
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (checkNetwork()) {
                    loadingIndicator.setVisibility(View.VISIBLE);

                    String userQuery = searchView.getQuery().toString();
                    userQuery = userQuery.replace(" ", "+");

                    builtURL = GUARDIAN_API_REQUEST_URL + userQuery;
                    //restart load manager
                    Log.v(LOG_TAG, userQuery);
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    searchView.clearFocus();
                    //reset variable builturl to empty string in order to be ready for the next search
                    builtURL = "";
                } else {
                    loadingIndicator.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(R.string.no_connection);
                    //clear the adapter or we won't be able to see the empty view!
                    adapter.clear();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public Loader<List<CurrentNews>> onCreateLoader(int i, Bundle bundle) {

        if (builtURL.equals("")) {
            builtURL = GUARDIAN_API_REQUEST_URL;
        }
        // Create a new loader for the given URL
        return new CurrentNewsLoader(this, builtURL);
    }

    @Override
    public void onLoadFinished(Loader<List<CurrentNews>> loader, List<CurrentNews> articles) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No articles found."
        emptyView.setText(R.string.no_articles);
        // Clear the adapter of previous articles data
        adapter.clear();

        if (articles != null && !articles.isEmpty()) {
            adapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<CurrentNews>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }

    //helper method to check network connectivity
    public boolean checkNetwork() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        return (networkInfo != null && networkInfo.isConnected());
    }
}
