package org.lamisplus.datafi.activities.pmtct.pmtctprogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.forms.pmtct.anc.ANCActivity;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class PMTCTProgramFragment extends LamisBaseFragment<PMTCTProgramContract.Presenter> implements PMTCTProgramContract.View {

    private TextView mEmptyList;
    private RecyclerView mPMTCTProgramRecyclerView;

    //Initialization Progress bar
    private ProgressBar mProgressBar;

    private MenuItem mAddPatientMenuItem;

    private Button mCreatePMTCTButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pmtct_program, container, false);
        // Patient list config
        mPMTCTProgramRecyclerView = root.findViewById(R.id.pmtctProgramRecyclerView);
        mPMTCTProgramRecyclerView.setHasFixedSize(true);
        mPMTCTProgramRecyclerView.setAdapter(new PMTCTProgramRecyclerViewAdapter(this,
                new ArrayList<>()));
        mPMTCTProgramRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        mPMTCTProgramRecyclerView.setLayoutManager(linearLayoutManager);

        mEmptyList = root.findViewById(R.id.emptyPMTCTPatientList);
        mProgressBar = root.findViewById(R.id.pmtctProgramInitialProgressBar);
        mCreatePMTCTButton = root.findViewById(R.id.createPMTCTButton);

        setListener();
        return root;
    }

    public static PMTCTProgramFragment newInstance() {
        return new PMTCTProgramFragment();
    }

    private void setListener(){
        mCreatePMTCTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ANCActivity.class);
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
        PMTCTProgramRecyclerViewAdapter adapter = new PMTCTProgramRecyclerViewAdapter(this, patientList);
        adapter.notifyDataSetChanged();
        mPMTCTProgramRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateListVisibility(boolean isVisible) {
        mProgressBar.setVisibility(View.GONE);
        mPMTCTProgramRecyclerView.setVisibility(View.VISIBLE);
    }

    public void updateView(){
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                List<Person> eligiblePmtct = new ArrayList<>();
                List<Person> personList = new Select().from(Person.class).where("sexId = 377").orderBy("id DESC").execute();
                updateListVisibility(true);
                //This section loops through all the female patients and filters those greater than 10 years old
                for(Person p : personList){
                    if(DateUtils.getAgeFromBirthdateString(p.getDateOfBirth()) >= 10){
                        eligiblePmtct.add(p);
                    }
                }
                updateAdapter(eligiblePmtct);

            }
        });
    }

    @Override
    public void updateListVisibility(boolean isVisible, @NonNull String replacementWord) {
        mProgressBar.setVisibility(View.GONE);
        if (isVisible) {
            mPMTCTProgramRecyclerView.setVisibility(View.VISIBLE);
            mEmptyList.setVisibility(View.GONE);
        } else {
            mPMTCTProgramRecyclerView.setVisibility(View.GONE);
            mEmptyList.setVisibility(View.VISIBLE);
            mEmptyList.setText(getString(R.string.search_patient_no_results));
        }
    }

}
