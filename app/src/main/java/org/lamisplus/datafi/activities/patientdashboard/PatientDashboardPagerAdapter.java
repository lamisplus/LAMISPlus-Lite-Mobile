package org.lamisplus.datafi.activities.patientdashboard;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.patientdashboard.details.PatientDashboardDetailsFragment;
import org.lamisplus.datafi.activities.patientdashboard.details.PatientDashboardDetailsPresenter;
import org.lamisplus.datafi.activities.patientdashboard.fingerprints.PatientDashboardFingerPrintsFragment;
import org.lamisplus.datafi.activities.patientdashboard.fingerprints.PatientDashboardFingerPrintsPresenter;
import org.lamisplus.datafi.activities.patientdashboard.followupvisits.PatientDashboardFollowUpVisitsFragment;
import org.lamisplus.datafi.activities.patientdashboard.followupvisits.PatientDashboardFollowUpVisitsPresenter;
import org.lamisplus.datafi.activities.patientdashboard.formdetails.PatientDashboardFormDetailsFragment;
import org.lamisplus.datafi.activities.patientdashboard.formdetails.PatientDashboardFormDetailsPresenter;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.DateUtils;

class PatientDashboardPagerAdapter extends FragmentPagerAdapter {

    private static final int TAB_COUNT = 2;

    //private static final int DETAILS_TAB_POS = 0;

    private static final int FORM_DETAILS_POS = 0;
    private static final int FINGERPRINTS_TAB_POS = 1;

    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    private String mPatientId;

    private Context context;

    PatientDashboardPagerAdapter(FragmentManager fm, Context context, String id) {
        super(fm);
        this.context = context;
        this.mPatientId = id;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case FORM_DETAILS_POS:
                PatientDashboardFormDetailsFragment patientDashboardFormDetailsFragment = PatientDashboardFormDetailsFragment.newInstance();
                new PatientDashboardFormDetailsPresenter(mPatientId, patientDashboardFormDetailsFragment);
                return patientDashboardFormDetailsFragment;
            case FINGERPRINTS_TAB_POS:
                PatientDashboardFingerPrintsFragment patientDashboardFingerPrintsFragment = PatientDashboardFingerPrintsFragment.newInstance();
                new PatientDashboardFingerPrintsPresenter(mPatientId, patientDashboardFingerPrintsFragment);
                return patientDashboardFingerPrintsFragment;
            default:
                if (checkPMTCTEligibility()) {
                    PatientDashboardFollowUpVisitsFragment patientDashboardFollowUpVisitsFragment = PatientDashboardFollowUpVisitsFragment.newInstance();
                    new PatientDashboardFollowUpVisitsPresenter(mPatientId, patientDashboardFollowUpVisitsFragment);
                    return patientDashboardFollowUpVisitsFragment;
                }
                return null;
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FORM_DETAILS_POS:
                return context.getString(R.string.patient_scroll_tab_formdetails_label);
            case FINGERPRINTS_TAB_POS:
                return context.getString(R.string.patient_scroll_tab_fingerprints_label);
            default:
                if (checkPMTCTEligibility()) {
                    return context.getString(R.string.patient_pmtct_followup_visit_label);
                }
                return super.getPageTitle(position);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    private boolean checkPMTCTEligibility() {
        Person person = PersonDAO.findPersonById(mPatientId);
        if(person != null){
            return CodesetsDAO.findCodesetsDisplayById(person.getGenderId()).equals("Female") && DateUtils.getAgeFromBirthdateString(person.getDateOfBirth()) > 10;
        }
        return false;
        //return false;
    }


    @Override
    public int getCount() {
        if (checkPMTCTEligibility()) {
            return TAB_COUNT + 1;
        } else {
            return TAB_COUNT;
        }
    }

}
