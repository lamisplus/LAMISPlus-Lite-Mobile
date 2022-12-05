package org.lamisplus.datafi.activities.forms.hts.pretest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.requestresult.RequestResultActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.KnowledgeAssessment;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.RiskAssessment;
import org.lamisplus.datafi.models.SexPartnerRiskAssessment;
import org.lamisplus.datafi.models.StiScreening;
import org.lamisplus.datafi.models.TbScreening;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ViewUtils;

public class PreTestFragment extends LamisBaseFragment<PreTestContract.Presenter> implements PreTestContract.View, View.OnClickListener {


    private AutoCompleteTextView autoPreviousTestedHIVNegative;
    private AutoCompleteTextView autoClientInformHivTransRoutes;
    private AutoCompleteTextView autoClientInformRiskkHivTrans;
    private AutoCompleteTextView autoClientInformPreventingsHivTrans;
    private AutoCompleteTextView autoClientInformPossibleTestResult;
    private AutoCompleteTextView autoInformConsentHivTest;
    private AutoCompleteTextView autoTimeLastHIVNegativeTestResult;
    private AutoCompleteTextView autoclientPregnant;


    private AutoCompleteTextView autoCurrentCough;
    private AutoCompleteTextView autoFever;
    private AutoCompleteTextView autoLymphadenopathy;
    private AutoCompleteTextView autoNightSweats;
    private AutoCompleteTextView autoWeightLoss;
    private AutoCompleteTextView autoeverHadSexualIntercourse;
    private AutoCompleteTextView autobloodtransInlastThreeMonths;


    private AutoCompleteTextView autouprotectedSexWithCasualLastThreeMonths;
    private AutoCompleteTextView autouprotectedSexWithRegularPartnerLastThreeMonths;
    private AutoCompleteTextView autounprotectedVaginalSex;
    private AutoCompleteTextView autoUprotectedAnalSexHivRiskAssess;
    private AutoCompleteTextView autostiLastThreeMonths;
    private AutoCompleteTextView autosexUnderInfluence;
    private AutoCompleteTextView automoreThanOneSexPartnerLastThreeMonths;
    private AutoCompleteTextView autoCurrentlyArvForPmtct;
    private AutoCompleteTextView autoKnowHivPositiveAfterLostToFollowUp;
    private AutoCompleteTextView autoKnowHivPositiveOnArv;
    private AutoCompleteTextView autoNewDiagnosedHivlastThreeMonths;
    private AutoCompleteTextView autoSexPartnerHivPositive;
    private AutoCompleteTextView autoUprotectedAnalSex;
    private AutoCompleteTextView autoComplaintsGenitalSore;
    private AutoCompleteTextView autoComplaintsOfScrotal;
    private AutoCompleteTextView autoUrethralDischarge;


    private Button mSaveContinueButton;

