package org.lamisplus.datafi.activities.formdisplay;

import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class FormDisplayContract {

    interface View {

        interface MainView extends LamisBaseView<Presenter.MainPresenter> {
            void quitFormEntry();

            void enableSubmitButton(boolean enabled);

            void showToast(String errorMessage);
        }

        interface PageView extends LamisBaseView<Presenter.PagePresenter> {
            void attachSectionToView(LinearLayout linearLayout);
            void getTabLayout(TabLayout tabLayout);
        }
    }

    interface Presenter {

        interface MainPresenter extends LamisBasePresenterContract {

        }

        interface PagePresenter extends LamisBasePresenterContract {
            void createEncounter();
            void updateEncounter();
        }

    }

}
