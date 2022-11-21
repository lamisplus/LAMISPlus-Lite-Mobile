package org.lamisplus.datafi.activities.addeditpatient;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.ContactPointClass;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.models.Address;
import org.lamisplus.datafi.models.Contact;
import org.lamisplus.datafi.models.ContactPoint;
import org.lamisplus.datafi.models.PatientIdentifier;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddEditPatientFragment extends LamisBaseFragment<AddEditPatientContract.Presenter> implements AddEditPatientContract.View {

    private String[] gender, maritalStatus, employmentStatus, educationalLevel, allStates, allRelationShipNok;
    private AutoCompleteTextView autoGender;
    private AutoCompleteTextView autoMaritalStatus;
    private AutoCompleteTextView autoEmploymentStatus;
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
    private AutoCompleteTextView autoNokRelationship;
    private EditText edNokFirstName;
    private EditText edNokMiddleName;
    private EditText edNokLastName;
    private EditText edNokPhone;
    private EditText edNokemail;
    private EditText edNokAddress;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_patient, container, false);
        setHasOptionsMenu(true);
        if (root != null) {
            initViews(root);
            initDropDowns();
            dropDownClickListeners();
            showDatePickers();
            fillFields(mPresenter.patientToUpdate());
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.submit_done_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.actionSubmit:
                submitAction();
                return true;
            default:
                // Do nothing
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitAction() {
        if (isUpdatePatient) {
            mPresenter.confirmUpdate(updatePatient(updatedPatient));
        } else {
            mPresenter.confirmRegister(createPatient());
        }
    }

    private Person updatePatient(Person person) {
        Person.load(Person.class, mPresenter.getPatientId());
        updatePatientWithData(person);
        return person;
    }

    public static AddEditPatientFragment newinstance() {
        return new AddEditPatientFragment();
    }

    public void initViews(View root) {
        autoGender = root.findViewById(R.id.fillGender);
        autoMaritalStatus = root.findViewById(R.id.fillMaritalStatus);
        autoEmploymentStatus = root.findViewById(R.id.fillEmploymentStatus);
        autoEducationalLevel = root.findViewById(R.id.fillEducationLevel);
        autoState = root.findViewById(R.id.fillState);
        autoCountry = root.findViewById(R.id.fillCountry);
        autoProvinceLGA = root.findViewById(R.id.fillProvinceDistrictLGA);
        autoNokRelationship = root.findViewById(R.id.edNokfillRelationshipType);

        rpDateofBirth = root.findViewById(R.id.rgdateOfBirthSelect);
        labelDateofBirth = root.findViewById(R.id.labelDateofBirth);
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
        edDateofBirth = root.findViewById(R.id.edDateofBirth);
        edAge = root.findViewById(R.id.edAge);

        edNokFirstName = root.findViewById(R.id.edNokFirstName);
        edNokMiddleName = root.findViewById(R.id.edNokMiddleName);
        edNokLastName = root.findViewById(R.id.edNokLastName);
        edNokPhone = root.findViewById(R.id.edNokPhone);
        edNokemail = root.findViewById(R.id.edNokEmail);
        edNokAddress = root.findViewById(R.id.edNokAddress);

        progressBar = root.findViewById(R.id.progress_bar);
    }

    public void initDropDowns() {
        gender = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, gender);
        autoGender.setAdapter(adapterGender);

        maritalStatus = getResources().getStringArray(R.array.marital_status);
        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, maritalStatus);
        autoMaritalStatus.setAdapter(adapterMaritalStatus);

        employmentStatus = getResources().getStringArray(R.array.employment_status);
        ArrayAdapter<String> adapterEmploymentStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, employmentStatus);
        autoEmploymentStatus.setAdapter(adapterEmploymentStatus);

        educationalLevel = getResources().getStringArray(R.array.educational_level);
        ArrayAdapter<String> adapterEducationalLevel = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, educationalLevel);
        autoEducationalLevel.setAdapter(adapterEducationalLevel);

        autoCountry.setText("Nigeria");

        allStates = getResources().getStringArray(R.array.nigeria);
        ArrayAdapter<String> adapterAllStates = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allStates);
        autoState.setAdapter(adapterAllStates);

        allRelationShipNok = getResources().getStringArray(R.array.relation_next_of_kin);
        ArrayAdapter<String> adapterAllRelNok = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allRelationShipNok);
        autoNokRelationship.setAdapter(adapterAllRelNok);

    }

    private void dropDownClickListeners() {
//        autoGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), autoGender.getText().toString(), Toast.LENGTH_LONG).show();
//            }
//        });

        autoState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);
                int resourceId = getActivity().getResources().getIdentifier(selection.toLowerCase(), "array", getContext().getPackageName());

                if (resourceId != 0) {
                    String[] allStates = getResources().getStringArray(resourceId);

                    LamisCustomHandler.showJson(allStates);

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
                    labelDateofBirth.setVisibility(View.VISIBLE);
                    labelAge.setVisibility(View.GONE);
                } else {
                    labelDateofBirth.setVisibility(View.GONE);
                    labelAge.setVisibility(View.VISIBLE);
                }
            }
        });

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

            DatePickerDialog mDatePicker = new DatePickerDialog(AddEditPatientFragment.this.getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edRegistrationDate.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateofBirth.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(AddEditPatientFragment.this.getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                edDateofBirth.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });
    }

    private void fillFields(final Person person) {
        if (person != null) {
            String updatePatientStr = getResources().getString(R.string.action_update_patient_data);
            this.getActivity().setTitle(updatePatientStr);

            isUpdatePatient = true;
            updatedPatient = person;

            edRegistrationDate.setText(person.getDateOfRegistration());
            edFirstName.setText(person.getFirstName());
            edLastName.setText(person.getSurname());
            edMiddleName.setText(person.getOtherName());
            edDateofBirth.setText(person.getDateOfBirth());

            autoGender.setText(CodesetsDAO.findCodesetsDisplayById(person.getGenderId()), false);
            autoMaritalStatus.setText(CodesetsDAO.findCodesetsDisplayById(person.getMaritalStatusId()), false);
            autoEmploymentStatus.setText(CodesetsDAO.findCodesetsDisplayById(person.getEmploymentStatusId()), false);
            autoEducationalLevel.setText(CodesetsDAO.findCodesetsDisplayById(person.getEducationId()), false);
            autoState.setText(person.getAddresses().getStateId(), false);
            autoProvinceLGA.setText(person.getAddresses().getDistrict(), false);
            edStreet.setText(person.getAddresses().getCity());
            edLandmark.setText(person.getAddresses().getLine()[0]);

            edhospitalNumber.setText(person.getIdentifiers().getValue());
            edNin.setText(person.getNinNumber());

            person.pullContactPointList();
            List<ContactPoint> contactPointList = person.pullContactPointList();

            if (contactPointList.size() >= 3 && contactPointList != null) {
                if (contactPointList.get(0).getValue() != null && contactPointList.get(0).getType().equals("phone")) {
                    edPhone.setText(contactPointList.get(0).getValue());
                }
                if (contactPointList.get(1).getValue() != null && contactPointList.get(1).getType().equals("email")) {
                    edEmail.setText(contactPointList.get(1).getValue());
                }
                if (contactPointList.get(2).getValue() != null && contactPointList.get(2).getType().equals("altphone")) {
                    edAltPhone.setText(contactPointList.get(2).getValue());
                }
            }

            edNokFirstName.setText(person.getContacts().getFirstName());
            edNokLastName.setText(person.getContacts().getSurname());
            edNokMiddleName.setText(person.getContacts().getOtherName());
            autoNokRelationship.setText(CodesetsDAO.findCodesetsDisplayById(person.getContacts().getRelationshipId()), false);

            if(person.getContacts().getContactPoint().size() >= 2 && person.getContacts().getContactPoint() != null){
                if(person.getContacts().getContactPoint().get(0).getValue() != null && person.getContacts().getContactPoint().get(0).getType().equals("phone")) {
                    edNokPhone.setText(person.getContacts().getContactPoint().get(0).getValue());
                }

                if(person.getContacts().getContactPoint().get(1).getValue() != null && person.getContacts().getContactPoint().get(1).getType().equals("email")) {
                    edNokemail.setText(person.getContacts().getContactPoint().get(1).getValue());
                }
            }

            if(person.getContacts().getAddress() != null){
                String[] contactAddress = person.getContacts().getAddress();
                edNokAddress.setText(contactAddress[0]);
            }
        }
    }

    private Person createPatient() {
        Person person = new Person();
        updatePatientWithData(person);
        return person;
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

        if (!ViewUtils.isEmpty(edMiddleName)) {
            person.setOtherName(ViewUtils.getInput(edMiddleName));
        }

        if (!ViewUtils.isEmpty(edDateofBirth)) {
            person.setDateOfBirth(ViewUtils.getInput(edDateofBirth));
        }

        person.setDeceased(false);

        person.setDeceasedDateTime("");

        person.setEmrId("");

        person.setFacilityId(0);

        if (!ViewUtils.isEmpty(autoGender)) {
            int genderId = CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoGender));
            person.setGenderId(genderId);
            person.setSexId(genderId);
        }

        int radioButtonID = rpDateofBirth.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) rpDateofBirth.findViewById(radioButtonID);
        String selectedText = (String) radioButton.getText();
        Log.v("Baron", "Selected Radio Button is " + selectedText);

        if (!ViewUtils.isEmpty(autoMaritalStatus)) {
            int maritalStatusId = CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoMaritalStatus));
            person.setMaritalStatusId(maritalStatusId);
        }

        if (!ViewUtils.isEmpty(autoEmploymentStatus)) {
            int employmentStatusId = CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoEmploymentStatus));
            person.setEmploymentStatusId(employmentStatusId);
        }

        if (!ViewUtils.isEmpty(autoEducationalLevel)) {
            int educationLevelId = CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoEducationalLevel));
            person.setEducationId(educationLevelId);
        }

