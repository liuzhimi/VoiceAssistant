package com.lzm.voiceassistant.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @author Morris
 * @email moon.liuzhimi@gmail.com
 * @date 2020-04-07 16:46
 */
public class DisplayUtil {

    public static final int L_DPI = 11;
    public static final int M_DPI = 12;
    public static final int H_DPI = 13;
    public static final int XH_DPI = 14;
    public static final int XXH_DPI = 15;

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dip2px(float dipValue) {
        return dip2px(CommonConfig.getContext(), dipValue);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        return width;

    }


    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static int getViewWidth(View view) {
        measureView(view);
        return view.getMeasuredWidth();
    }

    public static int getViewHeight(View view) {
        measureView(view);
        return view.getMeasuredHeight();
    }

    private static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public static int getDensityType(Context context) {
        // densityDpi = 120dpi is ldpi, densityDpi = 160dpi is mdpi,
        // densityDpi = 240dpi is hdpi, densityDpi = 320dpi is xhdpi
        float densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        int type = XH_DPI;
        if (densityDpi > DisplayMetrics.DENSITY_XHIGH) {
            type = XXH_DPI;
        } else if (densityDpi <= DisplayMetrics.DENSITY_XHIGH && densityDpi > DisplayMetrics.DENSITY_HIGH) {
            type = XH_DPI;
        } else if (densityDpi <= DisplayMetrics.DENSITY_HIGH && densityDpi > DisplayMetrics.DENSITY_MEDIUM) {
            type = H_DPI;
        } else if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM && densityDpi > DisplayMetrics.DENSITY_LOW) {
            type = M_DPI;
        } else if (densityDpi <= DisplayMetrics.DENSITY_LOW) {
            type = L_DPI;
        }
        return type;
    }


    public static boolean isNavigationBarShow(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = windowManager.getDefaultDisplay();
            if (null == display) {
                return false;
            }
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    //判断屏幕下方是否有虚拟按键
    private static boolean checkDeviceHasNavigationBar(Context context) {
        //Android 5.0以下没有虚拟按键
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    //获取NavigationBar的高度：
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    public static int getStatusBarHeight() {
        int height = 0;
        int resourceId = CommonConfig.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = CommonConfig.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    private static DisplayMetrics dm = null;

    public static DisplayMetrics displayMetrics(Context context) {
        if (null != dm) {
            return dm;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }


    public static int getScreenW(Context c) {
        return c.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenH(Context c) {
        return c.getResources().getDisplayMetrics().heightPixels;
    }

}
