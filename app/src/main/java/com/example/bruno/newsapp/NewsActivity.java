package com.example.bruno.newsapp;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final int News_Loader_ID = 0;
    private NewsAdapter madapter;
    public static final String LOG_TAG = NewsActivity.class.getName();
    private static final String base_url = "http://content.guardianapis.com/search?q=politics&order-by=newest&show-tags=contributor&api-key=test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ListView newslistView = (ListView) findViewById(R.id.list);
        madapter = new NewsAdapter(this, new ArrayList<News>());
        newslistView.setAdapter(madapter);
        newslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News url = madapter.getItem(position);
                Uri newsUri = Uri.parse(url.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
        LoaderManager newsloader = getLoaderManager();
        newsloader.initLoader(News_Loader_ID, null, this);
    }
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewLoader(this, base_url);
    }
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news){
        if(news != null){
            madapter.clear();

            madapter.addAll(news);
        }
        Log.e(LOG_TAG, "problem with something");
    }
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
    }
    }
