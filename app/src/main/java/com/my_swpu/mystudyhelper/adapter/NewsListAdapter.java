package com.my_swpu.mystudyhelper.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.entity.NewsEntity;
import com.my_swpu.mystudyhelper.parser.NewsParse;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter {

	private List<NewsEntity> newsList;
	private Context context;
	private BitmapUtils bitmap;
	private NewsParse newsParse = new NewsParse();

	public NewsListAdapter(Context context, List<NewsEntity> newsList,
			BitmapUtils bitmap) {
		this.context = context;
		this.newsList = newsList;
		this.bitmap = bitmap;
	}

	public List<NewsEntity> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<NewsEntity> newsList) {
		this.newsList = newsList;
	}

	public void addNewsList(List<NewsEntity> newsList){
		this.newsList.addAll(newsList);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("static-access")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(context)
					.inflate(R.layout.new_list, null);
			holder = new ViewHolder();
			holder.img = (ImageView) view.findViewById(R.id.imgNewsList);
			holder.tvTitle = (TextView) view.findViewById(R.id.tvNewsListTitle);
			holder.tvTime = (TextView) view.findViewById(R.id.tvNewsListTime);
			holder.tvChannelName = (TextView) view
					.findViewById(R.id.tvChannelName);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tvTitle.setText(newsList.get(position).getTitle());
		holder.tvTime.setText(newsList.get(position).getTime());
		holder.tvChannelName.setText(newsList.get(position).getChannelName());


		if(newsList.get(position).isHasImg()) {
			bitmap.display(holder.img, newsList.get(position).getImageUrl()
					.get(0));
		}else{
			holder.img.setVisibility(View.GONE);
		}
		return view;
	}

	static class ViewHolder {
		ImageView img;
		TextView tvTitle;
		TextView tvTime;
		TextView tvChannelName;
	}

}
