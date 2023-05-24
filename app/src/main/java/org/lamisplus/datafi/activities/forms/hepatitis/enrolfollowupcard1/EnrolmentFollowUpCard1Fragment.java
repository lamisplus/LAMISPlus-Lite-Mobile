package org.lamisplus.datafi.activities.forms.hepatitis.enrolfollowupcard1;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hepatitis.enrolfollowupcard2.EnrolmentFollowUpCard2Activity;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;

import java.util.Calendar;

public class EnrolmentFollowUpCard1Fragment extends LamisBaseFragment<EnrolmentFollowUpCard1Contract.Presenter> implements EnrolmentFollowUpCard1Contract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private PMTCTEnrollment updatedPmtctEnrollment;
    private AutoCompleteTextView autoSex;
    private AutoCompleteTextView autoOccupation;
    private AutoCompleteTextView autoMaritalStatus;
    private AutoCompleteTextView autoLevelEducation;
    private AutoCompleteTextView autoCareEntryPoint;
    private AutoCompleteTextView autoPregnant;
    private AutoCompleteTextView autoBreastfeeding;
    private AutoCompleteTextView autoHistoryInjectionUse;
    private AutoCompleteTextView autoTreatmentEligible;
    private AutoCompleteTextView autoPMTCT;
    private AutoCompleteTextView autoAscites;
    private AutoCompleteTextView autoHBsAg;
    private AutoCompleteTextView autoHCVAb;
    private AutoCompleteTextView autoHBeAg;
    private AutoCompleteTextView autoAntiHDV;
    private AutoCompleteTextView autoFacilityName;
    private AutoCompleteTextView autoTreatmentExperience;
    private AutoCompleteTextView autoAdverseEvent;
    private AutoCompleteTextView autoAdverseEventReported;
    private AutoCompleteTextView autoTreatmentExperiences;
    private AutoCompleteTextView autoPrescribedDuration;
    private AutoCompleteTextView autoPrescribedDurations;
    private AutoCompleteTextView autoPrescribedDurationHCVTreat;
    private EditText edDateRegistration;
    private EditText eddateFirstPositiveScreening;
    private EditText eddateHBVTest;
    private EditText eddateHBVSample;
    private EditText eddateHBVReported;
    private EditText edstagingDateLiverBiopsy;
    private EditText edDateStarted;
    private EditText edDateStartedNewRegimen;
    private EditText edDateStopped;
    private EditText edDateStartedHCV;
    private EditText edDateCompletedHCV;
    private EditText edNewRegimenHCV;
    private EditText edDateStartedHCVs;
    private EditText edDateCompletedHCVs;
    private EditText edDateSVR12Tested;
    private EditText edDateStartedHCVTreatment;
    private EditText edDateCompletedHCVTreatment;
    private EditText edRetreatmentDateSVR12Tested;

    private Button saveContinueButton;

    private String packageName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_enrolmentfollowupcard1, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            initDropDowns();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

        }
        return root;
    }

    public static EnrolmentFollowUpCard1Fragment newInstance() {
        return new EnrolmentFollowUpCard1Fragment();
    }

    private void initiateFragmentViews(View root) {
        autoSex = root.findViewById(R.id.autoSex);
        autoOccupation = root.findViewById(R.id.autoOccupation);
        autoMaritalStatus = root.findViewById(R.id.autoMaritalStatus);
        autoLevelEducation = root.findViewById(R.id.autoLevelEducation);
        autoCareEntryPoint = root.findViewById(R.id.autoCareEntryPoint);
        autoPregnant = root.findViewById(R.id.autoPregnant);
        autoBreastfeeding = root.findViewById(R.id.autoBreastfeeding);
        autoHistoryInjectionUse = root.findViewById(R.id.autoHistoryInjectionUse);
        autoTreatmentEligible = root.findViewById(R.id.autoTreatmentEligible);
        autoPMTCT = root.findViewById(R.id.autoPMTCT);
        autoAscites = root.findViewById(R.id.autoAscites);
        autoHBsAg = root.findViewById(R.id.autoHBsAg);
        autoHCVAb = root.findViewById(R.id.autoHCVAb);
        autoHBeAg = root.findViewById(R.id.autoHBeAg);
        autoAntiHDV = root.findViewById(R.id.autoAntiHDV);
        autoFacilityName = root.findViewById(R.id.autoFacilityName);
        autoTreatmentExperience = root.findViewById(R.id.autoTreatmentExperience);
        autoAdverseEvent = root.findViewById(R.id.autoAdverseEvent);
        autoAdverseEventReported = root.findViewById(R.id.autoAdverseEventReported);
        autoTreatmentExperiences = root.findViewById(R.id.autoTreatmentExperiences);
        autoPrescribedDuration = root.findViewById(R.id.autoPrescribedDuration);
        autoPrescribedDurations = root.findViewById(R.id.autoPrescribedDurations);
        autoPrescribedDurationHCVTreat = root.findViewById(R.id.autoPrescribedDurationHCVTreat);

        edDateRegistration = root.findViewById(R.id.edDateRegistration);
        eddateFirstPositiveScreening = root.findViewById(R.id.eddateFirstPositiveScreening);
        eddateHBVTest = root.findViewById(R.id.eddateHBVTest);
        eddateHBVSample = root.findViewById(R.id.eddateHBVSample);
        eddateHBVReported = root.findViewById(R.id.eddateHBVReported);
        edstagingDateLiverBiopsy = root.findViewById(R.id.edstagingDateLiverBiopsy);
        edDateStarted = root.findViewById(R.id.edDateStarted);
        edDateStartedNewRegimen = root.findViewById(R.id.edDateStartedNewRegimen);
        edDateStopped = root.findViewById(R.id.edDateStopped);
        edDateStartedHCV = root.findViewById(R.id.edDateStartedHCV);
        edDateCompletedHCV = root.findViewById(R.id.edDateCompletedHCV);
        edNewRegimenHCV = root.findViewById(R.id.edNewRegimenHCV);
        edDateStartedHCVs = root.findViewById(R.id.edDateStartedHCVs);
        edDateCompletedHCVs = root.findViewById(R.id.edDateCompletedHCVs);
        edDateSVR12Tested = root.findViewById(R.id.edDateSVR12Tested);
        edDateStartedHCVTreatment = root.findViewById(R.id.edDateStartedHCVTreatment);
        edDateCompletedHCVTreatment = root.findViewById(R.id.edDateCompletedHCVTreatment);
        edRetreatmentDateSVR12Tested = root.findViewById(R.id.edRetreatmentDateSVR12Tested);

        saveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    public void initDropDowns() {
        String[] facilityName = {"Abuja National Hospital", "Sabo GH Kaduna", "General Hospital Ankpa", "General Hospital Dekina", "FMC Gombe"};
        ArrayAdapter<String> adapterFacilityName = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, facilityName);
        autoFacilityName.setAdapter(adapterFacilityName);

        String[] gender = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, gender);
        autoSex.setAdapter(adapterGender);

        String[] maritalStatus = getResources().getStringArray(R.array.marital_status);
        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, maritalStatus);
        autoMaritalStatus.setAdapter(adapterMaritalStatus);

        String[] occupationStatus = getResources().getStringArray(R.array.occupation);
        ArrayAdapter<String> adapterOccupation = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, occupationStatus);
        autoOccupation.setAdapter(adapterOccupation);

        String[] educationalLevel = getResources().getStringArray(R.array.educational_level);
        ArrayAdapter<String> adapterEducationalLevel = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, educationalLevel);
        autoLevelEducation.setAdapter(adapterEducationalLevel);

        String[] entryLevel = getResources().getStringArray(R.array.care_entry_point);
        ArrayAdapter<String> adapterEntryLevel = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, entryLevel);
        autoCareEntryPoint.setAdapter(adapterEntryLevel);

        String[] prescribedDuration = {"8 weeks", "12 weeks", "24 weeks"};
        ArrayAdapter<String> adapterPrescribedDuration = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, prescribedDuration);
        autoPrescribedDuration.setAdapter(adapterPrescribedDuration);
        autoPrescribedDurations.setAdapter(adapterPrescribedDuration);
        autoPrescribedDurationHCVTreat.setAdapter(adapterPrescribedDuration);

        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoPregnant.setAdapter(adapterBooleanAnswers);
        autoHistoryInjectionUse.setAdapter(adapterBooleanAnswers);
        autoBreastfeeding.setAdapter(adapterBooleanAnswers);
        autoTreatmentEligible.setAdapter(adapterBooleanAnswers);
        autoPMTCT.setAdapter(adapterBooleanAnswers);
        autoAscites.setAdapter(adapterBooleanAnswers);
        autoTreatmentExperience.setAdapter(adapterBooleanAnswers);
        autoAdverseEvent.setAdapter(adapterBooleanAnswers);
        autoAdverseEventReported.setAdapter(adapterBooleanAnswers);
        autoTreatmentExperiences.setAdapter(adapterBooleanAnswers);

        String[] booleanReactiveNonreactive = getResources().getStringArray(R.array.reactive_non_reactive);
        ArrayAdapter<String> adapterReactiveNonReactive = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanReactiveNonreactive);
        autoHBsAg.setAdapter(adapterReactiveNonReactive);
        autoHCVAb.setAdapter(adapterReactiveNonReactive);
        autoHBeAg.setAdapter(adapterReactiveNonReactive);
        autoAntiHDV.setAdapter(adapterReactiveNonReactive);
    }

    private void setListeners() {
        saveContinueButton.setOnClickListener(this);

    }

    private void showDatePickers() {
        edDateRegistration.setOnClickListener(v -> {
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
                edDateRegistration.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        eddateFirstPositiveScreening.setOnClickListener(v -> {
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
                eddateFirstPositiveScreening.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        eddateHBVTest.setOnClickListener(v -> {
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
                eddateHBVTest.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        eddateHBVSample.setOnClickListener(v -> {
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
                eddateHBVSample.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        eddateHBVReported.setOnClickListener(v -> {
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
                eddateHBVReported.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edstagingDateLiverBiopsy.setOnClickListener(v -> {
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
                edstagingDateLiverBiopsy.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateStarted.setOnClickListener(v -> {
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
                edDateStarted.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateStartedNewRegimen.setOnClickListener(v -> {
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
                edDateStartedNewRegimen.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateStopped.setOnClickListener(v -> {
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
                edDateStopped.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateStartedHCV.setOnClickListener(v -> {
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
                edDateStartedHCV.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateCompletedHCV.setOnClickListener(v -> {
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
                edDateCompletedHCV.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edNewRegimenHCV.setOnClickListener(v -> {
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
                edNewRegimenHCV.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateStartedHCVs.setOnClickListener(v -> {
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
                edDateStartedHCVs.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateCompletedHCVs.setOnClickListener(v -> {
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
                edDateCompletedHCVs.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateSVR12Tested.setOnClickListener(v -> {
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
                edDateSVR12Tested.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateStartedHCVTreatment.setOnClickListener(v -> {
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
                edDateStartedHCVTreatment.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateCompletedHCVTreatment.setOnClickListener(v -> {
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
                edDateCompletedHCVTreatment.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edRetreatmentDateSVR12Tested.setOnClickListener(v -> {
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
                edRetreatmentDateSVR12Tested.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });


    }


    public void fillFields(PMTCTEnrollment pmtctEnrollment) {
        if (pmtctEnrollment != null) {
            isUpdateRst = true;
            updatedPmtctEnrollment = pmtctEnrollment;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId());

        }
    }

    private PMTCTEnrollment createEncounter() {
        PMTCTEnrollment pmtctEnrollment = new PMTCTEnrollment();
        updateEncounterWithData(pmtctEnrollment);
        return pmtctEnrollment;
    }

    private PMTCTEnrollment updateEncounter(PMTCTEnrollment pmtctEnrollment) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(pmtctEnrollment);
        return pmtctEnrollment;
    }

    private PMTCTEnrollment updateEncounterWithData(PMTCTEnrollment pmtctEnrollment) {


        return pmtctEnrollment;
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
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId());
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
                Intent i = new Intent(getContext(), EnrolmentFollowUpCard2Activity.class);
                startActivity(i);
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
