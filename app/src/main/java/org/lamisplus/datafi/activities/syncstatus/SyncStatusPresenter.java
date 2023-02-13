package org.lamisplus.datafi.activities.syncstatus;

import androidx.annotation.NonNull;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.FilterUtil;
import org.lamisplus.datafi.utilities.StringUtils;

import java.util.List;

public class SyncStatusPresenter extends LamisBasePresenter implements SyncStatusContract.Presenter {

    private final SyncStatusContract.View findPatientsInfoView;
    private String mQuery;

    public SyncStatusPresenter(SyncStatusContract.View findPatientsInfoView) {
        this.findPatientsInfoView = findPatientsInfoView;
        this.findPatientsInfoView.setPresenter(this);
    }

    public SyncStatusPresenter(@NonNull SyncStatusContract.View findPatientsInfoView, String mQuery) {
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
