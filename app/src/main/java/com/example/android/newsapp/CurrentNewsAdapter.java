package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevincurtis on 15/07/2017.
 */

public class CurrentNewsAdapter extends ArrayAdapter<CurrentNews> {
    private final Context context;


    public CurrentNewsAdapter(Context context, List<CurrentNews> articles) {
        super(context, 0, articles);
        this.context = context;
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        // Find the article at the given position in the list of articles
        CurrentNews currentArticle = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        //reuse view if it already exists
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            //inflate new view with custom list_item layout
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.news_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        // set this text on the Title TextView
        holder.titleTextView.setText(currentArticle.getTitle());
        // set this text on the section TextView
        holder.sectionTextView.setText(currentArticle.getSection());
        return convertView;
    }

    /**
     * ViewHolder
     * binds views and fields to a variable
     * no need to use findViewById anymore
     */
    static class ViewHolder {
        // Get the title  from the current article object and
        @BindView(R.id.title_text_view)
        TextView titleTextView;
        // Get the section  from the current article object and
        @BindView(R.id.section_text_view)
        TextView sectionTextView;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}