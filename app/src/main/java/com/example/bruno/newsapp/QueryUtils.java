package com.example.bruno.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.bruno.newsapp.NewsActivity.LOG_TAG;

/**
 * Created by Bruno on 1/3/2018.
 */
public final class QueryUtils {
    private QueryUtils() {
    }
    public static List<News> getData(String newsUrl){
        URL url = createURL(newsUrl);
        String json = null;
        try {
            json = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG,  "Couldn't make http request.", e);
        }
        List<News> news = extractFromJson(json);
        return news;
    }
    public static List<News> extractFromJson(String newsJson){
        if(TextUtils.isEmpty(newsJson)){
            return null;
        }
        List<News> news = new ArrayList<>();
        try{
            JSONObject rootJson = new JSONObject(newsJson);
            JSONObject jsonresponse = rootJson.getJSONObject("response");
            JSONArray resultsarr = jsonresponse.getJSONArray("results");
            for(int i = 0; i < resultsarr.length(); i++){
                JSONObject jsonObject = resultsarr.getJSONObject(i);
                String section = jsonObject.getString("sectionName");
                String title = jsonObject.getString("webTitle");
                String date = jsonObject.getString("webPublicationDate");
                String url = jsonObject.getString("webUrl");


                JSONArray reference = jsonObject.getJSONArray("tags");
                String author = "";
                if(reference.length() == 0){
                    author = null;
                }else{
                    for(int p = 0; p<reference.length(); p++ ){
                        JSONObject jsonObject1 = reference.getJSONObject(p);
                        author = jsonObject1.getString("webTitle");
                    }

                }
                News newsList = new News(title, section, author, date, url);
                news.add(newsList);
            }
        }catch(JSONException e){
            Log.e(LOG_TAG, "Problem parsing json", e);

        }
        return news;
    }
    private static URL createURL(String newsUrl){
        URL url = null;
        try{
            url = new URL(newsUrl);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG, "Problem building url", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException{
        String json = "";
        if(url == null){
            return json;
        }
        HttpURLConnection newsConnection = null;
        InputStream inputStream = null;
        try{
            newsConnection = (HttpURLConnection) url.openConnection();
            newsConnection.setRequestMethod("GET");
            newsConnection.setReadTimeout(10000);
            newsConnection.setConnectTimeout(15000);
            newsConnection.connect();
            if (newsConnection.getResponseCode() == 200) {
                inputStream = newsConnection.getInputStream();
                json = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + newsConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        }finally {
            if (newsConnection != null){
                newsConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return json;
    }
    private static String readFromStream(InputStream inputStream)throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
