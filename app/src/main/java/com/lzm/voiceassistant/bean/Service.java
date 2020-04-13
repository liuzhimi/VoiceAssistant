package com.lzm.voiceassistant.bean;

public enum Service {

    SETTING_XXX(2, "设置xx"),
    DISPLAY_XXX(1, "展示xx"),
    OPEN_XXX(0, "打开xx"); //

    private int value;
    private String name;

    Service(int value, String name) {
        this.value = value;
        this.name = name;
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

    public static Service valueOf(int value) {
        switch (value) {
            case 2:
                return SETTING_XXX;
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
