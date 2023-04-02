package org.lamisplus.datafi.activities.patientdashboard.formdetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.forms.hts.rst.RSTActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardContract;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardFragment;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.FontsUtil;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;

import java.util.List;

public class PatientDashboardFormDetailsFragment extends PatientDashboardFragment implements PatientDashboardContract.ViewPatientDetails {

    private View rootView;
    private PatientDashboardActivity mPatientDashboardActivity;
    private LinearLayout mFormDetailsLayout;

    public static PatientDashboardFormDetailsFragment newInstance(){
        return new PatientDashboardFormDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_patient_form_details, null, false);
//        FontsUtil.setFont(this.getActivity().findViewById(android.R.id.content));
//        FontsUtil.setFont((ViewGroup) rootView);
        initViews(rootView);
        setListeners();
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

    private void setListeners(){

    }

    private void initViews(View rootView){
        mFormDetailsLayout = rootView.findViewById(R.id.patientFormDetailsLayout);
    }

    @Override
    public void resolveFormDetailsDisplay(List<Encounter> encounterList) {
        mFormDetailsLayout.removeAllViewsInLayout();
        int id = 0;
        for(Encounter encounter : encounterList){
          //String FormName =  encounter.getName();
            LinearLayout tempLinearTextView = new LinearLayout(LamisPlus.getInstance());
            tempLinearTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_bottom_linearlayout));
            tempLinearTextView.setOrientation(LinearLayout.VERTICAL);
            id = encounter.getId().intValue();
            tempLinearTextView.setId(encounter.getId().intValue());

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(10, 0, 10, 0);
            tempLinearTextView.setPadding(0 , 20, 0 ,20);
            tempLinearTextView.setLayoutParams(layoutParams2);

            TextView textViewHeader = new TextView(getContext());
            textViewHeader.setText(encounter.getName());
            //textViewHeader.setTypeface(Typeface.DEFAULT_BOLD);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 20, 10, 20);
            textViewHeader.setLayoutParams(params);
            textViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            if(encounter.isSynced()) {
                textViewHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.view_eye_grey, 0, R.drawable.check_circle_green, 0);
            }else{
                textViewHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.view_eye_grey, 0, R.drawable.cancel_circle_red, 0);
            }
            textViewHeader.setCompoundDrawablePadding(10);

            tempLinearTextView.addView(textViewHeader);
            mFormDetailsLayout.addView(tempLinearTextView);

            LinearLayout linearLayout = rootView.findViewById(id);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Class className = Class.forName(encounter.getPackageName());
                        Intent form = new Intent(getContext(), className);
                        form.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                                String.valueOf(mPresenter.getPatientId()));
                        startActivity(form);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

}
