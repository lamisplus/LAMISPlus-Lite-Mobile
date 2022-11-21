package org.lamisplus.datafi.activities.patientprogram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.activities.formdisplay.FormDisplayActivity;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeActivity;
import org.lamisplus.datafi.activities.forms.hts.posttest.PostTestActivity;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestActivity;
import org.lamisplus.datafi.activities.forms.hts.recency.RecencyActivity;
import org.lamisplus.datafi.activities.forms.hts.requestresult.RequestResultActivity;
import org.lamisplus.datafi.activities.forms.hts.rst.RSTActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ImageUtils;

public class PatientProgramFragment extends LamisBaseFragment<PatientProgramContract.Presenter> implements PatientProgramContract.View, View.OnClickListener {

    private ImageView mhtsButton;
    private ImageView mtreatmentButton;
    private ImageView mpmtctButton;
    private RelativeLayout mhtsView;
    private RelativeLayout mtreatmentView;
    private RelativeLayout mpmtctView;

    private SparseArray<Bitmap> mBitmapCache;
    private String patientID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_program, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
        }
        return root;
    }

    public static PatientProgramFragment newInstance() {
        return new PatientProgramFragment();
    }

    private void initiateFragmentViews(View root) {
        mhtsButton = root.findViewById(R.id.htsButton);
        mhtsView = root.findViewById(R.id.htsView);
        mtreatmentButton = root.findViewById(R.id.treatmentButton);
        mtreatmentView = root.findViewById(R.id.treatmentView);
        mpmtctButton = root.findViewById(R.id.pmtctButton);
        mpmtctView = root.findViewById(R.id.pmtctView);
    }

    private void setListeners() {
        mhtsView.setOnClickListener(this);
        mtreatmentView.setOnClickListener(this);
        mpmtctView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.htsView:
                //These checks if a form has been entered and if it has then display the next form till completed and the dashboard is shown
                Encounter encounterRst = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, mPresenter.getPatientId());
                if (encounterRst == null) {
                    Intent htsProgram = new Intent(getContext(), RSTActivity.class);
                    htsProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                            String.valueOf(mPresenter.getPatientId()));
                    startActivity(htsProgram);
                } else {
                    Encounter encounterClientIntake = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, mPresenter.getPatientId());
                    if(encounterClientIntake == null){
                        Intent clientIntakeProgram = new Intent(getContext(), ClientIntakeActivity.class);
                        clientIntakeProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                                String.valueOf(mPresenter.getPatientId()));
                        startActivity(clientIntakeProgram);
                    }else {
                        Encounter encounterPretest = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM, mPresenter.getPatientId());
                        if(encounterPretest == null){
                            Intent pretestProgram = new Intent(getContext(), PreTestActivity.class);
                            pretestProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                                    String.valueOf(mPresenter.getPatientId()));
                            startActivity(pretestProgram);
                        }else {
                            Encounter encounterRequestForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.REQUEST_RESULT_FORM, mPresenter.getPatientId());
                            if(encounterRequestForm == null) {
                                Intent requestResultProgram = new Intent(getContext(), RequestResultActivity.class);
                                requestResultProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                                        String.valueOf(mPresenter.getPatientId()));
                                startActivity(requestResultProgram);
                            }else{
                                Encounter postTestForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM, mPresenter.getPatientId());
                                if(postTestForm == null) {
                                    Intent postTestProgram = new Intent(getContext(), PostTestActivity.class);
                                    postTestProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                                            String.valueOf(mPresenter.getPatientId()));
                                    startActivity(postTestProgram);
                                }else{
                                    Encounter recencyTestingForm = EncounterDAO.findFormByPatient(ApplicationConstants.Forms.HIV_RECENCY_FORM, mPresenter.getPatientId());
                                    if(recencyTestingForm == null) {
                                        Intent recencyProgram = new Intent(getContext(), RecencyActivity.class);
                                        recencyProgram.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                                                String.valueOf(mPresenter.getPatientId()));
                                        startActivity(recencyProgram);
                                    }else{
                                        Intent patientDashboard = new Intent(getContext(), PatientDashboardActivity.class);
                                        patientDashboard.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                                                String.valueOf(mPresenter.getPatientId()));
                                        startActivity(patientDashboard);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.treatmentView:
                startNewActivity(DashboardActivity.class);
                break;
            case R.id.pmtctView:

                break;
            default:
                startNewActivity(FormDisplayActivity.class);
                Log.v("Baron", "Nothings was clicked " + view.getId());
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
        bindDrawableResource(mhtsButton, R.drawable.hts);
        bindDrawableResource(mtreatmentButton, R.drawable.art);
        bindDrawableResource(mpmtctButton, R.drawable.pmtct);
    }

    @Override
    public void setPatientId(String id) {

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
