package org.lamisplus.datafi.activities.forms.hts.pretest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.app.AppActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.auth.AuthorizationManager;
import org.lamisplus.datafi.databases.LamisPlusDBOpenHelper;
import org.lamisplus.datafi.utilities.ImageUtils;

public class PreTestFragment extends LamisBaseFragment<PreTestContract.Presenter> implements PreTestContract.View, View.OnClickListener {

    private SparseArray<Bitmap> mBitmapCache;
    private ImageView mDashboardButton;
    private ImageView mAppButton;
    private ImageView mSettingsButton;
    private ImageView mLogoutButton;
    private ImageView mMyClientsButton;
    private ImageView mAppointmentsButton;
    private ImageView mUnsuppressedClientsButton;
    private ImageView mInterruptedTreatmentsButton;
    private ImageView mViralloadSuppressionButton;
    private ImageView mCovid19VaccinationButton;
    private LinearLayout mDashboardView;
    private LinearLayout mAppView;
    private LinearLayout mSettingsView;
    private LinearLayout mLogoutView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_client_intake, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
        }
        return root;
    }

    public static PreTestFragment newInstance() {
        return new PreTestFragment();
    }

    private void initiateFragmentViews(View root) {
        mDashboardButton = root.findViewById(R.id.dashboardButton);
        mAppButton = root.findViewById(R.id.appButton);
        mSettingsButton = root.findViewById(R.id.settingsButton);
        mLogoutButton = root.findViewById(R.id.logoutButton);
        mMyClientsButton = root.findViewById(R.id.myClientsButton);
        mAppointmentsButton = root.findViewById(R.id.appointmentsButton);
        mUnsuppressedClientsButton = root.findViewById(R.id.unsuppressedClientsButton);
        mInterruptedTreatmentsButton = root.findViewById(R.id.interruptedTreatmentButton);
        mViralloadSuppressionButton = root.findViewById(R.id.viralLoadSuppressionButton);
        mCovid19VaccinationButton = root.findViewById(R.id.covid19VaccinationButton);

        mDashboardView = root.findViewById(R.id.dashboardView);
        mAppView = root.findViewById(R.id.appView);
        mSettingsView = root.findViewById(R.id.settingsView);
        mLogoutView = root.findViewById(R.id.logoutView);
    }

    private void setListeners() {
        mDashboardView.setOnClickListener(this);
        mAppView.setOnClickListener(this);
        mSettingsView.setOnClickListener(this);
        mLogoutView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dashboardView:
                    startNewActivity(PreTestActivity.class);
                break;
            case R.id.appView:
                startNewActivity(AppActivity.class);
                break;
            case R.id.settingsView:
                //Do nothing for now
                break;
            case R.id.logoutView:
                LamisPlus.getInstance().clearUserPreferencesData();
                new AuthorizationManager().moveToLoginActivity();
                //ToastUtil.showShortToast(getApplicationContext(), ToastUtil.ToastType.SUCCESS, R.string.logout_success);
                LamisPlusDBOpenHelper.getInstance().closeDatabases();
                break;
            default:

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindDrawableResources();
    }

    /**
     * Binds drawable resources to all dashboard buttons
     * Initially called by this view's presenter
     */
    @Override
    public void bindDrawableResources() {
        bindDrawableResource(mMyClientsButton, R.drawable.clients);
        bindDrawableResource(mDashboardButton, R.drawable.dashboard_icon);
        bindDrawableResource(mAppButton, R.drawable.app_icon);
        bindDrawableResource(mSettingsButton, R.drawable.settings_icon);
        bindDrawableResource(mLogoutButton, R.drawable.logout_icon);
    }

    /**
     * Binds drawable resource to ImageView
     *
     * @param imageView  ImageView to bind resource to
     * @param drawableId id of drawable resource (for example R.id.somePicture);
     */
    private void bindDrawableResource(ImageView imageView, int drawableId) {
        mBitmapCache = new SparseArray<>();
        if (getView() != null) {
            createImageBitmap(drawableId, imageView.getLayoutParams());
            imageView.setImageBitmap(mBitmapCache.get(drawableId));
        }
    }

    /**
     * Unbinds drawable resources
     */
    private void unbindDrawableResources() {
        if (null != mBitmapCache) {
            for (int i = 0; i < mBitmapCache.size(); i++) {
                Bitmap bitmap = mBitmapCache.valueAt(i);
                bitmap.recycle();
            }
        }
    }

    private void createImageBitmap(Integer key, ViewGroup.LayoutParams layoutParams) {
        if (mBitmapCache.get(key) == null) {
            mBitmapCache.put(key, ImageUtils.decodeBitmapFromResource(getResources(), key,
                    layoutParams.width, layoutParams.height));
        }
    }


}
