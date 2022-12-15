package org.lamisplus.datafi.activities.patientdashboard.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardContract;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardFragment;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.OrganizationUnitDAO;
import org.lamisplus.datafi.models.Codesets;
import org.lamisplus.datafi.models.ContactPoint;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.StringUtils;

import java.util.List;

public class PatientDashboardDetailsFragment extends PatientDashboardFragment implements PatientDashboardContract.ViewPatientDetails {

    private View rootView;
    private PatientDashboardActivity mPatientDashboardActivity;

    public static PatientDashboardDetailsFragment newInstance(){
        return new PatientDashboardDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_patient_details, null, false);
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
        if (isAdded()) {
            if (("Male").equals(CodesetsDAO.findCodesetsDisplayById(person.getGenderId()))) {
                ((ImageView) rootView.findViewById(R.id.patientDetailsGenderIv)).setImageResource(R.mipmap.ic_male);
                ((TextView) rootView.findViewById(R.id.patientDetailsGender)).setText("Male");
            } else {
                ((ImageView) rootView.findViewById(R.id.patientDetailsGenderIv)).setImageResource(R.mipmap.ic_female);
                ((TextView) rootView.findViewById(R.id.patientDetailsGender)).setText("Female");
            }
        }
        ImageView patientImageView = rootView.findViewById(R.id.patientPhoto);


        ((TextView) rootView.findViewById(R.id.patientDetailsName)).setText(person.getFirstName() + " " + person.getOtherName() + " " + person.getSurname());


            ((TextView) rootView.findViewById(R.id.patientDetailsBirthDate)).setText(person.getDateOfBirth());

        if (null != person.getAddress()) {
            ((TextView) rootView.findViewById(R.id.addressDetailsStreet)).setText(person.getAddresses().getCity());
            showAddressDetailsViewElement(R.id.addressDetailsStateLabel, R.id.addressDetailsState, OrganizationUnitDAO.findOrganizationUnitNameById(person.getAddresses().getStateId()));
            showAddressDetailsViewElement(R.id.addressDetailsCountryLabel, R.id.addressDetailsCountry, OrganizationUnitDAO.findOrganizationUnitNameById(person.getAddresses().getCountryId()));
            showAddressDetailsViewElement(R.id.addressDetailsPostalCodeLabel, R.id.addressDetailsPostalCode, person.getAddresses().getPostalCode());
            showAddressDetailsViewElement(R.id.addressDetailsCityLabel, R.id.addressDetailsCity, person.getAddresses().getDistrict());
        }
        if(null != person.pullContactPointList()){
            List<ContactPoint> list = person.pullContactPointList();
            //showAddressDetailsViewElement(R.id.patientDetailsPhoneLabel, R.id.patientDetailsPhone, list.get(0).getValue());
        }
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