    private boolean isUpdatePretest = false;
    private Encounter updatedForm;
    private PreTest updatedPretest;
    private String packageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pre_test, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            packageName = LamisPlus.getInstance().getPackageName(getActivity());
            if (mPresenter.patientToUpdate(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM, mPresenter.getPatientId()) != null) {
                fillFields(mPresenter.patientToUpdate(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM, mPresenter.getPatientId()));
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isUpdatePretest) {
            inflater.inflate(R.menu.delete_multi_patient_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.confirmDeleteEncounterPreTest(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM, mPresenter.getPatientId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static PreTestFragment newInstance() {
        return new PreTestFragment();
    }

    private void initiateFragmentViews(View root) {
        autoPreviousTestedHIVNegative = root.findViewById(R.id.autoPreviousTestedHIVNegative);
        autoClientInformHivTransRoutes = root.findViewById(R.id.autoClientInformHivTransRoutes);
        autoClientInformRiskkHivTrans = root.findViewById(R.id.autoClientInformRiskkHivTrans);
        autoClientInformPreventingsHivTrans = root.findViewById(R.id.autoClientInformPreventingsHivTrans);
        autoClientInformPossibleTestResult = root.findViewById(R.id.autoClientInformPossibleTestResult);
        autoInformConsentHivTest = root.findViewById(R.id.autoInformConsentHivTest);
        autoTimeLastHIVNegativeTestResult = root.findViewById(R.id.autoTimeLastHIVNegativeTestResult);
        autoclientPregnant = root.findViewById(R.id.autoclientPregnant);

        autoCurrentCough = root.findViewById(R.id.autoCurrentCough);
        autoFever = root.findViewById(R.id.autoFever);
        autoLymphadenopathy = root.findViewById(R.id.autoLymphadenopathy);
        autoNightSweats = root.findViewById(R.id.autoNightSweats);
        autoWeightLoss = root.findViewById(R.id.autoWeightLoss);

        autoeverHadSexualIntercourse = root.findViewById(R.id.autoeverHadSexualIntercourse);
        autobloodtransInlastThreeMonths = root.findViewById(R.id.autobloodtransInlastThreeMonths);
        autouprotectedSexWithCasualLastThreeMonths = root.findViewById(R.id.autouprotectedSexWithCasualLastThreeMonths);
        autouprotectedSexWithRegularPartnerLastThreeMonths = root.findViewById(R.id.autouprotectedSexWithRegularPartnerLastThreeMonths);
        autounprotectedVaginalSex = root.findViewById(R.id.autounprotectedVaginalSex);
        autoCurrentCough = root.findViewById(R.id.autoCurrentCough);
        autoUprotectedAnalSexHivRiskAssess = root.findViewById(R.id.autoUprotectedAnalSexHivRiskAssess);
        autosexUnderInfluence = root.findViewById(R.id.autosexUnderInfluence);
        automoreThanOneSexPartnerLastThreeMonths = root.findViewById(R.id.automoreThanOneSexPartnerLastThreeMonths);

        autostiLastThreeMonths = root.findViewById(R.id.autostiLastThreeMonths);

        autoCurrentlyArvForPmtct = root.findViewById(R.id.autoCurrentlyArvForPmtct);
        autoKnowHivPositiveAfterLostToFollowUp = root.findViewById(R.id.autoKnowHivPositiveAfterLostToFollowUp);
        autoKnowHivPositiveOnArv = root.findViewById(R.id.autoKnowHivPositiveOnArv);
        autoNewDiagnosedHivlastThreeMonths = root.findViewById(R.id.autoNewDiagnosedHivlastThreeMonths);
        autoSexPartnerHivPositive = root.findViewById(R.id.autoSexPartnerHivPositive);
        autoUprotectedAnalSex = root.findViewById(R.id.autoUprotectedAnalSex);

        autoComplaintsGenitalSore = root.findViewById(R.id.autoComplaintsGenitalSore);
        autoComplaintsOfScrotal = root.findViewById(R.id.autoComplaintsOfScrotal);
        autoUrethralDischarge = root.findViewById(R.id.autoUrethralDischarge);


        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);
    }

    private void setListeners() {
        mSaveContinueButton.setOnClickListener(this);

        String[] lastNegativeTestResult = getResources().getStringArray(R.array.last_hiv_negative_test_result);
        ArrayAdapter<String> adapterLastNegativeTestResult = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, lastNegativeTestResult);
        autoTimeLastHIVNegativeTestResult.setAdapter(adapterLastNegativeTestResult);

        String[] booleanAnswers = getResources().getStringArray(R.array.booleanAnswers);
        ArrayAdapter<String> adapterBooleanAnswers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, booleanAnswers);

        autoPreviousTestedHIVNegative.setAdapter(adapterBooleanAnswers);
        autoClientInformHivTransRoutes.setAdapter(adapterBooleanAnswers);
        autoClientInformRiskkHivTrans.setAdapter(adapterBooleanAnswers);
        autoClientInformPreventingsHivTrans.setAdapter(adapterBooleanAnswers);
        autoClientInformPossibleTestResult.setAdapter(adapterBooleanAnswers);
        autoInformConsentHivTest.setAdapter(adapterBooleanAnswers);
        autoclientPregnant.setAdapter(adapterBooleanAnswers);

        autoCurrentCough.setAdapter(adapterBooleanAnswers);
        autoFever.setAdapter(adapterBooleanAnswers);
        autoLymphadenopathy.setAdapter(adapterBooleanAnswers);
        autoNightSweats.setAdapter(adapterBooleanAnswers);
        autoWeightLoss.setAdapter(adapterBooleanAnswers);

        autoeverHadSexualIntercourse.setAdapter(adapterBooleanAnswers);
        autobloodtransInlastThreeMonths.setAdapter(adapterBooleanAnswers);
        autouprotectedSexWithCasualLastThreeMonths.setAdapter(adapterBooleanAnswers);
        autouprotectedSexWithRegularPartnerLastThreeMonths.setAdapter(adapterBooleanAnswers);
        autounprotectedVaginalSex.setAdapter(adapterBooleanAnswers);
        autoUprotectedAnalSexHivRiskAssess.setAdapter(adapterBooleanAnswers);
        autostiLastThreeMonths.setAdapter(adapterBooleanAnswers);
        autosexUnderInfluence.setAdapter(adapterBooleanAnswers);
        automoreThanOneSexPartnerLastThreeMonths.setAdapter(adapterBooleanAnswers);

        autoCurrentlyArvForPmtct.setAdapter(adapterBooleanAnswers);
        autoKnowHivPositiveAfterLostToFollowUp.setAdapter(adapterBooleanAnswers);
        autoKnowHivPositiveOnArv.setAdapter(adapterBooleanAnswers);
        autoNewDiagnosedHivlastThreeMonths.setAdapter(adapterBooleanAnswers);
        autoSexPartnerHivPositive.setAdapter(adapterBooleanAnswers);
        autoUprotectedAnalSex.setAdapter(adapterBooleanAnswers);

        autoComplaintsGenitalSore.setAdapter(adapterBooleanAnswers);
        autoComplaintsOfScrotal.setAdapter(adapterBooleanAnswers);
        autoUrethralDischarge.setAdapter(adapterBooleanAnswers);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveContinueButton:
                if (isUpdatePretest) {
                    mPresenter.confirmUpdate(updateEncounter(updatedPretest), updatedForm);
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

    public void fillFields(PreTest preTest) {
        if (preTest != null) {
            Log.v("Baron", new Gson().toJson(preTest));
            isUpdatePretest = true;
            updatedPretest = preTest;
            updatedForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM, mPresenter.getPatientId());

            autoPreviousTestedHIVNegative.setText(StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isPreviousTestedHIVNegative()), false);

            autoTimeLastHIVNegativeTestResult.setText(preTest.getKnowledgeAssessment().getTimeLastHIVNegativeTestResult(), false);

            autoclientPregnant.setText(StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isClientPregnant()), false);

            autoClientInformHivTransRoutes.setText(StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isClientInformHivTransRoutes()), false);

