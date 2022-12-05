package org.lamisplus.datafi.dao;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.google.gson.Gson;

import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Elicitation;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.PostTest;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.models.RequestResult;
import org.lamisplus.datafi.models.RiskStratification;

import java.util.List;

public class EncounterDAO {

    /**
     * This function searches thee form table for all forms entered for this patient
     * @param patientId The patient id
     * @return Forms
     */
    public static List<Encounter> findAllEncounterForPatient(String patientId){
        List<Encounter> encounter = new Select().from(Encounter.class).where("person = ?", patientId).execute();
        if(encounter != null) {
            return encounter;
        }
        return null;
    }
    /**
     * This form searches the form table if this patient already has particular form entered
     * @param formName The name of the form
     * @param patientId The patient id
     * @return Forms
     */
    public static Encounter findFormByPatient(String formName, String patientId) {
        Encounter encounter = new Select().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).executeSingle();
        return encounter;
    }

    /**
     * This function is useful for editing RiskStratification form. It searches for the form and when it finds it, it returns the form and maps it to the corresponding serialized class
     * @param formName The name of the form
     * @param patientId The patient id
     * @return RiskStratification
     */
    public static RiskStratification findRstFromForm(String formName, String patientId){
        Encounter encounter = new Select().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).executeSingle();
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
        Encounter encounter = new Select().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).executeSingle();
        if(encounter != null) {
            ClientIntake clientIntake = new Gson().fromJson(encounter.getDataValues(), ClientIntake.class);
            return clientIntake;
        }
        return null;
    }

    /**
     * This function is useful for editing PreTest form. It searches for the form and when it finds it, it returns the form and maps it to the corresponding serialized class
     * @param formName The name of the form
     * @param patientId The patient id
     * @return PreTest
     */
    public static PreTest findPreTestFromForm(String formName, String patientId){
        Encounter encounter = new Select().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).executeSingle();
        if(encounter != null) {
            PreTest preTest = new Gson().fromJson(encounter.getDataValues(), PreTest.class);
            return preTest;
        }
        return null;
    }

    /**
     * This function is useful for editing RequestResult form. It searches for the form and when it finds it, it returns the form and maps it to the corresponding serialized class
     * @param formName The name of the form
     * @param patientId The patient id
     * @return RequestResult
     */
    public static RequestResult findRequestResultFromForm(String formName, String patientId){
        Encounter encounter = new Select().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).executeSingle();

        if(encounter != null) {
            RequestResult requestResult = new Gson().fromJson(encounter.getDataValues(), RequestResult.class);
            return requestResult;
        }
        return null;
    }

    /**
     * This function is useful for editing PostTest form. It searches for the form and when it finds it, it returns the form and maps it to the corresponding serialized class
     * @param formName The name of the form
     * @param patientId The patient id
     * @return PostTest
     */
    public static PostTest findPostTestFromForm(String formName, String patientId){
        Encounter encounter = new Select().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).executeSingle();
        if(encounter != null) {
            PostTest postTest = new Gson().fromJson(encounter.getDataValues(), PostTest.class);
            return postTest;
        }
        return null;
    }

    /**
     * This function is useful for editing Recency form. It searches for the form and when it finds it, it returns the form and maps it to the corresponding serialized class
     * @param formName The name of the form
     * @param patientId The patient id
     * @return Recency
     */
    public static Recency findRecencyFromForm(String formName, String patientId){
        Encounter encounter = new Select().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).executeSingle();
        if(encounter != null) {
            Recency recency = new Gson().fromJson(encounter.getDataValues(), Recency.class);
            return recency;
        }
        return null;
    }

    /**
     * This function is useful for editing Elicitation form. It searches for the form and when it finds it, it returns the form and maps it to the corresponding serialized class
     * @param formName The name of the form
     * @param patientId The patient id
     * @return Recency
     */
    public static Elicitation findElicitationFromForm(String formName, String patientId){
        Encounter encounter = new Select().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).executeSingle();
        if(encounter != null) {
            Elicitation elicitation = new Gson().fromJson(encounter.getDataValues(), Elicitation.class);
            return elicitation;
        }
        return null;
    }

    public static void deleteEncounter(String formName, String patientId){
        new Delete().from(Encounter.class).where("person = ? AND name = ?", patientId, formName).execute();
    }

    public static void deleteAllEncounter(long patientId){
        new Delete().from(Encounter.class).where("person = ?", patientId).execute();
    }

    public static void updateAllEncountersWithPatientId(int idFromServer, String personId){
        new Update(Encounter.class)
                .set("personId = ?", idFromServer)
                .where("person = ?", personId)
                .execute();
    }

    public static List<Encounter> getUnsyncedEncounters(){
        List<Encounter> encounter = new Select().from(Encounter.class).where("synced=?", 0).execute();
        return encounter;
    }

}
