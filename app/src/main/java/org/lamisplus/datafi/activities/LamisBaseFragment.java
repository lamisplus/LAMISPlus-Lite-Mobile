package org.lamisplus.datafi.activities;

import android.content.Intent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class LamisBaseFragment <T extends LamisBasePresenterContract> extends Fragment implements LamisBaseView<T> {

    protected T mPresenter;

    @Override
    public void setPresenter(@NonNull T presenter) {
        mPresenter = presenter;
    }

    public boolean isActive(){
        return isAdded();
    }

    public void startNewActivity(Class<? extends LamisBaseActivity> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mPresenter!= null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mPresenter!= null) {
            mPresenter.unsubscribe();
        }
    }

}