            autoClientInformPossibleTestResult.setText(StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isClientInformPossibleTestResult()), false);

            autoClientInformPreventingsHivTrans.setText(StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isClientInformPreventingsHivTrans()), false);

            autoClientInformRiskkHivTrans.setText(StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isClientInformRiskkHivTrans()), false);

            autoInformConsentHivTest.setText(StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isInformConsentHivTest()), false);

            autoPreviousTestedHIVNegative.setText(StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isPreviousTestedHIVNegative()), false);

            //TB Screening
            autoCurrentCough.setText(StringUtils.changeBooleanToString(preTest.getTbScreening().isCurrentCough()), false);

            autoWeightLoss.setText(StringUtils.changeBooleanToString(preTest.getTbScreening().isWeightLoss()), false);

            autoLymphadenopathy.setText(StringUtils.changeBooleanToString(preTest.getTbScreening().isLymphadenopathy()), false);

            autoFever.setText(StringUtils.changeBooleanToString(preTest.getTbScreening().isFever()), false);

            autoNightSweats.setText(StringUtils.changeBooleanToString(preTest.getTbScreening().isNightSweats()), false);

            //HIV Risk Assessment
            autoeverHadSexualIntercourse.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isEverHadSexualIntercourse()), false);

            autobloodtransInlastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isBloodtransInlastThreeMonths()), false);

            autouprotectedSexWithCasualLastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isUprotectedSexWithCasualLastThreeMonths()), false);

            autouprotectedSexWithRegularPartnerLastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isUprotectedSexWithRegularPartnerLastThreeMonths()), false);

            autounprotectedVaginalSex.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isUnprotectedVaginalSex()), false);

            autoUprotectedAnalSexHivRiskAssess.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isUprotectedAnalSex()), false);

            autostiLastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isStiLastThreeMonths()), false);

            autosexUnderInfluence.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isSexUnderInfluence()), false);

            automoreThanOneSexPartnerLastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isMoreThanOneSexPartnerLastThreeMonths()), false);

            //Sex Partner Risk Assessment
            autoSexPartnerHivPositive.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isSexPartnerHivPositive()), false);

            autoCurrentlyArvForPmtct.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isCurrentlyArvForPmtct()), false);

            autoNewDiagnosedHivlastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isNewDiagnosedHivlastThreeMonths()), false);

            autoKnowHivPositiveOnArv.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isKnowHivPositiveOnArv()), false);

            autoKnowHivPositiveAfterLostToFollowUp.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isKnowHivPositiveAfterLostToFollowUp()), false);

            autoUprotectedAnalSex.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isUprotectedAnalSex()), false);

            //STI Screening

            autoComplaintsGenitalSore.setText(StringUtils.changeBooleanToString(preTest.getStiScreening().isComplaintsGenitalSore()), false);

            autoComplaintsOfScrotal.setText(StringUtils.changeBooleanToString(preTest.getStiScreening().isComplaintsOfScrotal()), false);

            autoUrethralDischarge.setText(StringUtils.changeBooleanToString(preTest.getStiScreening().isUrethralDischarge()), false);

        }
    }

    private PreTest createEncounter() {
        PreTest preTest = new PreTest();
        updateEncounterWithData(preTest);
        return preTest;
    }

    private PreTest updateEncounter(PreTest preTest) {
        Encounter.load(Encounter.class, updatedForm.getId());
        updateEncounterWithData(preTest);
        return preTest;
    }

    private PreTest updateEncounterWithData(PreTest preTest) {
        KnowledgeAssessment knowledgeAssessment = new KnowledgeAssessment();
        if (!ViewUtils.isEmpty(autoPreviousTestedHIVNegative)) {
            knowledgeAssessment.setPreviousTestedHIVNegative(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoPreviousTestedHIVNegative)));
        }

        if (!ViewUtils.isEmpty(autoTimeLastHIVNegativeTestResult)) {
            knowledgeAssessment.setTimeLastHIVNegativeTestResult(ViewUtils.getInput(autoTimeLastHIVNegativeTestResult));
        }

        if (!ViewUtils.isEmpty(autoclientPregnant)) {
            knowledgeAssessment.setClientPregnant(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoclientPregnant)));
        }

        if (!ViewUtils.isEmpty(autoClientInformHivTransRoutes)) {
            knowledgeAssessment.setClientInformHivTransRoutes(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoClientInformHivTransRoutes)));
        }


        if (!ViewUtils.isEmpty(autoClientInformPossibleTestResult)) {
            knowledgeAssessment.setClientInformPossibleTestResult(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoClientInformPossibleTestResult)));
        }

        if (!ViewUtils.isEmpty(autoClientInformPreventingsHivTrans)) {
            knowledgeAssessment.setClientInformPreventingsHivTrans(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoClientInformPreventingsHivTrans)));
        }

        if (!ViewUtils.isEmpty(autoClientInformRiskkHivTrans)) {
            knowledgeAssessment.setClientInformRiskkHivTrans(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoClientInformRiskkHivTrans)));
        }

        if (!ViewUtils.isEmpty(autoInformConsentHivTest)) {
            knowledgeAssessment.setInformConsentHivTest(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoInformConsentHivTest)));
        }

        if (!ViewUtils.isEmpty(autoPreviousTestedHIVNegative)) {
            knowledgeAssessment.setPreviousTestedHIVNegative(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoPreviousTestedHIVNegative)));
        }

        preTest.setKnowledgeAssessment(knowledgeAssessment);

        RiskAssessment riskAssessment = new RiskAssessment();

        if (!ViewUtils.isEmpty(autoeverHadSexualIntercourse)) {
            riskAssessment.setEverHadSexualIntercourse(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoeverHadSexualIntercourse)));
        }

        if (!ViewUtils.isEmpty(autobloodtransInlastThreeMonths)) {
            riskAssessment.setBloodtransInlastThreeMonths(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autobloodtransInlastThreeMonths)));
        }

        if (!ViewUtils.isEmpty(autouprotectedSexWithCasualLastThreeMonths)) {
            riskAssessment.setUprotectedSexWithCasualLastThreeMonths(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autouprotectedSexWithCasualLastThreeMonths)));
        }

        if (!ViewUtils.isEmpty(autouprotectedSexWithRegularPartnerLastThreeMonths)) {
            riskAssessment.setUprotectedSexWithRegularPartnerLastThreeMonths(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autouprotectedSexWithRegularPartnerLastThreeMonths)));
        }

        if (!ViewUtils.isEmpty(autounprotectedVaginalSex)) {
            riskAssessment.setUnprotectedVaginalSex(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autounprotectedVaginalSex)));
        }

        if (!ViewUtils.isEmpty(autoUprotectedAnalSexHivRiskAssess)) {
            riskAssessment.setUprotectedAnalSex(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoUprotectedAnalSexHivRiskAssess)));
        }

        if (!ViewUtils.isEmpty(autostiLastThreeMonths)) {
            riskAssessment.setStiLastThreeMonths(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autostiLastThreeMonths)));
        }

        if (!ViewUtils.isEmpty(autosexUnderInfluence)) {
            riskAssessment.setSexUnderInfluence(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autosexUnderInfluence)));
        }

        if (!ViewUtils.isEmpty(automoreThanOneSexPartnerLastThreeMonths)) {
            riskAssessment.setMoreThanOneSexPartnerLastThreeMonths(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(automoreThanOneSexPartnerLastThreeMonths)));
        }


        preTest.setRiskAssessment(riskAssessment);

        StiScreening stiScreening = new StiScreening();

        if (!ViewUtils.isEmpty(autoComplaintsGenitalSore)) {
            stiScreening.setComplaintsGenitalSore(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoComplaintsGenitalSore)));
        }

        if (!ViewUtils.isEmpty(autoComplaintsOfScrotal)) {
            stiScreening.setComplaintsOfScrotal(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoComplaintsOfScrotal)));
        }

        if (!ViewUtils.isEmpty(autoUrethralDischarge)) {
            stiScreening.setUrethralDischarge(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoUrethralDischarge)));
        }

        preTest.setStiScreening(stiScreening);

        TbScreening tbScreening = new TbScreening();
        if (!ViewUtils.isEmpty(autoCurrentCough)) {
            tbScreening.setCurrentCough(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoCurrentCough)));
        }

        if (!ViewUtils.isEmpty(autoWeightLoss)) {
            tbScreening.setWeightLoss(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoWeightLoss)));
        }

        if (!ViewUtils.isEmpty(autoLymphadenopathy)) {
            tbScreening.setLymphadenopathy(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoLymphadenopathy)));
        }

        if (!ViewUtils.isEmpty(autoFever)) {
            tbScreening.setFever(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoFever)));
        }

        if (!ViewUtils.isEmpty(autoNightSweats)) {
            tbScreening.setNightSweats(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoNightSweats)));
        }

        preTest.setTbScreening(tbScreening);

        SexPartnerRiskAssessment sexPartnerRiskAssessment = new SexPartnerRiskAssessment();

        if (!ViewUtils.isEmpty(autoSexPartnerHivPositive)) {
            sexPartnerRiskAssessment.setSexPartnerHivPositive(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoSexPartnerHivPositive)));
        }

        if (!ViewUtils.isEmpty(autoCurrentlyArvForPmtct)) {
            sexPartnerRiskAssessment.setCurrentlyArvForPmtct(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoCurrentlyArvForPmtct)));
        }

        if (!ViewUtils.isEmpty(autoNewDiagnosedHivlastThreeMonths)) {
            sexPartnerRiskAssessment.setNewDiagnosedHivlastThreeMonths(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoNewDiagnosedHivlastThreeMonths)));
        }

        if (!ViewUtils.isEmpty(autoKnowHivPositiveOnArv)) {
            sexPartnerRiskAssessment.setKnowHivPositiveOnArv(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoKnowHivPositiveOnArv)));
        }

        if (!ViewUtils.isEmpty(autoKnowHivPositiveAfterLostToFollowUp)) {
            sexPartnerRiskAssessment.setKnowHivPositiveAfterLostToFollowUp(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoKnowHivPositiveAfterLostToFollowUp)));
        }

        if (!ViewUtils.isEmpty(autoUprotectedAnalSex)) {
            sexPartnerRiskAssessment.setUprotectedAnalSex(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoUprotectedAnalSex)));
        }

        preTest.setSexPartnerRiskAssessment(sexPartnerRiskAssessment);

        return preTest;
    }


    @Override
    public void startActivityForRequestResultForm() {
        Encounter encounter = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.REQUEST_RESULT_FORM, mPresenter.getPatientId());
        if (encounter == null) {
            Intent preTestProgram = new Intent(LamisPlus.getInstance(), RequestResultActivity.class);
            preTestProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                    String.valueOf(mPresenter.getPatientId()));
            startActivity(preTestProgram);
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
