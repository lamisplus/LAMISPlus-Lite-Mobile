package org.lamisplus.datafi.activities.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.hts.htsprogram.HTSProgramActivity;
import org.lamisplus.datafi.activities.login.LoginActivity;
import org.lamisplus.datafi.activities.preferences.PrefrencesActivity;
import org.lamisplus.datafi.activities.syncstatus.SyncStatusActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.auth.AuthorizationManager;
import org.lamisplus.datafi.dao.AccountDAO;
import org.lamisplus.datafi.databases.LamisPlusDBOpenHelper;
import org.lamisplus.datafi.models.Account;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ImageUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class DashboardFragment extends LamisBaseFragment<DashboardContract.Presenter> implements DashboardContract.View, View.OnClickListener, SearchView.OnQueryTextListener {

    private SparseArray<Bitmap> mBitmapCache;
    private ImageView mHTSButton;
    private ImageView mPMTCTButton;
    private ImageView mAllClientsButton;
    private ImageView mSyncStatusButton;

    private LinearLayout mHTSView;
    private LinearLayout mPMTCTView;
    private LinearLayout mAllClientsView;
    private LinearLayout mSyncStatusView;

    AnimatedBottomBar animatedBottomBar;

    private SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            setFacilityName();
        }
        return root;
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    private void initiateFragmentViews(View root) {
        mHTSButton = root.findViewById(R.id.mHTSButton);
        mPMTCTButton = root.findViewById(R.id.mPMTCTButton);
        mAllClientsButton = root.findViewById(R.id.mAllClientsButton);
        mSyncStatusButton = root.findViewById(R.id.mSyncStatusButton);

        mHTSView = root.findViewById(R.id.mHTSView);
        mPMTCTView = root.findViewById(R.id.mPMTCTView);
        mAllClientsView = root.findViewById(R.id.mAllClientsView);
        mSyncStatusView = root.findViewById(R.id.mSyncStatusView);
        /**
         * Menu Bottom Navigation Drawer
         * */
        animatedBottomBar = root.findViewById(R.id.navigation);

        animatedBottomBar.setOnTabSelectListener((lastIndex, lastTab, newIndex, newTab) -> {
            Fragment fragment = null;
            switch (newTab.getId()) {
                case R.id.nav_menu_home:
                    startNewActivity(DashboardActivity.class);
                    break;
                case R.id.nav_menu_settings:
                    startNewActivity(PrefrencesActivity.class);
                    break;
                case R.id.nav_menu_createpatient:
                    startNewActivity(AddEditPatientActivity.class);
                    break;
            }

        });

        searchView = (SearchView) root.findViewById(R.id.search_recipe);
        searchView.setOnQueryTextListener(this);
    }

    private void setListeners() {
        mHTSView.setOnClickListener(this);
        mPMTCTView.setOnClickListener(this);
        mAllClientsView.setOnClickListener(this);
        mSyncStatusView.setOnClickListener(this);
    }

    private void setFacilityName() {
//        Account account = AccountDAO.getUserDetails();
//        if (account != null) {
//            facilityName.setVisibility(View.VISIBLE);
//            facilityName.setText(account.getCurrentOrganisationUnitName());
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mHTSView:
                startNewActivity(HTSProgramActivity.class);
                break;
            case R.id.pmtctView:
                //startNewActivity(PMTCT.class);
                break;
            case R.id.mAllClientsView:
                startNewActivity(FindPatientActivity.class);
                break;
            case R.id.mSyncStatusView:
                startNewActivity(SyncStatusActivity.class);
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
        bindDrawableResource(mHTSButton, R.drawable.hts);
        bindDrawableResource(mPMTCTButton, R.drawable.pmtct);
        bindDrawableResource(mAllClientsButton, R.drawable.all_patients);
        bindDrawableResource(mSyncStatusButton, R.drawable.sync_status);
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


    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent searchQuery = new Intent(LamisPlus.getInstance(), FindPatientActivity.class);
        searchQuery.putExtra(ApplicationConstants.BundleKeys.PATIENT_QUERY_BUNDLE, query);
        startActivity(searchQuery);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
