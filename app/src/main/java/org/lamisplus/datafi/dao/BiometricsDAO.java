package org.lamisplus.datafi.dao;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.util.Log;

import net.sqlcipher.Cursor;

import org.lamisplus.datafi.models.Biometrics;
import org.lamisplus.datafi.utilities.FingerPositions;

import java.util.ArrayList;
import java.util.List;

public class BiometricsDAO {

    public void saveFingerPrint(List<Biometrics> pbs) {
        for (int i = 0; i < pbs.size(); i++) {

            if (pbs.get(i).getFingerPositions() != null) {
                //long id = new FingerPrintTable().insert(pbs.get(i));
                //Log.e(TAG, "return id: " + id);
            }
            else{
                Log.e(TAG, "skipping empty finger print: ");
            }
        }
    }

    public long saveFingerPrint(Biometrics pbs) {
        deletePrintPosition((long) pbs.getPatienId(), pbs.getFingerPositions());
        //long id =  new FingerPrintTable().insert(pbs);
       // Log.e(TAG, "return id: " +id);
        return 0;
    }

    public void deletePrintPosition(Long patientId, FingerPositions fingerPosition) {
        //new FingerPrintTable().deleteFingerPrintCapture(patientId, fingerPosition);
    }


    public void deleteAllPrints() {
//        DBOpenHelper openHelper = OpenMRSDBOpenHelper.getInstance().getDBOpenHelper();
//        openHelper.getWritableDatabase().execSQL(new FingerPrintTable().dropTableDefinition());
//        openHelper.getWritableDatabase().execSQL(new FingerPrintTable().createTableDefinition());
//        OpenMRS.getInstance().getOpenMRSLogger().d("All Finger Print deleted");
    }

    public void deletePrint(Long patientId) {
       // new FingerPrintTable().delete(patientId);
    }


    public  List<Biometrics> getAll(boolean IncludeSyncRecord, String patient_Id) {
        List<Biometrics> pbsList = new ArrayList<>();

        return pbsList;
    }

    public boolean checkIfFingerPrintUptoSixFingers(String patientId) {

        return false;
    }

    public int updatePatientFingerPrintSyncStatus(Long patientId, Biometrics tableObject) {
        return 0;
    }

}
