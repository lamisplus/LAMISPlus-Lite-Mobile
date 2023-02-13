package org.lamisplus.datafi.activities.hts.htsservices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.forms.hts.elicitation.ElicitationActivity;
import org.lamisplus.datafi.activities.forms.hts.posttest.PostTestActivity;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestActivity;
import org.lamisplus.datafi.activities.forms.hts.recency.RecencyActivity;
import org.lamisplus.datafi.activities.forms.hts.requestresult.RequestResultActivity;
import org.lamisplus.datafi.activities.forms.hts.rst.RSTActivity;
import org.lamisplus.datafi.activities.hts.htsprogram.HTSProgramActivity;
import org.lamisplus.datafi.activities.patientprofile.PatientProfileActivity;
import org.lamisplus.datafi.activities.preferences.PrefrencesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Elicitation;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PostTest;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.models.RequestResult;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ImageUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.List;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class HTSServicesFragment extends LamisBaseFragment<HTSServicesContract.Presenter> implements HTSServicesContract.View, View.OnClickListener {

    private LinearLayout lvrstFormView;
    private LinearLayout lvclientIntakeFormView;
    private LinearLayout lvpreTestFormView;
    private LinearLayout lvrequestResultFormView;
    private LinearLayout lvpostTestFormView;
    private LinearLayout lvrecencyFormView;
    private LinearLayout lvelicitationFormView;

    private TextView tvrst;
    private TextView tvclientIntake;
    private TextView tvPreTestForm;
    private TextView tvrequestresult;
    private TextView tvposttest;
    private TextView tvrecency;
    private TextView tvelicitation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hts_services, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            checkExistingEnteredForms();
        }
        return root;
    }

    public static HTSServicesFragment newInstance() {
        return new HTSServicesFragment();
    }

    private void initiateFragmentViews(View root) {
        lvrstFormView = root.findViewById(R.id.lvrstFormView);
        lvclientIntakeFormView = root.findViewById(R.id.lvclientIntakeFormView);
        lvpreTestFormView = root.findViewById(R.id.lvpreTestFormView);
        lvrequestResultFormView = root.findViewById(R.id.lvrequestResultFormView);
        lvpostTestFormView = root.findViewById(R.id.lvpostTestFormView);
        lvrecencyFormView = root.findViewById(R.id.lvrecencyFormView);
        lvelicitationFormView = root.findViewById(R.id.lvelicitationFormView);

        tvrst = root.findViewById(R.id.tvrst);
        tvclientIntake = root.findViewById(R.id.tvclientIntake);
        tvPreTestForm = root.findViewById(R.id.tvPreTestForm);
        tvrequestresult = root.findViewById(R.id.tvrequestresult);
        tvposttest = root.findViewById(R.id.tvposttest);
        tvrecency = root.findViewById(R.id.tvrecency);
        tvelicitation = root.findViewById(R.id.tvelicitation);
    }

    private void checkExistingEnteredForms() {
        List<Encounter> encounterList = EncounterDAO.findAllEncounterForPatient(mPresenter.getPatientId());
        for (Encounter encounter : encounterList) {
            if (encounter.getName().equals(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM)) {
                tvrst.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.CLIENT_INTAKE_FORM)) {
                tvclientIntake.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM)) {
                tvPreTestForm.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.REQUEST_RESULT_FORM)) {
                tvrequestresult.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM)) {
                tvposttest.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.HIV_RECENCY_FORM)) {
                tvrecency.setTextColor(getResources().getColor(R.color.black));
            } else if (encounter.getName().equals(ApplicationConstants.Forms.ELICITATION)) {
                tvelicitation.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void setListeners() {
        lvrstFormView.setOnClickListener(this);
        lvclientIntakeFormView.setOnClickListener(this);
        lvpreTestFormView.setOnClickListener(this);
        lvrequestResultFormView.setOnClickListener(this);
        lvpostTestFormView.setOnClickListener(this);
        lvrecencyFormView.setOnClickListener(this);
        lvelicitationFormView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvrstFormView:
                Intent intentRst = new Intent(getActivity(), RSTActivity.class);
                intentRst.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                startActivity(intentRst);
                break;
            case R.id.lvclientIntakeFormView:
                if (!checkRSTFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the RST Form first");
                } else {
                    Intent intentClin = new Intent(getActivity(), ClientIntakeActivity.class);
                    intentClin.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentClin);
                }
                break;
            case R.id.lvpreTestFormView:
                if (!checkRSTFormExists() || !checkClientIntakeFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the RST && Client Intake Form first before proceeding to this stage");
                } else {
                    Intent intentPreTest = new Intent(getActivity(), PreTestActivity.class);
                    intentPreTest.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentPreTest);
                }
                break;
            case R.id.lvrequestResultFormView:
                if (!checkRSTFormExists() || !checkClientIntakeFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the RST && Client Intake Form first before proceeding to this stage");
                } else {
                    Intent intentRequestResult = new Intent(getActivity(), RequestResultActivity.class);
                    intentRequestResult.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentRequestResult);
                }
                break;
            case R.id.lvpostTestFormView:
                if (!checkRSTFormExists() || !checkClientIntakeFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the RST && Client Intake Form first before proceeding to this stage");
                } else {
                    Intent intentPostTest = new Intent(getActivity(), PostTestActivity.class);
                    intentPostTest.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentPostTest);
                }
                break;
            case R.id.lvrecencyFormView:
                if (!checkRSTFormExists() || !checkClientIntakeFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the RST && Client Intake Form first before proceeding to this stage");
                } else {
                    Intent intentRecency = new Intent(getActivity(), RecencyActivity.class);
                    intentRecency.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentRecency);
                }
                break;
            case R.id.lvelicitationFormView:
                if (!checkRSTFormExists() || !checkClientIntakeFormExists()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the RST && Client Intake Form first before proceeding to this stage");
                } else {
                    Intent intentEliciation = new Intent(getActivity(), ElicitationActivity.class);
                    intentEliciation.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mPresenter.getPatientId());
                    startActivity(intentEliciation);
                }
                break;
            default:

                break;
        }
    }


    private boolean checkRSTFormExists() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            return false;
        }
        return true;
    }

    private boolean checkClientIntakeFormExists() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            return false;
        }
        return true;
    }

}
