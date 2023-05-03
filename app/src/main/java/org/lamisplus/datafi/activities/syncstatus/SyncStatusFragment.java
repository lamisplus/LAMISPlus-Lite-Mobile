package org.lamisplus.datafi.activities.syncstatus;

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
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.models.Person;

import java.util.ArrayList;
import java.util.List;

public class SyncStatusFragment extends LamisBaseFragment<SyncStatusContract.Presenter> implements SyncStatusContract.View {

    private TextView mEmptyList;
    private RecyclerView mSyncStatusRecyclerView;

    //Initialization Progress bar
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sync_status, container, false);
        // Patient list config
        mSyncStatusRecyclerView = root.findViewById(R.id.syncStatusRecyclerView);
        mSyncStatusRecyclerView.setHasFixedSize(true);
        mSyncStatusRecyclerView.setAdapter(new SyncStatusRecyclerViewAdapter(this,
                new ArrayList<>()));
        mSyncStatusRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        mSyncStatusRecyclerView.setLayoutManager(linearLayoutManager);

        mEmptyList = root.findViewById(R.id.emptySyncedPatientList);
        mProgressBar = root.findViewById(R.id.findPatientsInitialProgressBar);
        return root;
    }

    public static SyncStatusFragment newInstance() {
        return new SyncStatusFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateAdapter(List<Person> patientList) {
        SyncStatusRecyclerViewAdapter adapter = new SyncStatusRecyclerViewAdapter(this, patientList);
        adapter.notifyDataSetChanged();
        mSyncStatusRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateListVisibility(boolean isVisible) {
        mProgressBar.setVisibility(View.GONE);
        mSyncStatusRecyclerView.setVisibility(View.VISIBLE);
    }

    public void updateView(){
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                List<Person> personList = new Select().from(Person.class).where("synced = ? AND fromServer = ?", 0, 0).orderBy("id DESC").execute();
                updateListVisibility(true);
                updateAdapter(personList);

            }
        });
    }

    @Override
    public void updateListVisibility(boolean isVisible, @NonNull String replacementWord) {
        mProgressBar.setVisibility(View.GONE);
        if (isVisible) {
            mSyncStatusRecyclerView.setVisibility(View.VISIBLE);
            mEmptyList.setVisibility(View.GONE);
        } else {
            mSyncStatusRecyclerView.setVisibility(View.GONE);
            mEmptyList.setVisibility(View.VISIBLE);
            mEmptyList.setText(getString(R.string.search_patient_no_results));
        }
    }

}
