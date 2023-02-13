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

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.api.CustomApiCallback;
import org.lamisplus.datafi.api.repository.HTSRepository;
import org.lamisplus.datafi.api.repository.PatientRepository;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.List;
import java.util.ListIterator;

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

    private void countUnsyncedRSTAndSync() {
        List<Encounter> encounterRSTList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM);
        final ListIterator<Encounter> it = encounterRSTList.listIterator();
        if (encounterRSTList.size() > 0) {
            while (it.hasNext()) {
                Encounter encounter = it.next();
                Thread rstThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getRiskStratificationAndSync(encounter);
                    }
                });
                rstThread.start();
            }
        }
    }

    private void countUnsyncedClientIntakeAndSync() {
        List<Encounter> encounterRSTList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.CLIENT_INTAKE_FORM);
        final ListIterator<Encounter> it = encounterRSTList.listIterator();
        if (encounterRSTList.size() > 0) {
            while (it.hasNext()) {
                Encounter encounter = it.next();
                Thread rstThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getClientIntakeAndSync(encounter);
                    }
                });
                rstThread.start();
            }
        }
    }

    private void syncOtherHTSForms() {
        List<Encounter> encounterList = EncounterDAO.getUnsyncedEncounters();
        final ListIterator<Encounter> it = encounterList.listIterator();
        while (it.hasNext()) {
            Encounter encounter = it.next();
            if (encounter.getName().equals(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getPreTestAndSync(encounter);
                    }
                }).start();
            } else if (encounter.getName().equals(ApplicationConstants.Forms.REQUEST_RESULT_FORM)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getRequestResultAndSync(encounter);
                    }
                }).start();
            } else if (encounter.getName().equals(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getPostTestAndSync(encounter);
                    }
                }).start();
            } else if (encounter.getName().equals(ApplicationConstants.Forms.HIV_RECENCY_FORM)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getHivRecencyAndSync(encounter);
                    }
                }).start();
            } else if (encounter.getName().equals(ApplicationConstants.Forms.ELICITATION)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getIndexElicitationAndSync(encounter);
                    }
                }).start();
            }
        }
    }

    private void startPatientSyncProcess() {
        if (NetworkUtils.isOnline()) {
            List<Person> personList = new PersonDAO().getUnsyncedPatients();
            if (personList.size() > 0) {
                final ListIterator<Person> it = personList.listIterator();
                while (it.hasNext()) {
                    Person person = it.next();
                    getPatientAndSync(person);
                }
            } else if (EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM) > 0) {
                countUnsyncedRSTAndSync();
            } else if (EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.CLIENT_INTAKE_FORM) > 0) {
                countUnsyncedClientIntakeAndSync();
            } else {
                syncOtherHTSForms();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startPatientSyncProcess();
                }
            }).start();
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


}

