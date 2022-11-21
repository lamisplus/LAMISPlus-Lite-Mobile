package org.lamisplus.datafi.activities.forms.hts.requestresult;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.RiskAssessment;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RequestResultFragment extends LamisBaseFragment<RequestResultContract.Presenter> implements RequestResultContract.View, View.OnClickListener {

    private String[] settings, modality, targetGroup;
    private AutoCompleteTextView autoSettings;
    private AutoCompleteTextView autoModality;
    private AutoCompleteTextView autoTargetGroup;
    private EditText edDob;
    private EditText edAge;
    private EditText edVisitDate;

    private AutoCompleteTextView autoAbdPain;
    private AutoCompleteTextView autoWithoutCondom;
    private AutoCompleteTextView autoCondomBurst;
    private AutoCompleteTextView autoShareNeedle;
    private AutoCompleteTextView autoBloodTransfusion;
    private AutoCompleteTextView autoCough;
    private AutoCompleteTextView autoPaidSex;

    private LinearLayout riskAssessmentLayoutView;

    private boolean isEligible = false;
    private boolean isAdult = false;

    private Button mSaveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private RiskStratification updatedRst;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rst, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            if(mPresenter.patientToUpdate(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    public static RequestResultFragment newInstance() {
        return new RequestResultFragment();
    }

    private void initiateFragmentViews(View root) {
        autoSettings = root.findViewById(R.id.autoSettings);
        autoModality = root.findViewById(R.id.autoModality);
        autoTargetGroup = root.findViewById(R.id.autoTargetGroup);
        edVisitDate = root.findViewById(R.id.edVisitDate);
        edDob = root.findViewById(R.id.edDateofBirth);
        edAge = root.findViewById(R.id.edAge);

        autoAbdPain = root.findViewById(R.id.autoAbdPain);
        autoWithoutCondom = root.findViewById(R.id.autoWithoutCondom);
        autoCondomBurst = root.findViewById(R.id.autoCondomBurst);
        autoShareNeedle = root.findViewById(R.id.autoShareNeedle);
        autoBloodTransfusion = root.findViewById(R.id.autoBloodTransfusion);
        autoCough = root.findViewById(R.id.autoCough);
        autoPaidSex = root.findViewById(R.id.autoPaidSex);

        riskAssessmentLayoutView = root.findViewById(R.id.riskAssessmentLayout);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        settings = getResources().getStringArray(R.array.settings);
        ArrayAdapter<String> adapterSettings = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, settings);
        autoSettings.setAdapter(adapterSettings);

        autoSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);
                int resourceId = getActivity().getResources().getIdentifier(selection.toLowerCase().replace(" ", "_"), "array", getContext().getPackageName());

                if (resourceId != 0) {
                    String[] allModality = getResources().getStringArray(resourceId);

                    LamisCustomHandler.showJson(allModality);

                    ArrayAdapter<String> adapterModality = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allModality);
                    autoModality.setAdapter(adapterModality);
                }
            }
        });

        targetGroup = getResources().getStringArray(R.array.target_group);
        ArrayAdapter<String> adapterTargetGroup = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, targetGroup);
        autoTargetGroup.setAdapter(adapterTargetGroup);

        String[] booleanAnswers = {"Yes", "No"};
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoAbdPain.setAdapter(adapterBooleanAnswers);
        autoWithoutCondom.setAdapter(adapterBooleanAnswers);
        autoCondomBurst.setAdapter(adapterBooleanAnswers);
        autoShareNeedle.setAdapter(adapterBooleanAnswers);
        autoBloodTransfusion.setAdapter(adapterBooleanAnswers);
        autoCough.setAdapter(adapterBooleanAnswers);
        autoPaidSex.setAdapter(adapterBooleanAnswers);
    }

    private void showDatePickers() {
        edVisitDate.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edVisitDate.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDob.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edDob.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);

                long age = DateUtils.getAgeFromBirthdate(selectedYear, selectedMonth, selectedDay);

                edAge.setText(String.valueOf(age));
                if (age >= ApplicationConstants.RST_AGE) {
                    isAdult = true;
                    riskAssessmentLayoutView.setVisibility(View.VISIBLE);
                }

            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });


        edAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!ViewUtils.isEmpty(edAge) && Integer.parseInt(ViewUtils.getInput(edAge)) >= ApplicationConstants.RST_AGE) {
                    Log.v("Baron", "I have left the field");
                    isAdult = true;
                    riskAssessmentLayoutView.setVisibility(View.VISIBLE);
                } else {
                    isAdult = false;
                    riskAssessmentLayoutView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void fillFields(RiskStratification riskStratification){
        if(riskStratification != null){
            isUpdateRst = true;
            updatedRst = riskStratification;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId());
        }
    }

    private RiskStratification createEncounter() {
        RiskStratification riskStratification = new RiskStratification();
        updateEncounterWithData(riskStratification);
        return riskStratification;
    }

    private RiskStratification updateEncounter(RiskStratification riskStratification) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(riskStratification);
        return riskStratification;
    }

    private RiskStratification updateEncounterWithData(RiskStratification riskStratification){
        if (!ViewUtils.isEmpty(autoSettings)) {
            riskStratification.setTestingSetting(ViewUtils.getInput(autoSettings));
        }

        if (!ViewUtils.isEmpty(autoModality)) {
            riskStratification.setModality(ViewUtils.getInput(autoModality));
        }

        if (!ViewUtils.isEmpty(autoTargetGroup)) {
            riskStratification.setTargetGroup(ViewUtils.getInput(autoTargetGroup));
        }

        if (!ViewUtils.isEmpty(edDob)) {
            riskStratification.setDob(ViewUtils.getInput(edDob));
        }


        if (!ViewUtils.isEmpty(edVisitDate)) {
            riskStratification.setVisitDate(ViewUtils.getInput(edVisitDate));
        }

        if (!ViewUtils.isEmpty(edAge)) {
            riskStratification.setAge(Integer.parseInt(ViewUtils.getInput(edAge)));
        }

        RiskAssessment riskAssessment = new RiskAssessment();
        List<RiskAssessment> riskAssessmentList = new ArrayList<>();
        riskAssessmentList.add(riskAssessment);

        riskStratification.setRiskAssessmentList(riskAssessmentList);

        return riskStratification;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if(isUpdateRst){
                    mPresenter.confirmUpdate(updateEncounter(updatedRst), updatedForm);
                }else{
                    mPresenter.confirmCreate(createEncounter());
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void startActivityForClientIntakeForm() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM, mPresenter.getPatientId());
        if(encounter == null) {
            Intent clientIntakeProgram = new Intent(LamisPlus.getInstance(), ClientIntakeActivity.class);
            clientIntakeProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            startActivity(clientIntakeProgram);
        }else{
            Intent preTestProgram = new Intent(LamisPlus.getInstance(), PatientDashboardActivity.class);
            preTestProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            startActivity(preTestProgram);
        }
    }
}
