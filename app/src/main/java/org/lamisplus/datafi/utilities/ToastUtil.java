package org.lamisplus.datafi.utilities;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.lamisplus.datafi.R;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.application.LamisPlusLogger;

import java.util.ArrayList;
import java.util.List;

public final class ToastUtil {

    private static LamisPlusLogger logger = LamisPlus.getInstance().getLamisPlusLogger();

    private static List<ToastThread> toastQueue = new ArrayList<>();
    private static boolean isAppVisible = true;

    private ToastUtil() {
    }

    public static void setAppVisible(boolean appVisible) {
        isAppVisible = appVisible;
    }

    public enum ToastType {
        ERROR, NOTICE, SUCCESS, WARNING
    }

    public static void notifyLong(String message) {
        showToast(LamisPlus.getInstance(), ToastType.NOTICE, message, Toast.LENGTH_LONG);
    }

    public static void notify(String message) {
        showToast(LamisPlus.getInstance(), ToastType.NOTICE, message, Toast.LENGTH_SHORT);
    }

    public static void success(String message) {
        showToast(LamisPlus.getInstance(), ToastType.SUCCESS, message, Toast.LENGTH_SHORT);
    }

    public static void error(String message) {
        showToast(LamisPlus.getInstance(), ToastType.ERROR, message, Toast.LENGTH_SHORT);
    }

    public static void warning(String message) {
        showToast(LamisPlus.getInstance(), ToastType.WARNING, message, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, ToastType type, int textId) {
        showToast(context, type, context.getResources().getString(textId), Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, ToastType type, int textId) {
        showToast(context, type, context.getResources().getString(textId), Toast.LENGTH_LONG);
    }

    public static void showShortToast(Context context, ToastType type, String text) {
        showToast(context, type, text, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, ToastType type, String text) {
        showToast(context, type, text, Toast.LENGTH_LONG);
    }

    private static void showToast(Context context, ToastType type,
                                  String text, final int duration) {
        if (!isAppVisible)
            return;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastRoot = inflater.inflate(R.layout.toast, null);

        Bitmap bitmap;
        ImageView toastImage = toastRoot.findViewById(R.id.toastImage);
        TextView toastText = toastRoot.findViewById(R.id.toastText);
        toastText.setText(text);

        bitmap = ImageUtils.decodeBitmapFromResource(
                context.getResources(),
                getImageResId(type),
                toastImage.getLayoutParams().width,
                toastImage.getLayoutParams().height);
        toastImage.setImageBitmap(bitmap);

        logger.d("Decode bitmap: " + bitmap.toString());
        Toast toast = new Toast(context);

        toast.setView(toastRoot);
        toast.setDuration(duration);
        toast.show();

        ToastThread thread = new ToastThread(bitmap, duration);
        if (toastQueue.size() == 0) {
            thread.start();
        }
        toastQueue.add(thread);
    }

    private static int getImageResId(ToastType type) {
        int toastTypeImageId = 0;

        switch (type) {
            case ERROR:
                toastTypeImageId = R.drawable.toast_error;
                break;
            case NOTICE:
                toastTypeImageId = R.drawable.toast_notice;
                break;
            case SUCCESS:
                toastTypeImageId = R.drawable.toast_success;
                break;
            case WARNING:
                toastTypeImageId = R.drawable.toast_warning;
                break;
            default:
                break;
        }

        return toastTypeImageId;
    }

    private static class ToastThread extends Thread {
        private Bitmap mBitmap;
        private int mDuration;

        public ToastThread(Bitmap bitmap, int duration) {
            mBitmap = bitmap;
            mDuration = duration;
        }

        @Override
        public void run() {
            try {
                if (mDuration == Toast.LENGTH_SHORT) {
                    Thread.sleep(2000);
                } else {
                    Thread.sleep(3500);
                }
                toastQueue.remove(0);
                if (toastQueue.size() > 0) {
                    toastQueue.get(0).run();
                }
            } catch (Exception e) {
                logger.e(e.toString());
            }
        }
    }
}
