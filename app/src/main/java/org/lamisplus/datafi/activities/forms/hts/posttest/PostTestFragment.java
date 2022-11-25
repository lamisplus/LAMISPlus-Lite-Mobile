package org.lamisplus.datafi.activities.forms.hts.posttest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PostTest;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.utilities.ApplicationConstants;

import java.util.Calendar;

public class PostTestFragment extends LamisBaseFragment<PostTestContract.Presenter> implements PostTestContract.View, View.OnClickListener {

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

    private boolean isUpdatePostTest = false;
    private Encounter updatedForm;
    private PostTest updatedPostTest;

    private String packageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_test, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(isUpdatePostTest) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounterPostTest(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM, mPresenter.getPatientId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static PostTestFragment newInstance() {
        return new PostTestFragment();
    }

    private void initiateFragmentViews(View root) {

        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

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

    public void fillFields(PostTest postTest) {
        if (postTest != null) {
            isUpdatePostTest = true;
            updatedPostTest = postTest;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM, mPresenter.getPatientId());
        }
    }

    private PostTest createEncounter() {
        PostTest postTest = new PostTest();
        updateEncounterWithData(postTest);
        return postTest;
    }

    private PostTest updateEncounter(PostTest postTest) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(postTest);
        return postTest;
    }

    private PostTest updateEncounterWithData(PostTest postTest) {


        return postTest;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if (isUpdatePostTest) {
                    mPresenter.confirmUpdate(updateEncounter(updatedPostTest), updatedForm);
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
    public void startActivityForRecencyForm() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            Intent preTestProgram = new Intent(LamisPlus.getInstance(), Recency.class);
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
