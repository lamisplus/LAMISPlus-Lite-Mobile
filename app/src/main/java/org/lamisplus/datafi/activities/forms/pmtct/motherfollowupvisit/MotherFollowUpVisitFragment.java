package org.lamisplus.datafi.activities.forms.pmtct.motherfollowupvisit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.metrics.EditingSession;
import android.os.Bundle;
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
import org.lamisplus.datafi.activities.forms.pmtct.anc.ANCFragment;
import org.lamisplus.datafi.activities.hts.htsservices.HTSServicesActivity;
import org.lamisplus.datafi.activities.pmtct.pmtctservices.PMTCTServicesActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.MotherFollowupVisit;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;
import java.util.Objects;

public class MotherFollowUpVisitFragment extends LamisBaseFragment<MotherFollowUpVisitContract.Presenter> implements MotherFollowUpVisitContract.View, View.OnClickListener {

    private Button mSaveContinueButton;

    private boolean isUpdateRst = false;
    private Encounter updatedForm;
    private MotherFollowupVisit updatedMotherFollowupVisit;
    private EditText edDateVisit;
    private EditText edancNo;
    private AutoCompleteTextView autoEntryPoint;
    private AutoCompleteTextView autoFPCounselling;
    private AutoCompleteTextView autoFPMethod;
    private EditText edViralLoadCollectionDate;
    private EditText edGaVlCollection;
    private EditText edResult;
    private AutoCompleteTextView autoDSD;
    private AutoCompleteTextView autoDSDModel;
    private AutoCompleteTextView autoDSDModelType;
    private AutoCompleteTextView autoMaternalOutcome;
    private EditText edDateofOutcome;
    private AutoCompleteTextView autoClientVisitStatus;
    private EditText edNameofARTFacility;
    private LinearLayout autoFPMethodView;
    private LinearLayout edNameofARTFacilityView;
    private LinearLayout autoDsdModelView;
    private LinearLayout autoDSDModelTypeView;
    private TextInputLayout edVisitDateTIL;
    private TextInputLayout autoEntryPointTIL;
    private TextInputLayout autoFPCounsellingTIL;
    private TextInputLayout autoDSDTIL;
    private TextInputLayout autoMaternalOutcomeTIL;
    private TextInputLayout dateOfOutcomeTIL;
    private TextInputLayout autoClientVisitStatusTIL;

