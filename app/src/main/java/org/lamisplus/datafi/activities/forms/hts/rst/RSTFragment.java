package org.lamisplus.datafi.activities.forms.hts.rst;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputLayout;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientFragment;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Codesets;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskAssessment;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.models.RstRiskAssessment;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;

public class RSTFragment extends LamisBaseFragment<RSTContract.Presenter> implements RSTContract.View, View.OnClickListener {

    private String[] settings, modality, targetGroup;
    private AutoCompleteTextView autoentryPoint;
    private AutoCompleteTextView autocommunityEntryPoint;
    private AutoCompleteTextView autoSettings;
    private AutoCompleteTextView autoModality;
    private AutoCompleteTextView autoTargetGroup;
    //    private EditText edDob;
    private EditText edAge;
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
    private LinearLayout autocommunityEntryPointLayout;
    private RadioGroup rpDateofBirth;
    private EditText edDateofBirth;
    private TextInputLayout edDateofBirthTIL;
    private TextInputLayout labelAge;

    private boolean isEligible = false;
    private boolean isAdult = false;

    private Button mSaveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private RiskStratification updatedRst;

    private String packageName;

    private Person person;

    private TextInputLayout autoentryPointTIL;
    private TextInputLayout autocommunityEntryPointTIL;
    private TextInputLayout autoSettingsTIL;
    private TextInputLayout autoModalityTIL;
    private TextInputLayout edVisitDateTIL;
    private TextInputLayout autoTargetGroupTIL;
    private TextInputLayout autolastHivTestBasedOnRequestTIL;
    private TextInputLayout autowhatWasTheResultTIL;

