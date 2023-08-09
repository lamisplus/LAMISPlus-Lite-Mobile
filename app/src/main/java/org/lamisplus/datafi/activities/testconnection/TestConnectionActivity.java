package org.lamisplus.datafi.activities.testconnection;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class TestConnectionActivity extends LamisBaseActivity {

    public TestConnectionContract.Presenter mPresenter;
    public TestConnectionFragment testConnectionFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test_connection);

        testConnectionFragment = (TestConnectionFragment) getSupportFragmentManager().findFragmentById(R.id.testConnectionContentFrame);
        if(testConnectionFragment == null){
            testConnectionFragment = TestConnectionFragment.newInstance();
        }
        if(!testConnectionFragment.isActive()){
            addFragmentToActivity(getSupportFragmentManager(), testConnectionFragment, R.id.testConnectionContentFrame);
        }

        mPresenter = new TestConnectionPresenter(testConnectionFragment, mLamisPlus);
    }

}
