package org.lamisplus.datafi.activities.forms.hts.pretest;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PreTest;

public class PreTestContract {

    interface View extends LamisBaseView<Presenter> {
        void scrollToTop();

        void startActivityForRequestResultForm();

        void hideFieldsDefault(String patientId);

        void startDashboardActivity();

        void setErrorsVisibilityGenPop(boolean everHadSexualIntercourse, boolean bloodtransInlastThreeMonths, boolean uprotectedSexWithCasualLastThreeMonths, boolean uprotectedSexWithRegularPartnerLastThreeMonths,
                                       boolean autounprotectedVaginalSex, boolean uprotectedAnalSexHivRiskAssess, boolean stiLastThreeMonths, boolean sexUnderInfluence, boolean moreThanOneSexPartnerLastThreeMonths);

        void setErrorsVisibilityOthers(boolean experiencePain,
                                       boolean haveSexWithoutCondom,
                                       boolean haveCondomBurst,
                                       boolean abuseDrug,
                                       boolean bloodTransfusion,
                                       boolean consistentWeightFeverNightCough,
                                       boolean soldPaidVaginalSex);
    }

    interface Presenter extends LamisBasePresenterContract {

        String getPatientId();

        PreTest patientToUpdate(String formName, String patientId);

        void confirmCreate(PreTest preTest, String packageName);

        void confirmUpdate(PreTest preTest, Encounter encounter);

        void confirmDeleteEncounterPreTest(String formName, String patientId);
    }
}
