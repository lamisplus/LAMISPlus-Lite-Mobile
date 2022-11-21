package org.lamisplus.datafi.activities.findpatient;

import android.util.Log;

import com.activeandroid.query.Select;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.LamisCustomHandler;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class FindPatientPresenter extends LamisBasePresenter implements FindPatientContract.Presenter {

    private final FindPatientContract.View findPatientsInfoView;

    public FindPatientPresenter(FindPatientContract.View findPatientsInfoView) {
        this.findPatientsInfoView = findPatientsInfoView;
        this.findPatientsInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        updateLocalPatientsList();
    }

    @Override
    public void setQuery(String query) {

    }

    @Override
    public void updateLocalPatientsList() {
//        Thread thread = new Thread(){
//            @Override
//            public void run(){
//                List<Person> personList =   new Select().from(Person.class).execute();
//                findPatientsInfoView.updateListVisibility(true);
//                findPatientsInfoView.updateAdapter(personList);
//            }
//        };
//        thread.start();
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Person> personList = new Select().from(Person.class).execute();
                LamisCustomHandler.showJson(personList);
                findPatientsInfoView.updateListVisibility(true);
                findPatientsInfoView.updateAdapter(personList);
            }
        });
    }

}
