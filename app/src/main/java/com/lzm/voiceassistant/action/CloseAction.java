package com.lzm.voiceassistant.action;

import com.lzm.voiceassistant.activity.VoiceAssistantActivity;

/**
 * @author Morris
 * @email moon.liuzhimi@gmail.com
 * @date 2020-04-16 19:48
 */
public class CloseAction extends Action {
    @Override
    public void action(VoiceAssistantActivity voiceAssistantActivity) {
        voiceAssistantActivity.closeView();
    }
}
