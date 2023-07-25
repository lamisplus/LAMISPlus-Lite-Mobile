package org.lamisplus.datafi.activities.forms.pmtct.partners;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.PartnerRegistration;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ViewUtils;

public class PartnersFragment extends LamisBaseFragment<PartnersContract.Presenter> implements PartnersContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private PartnerRegistration updatedPartnerRegistration;

    private EditText edancNo;
    private AutoCompleteTextView autoPartnerHIVStatus;
    private EditText edFullName;
    private EditText edAge;
    private EditText edReferredToOthers;
    private AutoCompleteTextView autoPretestCounseled;
    private AutoCompleteTextView autoPartnerAcceptsHIVTest;
    private AutoCompleteTextView autoPostTestCounseled;
    private AutoCompleteTextView autoHBStatus;
    private AutoCompleteTextView autoHCStatus;
    private AutoCompleteTextView autoSyphilisStatus;
    private AutoCompleteTextView autoReferredTo;
    private TextInputLayout partnerFullNameTIL;
    private TextInputLayout edAgeTIL;
    private TextInputLayout autoPretestCounseledTIL;
    private LinearLayout edReferredToOthersView;
    private String packageName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partners, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            initDropDowns();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.PARTNERS_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.PARTNERS_FORM, mPresenter.getPatientId()));
            }
            autoPopulateFieldsFromANC();
        }
        return root;
    }

    public static PartnersFragment newInstance() {
        return new PartnersFragment();
    }

    private void initiateFragmentViews(View root) {
        mSaveContinueButton = root.findViewById(R.id.mSaveContinueButton);

        edancNo = root.findViewById(R.id.edancNo);
        autoPartnerHIVStatus = root.findViewById(R.id.autoPartnerHIVStatus);
        edFullName = root.findViewById(R.id.edFullName);
        edAge = root.findViewById(R.id.edAge);
        autoPretestCounseled = root.findViewById(R.id.autoPretestCounseled);
        autoPartnerAcceptsHIVTest = root.findViewById(R.id.autoPartnerAcceptsHIVTest);
        autoPostTestCounseled = root.findViewById(R.id.autoPostTestCounseled);
        autoHBStatus = root.findViewById(R.id.autoHBStatus);
        autoHCStatus = root.findViewById(R.id.autoHCStatus);
        autoSyphilisStatus = root.findViewById(R.id.autoSyphilisStatus);
        autoReferredTo = root.findViewById(R.id.autoReferredTo);
        edReferredToOthers = root.findViewById(R.id.edReferredToOthers);

        partnerFullNameTIL = root.findViewById(R.id.partnerFullNameTIL);
        edAgeTIL = root.findViewById(R.id.edAgeTIL);
        autoPretestCounseledTIL = root.findViewById(R.id.autoPretestCounseledTIL);
        edReferredToOthersView = root.findViewById(R.id.edReferredToOthersView);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        autoReferredTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoReferredTo.getText().toString().equals("OTHERS")) {
                    edReferredToOthersView.setVisibility(View.VISIBLE);
                } else {
                    edReferredToOthersView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void autoPopulateFieldsFromANC() {
        ANC anc = EncounterDAO.findAncFromForm(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
        if (anc != null) {
            edancNo.setText(anc.getAncNo());
        }
    }

    private void initDropDowns() {
        String[] positiveNegative = getResources().getStringArray(R.array.positive_negative);
        ArrayAdapter<String> adapterHivStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, positiveNegative);
        autoPartnerHIVStatus.setAdapter(adapterHivStatus);
        autoHBStatus.setAdapter(adapterHivStatus);
        autoHCStatus.setAdapter(adapterHivStatus);

        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoPretestCounseled.setAdapter(adapterBooleanAnswers);
        autoPartnerAcceptsHIVTest.setAdapter(adapterBooleanAnswers);
        autoPostTestCounseled.setAdapter(adapterBooleanAnswers);

        String[] reactiveNonReactive = getResources().getStringArray(R.array.reactive_non_reactive);
        ArrayAdapter<String> adapterReactiveNonReactive = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, reactiveNonReactive);
        autoSyphilisStatus.setAdapter(adapterReactiveNonReactive);

        String[] referredToPMTCT = getResources().getStringArray(R.array.referred_to_partners_pmtct);
        ArrayAdapter<String> adapterReferredPMTCT = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, referredToPMTCT);
        autoReferredTo.setAdapter(adapterReferredPMTCT);
    }


    public void fillFields(PartnerRegistration partnerRegistration) {
        if (partnerRegistration != null) {
            isUpdateRst = true;
            updatedPartnerRegistration = partnerRegistration;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PARTNERS_FORM, mPresenter.getPatientId());

            if (partnerRegistration.getAncNo() != null) {
                edancNo.setText(partnerRegistration.getAncNo());
            }

            if (partnerRegistration.getHivStatus() != null) {
                autoPartnerHIVStatus.setText(partnerRegistration.getHivStatus(), false);
            }

            if (partnerRegistration.getAcceptHivTest() != null) {
                autoPartnerAcceptsHIVTest.setText(partnerRegistration.getAcceptHivTest(), false);
            }

            if (partnerRegistration.getFullName() != null) {
                edFullName.setText(partnerRegistration.getFullName());
            }

            if (partnerRegistration.getAge() != null) {
                edAge.setText(partnerRegistration.getAge());
            }

            if (partnerRegistration.getPreTestCounseled() != null) {
                autoPretestCounseled.setText(partnerRegistration.getPreTestCounseled(), false);
            }

            if (partnerRegistration.getPostTestCounseled() != null) {
                autoPostTestCounseled.setText(partnerRegistration.getPostTestCounseled(), false);
            }

            if (partnerRegistration.getHbStatus() != null) {
                autoHBStatus.setText(partnerRegistration.getHbStatus(), false);
            }

            if (partnerRegistration.getHcStatus() != null) {
                autoHCStatus.setText(partnerRegistration.getHcStatus(), false);
            }

            if (partnerRegistration.getSyphillisStatus() != null) {
                autoSyphilisStatus.setText(partnerRegistration.getSyphillisStatus(), false);
            }

            if (partnerRegistration.getReferredTo() != null) {
                autoReferredTo.setText(partnerRegistration.getReferredTo(), false);
            }

            if (partnerRegistration.getReferredTo().equals("OTHERS")) {
                edReferredToOthersView.setVisibility(View.VISIBLE);
            }

            if (partnerRegistration.getReferredToOthers() != null) {
                edReferredToOthers.setText(partnerRegistration.getReferredToOthers());
            }
        }
    }

    private PartnerRegistration createEncounter() {
        PartnerRegistration partnerEnrollment = new PartnerRegistration();
        updateEncounterWithData(partnerEnrollment);
        return partnerEnrollment;
    }

    private PartnerRegistration updateEncounter(PartnerRegistration partnerRegistration) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(partnerRegistration);
        return partnerRegistration;
    }

    private PartnerRegistration updateEncounterWithData(PartnerRegistration partnerRegistration) {
        if (!ViewUtils.isEmpty(edancNo)) {
            partnerRegistration.setAncNo(ViewUtils.getInput(edancNo));
        }

        if (!ViewUtils.isEmpty(autoPartnerHIVStatus)) {
            partnerRegistration.setHivStatus(ViewUtils.getInput(autoPartnerHIVStatus));
        }

        if (!ViewUtils.isEmpty(autoPartnerAcceptsHIVTest)) {
            partnerRegistration.setAcceptHivTest(ViewUtils.getInput(autoPartnerAcceptsHIVTest));
        }

        if (!ViewUtils.isEmpty(edFullName)) {
            partnerRegistration.setFullName(ViewUtils.getInput(edFullName));
        }

        if (!ViewUtils.isEmpty(edAge)) {
            partnerRegistration.setAge(ViewUtils.getInput(edAge));
        }

        if (!ViewUtils.isEmpty(autoPretestCounseled)) {
            partnerRegistration.setPreTestCounseled(ViewUtils.getInput(autoPretestCounseled));
        }

        if (!ViewUtils.isEmpty(autoPostTestCounseled)) {
            partnerRegistration.setPostTestCounseled(ViewUtils.getInput(autoPostTestCounseled));
        }

        if (!ViewUtils.isEmpty(autoHBStatus)) {
            partnerRegistration.setHbStatus(ViewUtils.getInput(autoHBStatus));
        }

        if (!ViewUtils.isEmpty(autoHCStatus)) {
            partnerRegistration.setHcStatus(ViewUtils.getInput(autoHCStatus));
        }

        if (!ViewUtils.isEmpty(autoSyphilisStatus)) {
            partnerRegistration.setSyphillisStatus(ViewUtils.getInput(autoSyphilisStatus));
        }

        if (!ViewUtils.isEmpty(autoReferredTo)) {
            partnerRegistration.setReferredTo(ViewUtils.getInput(autoReferredTo));
        }

        if (!ViewUtils.isEmpty(edReferredToOthers)) {
            partnerRegistration.setReferredToOthers(ViewUtils.getInput(edReferredToOthers));
        }

        return partnerRegistration;
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
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.PARTNERS_FORM, mPresenter.getPatientId());
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
                if (isUpdateRst) {
                    mPresenter.confirmUpdate(updateEncounter(updatedPartnerRegistration), updatedForm);
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
    public void setErrorsVisibility(boolean fullName, boolean age, boolean pretestCounseled) {
        if (fullName) {
            partnerFullNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            partnerFullNameTIL.setError("This field is required");
        }

        if (age) {
            edAgeTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edAgeTIL.setError("This field is required");
        }

        if (pretestCounseled) {
            autoPretestCounseledTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoPretestCounseledTIL.setError("This field is required");
        }

    }

}
