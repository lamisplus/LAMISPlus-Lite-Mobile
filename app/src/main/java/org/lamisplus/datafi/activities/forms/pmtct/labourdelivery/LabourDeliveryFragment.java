package org.lamisplus.datafi.activities.forms.pmtct.labourdelivery;

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
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class LabourDeliveryFragment extends LamisBaseFragment<LabourDeliveryContract.Presenter> implements LabourDeliveryContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateLabourDelivery = false;
    private Encounter updatedForm;
    private LabourDelivery updatedLabourDelivery;

    private String packageName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_labour_delivery, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

        }
        return root;
    }

    public static LabourDeliveryFragment newInstance() {
        return new LabourDeliveryFragment();
    }

    private void initiateFragmentViews(View root) {

    }

    private void setListeners() {
        //mSaveContinueButton.setOnClickListener(this);

    }

    private void showDatePickers() {

    }


    public void fillFields(LabourDelivery labourDelivery) {
        if (labourDelivery != null) {
            isUpdateLabourDelivery = true;
            updatedLabourDelivery = labourDelivery;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM, mPresenter.getPatientId());

        }
    }

    private LabourDelivery createEncounter() {
        LabourDelivery labourDelivery = new LabourDelivery();
        updateEncounterWithData(labourDelivery);
        return labourDelivery;
    }

    private LabourDelivery updateEncounter(LabourDelivery labourDelivery) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(labourDelivery);
        return labourDelivery;
    }

    private LabourDelivery updateEncounterWithData(LabourDelivery labourDelivery) {


        return labourDelivery;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateLabourDelivery) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM, mPresenter.getPatientId());
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
                if (isUpdateLabourDelivery) {
                    mPresenter.confirmUpdate(updateEncounter(updatedLabourDelivery), updatedForm);
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
