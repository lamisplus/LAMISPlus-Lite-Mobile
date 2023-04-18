package org.lamisplus.datafi.activities.connectserver;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.io.File;

public class ConnectServerActivity extends LamisBaseActivity {

    public ConnectServerContract.Presenter mPresenter;
    public ConnectServerFragment connectServerFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_server);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        connectServerFragment = (ConnectServerFragment) getSupportFragmentManager().findFragmentById(R.id.connectServerContentFrame);
        if (connectServerFragment == null) {
            connectServerFragment = ConnectServerFragment.newInstance();
        }
        if (!connectServerFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), connectServerFragment, R.id.connectServerContentFrame);
        }
        mPresenter = new ConnectServerPresenter(connectServerFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
