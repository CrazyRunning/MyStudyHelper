package com.my_swpu.mystudyhelper.activity.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lidroid.xutils.BitmapUtils;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.NewsInfoActivity;
import com.my_swpu.mystudyhelper.adapter.NewsListAdapter;
import com.my_swpu.mystudyhelper.biz.NewsBiz;
import com.my_swpu.mystudyhelper.entity.NewsEntity;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.view.widget.XListView;

import java.util.List;

/**
 * Created by dsx on 2016/1/19 0019.
 */
public class NewsFragment extends BaseFragment {
    private XListView xLvNews;
    private NewsListAdapter newsListAdapter;
    private static List<NewsEntity> newsList;
    private BitmapUtils bitmap;
    private NewsReceiver receiver;
    private int page = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsfragment, null);
        xLvNews = (XListView) view.findViewById(R.id.xLvNews);
        bitmap = new BitmapUtils(getActivity());
        handler = new Handler();
        xLvNews.setPullRefreshEnable(true);
        xLvNews.setPullLoadEnable(true);
        xLvNews.setXListViewListener(this);
        xLvNews.setOnItemClickListener(this);
        xLvNews.setRefreshTime(getTime());
        DialogUtil.showLoading(getActivity(), Const.GET_NEWS_ACTION);
        NewsBiz.getNewsList(false, page);
        xLvNews.setAdapter(newsListAdapter);
        receiver = new NewsReceiver();
        getActivity().registerReceiver(receiver,
                new IntentFilter(Const.ACTION_GET_NEWS));
        return view;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
        newsList.clear();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        page = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NewsBiz.getNewsList(true, page);
            }
        }, 1500);

    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        if (newsList.size() < 100) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    page++;
                    NewsBiz.getNewsList(false, page);

                }
            }, 1500);
        } else {
            DialogUtil.showToast(getActivity(), "已达到最大条数", 1);
            xLvNews.stopLoadMore();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        DialogUtil.showToast(getActivity(), position + "", 0);
        NewsInfoActivity.launch(getActivity(), newsList.get(position - 1).getLink());
    }


    class NewsReceiver extends BroadcastReceiver {

        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            DialogUtil.dissMissLoading(Const.GET_NEWS_ACTION);
            if (newsListAdapter == null) {
                newsList = (List<NewsEntity>) intent
                        .getSerializableExtra("newsList");
                newsListAdapter = new NewsListAdapter(getActivity(), newsList, bitmap);
                xLvNews.setAdapter(newsListAdapter);
            } else {
                boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
                if (isRefresh) {
                    // 刷新界面
                    newsList = (List<NewsEntity>) intent
                            .getSerializableExtra("newsList");
                    newsListAdapter.setNewsList(newsList);
                    newsListAdapter.notifyDataSetChanged();
                    xLvNews.stopRefresh();

                } else {
                    newsList.addAll((List<NewsEntity>) intent
                            .getSerializableExtra("newsList"));
                    newsListAdapter.addNewsList(newsList);
                    newsListAdapter.notifyDataSetChanged();
                    xLvNews.stopLoadMore();

                }
            }
        }

    }


}