//class SyncPatientClass extends AsyncTask<String, String, String> {
//
//    @Override
//    protected String doInBackground(String[] s) {
//        List<Person> personList = new PersonDAO().getUnsyncedPatients();
//        if (personList.size() > 0) {
//            final ListIterator<Person> it = personList.listIterator();
//            while (it.hasNext()) {
//                Person person = it.next();
//                getPatientAndSync(person);
//            }
//        } else if (EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM) > 0) {
//            countUnsyncedRSTAndSync();
//        } else if (EncounterDAO.countUnsyncedEncounters(ApplicationConstants.Forms.CLIENT_INTAKE_FORM) > 0) {
//            countUnsyncedClientIntakeAndSync();
//        } else {
//            syncOtherHTSForms();
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new SyncPatientClass().execute();
//            }
//        }).start();
////            Handler mHandler = new Handler(Looper.getMainLooper()) {
////                @Override
////                public void handleMessage(Message message) {
////                    ToastUtil.warning(getString(R.string.activity_no_internet_connection) +
////                            getString(R.string.activity_sync_after_connection));
////                }
////            };
//        return "";
//
//    }
//
//
//    private synchronized void getPatientAndSync(Person person) {
//        new PatientRepository().syncPatient(person, new DefaultCallbackListener() {
//            @Override
//            public void onResponse() {
//
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//
//            }
//        });
//    }
//
//    public synchronized void getRiskStratificationAndSync(Encounter encounter) {
//        new HTSRepository().syncRst(encounter, new DefaultCallbackListener() {
//            @Override
//            public void onResponse() {
//
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//
//            }
//        });
//    }
//
//    public synchronized void getClientIntakeAndSync(Encounter encounter) {
//        new HTSRepository().syncClientIntake(encounter, new DefaultCallbackListener() {
//            @Override
//            public void onResponse() {
//
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//
//            }
//        });
//    }
//
//    public synchronized void getPreTestAndSync(Encounter encounter) {
//        new HTSRepository().syncPreTest(encounter, new DefaultCallbackListener() {
//            @Override
//            public void onResponse() {
//
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//
//            }
//        });
//    }
//
//    public synchronized void getRequestResultAndSync(Encounter encounter) {
//        new HTSRepository().syncRequestResult(encounter, new DefaultCallbackListener() {
//            @Override
//            public void onResponse() {
//
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//
//            }
//        });
//    }
//
//    public synchronized void getPostTestAndSync(Encounter encounter) {
//        new HTSRepository().syncPostTest(encounter, new DefaultCallbackListener() {
//            @Override
//            public void onResponse() {
//
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//
//            }
//        });
//    }
//
//    public synchronized void getHivRecencyAndSync(Encounter encounter) {
//        new HTSRepository().syncRecency(encounter, new DefaultCallbackListener() {
//            @Override
//            public void onResponse() {
//
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//
//            }
//        });
//    }
//
//    public synchronized void getIndexElicitationAndSync(Encounter encounter) {
//        new HTSRepository().syncElicitation(encounter, new DefaultCallbackListener() {
//            @Override
//            public void onResponse() {
//
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//
//            }
//        });
//    }
//
//    //Count the number of unsynced patients and if they are more than 0 return true
//    private boolean countUnsyncedPatients() {
//        List<Person> personList = new PersonDAO().getUnsyncedPatients();
//        if (personList.size() > 0) {
//            return true;
//        }
//        return false;
//    }
//
//    private void countUnsyncedRSTAndSync() {
//        List<Encounter> encounterRSTList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM);
//        final ListIterator<Encounter> it = encounterRSTList.listIterator();
//        if (encounterRSTList.size() > 0) {
//            while (it.hasNext()) {
//                Encounter encounter = it.next();
//                Thread rstThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getRiskStratificationAndSync(encounter);
//                    }
//                });
//                rstThread.start();
//            }
//        }
//    }
//
//    private void countUnsyncedClientIntakeAndSync() {
//        List<Encounter> encounterRSTList = EncounterDAO.getUnsyncedEncounters(ApplicationConstants.Forms.CLIENT_INTAKE_FORM);
//        final ListIterator<Encounter> it = encounterRSTList.listIterator();
//        if (encounterRSTList.size() > 0) {
//            while (it.hasNext()) {
//                Encounter encounter = it.next();
//                Thread rstThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getClientIntakeAndSync(encounter);
//                    }
//                });
//                rstThread.start();
//            }
//        }
//    }
//
//    private void syncOtherHTSForms() {
//        List<Encounter> encounterList = EncounterDAO.getUnsyncedEncounters();
//        final ListIterator<Encounter> it = encounterList.listIterator();
//        while (it.hasNext()) {
//            Encounter encounter = it.next();
//            if (encounter.getName().equals(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM)) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getPreTestAndSync(encounter);
//                    }
//                }).start();
//            } else if (encounter.getName().equals(ApplicationConstants.Forms.REQUEST_RESULT_FORM)) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getRequestResultAndSync(encounter);
//                    }
//                }).start();
//            } else if (encounter.getName().equals(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM)) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getPostTestAndSync(encounter);
//                    }
//                }).start();
//            } else if (encounter.getName().equals(ApplicationConstants.Forms.HIV_RECENCY_FORM)) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getHivRecencyAndSync(encounter);
//                    }
//                }).start();
//            } else if (encounter.getName().equals(ApplicationConstants.Forms.ELICITATION)) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getIndexElicitationAndSync(encounter);
//                    }
//                }).start();
//            }
//        }
//    }
//
//}

