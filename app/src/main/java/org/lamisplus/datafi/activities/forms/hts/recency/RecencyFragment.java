package org.lamisplus.datafi.activities.forms.hts.recency;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class RecencyFragment extends LamisBaseFragment<RecencyContract.Presenter> implements RecencyContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateRecency = false;
    private Encounter updatedForm;
    private Recency updatedRecency;
    private String packageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recency, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(isUpdateRecency) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounterRecency(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static RecencyFragment newInstance() {
        return new RecencyFragment();
    }

    private void initiateFragmentViews(View root) {

        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if (isUpdateRecency) {
                    mPresenter.confirmUpdate(updateEncounter(updatedRecency), updatedForm);
                } else {
                    mPresenter.confirmCreate(createEncounter(), packageName);
                }
                break;
            default:

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void fillFields(Recency recency) {
        if (recency != null) {
            isUpdateRecency = true;
            updatedRecency = recency;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId());
        }
    }

    private Recency createEncounter() {
        Recency recency = new Recency();
        updateEncounterWithData(recency);
        return recency;
    }

    private Recency updateEncounter(Recency recency) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(recency);
        return recency;
    }

    private Recency updateEncounterWithData(Recency recency) {
//        if (!ViewUtils.isEmpty(autoTargetGroup)) {
//            clientIntake.setTargetGroup(ViewUtils.getInput(autoTargetGroup));
//        }
//
//        if (!ViewUtils.isEmpty(edClientCode)) {
//            clientIntake.setClientCode(ViewUtils.getInput(edClientCode));
//        }
//
//        if (!ViewUtils.isEmpty(autoReferredFrom)) {
//            clientIntake.setReferredFrom(ViewUtils.getInput(autoReferredFrom));
//        }
//
//        if (!ViewUtils.isEmpty(autoSettings)) {
//            clientIntake.setTestingSetting(ViewUtils.getInput(autoSettings));
//        }
//
//        if (!ViewUtils.isEmpty(edVisitDate)) {
//            clientIntake.setDateVisit(ViewUtils.getInput(edVisitDate));
//        }
//
//        if (!ViewUtils.isEmpty(edNumberOfChildren)) {
//            clientIntake.setNumChildren(Integer.parseInt(ViewUtils.getInput(edNumberOfChildren)));
//        }
//
//        if (!ViewUtils.isEmpty(autoIndexTesting)) {
//            clientIntake.setIndexClient(Boolean.valueOf(ViewUtils.getInput(autoReferredFrom)));
//        }
//
//        if (!ViewUtils.isEmpty(edNoWives)) {
//            clientIntake.setNumWives(Integer.parseInt(ViewUtils.getInput(edNoWives)));
//        }
//
//        if (!ViewUtils.isEmpty(autoPregnant)) {
//            clientIntake.setPregnant(ViewUtils.getInput(autoPregnant));
//        }
//
//        if (!ViewUtils.isEmpty(autoBreastfeeding)) {
//            clientIntake.setBreastFeeding(Boolean.valueOf(ViewUtils.getInput(autoBreastfeeding)));
//        }
//
//        if (!ViewUtils.isEmpty(autoFirstimeVisit)) {
//            clientIntake.setFirstTimeVisit(Boolean.valueOf(ViewUtils.getInput(autoFirstimeVisit)));
//        }
//
//        if (!ViewUtils.isEmpty(autoPreviouslyTested)) {
//            clientIntake.setPreviouslyTested(Boolean.valueOf(ViewUtils.getInput(autoPreviouslyTested)));
//        }
//
//
//        if (!ViewUtils.isEmpty(autoTypeCounseling)) {
//            clientIntake.setTypeCounseling(ViewUtils.getInput(autoTypeCounseling));
//        }
//
//        if (!ViewUtils.isEmpty(autoRelationshipIndex)) {
//            clientIntake.setRelationWithIndexClient(ViewUtils.getInput(autoRelationshipIndex));
//        }
//
//        clientIntake.setPersonId("");
//
//        clientIntake.setPersonDto("{}");
//
//        clientIntake.setExtra("{}");

        return recency;
    }


    @Override
    public void startActivityForDashboard() {
            Intent DashboardProgram = new Intent(LamisPlus.getInstance(), PatientDashboardActivity.class);
        DashboardProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            startActivity(DashboardProgram);
    }

}
