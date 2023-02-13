package org.lamisplus.datafi.activities.hts.htsprogram;

import androidx.annotation.NonNull;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.FilterUtil;
import org.lamisplus.datafi.utilities.StringUtils;

import java.util.List;

public class HTSProgramPresenter extends LamisBasePresenter implements HTSProgramContract.Presenter {

    private final HTSProgramContract.View htsProgramInfoView;
    private String mQuery;

    public HTSProgramPresenter(HTSProgramContract.View htsProgramInfoView) {
        this.htsProgramInfoView = htsProgramInfoView;
        this.htsProgramInfoView.setPresenter(this);
    }

    public HTSProgramPresenter(@NonNull HTSProgramContract.View htsProgramInfoView, String mQuery) {
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