//        ContactPoint contactPoint = new ContactPoint();
//        List<ContactPoint> contactPointList = new ArrayList<>();
//
//        if (!ViewUtils.isEmpty(edPhone)) {
//            contactPoint.setPhone(ViewUtils.getInput(edPhone));
//        }
//        contactPoint.setAltphone(ViewUtils.getInput(edAltPhone));
//        contactPoint.setEmail(ViewUtils.getInput(edEmail));
//        contactPointList.add(contactPoint);
//
//        person.setContactPoint(contactPointList);
//        person.setContactPointList();


        List<ContactPointClass.ContactPointItems> contactPointItems = new ArrayList<>();
        contactPointItems.add(new ContactPointClass.ContactPointItems("phone", ViewUtils.getInput(edPhone)));
        contactPointItems.add(new ContactPointClass.ContactPointItems("email", ViewUtils.getInput(edEmail)));
        contactPointItems.add(new ContactPointClass.ContactPointItems("altphone", ViewUtils.getInput(edAltPhone)));

        String json = new Gson().toJson(contactPointItems);

//        Type type = new TypeToken<List<ContactPoint>>(){}.getType();
//        List<ContactPoint> contactPoints = new Gson().fromJson(json, type);
        person.setContactPoint(json);

//        contactPoints.get(0).getType();
//        LamisCustomHandler.showJson(contactPoints.get(0).getType());

