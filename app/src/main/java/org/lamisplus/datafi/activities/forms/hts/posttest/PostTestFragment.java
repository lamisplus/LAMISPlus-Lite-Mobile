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

import com.google.gson.annotations.SerializedName;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.recency.RecencyActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PostTest;
import org.lamisplus.datafi.models.PostTestCounselingKnowledgeAssessment;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.Calendar;

public class PostTestFragment extends LamisBaseFragment<PostTestContract.Presenter> implements PostTestContract.View, View.OnClickListener {

    private AutoCompleteTextView autoBringPartnerHivtesting;
    private AutoCompleteTextView autoChildrenHivtesting;
    private AutoCompleteTextView autoClientReceivedHivTestResult;
    private AutoCompleteTextView autoCondomProvidedToClient;
    private AutoCompleteTextView autoCorrectCondomUse;
    private AutoCompleteTextView autoHivRequestResult;
    private AutoCompleteTextView autoHivRequestResultCt;
    private AutoCompleteTextView autoHivTestBefore;
    private AutoCompleteTextView autoHivTestResult;
    private AutoCompleteTextView autoInformationFp;
    private AutoCompleteTextView autoPartnerFpThanCondom;
    private AutoCompleteTextView autoPartnerFpUseCondom;
    private AutoCompleteTextView autoPostTestCounseling;
    private AutoCompleteTextView autoPostTestDisclosure;
    private AutoCompleteTextView autoUnprotectedSex;
    private AutoCompleteTextView autoReferredToServices;
    private AutoCompleteTextView autoRiskReduction;

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
        if (isUpdatePostTest) {
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
        autoBringPartnerHivtesting = root.findViewById(R.id.autoBringPartnerHivtesting);
        autoChildrenHivtesting = root.findViewById(R.id.autoChildrenHivtesting);
        autoClientReceivedHivTestResult = root.findViewById(R.id.autoClientReceivedHivTestResult);
        autoCondomProvidedToClient = root.findViewById(R.id.autoCondomProvidedToClient);
        autoCorrectCondomUse = root.findViewById(R.id.autoCorrectCondomUse);
        autoHivRequestResult = root.findViewById(R.id.autoHivRequestResult);
        autoHivRequestResultCt = root.findViewById(R.id.autoHivRequestResultCt);
        autoHivTestBefore = root.findViewById(R.id.autoHivTestBefore);
        autoHivTestResult = root.findViewById(R.id.autoHivTestResult);
        autoInformationFp = root.findViewById(R.id.autoInformationFp);
        autoPartnerFpThanCondom = root.findViewById(R.id.autoPartnerFpThanCondom);
        autoPartnerFpUseCondom = root.findViewById(R.id.autoPartnerFpUseCondom);

        autoPostTestCounseling = root.findViewById(R.id.autoPostTestCounseling);
        autoPostTestDisclosure = root.findViewById(R.id.autoPostTestDisclosure);
        autoReferredToServices = root.findViewById(R.id.autoReferredToServices);
        autoUnprotectedSex = root.findViewById(R.id.autoUnprotectedSex);
        autoRiskReduction = root.findViewById(R.id.autoRiskReduction);
        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        String[] booleanPosNeg = {"Positive", "Negative"};
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanPosNeg);
        autoHivTestResult.setAdapter(adapterBooleanAnswers);

