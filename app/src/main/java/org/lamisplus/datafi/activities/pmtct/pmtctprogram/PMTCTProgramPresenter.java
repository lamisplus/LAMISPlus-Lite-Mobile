package org.lamisplus.datafi.activities.pmtct.pmtctprogram;

import androidx.annotation.NonNull;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.FilterUtil;
import org.lamisplus.datafi.utilities.StringUtils;

import java.util.List;

public class PMTCTProgramPresenter extends LamisBasePresenter implements PMTCTProgramContract.Presenter {

    private final PMTCTProgramContract.View htsProgramInfoView;
    private String mQuery;

    public PMTCTProgramPresenter(PMTCTProgramContract.View htsProgramInfoView) {
        this.htsProgramInfoView = htsProgramInfoView;
        this.htsProgramInfoView.setPresenter(this);
    }

    public PMTCTProgramPresenter(@NonNull PMTCTProgramContract.View htsProgramInfoView, String mQuery) {
        this.htsProgramInfoView = htsProgramInfoView;
        this.htsProgramInfoView.setPresenter(this);
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
                htsProgramInfoView.updateListVisibility(false, mQuery);
            } else {
                htsProgramInfoView.updateListVisibility(true);
            }
            personList = FilterUtil.getPatientsFilteredByQuery(personList);
            htsProgramInfoView.updateAdapter(patientList);
        }else {
            htsProgramInfoView.updateView();
        }
    }

}