//        person.setContactPoint(contactPointClasses);
//        person.setContactPointList();


        Address address = new Address();
        address.setCountryId(1);

        if (!ViewUtils.isEmpty(autoState)) {
            address.setStateId(ViewUtils.getInput(autoState));
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
        contact.setFirstName(ViewUtils.getInput(edNokFirstName));
        contact.setSurname(ViewUtils.getInput(edNokLastName));
        contact.setOtherName(ViewUtils.getInput(edNokMiddleName));

        contact.setRelationshipId(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoNokRelationship)));
        contact.setLine(new String[]{ViewUtils.getInput(edNokAddress)});


        String[] li = new String[]{ViewUtils.getInput(edNokAddress)};
        String jsonContactAdd = new Gson().toJson(new Contact.Address(li));

        contact.setAddress(jsonContactAdd);

        List<Contact> contactList = new ArrayList<>();
        contactList.add(contact);

        List<ContactPointClass.ContactPointItems> contactPoint = new ArrayList<>();
        contactPoint.add(new ContactPointClass.ContactPointItems("phone", ViewUtils.getInput(edNokPhone)));
        contactPoint.add(new ContactPointClass.ContactPointItems("email", ViewUtils.getInput(edNokemail)));

        String contactPointJson = new Gson().toJson(contactPoint);

        contact.setContactPoint(contactPointJson);

        LamisCustomHandler.showJson(contact);

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

        person.setOrganizationId(0);


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
    public void hideSoftKeys() {
        View view = this.getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(this.getActivity());
        }
        InputMethodManager inputMethodManager = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void startPatientDashbordActivity(Person person) {
        Intent intent = new Intent(getActivity(), PatientDashboardActivity.class);
        intent.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, person.getId());
        startActivity(intent);
    }

    @Override
    public boolean areFieldsNotEmpty() {
        return (!ViewUtils.isEmpty(edFirstName) ||
                (!ViewUtils.isEmpty(edLastName)) ||
                (!ViewUtils.isEmpty(edhospitalNumber)) ||
//                (!ViewUtils.isEmpty(edunique)) ||
                (!ViewUtils.isEmpty(edRegistrationDate)) ||
                (!ViewUtils.isEmpty(edDateofBirth)) ||
                (!ViewUtils.isEmpty(autoGender)) ||
                (!ViewUtils.isEmpty(autoMaritalStatus)) ||
                (!ViewUtils.isEmpty(autoEmploymentStatus)) ||
                (!ViewUtils.isEmpty(autoEducationalLevel)) ||
                (!ViewUtils.isEmpty(edPhone)) ||
                (!ViewUtils.isEmpty(autoState)) ||
                (!ViewUtils.isEmpty(autoProvinceLGA))
        );
    }

    @Override
    public void setProgressBarVisibility(boolean visibility) {
        progressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}
