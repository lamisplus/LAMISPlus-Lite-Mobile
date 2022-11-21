package org.lamisplus.datafi.dao;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.Gson;

import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.RiskStratification;

public class EncounterDAO {

    /**
     * This form searches thee form table if this patient already has particular form entered
     * @param formName The name of the form
     * @param patientId The patient id
     * @return Forms
     */
    public static Encounter findFormByPatient(String formName, String patientId) {
        Encounter encounter = new Select().from(Encounter.class).where("personId = ? AND name = ?", patientId, formName).executeSingle();
        return encounter;
    }

    /**
     * This function is useful for editing RiskStratification form. It searches for the form and when it finds it, it returns the form and maps it to the corresponding serialized class
     * @param formName The name of the form
     * @param patientId The patient id
     * @return RiskStratification
     */
    public static RiskStratification findRstFromForm(String formName, String patientId){
        Encounter encounter = new Select().from(Encounter.class).where("personId = ? AND name = ?", patientId, formName).executeSingle();
        if(encounter != null) {
            RiskStratification riskStratification = new Gson().fromJson(encounter.getDataValues(), RiskStratification.class);
            return riskStratification;
        }
        return null;
    }

    /**
     * This function is useful for editing ClientIntake form. It searches for the form and when it finds it, it returns the form and maps it to the corresponding serialized class
     * @param formName The name of the form
     * @param patientId The patient id
     * @return ClientIntake
     */
    public static ClientIntake findClientIntakeFromForm(String formName, String patientId){
        Encounter encounter = new Select().from(Encounter.class).where("personId = ? AND name = ?", patientId, formName).executeSingle();
        if(encounter != null) {
            ClientIntake clientIntake = new Gson().fromJson(encounter.getDataValues(), ClientIntake.class);
            return clientIntake;
        }
        return null;
    }

    public void deleteRSTForm(String formName, String patientId){
        new Delete().from(Encounter.class).where("personId = ? AND name = ?", patientId, formName).executeSingle();
    }

}