    private String packageName;
    private String lmpDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mother_followup_visit, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            initDropDownsPatients();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());

            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.MOTHER_FOLLOW_UP_VISIT_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.MOTHER_FOLLOW_UP_VISIT_FORM, mPresenter.getPatientId()));
            }
            autoPopulateFieldsFromANC();
        }
        return root;
    }

    public static MotherFollowUpVisitFragment newInstance() {
        return new MotherFollowUpVisitFragment();
    }

    private void initiateFragmentViews(View root) {
        edancNo = root.findViewById(R.id.edancNo);
        edDateVisit = root.findViewById(R.id.edDateVisit);
        autoEntryPoint = root.findViewById(R.id.autoEntryPoint);
        autoFPCounselling = root.findViewById(R.id.autoFPCounselling);
        autoFPMethod = root.findViewById(R.id.autoFPMethod);
        edViralLoadCollectionDate = root.findViewById(R.id.edViralLoadCollectionDate);
        edGaVlCollection = root.findViewById(R.id.edGaVlCollection);
        edResult = root.findViewById(R.id.edResult);
        autoDSD = root.findViewById(R.id.autoDSD);
        autoDSDModel = root.findViewById(R.id.autoDSDModel);
        autoDSDModelType = root.findViewById(R.id.autoDSDModelType);
        autoMaternalOutcome = root.findViewById(R.id.autoMaternalOutcome);
        edDateofOutcome = root.findViewById(R.id.edDateofOutcome);
        autoClientVisitStatus = root.findViewById(R.id.autoClientVisitStatus);
        edNameofARTFacility = root.findViewById(R.id.edNameofARTFacility);
        mSaveContinueButton = root.findViewById(R.id.mSaveContinueButton);

        autoFPMethodView = root.findViewById(R.id.autoFPMethodView);
        edNameofARTFacilityView = root.findViewById(R.id.edNameofARTFacilityView);
        autoDsdModelView = root.findViewById(R.id.autoDsdModelView);
        autoDSDModelTypeView = root.findViewById(R.id.autoDSDModelTypeView);
        edVisitDateTIL = root.findViewById(R.id.edVisitDateTIL);
        autoEntryPointTIL = root.findViewById(R.id.autoEntryPointTIL);
        autoFPCounsellingTIL = root.findViewById(R.id.autoFPCounsellingTIL);
        autoDSDTIL = root.findViewById(R.id.autoDSDTIL);
        autoMaternalOutcomeTIL = root.findViewById(R.id.autoMaternalOutcomeTIL);
        dateOfOutcomeTIL = root.findViewById(R.id.dateOfOutcomeTIL);
        autoClientVisitStatusTIL = root.findViewById(R.id.autoClientVisitStatusTIL);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        autoFPCounselling.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoFPCounselling.getText().toString().equals("Yes")) {
                    autoFPMethodView.setVisibility(View.VISIBLE);
                } else {
                    autoFPMethodView.setVisibility(View.GONE);
                }
            }
        });

        autoClientVisitStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoClientVisitStatus.getText().toString().equals("Transfer Out")) {
                    edNameofARTFacilityView.setVisibility(View.VISIBLE);
                } else {
                    edNameofARTFacilityView.setVisibility(View.GONE);
                }
            }
        });

        autoDSD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoDSD.getText().toString().equals("Yes")) {
                    autoDsdModelView.setVisibility(View.VISIBLE);
                    autoDSDModelTypeView.setVisibility(View.VISIBLE);
                } else {
                    autoDsdModelView.setVisibility(View.GONE);
                    autoDSDModelTypeView.setVisibility(View.GONE);
                }
            }
        });

        autoDSDModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] dsdModeltype = {};
                if (autoDSDModel.getText().toString().equals("Facility")) {
                    dsdModeltype = getResources().getStringArray(R.array.dsd_model_type_facility);
                } else if (autoDSDModel.getText().toString().equals("Community")) {
                    dsdModeltype = getResources().getStringArray(R.array.dsd_model_type_community);
                } else {
                    dsdModeltype = null;
                }
                ArrayAdapter<String> adapterDsdModeltype = new ArrayAdapter<>(requireActivity(), R.layout.form_dropdown, dsdModeltype);
                autoDSDModelType.setAdapter(adapterDsdModeltype);
                autoDSDModelType.setText(null);
            }
        });
    }

    private void initDropDownsPatients() {
        String[] entryPoint = getResources().getStringArray(R.array.pmtct_point_of_entry);
        ArrayAdapter<String> adapterEntryPoint = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, entryPoint);
        autoEntryPoint.setAdapter(adapterEntryPoint);

        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);
        autoFPCounselling.setAdapter(adapterBooleanAnswers);
        autoDSD.setAdapter(adapterBooleanAnswers);

        String[] fpMethod = getResources().getStringArray(R.array.fp_method);
        ArrayAdapter<String> adapterFpMethod = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, fpMethod);
        autoFPMethod.setAdapter(adapterFpMethod);

        String[] dsdModelType = getResources().getStringArray(R.array.entry_point);
        ArrayAdapter<String> adapterDsdModelType = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, dsdModelType);
        autoDSDModel.setAdapter(adapterDsdModelType);

        String[] maternalOutcome = getResources().getStringArray(R.array.maternal_outcome);
        ArrayAdapter<String> adapterMaternalOutcome = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, maternalOutcome);
        autoMaternalOutcome.setAdapter(adapterMaternalOutcome);

        String[] clientStatus = getResources().getStringArray(R.array.client_visit_status);
        ArrayAdapter<String> adapterClientStatus = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, clientStatus);
        autoClientVisitStatus.setAdapter(adapterClientStatus);


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

        edDateofOutcome.setOnClickListener(v -> {
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
                edDateofOutcome.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

        edViralLoadCollectionDate.setOnClickListener(v -> {
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
                edViralLoadCollectionDate.setText(selectedYear + "-" + stringMonth + "-" + stringDay);
                Integer ga = DateUtils.gestationAge(lmpDate, ViewUtils.getInput(edViralLoadCollectionDate));
                edGaVlCollection.setText(ga + "");
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });

    }


    public void fillFields(MotherFollowupVisit motherFollowupVisit) {
        if (motherFollowupVisit != null) {
            isUpdateRst = true;
            updatedMotherFollowupVisit = motherFollowupVisit;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.MOTHER_FOLLOW_UP_VISIT_FORM, mPresenter.getPatientId());


            if (motherFollowupVisit.getDateOfVisit() != null) {
                edDateVisit.setText(motherFollowupVisit.getDateOfVisit());
            }

            if (motherFollowupVisit.getEnteryPoint() != null) {
                autoEntryPoint.setText(CodesetsDAO.findCodesetsDisplayByCode(motherFollowupVisit.getEnteryPoint()), false);
            }

            if (motherFollowupVisit.getFpCounseling() != null) {
                autoFPCounselling.setText(motherFollowupVisit.getFpCounseling(), false);
            }

            if (motherFollowupVisit.getFpCounseling().equals("Yes")) {
                autoFPMethodView.setVisibility(View.VISIBLE);
            } else {
                autoFPMethodView.setVisibility(View.GONE);
            }

            if (motherFollowupVisit.getFpMethod() != null) {
                autoFPMethod.setText(CodesetsDAO.findCodesetsDisplayById(Integer.valueOf(motherFollowupVisit.getFpMethod())), false);
            }

            if (motherFollowupVisit.getDateOfViralLoad() != null) {
                edViralLoadCollectionDate.setText(motherFollowupVisit.getDateOfViralLoad());
            }

            if (motherFollowupVisit.getGaOfViralLoad() != null) {
                edGaVlCollection.setText(motherFollowupVisit.getGaOfViralLoad());
            }

            if (motherFollowupVisit.getResultOfViralLoad() != null) {
                edResult.setText(motherFollowupVisit.getResultOfViralLoad());
            }

            if (motherFollowupVisit.getDsd() != null) {
                autoDSD.setText(motherFollowupVisit.getDsd(), false);
            }

            if (motherFollowupVisit.getDsd().equals("Yes")) {
                autoDsdModelView.setVisibility(View.VISIBLE);
                autoDSDModelTypeView.setVisibility(View.VISIBLE);
            } else {
                autoDsdModelView.setVisibility(View.GONE);
                autoDSDModelTypeView.setVisibility(View.GONE);
            }

            if (motherFollowupVisit.getDsdModel() != null) {
                autoDSDModel.setText(motherFollowupVisit.getDsdModel(), false);
            }

            if (motherFollowupVisit.getMaternalOutcome() != null) {
                autoMaternalOutcome.setText(CodesetsDAO.findCodesetsDisplayByCode(motherFollowupVisit.getMaternalOutcome()), false);
            }

            if (motherFollowupVisit.getDateOfmeternalOutcome() != null) {
                edDateofOutcome.setText(motherFollowupVisit.getDateOfmeternalOutcome());
            }

            if (motherFollowupVisit.getVisitStatus() != null) {
                autoClientVisitStatus.setText(CodesetsDAO.findCodesetsDisplayByCode(motherFollowupVisit.getVisitStatus()), false);
            }

            if (motherFollowupVisit.getTransferTo() != null) {
                edNameofARTFacility.setText(motherFollowupVisit.getTransferTo());
            }

            String[] dsdModeltype = {};
            if (motherFollowupVisit.getDsdModel().equals("Facility")) {
                dsdModeltype = getResources().getStringArray(R.array.dsd_model_type_facility);
            } else if (motherFollowupVisit.getDsdModel().equals("Community")) {
                dsdModeltype = getResources().getStringArray(R.array.dsd_model_type_community);
            } else {
                dsdModeltype = null;
            }
            ArrayAdapter<String> adapterDsdModeltype = new ArrayAdapter<>(requireActivity(), R.layout.form_dropdown, dsdModeltype);
            autoDSDModelType.setAdapter(adapterDsdModeltype);
            autoDSDModelType.setText(null);

            if (motherFollowupVisit.getDsdOption() != null) {
                autoDSDModelType.setText(CodesetsDAO.findCodesetsDisplayByCode(motherFollowupVisit.getDsdOption()), false);
            }

        }
    }

    private MotherFollowupVisit createEncounter() {
        MotherFollowupVisit motherFollowupVisit = new MotherFollowupVisit();
        updateEncounterWithData(motherFollowupVisit);
        return motherFollowupVisit;
    }

    private MotherFollowupVisit updateEncounter(MotherFollowupVisit motherFollowupVisit) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(motherFollowupVisit);
        return motherFollowupVisit;
    }

    private void autoPopulateFieldsFromANC() {
        ANC anc = EncounterDAO.findAncFromForm(ApplicationConstants.Forms.ANC_FORM, mPresenter.getPatientId());
        if (anc != null) {
            edancNo.setText(anc.getAncNo());
            lmpDate = anc.getLmp();
        }
    }

    private MotherFollowupVisit updateEncounterWithData(MotherFollowupVisit motherFollowupVisit) {

        if (!ViewUtils.isEmpty(edancNo)) {
            motherFollowupVisit.setAncNo(ViewUtils.getInput(edancNo));
        }

        if (!ViewUtils.isEmpty(edDateVisit)) {
            motherFollowupVisit.setDateOfVisit(ViewUtils.getInput(edDateVisit));
        }

        if (!ViewUtils.isEmpty(autoEntryPoint)) {
            motherFollowupVisit.setEnteryPoint(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoEntryPoint)));
        }

        if (!ViewUtils.isEmpty(autoFPCounselling)) {
            motherFollowupVisit.setFpCounseling(ViewUtils.getInput(autoFPCounselling));
        }

        if (!ViewUtils.isEmpty(autoFPMethod)) {
            motherFollowupVisit.setFpMethod(String.valueOf(CodesetsDAO.findCodesetsIdByDisplay(ViewUtils.getInput(autoFPMethod))));
        }

        if (!ViewUtils.isEmpty(edViralLoadCollectionDate)) {
            motherFollowupVisit.setDateOfViralLoad(ViewUtils.getInput(edViralLoadCollectionDate));
        }

        if (!ViewUtils.isEmpty(edGaVlCollection)) {
            motherFollowupVisit.setGaOfViralLoad(ViewUtils.getInput(edGaVlCollection));
        }

        if (!ViewUtils.isEmpty(edResult)) {
            motherFollowupVisit.setResultOfViralLoad(ViewUtils.getInput(edResult));
        }

        if (!ViewUtils.isEmpty(autoDSD)) {
            motherFollowupVisit.setDsd(ViewUtils.getInput(autoDSD));
        }

        if (!ViewUtils.isEmpty(autoDSDModel)) {
            motherFollowupVisit.setDsdModel(ViewUtils.getInput(autoDSDModel));
        }

        if (!ViewUtils.isEmpty(autoMaternalOutcome)) {
            motherFollowupVisit.setMaternalOutcome(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoMaternalOutcome)));
        }

        if (!ViewUtils.isEmpty(edDateofOutcome)) {
            motherFollowupVisit.setDateOfmeternalOutcome(ViewUtils.getInput(edDateofOutcome));
        }

        if (!ViewUtils.isEmpty(autoClientVisitStatus)) {
            motherFollowupVisit.setVisitStatus(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoClientVisitStatus)));
        }

        if (!ViewUtils.isEmpty(edNameofARTFacility)) {
            motherFollowupVisit.setTransferTo(ViewUtils.getInput(edNameofARTFacility));
        }

        if (!ViewUtils.isEmpty(autoDSDModelType)) {
            motherFollowupVisit.setDsdOption(CodesetsDAO.findCodesetsCodeByDisplay(ViewUtils.getInput(autoDSDModelType)));
        }

        return motherFollowupVisit;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdateRst) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounter(ApplicationConstants.Forms.MOTHER_FOLLOW_UP_VISIT_FORM, mPresenter.getPatientId());
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
                if (isUpdateRst) {
                    mPresenter.confirmUpdate(updateEncounter(updatedMotherFollowupVisit), updatedForm);
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
    public void setErrorsVisibility(boolean edDateofVisit, boolean entryPoint, boolean fpCounselling, boolean dsd, boolean maternalOutcome, boolean dateOutcome, boolean clientStatusVisit) {
        if (edDateofVisit) {
            edVisitDateTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            edVisitDateTIL.setError("This field is required");
        }

        if (entryPoint) {
            autoEntryPointTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoEntryPointTIL.setError("This field is required");
        }

        if (fpCounselling) {
            autoFPCounsellingTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoFPCounsellingTIL.setError("This field is required");
        }

        if (dsd) {
            autoDSDTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoDSDTIL.setError("This field is required");
        }

        if (maternalOutcome) {
            autoMaternalOutcomeTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoMaternalOutcomeTIL.setError("This field is required");
        }

        if (dateOutcome) {
            dateOfOutcomeTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            dateOfOutcomeTIL.setError("This field is required");
        }

        if (clientStatusVisit) {
            autoClientVisitStatusTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            autoClientVisitStatusTIL.setError("This field is required");
        }

    }

}
