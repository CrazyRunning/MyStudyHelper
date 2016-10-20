package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.biz.TranslateBiz;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;

public class TranslateActivity extends BaseActivity {

    private LinearLayout ll_back_on_translateactivity;
    private EditText etInput, etResult;
    private Spinner spTo;
//    private Button btTranslate;
    private String[] spinner;
    private ArrayAdapter<String> arrayAdapter;
    private String content = "";
    private String from = "zh";
    private String to = "zh";
    private TranslateReceiver translateReceiver;
    private String result = "";
    private RelativeLayout activity_translate;
    private TextView tv_translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translateReceiver = new TranslateReceiver();
        registerReceiver(translateReceiver, new IntentFilter(Const.TRANSLATW_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(translateReceiver);
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        ll_back_on_translateactivity = (LinearLayout) findViewById(R.id.ll_back_on_translateactivity);
        tv_translate = (TextView) findViewById(R.id.tv_translate);
        activity_translate = (RelativeLayout) findViewById(R.id.activity_translate);
        etInput = (EditText) findViewById(R.id.etInput);
        etResult = (EditText) findViewById(R.id.etResult);
        spTo = (Spinner) findViewById(R.id.spTo);
        spinner = getResources().getStringArray(R.array.spinner);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        ll_back_on_translateactivity.setOnClickListener(this);
        tv_translate.setOnClickListener(this);
        etResult.setEnabled(false);
        spTo.setAdapter(arrayAdapter);
        spTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to = (String) spTo.getSelectedItem();
                TextView v1 = (TextView)view;
                v1.setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activity_translate.setOnTouchListener(new View.OnTouchListener() {
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
        switch (clickedViewId) {
            case R.id.ll_back_on_translateactivity:
//                onBackPressed();
                finish();
                break;
//            case R.id.btTranslate:
//                content = etInput.getText().toString().trim();
//                if (validate()) {
//                    DialogUtil.showLoading(this, Const.TRANSLATE_EVENT);
//                    TranslateBiz.translate(content, from, to);
//                }
//                break;
            case R.id.tv_translate:
                content = etInput.getText().toString().trim();
                if (validate()) {
                    DialogUtil.showLoading(this, Const.TRANSLATE_EVENT);
                    TranslateBiz.translate(content, from, to);
                }
                break;
            default:
                break;
        }
    }

    private boolean validate() {
        if (!TextUtils.isEmpty(content)) {
            if (!TextUtils.isEmpty(from) && !TextUtils.isEmpty(to)) {
                getReady();
                return true;
            } else {
                DialogUtil.showToast(this, "请选择要翻译的语种", 0);
            }
        }else{
            DialogUtil.showToast(this, "请输入翻译内容", 0);
        }
        return false;
    }

    private void getReady(){
        if(from.equals("汉语")) from = "zh";
        else if(from.equals("英语")) from = "en";
        else if(from.equals("日语")) from ="jp";
        else if(from.equals("西班牙语")) from ="es";
        else if(from.equals("法语")) from ="fr";
        else if(from.equals("泰语")) from ="th";
        else if(from.equals("阿拉伯语")) from ="ar";
        else if(from.equals("俄语")) from ="ru";
        else if(from.equals("葡萄牙语")) from ="pt";
        else if(from.equals("德语")) from ="de";
        else if(from.equals("意大利语")) from ="it";
        if(to.equals("汉语")) to = "zh";
        else if(to.equals("英语")) to = "en";
        else if(to.equals("日语")) to ="jp";
        else if(to.equals("韩语")) to ="kor";
        else if(to.equals("西班牙语")) to ="spa";
        else if(to.equals("法语")) to ="fra";
        else if(to.equals("泰语")) to ="th";
        else if(to.equals("阿拉伯语")) to ="ara";
        else if(to.equals("俄语")) to ="ru";
        else if(to.equals("葡萄牙语")) to ="pt";
        else if(to.equals("德语")) to ="de";
        else if(to.equals("希腊语")) to ="el";
        else if(to.equals("荷兰语")) to ="nl";
        else if(to.equals("波兰语")) to ="pl";
        else if(to.equals("瑞典语")) to ="swe";
        else if(to.equals("文言文")) to ="wyw";
        else if(to.equals("繁体中文")) to ="cht";
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, TranslateActivity.class)) {
            Intent intent = new Intent(activity, TranslateActivity.class);
            activity.startActivity(intent);
        }
    }

    class TranslateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DialogUtil.dissMissLoading(Const.TRANSLATE_EVENT);
            result = intent.getStringExtra("result");
            if(!TextUtils.isEmpty(result)){
                etResult.setText(result);
            }else{
                DialogUtil.showToast(TranslateActivity.this, "翻译失败，请稍后重试", 1);
            }
        }
    }

}
