package org.lamisplus.datafi.activities.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.utilities.ImageUtils;

public class AppFragment extends LamisBaseFragment<AppContract.Presenter> implements AppContract.View, View.OnClickListener {

    private ImageView mCreatePatientButton;
    private ImageView mFindPatientButton;
    private ImageView mSyncedStatusButton;
    private ImageView mComodityManagementButton;
    private RelativeLayout mCreatePatientView;
    private RelativeLayout mFindPatientView;
    private RelativeLayout mSyncedStatusView;
    private RelativeLayout mCommodityManagementView;

    private SparseArray<Bitmap> mBitmapCache;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_app, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
        }
        return root;
    }

    public static AppFragment newInstance() {
        return new AppFragment();
    }

    private void initiateFragmentViews(View root) {
        mCreatePatientButton = root.findViewById(R.id.createPatientButton);
        mCreatePatientView = root.findViewById(R.id.createPatientView);
        mFindPatientButton = root.findViewById(R.id.findPatientButton);
        mFindPatientView = root.findViewById(R.id.findPatientView);
        mSyncedStatusButton = root.findViewById(R.id.syncedStatusButton);
        mSyncedStatusView = root.findViewById(R.id.syncedStatusView);
        mComodityManagementButton = root.findViewById(R.id.commodityManagementButton);
        mCommodityManagementView = root.findViewById(R.id.commodityManagementView);
    }

    private void setListeners() {
        mCreatePatientView.setOnClickListener(this);
        mFindPatientView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createPatientView:
                startNewActivity(AddEditPatientActivity.class);
                break;
            case R.id.findPatientView:
                startNewActivity(FindPatientActivity.class);
                break;
            default:

                break;
        }
    }

//    private void startNewActivity(Class<? extends LamisBaseActivity> cls) {
//        Intent intent = new Intent(getActivity(), cls);
//        startActivity(intent);
//    }

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
        bindDrawableResource(mCreatePatientButton, R.drawable.create_user);
        bindDrawableResource(mFindPatientButton, R.drawable.search_icon);
        bindDrawableResource(mSyncedStatusButton, R.drawable.synced_status);
        bindDrawableResource(mComodityManagementButton, R.drawable.commodity_basket);
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
