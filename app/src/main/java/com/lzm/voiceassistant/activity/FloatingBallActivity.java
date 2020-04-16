package com.lzm.voiceassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lzm.voiceassistant.util.CommonConfig;
import com.lzm.voiceassistant.view.FloatingBallView;
import com.lzm.voiceassistant.OnTouch;
import com.lzm.voiceassistant.R;

public class FloatingBallActivity extends AppCompatActivity {

    FloatingBallView floatingBallView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonConfig.init(getApplication());
        setContentView(R.layout.activity_main);
        context = this;
        floatingBallView = findViewById(R.id.a);
        floatingBallView.setOnTouch(new OnTouch() {
            @Override
            public void onTouch() {
                startActivity(new Intent(context, VoiceAssistantActivity.class));
            }
        });
    }
}
