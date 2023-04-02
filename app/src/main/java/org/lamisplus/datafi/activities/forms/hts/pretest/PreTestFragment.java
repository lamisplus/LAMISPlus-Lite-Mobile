package org.lamisplus.datafi.activities.forms.hts.pretest;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.hts.requestresult.RequestResultActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.KnowledgeAssessment;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.RiskAssessment;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.models.SexPartnerRiskAssessment;
import org.lamisplus.datafi.models.StiScreening;
import org.lamisplus.datafi.models.TbScreening;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
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
    private AutoCompleteTextView autovaginalDischarge;
    private AutoCompleteTextView autolowerAbdominalPains;


    private Button mSaveContinueButton;

    private boolean isUpdatePretest = false;
    private Encounter updatedForm;
    private PreTest updatedPretest;
    private String packageName;
    private TextInputLayout clientInformPreventingsHivTransTIL;
    private TextInputLayout autoTimeLastHIVNegativeTestResultTIL;
    private TextInputLayout clientPregnantTIL;
    private TextInputLayout autoComplaintsOfScrotalTIL;
    private LinearLayout sexPartnerRiskAssessLayout;

    private TextInputLayout everHadSexualIntercourseTIL;
    private TextInputLayout bloodtransInlastThreeMonthsTIL;
    private TextInputLayout uprotectedSexWithCasualLastThreeMonthsTIL;
    private TextInputLayout uprotectedSexWithRegularPartnerLastThreeMonthsTIL;
    private TextInputLayout autounprotectedVaginalSexTIL;
    private TextInputLayout uprotectedAnalSexHivRiskAssessTIL;
    private TextInputLayout stiLastThreeMonthsTIL;
    private TextInputLayout sexUnderInfluenceTIL;
    private TextInputLayout moreThanOneSexPartnerLastThreeMonthsTIL;


    private TextInputLayout experiencePainTIL;
    private TextInputLayout haveSexWithoutCondomTIL;
    private TextInputLayout haveCondomBurstTIL;
    private TextInputLayout abuseDrugTIL;
    private TextInputLayout autobloodTransfusionTIL;
    private TextInputLayout consistentWeightFeverNightCoughTIL;
    private TextInputLayout soldPaidVaginalSexTIL;

    private AutoCompleteTextView autoexperiencePain;
    private AutoCompleteTextView autohaveSexWithoutCondom;
    private AutoCompleteTextView autohaveCondomBurst;
    private AutoCompleteTextView autoabuseDrug;
    private AutoCompleteTextView autobloodTransfusion;
    private AutoCompleteTextView autoconsistentWeightFeverNightCough;
    private AutoCompleteTextView autosoldPaidVaginalSex;

    private LinearLayout othersHIVRiskAssessmentView;
    private LinearLayout genPopHIVRiskAssessmentView;
    private LinearLayout syndromicSTIFemale;
    private LinearLayout syndromicSTIMale;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pre_test, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            dropDownClickListeners();
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
        autovaginalDischarge = root.findViewById(R.id.autovaginalDischarge);
        autolowerAbdominalPains = root.findViewById(R.id.autolowerAbdominalPains);


        mSaveContinueButton = root.findViewById(R.id.saveContinueButton);

        clientInformPreventingsHivTransTIL = root.findViewById(R.id.clientInformPreventingsHivTransTIL);
        autoTimeLastHIVNegativeTestResultTIL = root.findViewById(R.id.autoTimeLastHIVNegativeTestResultTIL);
        clientPregnantTIL = root.findViewById(R.id.clientPregnantTIL);
        autoComplaintsOfScrotalTIL = root.findViewById(R.id.autoComplaintsOfScrotalTIL);
        sexPartnerRiskAssessLayout = root.findViewById(R.id.sexPartnerRiskAssessLayout);

        everHadSexualIntercourseTIL = root.findViewById(R.id.everHadSexualIntercourseTIL);
        bloodtransInlastThreeMonthsTIL = root.findViewById(R.id.bloodtransInlastThreeMonthsTIL);
        uprotectedSexWithCasualLastThreeMonthsTIL = root.findViewById(R.id.uprotectedSexWithCasualLastThreeMonthsTIL);
        uprotectedSexWithRegularPartnerLastThreeMonthsTIL = root.findViewById(R.id.uprotectedSexWithRegularPartnerLastThreeMonthsTIL);
        autounprotectedVaginalSexTIL = root.findViewById(R.id.autounprotectedVaginalSexTIL);
        uprotectedAnalSexHivRiskAssessTIL = root.findViewById(R.id.uprotectedAnalSexHivRiskAssessTIL);
        stiLastThreeMonthsTIL = root.findViewById(R.id.stiLastThreeMonthsTIL);
        sexUnderInfluenceTIL = root.findViewById(R.id.sexUnderInfluenceTIL);
        moreThanOneSexPartnerLastThreeMonthsTIL = root.findViewById(R.id.moreThanOneSexPartnerLastThreeMonthsTIL);


        experiencePainTIL = root.findViewById(R.id.experiencePainTIL);
        haveSexWithoutCondomTIL = root.findViewById(R.id.haveSexWithoutCondomTIL);
        haveCondomBurstTIL = root.findViewById(R.id.haveCondomBurstTIL);
        abuseDrugTIL = root.findViewById(R.id.abuseDrugTIL);
        autobloodTransfusionTIL = root.findViewById(R.id.autobloodTransfusionTIL);
        consistentWeightFeverNightCoughTIL = root.findViewById(R.id.consistentWeightFeverNightCoughTIL);
        soldPaidVaginalSexTIL = root.findViewById(R.id.soldPaidVaginalSexTIL);

        autoexperiencePain = root.findViewById(R.id.autoexperiencePain);
        autohaveSexWithoutCondom = root.findViewById(R.id.autohaveSexWithoutCondom);
        autohaveCondomBurst = root.findViewById(R.id.autohaveCondomBurst);
        autoabuseDrug = root.findViewById(R.id.autoabuseDrug);
        autobloodTransfusion = root.findViewById(R.id.autobloodTransfusion);
        autoconsistentWeightFeverNightCough = root.findViewById(R.id.autoconsistentWeightFeverNightCough);
        autosoldPaidVaginalSex = root.findViewById(R.id.autosoldPaidVaginalSex);

        othersHIVRiskAssessmentView = root.findViewById(R.id.othersHIVRiskAssessmentView);
        genPopHIVRiskAssessmentView = root.findViewById(R.id.genPopHIVRiskAssessmentView);

        syndromicSTIFemale = root.findViewById(R.id.syndromicSTIFemale);
        syndromicSTIMale = root.findViewById(R.id.syndromicSTIMale);
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
        autovaginalDischarge.setAdapter(adapterBooleanAnswers);
        autolowerAbdominalPains.setAdapter(adapterBooleanAnswers);

        autoexperiencePain.setAdapter(adapterBooleanAnswers);
        autohaveSexWithoutCondom.setAdapter(adapterBooleanAnswers);
        autohaveCondomBurst.setAdapter(adapterBooleanAnswers);
        autoabuseDrug.setAdapter(adapterBooleanAnswers);
        autobloodTransfusion.setAdapter(adapterBooleanAnswers);
        autoconsistentWeightFeverNightCough.setAdapter(adapterBooleanAnswers);
        autosoldPaidVaginalSex.setAdapter(adapterBooleanAnswers);
    }

    private void hideFieldsDefaultSelected(PreTest preTest) {
        if (StringUtils.changeBooleanToString(preTest.getKnowledgeAssessment().isPreviousTestedHIVNegative()).equals("Yes")) {
            clientInformPreventingsHivTransTIL.setVisibility(View.VISIBLE);
        } else {
            clientInformPreventingsHivTransTIL.setVisibility(View.GONE);
        }

        if (StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isSexPartnerHivPositive()).equals("Yes")) {
            sexPartnerRiskAssessLayout.setVisibility(View.VISIBLE);
        } else {
            sexPartnerRiskAssessLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideFieldsDefault(String patientId) {
        Person person = PersonDAO.findPersonById(patientId);
        if (person != null) {
            if (CodesetsDAO.findCodesetsDisplayById(person.getSexId()).equals("Female")) {
                syndromicSTIFemale.setVisibility(View.VISIBLE);
                syndromicSTIMale.setVisibility(View.GONE);
                ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId());
                if (clientIntake.getPregnant() != null) {
                    if (CodesetsDAO.findCodesetsDisplayById(clientIntake.getPregnant()).equals("Pregnant")) {
                        autoclientPregnant.setText("Yes", false);
                    }
                }
                clientPregnantTIL.setVisibility(View.VISIBLE);
            } else {
                syndromicSTIFemale.setVisibility(View.GONE);
                syndromicSTIMale.setVisibility(View.VISIBLE);
                clientPregnantTIL.setVisibility(View.GONE);
            }
        }

        ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, patientId);

        if (CodesetsDAO.findCodesetsDisplayByCode(clientIntake.getTargetGroup()).equals("Gen Pop")) {
            genPopHIVRiskAssessmentView.setVisibility(View.VISIBLE);
            othersHIVRiskAssessmentView.setVisibility(View.GONE);
        } else {
            genPopHIVRiskAssessmentView.setVisibility(View.GONE);
            othersHIVRiskAssessmentView.setVisibility(View.VISIBLE);
        }

    }

    private void dropDownClickListeners() {
        autoPreviousTestedHIVNegative.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ViewUtils.getInput(autoPreviousTestedHIVNegative).equals("Yes")) {
                    clientInformPreventingsHivTransTIL.setVisibility(View.VISIBLE);
                    autoTimeLastHIVNegativeTestResultTIL.setVisibility(View.VISIBLE);
                } else {
                    clientInformPreventingsHivTransTIL.setVisibility(View.GONE);
                    autoTimeLastHIVNegativeTestResultTIL.setVisibility(View.GONE);
                }
            }
        });

        autoSexPartnerHivPositive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ViewUtils.getInput(autoSexPartnerHivPositive).equals("Yes")) {
                    sexPartnerRiskAssessLayout.setVisibility(View.VISIBLE);
                } else {
                    sexPartnerRiskAssessLayout.setVisibility(View.GONE);
                }
            }
        });

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
            hideFieldsDefaultSelected(preTest);

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

            //HIV Risk Assessment Gen Pop
            autoeverHadSexualIntercourse.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isEverHadSexualIntercourse()), false);

            autobloodtransInlastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isBloodtransInlastThreeMonths()), false);

            autouprotectedSexWithCasualLastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isUprotectedSexWithCasualLastThreeMonths()), false);

            autouprotectedSexWithRegularPartnerLastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isUprotectedSexWithRegularPartnerLastThreeMonths()), false);

            autounprotectedVaginalSex.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isUnprotectedVaginalSex()), false);

            autoUprotectedAnalSexHivRiskAssess.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isUprotectedAnalSex()), false);

            autostiLastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isStiLastThreeMonths()), false);

            autosexUnderInfluence.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isSexUnderInfluence()), false);

            automoreThanOneSexPartnerLastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isMoreThanOneSexPartnerLastThreeMonths()), false);

            //HIV Risk Assessment Others
            autoexperiencePain.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isExperiencePain()), false);

            autohaveSexWithoutCondom.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isHaveSexWithoutCondom()), false);

            autohaveCondomBurst.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isHaveCondomBurst()), false);

            autoabuseDrug.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isAbuseDrug()), false);

            autobloodTransfusion.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isBloodTransfusion()), false);

            autoconsistentWeightFeverNightCough.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isConsistentWeightFeverNightCough()), false);

            autosoldPaidVaginalSex.setText(StringUtils.changeBooleanToString(preTest.getRiskAssessment().isSoldPaidVaginalSex()), false);


            //Sex Partner Risk Assessment
            autoSexPartnerHivPositive.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isSexPartnerHivPositive()), false);

            autoCurrentlyArvForPmtct.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isCurrentlyArvForPmtct()), false);

            autoNewDiagnosedHivlastThreeMonths.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isNewDiagnosedHivlastThreeMonths()), false);

            autoKnowHivPositiveOnArv.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isKnowHivPositiveOnArv()), false);

            autoKnowHivPositiveAfterLostToFollowUp.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isKnowHivPositiveAfterLostToFollowUp()), false);

            autoUprotectedAnalSex.setText(StringUtils.changeBooleanToString(preTest.getSexPartnerRiskAssessment().isUprotectedAnalSex()), false);

            //STI Screening

            autoComplaintsGenitalSore.setText(StringUtils.changeBooleanToString(preTest.getStiScreening().getComplaintsGenitalSore()), false);

            autoComplaintsOfScrotal.setText(StringUtils.changeBooleanToString(preTest.getStiScreening().getComplaintsOfScrotal()), false);

            autoUrethralDischarge.setText(StringUtils.changeBooleanToString(preTest.getStiScreening().getUrethralDischarge()), false);
            autovaginalDischarge.setText(StringUtils.changeBooleanToString(preTest.getStiScreening().getUrethralDischarge()), false);
            autolowerAbdominalPains.setText(StringUtils.changeBooleanToString(preTest.getStiScreening().getUrethralDischarge()), false);

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

