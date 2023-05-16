package org.lamisplus.datafi.activities;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.activities.login.LoginActivity;
import org.lamisplus.datafi.activities.preferences.PrefrencesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.application.LamisPlusLogger;
import org.lamisplus.datafi.auth.AuthorizationManager;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.databases.LamisPlusDBOpenHelper;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.services.SyncServices;
import org.lamisplus.datafi.utilities.ForceClose;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.io.File;
import java.util.ArrayList;

public abstract class LamisBaseActivity extends AppCompatActivity {

    protected FragmentManager mFragmentManager;
    protected final LamisPlus mLamisPlus = LamisPlus.getInstance();
    protected final LamisPlusLogger mLamisPlusLogger = mLamisPlus.getLamisPlusLogger();
    protected AuthorizationManager authorizationManager;
    private MenuItem mSyncbutton;
    private Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ForceClose(this));
        mFragmentManager = getSupportFragmentManager();
        authorizationManager = new AuthorizationManager();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Boolean flag = extras.getBoolean("flag");
            String errorReport = extras.getString("error");
            if (flag) {
                showAppCrashDialog(errorReport);
            }
        }
    }

    public void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        if (fragmentManager == null || fragment == null) {
            throw new NullPointerException();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(frameId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mSyncbutton = menu.findItem(R.id.syncbutton);
        MenuItem logoutMenuItem = menu.findItem(R.id.actionLogout);
        if (logoutMenuItem != null) {
            logoutMenuItem.setTitle(getString(R.string.action_logout) + " " + mLamisPlus.getUsername());
        }
        if (mSyncbutton != null) {
            final Boolean syncState = NetworkUtils.isOnline();
            setSyncButtonState(syncState);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        FormServices broadcastReceiver = new FormServices();
//        IntentFilter intentFilter = new IntentFilter("org.lamisplus.datafi.FormServices");
//        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
//        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void setSyncButtonState(boolean syncState) {
        if (syncState) {
            mSyncbutton.setIcon(R.drawable.ic_sync_on);
        } else {
            mSyncbutton.setIcon(R.drawable.ic_sync_off);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        if (!(this instanceof LoginActivity) && !authorizationManager.isUserLoggedIn()) {
            authorizationManager.moveToLoginActivity();
        }
//        registerReceiver(mPasswordChangedReceiver, mIntentFilter);
//        ToastUtil.setAppVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.actionSettings:
                Intent i = new Intent(this, PrefrencesActivity.class);
                startActivity(i);
                return true;
            case R.id.actionLogout:
                this.logout();
                return true;
            case R.id.syncbutton:
                boolean syncState = LamisPlus.getInstance().getSyncState();
                if (syncState) {
                    LamisPlus.getInstance().setSyncState(false);
                    setSyncButtonState(false);
                    showNoInternetConnectionSnackbar();
                    //getApplicationContext().stopService(new Intent(getApplicationContext(), SyncServices.class));
                    ToastUtil.showShortToast(getApplicationContext(), ToastUtil.ToastType.NOTICE, R.string.disconn_server);
                } else if (NetworkUtils.hasNetwork()) {
                    LamisPlus.getInstance().setSyncState(true);
                    setSyncButtonState(true);

                    Intent ii = new Intent(getApplicationContext(), SyncServices.class);
                    getApplicationContext().startService(ii);

                    ToastUtil.showShortToast(getApplicationContext(), ToastUtil.ToastType.NOTICE, R.string.reconn_server);
                    if (snackbar != null)
                        snackbar.dismiss();
                    ToastUtil.showShortToast(getApplicationContext(), ToastUtil.ToastType.SUCCESS, R.string.connected_to_server_message);

                } else {
                    showNoInternetConnectionSnackbar();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDeletePatientDialog(long patientId) {
        //Log.v("Baron", "Delete this patient?");
    }

    public void dismissCustomFragmentDialog() {
//        if (mCustomFragmentDialog != null) {
//            mCustomFragmentDialog.dismiss();
//        }
    }

    public void showMultiDeletePatientDialog(ArrayList<Person> selectedItems) {
        LamisCustomHandler.showJson(selectedItems);
        for (Person p : selectedItems) {
            long patientIDLong = p.getId();
            int patientIDInteger = (int) patientIDLong;
            //Delete the Patient
            PersonDAO.deletePatient(patientIDLong);
            //Delete the Encounters
            EncounterDAO.deleteAllEncounter(patientIDLong);
            //Delete the Biometrics
            BiometricsDAO.deletePrint(patientIDInteger);
        }
        Intent i = new Intent(this, DashboardActivity.class);
        startActivity(i);
//        CustomDialogBundle bundle = new CustomDialogBundle();
//        bundle.setTitleViewMessage(getString(org.lamisplus.mobile.R.string.delete_multiple_patients));
//        bundle.setTextViewMessage(getString(org.lamisplus.mobile.R.string.delete_multiple_patients_dialog_message));
//        bundle.setRightButtonAction(CustomFragmentDialog.OnClickAction.MULTI_DELETE_PATIENT);
//        bundle.setRightButtonText(getString(R.string.dialog_button_confirm));
//        bundle.setSelectedItems(selectedItems);
//        bundle.setLeftButtonAction(CustomFragmentDialog.OnClickAction.DISMISS);
//        bundle.setLeftButtonText(getString(R.string.dialog_button_cancel));
//        createAndShowDialog(bundle, ApplicationConstants.DialogTAG.MULTI_DELETE_PATIENT_DIALOG_TAG);
    }

//    public void createAndShowDialog(CustomDialogBundle bundle, String tag) {
//        CustomFragmentDialog instance = CustomFragmentDialog.newInstance(bundle);
//        instance.show(mFragmentManager, tag);
//    }

    public void showAppCrashDialog(String error) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle(R.string.crash_dialog_title);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.crash_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.crash_dialog_positive_button, (dialog, id) -> dialog.cancel())
                .setNegativeButton(R.string.crash_dialog_negative_button, (dialog, id) -> finishAffinity())
                .setNeutralButton(R.string.crash_dialog_neutral_button, (dialog, id) -> {
                    String filename = LamisPlus.getInstance().getLamisPlusDir()
                            + File.separator + mLamisPlusLogger.getLogFilename();
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_SUBJECT, R.string.error_email_subject_app_crashed);
                    email.putExtra(Intent.EXTRA_TEXT, error);
                    email.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://" + filename));
                    email.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //need this to prompts email client only
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email, getString(R.string.choose_a_email_client)));
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showNoInternetConnectionSnackbar() {
        snackbar = Snackbar.make(findViewById(android.R.id.content),
                getString(R.string.no_internet_connection_message), Snackbar.LENGTH_INDEFINITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void logout() {
        mLamisPlus.clearUserPreferencesData();
        authorizationManager.moveToLoginActivity();
        //ToastUtil.showShortToast(getApplicationContext(), ToastUtil.ToastType.SUCCESS, R.string.logout_success);
        LamisPlusDBOpenHelper.getInstance().closeDatabases();
    }
}
