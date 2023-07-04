package org.lamisplus.datafi.activities.forms.pmtct.infantregistration;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.activities.pmtct.pmtctservices.PMTCTServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.InfantRegistration;
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ViewUtils;

public class InfantRegistrationFragment extends LamisBaseFragment<InfantRegistrationContract.Presenter> implements InfantRegistrationContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateInfantRegistration = false;
    private Encounter updatedForm;

    private EditText edancNo;
    private EditText edDateDelivery;
    private EditText edInfantGivenName;
    private EditText edInfantSurname;
    private EditText edhospitalNumber;
    private AutoCompleteTextView autoSex;
    private TextInputLayout edInfantGivenNameTIL;
    private TextInputLayout autoSexTIL;
    private TextInputLayout edhospitalNumberTIL;

    private String packageName;

    private InfantRegistration updatedInfantRegistration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_infant_registration, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            initDropDownsPatients();
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.INFANT_INFORMATION_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.INFANT_INFORMATION_FORM, mPresenter.getPatientId()));
            }
            autoPopulateFieldsFromANC();
        }
        return root;
    }

    public static InfantRegistrationFragment newInstance() {
        return new InfantRegistrationFragment();
    }

    private void initiateFragmentViews(View root) {
        mSaveContinueButton = root.findViewById(R.id.mSaveContinueButton);
        edancNo = root.findViewById(R.id.edancNo);
        edDateDelivery = root.findViewById(R.id.edDateDelivery);
        edInfantGivenName = root.findViewById(R.id.edInfantGivenName);
        edInfantSurname = root.findViewById(R.id.edInfantSurname);
        edhospitalNumber = root.findViewById(R.id.edhospitalNumber);
        autoSex = root.findViewById(R.id.autoSex);
        edInfantGivenNameTIL = root.findViewById(R.id.edInfantGivenNameTIL);
        autoSexTIL = root.findViewById(R.id.autoSexTIL);
        edhospitalNumberTIL = root.findViewById(R.id.edhospitalNumberTIL);
    }

    public void initDropDownsPatients() {
        String[] gender = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, gender);
        autoSex.setAdapter(adapterGender);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);
    }

    private void showDatePickers() {

    }

    private void autoPopulateFieldsFromANC() {
        ANC anc = EncounterDAO.findAncFromForm(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
        LabourDelivery labourDelivery = EncounterDAO.findLabourDeliveryFromForm(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM, mPresenter.getPatientId());
        if (anc != null) {
            edancNo.setText(anc.getAncNo());
        }
        if (labourDelivery != null) {
            edDateDelivery.setText(labourDelivery.getDateOfDelivery());
        }
    }


    public void fillFields(InfantRegistration infantRegistration) {
        if (infantRegistration != null) {
            isUpdateInfantRegistration = true;
            updatedInfantRegistration = infantRegistration;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.INFANT_INFORMATION_FORM, mPresenter.getPatientId());

            if(infantRegistration.getFirstName() != null){
                edInfantGivenName.setText(infantRegistration.getFirstName());
            }

            if(infantRegistration.getSurname() != null){
                edInfantSurname.setText(infantRegistration.getSurname());
            }

            if(infantRegistration.getSex() != null){
                autoSex.setText(infantRegistration.getSex(), false);
            }

            if(infantRegistration.getHospitalNumber() != null){
                edhospitalNumber.setText(infantRegistration.getHospitalNumber());
            }
        }
    }

    private InfantRegistration createEncounter() {
        InfantRegistration pmtctEnrollment = new InfantRegistration();
        updateEncounterWithData(pmtctEnrollment);
        return pmtctEnrollment;
    }

    private InfantRegistration updateEncounter(InfantRegistration infantRegistration) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(infantRegistration);
        return infantRegistration;
    }

    private InfantRegistration updateEncounterWithData(InfantRegistration infantRegistration) {
        if (!ViewUtils.isEmpty(edancNo)) {
            infantRegistration.setAncNo(ViewUtils.getInput(edancNo));
        }

        if (!ViewUtils.isEmpty(edDateDelivery)) {
            infantRegistration.setDateOfDelivery(ViewUtils.getInput(edDateDelivery));
        }

        if (!ViewUtils.isEmpty(edInfantGivenName)) {
            infantRegistration.setFirstName(ViewUtils.getInput(edInfantGivenName));
        }

        if (!ViewUtils.isEmpty(edInfantSurname)) {
            infantRegistration.setSurname(ViewUtils.getInput(edInfantSurname));
        }

        if (!ViewUtils.isEmpty(autoSex)) {
            infantRegistration.setSex(ViewUtils.getInput(autoSex));
        }

        if (!ViewUtils.isEmpty(edhospitalNumber)) {
            infantRegistration.setHospitalNumber(ViewUtils.getInput(edhospitalNumber));
        }

        return infantRegistration;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateInfantRegistration) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.INFANT_INFORMATION_FORM, mPresenter.getPatientId());
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
            case R.id.mSaveContinueButton:
                if (isUpdateInfantRegistration) {
                    mPresenter.confirmUpdate(updateEncounter(updatedInfantRegistration), updatedForm);
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
    public void startPMTCTServicesActivity() {
        Intent pmtctActivity = new Intent(LamisPlus.getInstance(), PMTCTServicesActivity.class);
        pmtctActivity.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(mPresenter.getPatientId()));
        startActivity(pmtctActivity);
        getActivity().finish();
    }


    @Override
    public void setErrorsVisibility(boolean InfantGivenName, boolean sex, boolean hospitalNumber) {
        if (InfantGivenName) {
            edInfantGivenNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edInfantGivenNameTIL.setError("This field is required");
        }

        if (sex) {
            autoSexTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoSexTIL.setError("This field is required");
        }

        if (hospitalNumber) {
            edhospitalNumberTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edhospitalNumberTIL.setError("This field is required");
        }
    }

}
