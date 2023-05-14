package org.lamisplus.datafi.activities.connectserver.findpatientserver;

import android.content.Intent;
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

import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.ContactPointClass;
import org.lamisplus.datafi.models.Address;
import org.lamisplus.datafi.models.PatientIdentifier;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPatientServerFragment extends LamisBaseFragment<FindPatientServerContract.Presenter> implements FindPatientServerContract.View, View.OnClickListener, SearchView.OnQueryTextListener {

    private TextView mEmptyList;
    private RecyclerView mFindPatientRecyclerView;

    //Initialization Progress bar
    private ProgressBar mProgressBar;

    private MenuItem mAddPatientMenuItem;

    private Button mCreatePatientButton;

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find_patient_server, container, false);
        // Patient list config
        mFindPatientRecyclerView = root.findViewById(R.id.findPatientRecyclerView);
        mFindPatientRecyclerView.setHasFixedSize(true);
        mFindPatientRecyclerView.setAdapter(new FindPatientServerRecyclerViewAdapter(this,
                new ArrayList<>()));
        mFindPatientRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        mFindPatientRecyclerView.setLayoutManager(linearLayoutManager);

        mEmptyList = root.findViewById(R.id.emptySyncedPatientList);
        mProgressBar = root.findViewById(R.id.findPatientsInitialProgressBar);

        searchView = (SearchView) root.findViewById(R.id.search_recipe);
        searchView.setOnQueryTextListener(this);

        setListener();
        return root;
    }

    public static FindPatientServerFragment newInstance() {
        return new FindPatientServerFragment();
    }

    private void setListener() {
//        mCreatePatientButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), AddEditPatientActivity.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateAdapter(List<Person> patientList) {
        FindPatientServerRecyclerViewAdapter adapter = new FindPatientServerRecyclerViewAdapter(this, patientList);
        adapter.notifyDataSetChanged();
        mFindPatientRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateListVisibility(boolean isVisible) {
        mProgressBar.setVisibility(View.GONE);
        mFindPatientRecyclerView.setVisibility(View.VISIBLE);
    }

    public void updateView() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

//                List<Person> personList = new Select().from(Person.class).orderBy("id DESC").execute();
//                updateListVisibility(false);
//                updateAdapter(personList);

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

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mProgressBar.setVisibility(View.VISIBLE);
        pullData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void pullData(String query) {
        if (NetworkUtils.isOnline()) {
            RestApi restApi = RestServiceBuilder.createService(RestApi.class);
            Call<Object> call = restApi.getPatients(query, 0, 100);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        mProgressBar.setVisibility(View.GONE);
                        String values = new Gson().toJson(response.body());
                        Log.v("Baron", values);
                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            String records = jsonObject.getString("records");
                            Log.v("Baron", records);

                            JSONArray jsonArray = jsonObject.getJSONArray("records");
                            List<Person> personList = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject objSections = jsonArray.getJSONObject(j);
                                Person person = new Person();
                                if (objSections.has("firstName")) {
                                    person.setFirstName(objSections.getString("firstName"));
                                }
                                if (objSections.has("otherName")) {
                                    person.setOtherName(objSections.getString("otherName"));
                                }
                                if (objSections.has("surname")) {
                                    person.setSurname(objSections.getString("surname"));
                                }
                                if (objSections.has("dateOfRegistration")) {
                                    person.setDateOfRegistration(objSections.getString("dateOfRegistration"));
                                }
                                if (objSections.has("isDateOfBirthEstimated")) {
                                    person.setDateOfBirthEstimated(Boolean.getBoolean(objSections.getString("isDateOfBirthEstimated")));
                                }
                                if (objSections.has("deceased")) {
                                    person.setDeceased(Boolean.getBoolean(objSections.getString("deceased")));
                                }

                                if (objSections.has("gender")) {
                                    JSONObject jsonObjectGender = new JSONObject(objSections.getString("gender"));
                                    Integer genderId = Integer.valueOf(jsonObjectGender.getString("id"));
                                    person.setGenderId(genderId);
                                }

                                if (objSections.has("id")) {
                                    person.setPersonId(Integer.valueOf(objSections.getInt("id")));
                                }

                                if (objSections.has("dateOfBirth")) {
                                    person.setDateOfBirth(objSections.getString("dateOfBirth"));
                                }

                                if (objSections.has("maritalStatus")) {
                                    JSONObject jsonObjectMaritalStatus = new JSONObject(objSections.getString("maritalStatus"));
                                    Integer maritalStatusId = Integer.valueOf(jsonObjectMaritalStatus.getString("id"));
                                    person.setMaritalStatusId(maritalStatusId);
                                }

                                if (objSections.has("education")) {
                                    JSONObject jsonObjectEducation = new JSONObject(objSections.getString("education"));
                                    Integer educationId = Integer.valueOf(jsonObjectEducation.getString("id"));
                                    if (educationId != null) {
                                        person.setEducationId(educationId);
                                    }
                                }

                                if (objSections.has("organization")) {
                                    JSONObject jsonObjectOrganization = new JSONObject(objSections.getString("organization"));
                                    Integer organizationId = Integer.valueOf(jsonObjectOrganization.getString("id"));
                                    person.setOrganizationId(organizationId);
                                }

                                //Address field
                                if (objSections.has("address")) {
                                    JSONObject jsonObjectAddress = new JSONObject(objSections.getString("address"));
                                    JSONArray jsonArrayAddress = jsonObjectAddress.getJSONArray("address");
                                    Address address = new Address();
                                    if (jsonArrayAddress.getJSONObject(0).has("city")) {
                                        address.setCity(jsonArrayAddress.getJSONObject(0).getString("city"));
                                    }
                                    if (jsonArrayAddress.getJSONObject(0).has("stateId")) {
                                        address.setStateId(jsonArrayAddress.getJSONObject(0).getInt("stateId"));
                                    }
                                    if (jsonArrayAddress.getJSONObject(0).has("district")) {
                                        address.setDistrict(jsonArrayAddress.getJSONObject(0).getString("district"));
                                    }
                                    //String[] line = new String[]{jsonArrayAddress.getJSONObject(0).getString("line")};
                                    if (jsonArrayAddress.getJSONObject(0).has("line")) {
                                        address.setLine(new String[]{jsonArrayAddress.getJSONObject(0).getString("line")});
                                    }
                                    if (jsonArrayAddress.getJSONObject(0).has("postalCode")) {
                                        address.setPostalCode(jsonArrayAddress.getJSONObject(0).getString("postalCode"));
                                    }
                                    List<Address> addressList = new ArrayList<>();
                                    addressList.add(address);
                                    person.setAddress(addressList);
                                    person.setAddressList();
                                }


                                //Identifier field
                                if (objSections.has("identifier")) {
                                    JSONObject jsonObjectIdentifier = new JSONObject(objSections.getString("identifier"));
                                    JSONArray jsonArrayIdentifier = jsonObjectIdentifier.getJSONArray("identifier");

                                    PatientIdentifier patientIdentifier = new PatientIdentifier();
                                    patientIdentifier.setAssignerId(jsonArrayIdentifier.getJSONObject(0).getInt("assignerId"));
                                    patientIdentifier.setType(jsonArrayIdentifier.getJSONObject(0).getString("type"));
                                    patientIdentifier.setValue(jsonArrayIdentifier.getJSONObject(0).getString("value"));
                                    List<PatientIdentifier> patientIdentifierList = new ArrayList<>();
                                    patientIdentifierList.add(patientIdentifier);

                                    person.setIdentifierList(patientIdentifierList);
                                    person.setIdentifierList();
                                }

                                //Contact
                                if (objSections.has("contactPoint")) {
                                    JSONObject jsonObjectContactPoint = new JSONObject(objSections.getString("contactPoint"));
                                    JSONArray jsonArrayContactPoint = jsonObjectContactPoint.getJSONArray("contactPoint");

                                    List<ContactPointClass.ContactPointItems> contactPointItems = new ArrayList<>();
                                    for (int x = 0; x < jsonArrayContactPoint.length(); x++) {
                                        JSONObject objSectionsContact = jsonArrayContactPoint.getJSONObject(x);
                                        LamisCustomHandler.showJson(objSectionsContact.getString("type"));
                                        if (objSectionsContact.getString("type").equals("phone")) {
                                            if (objSectionsContact.has("value")) {
                                                contactPointItems.add(new ContactPointClass.ContactPointItems("phone", objSectionsContact.getString("value")));
                                            }
                                        }

                                        if (objSectionsContact.getString("type").equals("email")) {
                                            if (objSectionsContact.has("value")) {
                                                contactPointItems.add(new ContactPointClass.ContactPointItems("email", objSectionsContact.getString("value")));
                                            }
                                        }

                                        if (objSectionsContact.getString("type").equals("altphone")) {
                                            if (objSectionsContact.has("value")) {
                                                contactPointItems.add(new ContactPointClass.ContactPointItems("altphone", objSectionsContact.getString("value")));
                                            }
                                        }
                                    }
                                    String json = new Gson().toJson(contactPointItems);
                                    person.setContactPoint(json);
                                }
                                person.setSynced(true);
                                person.setFromServer(1);
                                personList.add(person);

//                                String surname = objSections.getString("surname");
//                                Log.v("Baron", "The surname is " + surname);
                            }
                            updateListVisibility(true);
                            updateAdapter(personList);
                        } catch (Exception e) {
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
