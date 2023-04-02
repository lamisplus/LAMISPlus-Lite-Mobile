package org.lamisplus.datafi.activities.forms.hts.recency;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientFragment;
import org.lamisplus.datafi.activities.forms.hts.elicitation.ElicitationActivity;
import org.lamisplus.datafi.activities.forms.hts.requestresult.RequestResultActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.LabDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Elicitation;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Lab;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
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
    private AutoCompleteTextView autoReceivingPcrLab;
    private AutoCompleteTextView autoViralLoadResultClassification;
    private EditText edFinalRecencyResult;

    private Button mSaveContinueButton;

    private boolean isUpdateRecency = false;
    private Encounter updatedForm;
    private Recency updatedRecency;
    private String packageName;

    private LinearLayout viewOtherRecenceOptions;
    private TextInputLayout autoOptOutRTRITIL;
    private TextInputLayout autoOptOutRTRITestNameTIL;
    private TextInputLayout edOptOutRTRITestDateTIL;
    private TextInputLayout edRencencyIdTIL;
    private TextInputLayout autoControlLineTIL;
    private TextInputLayout autoVerififcationLineTIL;
    private TextInputLayout autoLongTermLineTIL;
    private TextInputLayout autoHasViralLoadTIL;
    private TextInputLayout autoSampleTypeTIL;
    private TextInputLayout edSampleReferanceNumberTIL;
    private LinearLayout viralLoadClassificationLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recency, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            dropDownListeners();
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
        autoReceivingPcrLab = root.findViewById(R.id.autoReceivingPcrLab);

        autoViralLoadResultClassification = root.findViewById(R.id.autoViralLoadResultClassification);
        edFinalRecencyResult = root.findViewById(R.id.edFinalRecencyResult);

        viewOtherRecenceOptions = root.findViewById(R.id.viewOtherRecenceOptions);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);

        autoOptOutRTRITIL = root.findViewById(R.id.autoOptOutRTRITIL);
        autoOptOutRTRITestNameTIL = root.findViewById(R.id.autoOptOutRTRITestNameTIL);
        edOptOutRTRITestDateTIL = root.findViewById(R.id.edOptOutRTRITestDateTIL);
        edRencencyIdTIL = root.findViewById(R.id.edRencencyIdTIL);
        autoControlLineTIL = root.findViewById(R.id.autoControlLineTIL);
        autoVerififcationLineTIL = root.findViewById(R.id.autoVerififcationLineTIL);
        autoLongTermLineTIL = root.findViewById(R.id.autoLongTermLineTIL);
        autoHasViralLoadTIL = root.findViewById(R.id.autoHasViralLoadTIL);
        autoSampleTypeTIL = root.findViewById(R.id.autoSampleTypeTIL);
        edSampleReferanceNumberTIL = root.findViewById(R.id.edSampleReferanceNumberTIL);
        viralLoadClassificationLayout = root.findViewById(R.id.viralLoadClassificationLayout);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        Lab labSelected = LabDAO.getDefaultLab();
        String labName = "";
        if(labSelected != null){
            labName = labSelected.getName();
        }
        autoReceivingPcrLab.setText(labName);

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
                } else {
                    viewOtherRecenceOptions.setVisibility(View.GONE);
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
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edOptOutRTRITestDate.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            Person person = PersonDAO.findPersonById(mPresenter.getPatientId());
            if (person != null) {
                String visitDate = person.getDateOfRegistration();
                String[] explodeDate = visitDate.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);
                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                mDatePicker.getDatePicker().setMinDate(bdt.getMillis());
            }
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edSampleCollectedDate.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edOptOutRTRITestDate))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edOptOutRTRITestDate);
                String[] explodeDate = dateTest1.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                long regMilli = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edSampleCollectedDate.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
                }, cYear, cMonth, cDay);
                mDatePicker.getDatePicker().setMinDate(regMilli);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Test Date First");
            }
        });

        edDateSampleSentToPCRLab.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edOptOutRTRITestDate))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edOptOutRTRITestDate);
                String[] explodeDate = dateTest1.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                long regMilli = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edDateSampleSentToPCRLab.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
                }, cYear, cMonth, cDay);
                mDatePicker.getDatePicker().setMinDate(regMilli);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Test Date First");
            }
        });

        edSampleTestDate.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edOptOutRTRITestDate))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edOptOutRTRITestDate);
                String[] explodeDate = dateTest1.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                long regMilli = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edSampleTestDate.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
                }, cYear, cMonth, cDay);
                mDatePicker.getDatePicker().setMinDate(regMilli);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Test Date First");
            }
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

            autoReceivingPcrLab.setText(recency.getRecencyDetails().getReceivingPcrLab(), false);

            autoViralLoadResultClassification.setText(recency.getRecencyDetails().getViralLoadResultClassification(), false);

            edFinalRecencyResult.setText(recency.getRecencyDetails().getFinalRecencyResult());

            if (recency.getRecencyDetails().getRencencyInterpretation() != null) {
                if (recency.getRecencyDetails().getRencencyInterpretation().equals("Recent")) {
                    autoHasViralLoadTIL.setVisibility(View.VISIBLE);
                    viralLoadClassificationLayout.setVisibility(View.VISIBLE);
                } else {
                    autoHasViralLoadTIL.setVisibility(View.GONE);
                    viralLoadClassificationLayout.setVisibility(View.GONE);
                }
            }
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
            recencyDetails.setRencencyInterpretation(ViewUtils.getInput(edRencencyInterpretation));
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

        if (!ViewUtils.isEmpty(autoReceivingPcrLab)) {
            recencyDetails.setReceivingPcrLab(ViewUtils.getInput(autoReceivingPcrLab));
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
    public void scrollToTop() {
        ScrollView scrollView = this.getActivity().findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, scrollView.getPaddingTop());
    }

    @Override
    public void startActivityForElicitation() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.ELICITATION, mPresenter.getPatientId());
        if (encounter == null) {
            Intent preTestProgram = new Intent(LamisPlus.getInstance(), ElicitationActivity.class);
            preTestProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            startActivity(preTestProgram);
            getActivity().finish();
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
        getActivity().finish();
    }

    @Override
    public void setErrorsVisibility(boolean optOutRTRI, boolean testName, boolean testDate,
                                    boolean rencencyId, boolean controlLine, boolean verififcationLine, boolean longTermLine,
                                    boolean hasViralLoad, boolean sampleReferanceNumber, boolean sampleType) {
        if (optOutRTRI) {
            autoOptOutRTRITIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoOptOutRTRITIL.setError("Select Opt Out of RTRI");
        }
        if (testName) {
            autoOptOutRTRITestNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoOptOutRTRITestNameTIL.setError("Select Test Name");
        }
        if (testDate) {
            edOptOutRTRITestDateTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edOptOutRTRITestDateTIL.setError("Select Test Date");
        }
        if (rencencyId) {
            edRencencyIdTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edRencencyIdTIL.setError("Enter the Recency ID");
        }

        if (controlLine) {
            autoControlLineTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoControlLineTIL.setError("Select Control Line");
        }

        if (verififcationLine) {
            autoVerififcationLineTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoVerififcationLineTIL.setError("Select Verification Line");
        }

        if (longTermLine) {
            autoLongTermLineTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoLongTermLineTIL.setError("Select Long Term Line");
        }

        if (hasViralLoad) {
            autoHasViralLoadTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoHasViralLoadTIL.setError("Select Has Viral Load");
        }

        if (sampleType) {
            autoSampleTypeTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoSampleTypeTIL.setError("Select Sample Type");
        }

        if (sampleType) {
            edSampleReferanceNumberTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edSampleReferanceNumberTIL.setError("Enter the Sample Reference Number");
        }
    }

    public void dropDownListeners() {
        autoHasViralLoad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoHasViralLoad.getText().toString().equals("Yes")) {
                    viralLoadClassificationLayout.setVisibility(View.VISIBLE);
                } else {
                    viralLoadClassificationLayout.setVisibility(View.GONE);
                }
            }
        });

        autoControlLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //If a user selects this field then clear other fields that are dependent on this so that the logic can work well
                autoVerififcationLine.clearListSelection();
                autoLongTermLine.clearListSelection();
                edRencencyInterpretation.setText("");
                autoHasViralLoadTIL.setVisibility(View.GONE);
            }
        });

        autoVerififcationLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                autoLongTermLine.clearListSelection();
                edRencencyInterpretation.setText("");
                autoHasViralLoadTIL.setVisibility(View.GONE);
            }
        });

        autoLongTermLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (StringUtils.isBlank(autoControlLine.getText().toString())) {
                    autoControlLineTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    autoControlLineTIL.setError("Select Control Line First");
                } else if (StringUtils.isBlank(autoVerififcationLine.getText().toString())) {
                    autoVerififcationLineTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    autoVerififcationLineTIL.setError("Select Verification Line First");
                } else {
                    if (autoLongTermLine.getText().toString().equals("Yes")) {
                        if (autoControlLine.getText().toString().equals("Yes") && autoVerififcationLine.getText().toString().equals("Yes")) {
                            edRencencyInterpretation.setText("Long Term");
                        } else if (autoControlLine.getText().toString().equals("Yes") && autoVerififcationLine.getText().toString().equals("No")) {
                            edRencencyInterpretation.setText("Invalid");
                        } else if (autoControlLine.getText().toString().equals("No") && autoVerififcationLine.getText().toString().equals("Yes")) {
                            edRencencyInterpretation.setText("Invalid");
                        } else {
                            edRencencyInterpretation.setText("");
                        }
                    } else {
                        autoHasViralLoadTIL.setVisibility(View.GONE);
                        if (autoControlLine.getText().toString().equals("Yes") && autoVerififcationLine.getText().toString().equals("Yes")) {
                            edRencencyInterpretation.setText("Recent");
                            autoHasViralLoadTIL.setVisibility(View.VISIBLE);
                        } else if (autoControlLine.getText().toString().equals("Yes") && autoVerififcationLine.getText().toString().equals("No")) {
                            edRencencyInterpretation.setText("Negative");
                        } else {
                            edRencencyInterpretation.setText("");
                        }
                    }
                }
            }
        });


        autoViralLoadResultClassification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    edFinalRecencyResult.setText("RITA Recent");
                } else if (i == 1) {
                    edFinalRecencyResult.setText("RITA Long Term");
                } else  {
                    edFinalRecencyResult.setText("RITA Inconclusive");
                }
            }
        });

    }

}
