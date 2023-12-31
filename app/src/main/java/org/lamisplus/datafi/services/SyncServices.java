package org.lamisplus.datafi.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.api.CustomApiCallback;
import org.lamisplus.datafi.api.repository.BiometricsRepository;
import org.lamisplus.datafi.api.repository.HTSRepository;
import org.lamisplus.datafi.api.repository.PMTCTRepository;
import org.lamisplus.datafi.api.repository.PatientRepository;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.Biometrics;
import org.lamisplus.datafi.models.BiometricsRecapture;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SyncServices extends IntentService {


    public SyncServices() {
        super("Sync Services");
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.startPatientSyncProcess();
    }

    private synchronized void getPatientAndSync(Person person) {
        new PatientRepository().syncPatient(person, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getRiskStratificationAndSync(Encounter encounter) {
        new HTSRepository().syncRst(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getClientIntakeAndSync(Encounter encounter) {
        new HTSRepository().syncClientIntake(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getPreTestAndSync(Encounter encounter) {
        new HTSRepository().syncPreTest(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getRequestResultAndSync(Encounter encounter) {
        new HTSRepository().syncRequestResult(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getPostTestAndSync(Encounter encounter) {
        new HTSRepository().syncPostTest(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getHivRecencyAndSync(Encounter encounter) {
        new HTSRepository().syncRecency(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getIndexElicitationAndSync(Encounter encounter) {
        new HTSRepository().syncElicitation(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    //Count the number of unsynced patients and if they are more than 0 return true
    private boolean countUnsyncedPatients() {
        List<Person> personList = new PersonDAO().getUnsyncedPatients();
        if (personList.size() > 0) {
            return true;
        }
        return false;
    }

    private boolean countUnsyncedEncounters() {
        List<Encounter> encounterList = EncounterDAO.getUnsyncedEncounters();
        if (encounterList.size() > 0) {
            return true;
        }
        return false;
    }

    private boolean countUnsyncedBiometrics() {
        List<Biometrics> biometricsList = BiometricsDAO.getUnsyncedBiometrics();
        if (biometricsList.size() > 0) {
            return true;
        }
        return false;
    }

    private void countUnsyncedRSTAndSync() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterRSTList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM);
                if (encounterRSTList.size() > 0) {
                    final ListIterator<Encounter> it = encounterRSTList.listIterator();
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getRiskStratificationAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }

    private void countUnsyncedClientIntakeAndSync() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterRSTList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.CLIENT_INTAKE_FORM);
                final ListIterator<Encounter> it = encounterRSTList.listIterator();
                if (encounterRSTList.size() > 0) {
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getClientIntakeAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }

    private void syncOtherHTSForms() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterList = EncounterDAO.getUnsyncedEncounters();
                final ListIterator<Encounter> it = encounterList.listIterator();

                while (it.hasNext()) {
                    Encounter encounter = it.next();
                    if (encounter.getName().equals(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM)) {
                        if (!encounter.isSynced()) {
                            getPreTestAndSync(encounter);
                        }
                    } else if (encounter.getName().equals(ApplicationConstants.Forms.REQUEST_RESULT_FORM)) {
                        if (!encounter.isSynced()) {
                            getRequestResultAndSync(encounter);
                        }
                    } else if (encounter.getName().equals(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM)) {
                        if (!encounter.isSynced()) {
                            getPostTestAndSync(encounter);
                        }
                    } else if (encounter.getName().equals(ApplicationConstants.Forms.HIV_RECENCY_FORM)) {
                        if (!encounter.isSynced()) {
                            getHivRecencyAndSync(encounter);
                        }
                    } else if (encounter.getName().equals(ApplicationConstants.Forms.ELICITATION)) {
                        if (!encounter.isSynced()) {
                            getIndexElicitationAndSync(encounter);
                        }
                    }
                }
            }
        }).start();
    }

    private void startPatientSyncProcess() {
        if (NetworkUtils.isOnline()) {
            List<Person> personList = new PersonDAO().getUnsyncedPatients();
            if (personList.size() > 0) {

                final ListIterator<Person> it = personList.listIterator();
                while (it.hasNext()) {
                    Person person = it.next();
                    if (!person.isSynced()) {
                        getPatientAndSync(person);
                    }
                }
            }

            if (personList.size() == 0) {
                if (BiometricsDAO.getUnsyncedBiometrics().size() > 0) {
                    syncBiometrics();
                } else if (BiometricsDAO.getUnsyncedBiometricsRecapture().size() > 0){
                    syncBiometricsRecapture();
                } else if (EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM) > 0) {
                    countUnsyncedRSTAndSync();
                } else if (EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM) == 0 && EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.CLIENT_INTAKE_FORM) > 0) {
                    countUnsyncedClientIntakeAndSync();
                } else {
                    syncANC();
                    syncPMTCTEnrollment();
                    syncLabourDelivery();
                    syncInfantRegistration();
                    syncPartnerInformation();
                    syncChildFollowUpVisit();
                    syncMotherFollowUpVisit();
                    if (EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM) == 0 && EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.CLIENT_INTAKE_FORM) == 0) {
                        syncOtherHTSForms();
                    }
                }
            }
            Thread startSyncAgain = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        startPatientSyncProcess();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            startSyncAgain.start();
        } else {
            Handler mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    ToastUtil.warning(getString(R.string.activity_no_internet_connection) +
                            getString(R.string.activity_sync_after_connection));
                }
            };

        }

    }


    private void syncBiometrics() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Biometrics> biometricsList = BiometricsDAO.getUnsyncedBiometrics();
                final ListIterator<Biometrics> it = biometricsList.listIterator();
                while (it.hasNext()) {
                    Biometrics biometrics = it.next();
                    if (biometrics.getSyncStatus() == 0) {
                        getBiometricsAndSync(biometrics);
                    }
                }
            }
        }).start();
    }

    private void syncBiometricsRecapture() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BiometricsRecapture> biometricsList = BiometricsDAO.getUnsyncedBiometricsRecapture();
                final ListIterator<BiometricsRecapture> it = biometricsList.listIterator();
                while (it.hasNext()) {
                    BiometricsRecapture biometrics = it.next();
                    if (biometrics.getSyncStatus() == 0) {
                        getBiometricsRecaptureAndSync(biometrics);
                    }
                }
            }
        }).start();
    }

    private synchronized void getBiometricsAndSync(Biometrics biometrics) {
        new BiometricsRepository().syncBiometrics(biometrics, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private synchronized void getBiometricsRecaptureAndSync(BiometricsRecapture biometricsRecapture) {
        new BiometricsRepository().syncBiometricsRecapture(biometricsRecapture, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private synchronized void getANCAndSync(Encounter encounter) {
        new PMTCTRepository().syncANC(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void syncANC() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterANCList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.ANC_FORM);
                final ListIterator<Encounter> it = encounterANCList.listIterator();
                if (encounterANCList.size() > 0) {
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getANCAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }


    private synchronized void getPMTCTEnrollmentAndSync(Encounter encounter) {
        new PMTCTRepository().syncPMTCTEnrollment(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void syncPMTCTEnrollment() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterPMTCTEnrollmentList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM);
                final ListIterator<Encounter> it = encounterPMTCTEnrollmentList.listIterator();
                if (encounterPMTCTEnrollmentList.size() > 0) {
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getPMTCTEnrollmentAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }


    private synchronized void getLabourDeliveryAndSync(Encounter encounter) {
        new PMTCTRepository().syncLabourDelivery(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void syncLabourDelivery() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterLabourDeliveryList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM);
                final ListIterator<Encounter> it = encounterLabourDeliveryList.listIterator();
                if (encounterLabourDeliveryList.size() > 0) {
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getLabourDeliveryAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }

    private synchronized void getInfantRegistrationAndSync(Encounter encounter) {
        new PMTCTRepository().syncInfantRegistration(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void syncInfantRegistration() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterInfantRegistrationList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.INFANT_INFORMATION_FORM);
                final ListIterator<Encounter> it = encounterInfantRegistrationList.listIterator();
                if (encounterInfantRegistrationList.size() > 0) {
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getInfantRegistrationAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }


    private synchronized void getPartnerInformationAndSync(Encounter encounter) {
        new PMTCTRepository().syncPartnerInformation(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void syncPartnerInformation() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterPartnerInformationList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.PARTNERS_FORM);
                final ListIterator<Encounter> it = encounterPartnerInformationList.listIterator();
                if (encounterPartnerInformationList.size() > 0) {
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getPartnerInformationAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }

    private synchronized void getChildFollowUpVisitAndSync(Encounter encounter) {
        new PMTCTRepository().syncChildFollowupVisit(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void syncChildFollowUpVisit() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterChildFollowUpList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.CHILD_FOLLOW_UP_VISIT_FORM);
                final ListIterator<Encounter> it = encounterChildFollowUpList.listIterator();
                if (encounterChildFollowUpList.size() > 0) {
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getChildFollowUpVisitAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }

    private synchronized void getMotherFollowUpVisitAndSync(Encounter encounter) {
        new PMTCTRepository().syncMotherFollowupVisit(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void syncMotherFollowUpVisit() {
        Thread rstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Encounter> encounterMotherFollowUpList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.MOTHER_FOLLOW_UP_VISIT_FORM);
                final ListIterator<Encounter> it = encounterMotherFollowUpList.listIterator();
                if (encounterMotherFollowUpList.size() > 0) {
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if (encounter != null && !encounter.isSynced()) {
                            getMotherFollowUpVisitAndSync(encounter);
                        }
                    }
                }
            }
        });
        rstThread.start();
    }

}

