package org.lamisplus.datafi.activities.hts.htsprogram;

import static com.activeandroid.Cache.getContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

public class HTSProgramActivity extends LamisBaseActivity {

    public HTSProgramContract.Presenter mPresenter;
    public HTSProgramFragment HTSProgramFragment;
    private SearchView searchView;
    private String query;
    private MenuItem mAddPatientMenuItem;

    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;
    private String mLattitude = null;
    private String mLongitude = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_hts_program);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();

        HTSProgramFragment = (HTSProgramFragment) getSupportFragmentManager().findFragmentById(R.id.htsProgramContentFrame);
        if (HTSProgramFragment == null) {
            HTSProgramFragment = HTSProgramFragment.newInstance();
        }
        if (!HTSProgramFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), HTSProgramFragment, R.id.htsProgramContentFrame);
        }

        Bundle queryBundle = savedInstanceState;
        if (queryBundle != null) {
            queryBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            queryBundle = getIntent().getExtras();
        }
        String query = "";
        if (queryBundle != null) {
            query = queryBundle.getString(ApplicationConstants.BundleKeys.PATIENT_QUERY_BUNDLE);
            mPresenter = new HTSProgramPresenter(HTSProgramFragment, query);
        }else{
            mPresenter = new HTSProgramPresenter(HTSProgramFragment);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String query = searchView.getQuery().toString();
        outState.putString(ApplicationConstants.BundleKeys.PATIENT_QUERY_BUNDLE, query);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.syncbutton:
                enableAddPatient(LamisPlus.getInstance().getSyncState());
                break;
            case R.id.actionAddPatients:
                //This section was intentionally left blank for future use of downloading patients from the server
//                Intent intent = new Intent(this, LastViewedPatientsActivity.class);
//                startActivity(intent);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                // Do nothing
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.find_locally_and_add_patients_menu, menu);

        mAddPatientMenuItem = menu.findItem(R.id.actionAddPatients);
        enableAddPatient(LamisPlus.getInstance().getSyncState());

        // Search function
        MenuItem searchMenuItem = menu.findItem(R.id.actionSearchLocal);
        searchView = (SearchView) searchMenuItem.getActionView();

        if(StringUtils.notEmpty(query)){
            searchMenuItem.expandActionView();
            searchView.setQuery(query, true);
            searchView.clearFocus();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mPresenter.setQuery(query);
                mPresenter.updateLocalPatientsList();
                return true;
            }
        });

        return true;
    }

    private void enableAddPatient(boolean enabled) {
        int resId = enabled ? R.drawable.ic_add : R.drawable.ic_add_disabled;
        mAddPatientMenuItem.setEnabled(enabled);
        mAddPatientMenuItem.setIcon(resId);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                android.location.Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    mLattitude = location.getLatitude() + "";
                                    mLongitude = location.getLongitude() + "";


                                }
                            }
                        }
                );
            } else {
                ToastUtil.showLongToast(this, ToastUtil.ToastType.ERROR, "Turn on location");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            mLattitude = mLastLocation.getLatitude() + "";
            mLongitude = mLastLocation.getLongitude() + "";

        }
    };

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

}
