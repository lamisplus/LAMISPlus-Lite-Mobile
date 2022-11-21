package org.lamisplus.datafi.activities.formdisplay;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class FormDisplayPagePresenter extends LamisBasePresenter implements FormDisplayContract.Presenter.PagePresenter {


    private final FormDisplayContract.View.PageView formDisplayInfoView;
    public TabLayout mTablayout;
    public LinearLayout mParent;

    public FormDisplayPagePresenter(FormDisplayContract.View.PageView formDisplayInfoView, TabLayout tabLayout){
        this.formDisplayInfoView = formDisplayInfoView;
        this.formDisplayInfoView.setPresenter(this);
        this.mTablayout = tabLayout;
    }

    @Override
    public void subscribe() {
        formDisplayInfoView.getTabLayout(this.mTablayout);
    }


    @Override
    public void createEncounter() {
        Log.v("Baron", "I am submitted");
    }

    @Override
    public void updateEncounter() {

    }
}
