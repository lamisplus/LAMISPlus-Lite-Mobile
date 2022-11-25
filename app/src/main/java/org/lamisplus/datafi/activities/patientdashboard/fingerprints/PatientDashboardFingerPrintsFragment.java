package org.lamisplus.datafi.activities.patientdashboard.fingerprints;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardContract;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardFragment;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.StringUtils;

import java.util.List;

public class PatientDashboardFingerPrintsFragment extends PatientDashboardFragment implements PatientDashboardContract.ViewPatientDetails {

    private View rootView;
    private PatientDashboardActivity mPatientDashboardActivity;

    public static PatientDashboardFingerPrintsFragment newInstance(){
        return new PatientDashboardFingerPrintsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_patient_fingerprints, null, false);
//        FontsUtil.setFont(this.getActivity().findViewById(android.R.id.content));
//        FontsUtil.setFont((ViewGroup) rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPatientDashboardActivity = (PatientDashboardActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setPresenter(mPresenter);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.actionSynchronize:
                //((PatientDashboardDetailsPresenter) mPresenter).synchronizePatient();
                break;
            default:
                // Do nothing
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.patient_details_menu, menu);
    }

    @Override
    public void attachSnackbarToActivity() {

    }

    @Override
    public void resolvePatientDataDisplay(Person person) {

    }

    @Override
    public void showDialog(int resId) {
        //mPatientDashboardActivity.showProgressDialog(resId);
    }

    private void showAddressDetailsViewElement(int detailsViewLabel, int detailsViewId, String detailsText) {
        if (StringUtils.notNull(detailsText) && StringUtils.notEmpty(detailsText)) {
            ((TextView) rootView.findViewById(detailsViewId)).setText(detailsText);
        } else {
            rootView.findViewById(detailsViewId).setVisibility(View.GONE);
            rootView.findViewById(detailsViewLabel).setVisibility(View.GONE);
        }
    }

    @Override
    public void dismissDialog() {
        mPatientDashboardActivity.dismissCustomFragmentDialog();
    }

    @Override
    public void showToast(int stringRes, boolean error) {
//        ToastUtil.ToastType toastType = error ? ToastUtil.ToastType.ERROR : ToastUtil.ToastType.SUCCESS;
//        ToastUtil.showShortToast(mPatientDashboardActivity, toastType, stringRes);
    }

    @Override
    public void setMenuTitle(String nameString, String identifier) {
        mPatientDashboardActivity.getSupportActionBar().setTitle(nameString);
        mPatientDashboardActivity.getSupportActionBar().setSubtitle("#" + identifier);
    }

    @Override
    public void resolveFormDetailsDisplay(List<Encounter> encounterList) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                PatientDashboardActivity.hideFABs(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
