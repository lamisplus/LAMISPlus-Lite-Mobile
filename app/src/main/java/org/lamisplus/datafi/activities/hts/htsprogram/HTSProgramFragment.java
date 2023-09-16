package org.lamisplus.datafi.activities.hts.htsprogram;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import org.lamisplus.datafi.activities.forms.hts.rst.RSTActivity;
import org.lamisplus.datafi.activities.preferences.PrefrencesActivity;
import org.lamisplus.datafi.dao.SettingsDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.FontsUtil;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HTSProgramFragment extends LamisBaseFragment<HTSProgramContract.Presenter> implements HTSProgramContract.View {

    private TextView mEmptyList;
    private RecyclerView mHTSProgramRecyclerView;

    //Initialization Progress bar
    private ProgressBar mProgressBar;

    private MenuItem mAddPatientMenuItem;

    private Button mCreateHTSButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hts_program, container, false);
        // Patient list config
        mHTSProgramRecyclerView = root.findViewById(R.id.htsProgramRecyclerView);
        mHTSProgramRecyclerView.setHasFixedSize(true);
        mHTSProgramRecyclerView.setAdapter(new HTSProgramRecyclerViewAdapter(this,
                new ArrayList<>()));
        mHTSProgramRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        mHTSProgramRecyclerView.setLayoutManager(linearLayoutManager);

        mEmptyList = root.findViewById(R.id.emptyHTSPatientList);
        mProgressBar = root.findViewById(R.id.htsProgramInitialProgressBar);
        mCreateHTSButton = root.findViewById(R.id.createHTSButton);

        setListener();
        return root;
    }

    public static HTSProgramFragment newInstance() {
        return new HTSProgramFragment();
    }

    private void setListener() {
        mCreateHTSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String deviceId = SettingsDAO.getSettings(ApplicationConstants.Settings.DEVICE_ID);
//                if (deviceId != null) {
                Intent intent = new Intent(getContext(), RSTActivity.class);
                startActivity(intent);
                requireActivity().finish();
//                } else {
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(requireActivity());
//                    dlgAlert.setMessage("Device ID Not Set. Please set the Device ID at the Settings page");
//                    dlgAlert.setTitle("Device ID");
//                    dlgAlert.setPositiveButton("OK",
//                            (dialog, whichButton) -> {
//                                //getActivity().finish();
//                                Intent intent = new Intent(getActivity(), PrefrencesActivity.class);
//                                startActivity(intent);
//                            }
//                    );
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.create().show();
//                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateAdapter(List<Person> patientList) {
        HTSProgramRecyclerViewAdapter adapter = new HTSProgramRecyclerViewAdapter(this, patientList);
        adapter.notifyDataSetChanged();
        mHTSProgramRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateListVisibility(boolean isVisible) {
        mProgressBar.setVisibility(View.GONE);
        mHTSProgramRecyclerView.setVisibility(View.VISIBLE);
    }

    public void updateView() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                List<Person> personList = new Select().from(Person.class).orderBy("id DESC").execute();
                LamisCustomHandler.showJson(personList);
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
            mHTSProgramRecyclerView.setVisibility(View.VISIBLE);
            mEmptyList.setVisibility(View.GONE);
        } else {
            mHTSProgramRecyclerView.setVisibility(View.GONE);
            mEmptyList.setVisibility(View.VISIBLE);
            mEmptyList.setText(getString(R.string.search_patient_no_results));
        }
    }

}
