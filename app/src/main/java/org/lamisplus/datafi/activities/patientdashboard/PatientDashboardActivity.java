package org.lamisplus.datafi.activities.patientdashboard;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.activities.biometrics.BiometricsActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.activities.patientdashboard.details.PatientDashboardDetailsFragment;
import org.lamisplus.datafi.activities.patientdashboard.details.PatientDashboardDetailsPresenter;
import org.lamisplus.datafi.activities.patientdashboard.fingerprints.PatientDashboardFingerPrintsActivity;
import org.lamisplus.datafi.activities.patientdashboard.fingerprints.PatientDashboardFingerPrintsFragment;
import org.lamisplus.datafi.activities.patientdashboard.fingerprints.PatientDashboardFingerPrintsPresenter;
import org.lamisplus.datafi.activities.patientprogram.PatientProgramActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.TabUtil;

public class PatientDashboardActivity extends LamisBaseActivity {

    private String mId;


    public PatientDashboardContract.PatientDashboardMainPresenter mPresenter;

    static boolean isActionFABOpen = false;
    public static FloatingActionButton additionalActionsFAB, updateFAB, deleteFAB, visitFAB, pbsFAB, commodityFAB;
    public static LinearLayout deleteFabLayout, updateFabLayout;
    public static Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        getSupportActionBar().setElevation(0);
        Bundle patientBundle = savedInstanceState;
        if (null != patientBundle) {
            patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            patientBundle = getIntent().getExtras();
        }
        mId = String.valueOf(patientBundle.get(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE));
        initViewPager(new PatientDashboardPagerAdapter(getSupportFragmentManager(), this, mId));

        resources = getResources();
        setupUpdateDeleteActionFAB();
    }

    private void initViewPager(PatientDashboardPagerAdapter adapter) {
        final ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabHost = findViewById(R.id.tabhost);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        viewPager.setAdapter(adapter);
        tabHost.setupWithViewPager(viewPager);
    }

    @Override
    public void onConfigurationChanged(final Configuration config) {
        super.onConfigurationChanged(config);
        TabUtil.setHasEmbeddedTabs(getSupportActionBar(), getWindowManager(), TabUtil.MIN_SCREEN_WIDTH_FOR_PATIENTDASHBOARDACTIVITY);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        attachPresenterToFragment(fragment);
    }

    private void attachPresenterToFragment(Fragment fragment) {
        Bundle patientBundle = getIntent().getExtras();
        String id = String.valueOf(patientBundle.get(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE));
        if (fragment instanceof PatientDashboardDetailsFragment) {
            mPresenter = new PatientDashboardDetailsPresenter(id, ((PatientDashboardDetailsFragment) fragment));
        } else if (fragment instanceof PatientDashboardFingerPrintsFragment) {
            mPresenter = new PatientDashboardFingerPrintsPresenter(id, ((PatientDashboardFingerPrintsFragment) fragment));
        }
    }

    public void setupUpdateDeleteActionFAB() {
        additionalActionsFAB = findViewById(R.id.activity_patient_dashboard_action_fab);
        updateFAB = findViewById(R.id.activity_patient_dashboard_update_fab);
        deleteFAB = findViewById(R.id.activity_patient_dashboard_delete_fab);
        updateFabLayout = findViewById(R.id.custom_fab_update_ll);
        deleteFabLayout = findViewById(R.id.custom_fab_delete_ll);
        visitFAB = findViewById(R.id.activity_patient_visit_action_fab);
        pbsFAB = findViewById(R.id.activity_patient_pbs_action_fab);

        additionalActionsFAB.setOnClickListener(v -> {
            animateFAB(isActionFABOpen);
            if (!isActionFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });
        //deleteFAB.setOnClickListener(v -> showDeletePatientDialog(mPresenter.getPatientId()));
        deleteFAB.setOnClickListener(v -> deletePatient());
        updateFAB.setOnClickListener(v -> startPatientUpdateActivity(mPresenter.getPatientId()));
        visitFAB.setOnClickListener(v -> startPatientProgramActivity(mPresenter.getPatientId()));
        pbsFAB.setOnClickListener(v -> startPatientPBSActivity(mPresenter.getPatientId()));

    }

    public void startPatientUpdateActivity(long patientId) {
        Intent updatePatient = new Intent(this, AddEditPatientActivity.class);
        updatePatient.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(patientId));
        startActivity(updatePatient);
    }

    public void startPatientProgramActivity(long patientId) {
        Intent patientProgram = new Intent(this, PatientProgramActivity.class);
        patientProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(patientId));
        startActivity(patientProgram);
    }

    public void startPatientPBSActivity(long patientId) {
        Intent pbsProgram = new Intent(this, BiometricsActivity.class);
        pbsProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                String.valueOf(patientId));
        startActivity(pbsProgram);
    }

    private static void animateFAB(boolean isFABClosed) {
        if (!isFABClosed) {
            ObjectAnimator.ofFloat(additionalActionsFAB, "rotation", 0f, 180f).setDuration(500).start();
            final Handler handler = new Handler();
            handler.postDelayed(() -> additionalActionsFAB.setImageDrawable(resources
                    .getDrawable(R.drawable.ic_close_white_24dp)), 400);
        } else {
            ObjectAnimator.ofFloat(additionalActionsFAB, "rotation", 180f, 0f).setDuration(500).start();

            final Handler handler = new Handler();
            handler.postDelayed(() -> additionalActionsFAB.setImageDrawable(resources
                    .getDrawable(R.drawable.ic_edit_white_24dp)), 400);
        }

    }

    public void deletePatient() {
        mPresenter.deletePatient();
        Intent i = new Intent(this, FindPatientActivity.class);
        startActivity(i);
    }

    public static void showFABMenu() {
        isActionFABOpen = true;
        deleteFabLayout.setVisibility(View.VISIBLE);
        updateFabLayout.setVisibility(View.VISIBLE);
        deleteFabLayout.animate().translationY(-resources.getDimension(R.dimen.custom_fab_bottom_margin_55));
        updateFabLayout.animate().translationY(-resources.getDimension(R.dimen.custom_fab_bottom_margin_105));

    }

    /**
     * This method is called from other Fragments only when they are visible to the user.
     *
     * @param hide To hide the FAB menu depending on the Fragment visible
     */
    @SuppressLint("RestrictedApi")
    public static void hideFABs(boolean hide) {
        closeFABMenu();
        if (hide) {
            additionalActionsFAB.setVisibility(View.GONE);
            visitFAB.setVisibility(View.GONE);
            updateFAB.setVisibility(View.GONE);
            pbsFAB.setVisibility(View.GONE);
        } else {
            additionalActionsFAB.setVisibility(View.VISIBLE);
            visitFAB.setVisibility(View.VISIBLE);
            pbsFAB.setVisibility(View.VISIBLE);

            // will animate back the icon back to its original angle instantaneously
            ObjectAnimator.ofFloat(additionalActionsFAB, "rotation", 180f, 0f).setDuration(0).start();
            additionalActionsFAB.setImageDrawable(resources
                    .getDrawable(R.drawable.ic_edit_white_24dp));
        }
    }

    public static void closeFABMenu() {
        isActionFABOpen = false;
        deleteFabLayout.animate().translationY(0);
        updateFabLayout.animate().translationY(0);
        deleteFabLayout.setVisibility(View.GONE);
        updateFabLayout.setVisibility(View.GONE);
    }

}
