package org.lamisplus.datafi.activities.biometrics;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class BiometricsActivity extends LamisBaseActivity {

    public BiometricsContract.Presenter mPresenter;
    public BiometricsFragment biometricsFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrics);

        biometricsFragment = (BiometricsFragment) getSupportFragmentManager().findFragmentById(R.id.biometricsContentFrame);
        if(biometricsFragment == null){
            biometricsFragment = BiometricsFragment.newInstance();
        }
        if(!biometricsFragment.isActive()){
            addFragmentToActivity(getSupportFragmentManager(), biometricsFragment, R.id.biometricsContentFrame);
        }

        mPresenter = new BiometricsPresenter(biometricsFragment, mLamisPlus);
    }


}
