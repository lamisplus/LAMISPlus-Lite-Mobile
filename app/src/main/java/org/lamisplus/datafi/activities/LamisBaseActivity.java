package org.lamisplus.datafi.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.login.LoginActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.application.LamisPlusLogger;
import org.lamisplus.datafi.auth.AuthorizationManager;
import org.lamisplus.datafi.databases.LamisPlusDBOpenHelper;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.services.EncounterService;
import org.lamisplus.datafi.services.PatientService;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        mFragmentManager = getSupportFragmentManager();
        authorizationManager = new AuthorizationManager();
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
//                Intent i = new Intent(this, SettingsActivity.class);
//                startActivity(i);
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
                    ToastUtil.showShortToast(getApplicationContext(), ToastUtil.ToastType.NOTICE, R.string.disconn_server);
                } else if (NetworkUtils.hasNetwork()) {
                    LamisPlus.getInstance().setSyncState(true);
                    setSyncButtonState(true);

//                    Intent intent = new Intent("org.openmrs.mobile.intent.action.SYNC_PATIENTS");
//                    getApplicationContext().sendBroadcast(intent);
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Intent ii = new Intent(getApplicationContext(), PatientService.class);
                    getApplicationContext().startService(ii);

                    //This is to handle android sync version 10
                    Intent i1 = new Intent(getApplicationContext(), EncounterService.class);
                    getApplicationContext().startService(i1);
//                    }else{
//                        Intent intent = new Intent("org.openmrs.mobile.intent.action.SYNC_PATIENTS");
//                        getApplicationContext().sendBroadcast(intent);
//                    }

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

    public void showDeletePatientDialog() {
        Log.v("Baron", "Delete this patient?");
    }

    public void dismissCustomFragmentDialog() {
//        if (mCustomFragmentDialog != null) {
//            mCustomFragmentDialog.dismiss();
//        }
    }

    public void showMultiDeletePatientDialog(ArrayList<Person> selectedItems) {
//        CustomDialogBundle bundle = new CustomDialogBundle();
//        bundle.setTitleViewMessage(getString(org.openmrs.mobile.R.string.delete_multiple_patients));
//        bundle.setTextViewMessage(getString(org.openmrs.mobile.R.string.delete_multiple_patients_dialog_message));
//        bundle.setRightButtonAction(CustomFragmentDialog.OnClickAction.MULTI_DELETE_PATIENT);
//        bundle.setRightButtonText(getString(R.string.dialog_button_confirm));
//        bundle.setSelectedItems(selectedItems);
//        bundle.setLeftButtonAction(CustomFragmentDialog.OnClickAction.DISMISS);
//        bundle.setLeftButtonText(getString(R.string.dialog_button_cancel));
//        createAndShowDialog(bundle, ApplicationConstants.DialogTAG.MULTI_DELETE_PATIENT_DIALOG_TAG);
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
