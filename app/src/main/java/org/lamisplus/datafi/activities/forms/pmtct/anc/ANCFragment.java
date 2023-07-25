package org.lamisplus.datafi.activities.forms.pmtct.anc;

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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientFragment;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.activities.pmtct.pmtctservices.PMTCTServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.ContactPointClass;
import org.lamisplus.datafi.dao.AccountDAO;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.OrganizationUnitDAO;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Account;
import org.lamisplus.datafi.models.Address;
import org.lamisplus.datafi.models.Contact;
import org.lamisplus.datafi.models.ContactPoint;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PatientIdentifier;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ANCFragment extends LamisBaseFragment<ANCContract.Presenter> implements ANCContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private String[] gender, maritalStatus, educationalLevel, allStates, allRelationShipNok;
    private AutoCompleteTextView autoGender;
    private AutoCompleteTextView autoMaritalStatus;
    private AutoCompleteTextView autoEducationalLevel;
    private AutoCompleteTextView autoEmploymentStatus;
    private AutoCompleteTextView autoState;
    private AutoCompleteTextView autoProvinceLGA;
    private EditText edRegistrationDate;
    private EditText edhospitalNumber;
    private EditText edNin;
    private EditText edFirstName;
    private EditText edMiddleName;
    private EditText edLastName;
    private EditText edDateofBirth;
    private EditText edAge;
    private EditText autoCountry;
    private EditText edPhone;
    private EditText edAltPhone;
    private EditText edEmail;
    private EditText edStreet;
    private EditText edLandmark;

    private TextInputLayout labelDateofBirth;
    private TextInputLayout labelAge;

    private RadioGroup rpDateofBirth;

    private RadioGroup rgDateOfBirthSelect;

    private boolean isUpdatePatient = false;
    private Person updatedPatient;
    private AutoCompleteTextView autoNokRelationship;
    private EditText edNokFirstName;
    private EditText edNokMiddleName;
    private EditText edNokLastName;
    private EditText edNokPhone;
    private EditText edNokemail;
    private EditText edNokAddress;

    private ProgressBar progressBar;

    private TextInputLayout registrationDateTIL;
    private TextInputLayout edhospitalNumberTIL;
    private TextInputLayout edfirstNameTIL;
    private TextInputLayout edLastNameTIL;
    private TextInputLayout edMiddleNameTIL;
    private TextInputLayout fillGenderTIL;
    private TextInputLayout edDateofBirthTIL;
    private TextInputLayout fillMaritalStatusTIL;
    private TextInputLayout fillEducationLevelTIL;
    private TextInputLayout fillEmploymentStatusTIL;
    private TextInputLayout edPhoneTIL;
    private TextInputLayout fillStateTIL;
    private TextInputLayout fillProvinceDistrictLGATIL;
    private TextInputLayout addressTIL;
    private TextInputLayout edNokFirstNameTIL;
    private TextInputLayout edNokMiddleNameTIL;
    private TextInputLayout edNokLastNameTIL;

    private boolean isUpdateAnc = false;
    private Encounter updatedForm;
    private ANC updatedAnc;
    private EditText edancNo;
    private EditText edDateEnrollment;
    private EditText edParity;
    private EditText edGravida;
    private EditText edLmp;
    private EditText edGaweeks;
    private AutoCompleteTextView autoSourceOfReferral;
    private AutoCompleteTextView autoTestedSyphilis;
    private AutoCompleteTextView autotestResultSyphilis;
    private AutoCompleteTextView autoTreatedSyphilis;
    private AutoCompleteTextView autoReferredSyphilisTreatment;
    private AutoCompleteTextView autoStaticHivStatus;
    private TextInputLayout edancNoTIL;
    private TextInputLayout edDateEnrollmentTIL;
    private TextInputLayout edParityTIL;
    private TextInputLayout edGravidaTIL;
    private TextInputLayout edLMpTIL;
    private TextInputLayout edGaWeeksTIL;
    private TextInputLayout autoSourceOfReferralTIL;
    private TextInputLayout autoTestedSyphilisTIL;
    private TextInputLayout autoHivStatusTIL;
    private LinearLayout patientDetailsView;
    private LinearLayout autoSyphilisTestResultView;
    private LinearLayout syphilisOtherView;

    private String packageName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_anc, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            initViewsPatients(root);
            initDropDownsPatients();
            setListeners();
            showDatePickers();
            dropDownClickListeners();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

            if (!StringUtils.notEmpty(mPresenter.getPatientId())) {
                patientDetailsView.setVisibility(View.VISIBLE);
            } else {
                patientDetailsView.setVisibility(View.GONE);
            }
            if (StringUtils.notEmpty(mPresenter.getPatientId()) && mPresenter.patientToUpdate(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    public static ANCFragment newInstance() {
        return new ANCFragment();
    }

    private void initiateFragmentViews(View root) {
        edancNo = root.findViewById(R.id.edancNo);
        edDateEnrollment = root.findViewById(R.id.edDateEnrollment);
        edParity = root.findViewById(R.id.edParity);
        edGravida = root.findViewById(R.id.edGravida);
        edLmp = root.findViewById(R.id.edLmp);
        edGaweeks = root.findViewById(R.id.edGaweeks);
        autoSourceOfReferral = root.findViewById(R.id.autoSourceOfReferral);
        autoTestedSyphilis = root.findViewById(R.id.autoTestedSyphilis);
        autotestResultSyphilis = root.findViewById(R.id.autoTestResultSyphilis);
        autoTreatedSyphilis = root.findViewById(R.id.autoTreatedSyphilis);
        autoReferredSyphilisTreatment = root.findViewById(R.id.autoReferredSyphilisTreatment);
        autoStaticHivStatus = root.findViewById(R.id.autoStaticHivStatus);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);

        edancNoTIL = root.findViewById(R.id.edancNoTIL);
        edDateEnrollmentTIL = root.findViewById(R.id.edDateEnrollmentTIL);
        edParityTIL = root.findViewById(R.id.edParityTIL);
        edGravidaTIL = root.findViewById(R.id.edGravidaTIL);
        edLMpTIL = root.findViewById(R.id.edLMpTIL);
        edGaWeeksTIL = root.findViewById(R.id.edGaWeeksTIL);
        autoSourceOfReferralTIL = root.findViewById(R.id.autoSourceOfReferralTIL);
        autoTestedSyphilisTIL = root.findViewById(R.id.autoTestedSyphilisTIL);
        autoHivStatusTIL = root.findViewById(R.id.autoHivStatusTIL);
        autoSyphilisTestResultView = root.findViewById(R.id.autoSyphilisTestResultView);
        syphilisOtherView = root.findViewById(R.id.syphilisOtherView);

        patientDetailsView = root.findViewById(R.id.patientDetailsView);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);
        String[] sourceReferral = getResources().getStringArray(R.array.source_of_referral);
        ArrayAdapter<String> adapterSourceReferral = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, sourceReferral);
        autoSourceOfReferral.setAdapter(adapterSourceReferral);

        String[] testedSyphilis = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterTestSyphylis = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, testedSyphilis);
        autoTestedSyphilis.setAdapter(adapterTestSyphylis);

        String[] testResultSyphilis = getResources().getStringArray(R.array.positive_negative);
        ArrayAdapter<String> adapterTestResultSyphilis = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, testResultSyphilis);
        autotestResultSyphilis.setAdapter(adapterTestResultSyphilis);

        String[] referredSyphilis = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterReferredSyphilis = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, referredSyphilis);
        autoReferredSyphilisTreatment.setAdapter(adapterReferredSyphilis);

        String[] treatedSyphilis = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterTreatedSyphilis = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, treatedSyphilis);
        autoTreatedSyphilis.setAdapter(adapterTreatedSyphilis);

        String[] hivStatus = getResources().getStringArray(R.array.pmtct_hiv_status);
        ArrayAdapter<String> adapterHivStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, hivStatus);
        autoStaticHivStatus.setAdapter(adapterHivStatus);
    }

    private void showDatePickers() {
        edRegistrationDate.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(ANCFragment.this.getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edRegistrationDate.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
                edDateEnrollment.getText().clear();
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateofBirth.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edRegistrationDate))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateReg = ViewUtils.getInput(edRegistrationDate);
                String[] explodeDate = dateReg.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);

                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                long regMilli = bdt.getMillis();

                DatePickerDialog mDatePicker = new DatePickerDialog(ANCFragment.this.getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    int adjustedMonth = selectedMonth + 1;
                    String stringMonth = String.format("%02d", adjustedMonth);
                    String stringDay = String.format("%02d", selectedDay);
                    edDateofBirth.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
                    int age = DateUtils.getAgeFromBirthdateString(selectedYear + "-" + stringMonth + "-" + stringDay);

                    edAge.setText(age + "");
                }, cYear, cMonth, cDay);

                mDatePicker.getDatePicker().setMaxDate(regMilli);
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Select the Date of Registration");
            }
        });

        edDateEnrollment.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            Long regMillis = 0L;
            if (!StringUtils.isBlank(ViewUtils.getInput(edRegistrationDate))) {
                String dateTestVisitDate = ViewUtils.getInput(edRegistrationDate);
                String[] explodeDate = dateTestVisitDate.split("-");
                int yearVisit = Integer.valueOf(explodeDate[0]);
                int monthVisit = Integer.valueOf(explodeDate[1]);
                int dayVisit = Integer.valueOf(explodeDate[2]);


                LocalDate birthdate = new LocalDate(yearVisit, monthVisit, dayVisit);
                DateTime bdt = birthdate.toDateTimeAtStartOfDay().toDateTime();
                regMillis = bdt.getMillis();
            }

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edDateEnrollment.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMinDate(regMillis);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edLmp.setOnClickListener(v -> {
            if (!StringUtils.isBlank(ViewUtils.getInput(edDateEnrollment))) {
                int cYear;
                int cMonth;
                int cDay;

                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                String dateTest1 = ViewUtils.getInput(edDateEnrollment);
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
                    String lmpDate = selectedYear + "-" + stringMonth + "-" + stringDay;
                    edLmp.setText(lmpDate);
                    Integer ga = DateUtils.gestationAge(lmpDate, ViewUtils.getInput(edDateEnrollment));
                    edGaweeks.setText(ga + "");
                }, cYear, cMonth, cDay);
                mDatePicker.getDatePicker().setMaxDate(regMilli);
                mDatePicker.setTitle(getString(R.string.date_picker_title));
                mDatePicker.show();
            } else {
                ToastUtil.error("Please select Date of Enrollment first");
            }
        });
    }


    public void fillFields(ANC anc) {
        if (anc != null) {
            isUpdateAnc = true;
            updatedAnc = anc;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());

            edancNo.setText(anc.getAncNo());

            edDateEnrollment.setText(anc.getFirstAncDate());

            edParity.setText(anc.getParity());

            edGravida.setText(anc.getGravida());

            edLmp.setText(anc.getLmp());

            edGaweeks.setText(anc.getGaweeks());

            if (CodesetsDAO.findCodesetsDisplayByCode(anc.getSourceOfReferral()) != null) {
                autoSourceOfReferral.setText(CodesetsDAO.findCodesetsDisplayByCode(anc.getSourceOfReferral()), false);
            }

            autoTestedSyphilis.setText(anc.getTestedSyphilis(), false);

            autotestResultSyphilis.setText(anc.getTestResultSyphilis(), false);

            autoTreatedSyphilis.setText(anc.getTreatedSyphilis(), false);

            autoReferredSyphilisTreatment.setText(anc.getReferredSyphilisTreatment(), false);

            autoStaticHivStatus.setText(anc.getStaticHivStatus());
        }
    }

    private ANC createEncounter() {
        ANC anc = new ANC();
        updateEncounterWithData(anc);
        return anc;
    }

    private ANC updateEncounter(ANC anc) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(anc);
        return anc;
    }

    private ANC updateEncounterWithData(ANC anc) {
        if (!ViewUtils.isEmpty(edancNo)) {
            anc.setAncNo(ViewUtils.getInput(edancNo));
        }

        if (!ViewUtils.isEmpty(edDateEnrollment)) {
            anc.setFirstAncDate(ViewUtils.getInput(edDateEnrollment));
        }

        if (!ViewUtils.isEmpty(edParity)) {
            anc.setParity(ViewUtils.getInput(edParity));
        }

        if (!ViewUtils.isEmpty(edGravida)) {
            anc.setGravida(ViewUtils.getInput(edGravida));
        }

        if (!ViewUtils.isEmpty(edLmp)) {
            anc.setLmp(ViewUtils.getInput(edLmp));
        }

        if (!ViewUtils.isEmpty(edGaweeks)) {
            anc.setGaweeks(ViewUtils.getInput(edGaweeks));
        }

        if (!ViewUtils.isEmpty(autoSourceOfReferral)) {
            anc.setSourceOfReferral(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoSourceOfReferral)));
        }

        if (!ViewUtils.isEmpty(autoTestedSyphilis)) {
            anc.setTestedSyphilis(ViewUtils.getInput(autoTestedSyphilis));
        }

        if (!ViewUtils.isEmpty(autotestResultSyphilis)) {
            anc.setTestResultSyphilis(ViewUtils.getInput(autotestResultSyphilis));
        }

        if (!ViewUtils.isEmpty(autoTreatedSyphilis)) {
            anc.setTreatedSyphilis(ViewUtils.getInput(autoTreatedSyphilis));
        }

        if (!ViewUtils.isEmpty(autoReferredSyphilisTreatment)) {
            anc.setReferredSyphilisTreatment(ViewUtils.getInput(autoReferredSyphilisTreatment));
        }

        if (!ViewUtils.isEmpty(autoStaticHivStatus)) {
            anc.setStaticHivStatus(ViewUtils.getInput(autoStaticHivStatus));
        }

        anc.setPersonDto(new ANC.PersonDto());
        anc.setPmtctHtsInfo(new ANC.PMTCTHtsInfo());
        anc.setSyphilisInfo(new ANC.SyphilisInfo());
        anc.setPartnerNotification(new ANC.PartnerNotification());

        return anc;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateAnc) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
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
                if (isUpdateAnc) {
                    mPresenter.confirmUpdate(updateEncounter(updatedAnc), updatedForm);
                } else {
                    mPresenter.confirmCreate(createEncounter(), createPatient(), packageName);
                }
                break;
            default:
                break;
        }
    }

    private Person createPatient() {
        Person person = new Person();
        updatePatientWithData(person);
        return person;
    }

    public void initViewsPatients(View root) {
        autoGender = root.findViewById(R.id.fillGender);
        autoMaritalStatus = root.findViewById(R.id.fillMaritalStatus);
        autoEducationalLevel = root.findViewById(R.id.fillEducationLevel);
        autoEmploymentStatus = root.findViewById(R.id.fillEmploymentStatus);
        autoState = root.findViewById(R.id.fillState);
        autoCountry = root.findViewById(R.id.fillCountry);
        autoProvinceLGA = root.findViewById(R.id.fillProvinceDistrictLGA);
        autoNokRelationship = root.findViewById(R.id.edNokfillRelationshipType);

        rpDateofBirth = root.findViewById(R.id.rgdateOfBirthSelect);
        labelAge = root.findViewById(R.id.labelAge);

        edRegistrationDate = root.findViewById(R.id.registrationDate);
        edhospitalNumber = root.findViewById(R.id.edhospitalNumber);
        edNin = root.findViewById(R.id.edNin);

        edFirstName = root.findViewById(R.id.edfirstName);
        edMiddleName = root.findViewById(R.id.edMiddleName);
        edLastName = root.findViewById(R.id.edLastName);
        edPhone = root.findViewById(R.id.edPhone);
        edAltPhone = root.findViewById(R.id.edAltPhone);
        edAge = root.findViewById(R.id.edAge);
        edDateofBirth = root.findViewById(R.id.edDateofBirth);
        edEmail = root.findViewById(R.id.edEmail);
        edStreet = root.findViewById(R.id.edStreet);

        edLandmark = root.findViewById(R.id.edLandMark);

        rgDateOfBirthSelect = root.findViewById(R.id.rgdateOfBirthSelect);

        edNokFirstName = root.findViewById(R.id.edNokFirstName);
        edNokMiddleName = root.findViewById(R.id.edNokMiddleName);
        edNokLastName = root.findViewById(R.id.edNokLastName);
        edNokPhone = root.findViewById(R.id.edNokPhone);
        edNokemail = root.findViewById(R.id.edNokEmail);
        edNokAddress = root.findViewById(R.id.edNokAddress);

        progressBar = root.findViewById(R.id.progress_bar);

        registrationDateTIL = root.findViewById(R.id.registrationDateTIL);
        edhospitalNumberTIL = root.findViewById(R.id.edhospitalNumberTIL);
        edfirstNameTIL = root.findViewById(R.id.edfirstNameTIL);
        edLastNameTIL = root.findViewById(R.id.edLastNameTIL);
        edMiddleNameTIL = root.findViewById(R.id.edMiddleNameTIL);
        fillGenderTIL = root.findViewById(R.id.fillGenderTIL);
        edDateofBirthTIL = root.findViewById(R.id.edDateofBirthTIL);
        fillMaritalStatusTIL = root.findViewById(R.id.fillMaritalStatusTIL);
        fillEducationLevelTIL = root.findViewById(R.id.fillEducationLevelTIL);
        edPhoneTIL = root.findViewById(R.id.edPhoneTIL);
        fillStateTIL = root.findViewById(R.id.fillStateTIL);
        fillProvinceDistrictLGATIL = root.findViewById(R.id.fillProvinceDistrictLGATIL);
        addressTIL = root.findViewById(R.id.addressTIL);

        edNokFirstNameTIL = root.findViewById(R.id.edNokFirstNameTIL);
        edNokMiddleNameTIL = root.findViewById(R.id.edNokMiddleNameTIL);
        edNokLastNameTIL = root.findViewById(R.id.edNokLastNameTIL);
    }

    public void initDropDownsPatients() {
        autoGender.setText("Female");

        maritalStatus = getResources().getStringArray(R.array.marital_status);
        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, maritalStatus);
        autoMaritalStatus.setAdapter(adapterMaritalStatus);

        educationalLevel = getResources().getStringArray(R.array.educational_level);
        ArrayAdapter<String> adapterEducationalLevel = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, educationalLevel);
        autoEducationalLevel.setAdapter(adapterEducationalLevel);


        String[] employment = getResources().getStringArray(R.array.employment_status);
        ArrayAdapter<String> adapterEmployment = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, employment);
        autoEmploymentStatus.setAdapter(adapterEmployment);

        autoCountry.setText("Nigeria");

        allStates = getResources().getStringArray(R.array.nigeria);
        ArrayAdapter<String> adapterAllStates = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allStates);
        autoState.setAdapter(adapterAllStates);

        allRelationShipNok = getResources().getStringArray(R.array.relation_next_of_kin);
        ArrayAdapter<String> adapterAllRelNok = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allRelationShipNok);
        autoNokRelationship.setAdapter(adapterAllRelNok);

    }

    public Person updatePatientWithData(Person person) {
        if (!ViewUtils.isEmpty(edRegistrationDate)) {
            person.setDateOfRegistration(ViewUtils.getInput(edRegistrationDate));
        }
        if (!ViewUtils.isEmpty(edFirstName)) {
            person.setFirstName(ViewUtils.getInput(edFirstName));
        }
        if (!ViewUtils.isEmpty(edLastName)) {
            person.setSurname(ViewUtils.getInput(edLastName));
        }

        person.setOtherName(ViewUtils.getInput(edMiddleName));

        int radioButtonID = rpDateofBirth.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) rpDateofBirth.findViewById(radioButtonID);
        String selectedText = (String) radioButton.getText();
        if (selectedText.equals("Actual")) {
            person.setDateOfBirth(ViewUtils.getInput(edDateofBirth));
        } else {
            String dateOfBirth = DateUtils.getAgeFromBirthdate(Integer.parseInt(ViewUtils.getInput(edAge)));
            person.setDateOfBirth(dateOfBirth);
            person.setDateOfBirthEstimated(true);
        }

        person.setDeceased(false);

        person.setDeceasedDateTime("");

        person.setEmrId("");

        Account account = AccountDAO.getDefaultLocation();

        person.setFacilityId(account.getCurrentOrganisationUnitId());

        if (!ViewUtils.isEmpty(autoGender)) {
            int genderId = CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoGender));
            person.setGenderId(genderId);
            person.setSexId(genderId);
        }

        if (!ViewUtils.isEmpty(autoMaritalStatus)) {
            int maritalStatusId = CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoMaritalStatus));
            person.setMaritalStatusId(maritalStatusId);
        }

        if (!ViewUtils.isEmpty(autoEducationalLevel)) {
            int educationLevelId = CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoEducationalLevel));
            person.setEducationId(educationLevelId);
        }

        if (!ViewUtils.isEmpty(autoEmploymentStatus)) {
            int employmentId = CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoEmploymentStatus));
            person.setEmploymentStatusId(employmentId);
        }


        List<ContactPointClass.ContactPointItems> contactPointItems = new ArrayList<>();
        contactPointItems.add(new ContactPointClass.ContactPointItems("phone", ViewUtils.getInput(edPhone)));
        contactPointItems.add(new ContactPointClass.ContactPointItems("email", ViewUtils.getInput(edEmail)));
        contactPointItems.add(new ContactPointClass.ContactPointItems("altphone", ViewUtils.getInput(edAltPhone)));

        String json = new Gson().toJson(contactPointItems);

        person.setContactPoint(json);


        Address address = new Address();
        address.setCountryId(OrganizationUnitDAO.findOrganizationUnitIdByName(ViewUtils.getInput(autoCountry)));

        if (!ViewUtils.isEmpty(autoState)) {
            //I selected Abuja as the state and the App crashed. for future references
            address.setStateId(OrganizationUnitDAO.findOrganizationUnitIdByName(ViewUtils.getInput(autoState)));
        }

        if (!ViewUtils.isEmpty(autoProvinceLGA)) {
            address.setDistrict(ViewUtils.getInput(autoProvinceLGA));
        }

        if (!ViewUtils.isEmpty(edStreet)) {
            address.setCity(ViewUtils.getInput(edStreet));
        }

        String[] line = new String[]{ViewUtils.getInput(edLandmark)};
        address.setLine(line);

        address.setPostalCode("");

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);

        person.setAddress(addressList);
        person.setAddressList();


        Contact contact = new Contact();
        if (!ViewUtils.isEmpty(edNokFirstName)) {
            contact.setFirstName(ViewUtils.getInput(edNokFirstName));
        }
        if (!ViewUtils.isEmpty(edNokLastName)) {
            contact.setSurname(ViewUtils.getInput(edNokLastName));
        }
        if (!ViewUtils.isEmpty(edNokMiddleName)) {
            contact.setOtherName(ViewUtils.getInput(edNokMiddleName));
        }
        if (!ViewUtils.isEmpty(autoNokRelationship)) {
            contact.setRelationshipId(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoNokRelationship)));
        }

        /** Working here **/
        if (!ViewUtils.isEmpty(edNokAddress)) {
            Address contactAddress = new Address();
            String[] li = new String[]{ViewUtils.getInput(edNokAddress)};
            contactAddress.setLine(li);

            contact.setAddress(contactAddress);
        }


        ContactPoint contactPointMe1 = new ContactPoint();
        if (!ViewUtils.isEmpty(edNokPhone)) {
            contactPointMe1.setType("phone");
            contactPointMe1.setValue(ViewUtils.getInput(edNokPhone));
        }

        contact.setContactPoint(contactPointMe1);

        List<Contact> contactList = new ArrayList<>();
        contactList.add(contact);


        person.setContact(contactList);
        person.setContactList();

        PatientIdentifier patientIdentifier = new PatientIdentifier();
        patientIdentifier.setAssignerId(1);
        patientIdentifier.setType("HospitalNumber");
        if (!ViewUtils.isEmpty(edhospitalNumber)) {
            patientIdentifier.setValue(ViewUtils.getInput(edhospitalNumber));
        }
        List<PatientIdentifier> patientIdentifierList = new ArrayList<>();
        patientIdentifierList.add(patientIdentifier);

        person.setIdentifierList(patientIdentifierList);
        person.setIdentifierList();

        //Get the OrganizationUnitId from the Accounts db table
        person.setOrganizationId(account.getCurrentOrganisationUnitId());


        if (!ViewUtils.isEmpty(edNin)) {
            person.setNinNumber(ViewUtils.getInput(edNin));
        }

        return person;
    }

    @Override
    public void scrollToTop() {
        ScrollView scrollView = this.getActivity().findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, scrollView.getPaddingTop());
    }

    @Override
    public void startPMTCTEnrollmentActivity() {
        Intent pmtctActivity = new Intent(LamisPlus.getInstance(), PMTCTServicesActivity.class);
        pmtctActivity.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(mPresenter.getPatientId()));
        startActivity(pmtctActivity);
        getActivity().finish();
    }

    private void dropDownClickListeners() {
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
                    String dateOfBirth = DateUtils.getAgeFromBirthdate(Integer.parseInt(ViewUtils.getInput(edAge)));
                    edDateofBirth.setText(dateOfBirth);
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
                }
            }
        });


        autoState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);
                int resourceId = getActivity().getResources().getIdentifier(selection.replace(" ", "_").toLowerCase(), "array", getContext().getPackageName());

                if (resourceId != 0) {
                    String[] allStates = getResources().getStringArray(resourceId);

                    ArrayAdapter<String> adapterAllProvince = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allStates);
                    autoProvinceLGA.setAdapter(adapterAllProvince);
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

        autoTestedSyphilis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (autoTestedSyphilis.getText().toString().equals("Yes")) {
                    autoSyphilisTestResultView.setVisibility(View.VISIBLE);
                } else {
                    autoSyphilisTestResultView.setVisibility(View.GONE);
                }

            }
        });

        autotestResultSyphilis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (autotestResultSyphilis.getText().toString().equals("Positive")) {
                    syphilisOtherView.setVisibility(View.VISIBLE);
                } else {
                    syphilisOtherView.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void setErrorsVisibility(boolean ancNo, boolean dateOfEnrollment, boolean parity, boolean gravida, boolean lmp, boolean gaweeks, boolean sourceRef, boolean testedSyphilis, boolean hivStatus) {
        if (ancNo) {
            edancNoTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edancNoTIL.setError("This field is required");
        }

        if (dateOfEnrollment) {
            edDateEnrollmentTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edDateEnrollmentTIL.setError("This field is required");
        }

        if (parity) {
            edParityTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edParityTIL.setError("This field is required");
        }

        if (gravida) {
            edGravidaTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edGravidaTIL.setError("This field is required or check that Gravida is not less than parity");
        }

        if (lmp) {
            edLMpTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edLMpTIL.setError("This field is required");
        }

        if (gaweeks) {
            edGaWeeksTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edGaWeeksTIL.setError("This field is required");
        }

        if (sourceRef) {
            autoSourceOfReferralTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoSourceOfReferralTIL.setError("This field is required");
        }

        if (testedSyphilis) {
            autoTestedSyphilisTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoTestedSyphilisTIL.setError("This field is required");
        }

        if (hivStatus) {
            autoHivStatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoHivStatusTIL.setError("This field is required");
        }

    }

    @Override
    public void setErrorsVisibilityPatient(boolean dateOfRegisterError, boolean hospitalError, boolean firstNameError, boolean lastNameError, boolean middleNameError, boolean dateOfBirthError, boolean genderError, boolean maritalNull, boolean educationNull, boolean employmentNull, boolean phoneNull, boolean stateError, boolean provinceError, boolean addressError,
                                           boolean nokFirstNameError, boolean nokMiddleNameError, boolean nokLastNameError) {
        if (firstNameError) {
            edfirstNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edfirstNameTIL.setError("Enter the First Name or check they do not contain symbols and numbers");
        }

        if (lastNameError) {
            edLastNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edLastNameTIL.setError("Enter the Last Name or check they do not contain symbols and numbers");
        }

        if (middleNameError) {
            edMiddleNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edMiddleNameTIL.setError("Check that the Middle Name do not contain symbols and numbers");
        }

        if (dateOfBirthError) {
            edDateofBirthTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edDateofBirthTIL.setError("Please check the Date of Birth or confirm that this patient is above 10 years old");
            labelAge.setError("Please check the Date of Birth or confirm that this patient is above 10 years old");
        }

        if (dateOfRegisterError) {
            registrationDateTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            registrationDateTIL.setError("Select the Date of Registration");
        }

        if (hospitalError) {
            edhospitalNumberTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edhospitalNumberTIL.setError("Enter the Hospital Number");
        }

        if (genderError) {
            fillGenderTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            fillGenderTIL.setError("Select the Gender");
        }

        if (maritalNull) {
            fillMaritalStatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            fillMaritalStatusTIL.setError("Select the Marital Status");
        }

        if (educationNull) {
            fillEducationLevelTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            fillEducationLevelTIL.setError("Select the Education Level");
        }

        if (employmentNull) {
            fillEmploymentStatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            fillEmploymentStatusTIL.setError("Select the Employment status");
        }

        if (phoneNull) {
            edPhoneTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edPhoneTIL.setError("Enter the Phone Number");
        }

        if (stateError) {
            fillStateTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            fillStateTIL.setError("Select the State");
        }

        if (provinceError) {
            fillProvinceDistrictLGATIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            fillProvinceDistrictLGATIL.setError("Select the Province/LGA");
        }

        if (addressError) {
            addressTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            addressTIL.setError("Enter Address");
        }

        if (nokFirstNameError) {
            edNokFirstNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edNokFirstNameTIL.setError("Check that the Relationship/Next of Kin First Name do not contain symbols and numbers");
        }

        if (nokMiddleNameError) {
            edNokMiddleNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edNokMiddleNameTIL.setError("Check that the Relationship/Next of Kin Middle Name do not contain symbols and numbers");
        }

        if (nokLastNameError) {
            edNokLastNameTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edNokLastNameTIL.setError("Check that the Relationship/Next of Kin Last Name do not contain symbols and numbers");
        }

    }


}
