package org.lamisplus.datafi.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.api.CustomApiCallback;
import org.lamisplus.datafi.api.repository.HTSRepository;
import org.lamisplus.datafi.api.repository.PatientRepository;
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

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Thread patientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtils.isOnline()) {
                    try {
                        List<Person> personList = new PersonDAO().getUnsyncedPatients();
                        final ListIterator<Person> it = personList.listIterator();
                        while (it.hasNext()) {
                            Log.v("Baron", "I am called first");
                            Person person = it.next();
                            getPatientAndSync(person);
                        }
                    } catch (Exception e) {
                        Log.v("Baron", "Baron Exception " +  e.toString());
                    }
                } else {
                    ToastUtil.warning(getString(R.string.activity_no_internet_connection) +
                            getString(R.string.activity_sync_after_connection));
                }
            }
        });

        Thread encounterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtils.isOnline()) {
                    Thread rstThread = null;
                    Thread clientIntakeThread = null;
                    List<Encounter> encounterList = EncounterDAO.getUnsyncedEncounters();
                    final ListIterator<Encounter> it = encounterList.listIterator();
                    while (it.hasNext()) {
                        Encounter encounter = it.next();
                        if(encounter.getName().equals(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM)){
                             rstThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.v("Baron", "I am called second");
                                    getRiskStratificationAndSync(encounter);
                                }
                            });
                            rstThread.start();
                        }else if(encounter.getName().equals(ApplicationConstants.Forms.CLIENT_INTAKE_FORM)){
                            try {
                                if(rstThread != null) {
                                    rstThread.join();
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            clientIntakeThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.v("Baron", "I am called third");
                                    getClientIntakeAndSync(encounter);
                                }
                            });
                            clientIntakeThread.start();
                        }else if(encounter.getName().equals(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM)){
                            try {
                                if(rstThread != null) {
                                    rstThread.join();
                                }
                                if(clientIntakeThread != null) {
                                    clientIntakeThread.join();
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getPreTestAndSync(encounter);
                                }
                            }).start();
                        }else if(encounter.getName().equals(ApplicationConstants.Forms.REQUEST_RESULT_FORM)){
                            try {
                                if(rstThread != null) {
                                    rstThread.join();
                                }
                                if(clientIntakeThread != null) {
                                    clientIntakeThread.join();
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getRequestResultAndSync(encounter);
                                }
                            }).start();
                        }else if(encounter.getName().equals(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM)){
                            try {
                                if(rstThread != null) {
                                    rstThread.join();
                                }
                                if(clientIntakeThread != null) {
                                    clientIntakeThread.join();
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getPostTestAndSync(encounter);
                                }
                            }).start();
                        }else if(encounter.getName().equals(ApplicationConstants.Forms.HIV_RECENCY_FORM)){
                            try {
                                if(rstThread != null) {
                                    rstThread.join();
                                }
                                if(clientIntakeThread != null) {
                                    clientIntakeThread.join();
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getHivRecencyAndSync(encounter);
                                }
                            }).start();
                        }else if(encounter.getName().equals(ApplicationConstants.Forms.ELICITATION)){
                            try {
                                if(rstThread != null) {
                                    rstThread.join();
                                }
                                if(clientIntakeThread != null) {
                                    clientIntakeThread.join();
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getIndexElicitationAndSync(encounter);
                                }
                            }).start();
                        }
                    }

                } else {
                    ToastUtil.warning(getString(R.string.activity_no_internet_connection) +
                            getString(R.string.activity_sync_after_connection));
                }
            }
        });

        patientThread.start();

        try {
            patientThread.join(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        encounterThread.start();

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

    public synchronized void getRiskStratificationAndSync(Encounter encounter){
        new HTSRepository().syncRst(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getClientIntakeAndSync(Encounter encounter){
        new HTSRepository().syncClientIntake(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getPreTestAndSync(Encounter encounter){
        new HTSRepository().syncPreTest(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {
                Log.v("Baron", "Seems i am finished");
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                Log.v("Baron", "Seems i am failed");
            }
        });
    }

    public synchronized void getRequestResultAndSync(Encounter encounter){
        new HTSRepository().syncRequestResult(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getPostTestAndSync(Encounter encounter){
        new HTSRepository().syncPostTest(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getHivRecencyAndSync(Encounter encounter){
        new HTSRepository().syncRecency(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getIndexElicitationAndSync(Encounter encounter){
        new HTSRepository().syncElicitation(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

}