package com.my_swpu.mystudyhelper.activity.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lidroid.xutils.BitmapUtils;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.MovieInfoActivity;
import com.my_swpu.mystudyhelper.adapter.MovieAdapter;
import com.my_swpu.mystudyhelper.biz.MovieBiz;
import com.my_swpu.mystudyhelper.entity.MovieEntity;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.view.widget.XListView;

import java.util.List;

/**
 * Created by dsx on 2016/1/19 0019.
 */
public class MoviesFragment extends BaseFragment {
    private XListView xLvMovies;
    private BitmapUtils bitmap;
    private MovieAdapter movieAdapter;
    private MovieReceiver receiver;
    private List<MovieEntity> movieList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moviesfragment, null);
        xLvMovies = (XListView) view.findViewById(R.id.xLvMovies);
        xLvMovies.setPullRefreshEnable(true);
        xLvMovies.setPullLoadEnable(true);
        xLvMovies.setXListViewListener(this);
        xLvMovies.setOnItemClickListener(this);
        xLvMovies.setRefreshTime(getTime());
        bitmap = new BitmapUtils(getActivity());
        handler = new Handler();
        DialogUtil.showLoading(getActivity(), Const.GET_MOVIES_ACTION);
        MovieBiz.getMovieList(false);
        xLvMovies.setAdapter(movieAdapter);
        receiver = new MovieReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter(Const.ACTION_GET_MOVIE));
        return view;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MovieBiz.getMovieList(true);
            }
        }, 1500);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtil.showToast(getActivity(), "已加载全部", 1);
                xLvMovies.stopLoadMore();
            }
        }, 1500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        MovieInfoActivity.launch(getActivity(), movieList.get(position - 1).getMovie_score(),
         movieList.get(position - 1).getMovie_nation(),
        movieList.get(position - 1).getMovie_type(),
        movieList.get(position - 1).getMovie_length(),
        movieList.get(position - 1).getMovie_director(),
        movieList.get(position - 1).getMovie_description(),
        movieList.get(position - 1).getMovie_picture(),
        movieList.get(position - 1).getMovie_starring());
    }

    class MovieReceiver extends BroadcastReceiver {

        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            DialogUtil.dissMissLoading(Const.GET_MOVIES_ACTION);
            if(movieAdapter == null){
                movieList = (List<MovieEntity>) intent.getSerializableExtra("movieList");
                movieAdapter = new MovieAdapter(movieList, bitmap, getActivity());
                xLvMovies.setAdapter(movieAdapter);
            }else{
                //刷新界面
                boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
                if(isRefresh){
                    movieList = (List<MovieEntity>) intent.getSerializableExtra("movieList");
                    movieAdapter.setMovieList(movieList);
                    movieAdapter.notifyDataSetChanged();
                    xLvMovies.stopRefresh();
                }else{
//                    movieList.addAll((List<MovieEntity>)intent.getSerializableExtra("movieList"));
//                    movieAdapter.notifyDataSetChanged();
                }

            }
        }

    }
}
