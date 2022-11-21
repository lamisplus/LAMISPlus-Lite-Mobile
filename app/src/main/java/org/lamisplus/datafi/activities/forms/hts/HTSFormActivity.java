package org.lamisplus.datafi.activities.forms.hts;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientContract;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientPresenter;

public class HTSFormActivity extends LamisBaseActivity {

    public HTSFormContract.Presenter mPresenter;
    public HTSFormFragment htsFormFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hts_form);

        htsFormFragment = (HTSFormFragment) getSupportFragmentManager().findFragmentById(R.id.htsFormContentFrame);
        if(htsFormFragment == null){
            htsFormFragment = HTSFormFragment.newinstance();
        }

        if(!htsFormFragment.isActive()){
            addFragmentToActivity(getSupportFragmentManager(), htsFormFragment, R.id.htsFormContentFrame);
        }

        mPresenter = new HTSFormPresenter(htsFormFragment);
    }

}
