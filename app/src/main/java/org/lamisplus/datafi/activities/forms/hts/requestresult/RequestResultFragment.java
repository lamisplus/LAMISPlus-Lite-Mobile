package org.lamisplus.datafi.activities.forms.hts.requestresult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.forms.hts.posttest.PostTestActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.RequestResult;
import org.lamisplus.datafi.models.RiskAssessment;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;
import java.util.Date;
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
    private AutoCompleteTextView autoprepAccepted;
    private AutoCompleteTextView autoprepOffered;

    private EditText edOthersLongitude;
    private EditText edOthersLatitude;
    private EditText edOthersAdhocCode;

    private AutoCompleteTextView autoCd4Count;
    private AutoCompleteTextView autocd4SemiQuantitative;
    private EditText edcd4FlowCyteometry;

    private LinearLayout riskAssessmentLayoutView;
    private LinearLayout prepLayout;

    private boolean isEligible = false;
    private boolean isAdult = false;

    private Button mSaveContinueButton;

    private boolean isUpdateRequestResult = false;
    private Encounter updatedForm;
    private RequestResult updatedRequestResult;
    private String packageName;

    private LinearLayout confirmatoryTestLayout;
    private LinearLayout tieBreakerLayout;
    private LinearLayout initialHivTest2Layout;
    private LinearLayout confirmatoryTest2Layout;
    private LinearLayout tieBreakerTest2Layout;
    private LinearLayout cd4Layout;

    private TextInputLayout cd4SemiQuantitativeTIL;
    private TextInputLayout edcd4FlowCyteometryTIL;
    private TextInputLayout prepAcceptedTIL;
    private Long regMilli;
    public Long regMillis;

    private TextView testResult1Message;
    private TextView testResult2Message;

    String hivTestResult = "";
    String hivTestResult2 = "";
    int requestCode = 1;
    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;
    private String mLattitude = null;
    private String mLongitude = null;

    private TextInputLayout edDateTest1TIL;
    private TextInputLayout autoResultTest1TIL;
    private TextInputLayout edConfirmDate1TIL;
    private TextInputLayout autoConfirmResult1TIL;
    private TextInputLayout edTieBreaker1TIL;
    private TextInputLayout autoResultTieBreaker1TIL;
    private TextInputLayout edDateTest2TIL;
    private TextInputLayout autoResultTest2TIL;
    private TextInputLayout edConfirmDate2TIL;
    private TextInputLayout autoConfirmResult2TIL;
    private TextInputLayout edDateTieBreaker2TIL;
    private TextInputLayout autoTieBreakerResult2TIL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request_result, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            dropDownListeners();
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            getLastLocation();
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
        autocd4SemiQuantitative = root.findViewById(R.id.autocd4SemiQuantitative);
        edcd4FlowCyteometry = root.findViewById(R.id.edcd4FlowCyteometry);

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

        confirmatoryTestLayout = root.findViewById(R.id.confirmatoryTestLayout);
        tieBreakerLayout = root.findViewById(R.id.tieBreakerLayout);
        initialHivTest2Layout = root.findViewById(R.id.initialHivTest2Layout);
        confirmatoryTest2Layout = root.findViewById(R.id.confirmatoryTest2Layout);
        tieBreakerTest2Layout = root.findViewById(R.id.tieBreakerTest2Layout);
        cd4Layout = root.findViewById(R.id.cd4Layout);
        autoprepAccepted = root.findViewById(R.id.autoprepAccepted);
        autoprepOffered = root.findViewById(R.id.autoprepOffered);
        prepLayout = root.findViewById(R.id.prepLayout);


        cd4SemiQuantitativeTIL = root.findViewById(R.id.cd4SemiQuantitativeTIL);
        edcd4FlowCyteometryTIL = root.findViewById(R.id.edcd4FlowCyteometryTIL);
        prepAcceptedTIL = root.findViewById(R.id.prepAcceptedTIL);
        testResult1Message = root.findViewById(R.id.testResult1Message);
        testResult2Message = root.findViewById(R.id.testResult2Message);

        edDateTest1TIL = root.findViewById(R.id.edDateTest1TIL);
        autoResultTest1TIL = root.findViewById(R.id.autoResultTest1TIL);
        edConfirmDate1TIL = root.findViewById(R.id.edConfirmDate1TIL);
        autoConfirmResult1TIL = root.findViewById(R.id.autoConfirmResult1TIL);
        edTieBreaker1TIL = root.findViewById(R.id.edTieBreaker1TIL);
        autoResultTieBreaker1TIL = root.findViewById(R.id.autoResultTieBreaker1TIL);
        edDateTest2TIL = root.findViewById(R.id.edDateTest2TIL);
        autoResultTest2TIL = root.findViewById(R.id.autoResultTest2TIL);
        edConfirmDate2TIL = root.findViewById(R.id.edConfirmDate2TIL);
        autoConfirmResult2TIL = root.findViewById(R.id.autoConfirmResult2TIL);
        edDateTieBreaker2TIL = root.findViewById(R.id.edDateTieBreaker2TIL);
        autoTieBreakerResult2TIL = root.findViewById(R.id.autoTieBreakerResult2TIL);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        String[] autoCd4CountAnswers = getResources().getStringArray(R.array.cd4_count);
        ArrayAdapter<String> adapterCd4CountAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, autoCd4CountAnswers);
        autoCd4Count.setAdapter(adapterCd4CountAnswers);

        String[] booleanAnswers = getResources().getStringArray(R.array.reactive_non_reactive);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoResultTest1.setAdapter(adapterBooleanAnswers);
        autoConfirmResult1.setAdapter(adapterBooleanAnswers);
        autoResultTieBreaker1.setAdapter(adapterBooleanAnswers);
        autoResultTest2.setAdapter(adapterBooleanAnswers);
        autoConfirmResult2.setAdapter(adapterBooleanAnswers);
        autoTieBreakerResult2.setAdapter(adapterBooleanAnswers);
        autoSyphilis.setAdapter(adapterBooleanAnswers);

        String[] booleanPositiveNegativeAnswers = getResources().getStringArray(R.array.positive_negative);
        ArrayAdapter<String> adapterBooleanPositiveNegativeAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanPositiveNegativeAnswers);

        autoHepatitisB.setAdapter(adapterBooleanPositiveNegativeAnswers);
        autoHepatitisC.setAdapter(adapterBooleanPositiveNegativeAnswers);

        String[] prepYesNo = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterPrepBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, prepYesNo);
        autoprepAccepted.setAdapter(adapterPrepBooleanAnswers);
        autoprepOffered.setAdapter(adapterPrepBooleanAnswers);

        String[] cd4CountValues = getResources().getStringArray(R.array.cd4_count_value);
        ArrayAdapter<String> adapterCd4Values = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, cd4CountValues);
        autocd4SemiQuantitative.setAdapter(adapterCd4Values);
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
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edDateTest1.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            //What happens here is that this function gets the patient registration date then it mandates the test that it cannot be earlier
            //than that date or before the current date.
            Person person = PersonDAO.findPersonById(mPresenter.getPatientId());
            if (person != null) {
                String visitDate = person.getDateOfRegistration();
                String[] explodeDate = visitDate.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);
                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                regMilli = bdt.getMillis();
                mDatePicker.getDatePicker().setMinDate(regMilli);
            }

            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edConfirmDate1.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edDateTest1))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edDateTest1);
                String[] explodeDate = dateTest1.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                regMilli = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edConfirmDate1.setText(selectedYear + "-" + stringMonth + "-" + stringDay);

                }, cYear, cMonth, cDay);

                mDatePicker.getDatePicker().setMinDate(regMilli);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Initial Test Date");
            }
        });

        edDateTieBreaker1.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edConfirmDate1))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edConfirmDate1);
                String[] explodeDate = dateTest1.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                regMilli = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edDateTieBreaker1.setText(selectedYear + "-" + stringMonth + "-" + stringDay);

                }, cYear, cMonth, cDay);

                mDatePicker.getDatePicker().setMinDate(regMilli);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Confirmatory Test Date");
            }
        });

        edDateTest2.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edConfirmDate1))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edConfirmDate1);
                String[] explodeDate = dateTest1.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                regMillis = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edDateTest2.setText(selectedYear + "-" + stringMonth + "-" + stringDay);

                }, cYear, cMonth, cDay);

                mDatePicker.getDatePicker().setMinDate(regMillis);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Confirmatory Test Date");
            }
        });

        edConfirmDate2.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edDateTest2))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edDateTest2);
                String[] explodeDate = dateTest1.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                regMilli = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edConfirmDate2.setText(selectedYear + "-" + stringMonth + "-" + stringDay);

                }, cYear, cMonth, cDay);

                mDatePicker.getDatePicker().setMinDate(regMilli);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            }else{
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the ReTest Date");
            }
        });

        edDateTieBreaker2.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edConfirmDate2))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edConfirmDate2);
                String[] explodeDate = dateTest1.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                regMilli = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edDateTieBreaker2.setText(selectedYear + "-" + stringMonth + "-" + stringDay);

                }, cYear, cMonth, cDay);
                mDatePicker.getDatePicker().setMinDate(regMilli);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            }else{
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Confirmatory Test Date");
            }
        });


    }

    public void fillFields(RequestResult requestResult) {
        if (requestResult != null) {
            isUpdateRequestResult = true;
            updatedRequestResult = requestResult;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.REQUEST_RESULT_FORM, mPresenter.getPatientId());

            hivTestResult = requestResult.getHivTestResult();
            hivTestResult2 = requestResult.getHivTestResult2();


            //Test 1
            edDateTest1.setText(requestResult.getTest1().getDate());

            if (!StringUtils.isBlank(requestResult.getTest1().getResult())) {
                autoResultTest1.setText(StringUtils.changeRequestResultBoolToReactNon(requestResult.getTest1().getResult()), false);
            }

            //Confirm Test
            edConfirmDate1.setText(requestResult.getConfirmatoryTest().getDate());

            if (!StringUtils.isBlank(requestResult.getConfirmatoryTest().getResult())) {
                autoConfirmResult1.setText(StringUtils.changeRequestResultBoolToReactNon(requestResult.getConfirmatoryTest().getResult()), false);
            }

            //Test 2
            edDateTest2.setText(requestResult.getTest2().getDate2());

            if (!StringUtils.isBlank(requestResult.getTest2().getResult2())) {
                autoResultTest2.setText(StringUtils.changeRequestResultBoolToReactNon(requestResult.getTest2().getResult2()), false);
            }

            //Confirm 2
            edConfirmDate2.setText(requestResult.getConfirmatoryTest2().getDate2());

            if (!StringUtils.isBlank(requestResult.getConfirmatoryTest2().getResult2())) {
                autoConfirmResult2.setText(StringUtils.changeRequestResultBoolToReactNon(requestResult.getConfirmatoryTest2().getResult2()), false);
            }

            //Tie Breaker 1
            edDateTieBreaker1.setText(requestResult.getTieBreakerTest().getDate());

            if (!StringUtils.isBlank(requestResult.getTieBreakerTest().getResult())) {
                autoResultTieBreaker1.setText(StringUtils.changeRequestResultBoolToReactNon(requestResult.getTieBreakerTest().getResult()), false);
            }

            //Tie Breaker 2
            edDateTieBreaker2.setText(requestResult.getTieBreakerTest2().getDate2());

            if (!StringUtils.isBlank(requestResult.getTieBreakerTest2().getResult2())) {
                autoTieBreakerResult2.setText(StringUtils.changeRequestResultBoolToReactNon(requestResult.getTieBreakerTest2().getResult2()), false);
            }

            //This fields auto populates the hidden fields based on selected inputs
            if (requestResult.getTest1().getResult() != null) {
                if (requestResult.getTest1().getResult().equals("Reactive")) {
                    confirmatoryTestLayout.setVisibility(View.VISIBLE);
                    cd4Layout.setVisibility(View.VISIBLE);
                } else {
                    confirmatoryTestLayout.setVisibility(View.GONE);
                    cd4Layout.setVisibility(View.GONE);
                }
            }

            if (requestResult.getConfirmatoryTest().getResult() != null) {
                if (requestResult.getConfirmatoryTest().getResult().equals("Reactive")) {
                    initialHivTest2Layout.setVisibility(View.VISIBLE);
                    tieBreakerLayout.setVisibility(View.GONE);
                    cd4Layout.setVisibility(View.VISIBLE);
                } else {
                    tieBreakerLayout.setVisibility(View.VISIBLE);
                    initialHivTest2Layout.setVisibility(View.GONE);
                    cd4Layout.setVisibility(View.GONE);
                }
            }

            if (requestResult.getTieBreakerTest().getResult() != null) {
                if (requestResult.getTieBreakerTest().getResult().equals("Reactive")) {
                    initialHivTest2Layout.setVisibility(View.VISIBLE);
                    confirmatoryTest2Layout.setVisibility(View.VISIBLE);
                } else {
                    initialHivTest2Layout.setVisibility(View.GONE);
                    confirmatoryTest2Layout.setVisibility(View.GONE);
                }
            }


            if (requestResult.getTieBreakerTest2().getResult2() != null) {
                if (requestResult.getTieBreakerTest2().getResult2().equals("Reactive")) {
                    confirmatoryTest2Layout.setVisibility(View.VISIBLE);
                } else {
                    confirmatoryTest2Layout.setVisibility(View.GONE);
                }
            }

            if (requestResult.getConfirmatoryTest2().getResult2() != null) {
                if (requestResult.getConfirmatoryTest2().getResult2().equals("Non Reactive")) {
                    tieBreakerTest2Layout.setVisibility(View.VISIBLE);
                } else {
                    tieBreakerTest2Layout.setVisibility(View.GONE);
                }
            }


            if (requestResult.getTieBreakerTest().getResult() != null) {
                if (requestResult.getTieBreakerTest().getResult().equals("Non Reactive")) {
                    initialHivTest2Layout.setVisibility(View.GONE);
                    confirmatoryTest2Layout.setVisibility(View.GONE);
                    tieBreakerTest2Layout.setVisibility(View.GONE);
                    cd4Layout.setVisibility(View.GONE);
                } else {
                    initialHivTest2Layout.setVisibility(View.VISIBLE);
                    confirmatoryTest2Layout.setVisibility(View.VISIBLE);
                    tieBreakerTest2Layout.setVisibility(View.VISIBLE);
                    cd4Layout.setVisibility(View.VISIBLE);
                }
            }

            if (requestResult.getCd4().getCd4Count() != null) {
                if (requestResult.getCd4().getCd4Count().equals("Semi-Quantitative")) {
                    cd4SemiQuantitativeTIL.setVisibility(View.VISIBLE);
                    edcd4FlowCyteometryTIL.setVisibility(View.GONE);
                    autocd4SemiQuantitative.setText(requestResult.getCd4().getCd4SemiQuantitative(), false);
                } else {
                    cd4SemiQuantitativeTIL.setVisibility(View.GONE);
                    edcd4FlowCyteometryTIL.setVisibility(View.VISIBLE);
                    edcd4FlowCyteometry.setText(requestResult.getCd4().getCd4FlowCyteometry());
                }
            }

            autoCd4Count.setText(requestResult.getCd4().getCd4Count(), false);

            autocd4SemiQuantitative.setText(requestResult.getCd4().getCd4SemiQuantitative(), false);

            edcd4FlowCyteometry.setText(requestResult.getCd4().getCd4FlowCyteometry());

            autoSyphilis.setText(StringUtils.changeRequestResultBoolToReactNon(requestResult.getSyphilisTesting().getSyphilisTestResult()), false);

            autoHepatitisB.setText(StringUtils.changeRequestResultBoolToPosNeg(requestResult.getHepatitisTesting().getHepatitisBTestResult()), false);

            autoHepatitisC.setText(StringUtils.changeRequestResultBoolToPosNeg(requestResult.getHepatitisTesting().getHepatitisCTestResult()), false);

            edOthersLongitude.setText(requestResult.getOthers().getLongitude());

            edOthersLatitude.setText(requestResult.getOthers().getLatitude());

            edOthersAdhocCode.setText(requestResult.getOthers().getAdhocCode());
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
        if (!ViewUtils.isEmpty(autoprepAccepted)) {
            requestResult.setPrepAccepted(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoprepAccepted)));
        }

        if (!ViewUtils.isEmpty(autoprepOffered)) {
            requestResult.setPrepOffered(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoprepOffered)));
        }

        if (!StringUtils.isBlank(hivTestResult)) {
            requestResult.setHivTestResult(hivTestResult);
        }

        if (!StringUtils.isBlank(hivTestResult2)) {
            requestResult.setHivTestResult2(hivTestResult2);
        }

        //Cd4
        RequestResult.CD4 cd4 = new RequestResult.CD4();
        if (!ViewUtils.isEmpty(autoCd4Count)) {
            cd4.setCd4Count(ViewUtils.getInput(autoCd4Count));
        }

        if (!ViewUtils.isEmpty(edcd4FlowCyteometry)) {
            cd4.setCd4FlowCyteometry(ViewUtils.getInput(edcd4FlowCyteometry));
        }

        if (!ViewUtils.isEmpty(autocd4SemiQuantitative)) {
            cd4.setCd4SemiQuantitative(ViewUtils.getInput(autocd4SemiQuantitative));
        }

        requestResult.setCd4(cd4);

        //Test1
        RequestResult.Test1 test1 = new RequestResult.Test1();
        if (!ViewUtils.isEmpty(edDateTest1)) {
            test1.setDate(ViewUtils.getInput(edDateTest1));
        }

        if (!ViewUtils.isEmpty(autoResultTest1)) {
            test1.setResult(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoResultTest1)));
        }
        requestResult.setTest1(test1);

        //Confirmatory Test1
        RequestResult.ConfirmatoryTest confirmatoryTest = new RequestResult.ConfirmatoryTest();

        if (!ViewUtils.isEmpty(edConfirmDate1)) {
            confirmatoryTest.setDate(ViewUtils.getInput(edConfirmDate1));
        }

        if (!ViewUtils.isEmpty(autoConfirmResult1)) {
            confirmatoryTest.setResult(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoConfirmResult1)));
        }

        requestResult.setConfirmatoryTest(confirmatoryTest);

        //Tie Breaker Test1
        RequestResult.TieBreakerTest tieBreakerTest = new RequestResult.TieBreakerTest();

        if (!ViewUtils.isEmpty(edDateTieBreaker1)) {
            tieBreakerTest.setDate(ViewUtils.getInput(edDateTieBreaker1));
        }

        if (!ViewUtils.isEmpty(autoResultTieBreaker1)) {
            tieBreakerTest.setResult(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoResultTieBreaker1)));
        }

        requestResult.setTieBreakerTest(tieBreakerTest);

        //Test2
        RequestResult.Test2 test2 = new RequestResult.Test2();

        if (!ViewUtils.isEmpty(edDateTest2)) {
            test2.setDate2(ViewUtils.getInput(edDateTest2));
        }

        if (!ViewUtils.isEmpty(autoResultTest2)) {
            test2.setResult2(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoResultTest2)));
        }

        requestResult.setTest2(test2);

        //Confirmatory Test 2
        RequestResult.ConfirmatoryTest2 confirmatoryTest2 = new RequestResult.ConfirmatoryTest2();

        if (!ViewUtils.isEmpty(edConfirmDate2)) {
            confirmatoryTest2.setDate2(ViewUtils.getInput(edConfirmDate2));
        }

        if (!ViewUtils.isEmpty(autoConfirmResult2)) {
            confirmatoryTest2.setResult2(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoConfirmResult2)));
        }

        requestResult.setConfirmatoryTest2(confirmatoryTest2);

        //TieBreaker 2
        RequestResult.TieBreakerTest2 tieBreakerTest2 = new RequestResult.TieBreakerTest2();

        if (!ViewUtils.isEmpty(edDateTieBreaker2)) {
            tieBreakerTest2.setDate2(ViewUtils.getInput(edDateTieBreaker2));
        }

        if (!ViewUtils.isEmpty(autoTieBreakerResult2)) {
            tieBreakerTest2.setResult2(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoTieBreakerResult2)));
        }

        requestResult.setTieBreakerTest2(tieBreakerTest2);

        //Syphylis
        RequestResult.SyphilisTesting syphilisTesting = new RequestResult.SyphilisTesting();

        if (!ViewUtils.isEmpty(autoSyphilis)) {
            syphilisTesting.setSyphilisTestResult(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoSyphilis)));
        }

        requestResult.setSyphilisTesting(syphilisTesting);

        //Hepatitis
        RequestResult.HepatitisTesting hepatitisTesting = new RequestResult.HepatitisTesting();

        if (!ViewUtils.isEmpty(autoHepatitisB)) {
            hepatitisTesting.setHepatitisBTestResult(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoHepatitisB)));
        }

        if (!ViewUtils.isEmpty(autoHepatitisC)) {
            hepatitisTesting.setHepatitisCTestResult(StringUtils.changeRequestResultToBool(ViewUtils.getInput(autoHepatitisC)));
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


    public void dropDownListeners() {
        autoResultTest1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    confirmatoryTestLayout.setVisibility(View.VISIBLE);
                    cd4Layout.setVisibility(View.VISIBLE);
                    testResult1Message.setVisibility(View.GONE);
                    prepLayout.setVisibility(View.GONE);
                } else {
                    confirmatoryTestLayout.setVisibility(View.GONE);
                    cd4Layout.setVisibility(View.GONE);
                    testResult1Message.setVisibility(View.VISIBLE);
                    testResult1Message.setText("Negative");
                    hivTestResult = "Negative";
                    prepLayout.setVisibility(View.VISIBLE);
                    testResult1Message.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }
        });

        autoprepOffered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoprepOffered.getText().toString().equals("Yes")) {
                    prepAcceptedTIL.setVisibility(View.VISIBLE);
                } else {
                    prepAcceptedTIL.setVisibility(View.GONE);
                }
            }
        });

        autoConfirmResult1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    initialHivTest2Layout.setVisibility(View.VISIBLE);
                    tieBreakerLayout.setVisibility(View.GONE);
                    cd4Layout.setVisibility(View.VISIBLE);
                    testResult1Message.setVisibility(View.VISIBLE);
                    testResult1Message.setText("Positive");
                    hivTestResult = "Positive";
                    testResult1Message.setBackgroundColor(getResources().getColor(R.color.red));
                    prepLayout.setVisibility(View.GONE);
                } else {
                    tieBreakerLayout.setVisibility(View.VISIBLE);
                    initialHivTest2Layout.setVisibility(View.GONE);
                    cd4Layout.setVisibility(View.GONE);
                    confirmatoryTest2Layout.setVisibility(View.GONE);
                    testResult1Message.setVisibility(View.GONE);
                }
            }
        });

        autoResultTieBreaker1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    initialHivTest2Layout.setVisibility(View.VISIBLE);
                    confirmatoryTest2Layout.setVisibility(View.VISIBLE);
                    testResult1Message.setVisibility(View.VISIBLE);
                    testResult1Message.setText("Positive");
                    hivTestResult = "Positive";
                    testResult1Message.setBackgroundColor(getResources().getColor(R.color.red));
                    prepLayout.setVisibility(View.GONE);
                } else {
                    initialHivTest2Layout.setVisibility(View.GONE);
                    confirmatoryTest2Layout.setVisibility(View.GONE);
                    testResult1Message.setVisibility(View.VISIBLE);
                    testResult1Message.setText("Negative");
                    hivTestResult = "Negative";
                    prepLayout.setVisibility(View.VISIBLE);
                    testResult1Message.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }
        });

        autoResultTest2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    confirmatoryTest2Layout.setVisibility(View.VISIBLE);
                    testResult2Message.setVisibility(View.GONE);
                } else {
                    confirmatoryTest2Layout.setVisibility(View.GONE);
                    testResult2Message.setVisibility(View.VISIBLE);
                    testResult2Message.setText("Negative");
                    testResult2Message.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }
        });

        autoConfirmResult2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    tieBreakerTest2Layout.setVisibility(View.VISIBLE);
                    testResult2Message.setVisibility(View.GONE);
                } else {
                    tieBreakerTest2Layout.setVisibility(View.GONE);
                    testResult2Message.setVisibility(View.VISIBLE);
                    testResult2Message.setText("Positive");
                    hivTestResult2 = "Positive";
                    testResult2Message.setBackgroundColor(getResources().getColor(R.color.red));
                }
            }
        });

        autoTieBreakerResult2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    cd4Layout.setVisibility(View.GONE);
                    testResult2Message.setVisibility(View.VISIBLE);
                    testResult2Message.setText("Negative");
                    testResult2Message.setBackgroundColor(getResources().getColor(R.color.green));
                    hivTestResult2 = "Negative";
                    prepLayout.setVisibility(View.VISIBLE);
                } else {
                    cd4Layout.setVisibility(View.VISIBLE);
                    testResult2Message.setVisibility(View.VISIBLE);
                    testResult2Message.setText("Positive");
                    testResult2Message.setBackgroundColor(getResources().getColor(R.color.red));
                    hivTestResult2 = "Positive";
                    prepLayout.setVisibility(View.GONE);
                }
            }
        });

        autoCd4Count.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    cd4SemiQuantitativeTIL.setVisibility(View.VISIBLE);
                    edcd4FlowCyteometryTIL.setVisibility(View.GONE);
                } else {
                    cd4SemiQuantitativeTIL.setVisibility(View.GONE);
                    edcd4FlowCyteometryTIL.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                android.location.Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    mLattitude = location.getLatitude() + "";
                                    mLongitude = location.getLongitude() + "";

                                    edOthersLatitude.setText(mLattitude);
                                    edOthersLongitude.setText(mLongitude);
                                }
                            }
                        }
                );
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Turn on location");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            mLattitude = mLastLocation.getLatitude() + "";
            mLongitude = mLastLocation.getLongitude() + "";

            edOthersLatitude.setText(mLattitude);
            edOthersLongitude.setText(mLongitude);
        }
    };

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    @Override
    public void scrollToTop() {
        ScrollView scrollView = this.getActivity().findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, scrollView.getPaddingTop());
    }

    @Override
    public void setErrorsVisibility(boolean edDateTest1, boolean autoResultTest1, boolean edConfirmDate1, boolean autoConfirmResult1, boolean edTieBreaker1, boolean autoResultTieBreaker1, boolean edDateTest2, boolean autoResultTest2, boolean edConfirmDate2, boolean autoConfirmResult2, boolean edDateTieBreaker2, boolean autoTieBreakerResult2) {
        if (edDateTest1) {
            edDateTest1TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (autoResultTest1) {
            autoResultTest1TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (edConfirmDate1) {
            edConfirmDate1TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (autoConfirmResult1) {
            autoConfirmResult1TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (edTieBreaker1) {
            edTieBreaker1TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (autoResultTieBreaker1) {
            autoResultTieBreaker1TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (edDateTest2) {
            edDateTest2TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (autoResultTest2) {
            autoResultTest2TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (edConfirmDate2) {
            edConfirmDate2TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (autoConfirmResult2) {
            autoConfirmResult2TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (edDateTieBreaker2) {
            edDateTieBreaker2TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (autoTieBreakerResult2) {
            autoTieBreakerResult2TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }
    }

}
