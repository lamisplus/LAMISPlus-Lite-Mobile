package org.lamisplus.datafi.activities.forms.pmtct.partners;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class PartnersActivity extends LamisBaseActivity {

    public PartnersContract.Presenter mPresenter;
    public PartnersFragment PartnersFragment;

    String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);

        PartnersFragment = (PartnersFragment) getSupportFragmentManager().findFragmentById(R.id.partnersContentFrame);
        if (PartnersFragment == null) {
            PartnersFragment = PartnersFragment.newInstance();
        }
        if (!PartnersFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), PartnersFragment, R.id.partnersContentFrame);
        }

        Bundle patientBundle = savedInstanceState;
        if (patientBundle != null) {
            patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            patientBundle = getIntent().getExtras();
        }

        if (patientBundle != null) {
            patientID = patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        }

        mPresenter = new PartnersPresenter(PartnersFragment, patientID);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, patientID);
        onSaveInstanceState(bundle);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
