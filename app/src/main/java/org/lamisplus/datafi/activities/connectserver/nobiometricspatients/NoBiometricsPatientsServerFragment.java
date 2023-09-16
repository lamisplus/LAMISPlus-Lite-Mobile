package org.lamisplus.datafi.activities.connectserver.nobiometricspatients;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.ContactPointClass;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Address;
import org.lamisplus.datafi.models.PatientIdentifier;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoBiometricsPatientsServerFragment extends LamisBaseFragment<NoBiometricsPatientsServerContract.Presenter> implements NoBiometricsPatientsServerContract.View, View.OnClickListener, SearchView.OnQueryTextListener {

    private TextView mEmptyList;
    private RecyclerView mFindPatientRecyclerView;

    //Initialization Progress bar
    private ProgressBar mProgressBar;

    private MenuItem mAddPatientMenuItem;

    private Button downloadAllPatientButton;

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find_patient_with_no_biometrics_server, container, false);
        // Patient list config
        mFindPatientRecyclerView = root.findViewById(R.id.findPatientRecyclerView);
        mFindPatientRecyclerView.setHasFixedSize(true);
        mFindPatientRecyclerView.setAdapter(new NoBiometricsPatientsServerRecyclerViewAdapter(this,
                new ArrayList<>()));
        mFindPatientRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        mFindPatientRecyclerView.setLayoutManager(linearLayoutManager);

        mEmptyList = root.findViewById(R.id.emptySyncedPatientList);
        mProgressBar = root.findViewById(R.id.findPatientsInitialProgressBar);

        downloadAllPatientButton = root.findViewById(R.id.downloadAllPatientButton);

        searchView = (SearchView) root.findViewById(R.id.search_recipe);
        searchView.setOnQueryTextListener(this);

        setListener();
        return root;
    }

    public static NoBiometricsPatientsServerFragment newInstance() {
        return new NoBiometricsPatientsServerFragment();
    }

    private void setListener() {
        downloadAllPatientButton.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateAdapter(List<Person> patientList) {
        NoBiometricsPatientsServerRecyclerViewAdapter adapter = new NoBiometricsPatientsServerRecyclerViewAdapter(this, patientList);
        adapter.notifyDataSetChanged();
        mFindPatientRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateListVisibility(boolean isVisible) {
        mProgressBar.setVisibility(View.GONE);
        mFindPatientRecyclerView.setVisibility(View.VISIBLE);
    }

    public void updateView() {
        mProgressBar.setVisibility(View.VISIBLE);
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pullData();
            }
        });
    }

    @Override
    public void updateListVisibility(boolean isVisible, @NonNull String replacementWord) {
        mProgressBar.setVisibility(View.GONE);
        if (isVisible) {
            mFindPatientRecyclerView.setVisibility(View.VISIBLE);
            mEmptyList.setVisibility(View.GONE);
        } else {
            mFindPatientRecyclerView.setVisibility(View.GONE);
            mEmptyList.setVisibility(View.VISIBLE);
            mEmptyList.setText(getString(R.string.search_patient_no_results));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downloadAllPatientButton:
                ToastUtil.success("Downloading all patients");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadAll();
                    }
                }).start();
                break;
            default:

                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mProgressBar.setVisibility(View.VISIBLE);
        pullData();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void downloadAll(){
        if (NetworkUtils.isOnline()) {
            RestApi restApi = RestServiceBuilder.createService(RestApi.class);
            Call<Object> call = restApi.getAllPatientWithoutBiomentic(LamisPlus.getInstance().getFacilityId());
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        mProgressBar.setVisibility(View.GONE);
                        String values = new Gson().toJson(response.body());
                        LamisCustomHandler.showJson(response.body());
                        try {
                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                            Log.v("Baron", "Total number of positive is " + jsonArray.length());
                            List<Person> personList = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject objSections = jsonArray.getJSONObject(j);
                                //Log.v("Baron", objSections.getString("surname"));
                                Person person = new Person();
                                if (objSections.has("surname")) {
                                    person.setSurname(objSections.getString("surname"));
                                }

                                if (objSections.has("otherName")) {
                                    person.setOtherName(objSections.getString("otherName"));
                                }

                                if (objSections.has("firstName")) {
                                    person.setFirstName(objSections.getString("firstName"));
                                }

                                if (objSections.has("id")) {
                                    person.setPersonId(objSections.getInt("id"));
                                }

                                if (objSections.has("personUuid")) {
                                    person.setPersonUuId(objSections.getString("personUuid"));
                                }

                                if (objSections.has("dateOfBirth")) {
                                    person.setDateOfBirth(objSections.getString("dateOfBirth"));
                                }

                                if (objSections.has("isDobEstimated")) {
                                    person.setDateOfBirthEstimated(objSections.getBoolean("isDobEstimated"));
                                }

                                if (objSections.has("dateOfRegistration")) {
                                    person.setDateOfRegistration(objSections.getString("dateOfRegistration"));
                                }

                                if (objSections.has("sex")) {
                                    if (CodesetsDAO.findCodesetsIdByDisplay(objSections.getString("sex")) != null) {
                                        int sexId = CodesetsDAO.findCodesetsIdByDisplay(objSections.getString("sex"));
                                        person.setSexId(sexId);
                                        person.setGenderId(sexId);
                                    }
                                }

                                if (objSections.has("hospitalNumber")) {
                                    PatientIdentifier patientIdentifier = new PatientIdentifier();
                                    patientIdentifier.setAssignerId(1);
                                    patientIdentifier.setType("HospitalNumber");
                                    patientIdentifier.setValue(objSections.getString("hospitalNumber"));
                                    List<PatientIdentifier> patientIdentifierList = new ArrayList<>();
                                    patientIdentifierList.add(patientIdentifier);

                                    person.setIdentifierList(patientIdentifierList);
                                    person.setIdentifierList();
                                }


                                person.setSynced(true);
                                person.setFromServer(1);
                                //personList.add(person);
                                //Check if Person Id already exists on mobile before adding the user
                                if (objSections.has("id")) {
                                    Boolean personIdExists = PersonDAO.checkPersonIdExists(objSections.getString("id"));
                                    if(!personIdExists) {
                                        person.save();
                                    }
                                }
                            }
                            updateListVisibility(true);
                            updateAdapter(personList);
                        } catch (Exception e) {
                            LamisCustomFileHandler.writeLogToFile("No Biometrics Patient Error: " + e.getMessage());
                            mProgressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    LamisCustomFileHandler.writeLogToFile("Download Patients, Message: " + t.getMessage());
                }
            });
        } else {
            ToastUtil.error("No Internet Connection");
        }
    }

    public void pullData() {
        if (NetworkUtils.isOnline()) {
            RestApi restApi = RestServiceBuilder.createService(RestApi.class);
            Call<Object> call = restApi.getAllPatientWithoutBiomentic(LamisPlus.getInstance().getFacilityId());
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        mProgressBar.setVisibility(View.GONE);
                        String values = new Gson().toJson(response.body());
                        LamisCustomHandler.showJson(response.body());
                        try {
                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));

                            List<Person> personList = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject objSections = jsonArray.getJSONObject(j);
                                //Log.v("Baron", objSections.getString("surname"));
                                Person person = new Person();
                                if (objSections.has("surname")) {
                                    person.setSurname(objSections.getString("surname"));
                                }

                                if (objSections.has("otherName")) {
                                    person.setOtherName(objSections.getString("otherName"));
                                }

                                if (objSections.has("firstName")) {
                                    person.setFirstName(objSections.getString("firstName"));
                                }

                                if (objSections.has("id")) {
                                    person.setPersonId(objSections.getInt("id"));
                                }

                                if (objSections.has("personUuid")) {
                                    person.setPersonUuId(objSections.getString("personUuid"));
                                }

                                if (objSections.has("dateOfBirth")) {
                                    person.setDateOfBirth(objSections.getString("dateOfBirth"));
                                }

                                if (objSections.has("isDobEstimated")) {
                                    person.setDateOfBirthEstimated(objSections.getBoolean("isDobEstimated"));
                                }

                                if (objSections.has("dateOfRegistration")) {
                                    person.setDateOfRegistration(objSections.getString("dateOfRegistration"));
                                }

                                if (objSections.has("sex")) {
                                    if (CodesetsDAO.findCodesetsIdByDisplay(objSections.getString("sex")) != null) {
                                        int sexId = CodesetsDAO.findCodesetsIdByDisplay(objSections.getString("sex"));
                                        person.setSexId(sexId);
                                        person.setGenderId(sexId);
                                    }
                                }

                                if (objSections.has("hospitalNumber")) {
                                    PatientIdentifier patientIdentifier = new PatientIdentifier();
                                    patientIdentifier.setAssignerId(1);
                                    patientIdentifier.setType("HospitalNumber");
                                    patientIdentifier.setValue(objSections.getString("hospitalNumber"));
                                    List<PatientIdentifier> patientIdentifierList = new ArrayList<>();
                                    patientIdentifierList.add(patientIdentifier);

                                    person.setIdentifierList(patientIdentifierList);
                                    person.setIdentifierList();
                                }


                                person.setSynced(true);
                                person.setFromServer(1);
                                //personList.add(person);
                                //Check if Person Id already exists on mobile before adding the user
                                if (objSections.has("id")) {
                                    Boolean personIdExists = PersonDAO.checkPersonIdExists(objSections.getString("id"));
                                    if(!personIdExists) {
                                        personList.add(person);
                                    }
                                }
                            }
                            updateListVisibility(true);
                            updateAdapter(personList);
                        } catch (Exception e) {
                            LamisCustomFileHandler.writeLogToFile("No Biometrics Patient Error: " + e.getMessage());
                            mProgressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    LamisCustomFileHandler.writeLogToFile("Download Patients, Message: " + t.getMessage());
                }
            });
        } else {
            ToastUtil.error("No Internet Connection");
        }
    }

}
