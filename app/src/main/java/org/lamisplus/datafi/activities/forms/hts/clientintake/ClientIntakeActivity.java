package org.lamisplus.datafi.activities.forms.hts.clientintake;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class ClientIntakeActivity extends LamisBaseActivity {

    public ClientIntakeContract.Presenter mPresenter;
    public ClientIntakeFragment clientIntakeFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_intake);

        clientIntakeFragment = (ClientIntakeFragment) getSupportFragmentManager().findFragmentById(R.id.clientIntakeContentFrame);
        if (clientIntakeFragment == null) {
            clientIntakeFragment = ClientIntakeFragment.newInstance();
        }
        if (!clientIntakeFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), clientIntakeFragment, R.id.clientIntakeContentFrame);
        }

        Bundle patientBundle = savedInstanceState;
        if (patientBundle != null) {
            patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            patientBundle = getIntent().getExtras();
        }
        String patientID = "";
        if (patientBundle != null) {
            patientID = patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        }

        mPresenter = new ClientIntakePresenter(clientIntakeFragment, patientID);
    }

}
