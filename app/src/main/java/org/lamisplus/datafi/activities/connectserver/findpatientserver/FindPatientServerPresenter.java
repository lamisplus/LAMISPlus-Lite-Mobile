package org.lamisplus.datafi.activities.connectserver.findpatientserver;

import androidx.annotation.NonNull;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.FilterUtil;
import org.lamisplus.datafi.utilities.StringUtils;

import java.util.List;

public class FindPatientServerPresenter extends LamisBasePresenter implements FindPatientServerContract.Presenter {

    private final FindPatientServerContract.View findPatientsInfoView;
    private String mQuery;

    public FindPatientServerPresenter(FindPatientServerContract.View findPatientsInfoView) {
        this.findPatientsInfoView = findPatientsInfoView;
        this.findPatientsInfoView.setPresenter(this);
    }

    public FindPatientServerPresenter(@NonNull FindPatientServerContract.View findPatientsInfoView, String mQuery) {
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
        boolean isFiltering = StringUtils.notNull(mQuery) && !mQuery.isEmpty();
        List<Person> personList = PersonDAO.getAllPatients();
        if (isFiltering) {
            List<Person> patientList = FilterUtil.getPatientsFilteredByQuery(personList, mQuery);
            if (patientList.isEmpty()) {
                findPatientsInfoView.updateListVisibility(false, mQuery);
            } else {
                findPatientsInfoView.updateListVisibility(true);
            }
            personList = FilterUtil.getPatientsFilteredByQuery(personList);
            findPatientsInfoView.updateAdapter(patientList);
        }else {
            findPatientsInfoView.updateView();
        }
    }

}
