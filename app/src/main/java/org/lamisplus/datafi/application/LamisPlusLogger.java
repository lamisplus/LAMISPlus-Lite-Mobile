package org.lamisplus.datafi.application;

import android.util.Log;

public class LamisPlusLogger {

    private static String mTAG = "LamisPlus";
    private static LamisPlus mLamisPlus = LamisPlus.getInstance();
    private static final boolean IS_DEBUGGING_ON = true;
    private static LamisPlusLogger logger = null;

    public LamisPlusLogger(){
        logger = this;
    }

    public void w(final String msg) {
        Log.w(mTAG, getMessage(msg));
        saveToFile();
    }

    private static String getMessage(String msg) {
        final String fullClassName = Thread.currentThread().getStackTrace()[4].getClassName();
        final String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        final String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
        final int lineNumber = Thread.currentThread().getStackTrace()[4].getLineNumber();

        return "#" + lineNumber + " " + className + "." + methodName + "() : " + msg;
    }

    public void v(final String msg) {
        Log.v(mTAG, getMessage(msg));
        saveToFile();
    }

    public void v(final String msg, Throwable tr) {
        Log.v(mTAG, getMessage(msg), tr);
        saveToFile();
    }

    public void d(final String msg) {
        if (IS_DEBUGGING_ON) {
            Log.d(mTAG, getMessage(msg));
            saveToFile();
        }
    }

    public void d(final String msg, Throwable tr) {
        if (IS_DEBUGGING_ON) {
            Log.d(mTAG, getMessage(msg), tr);
            saveToFile();
        }
    }

    public void i(final String msg) {
        Log.i(mTAG, getMessage(msg));
        saveToFile();
    }

    public void i(final String msg, Throwable tr) {
        Log.i(mTAG, getMessage(msg), tr);
        saveToFile();
    }

    public void w(final String msg, Throwable tr) {
        Log.w(mTAG, getMessage(msg), tr);
        saveToFile();
    }

    public void e(final String msg) {
        Log.e(mTAG, getMessage(msg));
        saveToFile();
    }

    public void e(final String msg, Throwable tr) {
        Log.e(mTAG, getMessage(msg), tr);
        saveToFile();
    }

    private static void saveToFile() {
        Log.v(mTAG, "File Saved");
//        SaveToFileAsyncTask asyncTask = new SaveToFileAsyncTask();
//        asyncTask.execute();
    }

}
