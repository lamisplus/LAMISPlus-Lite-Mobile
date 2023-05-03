package org.lamisplus.datafi.activities.patientprofile;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestActivity;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.BiometricsClass;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.OrganizationUnitDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Biometrics;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.BiometricsUtil;
import org.lamisplus.datafi.utilities.FingerPositions;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import SecuGen.FDxSDKPro.SGImpressionType;

public class PatientProfileFragment extends LamisBaseFragment<PatientProfileContract.Presenter> implements PatientProfileContract.View, View.OnClickListener {

    private View root;

    private TextView your_profile;
    private TextView tvPatientName;
    private TextView tvHospitalNumber;
    private TextView tvGender;
    private TextView tvDateOfBirth;
    private TextView tvMaritalStatus;
    private TextView tvEducationLevel;
    private TextView tvPhoneNumber;
    private TextView tvCountry;
    private TextView tvState;
    private TextView tvLGA;
    private TextView tvStreet;

    private Button btn_Dashboard;

    private Button btn_EditProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        if (root != null) {
            setHasOptionsMenu(true);
            initiateFragmentViews(root);
            fillFields(root);
            setListeners();
        }
        return root;
    }

    public static PatientProfileFragment newInstance() {
        return new PatientProfileFragment();
    }

    public void setListeners() {
        btn_Dashboard.setOnClickListener(this);
        btn_EditProfile.setOnClickListener(this);
    }

    public void initiateFragmentViews(View root) {
        tvPatientName = root.findViewById(R.id.tvPatientName);
        tvHospitalNumber = root.findViewById(R.id.tvHospitalNumber);
        tvGender = root.findViewById(R.id.tvGender);
        tvDateOfBirth = root.findViewById(R.id.tvDateOfBirth);
        tvMaritalStatus = root.findViewById(R.id.tvMaritalStatus);
        tvEducationLevel = root.findViewById(R.id.tvEducationLevel);
        tvPhoneNumber = root.findViewById(R.id.tvPhoneNumber);
        tvCountry = root.findViewById(R.id.tvCountry);
        tvState = root.findViewById(R.id.tvState);
        tvLGA = root.findViewById(R.id.tvLGA);
        tvStreet = root.findViewById(R.id.tvStreet);
        your_profile = root.findViewById(R.id.your_profile);

        btn_Dashboard = root.findViewById(R.id.btnPatientDashboard);
        btn_EditProfile = root.findViewById(R.id.btn_edit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPatientDashboard:
                Intent patientDashboard = new Intent(LamisPlus.getInstance(), PatientDashboardActivity.class);
                patientDashboard.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                        String.valueOf(mPresenter.getPatientId()));
                startActivity(patientDashboard);
                break;
            case R.id.btn_edit:
                Intent editPatient = new Intent(LamisPlus.getInstance(), AddEditPatientActivity.class);
                editPatient.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE,
                        String.valueOf(mPresenter.getPatientId()));
                startActivity(editPatient);
                break;
        }
    }

    private void fillFields(View root) {
        Person person = PersonDAO.findPersonById(mPresenter.getPatientId());
        if (person != null) {
            your_profile.setText(person.getFirstName() + "'s Profile");

            tvPatientName.setText(person.getFirstName() + " " + person.getOtherName() + " " + person.getSurname());

            if(person.getIdentifiers().getValue() != null) {
                tvHospitalNumber.setText(person.getIdentifiers().getValue());
            }

            if(person.getGenderId() != null) {
                if (("Male").equals(CodesetsDAO.findCodesetsDisplayById(person.getGenderId()))) {
                    tvGender.setText("Male");
                    ((ImageView) root.findViewById(R.id.genderImage)).setImageResource(R.mipmap.ic_male);
                } else {
                    tvGender.setText("Female");
                    ((ImageView) root.findViewById(R.id.genderImage)).setImageResource(R.mipmap.ic_female);
                }
            }

            if (person.getDateOfBirth() != null) {
                tvDateOfBirth.setText(person.getDateOfBirth());
            }
            if (person.getMaritalStatusId() != null) {
                tvMaritalStatus.setText(CodesetsDAO.findCodesetsDisplayById(person.getMaritalStatusId()));
            }
            if (person.getEducationId() != null) {
                tvEducationLevel.setText(CodesetsDAO.findCodesetsDisplayById(person.getEducationId()));
            }
            if (person.pullContactPointList() != null) {
                tvPhoneNumber.setText(person.pullContactPointList().get(0).getValue());
            }
            if (person.getAddresses().getCountryId() != null) {
                tvCountry.setText(OrganizationUnitDAO.findOrganizationUnitNameById(person.getAddresses().getCountryId()));
            }
            if (person.getAddresses().getStateId() != null) {
                tvState.setText(OrganizationUnitDAO.findOrganizationUnitNameById(person.getAddresses().getStateId()));
            }
            if (person.getAddresses().getDistrict() != null) {
                tvLGA.setText(person.getAddresses().getDistrict());
            }
            if (person.getAddresses().getCity() != null) {
                tvStreet.setText(person.getAddresses().getCity());
            }
        }
    }
}
