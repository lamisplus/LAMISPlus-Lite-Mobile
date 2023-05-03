package org.lamisplus.datafi.activities.biometricsselect;

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

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.biometrics.BiometricsActivity;
import org.lamisplus.datafi.activities.biometrics.neurotech.FingerActivity;
import org.lamisplus.datafi.activities.connectserver.findpatientserver.FindPatientServerActivity;
import org.lamisplus.datafi.activities.connectserver.nobiometricspatients.NoBiometricsPatientsServerActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ImageUtils;

public class BiometricsSelectFragment extends LamisBaseFragment<BiometricsSelectContract.Presenter> implements BiometricsSelectContract.View, View.OnClickListener, SearchView.OnQueryTextListener {

    private SparseArray<Bitmap> mBitmapCache;
    private ImageView mSecugenButton;
    private ImageView mFutronicButton;

    private LinearLayout mSecugenView;
    private LinearLayout mFutronicView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_biometrics_select, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
        }
        return root;
    }

    public static BiometricsSelectFragment newInstance() {
        return new BiometricsSelectFragment();
    }

    private void initiateFragmentViews(View root) {
        mSecugenButton = root.findViewById(R.id.mSecugenButton);
        mFutronicButton = root.findViewById(R.id.mFutronicButton);

        mSecugenView = root.findViewById(R.id.mSecugenView);
        mFutronicView = root.findViewById(R.id.mFutronicView);
    }

    private void setListeners() {
        mSecugenView.setOnClickListener(this);
        mFutronicView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mSecugenView:
                Intent secugenProgram = new Intent(getContext(), BiometricsActivity.class);
                secugenProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                        String.valueOf(mPresenter.getPatientId()));
                startActivity(secugenProgram);
                break;
            case R.id.mFutronicView:
                Intent futronicProgram = new Intent(getContext(), FingerActivity.class);
                futronicProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                        String.valueOf(mPresenter.getPatientId()));
                startActivity(futronicProgram);
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
        bindDrawableResource(mSecugenButton, R.drawable.secugen_logo);
        bindDrawableResource(mFutronicButton, R.drawable.futronic_logo);
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
