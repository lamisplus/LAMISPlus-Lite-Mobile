package org.lamisplus.datafi.activities.connectserver.withbiometricspatients;

import androidx.annotation.NonNull;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class WithBiometricsPatientsServerPresenter extends LamisBasePresenter implements WithBiometricsPatientsServerContract.Presenter {

    private final WithBiometricsPatientsServerContract.View findPatientsInfoView;
    private String mQuery;

    public WithBiometricsPatientsServerPresenter(WithBiometricsPatientsServerContract.View findPatientsInfoView) {
        this.findPatientsInfoView = findPatientsInfoView;
        this.findPatientsInfoView.setPresenter(this);
    }

    public WithBiometricsPatientsServerPresenter(@NonNull WithBiometricsPatientsServerContract.View findPatientsInfoView, String mQuery) {
        this.findPatientsInfoView = findPatientsInfoView;
        this.findPatientsInfoView.setPresenter(this);
        this.mQuery = mQuery;
    }

    @Override
    public void subscribe() {
        updateLocalPatientsList();
    }

    @Override
    public void setQuery(String query) {
        mQuery = query;
    }

    @Override
    public void updateLocalPatientsList() {
        findPatientsInfoView.updateView();
//        boolean isFiltering = StringUtils.notNull(mQuery) && !mQuery.isEmpty();
//        List<Person> personList = PersonDAO.getAllPatients();
//        if (isFiltering) {
//            List<Person> patientList = FilterUtil.getPatientsFilteredByQuery(personList, mQuery);
//            if (patientList.isEmpty()) {
//                findPatientsInfoView.updateListVisibility(false, mQuery);
//            } else {
//                findPatientsInfoView.updateListVisibility(true);
//            }
//            personList = FilterUtil.getPatientsFilteredByQuery(personList);
//            findPatientsInfoView.updateAdapter(patientList);
//        }else {
//            findPatientsInfoView.updateView();
//        }
    }

}
