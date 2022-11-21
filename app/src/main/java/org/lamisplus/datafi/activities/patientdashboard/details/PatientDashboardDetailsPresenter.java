package org.lamisplus.datafi.activities.patientdashboard.details;

import android.util.Log;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardContract;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardPresenter;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;

public class PatientDashboardDetailsPresenter extends PatientDashboardPresenter implements PatientDashboardContract.PatientDetailsPresenter {

    private PatientDashboardContract.ViewPatientDetails mPatientDetailsView;
    private PersonDAO mPersonDAO;

    public static String personId = "0";

    public PatientDashboardDetailsPresenter(String id, PatientDashboardContract.ViewPatientDetails mPatientDetailsView) {
        this.mPatientDetailsView = mPatientDetailsView;
        this.mPersonDAO = new PersonDAO();
        this.mPerson = this.mPersonDAO.findPersonById(id);
        setPersonId(id);
        this.mPatientDetailsView.setPresenter(this);
    }

    @Override
    public void synchronizePatient() {
        if(NetworkUtils.isOnline()) {
            mPatientDetailsView.showDialog(R.string.action_synchronize_patients);
            syncDetailsData();
            syncVisitsData();
            syncVitalsData();
        } else {
            reloadPatientData(mPerson);
            mPatientDetailsView.showToast(R.string.synchronize_patient_network_error, true);
        }
    }

    @Override
    public void updatePatientDataFromServer() {
        if(NetworkUtils.isOnline()) {
            syncDetailsData();
            //syncVisitsData();
            //syncVitalsData();
        }
    }

    @Override
    public void reloadPatientData(Person mPerson) {
        mPatientDetailsView.resolvePatientDataDisplay(mPerson);
    }

    @Override
    public void deletePatient() {

    }

    private void syncVitalsData() {
        //visitRepository.syncLastVitals(mPatient.getUuid());
    }
    private void syncVisitsData() {
//        visitRepository.syncVisitsData(mPatient, new DefaultResponseCallbackListener() {
//            @Override
//            public void onResponse() {
//                mPatientDetailsView.showToast(R.string.synchronize_patient_successful, false);
//                mPatientDetailsView.dismissDialog();
//            }
//
//            @Override
//            public void onErrorResponse(String errorMessage) {
//                mPatientDetailsView.showToast(R.string.synchronize_patient_error, true);
//                mPatientDetailsView.dismissDialog();
//            }
//        });
    }

    private void syncDetailsData() {
//        if (null != mPatient.getUuid() && !mPatient.getUuid().isEmpty() && !mPatient.getUuid().equals(" ")) {
//            patientRepository.downloadPatientByUuid(mPatient.getUuid(), new DownloadPatientCallbackListener() {
//                @Override
//                public void onPatientDownloaded(Patient patient) {
//                    updatePatientData(patient);
//                }
//
//                @Override
//                public void onPatientPhotoDownloaded(Patient patient) {
//                    updatePatientData(patient);
//                }
//
//                @Override
//                public void onResponse() {
//                    // This method is intentionally empty
//                }
//
//                @Override
//                public void onErrorResponse(String errorMessage) {
//                    mPatientDetailsView.showToast(R.string.synchronize_patient_error, true);
//                }
//            });
//        }

    }

    @Override
    public long getPatientId() {
        return mPerson.getId();
    }

    public void setPersonId(String id){
        this.personId = id;
    }


    @Override
    public void subscribe() {
        updatePatientDataFromServer();
        mPerson = mPersonDAO.findPersonById(personId);
        mPatientDetailsView.resolvePatientDataDisplay(mPersonDAO.findPersonById(this.personId));
        mPatientDetailsView.setMenuTitle(mPerson.getFirstName(), mPerson.getIdentifiers().getValue());
    }
}
