package org.lamisplus.datafi.activities.pmtct.pmtctservices;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.forms.hts.elicitation.ElicitationActivity;
import org.lamisplus.datafi.activities.forms.hts.posttest.PostTestActivity;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestActivity;
import org.lamisplus.datafi.activities.forms.hts.recency.RecencyActivity;
import org.lamisplus.datafi.activities.forms.hts.requestresult.RequestResultActivity;
import org.lamisplus.datafi.activities.forms.hts.rst.RSTActivity;
import org.lamisplus.datafi.activities.forms.pmtct.anc.ANCActivity;
import org.lamisplus.datafi.activities.forms.pmtct.infantregistration.InfantRegistrationActivity;
import org.lamisplus.datafi.activities.forms.pmtct.labourdelivery.LabourDeliveryActivity;
import org.lamisplus.datafi.activities.forms.pmtct.partners.PartnersActivity;
import org.lamisplus.datafi.activities.forms.pmtct.pmtctenrollment.PMTCTEnrollmentActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.List;
import java.util.Objects;

public class PMTCTServicesFragment extends LamisBaseFragment<PMTCTServicesContract.Presenter> implements PMTCTServicesContract.View, View.OnClickListener {

    private LinearLayout lvAncFormView;
    private LinearLayout lvPmtctEnrollmentFormView;
    private LinearLayout lvLabourDeliveryFormView;
    private LinearLayout lvInfantInformationFormView;
    private LinearLayout lvPartnersFormView;

    private TextView tvAnc;
    private TextView tvPmtctEnrollmentForm;
    private TextView tvLabourDeliveryForm;
    private TextView tvInfantInformationForm;
    private TextView tvPartnersForm;
    private Button patientDashboardButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pmtct_services, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            checkExistingEnteredForms();
        }
        return root;
    }

    public static PMTCTServicesFragment newInstance() {
        return new PMTCTServicesFragment();
    }

    private void initiateFragmentViews(View root) {
        lvAncFormView = root.findViewById(R.id.lvAncFormView);
        lvPmtctEnrollmentFormView = root.findViewById(R.id.lvPmtctEnrollmentFormView);
        lvLabourDeliveryFormView = root.findViewById(R.id.lvLabourDeliveryFormView);
        lvInfantInformationFormView = root.findViewById(R.id.lvInfantInformationFormView);
        lvPartnersFormView = root.findViewById(R.id.lvPartnersFormView);
        patientDashboardButton = root.findViewById(R.id.patientDashboardButton);

        tvAnc = root.findViewById(R.id.tvAnc);
        tvPmtctEnrollmentForm = root.findViewById(R.id.tvPmtctEnrollmentForm);
        tvLabourDeliveryForm = root.findViewById(R.id.tvLabourDeliveryForm);
        tvInfantInformationForm = root.findViewById(R.id.tvInfantInformationForm);
        tvPartnersForm = root.findViewById(R.id.tvPartnersForm);
    }

    private void checkExistingEnteredForms() {
        List<Encounter> encounterList = EncounterDAO.findAllEncounterForPatient(mPresenter.getPatientId());
        for (Encounter encounter : encounterList) {
            if (encounter.getName().equals(ApplicationConstants.Forms.ANC_FORM)) {
                tvAnc.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM)) {
                tvPmtctEnrollmentForm.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM)) {
                tvLabourDeliveryForm.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.INFANT_INFORMATION_FORM)) {
                tvInfantInformationForm.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.PARTNERS_FORM)) {
                tvPartnersForm.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void setListeners() {
        lvAncFormView.setOnClickListener(this);
        lvPmtctEnrollmentFormView.setOnClickListener(this);
        lvLabourDeliveryFormView.setOnClickListener(this);
        lvInfantInformationFormView.setOnClickListener(this);
        lvPartnersFormView.setOnClickListener(this);
        patientDashboardButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvAncFormView:
                Intent intentAnc = new Intent(getActivity(), ANCActivity.class);
                intentAnc.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                startActivity(intentAnc);
                break;
            case R.id.lvPmtctEnrollmentFormView:
                if (!checkANCFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the ANC Form first");
                } else {
                    if (checkEligiblePMTCT()) {
                        Intent intentClin = new Intent(getActivity(), PMTCTEnrollmentActivity.class);
                        intentClin.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                        startActivity(intentClin);
                    } else {
                        ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Not eligible for PMTCT");
                    }
                }
                break;
            case R.id.lvLabourDeliveryFormView:
                if (!checkANCFormExists() || !checkPMTCTEnrollFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the ANC && PMTCT Enrollment Forms first before proceeding to this stage");
                } else {
                    if (checkEligiblePMTCT()) {
                        Intent intentPreTest = new Intent(getActivity(), LabourDeliveryActivity.class);
                        intentPreTest.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                        startActivity(intentPreTest);
                    } else {
                        ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Not eligible for PMTCT");
                    }
                }
                break;
            case R.id.lvInfantInformationFormView:
                if (!checkANCFormExists() || !checkPMTCTEnrollFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the ANC && PMTCT Enrollment Forms first before proceeding to this stage");
                } else {
                    if (checkEligiblePMTCT() && checkEligibleInfantInformation()) {
                        Intent intentInfantReg = new Intent(getActivity(), InfantRegistrationActivity.class);
                        intentInfantReg.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                        startActivity(intentInfantReg);
                    } else {
                        ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Not eligible");
                    }
                }
                break;
            case R.id.lvPartnersFormView:
                if (!checkANCFormExists() || !checkPMTCTEnrollFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the ANC && PMTCT Enrollment Forms first before proceeding to this stage");
                } else {
                    if (checkEligiblePMTCT()) {
                        Intent intentPartners = new Intent(getActivity(), PartnersActivity.class);
                        intentPartners.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                        startActivity(intentPartners);
                    } else {
                        ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Not eligible for PMTCT");
                    }
                }
                break;
            case R.id.patientDashboardButton:
                if (!checkANCFormExists() || !checkPMTCTEnrollFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the ANC && PMTCT Enrollment Forms first before proceeding to this stage");
                } else {
                    if (checkEligiblePMTCT()) {
                        Intent intentPartners = new Intent(getActivity(), PatientDashboardActivity.class);
                        intentPartners.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                        startActivity(intentPartners);
                    } else {
                        ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Not eligible for PMTCT");
                    }
                }
                break;
            default:

                break;
        }
    }

    private boolean checkEligiblePMTCT() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
        if (encounter != null) {
            ANC anc = new Gson().fromJson(Objects.requireNonNull(encounter).getDataValues(), ANC.class);
            if(anc != null) {
                return anc.getStaticHivStatus().equals("Positive");
            }
        }
        return false;
    }

    private boolean checkANCFormExists() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            return false;
        }
        return true;
    }

    private boolean checkEligibleInfantInformation() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            return false;
        }
        LabourDelivery labourDelivery = new Gson().fromJson(Objects.requireNonNull(encounter).getDataValues(), LabourDelivery.class);
        return !CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getChildStatus()).equals("Still Birth");
    }

    private boolean checkPMTCTEnrollFormExists() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            return false;
        }
        return true;
    }

}
