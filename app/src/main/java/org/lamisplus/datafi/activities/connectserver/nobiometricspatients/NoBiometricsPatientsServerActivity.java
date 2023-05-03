package org.lamisplus.datafi.activities.connectserver.nobiometricspatients;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class NoBiometricsPatientsServerActivity extends LamisBaseActivity {

    public NoBiometricsPatientsServerContract.Presenter mPresenter;
    public NoBiometricsPatientsServerFragment noBiometricsPatientsServerFragment;
    private SearchView searchView;
    private String query;
    private MenuItem mAddPatientMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_find_patient_with_no_biometrics_server);

        noBiometricsPatientsServerFragment = (NoBiometricsPatientsServerFragment) getSupportFragmentManager().findFragmentById(R.id.findPatientServerContentFrame);
        if (noBiometricsPatientsServerFragment == null) {
            noBiometricsPatientsServerFragment = NoBiometricsPatientsServerFragment.newInstance();
        }
        if (!noBiometricsPatientsServerFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), noBiometricsPatientsServerFragment, R.id.findPatientServerContentFrame);
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
            mPresenter = new NoBiometricsPatientsServerPresenter(noBiometricsPatientsServerFragment, query);
        }else{
            mPresenter = new NoBiometricsPatientsServerPresenter(noBiometricsPatientsServerFragment);
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

}
