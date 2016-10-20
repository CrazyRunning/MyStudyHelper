package com.my_swpu.mystudyhelper.activity.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.my_swpu.mystudyhelper.view.widget.XListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/1/19 0019.
 */
public class BaseFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener,
        View.OnClickListener{

    public Handler handler;



    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    public void onClick(View v) {

    }
}
