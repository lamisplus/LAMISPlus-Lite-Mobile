package org.lamisplus.datafi.databases;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.application.LamisPlus;

public class DBOpenHelper extends LamisPlusSQLiteOpenHelper {

    private static int DATABASE_VERSION = LamisPlus.getInstance().getResources().getInteger(R.integer.dbversion);

    public DBOpenHelper(Context context) {
        super(context, null, DATABASE_VERSION);
        SQLiteDatabase dbs = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int currentVersion, int newVersion) {
        Log.v("Baron", "Upgrading database from " + currentVersion + " to " + newVersion);
        LamisCustomFileHandler.writeLogToFile("I have upgraded to " + currentVersion);
        if (currentVersion == 7) {
            LamisCustomFileHandler.writeLogToFile("Starting call, I have upgraded to " + currentVersion);
            //db.execSQL("ALTER TABLE person ADD personUuId TEXT");
            db.execSQL("ALTER TABLE person ADD biometricStatus INTEGER");
            db.execSQL("ALTER TABLE person ADD dateTime TEXT");
            db.execSQL("ALTER TABLE person ADD lastEditDateTime TEXT");
            db.execSQL("ALTER TABLE encounter ADD dateTime TEXT");
            db.execSQL("ALTER TABLE encounter ADD lastEditDateTime TEXT");
            db.execSQL("ALTER TABLE biometrics ADD dateTime TEXT");
            db.execSQL("ALTER TABLE biometricsrecapture ADD dateTime TEXT");
            LamisCustomFileHandler.writeLogToFile("Ended call, I have upgraded to " + currentVersion);
//                if (!checkColumnExists(db)) {
//                    db.execSQL("ALTER TABLE person ADD personUuId TEXT");
//                }
        }
    }

    private boolean checkColumnExists(SQLiteDatabase db) {
        String query = "SELECT COUNT(*) AS count FROM pragma_table_info('person') WHERE name='personUuId'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    return true;
                }
            }
            return false;
        }
    }
}
