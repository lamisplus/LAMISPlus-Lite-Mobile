package org.lamisplus.datafi.utilities;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import org.lamisplus.datafi.application.LamisPlus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public final class NetworkUtils {


    public static boolean hasNetwork() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) LamisPlus.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    public static boolean isOnline() {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LamisPlus.getInstance());
        boolean toggle = prefs.getBoolean("sync", true);

        if (toggle) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) LamisPlus.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
            if (isConnected)
                return true;
            else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("sync", false);
                editor.apply();
                return false;
            }

        } else
            return false;

    }

    static public boolean isURLReachable(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return true;
            }
        } catch (ProtocolException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
//        try {
//            InetAddress.getByName("google.com").isReachable(3000); //Replace with your name
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
    }
}

