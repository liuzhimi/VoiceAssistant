package com.lzm.voiceassistant.bean;

import com.lzm.voiceassistant.action.Action;
import com.lzm.voiceassistant.action.CloseAction;
import com.lzm.voiceassistant.action.CommunicationAction;
import com.lzm.voiceassistant.action.ShowListAction;
import com.lzm.voiceassistant.action.ShowWebAction;

public enum Service {

    COMMUNICATION(3, "对话", new CommunicationAction()),
    CLOSE_XXX(2, "关闭xx", new CloseAction()),
    DISPLAY_XXX(1, "展示xx", new ShowListAction()),
    OPEN_XXX(0, "打开xx", new ShowWebAction()); //

    private int value;
    private String name;
    private Action action;

    Service(int value, String name, Action action) {
        this.value = value;
        this.name = name;
        this.action = action;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public static Service valueOf(int value) {
        switch (value) {
            case 3:
                return COMMUNICATION;
            case 2:
                return CLOSE_XXX;
            case 1:
                return DISPLAY_XXX;
            case 0:
                return OPEN_XXX;
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
