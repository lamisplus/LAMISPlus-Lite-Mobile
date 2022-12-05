package org.lamisplus.datafi.services;

import static java.lang.reflect.Modifier.TRANSIENT;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.api.CustomApiCallback;
import org.lamisplus.datafi.api.repository.PatientRepository;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.Address;
import org.lamisplus.datafi.models.Contact;
import org.lamisplus.datafi.models.ContactPoint;
import org.lamisplus.datafi.models.Patient;
import org.lamisplus.datafi.models.PatientIdentifier;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import kotlin.jvm.Transient;

public class PatientService extends IntentService implements CustomApiCallback {


    public PatientService() {
        super("Register Patient");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (NetworkUtils.isOnline()) {
            try {
                List<Person> personList = new PersonDAO().getUnsyncedPatients();
                final ListIterator<Person> it = personList.listIterator();
                while (it.hasNext()) {
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

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure() {

    }
}
