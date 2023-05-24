package org.lamisplus.datafi.activities.forms.hepatitis.enrolfollowupcard2;

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

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.hepatitis.HepatitisActivity;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.utilities.ApplicationConstants;

import java.util.Calendar;

public class EnrolmentFollowUpCard2Fragment extends LamisBaseFragment<EnrolmentFollowUpCard2Contract.Presenter> implements EnrolmentFollowUpCard2Contract.View, View.OnClickListener {

    private Button saveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private PMTCTEnrollment updatedPmtctEnrollment;
    private EditText edDateVisit;
    private EditText edNextAppointment;
    private AutoCompleteTextView autoHBsAg;
    private AutoCompleteTextView autoEncephalopathy;
    private AutoCompleteTextView autoOutcome;
    private AutoCompleteTextView autoAscites;
    private String packageName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_enrolmentfollowupcard2, container, false);
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

    public static EnrolmentFollowUpCard2Fragment newInstance() {
        return new EnrolmentFollowUpCard2Fragment();
    }

    private void initiateFragmentViews(View root) {
        autoHBsAg = root.findViewById(R.id.autoHBsAg);
        autoEncephalopathy = root.findViewById(R.id.autoEncephalopathy);
        autoOutcome = root.findViewById(R.id.autoOutcome);
        autoAscites = root.findViewById(R.id.autoAscites);

        edDateVisit = root.findViewById(R.id.edDateVisit);
        edNextAppointment = root.findViewById(R.id.edNextAppointment);

        saveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    public void initDropDowns() {
        String[] booleanReactiveNonreactive = getResources().getStringArray(R.array.reactive_non_reactive);
        ArrayAdapter<String> adapterReactiveNonReactive = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanReactiveNonreactive);
        autoHBsAg.setAdapter(adapterReactiveNonReactive);

        String[] encephalopathy = {"0", "1", "2", "3", "4"};
        ArrayAdapter<String> adapterEncephalopathy = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, encephalopathy);
        autoEncephalopathy.setAdapter(adapterEncephalopathy);

        String[] outcome = {"Lost to follow up", "Death", "Cured", "Referred", "Undetectable Viral load"};
        ArrayAdapter<String> adapterOutcome = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, outcome);
        autoOutcome.setAdapter(adapterOutcome);


        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoAscites.setAdapter(adapterBooleanAnswers);
    }

    private void setListeners() {
        saveContinueButton.setOnClickListener(this);
    }

    private void showDatePickers() {
        edDateVisit.setOnClickListener(v -> {
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
                edDateVisit.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edNextAppointment.setOnClickListener(v -> {
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
                edNextAppointment.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
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
                Intent intentHepatitis = new Intent(getActivity(), HepatitisActivity.class);
                startActivity(intentHepatitis);
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
