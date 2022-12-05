package org.lamisplus.datafi.activities.forms.hts.recency;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientFragment;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;

public class RecencyFragment extends LamisBaseFragment<RecencyContract.Presenter> implements RecencyContract.View, View.OnClickListener {


    private AutoCompleteTextView autoOptOutRTRI;
    private AutoCompleteTextView autoOptOutRTRITestName;
    private EditText edOptOutRTRITestDate;
    private EditText edRencencyId;
    private AutoCompleteTextView autoControlLine;
    private AutoCompleteTextView autoVerififcationLine;
    private AutoCompleteTextView autoLongTermLine;
    private EditText edRencencyInterpretation;
    private EditText edSampleReferanceNumber;
    private AutoCompleteTextView autoSampleType;
    private AutoCompleteTextView autoHasViralLoad;
    private EditText edSampleCollectedDate;
    private EditText edDateSampleSentToPCRLab;
    private EditText edSampleTestDate;
    private EditText edReceivingPcrLab;
    private AutoCompleteTextView autoViralLoadResultClassification;
    private EditText edFinalRecencyResult;

    private Button mSaveContinueButton;

    private boolean isUpdateRecency = false;
    private Encounter updatedForm;
    private Recency updatedRecency;
    private String packageName;

    private LinearLayout viewOtherRecenceOptions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recency, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateRecency) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounterRecency(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static RecencyFragment newInstance() {
        return new RecencyFragment();
    }

    private void initiateFragmentViews(View root) {
        autoOptOutRTRI = root.findViewById(R.id.autoOptOutRTRI);
        autoOptOutRTRITestName = root.findViewById(R.id.autoOptOutRTRITestName);
        edOptOutRTRITestDate = root.findViewById(R.id.edOptOutRTRITestDate);
        edRencencyId = root.findViewById(R.id.edRencencyId);

        autoControlLine = root.findViewById(R.id.autoControlLine);
        autoVerififcationLine = root.findViewById(R.id.autoVerififcationLine);
        autoLongTermLine = root.findViewById(R.id.autoLongTermLine);
        edRencencyInterpretation = root.findViewById(R.id.edRencencyInterpretation);
        autoHasViralLoad = root.findViewById(R.id.autoHasViralLoad);

        edSampleCollectedDate = root.findViewById(R.id.edSampleCollectedDate);
        edSampleReferanceNumber = root.findViewById(R.id.edSampleReferanceNumber);
        autoSampleType = root.findViewById(R.id.autoSampleType);

        edDateSampleSentToPCRLab = root.findViewById(R.id.edDateSampleSentToPCRLab);
        edSampleTestDate = root.findViewById(R.id.edSampleTestDate);
        edReceivingPcrLab = root.findViewById(R.id.edReceivingPcrLab);

        autoViralLoadResultClassification = root.findViewById(R.id.autoViralLoadResultClassification);
        edFinalRecencyResult = root.findViewById(R.id.edFinalRecencyResult);

        viewOtherRecenceOptions = root.findViewById(R.id.viewOtherRecenceOptions);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        String[] booleanYesNo = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanYesNo = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanYesNo);
        autoOptOutRTRI.setAdapter(adapterBooleanYesNo);
        autoControlLine.setAdapter(adapterBooleanYesNo);
        autoVerififcationLine.setAdapter(adapterBooleanYesNo);
        autoLongTermLine.setAdapter(adapterBooleanYesNo);
        autoHasViralLoad.setAdapter(adapterBooleanYesNo);

        String[] testNameOptions = getResources().getStringArray(R.array.test_name);
        ArrayAdapter<String> adapterTestNameOptions = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, testNameOptions);
        autoOptOutRTRITestName.setAdapter(adapterTestNameOptions);

        String[] sampleTypeOptions = getResources().getStringArray(R.array.sample_type);
        ArrayAdapter<String> adaptersampleTypeOptions = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, sampleTypeOptions);
        autoSampleType.setAdapter(adaptersampleTypeOptions);

        String[] viralLoadResultClassOptions = getResources().getStringArray(R.array.viral_load_result_classification);
        ArrayAdapter<String> adapterviralLoadResultClassOptions = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, viralLoadResultClassOptions);
        autoViralLoadResultClassification.setAdapter(adapterviralLoadResultClassOptions);

        autoOptOutRTRI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ViewUtils.getInput(autoOptOutRTRI).equals("No")) {
                    viewOtherRecenceOptions.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void showDatePickers() {
        edOptOutRTRITestDate.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edOptOutRTRITestDate.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edSampleCollectedDate.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edSampleCollectedDate.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateSampleSentToPCRLab.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edDateSampleSentToPCRLab.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edSampleTestDate.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edSampleTestDate.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if (isUpdateRecency) {
                    mPresenter.confirmUpdate(updateEncounter(updatedRecency), updatedForm);
                } else {
                    mPresenter.confirmCreate(createEncounter(), packageName);
                }
                break;
            default:

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void fillFields(Recency recency) {
        if (recency != null) {
            isUpdateRecency = true;
            updatedRecency = recency;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId());

            Log.v("Baron Lamisplus", new Gson().toJson(recency));

            autoOptOutRTRI.setText(StringUtils.changeBooleanToString(recency.getRecencyDetails().getOptOutRTRI()), false);

            if (StringUtils.changeBooleanToString(recency.getRecencyDetails().getOptOutRTRI()).equals("No")) {
                viewOtherRecenceOptions.setVisibility(View.VISIBLE);
            }

            autoOptOutRTRITestName.setText(recency.getRecencyDetails().getOptOutRTRITestName(), false);

            edOptOutRTRITestDate.setText(recency.getRecencyDetails().getOptOutRTRITestDate());

            edRencencyId.setText(recency.getRecencyDetails().getRencencyId());

            autoControlLine.setText(StringUtils.changeBooleanToString(recency.getRecencyDetails().getControlLine()), false);

            autoVerififcationLine.setText(StringUtils.changeBooleanToString(recency.getRecencyDetails().getVerififcationLine()), false);

            autoLongTermLine.setText(StringUtils.changeBooleanToString(recency.getRecencyDetails().getLongTermLine()), false);

            edRencencyInterpretation.setText(recency.getRecencyDetails().getRencencyInterpretation());

            autoHasViralLoad.setText(StringUtils.changeBooleanToString(recency.getRecencyDetails().getHasViralLoad()), false);

            edSampleCollectedDate.setText(recency.getRecencyDetails().getSampleCollectedDate());

            edSampleReferanceNumber.setText(recency.getRecencyDetails().getSampleReferanceNumber());

            autoSampleType.setText(recency.getRecencyDetails().getSampleType(), false);

            edDateSampleSentToPCRLab.setText(recency.getRecencyDetails().getDateSampleSentToPCRLab());

            edSampleTestDate.setText(recency.getRecencyDetails().getSampleTestDate());

            edReceivingPcrLab.setText(recency.getRecencyDetails().getReceivingPcrLab());

            autoViralLoadResultClassification.setText(recency.getRecencyDetails().getViralLoadResultClassification(), false);

            edFinalRecencyResult.setText(recency.getRecencyDetails().getFinalRecencyResult());

        }
    }

    private Recency createEncounter() {
        Recency recency = new Recency();
        updateEncounterWithData(recency);
        return recency;
    }

    private Recency updateEncounter(Recency recency) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(recency);
        return recency;
    }

    private Recency updateEncounterWithData(Recency recency) {
        Recency.RecencyDetails recencyDetails = new Recency.RecencyDetails();

        if (!ViewUtils.isEmpty(autoOptOutRTRI)) {
            recencyDetails.setOptOutRTRI(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoOptOutRTRI)));
        }

        if (!ViewUtils.isEmpty(autoOptOutRTRITestName)) {
            recencyDetails.setOptOutRTRITestName(ViewUtils.getInput(autoOptOutRTRITestName));
        }

        if (!ViewUtils.isEmpty(edOptOutRTRITestDate)) {
            recencyDetails.setOptOutRTRITestDate(ViewUtils.getInput(edOptOutRTRITestDate));
        }

        if (!ViewUtils.isEmpty(edRencencyId)) {
            recencyDetails.setRencencyId(ViewUtils.getInput(edRencencyId));
        }

        if (!ViewUtils.isEmpty(autoControlLine)) {
            recencyDetails.setControlLine(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoControlLine)));
        }

        if (!ViewUtils.isEmpty(autoVerififcationLine)) {
            recencyDetails.setVerififcationLine(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoVerififcationLine)));
        }

        if (!ViewUtils.isEmpty(autoLongTermLine)) {
            recencyDetails.setLongTermLine(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoLongTermLine)));
        }

        if (!ViewUtils.isEmpty(edRencencyInterpretation)) {
            recencyDetails.setRencencyInterpretation(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(edRencencyInterpretation)));
        }

        if (!ViewUtils.isEmpty(autoHasViralLoad)) {
            recencyDetails.setHasViralLoad(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoHasViralLoad)));
        }

        if (!ViewUtils.isEmpty(edSampleCollectedDate)) {
            recencyDetails.setSampleCollectedDate(ViewUtils.getInput(edSampleCollectedDate));
        }

        if (!ViewUtils.isEmpty(edSampleReferanceNumber)) {
            recencyDetails.setSampleReferanceNumber(ViewUtils.getInput(edSampleReferanceNumber));
        }

        if (!ViewUtils.isEmpty(autoSampleType)) {
            recencyDetails.setSampleType(ViewUtils.getInput(autoSampleType));
        }

        if (!ViewUtils.isEmpty(edDateSampleSentToPCRLab)) {
            recencyDetails.setDateSampleSentToPCRLab(ViewUtils.getInput(edDateSampleSentToPCRLab));
        }

        if (!ViewUtils.isEmpty(edSampleTestDate)) {
            recencyDetails.setSampleTestDate(ViewUtils.getInput(edSampleTestDate));
        }

        if (!ViewUtils.isEmpty(edReceivingPcrLab)) {
            recencyDetails.setReceivingPcrLab(ViewUtils.getInput(edReceivingPcrLab));
        }

        if (!ViewUtils.isEmpty(autoViralLoadResultClassification)) {
            recencyDetails.setViralLoadResultClassification(ViewUtils.getInput(autoViralLoadResultClassification));
        }

        if (!ViewUtils.isEmpty(edFinalRecencyResult)) {
            recencyDetails.setFinalRecencyResult(ViewUtils.getInput(edFinalRecencyResult));
        }

        recency.setRecencyDetails(recencyDetails);

        return recency;
    }


    @Override
    public void startActivityForDashboard() {
        Intent DashboardProgram = new Intent(LamisPlus.getInstance(), PatientDashboardActivity.class);
        DashboardProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(mPresenter.getPatientId()));
        startActivity(DashboardProgram);
    }

}
