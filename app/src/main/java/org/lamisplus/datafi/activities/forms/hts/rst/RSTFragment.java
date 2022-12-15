package org.lamisplus.datafi.activities.forms.hts.rst;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskAssessment;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.models.RstRiskAssessment;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;

public class RSTFragment extends LamisBaseFragment<RSTContract.Presenter> implements RSTContract.View, View.OnClickListener {

    private String[] settings, modality, targetGroup;
    private AutoCompleteTextView autoentryPoint;
    private AutoCompleteTextView autoSettings;
    private AutoCompleteTextView autoModality;
    private AutoCompleteTextView autoTargetGroup;
    //    private EditText edDob;
//    private EditText edAge;
    private EditText edVisitDate;

    private AutoCompleteTextView autolastHivTestBasedOnRequest;
    private AutoCompleteTextView autolastHivTestDone;
    private AutoCompleteTextView autowhatWasTheResult;
    private AutoCompleteTextView autolastHivTestVaginalOral;
    private AutoCompleteTextView autolastHivTestBloodTransfusion;
    private AutoCompleteTextView autolastHivTestPainfulUrination;
    private AutoCompleteTextView autodiagnosedWithTb;
    private AutoCompleteTextView autolastHivTestInjectedDrugs;
    private AutoCompleteTextView autolastHivTestHadAnal;
    private AutoCompleteTextView autolastHivTestForceToHaveSex;

    private LinearLayout riskAssessmentLayoutView;

    private boolean isEligible = false;
    private boolean isAdult = false;

    private Button mSaveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private RiskStratification updatedRst;

    private String packageName;

    private Person person;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rst, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            person = PersonDAO.findPersonById(mPresenter.getPatientId());
            if (DateUtils.getAgeFromBirthdateString(person.getDateOfBirth()) >= ApplicationConstants.RST_AGE) {
                isAdult = true;
                riskAssessmentLayoutView.setVisibility(View.VISIBLE);
            }
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    public static RSTFragment newInstance() {
        return new RSTFragment();
    }

