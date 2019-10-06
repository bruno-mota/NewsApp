package com.example.bruno.newsapp;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.bruno.newsapp.NewsActivity.LOG_TAG;

/**
 * Created by Bruno on 1/3/2018.
 */
public class NewsAdapter extends ArrayAdapter<News>{
    public NewsAdapter(Context context, ArrayList<News> news){
        super(context, 0, news);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_news, parent, false);
        }
        News currentNews = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(currentNews.getAuthor());
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews.getSection());
        String date = currentNews.getDate();
        TextView dateView = (TextView)listItemView.findViewById(R.id.date);
        String formattedDate = formateDate(date);
        dateView.setText(formattedDate);
        return listItemView;
    }
    private String formateDate(String date){
        String jsonDate = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat dateFormat = new SimpleDateFormat(jsonDate);
        try {
            Date parsedDate = dateFormat.parse(date);
            String datePattern = "MM dd, yyyy";
            SimpleDateFormat dateFormatted = new SimpleDateFormat(datePattern);
            return dateFormatted.format(parsedDate);
        }catch(ParseException e){
            Log.e(LOG_TAG, "problem parsing date", e);
            return "";
        }


    }
}
