package com.lzm.voiceassistant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechRecognizer;
import com.lzm.voiceassistant.R;

public class HelpActivity extends Activity {
    private LinearLayout  ll2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help);
        ll2 = findViewById(R.id.ll);

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(HelpActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage(R.string.cando);
                dialog.setPositiveButton("确定",null);
                dialog.show();
            }
        });

    }
}
