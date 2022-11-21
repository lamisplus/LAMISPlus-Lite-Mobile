package org.lamisplus.datafi.utilities;


import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.application.LamisPlusLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class TabUtil {
    public static final int MIN_SCREEN_WIDTH_FOR_FINDPATIENTSACTIVITY = 480;
    public static final int MIN_SCREEN_WIDTH_FOR_PATIENTDASHBOARDACTIVITY = 960;
    private static LamisPlusLogger mLogger = LamisPlus.getInstance().getLamisPlusLogger();

    private TabUtil() {
    }

    public static void setHasEmbeddedTabs(Object inActionBar, WindowManager windowManager, int minScreenWidth) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int scaledScreenWidth = (int) (displaymetrics.widthPixels / displaymetrics.density);
        boolean inHasEmbeddedTabs = scaledScreenWidth >= minScreenWidth;

        // get the ActionBar class
        Class<?> actionBarClass = inActionBar.getClass();

        if (LamisPlus.getInstance().isRunningKitKatVersionOrHigher()) {
            actionBarClass = actionBarClass.getSuperclass().getSuperclass();
        } else {
            actionBarClass = actionBarClass.getSuperclass();
        }

        Object inActionBar2 = null;
        try {
            final Field actionBarField = actionBarClass.getDeclaredField("mActionBar");
            actionBarField.setAccessible(true);
            inActionBar2 = actionBarField.get(inActionBar);
            actionBarClass = inActionBar2.getClass();
        } catch (IllegalAccessException e) {
            mLogger.d(e.toString());
        } catch (IllegalArgumentException e) {
            mLogger.d(e.toString());
        } catch (NoSuchFieldException e) {
            inActionBar2 = inActionBar;
            mLogger.d(e.toString());
        }

        try {
            final Method method = actionBarClass.getDeclaredMethod("setHasEmbeddedTabs", new Class[]{Boolean.TYPE});
            method.setAccessible(true);
            method.invoke(inActionBar2, new Object[]{inHasEmbeddedTabs});
        } catch (NoSuchMethodException e) {
            mLogger.d(e.toString());
        } catch (InvocationTargetException e) {
            mLogger.d(e.toString());
        } catch (IllegalAccessException e) {
            mLogger.d(e.toString());
        } catch (IllegalArgumentException e) {
            mLogger.d(e.toString());
        }
    }
}
