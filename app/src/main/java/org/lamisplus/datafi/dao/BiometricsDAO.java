package org.lamisplus.datafi.dao;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sqlcipher.Cursor;

import org.lamisplus.datafi.models.Biometrics;
import org.lamisplus.datafi.models.BiometricsList;
import org.lamisplus.datafi.models.BiometricsRecapture;
import org.lamisplus.datafi.models.ContactPoint;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.FingerPositions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BiometricsDAO {

    public static void updateAllBiometricsWithPatiendId(int idFromServer, String personId) {
        new Update(Biometrics.class)
                .set("patientId = ?", idFromServer)
                .where("person = ?", personId)
                .execute();
    }

    /**
     * This function returns the patientId from the server if it already exists else set as null
     *
     * @param personId This is the personId from the person Table
     * @return person id integer
     */
    public static Integer getPatientId(Integer personId) {
        if(personId != null) {
            Person person = new Select().from(Person.class).where("id = ?", personId).executeSingle();
            if (person != null) {
                return person.getPersonId();
            }
        }
        return null;
    }



    public static void deletePrint(Integer personId) {
        new Delete().from(Biometrics.class).where("person = ?", personId).execute();
    }

    public static List<BiometricsList> getFingerPrints(Integer personId) {
        Biometrics biometrics = new Select().from(Biometrics.class).where("person = ?", personId).executeSingle();
        if (biometrics != null) {
            Type type = new TypeToken<List<BiometricsList>>() {
            }.getType();
            List<BiometricsList> biometricsLists = new Gson().fromJson(biometrics.getCapturedBiometricsList(), type);
            return biometricsLists;
        }
        return null;
    }

    public static List<BiometricsList> getFingerPrintsRecapture(Integer personId) {
        BiometricsRecapture biometrics = new Select().from(BiometricsRecapture.class).where("person = ?", personId).executeSingle();
        if (biometrics != null) {
            Type type = new TypeToken<List<BiometricsList>>() {
            }.getType();
            List<BiometricsList> biometricsLists = new Gson().fromJson(biometrics.getCapturedBiometricsList(), type);
            return biometricsLists;
        }
        return null;
    }

    public static Biometrics getFingerPrintsForUser(Integer personId) {
        Biometrics biometrics = new Select().from(Biometrics.class).where("person = ?", personId).executeSingle();
        if (biometrics != null) {
            return biometrics;
        }
        return null;
    }

    public static BiometricsRecapture getFingerPrintsForUserRecapture(Integer personId) {
        BiometricsRecapture biometrics = new Select().from(BiometricsRecapture.class).where("person = ?", personId).executeSingle();
        if (biometrics != null) {
            return biometrics;
        }
        return null;
    }

    public static Integer syncStatus(Integer personId) {
        Biometrics biometrics = new Select().from(Biometrics.class).where("person = ?", personId).executeSingle();
        if (biometrics != null) {
            return biometrics.getSyncStatus();
        }
        return null;
    }

    public static List<Biometrics> getUnsyncedBiometrics() {
        List<Biometrics> biometrics = new Select().from(Biometrics.class).where("syncStatus = ?", 0).execute();
        if (biometrics != null) {
            return biometrics;
        }
        return null;
    }

    public static List<BiometricsRecapture> getUnsyncedBiometricsRecapture() {
        List<BiometricsRecapture> biometrics = new Select().from(BiometricsRecapture.class).where("syncStatus = ?", 0).execute();
        if (biometrics != null) {
            return biometrics;
        }
        return null;
    }


}
