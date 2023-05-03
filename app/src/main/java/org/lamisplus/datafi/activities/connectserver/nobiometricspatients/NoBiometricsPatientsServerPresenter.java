package org.lamisplus.datafi.activities.connectserver.nobiometricspatients;

import androidx.annotation.NonNull;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.FilterUtil;
import org.lamisplus.datafi.utilities.StringUtils;

import java.util.List;

public class NoBiometricsPatientsServerPresenter extends LamisBasePresenter implements NoBiometricsPatientsServerContract.Presenter {

    private final NoBiometricsPatientsServerContract.View findPatientsInfoView;
    private String mQuery;

    public NoBiometricsPatientsServerPresenter(NoBiometricsPatientsServerContract.View findPatientsInfoView) {
        this.findPatientsInfoView = findPatientsInfoView;
        this.findPatientsInfoView.setPresenter(this);
    }

    public NoBiometricsPatientsServerPresenter(@NonNull NoBiometricsPatientsServerContract.View findPatientsInfoView, String mQuery) {
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
