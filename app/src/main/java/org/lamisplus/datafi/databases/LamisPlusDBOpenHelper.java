package org.lamisplus.datafi.databases;

import android.util.Log;

import org.lamisplus.datafi.application.LamisPlus;

public class LamisPlusDBOpenHelper {

    private static LamisPlusDBOpenHelper sInstance;

    private final DBOpenHelper mDBOpenHelper;

    public LamisPlusDBOpenHelper() {
        mDBOpenHelper = new DBOpenHelper(LamisPlus.getInstance());
    }

    public static void init() {
        if (null == sInstance) {
            sInstance = new LamisPlusDBOpenHelper();
        }
    }

    public static LamisPlusDBOpenHelper getInstance() {
        if (null == sInstance) {
            init();
        }
        return sInstance;
    }

    public void closeDatabases() {
        mDBOpenHelper.close();
    }

    public DBOpenHelper getDBOpenHelper() {
        return mDBOpenHelper;
    }

}
