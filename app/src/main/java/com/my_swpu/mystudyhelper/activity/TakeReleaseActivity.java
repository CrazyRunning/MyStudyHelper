package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.fragments.JokesFragment;
import com.my_swpu.mystudyhelper.activity.fragments.MoviesFragment;
import com.my_swpu.mystudyhelper.activity.fragments.NewsFragment;
import com.my_swpu.mystudyhelper.adapter.FragmentAdapter;
import com.my_swpu.mystudyhelper.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TakeReleaseActivity extends BaseActivity {

    private LinearLayout ll_news, ll_movies, ll_jokes, ll_back_on_releaseactivity;
    private ViewPager viewPager;
    private ImageView imgNews, imgMovie, imgJoke;
    private TextView tvNews, tvMovies, tvJokes, tvTitleOnRelease;
    private FragmentAdapter fragmentAdapter;
    private Vector<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        ll_news = (LinearLayout) findViewById(R.id.ll_news);
        ll_movies = (LinearLayout) findViewById(R.id.ll_movies);
        ll_jokes = (LinearLayout) findViewById(R.id.ll_jokes);
        ll_back_on_releaseactivity = (LinearLayout) findViewById(R.id.ll_back_on_releaseactivity);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imgNews = (ImageView) findViewById(R.id.imgNews);
        imgMovie = (ImageView) findViewById(R.id.imgMovie);
        imgJoke = (ImageView) findViewById(R.id.imgJoke);
        tvNews = (TextView) findViewById(R.id.tvNews);
        tvMovies = (TextView) findViewById(R.id.tvMovies);
        tvJokes = (TextView) findViewById(R.id.tvJokes);
        tvTitleOnRelease = (TextView) findViewById(R.id.tvTitleOnRelease);
        if(fragments == null) {
            fragments = getFragments();
        }
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);

    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        ll_news.setOnClickListener(this);
        ll_movies.setOnClickListener(this);
        ll_jokes.setOnClickListener(this);
        ll_back_on_releaseactivity.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private Vector<Fragment> getFragments() {
        Vector<Fragment> fragments = new Vector<>();
        fragments.add(new NewsFragment());
        fragments.add(new MoviesFragment());
        fragments.add(new JokesFragment());
        return fragments;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(clickedViewId){
            case R.id.ll_news:
                change(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_movies:
                change(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_jokes:
                change(2);
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_back_on_releaseactivity:
//                onBackPressed();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        change(position);
    }

    private void change(int position) {
        imgNews.setImageResource(R.drawable.news_no);
        imgMovie.setImageResource(R.drawable.movie_no);
        imgJoke.setImageResource(R.drawable.joke_no);
        tvNews.setTextColor(getResources().getColor(R.color.white));
        tvMovies.setTextColor(getResources().getColor(R.color.white));
        tvJokes.setTextColor(getResources().getColor(R.color.white));
        switch (position) {
            case 0:
                tvTitleOnRelease.setText("新闻");
                imgNews.setImageResource(R.drawable.news);
                tvNews.setTextColor(getResources().getColor(R.color.light_green));
                break;
            case 1:
                tvTitleOnRelease.setText("电影");
                imgMovie.setImageResource(R.drawable.movie);
                tvMovies.setTextColor(getResources().getColor(R.color.light_green));
                break;
            case 2:
                tvTitleOnRelease.setText("笑话");
                imgJoke.setImageResource(R.drawable.joke);
                tvJokes.setTextColor(getResources().getColor(R.color.light_green));
                break;
            default:
                break;
        }
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, TakeReleaseActivity.class)) {
            Intent intent = new Intent(activity, TakeReleaseActivity.class);
            activity.startActivity(intent);
        }
    }
}
