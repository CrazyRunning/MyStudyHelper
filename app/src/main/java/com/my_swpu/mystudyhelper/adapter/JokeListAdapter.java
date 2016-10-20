package com.my_swpu.mystudyhelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.entity.TextJokeEntity;

import java.util.List;

/**
 * Created by dsx on 2016/1/20 0020.
 */
public class JokeListAdapter extends BaseAdapter {

    private List<TextJokeEntity> jokeList;
    private Context context;

    public JokeListAdapter(List<TextJokeEntity> jokeList, Context context){
        this.jokeList = jokeList;
        this.context = context;
    }

    public List<TextJokeEntity> getJokeList() {
        return jokeList;
    }

    public void setJokeList(List<TextJokeEntity> jokeList) {
        this.jokeList = jokeList;
    }

    @Override
    public int getCount() {
        return jokeList.size();
    }

    @Override
    public Object getItem(int position) {
        return jokeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.joke_list, null);
            holder = new ViewHolder();
            holder.joke_list_item_date = (TextView) view.findViewById(R.id.joke_list_item_date);
            holder.joke_list_item_title = (TextView) view.findViewById(R.id.joke_list_item_title);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.joke_list_item_date.setText(jokeList.get(position).getDate());
        holder.joke_list_item_title.setText(jokeList.get(position).getTitle());
        return view;
    }

    class ViewHolder {
        TextView joke_list_item_date, joke_list_item_title;
    }
}
