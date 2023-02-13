package org.lamisplus.datafi.application;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.utilities.ToastUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LamisCustomFileHandler {

    public static String folderName = "LAMISPlusLiteLog";
    private static String logFile = "lamispluslite_log_";
    private static String crashFile = "lamispluslite_crash_log_";

    public static void showDialogMessage(Context context, String message){
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

    public static void showJson(Object object){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()// STATIC|TRANSIENT in the default configuration
                .create();
        String values = gson.toJson(object);
    }

    public static String showJson(Object object, String passNovalue){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()// STATIC|TRANSIENT in the default configuration
                .create();
        String values = gson.toJson(object);
        return  values;
    }

    public static File createFolder(){
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + LamisCustomFileHandler.folderName);

        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public static void writeLogToFile(String message){
        createFolder();
        String currentTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            String fileNaming = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
            File gpxfile = new File(createFolder(), LamisCustomFileHandler.logFile + fileNaming + ".txt");
            FileWriter writer = new FileWriter(gpxfile, true);
            BufferedWriter buf = new BufferedWriter(writer);
            buf.append(currentTime + ": " + message);
            buf.newLine();
            buf.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void writeCrashToFile(String message){
        String currentTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            String fileNaming = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
            File gpxfile = new File(createFolder(), LamisCustomFileHandler.crashFile + fileNaming + ".txt");
            FileWriter writer = new FileWriter(gpxfile, true);
            BufferedWriter buf = new BufferedWriter(writer);
            buf.append(currentTime + ": " + message);
            buf.newLine();
            buf.flush();
            buf.close();
        } catch (Exception e){
            ToastUtil.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
