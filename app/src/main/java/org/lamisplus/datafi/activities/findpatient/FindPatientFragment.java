package org.lamisplus.datafi.activities.findpatient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskAssessment;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.FontsUtil;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FindPatientFragment extends LamisBaseFragment<FindPatientContract.Presenter> implements FindPatientContract.View {

    private TextView mEmptyList;
    private RecyclerView mFindPatientRecyclerView;

    //Initialization Progress bar
    private ProgressBar mProgressBar;

    private MenuItem mAddPatientMenuItem;

    private Button mCreatePatientButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find_patient, container, false);
        // Patient list config
        mFindPatientRecyclerView = root.findViewById(R.id.findPatientRecyclerView);
        mFindPatientRecyclerView.setHasFixedSize(true);
        mFindPatientRecyclerView.setAdapter(new FindPatientRecyclerViewAdapter(this,
                new ArrayList<>()));
        mFindPatientRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        mFindPatientRecyclerView.setLayoutManager(linearLayoutManager);

        mEmptyList = root.findViewById(R.id.emptySyncedPatientList);
        mProgressBar = root.findViewById(R.id.findPatientsInitialProgressBar);
        mCreatePatientButton = root.findViewById(R.id.createPatientButton);

        setListener();
        return root;
    }

    public static FindPatientFragment newInstance() {
        return new FindPatientFragment();
    }

    private void setListener(){
        mCreatePatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEditPatientActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateAdapter(List<Person> patientList) {
        FindPatientRecyclerViewAdapter adapter = new FindPatientRecyclerViewAdapter(this, patientList);
        adapter.notifyDataSetChanged();
        mFindPatientRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateListVisibility(boolean isVisible) {
        mProgressBar.setVisibility(View.GONE);
        mFindPatientRecyclerView.setVisibility(View.VISIBLE);
    }

    public void updateView(){
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                List<Person> personList = new Select().from(Person.class).orderBy("id DESC").execute();
                updateListVisibility(true);
                updateAdapter(personList);

            }
        });
//        Executor executor = Executors.newSingleThreadExecutor();
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<Person> personList = new Select().from(Person.class).execute();
//                updateListVisibility(true);
//                updateAdapter(personList);
//            }
//        });
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

}
