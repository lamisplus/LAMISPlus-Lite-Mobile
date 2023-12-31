package org.lamisplus.datafi.utilities;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.application.LamisPlusLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ForceClose implements java.lang.Thread.UncaughtExceptionHandler{

    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ForceClose(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK_INT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ APP LOGS ************\n");
        errorReport.append(getLogs());
        Log.v("Baron", errorReport.toString());
        Intent i = new Intent(myContext, DashboardActivity.class); // myContext.getPackageManager().getLaunchIntentForPackage(myContext.getPackageName());
        Bundle bundle = new Bundle();
        bundle.putBoolean("flag", true);
        bundle.putString("error", errorReport.toString());
        i.putExtras(bundle);
//        i.putExtra("flag",true);
//        i.putExtra("error", errorReport.toString());
        LamisCustomFileHandler.writeCrashToFile(errorReport.toString());
        myContext.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    public String getLogs() {
        LamisPlusLogger lamisPlusLogger = new LamisPlusLogger();
        String textLogs = "";
        String filename = LamisPlus.getInstance().getLamisPlusDir()
                + File.separator + lamisPlusLogger.getLogFilename();
        try {
            File myFile = new File(filename);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow;
            while ((aDataRow = myReader.readLine()) != null) {
                textLogs += aDataRow;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textLogs;
    }

}
