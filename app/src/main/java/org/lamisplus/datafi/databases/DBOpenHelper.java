package org.lamisplus.datafi.databases;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.application.LamisPlus;

public class DBOpenHelper extends LamisPlusSQLiteOpenHelper{

    private static int DATABASE_VERSION = LamisPlus.getInstance().getResources().getInteger(R.integer.dbversion);

    public DBOpenHelper(Context context) {
        super(context, null, DATABASE_VERSION);
        SQLiteDatabase dbs = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
