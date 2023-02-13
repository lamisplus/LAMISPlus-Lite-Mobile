package org.lamisplus.datafi.activities.findpatient;

import android.util.Log;

import androidx.annotation.NonNull;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.FilterUtil;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class FindPatientPresenter extends LamisBasePresenter implements FindPatientContract.Presenter {

    private final FindPatientContract.View findPatientsInfoView;
    private String mQuery;

    public FindPatientPresenter(FindPatientContract.View findPatientsInfoView) {
        this.findPatientsInfoView = findPatientsInfoView;
        this.findPatientsInfoView.setPresenter(this);
    }

    public FindPatientPresenter(@NonNull FindPatientContract.View findPatientsInfoView, String mQuery) {
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
