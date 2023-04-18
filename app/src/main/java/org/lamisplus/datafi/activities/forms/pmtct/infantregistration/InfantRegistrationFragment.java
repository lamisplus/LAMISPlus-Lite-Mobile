package org.lamisplus.datafi.activities.forms.pmtct.infantregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class InfantRegistrationFragment extends LamisBaseFragment<InfantRegistrationContract.Presenter> implements InfantRegistrationContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private PMTCTEnrollment updatedPmtctEnrollment;

    private String packageName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pmtct_enrollment, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

        }
        return root;
    }

    public static InfantRegistrationFragment newInstance() {
        return new InfantRegistrationFragment();
    }

    private void initiateFragmentViews(View root) {

    }

    private void setListeners() {
        //mSaveContinueButton.setOnClickListener(this);

    }

    private void showDatePickers() {

    }


    public void fillFields(PMTCTEnrollment pmtctEnrollment) {
        if (pmtctEnrollment != null) {
            isUpdateRst = true;
            updatedPmtctEnrollment = pmtctEnrollment;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId());

        }
    }

    private PMTCTEnrollment createEncounter() {
        PMTCTEnrollment pmtctEnrollment = new PMTCTEnrollment();
        updateEncounterWithData(pmtctEnrollment);
        return pmtctEnrollment;
    }

    private PMTCTEnrollment updateEncounter(PMTCTEnrollment pmtctEnrollment) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(pmtctEnrollment);
        return pmtctEnrollment;
    }

    private PMTCTEnrollment updateEncounterWithData(PMTCTEnrollment pmtctEnrollment) {


        return pmtctEnrollment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateRst) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if (isUpdateRst) {
                    mPresenter.confirmUpdate(updateEncounter(updatedPmtctEnrollment), updatedForm);
                } else {
                    mPresenter.confirmCreate(createEncounter(), packageName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void scrollToTop() {
        ScrollView scrollView = this.getActivity().findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, scrollView.getPaddingTop());
    }

    @Override
    public void startPMTCTEnrollmentActivity() {
        Intent htsActivity = new Intent(LamisPlus.getInstance(), HTSServicesActivity.class);
        htsActivity.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(mPresenter.getPatientId()));
        startActivity(htsActivity);
        getActivity().finish();
    }


    @Override
    public void setErrorsVisibility(boolean dateOfBirth, boolean entryPoint, boolean settings, boolean modality, boolean visitDate, boolean targetGroup, boolean autolastHivTestBasedOnRequest) {


    }

}
