package com.lzm.voiceassistant.action;

import com.lzm.voiceassistant.activity.VoiceAssistantActivity;

/**
 * @author Morris
 * @email moon.liuzhimi@gmail.com
 * @date 2020-04-16 19:52
 */
public class CommunicationAction extends Action {
    @Override
    public void action(VoiceAssistantActivity voiceAssistantActivity) {
        voiceAssistantActivity.wake();
    }
}