    private void initiateFragmentViews(View root) {
        autoentryPoint = root.findViewById(R.id.autoentryPoint);
        autoSettings = root.findViewById(R.id.autoSettings);
        autoModality = root.findViewById(R.id.autoModality);
        autoTargetGroup = root.findViewById(R.id.autoTargetGroup);
        edVisitDate = root.findViewById(R.id.edVisitDate);

        autolastHivTestBasedOnRequest = root.findViewById(R.id.autolastHivTestBasedOnRequest);
        autolastHivTestDone = root.findViewById(R.id.autolastHivTestDone);
        autowhatWasTheResult = root.findViewById(R.id.autowhatWasTheResult);
        autolastHivTestVaginalOral = root.findViewById(R.id.autolastHivTestVaginalOral);
        autolastHivTestBloodTransfusion = root.findViewById(R.id.autolastHivTestBloodTransfusion);
        autolastHivTestPainfulUrination = root.findViewById(R.id.autolastHivTestPainfulUrination);
        autodiagnosedWithTb = root.findViewById(R.id.autodiagnosedWithTb);
        autolastHivTestInjectedDrugs = root.findViewById(R.id.autolastHivTestInjectedDrugs);
        autolastHivTestHadAnal = root.findViewById(R.id.autolastHivTestHadAnal);
        autolastHivTestForceToHaveSex = root.findViewById(R.id.autolastHivTestForceToHaveSex);

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

                    ArrayAdapter<String> adapterModality = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allModality);
                    autoModality.setAdapter(adapterModality);
                }
            }
        });

        targetGroup = getResources().getStringArray(R.array.target_group);
        ArrayAdapter<String> adapterTargetGroup = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, targetGroup);
        autoTargetGroup.setAdapter(adapterTargetGroup);


        String[] entryPointDropdown = getResources().getStringArray(R.array.entry_point);
        ArrayAdapter<String> adapterentryPoint = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, entryPointDropdown);
        autoentryPoint.setAdapter(adapterentryPoint);

        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);

        autolastHivTestBasedOnRequest.setAdapter(adapterBooleanAnswers);
        autolastHivTestDone.setAdapter(adapterBooleanAnswers);
        autowhatWasTheResult.setAdapter(adapterBooleanAnswers);
        autolastHivTestVaginalOral.setAdapter(adapterBooleanAnswers);
        autolastHivTestBloodTransfusion.setAdapter(adapterBooleanAnswers);
        autolastHivTestPainfulUrination.setAdapter(adapterBooleanAnswers);
        autodiagnosedWithTb.setAdapter(adapterBooleanAnswers);
        autolastHivTestInjectedDrugs.setAdapter(adapterBooleanAnswers);
        autolastHivTestHadAnal.setAdapter(adapterBooleanAnswers);
        autolastHivTestForceToHaveSex.setAdapter(adapterBooleanAnswers);
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
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edVisitDate.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

    }

    public void fillFields(RiskStratification riskStratification) {
        if (riskStratification != null) {
            isUpdateRst = true;
            updatedRst = riskStratification;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId());

            autoentryPoint.setText(riskStratification.getEntryPoint(), false);
            autoSettings.setText(CodesetsDAO.findCodesetsDisplayByCode(riskStratification.getTestingSetting()), false);
            autoModality.setText(CodesetsDAO.findCodesetsDisplayByCode(riskStratification.getModality()), false);
            autoTargetGroup.setText(CodesetsDAO.findCodesetsDisplayByCode(riskStratification.getTargetGroup()), false);
            edVisitDate.setText(riskStratification.getVisitDate());

            autolastHivTestBasedOnRequest.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getLastHivTestBasedOnRequest()), false);

            autolastHivTestDone.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getLastHivTestDone()), false);

            autowhatWasTheResult.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getWhatWasTheResult()), false);

            autolastHivTestVaginalOral.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getWhatWasTheResult()), false);

            autolastHivTestBloodTransfusion.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getLastHivTestBloodTransfusion()), false);

            autolastHivTestPainfulUrination.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getLastHivTestPainfulUrination()), false);

            autodiagnosedWithTb.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getDiagnosedWithTb()), false);

            autolastHivTestInjectedDrugs.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getLastHivTestInjectedDrugs()), false);

            autolastHivTestHadAnal.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getLastHivTestHadAnal()), false);

            autolastHivTestForceToHaveSex.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getLastHivTestForceToHaveSex()), false);
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

    private RiskStratification updateEncounterWithData(RiskStratification riskStratification) {
        if (!ViewUtils.isEmpty(autoentryPoint)) {
            riskStratification.setEntryPoint(ViewUtils.getInput(autoentryPoint));
        }

        if (!ViewUtils.isEmpty(autoSettings)) {
            riskStratification.setTestingSetting(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoSettings)));
        }

        if (!ViewUtils.isEmpty(autoModality)) {
            riskStratification.setModality(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoModality)));
        }

        if (!ViewUtils.isEmpty(autoTargetGroup)) {
            riskStratification.setTargetGroup(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoTargetGroup)));
        }


        riskStratification.setDob(person.getDateOfBirth());

        riskStratification.setAge(DateUtils.getAgeFromBirthdateString(person.getDateOfBirth()));


        if (!ViewUtils.isEmpty(edVisitDate)) {
            riskStratification.setVisitDate(ViewUtils.getInput(edVisitDate));
        }

        riskStratification.setCode("");

        if (updatedForm != null) {
            if (updatedForm.getPersonId() != 0) {
                riskStratification.setPersonId(updatedForm.getPersonId());
            } else {
                riskStratification.setPersonId(0);
            }
        } else {
            riskStratification.setPersonId(0);
        }

        RstRiskAssessment riskAssessment = new RstRiskAssessment();

        if (!ViewUtils.isEmpty(autolastHivTestBasedOnRequest)) {
            riskAssessment.setLastHivTestBasedOnRequest(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolastHivTestBasedOnRequest)));
        }

        if (!ViewUtils.isEmpty(autolastHivTestDone)) {
            riskAssessment.setLastHivTestDone(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolastHivTestDone)));
        }

        if (!ViewUtils.isEmpty(autowhatWasTheResult)) {
            riskAssessment.setWhatWasTheResult(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autowhatWasTheResult)));
        }

        if (!ViewUtils.isEmpty(autolastHivTestVaginalOral)) {
            riskAssessment.setLastHivTestVaginalOral(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolastHivTestVaginalOral)));
        }

        if (!ViewUtils.isEmpty(autolastHivTestBloodTransfusion)) {
            riskAssessment.setLastHivTestBloodTransfusion(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolastHivTestBloodTransfusion)));
        }

        if (!ViewUtils.isEmpty(autolastHivTestPainfulUrination)) {
            riskAssessment.setLastHivTestPainfulUrination(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolastHivTestPainfulUrination)));
        }

        if (!ViewUtils.isEmpty(autodiagnosedWithTb)) {
            riskAssessment.setDiagnosedWithTb(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autodiagnosedWithTb)));
        }

        if (!ViewUtils.isEmpty(autolastHivTestInjectedDrugs)) {
            riskAssessment.setLastHivTestInjectedDrugs(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolastHivTestInjectedDrugs)));
        }

        if (!ViewUtils.isEmpty(autolastHivTestHadAnal)) {
            riskAssessment.setLastHivTestHadAnal(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolastHivTestHadAnal)));
        }

        if (!ViewUtils.isEmpty(autolastHivTestForceToHaveSex)) {
            riskAssessment.setLastHivTestForceToHaveSex(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolastHivTestForceToHaveSex)));
        }

        riskStratification.setRiskAssessment(riskAssessment);

        return riskStratification;
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
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId());
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
                    mPresenter.confirmUpdate(updateEncounter(updatedRst), updatedForm);
                } else {
                    mPresenter.confirmCreate(createEncounter(), packageName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void startActivityForClientIntakeForm() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            Intent clientIntakeProgram = new Intent(LamisPlus.getInstance(), ClientIntakeActivity.class);
            clientIntakeProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            startActivity(clientIntakeProgram);
        } else {
            startDashboardActivity();
        }
    }

    @Override
    public void startDashboardActivity() {
        Intent preTestProgram = new Intent(LamisPlus.getInstance(), PatientDashboardActivity.class);
        preTestProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(mPresenter.getPatientId()));
        startActivity(preTestProgram);
    }

}