        String[] previouslyTested = {"Not Previously Tested", "Previously tested negative", "Previously tested positive in HIV care", "Previously tested positive not in HIV care"};
        ArrayAdapter<String> adapterPreviouslyTested = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, previouslyTested);
        autoHivTestBefore.setAdapter(adapterPreviouslyTested);

        String[] booleanYesNo = {"Yes", "No"};
        ArrayAdapter<String> adapterBooleanYesNo = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanYesNo);
        autoBringPartnerHivtesting.setAdapter(adapterBooleanYesNo);
        autoChildrenHivtesting.setAdapter(adapterBooleanYesNo);
        autoClientReceivedHivTestResult.setAdapter(adapterBooleanYesNo);
        autoCondomProvidedToClient.setAdapter(adapterBooleanYesNo);
        autoCorrectCondomUse.setAdapter(adapterBooleanYesNo);
        autoHivRequestResult.setAdapter(adapterBooleanYesNo);
        autoHivRequestResultCt.setAdapter(adapterBooleanYesNo);
        autoInformationFp.setAdapter(adapterBooleanYesNo);
        autoPartnerFpThanCondom.setAdapter(adapterBooleanYesNo);
        autoPartnerFpUseCondom.setAdapter(adapterBooleanYesNo);

        autoPostTestCounseling.setAdapter(adapterBooleanYesNo);
        autoPostTestDisclosure.setAdapter(adapterBooleanYesNo);
        autoUnprotectedSex.setAdapter(adapterBooleanYesNo);
        autoReferredToServices.setAdapter(adapterBooleanYesNo);
        autoRiskReduction.setAdapter(adapterBooleanYesNo);
    }


    public void fillFields(PostTest postTest) {
        if (postTest != null) {
            isUpdatePostTest = true;
            updatedPostTest = postTest;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM, mPresenter.getPatientId());

            autoBringPartnerHivtesting.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getBringPartnerHivtesting()), false);
            autoChildrenHivtesting.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getChildrenHivtesting()), false);
            autoClientReceivedHivTestResult.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getClientReceivedHivTestResult()), false);
            autoCondomProvidedToClient.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getCondomProvidedToClient()), false);
            autoCorrectCondomUse.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getCorrectCondomUse()), false);
            autoHivRequestResult.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getHivRequestResult()), false);
            autoHivRequestResultCt.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getHivRequestResultCt()), false);
            autoHivTestBefore.setText(postTest.getPostTestCounselingKnowledgeAssessment().getHivTestBefore(), false);
            autoHivTestResult.setText(StringUtils.changeBooleanToPosNeg(postTest.getPostTestCounselingKnowledgeAssessment().getHivTestResult()), false);
            autoInformationFp.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getInformationFp()), false);
            autoPartnerFpThanCondom.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getPartnerFpThanCondom()), false);
            autoPartnerFpUseCondom.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getPartnerFpUseCondom()), false);

            autoPostTestCounseling.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getPostTestCounseling()), false);
            autoPostTestDisclosure.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getPostTestDisclosure()), false);
            autoReferredToServices.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getReferredToServices()), false);
            autoUnprotectedSex.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getUnprotectedSexRegularPartnerLastThreeMonth()), false);
            autoRiskReduction.setText(StringUtils.changeBooleanToString(postTest.getPostTestCounselingKnowledgeAssessment().getRiskReduction()), false);
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
        PostTestCounselingKnowledgeAssessment postTestCounselingKnowledgeAssessment = new PostTestCounselingKnowledgeAssessment();

        if (!ViewUtils.isEmpty(autoBringPartnerHivtesting)) {
            postTestCounselingKnowledgeAssessment.setBringPartnerHivtesting(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoBringPartnerHivtesting)));
        }

        if (!ViewUtils.isEmpty(autoBringPartnerHivtesting)) {
            postTestCounselingKnowledgeAssessment.setBringPartnerHivtesting(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoBringPartnerHivtesting)));
        }

        if (!ViewUtils.isEmpty(autoChildrenHivtesting)) {
            postTestCounselingKnowledgeAssessment.setChildrenHivtesting(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoChildrenHivtesting)));
        }

        if (!ViewUtils.isEmpty(autoHivRequestResultCt)) {
            postTestCounselingKnowledgeAssessment.setHivRequestResultCt(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoHivRequestResultCt)));
        }



        if (!ViewUtils.isEmpty(autoClientReceivedHivTestResult)) {
            postTestCounselingKnowledgeAssessment.setClientReceivedHivTestResult(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoClientReceivedHivTestResult)));
        }

        if (!ViewUtils.isEmpty(autoCondomProvidedToClient)) {
            postTestCounselingKnowledgeAssessment.setCondomProvidedToClient(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoCondomProvidedToClient)));
        }

        if (!ViewUtils.isEmpty(autoCorrectCondomUse)) {
            postTestCounselingKnowledgeAssessment.setCorrectCondomUse(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoCorrectCondomUse)));
        }

        if (!ViewUtils.isEmpty(autoHivRequestResult)) {
            postTestCounselingKnowledgeAssessment.setHivRequestResult(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoHivRequestResult)));
        }

        if (!ViewUtils.isEmpty(autoHivTestBefore)) {
            postTestCounselingKnowledgeAssessment.setHivTestBefore(ViewUtils.getInput(autoHivTestBefore));
        }

        if (!ViewUtils.isEmpty(autoHivTestResult)) {
            postTestCounselingKnowledgeAssessment.setHivTestResult(StringUtils.changePosNegtoBooleanString(ViewUtils.getInput(autoHivTestResult)));
        }

        if (!ViewUtils.isEmpty(autoInformationFp)) {
            postTestCounselingKnowledgeAssessment.setInformationFp(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoInformationFp)));
        }

        if (!ViewUtils.isEmpty(autoPartnerFpThanCondom)) {
            postTestCounselingKnowledgeAssessment.setPartnerFpThanCondom(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoPartnerFpThanCondom)));
        }

        if (!ViewUtils.isEmpty(autoPartnerFpUseCondom)) {
            postTestCounselingKnowledgeAssessment.setPartnerFpUseCondom(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoPartnerFpUseCondom)));
        }

        if (!ViewUtils.isEmpty(autoPostTestCounseling)) {
            postTestCounselingKnowledgeAssessment.setPostTestCounseling(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoPostTestCounseling)));
        }

        if (!ViewUtils.isEmpty(autoPostTestDisclosure)) {
            postTestCounselingKnowledgeAssessment.setPostTestDisclosure(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoPostTestDisclosure)));
        }

        if (!ViewUtils.isEmpty(autoUnprotectedSex)) {
            postTestCounselingKnowledgeAssessment.setUnprotectedSexRegularPartnerLastThreeMonth(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoUnprotectedSex)));
        }

        if (!ViewUtils.isEmpty(autoReferredToServices)) {
            postTestCounselingKnowledgeAssessment.setReferredToServices(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoReferredToServices)));
        }

        if (!ViewUtils.isEmpty(autoRiskReduction)) {
            postTestCounselingKnowledgeAssessment.setRiskReduction(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoRiskReduction)));
        }

        postTest.setPostTestCounselingKnowledgeAssessment(postTestCounselingKnowledgeAssessment);

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
            Intent recencyProgram = new Intent(LamisPlus.getInstance(), RecencyActivity.class);
            recencyProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            startActivity(recencyProgram);
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
    }


}
