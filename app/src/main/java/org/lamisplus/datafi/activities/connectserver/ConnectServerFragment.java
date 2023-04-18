package org.lamisplus.datafi.activities.connectserver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.activities.connectserver.findpatientserver.FindPatientServerActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.activities.hts.htsprogram.HTSProgramActivity;
import org.lamisplus.datafi.activities.pmtct.pmtctprogram.PMTCTProgramActivity;
import org.lamisplus.datafi.activities.preferences.PrefrencesActivity;
import org.lamisplus.datafi.activities.syncstatus.SyncStatusActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ImageUtils;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class ConnectServerFragment extends LamisBaseFragment<ConnectServerContract.Presenter> implements ConnectServerContract.View, View.OnClickListener, SearchView.OnQueryTextListener {

    private SparseArray<Bitmap> mBitmapCache;
    private ImageView mFindPatientServerButton;
    private ImageView mPMTCTButton;

    private LinearLayout mFindPatientServerView;
    private LinearLayout mPMTCTView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_connect_server, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
        }
        return root;
    }

    public static ConnectServerFragment newInstance() {
        return new ConnectServerFragment();
    }

    private void initiateFragmentViews(View root) {
        mFindPatientServerButton = root.findViewById(R.id.mFindPatientServerButton);
        mPMTCTButton = root.findViewById(R.id.mPMTCTButton);

        mFindPatientServerView = root.findViewById(R.id.mFindPatientServerView);
        mPMTCTView = root.findViewById(R.id.mPMTCTView);
    }

    private void setListeners() {
        mFindPatientServerView.setOnClickListener(this);
        mPMTCTView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mFindPatientServerView:
                startNewActivity(FindPatientServerActivity.class);
                break;
            case R.id.mPMTCTView:
                startNewActivity(PMTCTProgramActivity.class);
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
        bindDrawableResource(mFindPatientServerButton, R.drawable.hts);
        bindDrawableResource(mPMTCTButton, R.drawable.pmtct);
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
