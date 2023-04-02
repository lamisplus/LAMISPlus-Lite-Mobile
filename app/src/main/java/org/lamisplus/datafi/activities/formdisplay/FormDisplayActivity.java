package org.lamisplus.datafi.activities.formdisplay;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class FormDisplayActivity extends LamisBaseActivity implements FormDisplayContract.View.MainView {

    public FormDisplayContract.Presenter.MainPresenter mPresenter;
    public FormDisplayFragment formDisplayFragment;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String title[] = {"HIV Enrollment Form", "Two", "Three"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_display);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.lamis_plus_logo);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        FormPageAdapter adapter = new FormPageAdapter(getSupportFragmentManager(), "");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 1){
                    FormDisplayClientIntakeFragment.getInstance(1, "Client Intake");
                } else if (tab.getPosition() == 2) {
                    FormDisplayPostTest.getInstance(tab.getPosition(), "Pre Test Councelling");
                }else{
                    FormDisplayFragment.getInstance(tab.getPosition(), "HTS");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        attachPresenterToFragment(fragment);
        super.onAttachFragment(fragment);
    }


    private void attachPresenterToFragment(Fragment fragment) {
        if (fragment instanceof FormDisplayFragment) {
            new FormDisplayPagePresenter((FormDisplayFragment) fragment, tabLayout);
//            Bundle bundle = getIntent().getExtras();
//            String encounterDate = null;
//            String personGender = null;
//            String valueRef = null;
//            ArrayList<FormFieldsWrapper> formFieldsWrappers = null;
//            if (bundle != null) {
//                Patient patient = new PatientDAO().findPatientByID(Long.toString(personID));
//                valueRef = (String) bundle.get(ApplicationConstants.BundleKeys.VALUEREFERENCE);
//                formFieldsWrappers = bundle.getParcelableArrayList(ApplicationConstants.BundleKeys.FORM_FIELDS_LIST_BUNDLE);
//                personGender = patient.getGender();
//                encounterDate = (String) bundle.get(ApplicationConstants.BundleKeys.ENCOUNTERDATETIME);
//            }
//            Form form = FormService.getForm(valueRef);
//            List<Page> pageList = form.getPages();
//            for (Page page : pageList) {
//                if (formFieldsWrappers != null) {
//                    new FormDisplayPagePresenter((FormDisplayPageFragment) fragment, pageList.get(getFragmentNumber(fragment)), formFieldsWrappers, pageList, encounterDate);
//
//                } else {
//                    if (personGender != null) {
//                        new FormDisplayPagePresenter((FormDisplayPageFragment) fragment, pageList.get(getFragmentNumber(fragment)), personGender);
//                    } else {
//                        new FormDisplayPagePresenter((FormDisplayPageFragment) fragment, pageList.get(getFragmentNumber(fragment)));
//                    }
//                }
//            }
        }
    }

    private void initComponents() {
        mPresenter = new FormDisplayMainPresenter(this, getIntent().getExtras(), (FormPageAdapter) viewPager.getAdapter());
    }

    @Override
    public void quitFormEntry() {

    }

    @Override
    public void enableSubmitButton(boolean enabled) {

    }

    @Override
    public void showToast(String errorMessage) {

    }

    @Override
    public void setPresenter(FormDisplayContract.Presenter.MainPresenter presenter) {
        this.mPresenter = presenter;
    }
}
