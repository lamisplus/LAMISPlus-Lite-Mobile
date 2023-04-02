package org.lamisplus.datafi.activities.forms.hts.clientintake;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientFragment;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestActivity;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.ContactPointClass;
import org.lamisplus.datafi.dao.AccountDAO;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.OrganizationUnitDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Account;
import org.lamisplus.datafi.models.Address;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Codesets;
import org.lamisplus.datafi.models.Contact;
import org.lamisplus.datafi.models.ContactPoint;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PatientIdentifier;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientIntakeFragment extends LamisBaseFragment<ClientIntakeContract.Presenter> implements ClientIntakeContract.View, View.OnClickListener {

    private String[] settings, targetGroup, relationshipIndex, referredFrom, typeofCounselling, pregnant;
    private AutoCompleteTextView autoTargetGroup;
    private AutoCompleteTextView autoReferredFrom;
    private AutoCompleteTextView autoSettings;
    private AutoCompleteTextView autoIndexTesting;
    private AutoCompleteTextView autoRelationshipIndex;
    private AutoCompleteTextView autoFirstimeVisit;
    private AutoCompleteTextView autoPreviouslyTested;
    private AutoCompleteTextView autoTypeCounseling;
    private AutoCompleteTextView autoPregnant;
    private AutoCompleteTextView autoBreastfeeding;

    private EditText edClientCode;
    private EditText edIndexClientCode;
    private EditText edVisitDate;
    private EditText edNoWives;
    private EditText edNumberOfChildren;

    private Button mSaveContinueButton;

    private boolean isUpdateClientIntake = false;
    private Encounter updatedForm;
    private ClientIntake updatedClientIntake;

    private String packageName;

    private LinearLayout indexTestingLayout;
    private LinearLayout pregnantLayout;
    private TextInputLayout noWivesTIL;
    private TextInputLayout autotargetGroupTIL;
    private TextInputLayout edclientCodeTIL;
    private TextInputLayout autoreferredFromTIL;
    private TextInputLayout autosettingsTIL;
    private TextInputLayout edvisitDateTIL;

    private String[] gender, maritalStatus, educationalLevel, allStates;
    private AutoCompleteTextView autoGender;
    private AutoCompleteTextView autoMaritalStatus;
    private AutoCompleteTextView autoEducationalLevel;
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
    private TextInputLayout edPhoneTIL;
    private TextInputLayout fillStateTIL;
    private TextInputLayout fillProvinceDistrictLGATIL;
    private TextInputLayout edStreetTIL;
    private TextInputLayout autoFirstTimeVisitTIL;
    private TextInputLayout autoPreviouslyTestedTIL;
    private TextInputLayout autoTypeCounselingTIL;
    private TextInputLayout autoIndexTestingTIL;
    private LinearLayout createPatientView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_client_intake, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            initViewsPatient(root);
            setHasOptionsMenu(true);
            setListeners();
            dropDownClickListeners();
            showDatePickers();
            hideFieldsDefault();
            //For patients section
            initDropDownsPatients();
            showDatePickersPatients();
            dropDownClickListenersPatients();

            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateClientIntake) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounterClientIntake(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static ClientIntakeFragment newInstance() {
        return new ClientIntakeFragment();
    }

    private void initiateFragmentViews(View root) {
        autoTargetGroup = root.findViewById(R.id.autoTargetGroup);
        autoReferredFrom = root.findViewById(R.id.autoReferredFrom);
        autoSettings = root.findViewById(R.id.autoSettings);
        autoIndexTesting = root.findViewById(R.id.autoIndexTesting);
        autoRelationshipIndex = root.findViewById(R.id.autoRelationshipIndexClient);
        autoFirstimeVisit = root.findViewById(R.id.autoFirstTimeVisit);
        autoPreviouslyTested = root.findViewById(R.id.autoPreviouslyTested);
        autoTypeCounseling = root.findViewById(R.id.autoTypeCounseling);
        edNoWives = root.findViewById(R.id.edNoWives);
        autoPregnant = root.findViewById(R.id.autoPregnant);
        autoBreastfeeding = root.findViewById(R.id.autoBreastFeeding);
        edClientCode = root.findViewById(R.id.edClientCode);
        edIndexClientCode = root.findViewById(R.id.edIndexClientCode);
        edVisitDate = root.findViewById(R.id.edVisitDate);
        edNumberOfChildren = root.findViewById(R.id.edNoChildren);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
        indexTestingLayout = root.findViewById(R.id.indexTestingLayout);
        pregnantLayout = root.findViewById(R.id.pregnantLayout);
        noWivesTIL = root.findViewById(R.id.noWivesTIL);
        autotargetGroupTIL = root.findViewById(R.id.autotargetGroupTIL);
        edclientCodeTIL = root.findViewById(R.id.edclientCodeTIL);
        autoreferredFromTIL = root.findViewById(R.id.autoreferredFromTIL);
        autosettingsTIL = root.findViewById(R.id.autosettingsTIL);
        edvisitDateTIL = root.findViewById(R.id.edvisitDateTIL);
        autoFirstTimeVisitTIL = root.findViewById(R.id.autoFirstTimeVisitTIL);
        autoPreviouslyTestedTIL = root.findViewById(R.id.autoPreviouslyTestedTIL);
        autoTypeCounselingTIL = root.findViewById(R.id.autoTypeCounselingTIL);
        autoIndexTestingTIL = root.findViewById(R.id.autoIndexTestingTIL);
    }

    public void initViewsPatient(View root) {
        autoGender = root.findViewById(R.id.fillGender);
        autoMaritalStatus = root.findViewById(R.id.fillMaritalStatus);
        autoEducationalLevel = root.findViewById(R.id.fillEducationLevel);
        autoState = root.findViewById(R.id.fillState);
        autoCountry = root.findViewById(R.id.fillCountry);
        autoProvinceLGA = root.findViewById(R.id.fillProvinceDistrictLGA);

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
        edStreetTIL = root.findViewById(R.id.edStreetTIL);
        createPatientView = root.findViewById(R.id.createPatientView);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        settings = getResources().getStringArray(R.array.settings);
        ArrayAdapter<String> adapterSettings = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, settings);
        autoSettings.setAdapter(adapterSettings);

        targetGroup = getResources().getStringArray(R.array.target_group);
        ArrayAdapter<String> adapterTargetGroup = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, targetGroup);
        autoTargetGroup.setAdapter(adapterTargetGroup);

        referredFrom = getResources().getStringArray(R.array.referred_from);
        ArrayAdapter<String> adapterReferredFrom = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, referredFrom);
        autoReferredFrom.setAdapter(adapterReferredFrom);

        relationshipIndex = getResources().getStringArray(R.array.relationship_of_the_index_client);
        ArrayAdapter<String> adapterRelationshipIndex = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, relationshipIndex);
        autoRelationshipIndex.setAdapter(adapterRelationshipIndex);

        typeofCounselling = getResources().getStringArray(R.array.type_of_counseling);
        ArrayAdapter<String> adapterTypeOfCounselling = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, typeofCounselling);
        autoTypeCounseling.setAdapter(adapterTypeOfCounselling);

        pregnant = getResources().getStringArray(R.array.pregnant);
        ArrayAdapter<String> adapterPregnant = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, pregnant);
        autoPregnant.setAdapter(adapterPregnant);

        String[] booleanAnswers = {"Yes", "No"};
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoFirstimeVisit.setAdapter(adapterBooleanAnswers);
        autoPreviouslyTested.setAdapter(adapterBooleanAnswers);
        autoBreastfeeding.setAdapter(adapterBooleanAnswers);
        autoIndexTesting.setAdapter(adapterBooleanAnswers);
    }

    private void hideFieldsDefault() {
        Person person = PersonDAO.findPersonById(mPresenter.getPatientId());
        if (person != null) {
            if (CodesetsDAO.findCodesetsDisplayById(person.getSexId()).equals("Male")) {
                pregnantLayout.setVisibility(View.GONE);
            } else {
                noWivesTIL.setVisibility(View.GONE);
            }
        }
    }

    private void dropDownClickListeners() {
        autoIndexTesting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoIndexTesting.getText().toString().equals("Yes")) {
                    indexTestingLayout.setVisibility(View.VISIBLE);
                } else {
                    indexTestingLayout.setVisibility(View.GONE);
                }
            }
        });
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
    }

    public void fillFields(ClientIntake clientIntake) {
        if (clientIntake != null) {
            createPatientView.setVisibility(View.GONE);
            isUpdateClientIntake = true;
            updatedClientIntake = clientIntake;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId());

            if (CodesetsDAO.findCodesetsDisplayByCode(clientIntake.getTargetGroup()) != null) {
                autoTargetGroup.setText(CodesetsDAO.findCodesetsDisplayByCode(clientIntake.getTargetGroup()), false);
            }

            if (CodesetsDAO.findCodesetsDisplayById(clientIntake.getReferredFrom()) != null) {
                autoReferredFrom.setText(CodesetsDAO.findCodesetsDisplayById(clientIntake.getReferredFrom()), false);
            }

            if (CodesetsDAO.findCodesetsDisplayByCode(clientIntake.getTestingSetting()) != null) {
                autoSettings.setText(CodesetsDAO.findCodesetsDisplayByCode(clientIntake.getTestingSetting()), false);
            }

            autoIndexTesting.setText(StringUtils.changeBooleanToString(clientIntake.getIndexClient()), false);

            edIndexClientCode.setText(clientIntake.getIndexClientCode());

            if (StringUtils.changeBooleanToString(clientIntake.getIndexClient()).equals("Yes")) {
                indexTestingLayout.setVisibility(View.VISIBLE);
            } else {
                indexTestingLayout.setVisibility(View.GONE);
            }

            if (CodesetsDAO.findCodesetsDisplayById(clientIntake.getRelationWithIndexClient()) != null) {
                autoRelationshipIndex.setText(CodesetsDAO.findCodesetsDisplayById(clientIntake.getRelationWithIndexClient()), false);
            }

            autoFirstimeVisit.setText(StringUtils.changeBooleanToString(clientIntake.isFirstTimeVisit()), false);

            if (clientIntake.getPreviouslyTested() != null) {
                autoPreviouslyTested.setText(StringUtils.changeBooleanToString(clientIntake.getPreviouslyTested()), false);
            }
            if (CodesetsDAO.findCodesetsDisplayById(clientIntake.getTypeCounseling()) != null) {
                autoTypeCounseling.setText(CodesetsDAO.findCodesetsDisplayById(clientIntake.getTypeCounseling()), false);
            }
            edNoWives.setText(String.valueOf(clientIntake.getNumWives()));
            if (clientIntake.getPregnant() != null) {
                autoPregnant.setText(CodesetsDAO.findCodesetsDisplayById(clientIntake.getPregnant()), false);
            }
            autoBreastfeeding.setText(StringUtils.changeBooleanToString(clientIntake.isBreastFeeding()), false);
            edClientCode.setText(clientIntake.getClientCode());
            edVisitDate.setText(clientIntake.getDateVisit());
            edNumberOfChildren.setText(String.valueOf(clientIntake.getNumChildren()));
        }
    }

    private ClientIntake createEncounter() {
        ClientIntake clientIntake = new ClientIntake();
        updateEncounterWithData(clientIntake);
        return clientIntake;
    }

    private Person createPatient() {
        Person person = new Person();
        updatePatientWithData(person);
        return person;
    }

    private ClientIntake updateEncounter(ClientIntake clientIntake) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(clientIntake);
        return clientIntake;
    }

    private ClientIntake updateEncounterWithData(ClientIntake clientIntake) {
        if (!ViewUtils.isEmpty(autoTargetGroup)) {
            clientIntake.setTargetGroup(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoTargetGroup)));
        }

        if (!ViewUtils.isEmpty(edClientCode)) {
            clientIntake.setClientCode(ViewUtils.getInput(edClientCode));
        }

        if (!ViewUtils.isEmpty(autoReferredFrom)) {
            clientIntake.setReferredFrom(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoReferredFrom)));
        }

        if (!ViewUtils.isEmpty(autoSettings)) {
            clientIntake.setTestingSetting(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoSettings)));
        }

        if (!ViewUtils.isEmpty(edVisitDate)) {
            clientIntake.setDateVisit(ViewUtils.getInput(edVisitDate));
        }

        if (!ViewUtils.isEmpty(edNumberOfChildren)) {
            clientIntake.setNumChildren(Integer.parseInt(ViewUtils.getInput(edNumberOfChildren)));
        }

        if (!ViewUtils.isEmpty(autoIndexTesting)) {
            clientIntake.setIndexClient(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoIndexTesting)));
        }

        if (!ViewUtils.isEmpty(edIndexClientCode)) {
            clientIntake.setIndexClientCode(ViewUtils.getInput(edIndexClientCode));
        }

        if (!ViewUtils.isEmpty(edNoWives)) {
            clientIntake.setNumWives(Integer.parseInt(ViewUtils.getInput(edNoWives)));
        }

        if (!ViewUtils.isEmpty(autoPregnant)) {
            clientIntake.setPregnant(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoPregnant)));
        }

        if (!ViewUtils.isEmpty(autoBreastfeeding)) {
            clientIntake.setBreastFeeding(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoBreastfeeding)));
        }

        if (!ViewUtils.isEmpty(autoFirstimeVisit)) {
            clientIntake.setFirstTimeVisit(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoFirstimeVisit)));
        }

        if (!ViewUtils.isEmpty(autoPreviouslyTested)) {
            clientIntake.setPreviouslyTested(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoPreviouslyTested)));
        }

        if (!ViewUtils.isEmpty(autoTypeCounseling)) {
            clientIntake.setTypeCounseling(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoTypeCounseling)));
        }

        if (!ViewUtils.isEmpty(autoRelationshipIndex)) {
            clientIntake.setRelationWithIndexClient(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoRelationshipIndex)));
        }

        if (updatedForm != null) {
            if (updatedForm.getPersonId() != null) {
                clientIntake.setPersonId(updatedForm.getPersonId());
            } else {
                clientIntake.setPersonId(0);
            }
        } else {
            clientIntake.setPersonId(0);
        }

        clientIntake.setExtra("{}");

        RiskStratification rst = EncounterDAO.findRstFromForm(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId());
        if (rst != null) {
            clientIntake.setRiskStratificationCode(rst.getCode());
        } else {
            clientIntake.setRiskStratificationCode("");
        }
        return clientIntake;
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

        Account account = AccountDAO.getUserDetails();
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


        PatientIdentifier patientIdentifier = new PatientIdentifier();
        patientIdentifier.setAssignerId(1);
        patientIdentifier.setType("HospitalNumber");
        //Let us replace hospital number now with client code
        if (!ViewUtils.isEmpty(edClientCode)) {
            patientIdentifier.setValue(ViewUtils.getInput(edClientCode));
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if (isUpdateClientIntake) {
                    mPresenter.confirmUpdate(updateEncounter(updatedClientIntake), updatedForm);
                } else {
                    mPresenter.confirmCreate(createEncounter(), createPatient(), packageName);
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


    @Override
    public void scrollToTop() {
        ScrollView scrollView = this.getActivity().findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, scrollView.getPaddingTop());
    }

    @Override
    public void startActivityForPreTestForm(String patientId) {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            Intent preTestProgram = new Intent(LamisPlus.getInstance(), PreTestActivity.class);
            preTestProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, patientId);
            startActivity(preTestProgram);
            getActivity().finish();
        } else {
            startHTSActivity();
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
    public void retreiveRSTFormData(String rstForm) {
        RiskStratification rst = new Gson().fromJson(rstForm, RiskStratification.class);
        if (rst != null) {
            if (rst.getTestingSetting() != null) {
                autoSettings.setText(CodesetsDAO.findCodesetsDisplayByCode(rst.getTestingSetting()), false);
            }

            if (rst.getTargetGroup() != null) {
                autoTargetGroup.setText(CodesetsDAO.findCodesetsDisplayByCode(rst.getTargetGroup()), false);
            }

            edVisitDate.setText(rst.getVisitDate());
            edRegistrationDate.setText(rst.getVisitDate());
            edDateofBirth.setText(rst.getDob());
            edAge.setText(rst.getAge()+"");
        }

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
    public void setErrorsVisibility(boolean targetGroup, boolean clientCode, boolean referredFrom, boolean settings, boolean visitDate, boolean firstTimeVisitError, boolean previouslyTestedError, boolean typeCounselingError, boolean indexTestingError) {
        if (targetGroup) {
            autotargetGroupTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autotargetGroupTIL.setError("Select Target Group");
        }

        if (clientCode) {
            edclientCodeTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edclientCodeTIL.setError("Enter the Client Code");
        }

        if (referredFrom) {
            autoreferredFromTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoreferredFromTIL.setError("Select Referred From");
        }

        if (settings) {
            autosettingsTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autosettingsTIL.setError("Select Settings");
        }

        if (visitDate) {
            edvisitDateTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edvisitDateTIL.setError("Enter Visit Date");
        }

        if (firstTimeVisitError) {
            autoFirstTimeVisitTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoFirstTimeVisitTIL.setError("Select First time visit");
        }

        if (previouslyTestedError) {
            autoPreviouslyTestedTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoPreviouslyTestedTIL.setError("Select Previously Tested");
        }

        if (typeCounselingError) {
            autoTypeCounselingTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoTypeCounselingTIL.setError("Select Type of Counseling");
        }

        if (indexTestingError) {
            autoIndexTestingTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoIndexTestingTIL.setError("Select Index Testing");
        }


    }

    @Override
    public void setErrorsVisibilityPatient(boolean firstNameError, boolean lastNameError, boolean middleNameError, boolean dateOfBirthError, boolean genderError,
                                           boolean maritalNull, boolean educationNull, boolean phoneNull, boolean stateError, boolean provinceError, boolean addressError) {
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
            edDateofBirthTIL.setError("Select the Date of Birth");
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
            edStreetTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edStreetTIL.setError("Enter the Address");
        }
    }


    public void initDropDownsPatients() {
        gender = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, gender);
        autoGender.setAdapter(adapterGender);

        maritalStatus = getResources().getStringArray(R.array.marital_status);
        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, maritalStatus);
        autoMaritalStatus.setAdapter(adapterMaritalStatus);

        educationalLevel = getResources().getStringArray(R.array.educational_level);
        ArrayAdapter<String> adapterEducationalLevel = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, educationalLevel);
        autoEducationalLevel.setAdapter(adapterEducationalLevel);

        autoCountry.setText("Nigeria");

        allStates = getResources().getStringArray(R.array.nigeria);
        ArrayAdapter<String> adapterAllStates = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allStates);
        autoState.setAdapter(adapterAllStates);

    }

    private void dropDownClickListenersPatients() {
        autoGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoGender.getText().toString().equals("Male")) {
                    pregnantLayout.setVisibility(View.GONE);
                    noWivesTIL.setVisibility(View.VISIBLE);
                } else {
                    pregnantLayout.setVisibility(View.VISIBLE);
                    noWivesTIL.setVisibility(View.GONE);
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

        autoMaritalStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (autoMaritalStatus.getText().toString().equals("Single")) {
                    noWivesTIL.setVisibility(View.GONE);
                } else {
                    noWivesTIL.setVisibility(View.VISIBLE);
                }

                if(autoGender.getText().toString().equals("Female")) {
                    noWivesTIL.setVisibility(View.GONE);
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

    }

    private void showDatePickersPatients() {
        edRegistrationDate.setOnClickListener(v -> {
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
                edRegistrationDate.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

//        edDateofBirth.setOnClickListener(v -> {
//            int cYear;
//            int cMonth;
//            int cDay;
//
//            Calendar currentDate = Calendar.getInstance();
//            cYear = currentDate.get(Calendar.YEAR);
//            cMonth = currentDate.get(Calendar.MONTH);
//            cDay = currentDate.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
//                int adjustedMonth = selectedMonth + 1;
//                String stringMonth = String.format("%02d", adjustedMonth);
//                String stringDay = String.format("%02d", selectedDay);
//                edDateofBirth.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
//
//                int age = DateUtils.getAgeFromBirthdateString(selectedYear + "-" + stringMonth + "-" + stringDay);
//
//                edAge.setText(age + "");
//            }, cYear, cMonth, cDay);
//            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
//            mDatePicker.setTitle(getString(R.string.date_picker_title));
//            mDatePicker.show();
//        });
    }


}
