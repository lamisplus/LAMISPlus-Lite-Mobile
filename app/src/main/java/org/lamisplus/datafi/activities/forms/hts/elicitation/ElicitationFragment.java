package org.lamisplus.datafi.activities.forms.hts.elicitation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientFragment;
import org.lamisplus.datafi.activities.forms.hts.requestresult.RequestResultActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Elicitation;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;

public class ElicitationFragment extends LamisBaseFragment<ElicitationContract.Presenter> implements ElicitationContract.View, View.OnClickListener {


    private EditText edfirstName;
    private EditText edMiddleName;
    private EditText edLastName;
    private AutoCompleteTextView autoSex;
    private EditText eddob;
    private EditText edAge;
    private EditText edphoneNumber;
    private EditText edaltPhoneNumber;
    private EditText edaddress;
    private AutoCompleteTextView autophysicalHurt;
    private AutoCompleteTextView autothreatenToHurt;
    private EditText edhangOutSpots;
    private AutoCompleteTextView autorelativeToIndexClient;
    private AutoCompleteTextView autocurrentlyLiveWithPartner;
    private AutoCompleteTextView autopartnerTestedPositive;
    private AutoCompleteTextView autosexuallyUncomfortable;
    private AutoCompleteTextView autonotificationMethod;
    private EditText eddatePartnerCameForTesting;
    private TextInputLayout labelDateofBirth;
    private TextInputLayout labelAge;

    private RadioGroup rpDateofBirth;

    private RadioGroup rgDateOfBirthSelect;

    private Button mSaveContinueButton;

