package org.lamisplus.datafi.activities.forms.pmtct.labourdelivery;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.activities.pmtct.pmtctservices.PMTCTServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;
import java.util.Objects;

public class LabourDeliveryFragment extends LamisBaseFragment<LabourDeliveryContract.Presenter> implements LabourDeliveryContract.View, View.OnClickListener {

    private Button mSaveContinueButton;
    private EditText edancNo;
    private AutoCompleteTextView autoBookingStatus;
    private EditText edDateOfDelivery;
    private EditText edGaWeeks;
    private AutoCompleteTextView autoRomDeliveryInterval;
    private AutoCompleteTextView autoModeDelivery;
    private AutoCompleteTextView autoEpisiotomy;
    private AutoCompleteTextView autoVaginalTear;
    private AutoCompleteTextView autoFeedingDecision;
    private AutoCompleteTextView autoChildGivenArvWithin72;
    private AutoCompleteTextView autoOnArt;
    private AutoCompleteTextView autoHivExposedInfantGivenHbWithin24hrs;
    private AutoCompleteTextView autoDeliveryTime;
    private AutoCompleteTextView autoArtStartedLdWard;
    private AutoCompleteTextView autoHbstatus;
    private AutoCompleteTextView autoHcstatus;
    private AutoCompleteTextView autoMaternalOutcome;
    private AutoCompleteTextView autoChildStatus;
    private EditText edNumberOfInfantsAlive;
    private EditText edNumberOfInfantsDead;
    private boolean isUpdateLabourDelivery = false;
    private Encounter updatedForm;
    private LabourDelivery updatedLabourDelivery;
    private TextInputLayout autoBookingStatusTIL;
    private TextInputLayout dateEnrollmentTIL;
    private TextInputLayout autoRomDeliveryTIL;
    private TextInputLayout autoModeDeliveryTIL;
    private TextInputLayout autoEpisiotomyTIL;
    private TextInputLayout autoVaginalTearTIL;
    private TextInputLayout autoFeedingDecisionTIL;
    private TextInputLayout autoChildGivenArvWithin72TIL;
    private TextInputLayout autoOnArtTIL;
    private TextInputLayout autoHivExposedInfantGivenHbWithin24hrsTIL;
    private TextInputLayout autoDeliveryTimeTIL;
    private TextInputLayout autoArtStartedLdWardTIL;
    private TextInputLayout autoHbstatusTIL;
    private TextInputLayout autoHcstatusTIL;
    private TextInputLayout autoMaternalOutcomeTIL;
    private TextInputLayout autoChildStatusTIL;
    private TextInputLayout edNumberOfInfantsAliveTIL;
    private TextInputLayout edNumberOfInfantsDeadTIL;
    private LinearLayout childStatusDependentView;

