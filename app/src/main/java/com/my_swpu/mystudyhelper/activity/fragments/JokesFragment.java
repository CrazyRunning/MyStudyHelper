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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.adapter.JokeListAdapter;
import com.my_swpu.mystudyhelper.biz.JokeBiz;
import com.my_swpu.mystudyhelper.entity.TextJokeEntity;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.view.widget.XListView;

import java.util.List;

/**
 * Created by dsx on 2016/1/19 0019.
 */
public class JokesFragment extends BaseFragment {
    private XListView xLvJokes;
    private JokeListAdapter jokeListAdapter;
    private  int page = 1;
    private List<TextJokeEntity> jokeList;
    private LinearLayout ll_joke_content;
    private TextView tvJokeTitle, tvJokeContent;
    private JokeReceiver receiver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jokesfragment, null);
        xLvJokes = (XListView) view.findViewById(R.id.xLvJokes);
        ll_joke_content = (LinearLayout) view.findViewById(R.id.ll_joke_content);
        tvJokeTitle = (TextView) view.findViewById(R.id.tvJokeTitle);
        tvJokeContent = (TextView) view.findViewById(R.id.tvJokeContent);
        ll_joke_content.setVisibility(View.GONE);
        xLvJokes.setPullRefreshEnable(true);
        xLvJokes.setPullLoadEnable(true);
        xLvJokes.setXListViewListener(this);
        xLvJokes.setOnItemClickListener(this);
        ll_joke_content.setOnClickListener(this);
        xLvJokes.setRefreshTime(getTime());
        handler = new Handler();
        DialogUtil.showLoading(getActivity(), Const.GET_JOKES_ACTION);
        JokeBiz.getJokeList(false, page);
        xLvJokes.setAdapter(jokeListAdapter);
        receiver = new JokeReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter(Const.ACTION_GET_JOKE));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ll_joke_content){
            ll_joke_content.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                JokeBiz.getJokeList(true, page);
            }
        }, 1500);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        if(jokeList.size() < 100) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    page++;
                    JokeBiz.getJokeList(false, page);
                }
            }, 1500);
        }else{
            DialogUtil.showToast(getActivity(), "已达到最大条数", 1);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        tvJokeTitle.setText(jokeList.get(position - 1).getTitle());
        tvJokeContent.setText(jokeList.get(position - 1).getContent());
        ll_joke_content.setVisibility(View.VISIBLE);
    }

    class JokeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DialogUtil.dissMissLoading(Const.GET_JOKES_ACTION);
            if(jokeListAdapter == null) {
                jokeList = (List<TextJokeEntity>) intent.getSerializableExtra("jokeList");
                jokeListAdapter = new JokeListAdapter(jokeList, getActivity());
                xLvJokes.setAdapter(jokeListAdapter);
            }else {
                boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
                if (isRefresh) {
                    jokeList = (List<TextJokeEntity>) intent.getSerializableExtra("jokeList");
                    jokeListAdapter.setJokeList(jokeList);
                    jokeListAdapter.notifyDataSetChanged();
                    xLvJokes.stopRefresh();
                } else {
                    jokeList.addAll((List<TextJokeEntity>) intent.getSerializableExtra("jokeList"));
                    jokeListAdapter.setJokeList(jokeList);
                    jokeListAdapter.notifyDataSetChanged();
                    xLvJokes.stopLoadMore();
                }
            }

        }
    }
}
