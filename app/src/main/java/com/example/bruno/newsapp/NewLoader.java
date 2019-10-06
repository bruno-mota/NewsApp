package com.example.bruno.newsapp;
import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;
import static com.example.bruno.newsapp.NewsActivity.LOG_TAG;
/**
 * Created by Bruno on 1/3/2018.
 */
public class NewLoader extends AsyncTaskLoader<List<News>> {
    private String mUrl;
    public NewLoader(Context context, String url){
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading(){
        super.onStartLoading();
        forceLoad();
    }
    @Override
    public List<News> loadInBackground(){
        if(mUrl == null){
            return null;
        }
        List<News> news = QueryUtils.getData(mUrl);
        return news;
    }

}