    private String packageName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_labour_delivery, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            initDropDownsPatients();
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM, mPresenter.getPatientId()));
            }
            autoPopulateFieldsFromANC();
        }
        return root;
    }

    public static LabourDeliveryFragment newInstance() {
        return new LabourDeliveryFragment();
    }

    private void initiateFragmentViews(View root) {
        mSaveContinueButton = root.findViewById(R.id.mSaveContinueButton);
        edancNo = root.findViewById(R.id.edancNo);
        autoBookingStatus = root.findViewById(R.id.autoBookingStatus);
        edDateOfDelivery = root.findViewById(R.id.edDateOfDelivery);
        edGaWeeks = root.findViewById(R.id.edGaWeeks);
        autoRomDeliveryInterval = root.findViewById(R.id.autoRomDeliveryInterval);
        autoModeDelivery = root.findViewById(R.id.autoModeDelivery);
        autoEpisiotomy = root.findViewById(R.id.autoEpisiotomy);
        autoVaginalTear = root.findViewById(R.id.autoVaginalTear);
        autoFeedingDecision = root.findViewById(R.id.autoFeedingDecision);
        autoChildGivenArvWithin72 = root.findViewById(R.id.autoChildGivenArvWithin72);
        autoOnArt = root.findViewById(R.id.autoOnArt);
        autoHivExposedInfantGivenHbWithin24hrs = root.findViewById(R.id.autoHivExposedInfantGivenHbWithin24hrs);
        autoDeliveryTime = root.findViewById(R.id.autoDeliveryTime);
        autoArtStartedLdWard = root.findViewById(R.id.autoArtStartedLdWard);
        autoHbstatus = root.findViewById(R.id.autoHbstatus);
        autoHcstatus = root.findViewById(R.id.autoHcstatus);
        autoMaternalOutcome = root.findViewById(R.id.autoMaternalOutcome);
        autoChildStatus = root.findViewById(R.id.autoChildStatus);
        edNumberOfInfantsAlive = root.findViewById(R.id.edNumberOfInfantsAlive);
        edNumberOfInfantsDead = root.findViewById(R.id.edNumberOfInfantsDead);

        autoBookingStatusTIL = root.findViewById(R.id.autoBookingStatusTIL);
        dateEnrollmentTIL = root.findViewById(R.id.dateEnrollmentTIL);
        autoRomDeliveryTIL = root.findViewById(R.id.autoRomDeliveryTIL);
        autoModeDeliveryTIL = root.findViewById(R.id.autoModeDeliveryTIL);
        autoEpisiotomyTIL = root.findViewById(R.id.autoEpisiotomyTIL);
        autoVaginalTearTIL = root.findViewById(R.id.autoVaginalTearTIL);
        autoFeedingDecisionTIL = root.findViewById(R.id.autoFeedingDecisionTIL);
        autoChildGivenArvWithin72TIL = root.findViewById(R.id.autoChildGivenArvWithin72TIL);
        autoOnArtTIL = root.findViewById(R.id.autoOnArtTIL);
        autoHivExposedInfantGivenHbWithin24hrsTIL = root.findViewById(R.id.autoHivExposedInfantGivenHbWithin24hrsTIL);
        autoDeliveryTimeTIL = root.findViewById(R.id.autoDeliveryTimeTIL);
        autoArtStartedLdWardTIL = root.findViewById(R.id.autoArtStartedLdWardTIL);
        autoHbstatusTIL = root.findViewById(R.id.autoHbstatusTIL);
        autoHcstatusTIL = root.findViewById(R.id.autoHcstatusTIL);
        autoMaternalOutcomeTIL = root.findViewById(R.id.autoMaternalOutcomeTIL);
        autoChildStatusTIL = root.findViewById(R.id.autoChildStatusTIL);
        edNumberOfInfantsAliveTIL = root.findViewById(R.id.edNumberOfInfantsAliveTIL);
        edNumberOfInfantsDeadTIL = root.findViewById(R.id.edNumberOfInfantsDeadTIL);

        childStatusDependentView = root.findViewById(R.id.childStatusDependentView);
    }

    public void initDropDownsPatients() {
        String[] romDeliveryInterval = getResources().getStringArray(R.array.rom_delivery_interval);
        ArrayAdapter<String> adapterRomDeliveryInterval = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, romDeliveryInterval);
        autoRomDeliveryInterval.setAdapter(adapterRomDeliveryInterval);

        String[] bookingStatus = getResources().getStringArray(R.array.booking_status);
        ArrayAdapter<String> adapterBookingStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, bookingStatus);
        autoBookingStatus.setAdapter(adapterBookingStatus);

        String[] modeOfDelivery = getResources().getStringArray(R.array.mode_of_delivery);
        ArrayAdapter<String> adapterModeOfDelivery = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, modeOfDelivery);
        autoModeDelivery.setAdapter(adapterModeOfDelivery);

        String[] feedingDecision = getResources().getStringArray(R.array.feeding_decision);
        ArrayAdapter<String> adapterFeedingDecision = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, feedingDecision);
        autoFeedingDecision.setAdapter(adapterFeedingDecision);

        String[] timeOfDiagnosis = getResources().getStringArray(R.array.time_of_diagnosis);
        ArrayAdapter<String> adapterTimeOfDiagnosis = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, timeOfDiagnosis);
        autoDeliveryTime.setAdapter(adapterTimeOfDiagnosis);

        String[] maternalOutcome = getResources().getStringArray(R.array.maternal_outcome);
        ArrayAdapter<String> adapterMaternalOutcome = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, maternalOutcome);
        autoMaternalOutcome.setAdapter(adapterMaternalOutcome);

        String[] childStatus = getResources().getStringArray(R.array.child_status);
        ArrayAdapter<String> adapterChildStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, childStatus);
        autoChildStatus.setAdapter(adapterChildStatus);

        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoEpisiotomy.setAdapter(adapterBooleanAnswers);
        autoVaginalTear.setAdapter(adapterBooleanAnswers);
        autoChildGivenArvWithin72.setAdapter(adapterBooleanAnswers);
        autoOnArt.setAdapter(adapterBooleanAnswers);
        autoHivExposedInfantGivenHbWithin24hrs.setAdapter(adapterBooleanAnswers);
        autoArtStartedLdWard.setAdapter(adapterBooleanAnswers);

        String[] positiveNegative = getResources().getStringArray(R.array.positive_negative);
        ArrayAdapter<String> adapterPositiveNegative = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, positiveNegative);
        autoHbstatus.setAdapter(adapterPositiveNegative);
        autoHcstatus.setAdapter(adapterPositiveNegative);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        autoChildStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoChildStatus.getText().toString().equals("Alive")) {
                    childStatusDependentView.setVisibility(View.VISIBLE);
                } else {
                    childStatusDependentView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showDatePickers() {
        edDateOfDelivery.setOnClickListener(v -> {
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
                edDateOfDelivery.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
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
            edGaWeeks.setText(anc.getGaweeks());
        }
    }


    public void fillFields(LabourDelivery labourDelivery) {
        if (labourDelivery != null) {
            isUpdateLabourDelivery = true;
            updatedLabourDelivery = labourDelivery;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM, mPresenter.getPatientId());

            if(CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getBookingStatus()) != null){
                autoBookingStatus.setText(CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getBookingStatus()), false);
            }

            edDateOfDelivery.setText(labourDelivery.getDateOfDelivery());

            if(labourDelivery.getRomDeliveryInterval() != null){
                autoRomDeliveryInterval.setText(labourDelivery.getRomDeliveryInterval(), false);
            }

            if(labourDelivery.getModeOfDelivery() != null){
                autoModeDelivery.setText(CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getModeOfDelivery()), false);
            }

            if(labourDelivery.getEpisiotomy() != null){
                autoEpisiotomy.setText(labourDelivery.getEpisiotomy(), false);
            }

            if(labourDelivery.getVaginalTear() != null){
                autoVaginalTear.setText(labourDelivery.getVaginalTear(), false);
            }

            if(labourDelivery.getFeedingDecision() != null){
                autoFeedingDecision.setText(CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getFeedingDecision()), false);
            }

            if(labourDelivery.getChildGivenArvWithin72() != null){
                autoChildGivenArvWithin72.setText(labourDelivery.getChildGivenArvWithin72(), false);
            }

            if(labourDelivery.getOnArt() != null){
                autoOnArt.setText(labourDelivery.getOnArt(), false);
            }

            if(labourDelivery.getHivExposedInfantGivenHbWithin24hrs() != null){
                autoHivExposedInfantGivenHbWithin24hrs.setText(labourDelivery.getHivExposedInfantGivenHbWithin24hrs(), false);
            }

            if(labourDelivery.getDeliveryTime() != null){
                autoDeliveryTime.setText(CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getDeliveryTime()), false);
            }

            if(labourDelivery.getArtStartedLdWard() != null){
                autoArtStartedLdWard.setText(labourDelivery.getArtStartedLdWard(), false);
            }

            if(labourDelivery.getHbstatus() != null){
                autoHbstatus.setText(labourDelivery.getHbstatus(), false);
            }

            if(labourDelivery.getHcstatus() != null){
                autoHcstatus.setText(labourDelivery.getHcstatus(), false);
            }

            if(labourDelivery.getMaternalOutcome() != null){
                autoMaternalOutcome.setText(CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getMaternalOutcome()), false);
            }

            if(labourDelivery.getChildStatus() != null){
                autoChildStatus.setText(CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getChildStatus()), false);
            }

            if(labourDelivery.getNumberOfInfantsAlive() != null){
                edNumberOfInfantsAlive.setText(labourDelivery.getNumberOfInfantsAlive()+"");
            }

            if(labourDelivery.getNumberOfInfantsDead() != null){
                edNumberOfInfantsDead.setText(labourDelivery.getNumberOfInfantsDead()+"");
            }

            if (labourDelivery.getChildStatus() != null && CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getChildStatus()).equals("Alive")) {
                childStatusDependentView.setVisibility(View.VISIBLE);
            }else{
                childStatusDependentView.setVisibility(View.GONE);
            }
        }
    }

    private LabourDelivery createEncounter() {
        LabourDelivery labourDelivery = new LabourDelivery();
        updateEncounterWithData(labourDelivery);
        return labourDelivery;
    }

    private LabourDelivery updateEncounter(LabourDelivery labourDelivery) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(labourDelivery);
        return labourDelivery;
    }

    private LabourDelivery updateEncounterWithData(LabourDelivery labourDelivery) {
        if (!ViewUtils.isEmpty(edancNo)) {
            labourDelivery.setAncNo(ViewUtils.getInput(edancNo));
        }

        if (!ViewUtils.isEmpty(autoBookingStatus)) {
            labourDelivery.setBookingStatus(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoBookingStatus)));
        }

        if (!ViewUtils.isEmpty(edDateOfDelivery)) {
            labourDelivery.setDateOfDelivery(ViewUtils.getInput(edDateOfDelivery));
        }

        if (!ViewUtils.isEmpty(edGaWeeks)) {
            labourDelivery.setGaweeks(ViewUtils.getInput(edGaWeeks));
        }

        if (!ViewUtils.isEmpty(autoRomDeliveryInterval)) {
            labourDelivery.setRomDeliveryInterval(ViewUtils.getInput(autoRomDeliveryInterval));
        }

        if (!ViewUtils.isEmpty(autoModeDelivery)) {
            labourDelivery.setModeOfDelivery(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoModeDelivery)));
        }

        if (!ViewUtils.isEmpty(autoEpisiotomy)) {
            labourDelivery.setEpisiotomy(ViewUtils.getInput(autoEpisiotomy));
        }

        if (!ViewUtils.isEmpty(autoVaginalTear)) {
            labourDelivery.setVaginalTear(ViewUtils.getInput(autoVaginalTear));
        }

        if (!ViewUtils.isEmpty(autoFeedingDecision)) {
            labourDelivery.setFeedingDecision(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoFeedingDecision)));
        }

        if (!ViewUtils.isEmpty(autoChildGivenArvWithin72)) {
            labourDelivery.setChildGivenArvWithin72(ViewUtils.getInput(autoChildGivenArvWithin72));
        }

        if (!ViewUtils.isEmpty(autoOnArt)) {
            labourDelivery.setOnArt(ViewUtils.getInput(autoOnArt));
        }

        if (!ViewUtils.isEmpty(autoHivExposedInfantGivenHbWithin24hrs)) {
            labourDelivery.setHivExposedInfantGivenHbWithin24hrs(ViewUtils.getInput(autoHivExposedInfantGivenHbWithin24hrs));
        }

        if (!ViewUtils.isEmpty(autoDeliveryTime)) {
            labourDelivery.setDeliveryTime(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoDeliveryTime)));
        }

        if (!ViewUtils.isEmpty(autoArtStartedLdWard)) {
            labourDelivery.setArtStartedLdWard(ViewUtils.getInput(autoArtStartedLdWard));
        }

        if (!ViewUtils.isEmpty(autoHbstatus)) {
            labourDelivery.setHbstatus(ViewUtils.getInput(autoHbstatus));
        }

        if (!ViewUtils.isEmpty(autoHcstatus)) {
            labourDelivery.setHcstatus(ViewUtils.getInput(autoHcstatus));
        }

        if (!ViewUtils.isEmpty(autoMaternalOutcome)) {
            labourDelivery.setMaternalOutcome(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoMaternalOutcome)));
        }

        if (!ViewUtils.isEmpty(autoChildStatus)) {
            labourDelivery.setChildStatus(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoChildStatus)));
        }

        if (!ViewUtils.isEmpty(edNumberOfInfantsAlive)) {
            labourDelivery.setNumberOfInfantsAlive(Integer.parseInt(Objects.requireNonNull(ViewUtils.getInput(edNumberOfInfantsAlive))));
        }

        if (!ViewUtils.isEmpty(edNumberOfInfantsDead)) {
            labourDelivery.setNumberOfInfantsDead(Integer.parseInt(Objects.requireNonNull(ViewUtils.getInput(edNumberOfInfantsDead))));
        }

        return labourDelivery;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateLabourDelivery) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM, mPresenter.getPatientId());
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
            case R.id.mSaveContinueButton:
                if (isUpdateLabourDelivery) {
                    mPresenter.confirmUpdate(updateEncounter(updatedLabourDelivery), updatedForm);
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
    public void setErrorsVisibility(boolean bookingStatus, boolean dateOfDelivery, boolean romDelivery, boolean modeOfDelivery, boolean episiotomy, boolean vaginalTear, boolean feedingDecision, boolean childGivenARVwithin72hrs, boolean onArt, boolean hivExposedInfant24hrs, boolean timeDelivery, boolean artStartedLDWard, boolean hbStatus, boolean hcStatus, boolean maternalOutcome, boolean childStatus, boolean noChildAlive, boolean noChildDead) {
        if (bookingStatus) {
            autoBookingStatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoBookingStatusTIL.setError("This field is required");
        }

        if (dateOfDelivery) {
            dateEnrollmentTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            dateEnrollmentTIL.setError("This field is required");
        }

        if (romDelivery) {
            autoRomDeliveryTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoRomDeliveryTIL.setError("This field is required");
        }

        if (modeOfDelivery) {
            autoModeDeliveryTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoModeDeliveryTIL.setError("This field is required");
        }

        if (episiotomy) {
            autoEpisiotomyTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoEpisiotomyTIL.setError("This field is required");
        }

        if (vaginalTear) {
            autoVaginalTearTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoVaginalTearTIL.setError("This field is required");
        }

        if (feedingDecision) {
            autoFeedingDecisionTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoFeedingDecisionTIL.setError("This field is required");
        }

        if (childGivenARVwithin72hrs) {
            autoChildGivenArvWithin72TIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoChildGivenArvWithin72TIL.setError("This field is required");
        }

        if (onArt) {
            autoOnArtTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoOnArtTIL.setError("This field is required");
        }

        if (hivExposedInfant24hrs) {
            autoHivExposedInfantGivenHbWithin24hrsTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoHivExposedInfantGivenHbWithin24hrsTIL.setError("This field is required");
        }

        if (timeDelivery) {
            autoDeliveryTimeTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoDeliveryTimeTIL.setError("This field is required");
        }

        if (artStartedLDWard) {
            autoArtStartedLdWardTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoArtStartedLdWardTIL.setError("This field is required");
        }

        if (hbStatus) {
            autoHbstatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoHbstatusTIL.setError("This field is required");
        }

        if (hcStatus) {
            autoHcstatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoHcstatusTIL.setError("This field is required");
        }

        if (maternalOutcome) {
            autoMaternalOutcomeTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoMaternalOutcomeTIL.setError("This field is required");
        }

        if (childStatus) {
            autoChildStatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoChildStatusTIL.setError("This field is required");
        }

        if (noChildAlive) {
            edNumberOfInfantsAliveTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edNumberOfInfantsAliveTIL.setError("This field is required");
        }

        if (noChildDead) {
            edNumberOfInfantsDeadTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edNumberOfInfantsDeadTIL.setError("This field is required");
        }
    }


}
