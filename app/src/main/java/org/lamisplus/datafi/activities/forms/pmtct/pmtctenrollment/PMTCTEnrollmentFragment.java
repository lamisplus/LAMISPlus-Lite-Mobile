package org.lamisplus.datafi.activities.forms.pmtct.pmtctenrollment;

import android.app.DatePickerDialog;
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
import org.lamisplus.datafi.activities.forms.pmtct.anc.ANCFragment;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.activities.pmtct.pmtctservices.PMTCTServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;

public class PMTCTEnrollmentFragment extends LamisBaseFragment<PMTCTEnrollmentContract.Presenter> implements PMTCTEnrollmentContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private PMTCTEnrollment updatedPmtctEnrollment;
    private EditText edancNo;
    private EditText edDateEnrollment;
    private AutoCompleteTextView autoEntryPoint;
    private EditText edGravida;
    private EditText edArtStartDate;
    private EditText edGaWeeks;
    private AutoCompleteTextView autoTimingARTInitiation;
    private AutoCompleteTextView autoTBStatus;
    private TextInputLayout dateEnrollmentTIL;
    private TextInputLayout autoEntryPointTIL;
    private TextInputLayout artStartDateTIL;
    private TextInputLayout autoTimingARTInitiationTIL;
    private TextInputLayout autoTBStatusTIL;

    private String packageName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pmtct_enrollment, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            initDropDownsPatients();
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId()));
            }
            autoPopulateFieldsFromANC();
        }
        return root;
    }

    public static PMTCTEnrollmentFragment newInstance() {
        return new PMTCTEnrollmentFragment();
    }

    private void initiateFragmentViews(View root) {
        edancNo = root.findViewById(R.id.edancNo);
        edDateEnrollment = root.findViewById(R.id.edDateEnrollment);
        autoEntryPoint = root.findViewById(R.id.autoEntryPoint);
        edGravida = root.findViewById(R.id.edGravida);
        edArtStartDate = root.findViewById(R.id.edArtStartDate);
        autoTimingARTInitiation = root.findViewById(R.id.autoTimingARTInitiation);
        autoTBStatus = root.findViewById(R.id.autoTBStatus);
        edGaWeeks = root.findViewById(R.id.edGaWeeks);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);

        dateEnrollmentTIL = root.findViewById(R.id.dateEnrollmentTIL);
        autoEntryPointTIL = root.findViewById(R.id.autoEntryPointTIL);
        artStartDateTIL = root.findViewById(R.id.artStartDateTIL);
        autoTimingARTInitiationTIL = root.findViewById(R.id.autoTimingARTInitiationTIL);
        autoTBStatusTIL = root.findViewById(R.id.autoTBStatusTIL);
    }

    public void initDropDownsPatients() {
        String[] entryPoint = getResources().getStringArray(R.array.pmtct_point_of_entry);
        ArrayAdapter<String> adapterEntryPoint = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, entryPoint);
        autoEntryPoint.setAdapter(adapterEntryPoint);

        String[] timingArt = getResources().getStringArray(R.array.timing_art_initiation);
        ArrayAdapter<String> adapterTimingArt = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, timingArt);
        autoTimingARTInitiation.setAdapter(adapterTimingArt);

        String[] tbStatus = getResources().getStringArray(R.array.tb_status);
        ArrayAdapter<String> adapterTbStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, tbStatus);
        autoTBStatus.setAdapter(adapterTbStatus);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);
    }

    private void showDatePickers() {
        edDateEnrollment.setOnClickListener(v -> {
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
                edDateEnrollment.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edArtStartDate.setOnClickListener(v -> {
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
                edArtStartDate.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });
    }

    private void autoPopulateFieldsFromANC() {
        ANC anc = EncounterDAO.findAncFromForm(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
        if (anc != null) {
            edancNo.setText(anc.getAncNo());
            edGravida.setText(anc.getGravida());
            edGaWeeks.setText(anc.getGaweeks());
        }
    }


    public void fillFields(PMTCTEnrollment pmtctEnrollment) {
        if (pmtctEnrollment != null) {
            isUpdateRst = true;
            updatedPmtctEnrollment = pmtctEnrollment;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM, mPresenter.getPatientId());

            edancNo.setText(pmtctEnrollment.getAncNo());

            edDateEnrollment.setText(pmtctEnrollment.getPmtctEnrollmentDate());

            edGravida.setText(pmtctEnrollment.getGravida());

            edGaWeeks.setText(pmtctEnrollment.getGaweeks());

            edArtStartDate.setText(pmtctEnrollment.getArtStartDate());

            if(CodesetsDAO.findCodesetsDisplayByCode(pmtctEnrollment.getEntryPoint()) != null){
                autoEntryPoint.setText(CodesetsDAO.findCodesetsDisplayByCode(pmtctEnrollment.getEntryPoint()), false);
            }

            if(CodesetsDAO.findCodesetsDisplayByCode(pmtctEnrollment.getArtStartTime()) != null){
                autoTimingARTInitiation.setText(CodesetsDAO.findCodesetsDisplayByCode(pmtctEnrollment.getArtStartTime()), false);
            }

            if(CodesetsDAO.findCodesetsDisplayByCode(pmtctEnrollment.getTbStatus()) != null){
                autoTBStatus.setText(CodesetsDAO.findCodesetsDisplayByCode(pmtctEnrollment.getTbStatus()), false);
            }

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
        if (!ViewUtils.isEmpty(edancNo)) {
            pmtctEnrollment.setAncNo(ViewUtils.getInput(edancNo));
        }

        if (!ViewUtils.isEmpty(edDateEnrollment)) {
            pmtctEnrollment.setPmtctEnrollmentDate(ViewUtils.getInput(edDateEnrollment));
        }

        if (!ViewUtils.isEmpty(autoEntryPoint)) {
            pmtctEnrollment.setEntryPoint(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoEntryPoint)));
        }

        if (!ViewUtils.isEmpty(edGravida)) {
            pmtctEnrollment.setGravida(ViewUtils.getInput(edGravida));
        }

        if (!ViewUtils.isEmpty(edArtStartDate)) {
            pmtctEnrollment.setArtStartDate(ViewUtils.getInput(edArtStartDate));
        }

        if (!ViewUtils.isEmpty(autoTimingARTInitiation)) {
            pmtctEnrollment.setArtStartTime(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoTimingARTInitiation)));
        }

        if (!ViewUtils.isEmpty(autoTBStatus)) {
            pmtctEnrollment.setTbStatus(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoTBStatus)));
        }

        if (!ViewUtils.isEmpty(edGaWeeks)) {
            pmtctEnrollment.setGaweeks(ViewUtils.getInput(edGaWeeks));
        }

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
                if (isUpdateRst) {
                    mPresenter.confirmUpdate(updateEncounter(updatedPmtctEnrollment), updatedForm);
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
    public void setErrorsVisibility(boolean dateEnrollment, boolean entryPoint, boolean artStartDate, boolean timingARTInitiation, boolean tBStatus) {
        if (dateEnrollment) {
            dateEnrollmentTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            dateEnrollmentTIL.setError("This field is required");
        }

        if (entryPoint) {
            autoEntryPointTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoEntryPointTIL.setError("This field is required");
        }

        if (artStartDate) {
            artStartDateTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            artStartDateTIL.setError("This field is required");
        }

        if (timingARTInitiation) {
            autoTimingARTInitiationTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoTimingARTInitiationTIL.setError("This field is required");
        }

        if (tBStatus) {
            autoTBStatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoTBStatusTIL.setError("This field is required");
        }

    }

}
