package com.lzm.voiceassistant.action;

import android.view.View;
import android.webkit.WebView;

import com.lzm.voiceassistant.activity.VoiceAssistantActivity;
import com.lzm.voiceassistant.bean.AssistantResponse;
import com.lzm.voiceassistant.bean.WebPageBean;

/**
 * @author Morris
 * @email moon.liuzhimi@gmail.com
 * @date 2020-04-13 01:30
 */
public class ShowWebAction extends Action{

    @Override
    public void action(VoiceAssistantActivity voiceAssistantActivity) {
        voiceAssistantActivity.showWebView();
    }
}
