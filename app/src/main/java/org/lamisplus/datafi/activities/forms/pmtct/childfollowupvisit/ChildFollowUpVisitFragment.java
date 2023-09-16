package org.lamisplus.datafi.activities.forms.pmtct.childfollowupvisit;

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
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.activities.pmtct.pmtctservices.PMTCTServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.RegimenDAO;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.ChildFollowupVisit;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.InfantRegistration;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.PartnerRegistration;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ChildFollowUpVisitFragment extends LamisBaseFragment<ChildFollowUpVisitContract.Presenter> implements ChildFollowUpVisitContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateChildFollowUpVisit = false;
    private Encounter updatedForm;
    private ChildFollowupVisit updatedChildFollowupVisit;
    private EditText edDateVisit;
    private AutoCompleteTextView autoInfantHospitalNumber;
    private EditText edMotherANCNumber;
    private EditText edBodyWeight;
    private AutoCompleteTextView autoBreastFeeding;
    private AutoCompleteTextView autoCTX;
    private AutoCompleteTextView autoVisitStatus;
    private AutoCompleteTextView autoTimingMothersArt;
    private AutoCompleteTextView autoOriginalRegimenLine;
    private AutoCompleteTextView autoOriginalRegimen;
    private AutoCompleteTextView autoAgeCTXInitiation;
    private AutoCompleteTextView autoInfantARVType;
    private AutoCompleteTextView autoTimingARVProphylaxis;
    private AutoCompleteTextView autoPlaceOfDelivery;
    private EditText edAgeAtTestMonths;
    private AutoCompleteTextView autoSampleType;
    private EditText edDateSampleCollected;
    private EditText edDateResultReceived;
    private EditText edDateResultReceivedByCaregiver;
    private EditText edDateSampleSent;
    private AutoCompleteTextView autoResult;
    private TextInputLayout edDateVisitTIL;
    private TextInputLayout autoInfantHospitalNumberTIL;
    private TextInputLayout edBodyWeightTIL;

    private String packageName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_child_followup_visit, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            initDropDownsPatients();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.CHILD_FOLLOW_UP_VISIT_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.CHILD_FOLLOW_UP_VISIT_FORM, mPresenter.getPatientId()));
            }
            autoPopulateFieldsFromANC();
        }
        return root;
    }

    private void autoPopulateFieldsFromANC() {
        ANC anc = EncounterDAO.findAncFromForm(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
        if (anc != null) {
            edMotherANCNumber.setText(anc.getAncNo());
        }

        ArrayList<String> infantHospitalNumbers = new ArrayList<String>();
        List<Encounter> encounters = EncounterDAO.getAllForms(ApplicationConstants.Forms.INFANT_INFORMATION_FORM);
        assert encounters != null;
        for (Encounter e : encounters) {
            InfantRegistration infantRegistration = new Gson().fromJson(e.getDataValues(), InfantRegistration.class);
            assert anc != null;
            if (infantRegistration.getAncNo().equals(anc.getAncNo())) {
                infantHospitalNumbers.add(infantRegistration.getFirstName() + "-" + infantRegistration.getHospitalNumber());
            }
        }

        ArrayAdapter<String> infants = new ArrayAdapter<>(requireActivity(), R.layout.form_dropdown, infantHospitalNumbers);
        autoInfantHospitalNumber.setAdapter(infants);
    }

    public static ChildFollowUpVisitFragment newInstance() {
        return new ChildFollowUpVisitFragment();
    }

    private void initiateFragmentViews(View root) {
        mSaveContinueButton = root.findViewById(R.id.mSaveContinueButton);

        edDateVisit = root.findViewById(R.id.edDateVisit);
        autoInfantHospitalNumber = root.findViewById(R.id.autoInfantHospitalNumber);
        edMotherANCNumber = root.findViewById(R.id.edMotherANCNumber);
        edBodyWeight = root.findViewById(R.id.edBodyWeight);
        autoBreastFeeding = root.findViewById(R.id.autoBreastFeeding);
        autoCTX = root.findViewById(R.id.autoCTX);
        autoVisitStatus = root.findViewById(R.id.autoVisitStatus);
        autoTimingMothersArt = root.findViewById(R.id.autoTimingMothersArt);
        autoOriginalRegimenLine = root.findViewById(R.id.autoOriginalRegimenLine);
        autoOriginalRegimen = root.findViewById(R.id.autoOriginalRegimen);
        autoAgeCTXInitiation = root.findViewById(R.id.autoAgeCTXInitiation);
        autoInfantARVType = root.findViewById(R.id.autoInfantARVType);
        autoTimingARVProphylaxis = root.findViewById(R.id.autoTimingARVProphylaxis);
        autoPlaceOfDelivery = root.findViewById(R.id.autoPlaceOfDelivery);
        edAgeAtTestMonths = root.findViewById(R.id.edAgeAtTestMonths);
        autoSampleType = root.findViewById(R.id.autoSampleType);
        edDateSampleCollected = root.findViewById(R.id.edDateSampleCollected);
        edDateResultReceived = root.findViewById(R.id.edDateResultReceived);
        edDateResultReceivedByCaregiver = root.findViewById(R.id.edDateResultReceivedByCaregiver);
        edDateSampleSent = root.findViewById(R.id.edDateSampleSent);
        autoResult = root.findViewById(R.id.autoResult);

        edDateVisitTIL = root.findViewById(R.id.edDateVisitTIL);
        autoInfantHospitalNumberTIL = root.findViewById(R.id.autoInfantHospitalNumberTIL);
        edBodyWeightTIL = root.findViewById(R.id.edBodyWeightTIL);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

    }

    private void showDatePickers() {
        edDateVisit.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(requireActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edDateVisit.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateSampleCollected.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(requireActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edDateSampleCollected.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateResultReceived.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(requireActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edDateResultReceived.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateSampleSent.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(requireActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edDateSampleSent.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edDateResultReceivedByCaregiver.setOnClickListener(v -> {
            int cYear;
            int cMonth;
            int cDay;

            Calendar currentDate = Calendar.getInstance();
            cYear = currentDate.get(Calendar.YEAR);
            cMonth = currentDate.get(Calendar.MONTH);
            cDay = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(requireActivity(), (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                int adjustedMonth = selectedMonth + 1;
                String stringMonth = String.format("%02d", adjustedMonth);
                String stringDay = String.format("%02d", selectedDay);
                edDateResultReceivedByCaregiver.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });
    }

    private void initDropDownsPatients() {
        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoBreastFeeding.setAdapter(adapterBooleanAnswers);
        autoCTX.setAdapter(adapterBooleanAnswers);

        String[] visitStatus = getResources().getStringArray(R.array.infant_visit_status);
        ArrayAdapter<String> adapterVisitStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, visitStatus);
        autoVisitStatus.setAdapter(adapterVisitStatus);

        String[] artInitiation = getResources().getStringArray(R.array.timing_mothers_art_initiation);
        ArrayAdapter<String> adapterArtInitiation = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, artInitiation);
        autoTimingMothersArt.setAdapter(adapterArtInitiation);

        String[] regimenLine = getResources().getStringArray(R.array.original_regimen_line);
        ArrayAdapter<String> adapterRegimenLine = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, regimenLine);
        autoOriginalRegimenLine.setAdapter(adapterRegimenLine);

        autoOriginalRegimenLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] originalRegimen = {};
                if (autoOriginalRegimenLine.getText().toString().equals("Adult 1st Line")) {
                    originalRegimen = getResources().getStringArray(R.array.first_line_original_regimen);
                } else if (autoOriginalRegimenLine.getText().toString().equals("Adult 2nd Line")) {
                    originalRegimen = getResources().getStringArray(R.array.second_line_original_regimen);
                } else if (autoOriginalRegimenLine.getText().toString().equals("Adult 3rd Line")) {
                    originalRegimen = getResources().getStringArray(R.array.third_line_original_regimen);
                } else {
                    originalRegimen = null;
                }
                ArrayAdapter<String> adapterOriginalRegimen = new ArrayAdapter<>(requireActivity(), R.layout.form_dropdown, originalRegimen);
                autoOriginalRegimen.setAdapter(adapterOriginalRegimen);
                autoOriginalRegimen.setText(null);
            }
        });

        String[] ageCtx = getResources().getStringArray(R.array.age_ctx_initiation);
        ArrayAdapter<String> adapterAgeCtx = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, ageCtx);
        autoAgeCTXInitiation.setAdapter(adapterAgeCtx);

        String[] infantArvType = getResources().getStringArray(R.array.infant_arv_type);
        ArrayAdapter<String> adapterInfantArvType = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, infantArvType);
        autoInfantARVType.setAdapter(adapterInfantArvType);

        String[] arvProphylasis = getResources().getStringArray(R.array.timing_arv_prophylaxis);
        ArrayAdapter<String> adapterArvProphylasis = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, arvProphylasis);
        autoTimingARVProphylaxis.setAdapter(adapterArvProphylasis);

        String[] sampleType = getResources().getStringArray(R.array.infant_sample_type);
        ArrayAdapter<String> adapterSampleType = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, sampleType);
        autoSampleType.setAdapter(adapterSampleType);

        String[] result = getResources().getStringArray(R.array.infant_pcr_result);
        ArrayAdapter<String> adapterResult = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, result);
        autoResult.setAdapter(adapterResult);

        String[] placeDelivery = getResources().getStringArray(R.array.place_of_delivery);
        ArrayAdapter<String> adapterPlaceDelivery = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, placeDelivery);
        autoPlaceOfDelivery.setAdapter(adapterPlaceDelivery);
    }


    public void fillFields(ChildFollowupVisit childFollowupVisit) {
        if (childFollowupVisit != null) {
            isUpdateChildFollowUpVisit = true;
            updatedChildFollowupVisit = childFollowupVisit;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.CHILD_FOLLOW_UP_VISIT_FORM, mPresenter.getPatientId());

            //InfantVisitRequestDto
            ChildFollowupVisit.InfantVisitRequestDto infantVisitRequestDto = new ChildFollowupVisit.InfantVisitRequestDto();
            if (childFollowupVisit.getInfantVisitRequestDto().getVisitDate() != null) {
                edDateVisit.setText(childFollowupVisit.getInfantVisitRequestDto().getVisitDate());
            }


            if (childFollowupVisit.getInfantVisitRequestDto().getInfantHospitalNumber() != null) {
                autoInfantHospitalNumber.setText("-" + childFollowupVisit.getInfantVisitRequestDto().getInfantHospitalNumber(), false);
            }

            if (childFollowupVisit.getInfantVisitRequestDto().getVisitStatus() != null) {
                autoVisitStatus.setText(CodesetsDAO.findCodesetsDisplayByCode(childFollowupVisit.getInfantVisitRequestDto().getVisitStatus()));
            }

            if (childFollowupVisit.getInfantVisitRequestDto().getBodyWeight() != null) {
                edBodyWeight.setText(childFollowupVisit.getInfantVisitRequestDto().getBodyWeight());
            }

            if (childFollowupVisit.getInfantVisitRequestDto().getBreastFeeding() != null) {
                autoBreastFeeding.setText(childFollowupVisit.getInfantVisitRequestDto().getBreastFeeding(), false);
            }

            if (childFollowupVisit.getInfantVisitRequestDto().getCtxStatus() != null) {
                autoCTX.setText(childFollowupVisit.getInfantVisitRequestDto().getCtxStatus(), false);
            }

            childFollowupVisit.getInfantVisitRequestDto().setInfantOutcomeAt18Months("");
            childFollowupVisit.getInfantVisitRequestDto().setUuid("");
            childFollowupVisit.getInfantVisitRequestDto().setAgeAtCtx("");

            //InfantArvDto
            if (childFollowupVisit.getInfantArvDto().getAgeAtCtx() != null) {
                autoAgeCTXInitiation.setText(CodesetsDAO.findCodesetsDisplayByCode(childFollowupVisit.getInfantArvDto().getAgeAtCtx()), false);
            }


            if (childFollowupVisit.getInfantArvDto().getArvDeliveryPoint() != null) {
                autoTimingARVProphylaxis.setText(childFollowupVisit.getInfantArvDto().getArvDeliveryPoint(), false);
            }

            if (childFollowupVisit.getInfantArvDto().getInfantArvTime() != null) {
                autoPlaceOfDelivery.setText(childFollowupVisit.getInfantArvDto().getInfantArvTime(), false);
            }

            if (childFollowupVisit.getInfantArvDto().getInfantArvType() != null) {
                autoInfantARVType.setText(childFollowupVisit.getInfantArvDto().getInfantArvType(), false);
            }

            //InfantMotherArtDto
            if (childFollowupVisit.getInfantMotherArtDto().getMotherArtInitiationTime() != null) {
                autoTimingMothersArt.setText(CodesetsDAO.findCodesetsDisplayByCode(childFollowupVisit.getInfantMotherArtDto().getMotherArtInitiationTime()), false);
            }

            childFollowupVisit.getInfantMotherArtDto().setMotherArtRegimen("");

            if (childFollowupVisit.getInfantMotherArtDto().getRegimenTypeId() != null) {
                autoOriginalRegimenLine.setText(RegimenDAO.findRegimenDescriptionByRegimenTypeId(childFollowupVisit.getInfantMotherArtDto().getRegimenTypeId()), false);
            }

            if (childFollowupVisit.getInfantMotherArtDto().getRegimenId() != null) {
                autoOriginalRegimen.setText(RegimenDAO.findRegimenDescriptionById(childFollowupVisit.getInfantMotherArtDto().getRegimenId()), false);
            }

            //infantPCRTestDto
            if (childFollowupVisit.getInfantPCRTestDto().getAgeAtTest() != null) {
                edAgeAtTestMonths.setText(childFollowupVisit.getInfantPCRTestDto().getAgeAtTest()+"");
            }


            if (childFollowupVisit.getInfantPCRTestDto().getDateResultReceivedAtFacility() != null) {
                edDateResultReceived.setText(childFollowupVisit.getInfantPCRTestDto().getDateResultReceivedAtFacility());
            }

            if (childFollowupVisit.getInfantPCRTestDto().getDateResultReceivedByCaregiver() != null) {
                edDateResultReceivedByCaregiver.setText(childFollowupVisit.getInfantPCRTestDto().getDateResultReceivedByCaregiver());
            }

            if (childFollowupVisit.getInfantPCRTestDto().getDateSampleCollected() != null) {
                edDateSampleCollected.setText(childFollowupVisit.getInfantPCRTestDto().getDateSampleCollected());
            }

            if (childFollowupVisit.getInfantPCRTestDto().getDateSampleSent() != null) {
                edDateSampleSent.setText(childFollowupVisit.getInfantPCRTestDto().getDateSampleSent());
            }

            if (childFollowupVisit.getInfantPCRTestDto().getResults() != null) {
                autoResult.setText(childFollowupVisit.getInfantPCRTestDto().getResults(), false);
            }

            if (childFollowupVisit.getInfantPCRTestDto().getTestType() != null) {
                autoSampleType.setText(childFollowupVisit.getInfantPCRTestDto().getTestType(), false);
            }

        }
    }

    private ChildFollowupVisit createEncounter() {
        ChildFollowupVisit childFollowupVisit = new ChildFollowupVisit();
        updateEncounterWithData(childFollowupVisit);
        return childFollowupVisit;
    }

    private ChildFollowupVisit updateEncounter(ChildFollowupVisit childFollowupVisit) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(childFollowupVisit);
        return childFollowupVisit;
    }

    private ChildFollowupVisit updateEncounterWithData(ChildFollowupVisit childFollowupVisit) {
        //InfantVisitRequestDto
        ChildFollowupVisit.InfantVisitRequestDto infantVisitRequestDto = new ChildFollowupVisit.InfantVisitRequestDto();
        if (!ViewUtils.isEmpty(edDateVisit)) {
            infantVisitRequestDto.setVisitDate(ViewUtils.getInput(edDateVisit));
        }

        if (!ViewUtils.isEmpty(edMotherANCNumber)) {
//            Log.v("Baron", "ANC is " + ViewUtils.getInput(edMotherANCNumber));
            infantVisitRequestDto.setAncNumber(ViewUtils.getInput(edMotherANCNumber));
        }

        if (!ViewUtils.isEmpty(autoInfantHospitalNumber)) {
            String[] splitNameHosp = Objects.requireNonNull(ViewUtils.getInput(autoInfantHospitalNumber)).split("-");
            infantVisitRequestDto.setInfantHospitalNumber(splitNameHosp[1]);
        }

        if (!ViewUtils.isEmpty(autoVisitStatus)) {
            infantVisitRequestDto.setVisitStatus(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoVisitStatus)));
        }

        if (!ViewUtils.isEmpty(edBodyWeight)) {
            infantVisitRequestDto.setBodyWeight(ViewUtils.getInput(edBodyWeight));
        }

        if (!ViewUtils.isEmpty(autoBreastFeeding)) {
            infantVisitRequestDto.setBreastFeeding(ViewUtils.getInput(autoBreastFeeding));
        }

        if (!ViewUtils.isEmpty(autoCTX)) {
            infantVisitRequestDto.setCtxStatus(ViewUtils.getInput(autoCTX));
        }

        infantVisitRequestDto.setInfantOutcomeAt18Months("");
        infantVisitRequestDto.setUuid("");
        infantVisitRequestDto.setAgeAtCtx("");

        childFollowupVisit.setInfantVisitRequestDto(infantVisitRequestDto);

        //InfantArvDto
        ChildFollowupVisit.InfantArvDto infantArvDto = new ChildFollowupVisit.InfantArvDto();
        if (!ViewUtils.isEmpty(autoAgeCTXInitiation)) {
            infantArvDto.setAgeAtCtx(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoAgeCTXInitiation)));
        }

        if (!ViewUtils.isEmpty(edDateVisit)) {
            infantArvDto.setVisitDate(ViewUtils.getInput(edDateVisit));
        }

        if (!ViewUtils.isEmpty(autoInfantHospitalNumber)) {
            String[] splitNameHosp = Objects.requireNonNull(ViewUtils.getInput(autoInfantHospitalNumber)).split("-");
            infantArvDto.setInfantHospitalNumber(splitNameHosp[1]);
        }

        if (!ViewUtils.isEmpty(edMotherANCNumber)) {
            infantArvDto.setAncNumber(ViewUtils.getInput(edMotherANCNumber));
        }

        if (!ViewUtils.isEmpty(autoTimingARVProphylaxis)) {
            infantArvDto.setArvDeliveryPoint(ViewUtils.getInput(autoTimingARVProphylaxis));
        }

        if (!ViewUtils.isEmpty(autoPlaceOfDelivery)) {
            infantArvDto.setInfantArvTime(ViewUtils.getInput(autoPlaceOfDelivery));
        }

        if (!ViewUtils.isEmpty(autoInfantARVType)) {
            infantArvDto.setInfantArvType(ViewUtils.getInput(autoInfantARVType));
        }

        childFollowupVisit.setInfantArvDto(infantArvDto);

        //InfantMotherArtDto
        ChildFollowupVisit.InfantMotherArtDto infantMotherArtDto = new ChildFollowupVisit.InfantMotherArtDto();

        infantMotherArtDto.setAncNumber(ViewUtils.getInput(edMotherANCNumber));

        if (!ViewUtils.isEmpty(autoTimingMothersArt)) {
            infantMotherArtDto.setMotherArtInitiationTime(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoTimingMothersArt)));
        }

        infantMotherArtDto.setMotherArtRegimen("");

        if (!ViewUtils.isEmpty(autoOriginalRegimenLine)) {
            infantMotherArtDto.setRegimenTypeId(RegimenDAO.findRegimenTypeIdByDescription(ViewUtils.getInput(autoOriginalRegimenLine)));
        }

        if (!ViewUtils.isEmpty(autoOriginalRegimen)) {
            infantMotherArtDto.setRegimenId(RegimenDAO.findRegimenIdByDescription(ViewUtils.getInput(autoOriginalRegimen)));
        }

        childFollowupVisit.setInfantMotherArtDto(infantMotherArtDto);

        //infantPCRTestDto
        ChildFollowupVisit.InfantPCRTestDto infantPCRTestDto = new ChildFollowupVisit.InfantPCRTestDto();
        if (!ViewUtils.isEmpty(edAgeAtTestMonths)) {
            infantPCRTestDto.setAgeAtTest(Integer.parseInt(Objects.requireNonNull(ViewUtils.getInput(edAgeAtTestMonths))));
        }

        if (!ViewUtils.isEmpty(edDateVisit)) {
            infantPCRTestDto.setVisitDate(ViewUtils.getInput(edDateVisit));
        }

        if (!ViewUtils.isEmpty(autoInfantHospitalNumber)) {
            String[] splitNameHosp = Objects.requireNonNull(ViewUtils.getInput(autoInfantHospitalNumber)).split("-");
            infantPCRTestDto.setInfantHospitalNumber(splitNameHosp[1]);
        }

        infantPCRTestDto.setAncNumber(ViewUtils.getInput(edMotherANCNumber));

        if (!ViewUtils.isEmpty(edDateResultReceived)) {
            infantPCRTestDto.setDateResultReceivedAtFacility(ViewUtils.getInput(edDateResultReceived));
        }

        if (!ViewUtils.isEmpty(edDateResultReceivedByCaregiver)) {
            infantPCRTestDto.setDateResultReceivedByCaregiver(ViewUtils.getInput(edDateResultReceivedByCaregiver));
        }

        if (!ViewUtils.isEmpty(edDateSampleCollected)) {
            infantPCRTestDto.setDateSampleCollected(ViewUtils.getInput(edDateSampleCollected));
        }

        if (!ViewUtils.isEmpty(edDateSampleSent)) {
            infantPCRTestDto.setDateSampleSent(ViewUtils.getInput(edDateSampleSent));
        }

        if (!ViewUtils.isEmpty(autoResult)) {
            infantPCRTestDto.setResults(ViewUtils.getInput(autoResult));
        }

        if (!ViewUtils.isEmpty(autoSampleType)) {
            infantPCRTestDto.setTestType(ViewUtils.getInput(autoSampleType));
        }

        childFollowupVisit.setInfantPCRTestDto(infantPCRTestDto);

        return childFollowupVisit;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateChildFollowUpVisit) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.CHILD_FOLLOW_UP_VISIT_FORM, mPresenter.getPatientId());
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
                if (isUpdateChildFollowUpVisit) {
                    mPresenter.confirmUpdate(updateEncounter(updatedChildFollowupVisit), updatedForm);
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
    public void setErrorsVisibility(boolean edDateofVisit, boolean infantHospitalNo, boolean bodyWeight) {
        if (edDateofVisit) {
            edDateVisitTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edDateVisitTIL.setError("This field is required");
        }

        if (infantHospitalNo) {
            autoInfantHospitalNumberTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoInfantHospitalNumberTIL.setError("This field is required");
        }

        if (bodyWeight) {
            edBodyWeightTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edBodyWeightTIL.setError("This field is required");
        }
    }

}
