package com.lzm.voiceassistant.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lzm.voiceassistant.VoiceAssistantSwitch;
import com.lzm.voiceassistant.util.DisplayUtil;
import com.lzm.voiceassistant.OnTouch;

public class FloatingBallView extends FloatingActionButton implements VoiceAssistantSwitch {

    private static final String TAG = "Morris";

    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;
    private Context context;

    public FloatingBallView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FloatingBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FloatingBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        screenWidth = DisplayUtil.getScreenW(context);
        screenWidthHalf = screenWidth / 2;
        screenHeight = DisplayUtil.getScreenH(context);
        statusHeight = DisplayUtil.getStatusBarHeight();
        setAlpha(0.2f);
    }

    private int lastX;
    private int lastY;

    private boolean isDrag;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setAlpha(0.2f);
        }
    };

    private OnTouch onTouch;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        Log.i("Morris", "onTouchEvent: " + rawX + " " + rawY);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: ACTION_DOWN " + isDrag);
                if (handler.hasCallbacks(runnable)) {
                    handler.removeCallbacks(runnable);
                }
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: ACTION_MOVE ");
                if (handler.hasCallbacks(runnable)) {
                    handler.removeCallbacks(runnable);
                }
                isDrag = true;
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些华为手机无法触发点击事件的问题
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                Log.i(TAG, "onTouchEvent: alpha " + getAlpha());
                if (distance == 0 || getAlpha() < 1f) {
                    isDrag = false;
                    return false;
                }
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                y = y < statusHeight ? statusHeight : y + getHeight() > screenHeight ? screenHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                //Log.i("getX="+getX()+";getY="+getY()+";screenHeight="+screenHeight);
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: ACTION_UP " + isDrag);
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                    Log.i("Morris", "getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf);
                    if (rawX >= screenWidthHalf) {
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(screenWidth - getWidth() - getX())
                                .start();
                    } else {
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(500);
                        oa.start();
                    }
                } else {
                    if (getAlpha() >= 1f) {
                        onTouch.onTouch();
                        // TODO: 这里需要判空操作
                    } else {
                        setAlpha(1f);
                    }
                }
                handler.postDelayed(runnable, 2000);
                break;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return true || super.onTouchEvent(event);
    }

    public void setOnTouch(OnTouch onTouch) {
        this.onTouch = onTouch;
    }

    @Override
    public boolean getSwitchStatus() {
        // TODO: 获取开关状态
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setFloatingBallVisibility(boolean flag) {
        if (flag) {
            this.setVisibility(VISIBLE);
        } else {
            this.setVisibility(GONE);
        }
    }
}