//    @Override
//    public void retreiveClientFormData(String clientIntakeForm) {
//        ClientIntake clientIntake = new Gson().fromJson(clientIntakeForm, ClientIntake.class);
//        if (rst != null) {
//            if (rst.getTestingSetting() != null) {
//                autoSettings.setText(CodesetsDAO.findCodesetsDisplayByCode(rst.getTestingSetting()), false);
//            }
//
//            if (rst.getTargetGroup() != null) {
//                autoTargetGroup.setText(CodesetsDAO.findCodesetsDisplayByCode(rst.getTargetGroup()), false);
//            }
//
//            edVisitDate.setText(rst.getVisitDate());
//            edRegistrationDate.setText(rst.getVisitDate());
//            edDateofBirth.setText(rst.getDob());
//            edAge.setText(rst.getAge()+"");
//        }
//
//    }

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

        if (!ViewUtils.isEmpty(autoexperiencePain)) {
            riskAssessment.setExperiencePain(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoexperiencePain)));
        }

        if (!ViewUtils.isEmpty(autohaveSexWithoutCondom)) {
            riskAssessment.setHaveSexWithoutCondom(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autohaveSexWithoutCondom)));
        }

        if (!ViewUtils.isEmpty(autohaveCondomBurst)) {
            riskAssessment.setHaveCondomBurst(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autohaveCondomBurst)));
        }

        if (!ViewUtils.isEmpty(autoabuseDrug)) {
            riskAssessment.setAbuseDrug(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoabuseDrug)));
        }

        if (!ViewUtils.isEmpty(autobloodTransfusion)) {
            riskAssessment.setBloodTransfusion(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autobloodTransfusion)));
        }

        if (!ViewUtils.isEmpty(autoconsistentWeightFeverNightCough)) {
            riskAssessment.setConsistentWeightFeverNightCough(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autoconsistentWeightFeverNightCough)));
        }

        if (!ViewUtils.isEmpty(autosoldPaidVaginalSex)) {
            riskAssessment.setSoldPaidVaginalSex(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autosoldPaidVaginalSex)));
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

        if (!ViewUtils.isEmpty(autovaginalDischarge)) {
            stiScreening.setVaginalDischarge(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autovaginalDischarge)));
        }

        if (!ViewUtils.isEmpty(autolowerAbdominalPains)) {
            stiScreening.setLowerAbdominalPains(StringUtils.changeYesNoToTrueFalse(ViewUtils.getInput(autolowerAbdominalPains)));
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
            getActivity().finish();
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
        getActivity().finish();
    }

    @Override
    public void scrollToTop() {
        ScrollView scrollView = this.getActivity().findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, scrollView.getPaddingTop());
    }

    @Override
    public void setErrorsVisibilityGenPop(boolean everHadSexualIntercourse, boolean bloodtransInlastThreeMonths, boolean uprotectedSexWithCasualLastThreeMonths, boolean uprotectedSexWithRegularPartnerLastThreeMonths,
                                          boolean autounprotectedVaginalSex, boolean uprotectedAnalSexHivRiskAssess, boolean stiLastThreeMonths, boolean sexUnderInfluence, boolean moreThanOneSexPartnerLastThreeMonths) {
        if (everHadSexualIntercourse) {
            everHadSexualIntercourseTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (bloodtransInlastThreeMonths) {
            bloodtransInlastThreeMonthsTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (uprotectedSexWithCasualLastThreeMonths) {
            uprotectedSexWithCasualLastThreeMonthsTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (uprotectedSexWithRegularPartnerLastThreeMonths) {
            uprotectedSexWithRegularPartnerLastThreeMonthsTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (autounprotectedVaginalSex) {
            autounprotectedVaginalSexTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (uprotectedAnalSexHivRiskAssess) {
            uprotectedAnalSexHivRiskAssessTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (stiLastThreeMonths) {
            stiLastThreeMonthsTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (sexUnderInfluence) {
            sexUnderInfluenceTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (moreThanOneSexPartnerLastThreeMonths) {
            moreThanOneSexPartnerLastThreeMonthsTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }
    }

    @Override
    public void setErrorsVisibilityOthers(boolean experiencePain, boolean haveSexWithoutCondom, boolean haveCondomBurst, boolean abuseDrug, boolean bloodTransfusion, boolean consistentWeightFeverNightCough, boolean soldPaidVaginalSex) {
        if (experiencePain) {
            experiencePainTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (haveSexWithoutCondom) {
            haveSexWithoutCondomTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (haveCondomBurst) {
            haveCondomBurstTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (abuseDrug) {
            abuseDrugTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (bloodTransfusion) {
            autobloodTransfusionTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (consistentWeightFeverNightCough) {
            consistentWeightFeverNightCoughTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        if (soldPaidVaginalSex) {
            soldPaidVaginalSexTIL.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

    }


}