    private LinearLayout riskAssessmentLayout;

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
            if (mPresenter.getPatientId() != null && !mPresenter.getPatientId().equals("")) {
                person = PersonDAO.findPersonById(mPresenter.getPatientId());
            }
            if (person != null) {
                if (DateUtils.getAgeFromBirthdateString(person.getDateOfBirth()) >= ApplicationConstants.RST_AGE) {
                    isAdult = true;
                    riskAssessmentLayout.setVisibility(View.VISIBLE);
                }
            }
            if (mPresenter.getPatientId() != null && !mPresenter.getPatientId().equals("")) {
                RiskStratification rst = mPresenter.patientToUpdate(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId());
                if (rst != null) {
                    fillFields(rst);
                    hideDependentFields(rst);
                }
            }
        }
        return root;
    }

    public static RSTFragment newInstance() {
        return new RSTFragment();
    }

    private void initiateFragmentViews(View root) {
        autoentryPoint = root.findViewById(R.id.autoentryPoint);
        autocommunityEntryPoint = root.findViewById(R.id.autocommunityEntryPoint);
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

        riskAssessmentLayout = root.findViewById(R.id.riskAssessmentLayout);
        autocommunityEntryPointLayout = root.findViewById(R.id.autocommunityEntryPointLayout);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
        edAge = root.findViewById(R.id.edAge);
        rpDateofBirth = root.findViewById(R.id.rgdateOfBirthSelect);
        edDateofBirthTIL = root.findViewById(R.id.edDateofBirthTIL);
        labelAge = root.findViewById(R.id.labelAge);

        edDateofBirth = root.findViewById(R.id.edDateofBirth);
        autoentryPointTIL = root.findViewById(R.id.autoentryPointTIL);
        autocommunityEntryPointTIL = root.findViewById(R.id.autocommunityEntryPointTIL);
        autoSettingsTIL = root.findViewById(R.id.autoSettingsTIL);
        autoModalityTIL = root.findViewById(R.id.autoModalityTIL);
        edVisitDateTIL = root.findViewById(R.id.edVisitDateTIL);
        autoTargetGroupTIL = root.findViewById(R.id.autoTargetGroupTIL);
        autolastHivTestBasedOnRequestTIL = root.findViewById(R.id.autolastHivTestBasedOnRequestTIL);
        autowhatWasTheResultTIL = root.findViewById(R.id.autowhatWasTheResultTIL);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        String[] communityEntry = getResources().getStringArray(R.array.community_entry_point);
        ArrayAdapter<String> adapterCommunityEntryPoint = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, communityEntry);
        autocommunityEntryPoint.setAdapter(adapterCommunityEntryPoint);

        autoentryPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (autoentryPoint.getText().toString().equals("Community")) {
                    autocommunityEntryPointLayout.setVisibility(View.VISIBLE);
                } else {
                    autocommunityEntryPointLayout.setVisibility(View.GONE);
                }
            }
        });

        edAge.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strEnteredVal = ViewUtils.getInput(edAge);
                int max_age = 60;
                if (strEnteredVal != null && !strEnteredVal.equals("")) {
                    int num = Integer.parseInt(strEnteredVal);

                    //edDateofBirth.setText(DateUtils.getAgeFromBirthdate(num));

                    if (num < ApplicationConstants.RST_AGE) {
                        riskAssessmentLayout.setVisibility(View.GONE);
                    }
                    if (num > max_age) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getContext());
                        alertDialogBuilder.setTitle("Age Pop up alert");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("You have entered an age greater than " + max_age + " years, Are you Sure of the Age entered?")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                } else {
                    edDateofBirth.setText("");
                }
            }
        });

        String[] lastHivTestDone = getResources().getStringArray(R.array.last_hiv_test_done);
        ArrayAdapter<String> adapterlastHivTestDone = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, lastHivTestDone);
        autolastHivTestDone.setAdapter(adapterlastHivTestDone);


        settings = getResources().getStringArray(R.array.settings);
        ArrayAdapter<String> adapterSettings = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, settings);
        autoSettings.setAdapter(adapterSettings);

        autoSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);
                int resourceId = getActivity().getResources().getIdentifier(selection.toLowerCase().replace(" ", "_"), "array", getContext().getPackageName());

                if (resourceId != 0) {
                    //When any field is selected first clear the autocomplete input field
                    autoModality.setText("");
                    String[] allModality = getResources().getStringArray(resourceId);

                    ArrayAdapter<String> adapterModality = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allModality);
                    autoModality.setAdapter(adapterModality);
                }
            }
        });

        autolastHivTestBasedOnRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!StringUtils.isBlank(ViewUtils.getInput(edAge))) {
                    int age = Integer.parseInt(ViewUtils.getInput(edAge));
                    if (autolastHivTestBasedOnRequest.getText().toString().equals("Yes")) {
                        riskAssessmentLayout.setVisibility(View.GONE);
                    } else {
                        if (age >= ApplicationConstants.RST_AGE) {
                            riskAssessmentLayout.setVisibility(View.VISIBLE);
                        } else {
                            riskAssessmentLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        autolastHivTestDone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (autolastHivTestDone.getText().toString().equals("Never")) {
                    autowhatWasTheResultTIL.setVisibility(View.GONE);
                } else {
                    autowhatWasTheResultTIL.setVisibility(View.VISIBLE);
                }
            }
        });

        rpDateofBirth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                if (index == 0) {
                    edDateofBirthTIL.setVisibility(View.VISIBLE);
                    labelAge.setVisibility(View.GONE);
                } else {
                    edDateofBirthTIL.setVisibility(View.GONE);
                    labelAge.setVisibility(View.VISIBLE);
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

        String[] posNegAnswers = getResources().getStringArray(R.array.positive_negative);
        ArrayAdapter<String> adapterPosNegAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, posNegAnswers);

        autolastHivTestBasedOnRequest.setAdapter(adapterBooleanAnswers);
        autowhatWasTheResult.setAdapter(adapterPosNegAnswers);
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

            if (person != null) {
                String visitDate = person.getDateOfRegistration();
                String[] explodeDate = visitDate.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);
                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                Long regMilli = bdt.getMillis();
                mDatePicker.getDatePicker().setMinDate(regMilli);
            }

            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateofBirth.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edVisitDate))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTestVisitDate = ViewUtils.getInput(edVisitDate);
                String[] explodeDate = dateTestVisitDate.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);


                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                Long regMillis = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edDateofBirth.setText(selectedYear + "-" + stringMonth + "-" + stringDay);

                    int age = DateUtils.getAgeFromBirthdateString(selectedYear + "-" + stringMonth + "-" + stringDay);
                    if (age >= ApplicationConstants.RST_AGE) {
                        isAdult = true;
                        riskAssessmentLayout.setVisibility(View.VISIBLE);
                    } else {
                        isAdult = false;
                        riskAssessmentLayout.setVisibility(View.GONE);
                    }
                    edAge.setText(age + "");
                }, cYear, cMonth, cDay);
                mDatePicker.getDatePicker().setMaxDate(regMillis);
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Visit Date first");
            }
        });

    }

    private void hideDependentFields(RiskStratification rst) {
        //if()
    }

    public void fillFields(RiskStratification riskStratification) {
        if (riskStratification != null) {
            isUpdateRst = true;
            updatedRst = riskStratification;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId());

            autoentryPoint.setText(riskStratification.getEntryPoint(), false);

            if (riskStratification.getCommunityEntryPoint() != null) {
                autocommunityEntryPoint.setText(CodesetsDAO.findCodesetsDisplayByCode(riskStratification.getCommunityEntryPoint()), false);
            }

            autoSettings.setText(CodesetsDAO.findCodesetsDisplayByCode(riskStratification.getTestingSetting()), false);
            autoModality.setText(CodesetsDAO.findCodesetsDisplayByCode(riskStratification.getModality()), false);
            autoTargetGroup.setText(CodesetsDAO.findCodesetsDisplayByCode(riskStratification.getTargetGroup()), false);
            edVisitDate.setText(riskStratification.getVisitDate());

            edDateofBirth.setText(riskStratification.getDob());

            autolastHivTestBasedOnRequest.setText(StringUtils.changeBooleanToString(riskStratification.getRiskAssessment().getLastHivTestBasedOnRequest()), false);

            if (autolastHivTestBasedOnRequest.getText().toString().equals("Yes")) {
                riskAssessmentLayout.setVisibility(View.GONE);
            } else {
                riskAssessmentLayout.setVisibility(View.VISIBLE);
            }

            autolastHivTestDone.setText(riskStratification.getRiskAssessment().getLastHivTestDone(), false);

            autowhatWasTheResult.setText(riskStratification.getRiskAssessment().getWhatWasTheResult(), false);

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

        if (!ViewUtils.isEmpty(autocommunityEntryPoint)) {
            riskStratification.setCommunityEntryPoint(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoentryPoint)));
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

        int radioButtonID = rpDateofBirth.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) rpDateofBirth.findViewById(radioButtonID);
        String selectedText = (String) radioButton.getText();
        if (!StringUtils.isBlank(ViewUtils.getInput(edDateofBirth)) || !StringUtils.isBlank(ViewUtils.getInput(edAge))) {
            if (selectedText.equals("Actual")) {
                riskStratification.setDob(ViewUtils.getInput(edDateofBirth));
                int age = DateUtils.getAgeFromBirthdateString(ViewUtils.getInput(edDateofBirth));
                riskStratification.setAge(age);
            } else {
                String dateOfBirth = DateUtils.getAgeFromBirthdate(Integer.parseInt(ViewUtils.getInput(edAge)));
                riskStratification.setDob(dateOfBirth);
                riskStratification.setAge(Integer.parseInt(ViewUtils.getInput(edAge)));
            }
        }

        if (!ViewUtils.isEmpty(edVisitDate)) {
            riskStratification.setVisitDate(ViewUtils.getInput(edVisitDate));
        }

        riskStratification.setCode("");

        if (updatedForm != null) {
            if (updatedForm.getPersonId() != null) {
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

        //if this request is based on clinical request then we have a score of one which means client is eligible
        if (!ViewUtils.isEmpty(autolastHivTestBasedOnRequest)) {
            if (ViewUtils.getInput(autolastHivTestBasedOnRequest).equals("No")) {
                riskStratification.setEligible(true);
            } else {
                riskStratification.setEligible(false);
            }
        }

        if (!ViewUtils.isEmpty(autolastHivTestDone)) {
            riskAssessment.setLastHivTestDone(ViewUtils.getInput(autolastHivTestDone));
        }

        if (!ViewUtils.isEmpty(autowhatWasTheResult)) {
            riskAssessment.setWhatWasTheResult(ViewUtils.getInput(autowhatWasTheResult));
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
    public void startActivityForClientIntakeForm(String s, String packageName) {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            Intent clientIntakeProgram = new Intent(LamisPlus.getInstance(), ClientIntakeActivity.class);
            clientIntakeProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            //Save the Risk stratification here and pass it to the Client Intake Form Activity
            clientIntakeProgram.putExtra(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, s);
            clientIntakeProgram.putExtra("packageName", packageName);
            startActivity(clientIntakeProgram);
            getActivity().finish();
        } else {
            startHTSActivity();
        }
    }

    @Override
    public void scrollToTop() {
        ScrollView scrollView = this.getActivity().findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, scrollView.getPaddingTop());
    }

    @Override
    public void startHTSActivity() {
        Intent htsActivity = new Intent(LamisPlus.getInstance(), HTSServicesActivity.class);
        htsActivity.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(mPresenter.getPatientId()));
        startActivity(htsActivity);
        getActivity().finish();
    }


    @Override
    public void setErrorsVisibility(boolean dateOfBirth, boolean entryPoint, boolean settings, boolean modality, boolean visitDate, boolean targetGroup, boolean autolastHivTestBasedOnRequest) {
        if (dateOfBirth) {
            edDateofBirthTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edDateofBirthTIL.setError("Select Date of Birth");
        }

        if (entryPoint) {
            autoentryPointTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoentryPointTIL.setError("Select the Entry Point");
        }

        if (settings) {
            autoSettingsTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoSettingsTIL.setError("Select Settings");
        }

        if (modality) {
            autoModalityTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoModalityTIL.setError("Select Modality");
        }

        if (visitDate) {
            edVisitDateTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edVisitDateTIL.setError("Select Visit Date");
        }

        if (targetGroup) {
            autoTargetGroupTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoTargetGroupTIL.setError("Select the Target Group");
        }

        if (autolastHivTestBasedOnRequest) {
            autolastHivTestBasedOnRequestTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autolastHivTestBasedOnRequestTIL.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

    }

}
