package org.lamisplus.datafi.activities.biometrics.neurotech;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;

import com.neurotec.samples.view.BasePreferenceFragment;
import com.neurotec.samples.view.InfoDialogFragment;

import org.lamisplus.datafi.R;

public class ConnectionPreferences extends PreferenceActivity {

    public static enum ConnectionType {
        SQLITE,
        CLUSTER,
        MMABIS
    }

    // ===========================================================
    // Public static fields
    // ===========================================================

    public static final String SQLITE_DATABASE_CONNECTION_PREFERENCE = "sqlite_database_connection";
    public static final String REMOTE_CLUSTER_SERVER_PREFERENCE = "remote_cluster_server";
    public static final String REMOTE_MMABIS_SERVER_PREFERENCE = "remote_mmabis_server";

    public static final String CLUSTER_SERVER_ADDRESS_PREFERENCE = "cluster_server_address";
    public static final String CLUSTER_CLIENT_PORT_PREFERENCE = "cluster_client_port";
    public static final String CLUSTER_ADMIN_PORT_PREFERENCE = "cluster_admin_port";

    public static final String MMABIS_SERVER_PREFERENCE = "remote_mmabis_server";
    public static final String MMABIS_SERVER_ADDRESS_PREFERENCE = "mmabis_server_address";
    public static final String MMABIS_USERNAME_PREFERENCE = "mmabis_username";
    public static final String MMABIS_PASSWORD_PREFERENCE = "mmabis_password";

    // ===========================================================
    // Public methods
    // ===========================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ConnectionPreferencesFragment()).commit();
    }

    public static ConnectionType getConnectionType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences.getBoolean(SQLITE_DATABASE_CONNECTION_PREFERENCE, true)) {
            return ConnectionType.SQLITE;
        } else if (preferences.getBoolean(REMOTE_CLUSTER_SERVER_PREFERENCE, false)) {
            return ConnectionType.CLUSTER;
        } else if (preferences.getBoolean(REMOTE_MMABIS_SERVER_PREFERENCE, false)) {
            return ConnectionType.MMABIS;
        } else {
            throw new AssertionError("Was unable to detect connection type");
        }
    }

    public static String getClusterServerAddress(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(CLUSTER_SERVER_ADDRESS_PREFERENCE, "localhost");
    }

    public static int getClusterClientPort(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(preferences.getString(CLUSTER_CLIENT_PORT_PREFERENCE, "25452"));
    }

    public static int getClusterAdminPort(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(preferences.getString(CLUSTER_ADMIN_PORT_PREFERENCE, "24932"));
    }

    public static String getMMABISServerAddress(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(MMABIS_SERVER_ADDRESS_PREFERENCE, "megamatcher.online");
    }

    public static String getMMABISUsername(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(MMABIS_USERNAME_PREFERENCE, "user");
    }

    public static String getMMABISPassword(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(MMABIS_PASSWORD_PREFERENCE, "pass");
    }

    public static class ConnectionPreferencesFragment extends BasePreferenceFragment {

        private boolean showInfo = true;

        // ===========================================================
        // Public methods
        // ===========================================================

        private void showInfoMessage() {
            if (showInfo) {
                InfoDialogFragment.newInstance("Changing connection requires applicatin to be restarted").show(getFragmentManager(), "info");
                showInfo = false;
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.connection_preferences);
        }

        private boolean isAtleastOneConnectionSelected() {
            return ((SwitchPreference)findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE)).isChecked() ||
                    ((SwitchPreference)findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE)).isChecked() ||
                    ((SwitchPreference)findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE)).isChecked();
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            if (preference.getKey().equals(SQLITE_DATABASE_CONNECTION_PREFERENCE)) {
                if (isAtleastOneConnectionSelected()) {
                    ((SwitchPreference)findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE)).setChecked(false);
                } else {
                    ((SwitchPreference)findPreference(SQLITE_DATABASE_CONNECTION_PREFERENCE)).setChecked(true);
                }
                //((SwitchPreference)findPreference(REMOTE_MMABIS_SERVER_PREFERENCE)).setChecked(false);
                showInfoMessage();
            } else if (preference.getKey().equals(REMOTE_CLUSTER_SERVER_PREFERENCE)) {
                if (isAtleastOneConnectionSelected()) {
                    ((SwitchPreference)findPreference(SQLITE_DATABASE_CONNECTION_PREFERENCE)).setChecked(false);
                } else {
                    ((SwitchPreference)findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE)).setChecked(true);
                }
                //((SwitchPreference)findPreference(REMOTE_MMABIS_SERVER_PREFERENCE)).setChecked(false);
                showInfoMessage();
            /*} else if (preference.getKey().equals(REMOTE_MMABIS_SERVER_PREFERENCE)) {
                ((SwitchPreference)findPreference(SQLITE_DATABASE_CONNECTION_PREFERENCE)).setChecked(false);
                ((SwitchPreference)findPreference(REMOTE_CLUSTER_SERVER_PREFERENCE)).setChecked(false);
                showInfoMessage();*/
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
    }

}
