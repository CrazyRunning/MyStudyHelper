package com.my_swpu.mystudyhelper.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.entity.MovieEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter {

	private List<MovieEntity> movieList;
	private BitmapUtils bitmap;
	private Context context;

	public MovieAdapter(List<MovieEntity> movieList, BitmapUtils bitmap,
			Context context) {
		this.movieList = movieList;
		this.bitmap = bitmap;
		this.context = context;
	}

	public List<MovieEntity> getMovieList() {
		return movieList;
	}

	public void setMovieList(List<MovieEntity> movieList) {
		this.movieList = movieList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return movieList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return movieList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.movie_list, null);
			holder.imgMovie = (ImageView) view.findViewById(R.id.imgMovie);
			holder.tvMovieListTitle = (TextView) view.findViewById(R.id.tvMovieListTitle);
			holder.tvMovieListRB = (TextView) view.findViewById(R.id.tvMovieListRB);
			holder.tvMovieListMsg = (TextView) view.findViewById(R.id.tvMessage);
			holder.rb = (RatingBar) view.findViewById(R.id.rb);
			holder.rb.setIsIndicator(true);
			holder.rb.setMax(10);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		bitmap.display(holder.imgMovie, movieList.get(position).getMovie_picture());
		holder.tvMovieListTitle.setText(movieList.get(position).getMovie_name());
		//holder.tvMovieListRB.setText(movieList.get(position).getMovie_score() + "��");
		Log.i("desc", movieList.get(position).getMovie_msg());
		holder.tvMovieListMsg.setText(movieList.get(position).getMovie_msg());
		float score;
		try {
			score = Float.valueOf(movieList.get(position).getMovie_score());
			holder.rb.setRating(score);
			holder.tvMovieListRB.setText(movieList.get(position).getMovie_score() + "分");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			holder.tvMovieListRB.setText("暂无评分");
		}
		return view;
	}

	class ViewHolder {
		ImageView imgMovie;
		TextView tvMovieListTitle, tvMovieListRB, tvMovieListMsg;
		RatingBar rb;
	}

}