    private boolean isUpdateElicitation = false;
    private Encounter updatedForm;
    private Elicitation updatedElicitation;
    private String packageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_elicitation, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.ELICITATION, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.ELICITATION, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateElicitation) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounterElicitation(ApplicationConstants.Forms.ELICITATION, mPresenter.getPatientId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static ElicitationFragment newInstance() {
        return new ElicitationFragment();
    }

    private void initiateFragmentViews(View root) {
        edfirstName = root.findViewById(R.id.edfirstName);
        edMiddleName = root.findViewById(R.id.edMiddleName);
        edLastName = root.findViewById(R.id.edLastName);
        autoSex = root.findViewById(R.id.autoSex);
        eddob = root.findViewById(R.id.eddob);
        edAge = root.findViewById(R.id.edAge);
        edphoneNumber = root.findViewById(R.id.edphoneNumber);
        edaltPhoneNumber = root.findViewById(R.id.edaltPhoneNumber);
        edaddress = root.findViewById(R.id.edaddress);
        autophysicalHurt = root.findViewById(R.id.autophysicalHurt);
        autothreatenToHurt = root.findViewById(R.id.autothreatenToHurt);
        edhangOutSpots = root.findViewById(R.id.edhangOutSpots);
        autorelativeToIndexClient = root.findViewById(R.id.autorelativeToIndexClient);
        autocurrentlyLiveWithPartner = root.findViewById(R.id.autocurrentlyLiveWithPartner);
        autopartnerTestedPositive = root.findViewById(R.id.autopartnerTestedPositive);
        autosexuallyUncomfortable = root.findViewById(R.id.autosexuallyUncomfortable);
        autonotificationMethod = root.findViewById(R.id.autonotificationMethod);
        eddatePartnerCameForTesting = root.findViewById(R.id.eddatePartnerCameForTesting);

        rpDateofBirth = root.findViewById(R.id.rgdateOfBirthSelect);
        labelDateofBirth = root.findViewById(R.id.labelDateofBirth);
        labelAge = root.findViewById(R.id.labelAge);

        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        String[] gender = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, gender);
        autoSex.setAdapter(adapterGender);

        String[] relationShipIndexClient = getResources().getStringArray(R.array.relationship_of_the_index_client);
        ArrayAdapter<String> adapterRelationshipIndexClient = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, relationShipIndexClient);
        autorelativeToIndexClient.setAdapter(adapterRelationshipIndexClient);

        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);

        autocurrentlyLiveWithPartner.setAdapter(adapterBooleanAnswers);

        String[] booleanAnswersExtra = getResources().getStringArray(R.array.booleanAnswersExtra);
        ArrayAdapter<String> adapterBooleanAnswersExtra = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswersExtra);

        autopartnerTestedPositive.setAdapter(adapterBooleanAnswersExtra);
        autothreatenToHurt.setAdapter(adapterBooleanAnswersExtra);
        autophysicalHurt.setAdapter(adapterBooleanAnswersExtra);
        autosexuallyUncomfortable.setAdapter(adapterBooleanAnswersExtra);

        String[] notificationMethod = getResources().getStringArray(R.array.notification_method);
        ArrayAdapter<String> adapternotificationMethod = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, notificationMethod);
        autonotificationMethod.setAdapter(adapternotificationMethod);

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
        eddob.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(this.getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay =  String.format("%02d", selectedDay);
                eddob.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        eddatePartnerCameForTesting.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(this.getActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay =  String.format("%02d", selectedDay);
                eddatePartnerCameForTesting.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
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
                if (isUpdateElicitation) {
                    mPresenter.confirmUpdate(updateEncounter(updatedElicitation), updatedForm);
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

    public void fillFields(Elicitation elicitation) {
        if (elicitation != null) {
            isUpdateElicitation = true;
            updatedElicitation = elicitation;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.ELICITATION, mPresenter.getPatientId());

            edfirstName.setText(elicitation.getFirstName());

            edMiddleName.setText(elicitation.getMiddleName());

            edLastName.setText(elicitation.getLastName());

            autoSex.setText(CodesetsDAO.findCodesetsDisplayById(elicitation.getSex()), false);

            eddob.setText(elicitation.getDob());

            edAge.setText(elicitation.getAge());

            edphoneNumber.setText(elicitation.getPhoneNumber());

            edaltPhoneNumber.setText(elicitation.getAltPhoneNumber());

            edaddress.setText(elicitation.getAddress());

            autophysicalHurt.setText(CodesetsDAO.findCodesetsDisplayById(elicitation.getPhysicalHurt()), false);

            autothreatenToHurt.setText(CodesetsDAO.findCodesetsDisplayById(elicitation.getThreatenToHurt()), false);

            edhangOutSpots.setText(elicitation.getHangOutSpots());

            autorelativeToIndexClient.setText(CodesetsDAO.findCodesetsDisplayById(elicitation.getRelativeToIndexClient()), false);

            autocurrentlyLiveWithPartner.setText(CodesetsDAO.findCodesetsDisplayById(elicitation.getCurrentlyLiveWithPartner()), false);

            autopartnerTestedPositive.setText(CodesetsDAO.findCodesetsDisplayById(elicitation.getPartnerTestedPositive()), false);

            autosexuallyUncomfortable.setText(CodesetsDAO.findCodesetsDisplayById(elicitation.getSexuallyUncomfortable()), false);

            autonotificationMethod.setText(CodesetsDAO.findCodesetsDisplayById(elicitation.getNotificationMethod()), false);

            eddatePartnerCameForTesting.setText(elicitation.getDatePartnerCameForTesting());

        }
    }

    private Elicitation createEncounter() {
        Elicitation elicitation = new Elicitation();
        updateEncounterWithData(elicitation);
        return elicitation;
    }

    private Elicitation updateEncounter(Elicitation elicitation) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(elicitation);
        return elicitation;
    }

    private Elicitation updateEncounterWithData(Elicitation elicitation) {
        if (!ViewUtils.isEmpty(edfirstName)) {
            elicitation.setFirstName(ViewUtils.getInput(edfirstName));
        }

        if (!ViewUtils.isEmpty(edMiddleName)) {
            elicitation.setMiddleName(ViewUtils.getInput(edMiddleName));
        }

        if (!ViewUtils.isEmpty(edLastName)) {
            elicitation.setLastName(ViewUtils.getInput(edLastName));
        }

        if (!ViewUtils.isEmpty(autoSex)) {
            elicitation.setSex(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoSex)));
        }

        if (!ViewUtils.isEmpty(edAge) && ViewUtils.isEmpty(eddob)) {
            String dateOfBirth = DateUtils.getAgeFromBirthdate(Integer.parseInt(ViewUtils.getInput(edAge)));
            elicitation.setDob(dateOfBirth);
            elicitation.setDateOfBirthEstimated(true);
        }

        if (!ViewUtils.isEmpty(eddob) && ViewUtils.isEmpty(edAge)) {
            elicitation.setDob(ViewUtils.getInput(eddob));
        }

        if (!ViewUtils.isEmpty(edAge)) {
            elicitation.setAge(ViewUtils.getInput(edAge));
        }

        if (!ViewUtils.isEmpty(edphoneNumber)) {
            elicitation.setPhoneNumber(ViewUtils.getInput(edphoneNumber));
        }

        if (!ViewUtils.isEmpty(edaltPhoneNumber)) {
            elicitation.setAltPhoneNumber(ViewUtils.getInput(edaltPhoneNumber));
        }

        if (!ViewUtils.isEmpty(edaddress)) {
            elicitation.setAddress(ViewUtils.getInput(edaddress));
        }

        if (!ViewUtils.isEmpty(autophysicalHurt)) {
            elicitation.setPhysicalHurt(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autophysicalHurt)));
        }

        if (!ViewUtils.isEmpty(autothreatenToHurt)) {
            //Log.v("Baron", "Threaten to Hurt " + CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autothreatenToHurt)));
            elicitation.setThreatenToHurt(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autothreatenToHurt)));
        }

        if (!ViewUtils.isEmpty(edhangOutSpots)) {
            elicitation.setHangOutSpots(ViewUtils.getInput(edhangOutSpots));
        }


        if (!ViewUtils.isEmpty(autorelativeToIndexClient)) {
            elicitation.setRelativeToIndexClient(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autorelativeToIndexClient)));
        }

        if (!ViewUtils.isEmpty(autocurrentlyLiveWithPartner)) {
            elicitation.setCurrentlyLiveWithPartner(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autocurrentlyLiveWithPartner)));
        }

        if (!ViewUtils.isEmpty(autopartnerTestedPositive)) {
            elicitation.setPartnerTestedPositive(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autopartnerTestedPositive)));
        }

        if (!ViewUtils.isEmpty(autosexuallyUncomfortable)) {
            elicitation.setSexuallyUncomfortable(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autosexuallyUncomfortable)));
        }

        if (!ViewUtils.isEmpty(autonotificationMethod)) {
            elicitation.setNotificationMethod(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autonotificationMethod)));
        }

        if (!ViewUtils.isEmpty(eddatePartnerCameForTesting)) {
            elicitation.setDatePartnerCameForTesting(ViewUtils.getInput(eddatePartnerCameForTesting));
        }

        LamisCustomHandler.showJson(elicitation);
        return elicitation;
    }


    @Override
    public void startDashboardActivity() {
        Intent elicitationProgram = new Intent(LamisPlus.getInstance(), PatientDashboardActivity.class);
        elicitationProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(mPresenter.getPatientId()));
        startActivity(elicitationProgram);
    }

}
