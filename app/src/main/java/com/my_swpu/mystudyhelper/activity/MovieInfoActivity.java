package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.entity.MovieEntity;
import com.my_swpu.mystudyhelper.util.CommonUtils;

public class MovieInfoActivity extends BaseActivity {

    private FloatingActionButton floatingActionButton;
    private TextView tvRating, tvRatingNum, tvType, tvLocattion,
            tvLong, tvStars, tvDesc;
    private ImageView imgPost;
    private BitmapUtils bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabBackOnMovies);
//        tvTitle = (TextView) findViewById(R.id.tvMovieTitle);
        tvRating = (TextView) findViewById(R.id.tvRating);
        tvRatingNum = (TextView) findViewById(R.id.tvRatingNum);
        tvType = (TextView) findViewById(R.id.tvType);
        tvLocattion = (TextView) findViewById(R.id.tvLocation);
        tvLong = (TextView) findViewById(R.id.tvLong);
        imgPost = (ImageView) findViewById(R.id.imgPost);
        tvStars = (TextView) findViewById(R.id.tvStarts);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        bitmap = new BitmapUtils(this);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        floatingActionButton.setOnClickListener(this);
        tvRating.setText("评分：" + getIntent().getStringExtra("rating") + "分");
        tvRatingNum.setText("评分人数：" + "1000");
        tvType.setText("电影类型：" + getIntent().getStringExtra("type"));
        tvLocattion.setText("上映地点：" + getIntent().getStringExtra("nation"));
        tvLong.setText("电影时长：" + getIntent().getStringExtra("length") + "分钟");
        bitmap.display(imgPost, getIntent().getStringExtra("imgPost"));
        tvStars.setText("导演：" + getIntent().getStringExtra("director") + "\n"
                + "主演：" + getIntent().getStringExtra("starring"));
        tvDesc.setText("电影简介：" + getIntent().getStringExtra("desc"));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(clickedViewId == R.id.fabBackOnMovies){
//            onBackPressed();
            finish();
        }
    }

    public static void launch(Activity activity, String rate, String location, String type,
                               String length, String director, String desc, String imgPost,
                              String starring) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, MovieInfoActivity.class)) {
            Intent intent = new Intent(activity, MovieInfoActivity.class);
            intent.putExtra("rating", rate);
            intent.putExtra("nation", location);
            intent.putExtra("type", type);
            intent.putExtra("length", length);
            intent.putExtra("starring", starring);
            intent.putExtra("director", director);
            intent.putExtra("desc", desc);
            intent.putExtra("imgPost", imgPost);
            activity.startActivity(intent);
        }
    }

}
