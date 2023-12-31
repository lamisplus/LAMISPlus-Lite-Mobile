package org.lamisplus.datafi.activities.hepatitis;

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
import org.lamisplus.datafi.activities.connectserver.findpatientserver.FindPatientServerActivity;
import org.lamisplus.datafi.activities.connectserver.nobiometricspatients.NoBiometricsPatientsServerActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.activities.forms.hepatitis.enrolfollowupcard1.EnrolmentFollowUpCard1Activity;
import org.lamisplus.datafi.activities.forms.hepatitis.enrolfollowupcard2.EnrolmentFollowUpCard2Activity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ImageUtils;

public class HepatitisFragment extends LamisBaseFragment<HepatitisContract.Presenter> implements HepatitisContract.View, View.OnClickListener, SearchView.OnQueryTextListener {

    private SparseArray<Bitmap> mBitmapCache;
    private ImageView mEnrolFollowUpCard1Button;
    private ImageView mEnrolFollowUpCard2Button;

    private LinearLayout mEnrolFollowUpCard1View;
    private LinearLayout mEnrolFollowUpCard2View;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hepatitis, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
        }
        return root;
    }

    public static HepatitisFragment newInstance() {
        return new HepatitisFragment();
    }

    private void initiateFragmentViews(View root) {
        mEnrolFollowUpCard1Button = root.findViewById(R.id.mEnrolFollowUpCard1Button);
        mEnrolFollowUpCard2Button = root.findViewById(R.id.mEnrolFollowUpCard2Button);

        mEnrolFollowUpCard1View = root.findViewById(R.id.mEnrolFollowUpCard1View);
        mEnrolFollowUpCard2View = root.findViewById(R.id.mEnrolFollowUpCard2View);
    }

    private void setListeners() {
        mEnrolFollowUpCard1View.setOnClickListener(this);
        mEnrolFollowUpCard2View.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mEnrolFollowUpCard1View:
                startNewActivity(EnrolmentFollowUpCard1Activity.class);
                break;
            case R.id.mEnrolFollowUpCard2View:
                startNewActivity(EnrolmentFollowUpCard2Activity.class);
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
        bindDrawableResource(mEnrolFollowUpCard1Button, R.drawable.ic_form);
        bindDrawableResource(mEnrolFollowUpCard2Button, R.drawable.ic_form);
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
