package org.lamisplus.datafi.activities.pmtct.pmtctservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import org.lamisplus.datafi.activities.forms.pmtct.pmtctenrollment.PMTCTEnrollmentActivity;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.List;

public class PMTCTServicesFragment extends LamisBaseFragment<PMTCTServicesContract.Presenter> implements PMTCTServicesContract.View, View.OnClickListener {

    private LinearLayout lvAncFormView;
    private LinearLayout lvPmtctEnrollmentFormView;
    private LinearLayout lvLabourDeliveryFormView;
    private LinearLayout lvInfantInformationFormView;

    private TextView tvAnc;
    private TextView tvPmtctEnrollmentForm;
    private TextView tvLabourDeliveryForm;
    private TextView tvInfantInformationForm;


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

        tvAnc = root.findViewById(R.id.tvAnc);
        tvPmtctEnrollmentForm = root.findViewById(R.id.tvPmtctEnrollmentForm);
        tvLabourDeliveryForm = root.findViewById(R.id.tvLabourDeliveryForm);
        tvInfantInformationForm = root.findViewById(R.id.tvInfantInformationForm);
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
//                tvrequestresult.setTextColor(getResources().getColor(R.color.black));
//            } else if (encounter.getName().equals(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM)) {
//                tvposttest.setTextColor(getResources().getColor(R.color.black));
//            } else if (encounter.getName().equals(ApplicationConstants.Forms.HIV_RECENCY_FORM)) {
//                tvrecency.setTextColor(getResources().getColor(R.color.black));
//            } else if (encounter.getName().equals(ApplicationConstants.Forms.ELICITATION)) {
//                tvelicitation.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void setListeners() {
        lvAncFormView.setOnClickListener(this);
        lvPmtctEnrollmentFormView.setOnClickListener(this);
        lvLabourDeliveryFormView.setOnClickListener(this);
        lvInfantInformationFormView.setOnClickListener(this);
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
                    Intent intentClin = new Intent(getActivity(), PMTCTEnrollmentActivity.class);
                    intentClin.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentClin);
                }
                break;
            case R.id.lvLabourDeliveryFormView:
                if (!checkANCFormExists() || !checkPMTCTEnrollFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the ANC && PMTCT Enrollment Forms first before proceeding to this stage");
                } else {
                    Intent intentPreTest = new Intent(getActivity(), LabourDeliveryActivity.class);
                    intentPreTest.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentPreTest);
                }
                break;
            case R.id.lvInfantInformationFormView:
                if (!checkANCFormExists() || !checkPMTCTEnrollFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the ANC && PMTCT Enrollment Forms first before proceeding to this stage");
                } else {
                    Intent intentInfantReg = new Intent(getActivity(), InfantRegistrationActivity.class);
                    intentInfantReg.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentInfantReg);
                }
                break;
            default:

                break;
        }
    }


    private boolean checkANCFormExists() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            return false;
        }
        return true;
    }

    private boolean checkPMTCTEnrollFormExists() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            return false;
        }
        return true;
    }

}
