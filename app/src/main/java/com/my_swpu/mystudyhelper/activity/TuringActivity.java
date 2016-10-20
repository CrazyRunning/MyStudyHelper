package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.biz.TuringBiz;
import com.my_swpu.mystudyhelper.entity.TuringMessage;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;

public class TuringActivity extends BaseActivity {


    private LinearLayout ll_back_on_turingactivity, ll_turing;
    private ScrollView scrollView;
    private ImageView iv_send;
    private EditText et_send;
    private Handler handler;
    private TuringReceiver turingReceiver;
    private RelativeLayout activity_turing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        turingReceiver = new TuringReceiver();
        registerReceiver(turingReceiver, new IntentFilter(Const.TURING_MESSAGE));
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        ll_back_on_turingactivity = (LinearLayout) findViewById(R.id.ll_back_on_turingactivity);
        activity_turing = (RelativeLayout) findViewById(R.id.activity_turing);
        iv_send = (ImageView) findViewById(R.id.iv_send);
        et_send = (EditText) findViewById(R.id.et_send);
        scrollView =(ScrollView) findViewById(R.id.scrollView_chat);
        ll_turing = (LinearLayout) findViewById(R.id.ll_turing);
        sayHello();
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        ll_back_on_turingactivity.setOnClickListener(this);
        iv_send.setOnClickListener(this);
        activity_turing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (clickedViewId){
            case R.id.ll_back_on_turingactivity:
//                onBackPressed();
                finish();
                break;
            case R.id.iv_send:
                String val = et_send.getText().toString();
                if (!TextUtils.isEmpty(val)) {
                    View view = View.inflate(this, R.layout.chat_right, null);
                    TextView tv = (TextView) view.findViewById(R.id.tv_right);
                    tv.setText(val);
                    et_send.setText("");
                    ll_turing.addView(view);
                    TuringBiz.sendMessage(val);
                    Log.i("聊天内容：", val);
                }
                break;
            default:
                break;
        }
    }

    private void createView(TuringMessage m) {
        // TODO Auto-generated method stub
        View view = View.inflate(this, R.layout.chat_left, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_left);
        tv.setText(m.getText());
        ll_turing.addView(view);

    }

    private void sayHello(){
        final View view = View.inflate(this, R.layout.chat_left, null);
        final TextView tv = (TextView) view.findViewById(R.id.tv_left);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setText("亲爱的，我是你无所不知的小图");
                ll_turing.addView(view);
            }
        }, 1000);

    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, TuringActivity.class)) {
            Intent intent = new Intent(activity, TuringActivity.class);
            activity.startActivity(intent);
        }
    }

    class TuringReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            TuringMessage m = (TuringMessage) intent
                    .getSerializableExtra("message");
            createView(m);
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(turingReceiver);
    }
}
