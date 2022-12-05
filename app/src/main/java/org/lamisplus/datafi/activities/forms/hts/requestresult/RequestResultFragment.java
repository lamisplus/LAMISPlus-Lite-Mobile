package org.lamisplus.datafi.activities.forms.hts.requestresult;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.forms.hts.posttest.PostTestActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.RequestResult;
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

    private EditText edDateTest1;
    private AutoCompleteTextView autoResultTest1;
    private EditText edConfirmDate1;
    private AutoCompleteTextView autoConfirmResult1;
    private EditText edDateTieBreaker1;
    private AutoCompleteTextView autoResultTieBreaker1;

    private EditText edDateTest2;
    private AutoCompleteTextView autoResultTest2;
    private EditText edConfirmDate2;
    private AutoCompleteTextView autoConfirmResult2;
    private EditText edDateTieBreaker2;
    private AutoCompleteTextView autoTieBreakerResult2;

    private AutoCompleteTextView autoSyphilis;

    private AutoCompleteTextView autoHepatitisB;
    private AutoCompleteTextView autoHepatitisC;

    private EditText edOthersLongitude;
    private EditText edOthersLatitude;
    private EditText edOthersAdhocCode;

    private AutoCompleteTextView autoCd4Count;
    private EditText edcd4SemiQuantitative;

    private LinearLayout riskAssessmentLayoutView;

    private boolean isEligible = false;
    private boolean isAdult = false;

    private Button mSaveContinueButton;

    private boolean isUpdateRequestResult = false;
    private Encounter updatedForm;
    private RequestResult updatedRequestResult;
    private String packageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request_result, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.REQUEST_RESULT_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.REQUEST_RESULT_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateRequestResult) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounterRequestResult(ApplicationConstants.Forms.REQUEST_RESULT_FORM, mPresenter.getPatientId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static RequestResultFragment newInstance() {
        return new RequestResultFragment();
    }

    private void initiateFragmentViews(View root) {
        edDateTest1 = root.findViewById(R.id.edDateTest1);
        autoResultTest1 = root.findViewById(R.id.autoResultTest1);
        edConfirmDate1 = root.findViewById(R.id.edConfirmDate1);
        autoConfirmResult1 = root.findViewById(R.id.autoConfirmResult1);
        edDateTieBreaker1 = root.findViewById(R.id.edTieBreaker1);
        autoResultTieBreaker1 = root.findViewById(R.id.autoResultTieBreaker1);

         autoCd4Count = root.findViewById(R.id.autoCd4Count);
        edcd4SemiQuantitative = root.findViewById(R.id.edcd4SemiQuantitative);

        edDateTest2 = root.findViewById(R.id.edDateTest2);
        autoResultTest2 = root.findViewById(R.id.autoResultTest2);
        edConfirmDate2 = root.findViewById(R.id.edConfirmDate2);
        autoConfirmResult2 = root.findViewById(R.id.autoConfirmResult2);
        edDateTieBreaker2 = root.findViewById(R.id.edDateTieBreaker2);
        autoTieBreakerResult2 = root.findViewById(R.id.autoTieBreakerResult2);

        autoSyphilis = root.findViewById(R.id.autoSyphilis);

        autoHepatitisB = root.findViewById(R.id.autoHepatitisB);
        autoHepatitisC = root.findViewById(R.id.autoHepatitisC);

        edOthersLongitude = root.findViewById(R.id.edOthersLongitude);
        edOthersLatitude = root.findViewById(R.id.edOthersLatitude);
        edOthersAdhocCode = root.findViewById(R.id.edOthersAdhocCode);


        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        String[] autoCd4CountAnswers = {"Semi-Quantitative","Flow Cyteometry"};
        ArrayAdapter<String> adapterCd4CountAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, autoCd4CountAnswers);
        autoCd4Count.setAdapter(adapterCd4CountAnswers);

        String[] booleanAnswers = {"Reactive", "Non Reactive"};
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoResultTest1.setAdapter(adapterBooleanAnswers);
        autoResultTest1.setAdapter(adapterBooleanAnswers);
        autoConfirmResult1.setAdapter(adapterBooleanAnswers);
        autoResultTieBreaker1.setAdapter(adapterBooleanAnswers);
        autoResultTest2.setAdapter(adapterBooleanAnswers);
        autoConfirmResult2.setAdapter(adapterBooleanAnswers);
        autoTieBreakerResult2.setAdapter(adapterBooleanAnswers);
        autoSyphilis.setAdapter(adapterBooleanAnswers);
        autoHepatitisB.setAdapter(adapterBooleanAnswers);
        autoHepatitisC.setAdapter(adapterBooleanAnswers);
    }

    private void showDatePickers() {
        edDateTest1.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edDateTest1.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edConfirmDate1.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edConfirmDate1.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);

            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateTieBreaker1.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edDateTieBreaker1.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);

            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateTest2.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edDateTest2.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);

            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edConfirmDate2.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edConfirmDate2.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);

            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateTieBreaker2.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edDateTieBreaker2.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);

            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });


    }

    public void fillFields(RequestResult requestResult) {
        if (requestResult != null) {
            isUpdateRequestResult = true;
            updatedRequestResult = requestResult;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.REQUEST_RESULT_FORM, mPresenter.getPatientId());
        }
    }

    private RequestResult createEncounter() {
        RequestResult requestResult = new RequestResult();
        updateEncounterWithData(requestResult);
        return requestResult;
    }

    private RequestResult updateEncounter(RequestResult requestResult) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(requestResult);
        return requestResult;
    }

    private RequestResult updateEncounterWithData(RequestResult requestResult) {
        //Cd4
        RequestResult.CD4 cd4 = new RequestResult.CD4();
        if (!ViewUtils.isEmpty(autoCd4Count)) {
            cd4.setCd4Count(ViewUtils.getInput(autoCd4Count));
        }

        if (!ViewUtils.isEmpty(autoCd4Count)) {
            cd4.setCd4SemiQuantitative(ViewUtils.getInput(autoCd4Count));
        }

        requestResult.setCd4(cd4);

        //Test1
        RequestResult.Test1  test1 = new RequestResult.Test1();
        if (!ViewUtils.isEmpty(edDateTest1)) {
            test1.setDate(ViewUtils.getInput(edDateTest1));
        }

        if (!ViewUtils.isEmpty(autoResultTest1)) {
            test1.setResult(ViewUtils.getInput(autoResultTest1));
        }
        requestResult.setTest1(test1);

        //Confirmatory Test1
        RequestResult.ConfirmatoryTest confirmatoryTest = new RequestResult.ConfirmatoryTest();

        if (!ViewUtils.isEmpty(edConfirmDate1)) {
            confirmatoryTest.setDate(ViewUtils.getInput(edConfirmDate1));
        }

        if (!ViewUtils.isEmpty(autoConfirmResult1)) {
            confirmatoryTest.setResult(ViewUtils.getInput(autoConfirmResult1));
        }

        requestResult.setConfirmatoryTest(confirmatoryTest);

        //Tie Breaker Test1
        RequestResult.TieBreakerTest tieBreakerTest = new RequestResult.TieBreakerTest();

        if (!ViewUtils.isEmpty(edDateTieBreaker1)) {
            tieBreakerTest.setDate(ViewUtils.getInput(edDateTieBreaker1));
        }

        if (!ViewUtils.isEmpty(autoResultTieBreaker1)) {
            tieBreakerTest.setResult(ViewUtils.getInput(autoResultTieBreaker1));
        }

        requestResult.setTieBreakerTest(tieBreakerTest);

        //Test2
        RequestResult.Test2 test2 = new RequestResult.Test2();

        if (!ViewUtils.isEmpty(edDateTest2)) {
            test2.setDate2(ViewUtils.getInput(edDateTest2));
        }

        if (!ViewUtils.isEmpty(autoResultTest2)) {
            test2.setResult2(ViewUtils.getInput(autoResultTest2));
        }

        requestResult.setTest2(test2);

        //Confirmatory Test 2
        RequestResult.ConfirmatoryTest2 confirmatoryTest2 = new RequestResult.ConfirmatoryTest2();

        if (!ViewUtils.isEmpty(edConfirmDate2)) {
            confirmatoryTest2.setDate2(ViewUtils.getInput(edConfirmDate2));
        }

        if (!ViewUtils.isEmpty(autoConfirmResult2)) {
            confirmatoryTest2.setResult2(ViewUtils.getInput(autoConfirmResult2));
        }

        requestResult.setConfirmatoryTest2(confirmatoryTest2);

        //TieBreaker 2
        RequestResult.TieBreakerTest2 tieBreakerTest2 = new RequestResult.TieBreakerTest2();

        if (!ViewUtils.isEmpty(edDateTieBreaker2)) {
            tieBreakerTest2.setDate2(ViewUtils.getInput(edDateTieBreaker2));
        }

        if (!ViewUtils.isEmpty(autoTieBreakerResult2)) {
            tieBreakerTest2.setResult2(ViewUtils.getInput(autoTieBreakerResult2));
        }

        requestResult.setTieBreakerTest2(tieBreakerTest2);

        //Syphylis
        RequestResult.SyphilisTesting syphilisTesting = new RequestResult.SyphilisTesting();

        if (!ViewUtils.isEmpty(autoSyphilis)) {
            syphilisTesting.setSyphilisTestResult(ViewUtils.getInput(autoSyphilis));
        }

        requestResult.setSyphilisTesting(syphilisTesting);

        //Hepatitis
        RequestResult.HepatitisTesting hepatitisTesting = new RequestResult.HepatitisTesting();

        if (!ViewUtils.isEmpty(autoHepatitisB)) {
            hepatitisTesting.setHepatitisBTestResult(ViewUtils.getInput(autoHepatitisB));
        }

        if (!ViewUtils.isEmpty(autoHepatitisC)) {
            hepatitisTesting.setHepatitisCTestResult(ViewUtils.getInput(autoHepatitisC));
        }

        requestResult.setHepatitisTesting(hepatitisTesting);

        //Others
        RequestResult.Others others = new RequestResult.Others();
        if (!ViewUtils.isEmpty(edOthersLatitude)) {
            others.setLatitude(ViewUtils.getInput(edOthersLatitude));
        }

        if (!ViewUtils.isEmpty(edOthersLongitude)) {
            others.setLongitude(ViewUtils.getInput(edOthersLongitude));
        }

        if (!ViewUtils.isEmpty(edOthersAdhocCode)) {
            others.setAdhocCode(ViewUtils.getInput(edOthersLongitude));
        }

        requestResult.setOthers(others);

        return requestResult;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if (isUpdateRequestResult) {
                    mPresenter.confirmUpdate(updateEncounter(updatedRequestResult), updatedForm);
                } else {
                    mPresenter.confirmCreate(createEncounter(), packageName);
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void startActivityForPostTestForm() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            Intent clientIntakeProgram = new Intent(LamisPlus.getInstance(), PostTestActivity.class);
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
