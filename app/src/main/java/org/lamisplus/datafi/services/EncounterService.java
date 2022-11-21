package org.lamisplus.datafi.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class EncounterService extends IntentService {

    public EncounterService(){
        super("Encounter Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
