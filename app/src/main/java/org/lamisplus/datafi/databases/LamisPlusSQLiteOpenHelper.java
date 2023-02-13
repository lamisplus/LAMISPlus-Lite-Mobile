package org.lamisplus.datafi.databases;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteStatement;

import net.sqlcipher.database.SQLiteDatabaseHook;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.application.LamisPlusLogger;

public abstract class LamisPlusSQLiteOpenHelper extends SQLiteOpenHelper {

    protected LamisPlusLogger mLogger = LamisPlus.getInstance().getLamisPlusLogger();
    public static final String DATABASE_NAME = LamisPlus.getInstance().getString(R.string.dbname);

    private String mSecretKey;


    public LamisPlusSQLiteOpenHelper(Context context, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, version, new LamisPlusDefaultDBhook());
    }

    public String getSecretKey() {
        return mSecretKey != null ? mSecretKey : LamisPlus.getInstance().getSecretKey();
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db;

        try {
            db = getWritableDatabase(getSecretKey());
        } catch (SQLiteException e) {
            db = openDatabaseWithoutSecretKey(true);
        }
        return db;
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db;
        try {
            db = getReadableDatabase(getSecretKey());
        } catch (SQLiteException e) {
            db = openDatabaseWithoutSecretKey(false);
        }
        return db;
    }

    private SQLiteDatabase openDatabaseWithoutSecretKey(boolean writable) {
        SQLiteDatabase db;
        mLogger.w("Can't open database with secret key. Trying to open without key (may be not encrypted).");
        if (writable) {
            db = getWritableDatabase("");
        } else {
            db = getReadableDatabase("");
        }
        mLogger.w("Database opened but is not encrypted!");
        return db;
    }

    public static class LamisPlusDefaultDBhook implements SQLiteDatabaseHook{

        @Override
        public void preKey(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("PRAGMA cipher_default_kdf_iter = '4000'");
        }

        @Override
        public void postKey(SQLiteDatabase database) {

        }

    }
}
