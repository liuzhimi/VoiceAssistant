package com.lzm.voiceassistant.action;

import com.lzm.voiceassistant.activity.VoiceAssistantActivity;

/**
 * @author Morris
 * @email moon.liuzhimi@gmail.com
 * @date 2020-04-16 19:45
 */
public class ShowListAction extends Action {
    @Override
    public void action(VoiceAssistantActivity voiceAssistantActivity) {
        voiceAssistantActivity.showRecyclerView();
    }
}
