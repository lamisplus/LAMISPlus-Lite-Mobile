package org.lamisplus.datafi.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.api.CustomApiCallback;
import org.lamisplus.datafi.api.repository.HTSRepository;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.List;
import java.util.ListIterator;

public class EncounterService extends IntentService implements CustomApiCallback {

    public EncounterService(){
        super("Encounter Service");
    }

    @Override
    protected synchronized void onHandleIntent(@Nullable Intent intent) {
        if (NetworkUtils.isOnline()) {
                List<Encounter> encounterList = EncounterDAO.getUnsyncedEncounters();
                final ListIterator<Encounter> it = encounterList.listIterator();
                while (it.hasNext()) {
                    Encounter encounter = it.next();
                    if(encounter.getName().equals(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM)){
                        getRiskStratificationAndSync(encounter, this);
                    }else if(encounter.getName().equals(ApplicationConstants.Forms.CLIENT_INTAKE_FORM)){
                        getClientIntakeAndSync(encounter, this);
                    }else if(encounter.getName().equals(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM)){
                        getPreTestAndSync(encounter, this);
                    }else if(encounter.getName().equals(ApplicationConstants.Forms.REQUEST_RESULT_FORM)){
                        getRequestResultAndSync(encounter, this);
                    }else if(encounter.getName().equals(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM)){
                        getPostTestAndSync(encounter, this);
                    }else if(encounter.getName().equals(ApplicationConstants.Forms.HIV_RECENCY_FORM)){
                        getHivRecencyAndSync(encounter, this);
                    } else if (encounter.getName().equals(ApplicationConstants.Forms.ELICITATION)) {
                        getIndexElicitationAndSync(encounter, this);
                    }
                }

        } else {
            ToastUtil.warning(getString(R.string.activity_no_internet_connection) +
                    getString(R.string.activity_sync_after_connection));
        }
    }

    public synchronized void getRiskStratificationAndSync(Encounter encounter, CustomApiCallback customApiCallback){
        new HTSRepository().syncRst(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {
                customApiCallback.onSuccess();
            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getClientIntakeAndSync(Encounter encounter, CustomApiCallback customApiCallback){
        new HTSRepository().syncClientIntake(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getPreTestAndSync(Encounter encounter, CustomApiCallback customApiCallback){
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

    public synchronized void getRequestResultAndSync(Encounter encounter, CustomApiCallback customApiCallback){
        new HTSRepository().syncRequestResult(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getPostTestAndSync(Encounter encounter, CustomApiCallback customApiCallback){
        new HTSRepository().syncPostTest(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getHivRecencyAndSync(Encounter encounter, CustomApiCallback customApiCallback){
        new HTSRepository().syncRecency(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    public synchronized void getIndexElicitationAndSync(Encounter encounter, CustomApiCallback customApiCallback){
        new HTSRepository().syncElicitation(encounter, new DefaultCallbackListener() {
            @Override
            public void onResponse() {

            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure() {

    }
}
