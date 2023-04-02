package org.lamisplus.datafi.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class LamisCustomHandler {

    public static void showDialogMessage(Context context, String message) {
        AlertDialog alertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Message");
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showJson(Object object) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .excludeFieldsWithoutExposeAnnotation()// STATIC|TRANSIENT in the default configuration
                .create();
        String values = gson.toJson(object);
        Log.v("Baron", values);
    }

    public static String getJson(Object object) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()// STATIC|TRANSIENT in the default configuration
                .create();
        return gson.toJson(object);
    }

    public static void writeErrorToFile(String message) {
        File logFile = new File("lamislogfile.log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(message);
            buf.newLine();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
