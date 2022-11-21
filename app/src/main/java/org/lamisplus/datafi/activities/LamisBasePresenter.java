package org.lamisplus.datafi.activities;


import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class LamisBasePresenter implements LamisBasePresenterContract {

    private CompositeSubscription mSubscription;

    public LamisBasePresenter(){
        mSubscription = new CompositeSubscription();
    }

    public void addSubscription(Subscription subscription){
        if(mSubscription != null){
            mSubscription.add(subscription);
        }
    }

    @Override
    public void unsubscribe(){
        if(mSubscription != null){
            mSubscription.clear();
        }
    }

}
