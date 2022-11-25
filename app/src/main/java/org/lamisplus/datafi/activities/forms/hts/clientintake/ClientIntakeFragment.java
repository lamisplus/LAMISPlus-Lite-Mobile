package org.lamisplus.datafi.activities.forms.hts.clientintake;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;

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
    private EditText edVisitDate;
    private EditText edNoWives;
    private EditText edNumberOfChildren;

    private Button mSaveContinueButton;

    private boolean isUpdateClientIntake = false;
    private Encounter updatedForm;
    private ClientIntake updatedClientIntake;

    private String packageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_client_intake, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            showDatePickers();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(isUpdateClientIntake) {
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
        edVisitDate = root.findViewById(R.id.edVisitDate);
        edNumberOfChildren = root.findViewById(R.id.edNoChildren);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
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
                edVisitDate.setText(selectedYear + "-" + adjustedMonth + "-" + selectedDay);
            }, cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.setTitle(getString(R.string.date_picker_title));
            mDatePicker.show();
        });
    }

    public void fillFields(ClientIntake clientIntake) {
        if (clientIntake != null) {
            isUpdateClientIntake = true;
            updatedClientIntake = clientIntake;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId());
        }
    }

    private ClientIntake createEncounter() {
        ClientIntake clientIntake = new ClientIntake();
        updateEncounterWithData(clientIntake);
        return clientIntake;
    }

    private ClientIntake updateEncounter(ClientIntake clientIntake) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(clientIntake);
        return clientIntake;
    }

    private ClientIntake updateEncounterWithData(ClientIntake clientIntake) {
        if (!ViewUtils.isEmpty(autoTargetGroup)) {
            clientIntake.setTargetGroup(ViewUtils.getInput(autoTargetGroup));
        }

        if (!ViewUtils.isEmpty(edClientCode)) {
            clientIntake.setClientCode(ViewUtils.getInput(edClientCode));
        }

        if (!ViewUtils.isEmpty(autoReferredFrom)) {
            clientIntake.setReferredFrom(ViewUtils.getInput(autoReferredFrom));
        }

        if (!ViewUtils.isEmpty(autoSettings)) {
            clientIntake.setTestingSetting(ViewUtils.getInput(autoSettings));
        }

        if (!ViewUtils.isEmpty(edVisitDate)) {
            clientIntake.setDateVisit(ViewUtils.getInput(edVisitDate));
        }

        if (!ViewUtils.isEmpty(edNumberOfChildren)) {
            clientIntake.setNumChildren(Integer.parseInt(ViewUtils.getInput(edNumberOfChildren)));
        }

        if (!ViewUtils.isEmpty(autoIndexTesting)) {
            clientIntake.setIndexClient(Boolean.valueOf(ViewUtils.getInput(autoReferredFrom)));
        }

        if (!ViewUtils.isEmpty(edNoWives)) {
            clientIntake.setNumWives(Integer.parseInt(ViewUtils.getInput(edNoWives)));
        }

        if (!ViewUtils.isEmpty(autoPregnant)) {
            clientIntake.setPregnant(ViewUtils.getInput(autoPregnant));
        }

        if (!ViewUtils.isEmpty(autoBreastfeeding)) {
            clientIntake.setBreastFeeding(Boolean.valueOf(ViewUtils.getInput(autoBreastfeeding)));
        }

        if (!ViewUtils.isEmpty(autoFirstimeVisit)) {
            clientIntake.setFirstTimeVisit(Boolean.valueOf(ViewUtils.getInput(autoFirstimeVisit)));
        }

        if (!ViewUtils.isEmpty(autoPreviouslyTested)) {
            clientIntake.setPreviouslyTested(Boolean.valueOf(ViewUtils.getInput(autoPreviouslyTested)));
        }


        if (!ViewUtils.isEmpty(autoTypeCounseling)) {
            clientIntake.setTypeCounseling(ViewUtils.getInput(autoTypeCounseling));
        }

        if (!ViewUtils.isEmpty(autoRelationshipIndex)) {
            clientIntake.setRelationWithIndexClient(ViewUtils.getInput(autoRelationshipIndex));
        }

        clientIntake.setPersonId("");

        clientIntake.setPersonDto("{}");

        clientIntake.setExtra("{}");

        return clientIntake;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if (isUpdateClientIntake) {
                    mPresenter.confirmUpdate(updateEncounter(updatedClientIntake), updatedForm);
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


    @Override
    public void startActivityForPreTestForm() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            Intent preTestProgram = new Intent(LamisPlus.getInstance(), PreTestActivity.class);
            preTestProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            startActivity(preTestProgram);
        }else{
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
