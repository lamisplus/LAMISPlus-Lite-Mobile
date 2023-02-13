package org.lamisplus.datafi.activities.biometrics;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.activities.dashboard.DashboardFragment;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.BiometricsClass;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Biometrics;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.BiometricsUtil;
import org.lamisplus.datafi.utilities.FingerPositions;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.URLValidator;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxConstant;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import SecuGen.FDxSDKPro.SGImpressionType;

public class BiometricsFragment extends LamisBaseFragment<BiometricsContract.Presenter> implements BiometricsContract.View, View.OnClickListener{

    private View root;

    private static final String TAG = "SecuGen USB";
    private static final int IMAGE_CAPTURE_TIMEOUT_MS = 10000;
    private static final int IMAGE_CAPTURE_QUALITY = 60; //The default value here is 50 so i changed it to 60

    private Button mButtonRegister;
    private Button mButtonSaveCapture;
    private Button mButtonClearUnsyncFingerPrint;

    private PendingIntent mPermissionIntent;
    private ImageView mImageViewFingerprint;

    private byte[] mRegisterImage;
    private byte[] mRegisterTemplate;
    private int[] mMaxTemplateSize;
    private int mImageWidth;
    private int mImageHeight;
    private int mImageDPI;
    private String mDeviceSN;
    private Bitmap grayBitmap;
    private IntentFilter filter;
    private boolean bSecuGenDeviceOpened;
    private JSGFPLib sgfplib;
    private boolean usbPermissionRequested;
    private FingerPositions fingerPosition = null;
    private ImageView fingerPrintImageDisplay;
    private Button fingerLeftThumb, fingerLeftIndex, fingerLeftMiddle, fingerLeftRing, fingerLeftPinky, fingerRightThumb, fingerRightIndex, fingerRightMiddle, fingerRightRing, fingerRightPinky;
    private List<Biometrics> patientFingerPrints;

    String patientId = "";
    String patientUUID = "";
    BiometricsDAO fingerPrintDAO;
    BiometricsUtil biometricsUtil;

    int fingerPrintCaptureCount = 0;

    private int[] grayBuffer;
    private SGAutoOnEventNotifier autoOn;
    private boolean mAutoOnEnabled;
    private int nCaptureModeN;
    private int[] mNumFakeThresholds;
    private int[] mDefaultFakeThreshold;
    private boolean[] mFakeEngineReady;
    private int mFakeDetectionLevel = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_biometrics, container, false);
        if (root != null) {
            setHasOptionsMenu(true);

            if (savedInstanceState != null) {
                patientId = savedInstanceState.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
            } else {
                savedInstanceState = getActivity().getIntent().getExtras();
            }

            if (savedInstanceState != null) {
                patientId = savedInstanceState.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
            }

            createViewObject(root);

//            if (patientUUID != null) {
//                CheckIfAlreadyCapturedOnServer(patientId);
//            }
            CheckIfAlreadyCapturedOnLocalDB(patientId);

        }
        return root;
    }

    public static BiometricsFragment newInstance() {
        return new BiometricsFragment();
    }

    public void createViewObject(View root) {
        //Selecting Different Fingers
        //Declare the variables for the different fingers
        fingerLeftThumb = root.findViewById(R.id.fingerLeftThumb);
        fingerLeftIndex =  root.findViewById(R.id.fingerLeftIndex);
        fingerLeftMiddle = root.findViewById(R.id.fingerLeftMiddle);
        fingerLeftRing =  root.findViewById(R.id.fingerLeftRing);
        fingerLeftPinky =  root.findViewById(R.id.fingerLeftPinky);
        fingerRightThumb =  root.findViewById(R.id.fingerRightThumb);
        fingerRightIndex =  root.findViewById(R.id.fingerRightIndex);
        fingerRightMiddle =  root.findViewById(R.id.fingerRightMiddle);
        fingerRightRing =  root.findViewById(R.id.fingerRightRing);
        fingerRightPinky =  root.findViewById(R.id.fingerRightPinky);

        //Set onclick listener
        fingerLeftThumb.setOnClickListener(this);
        fingerLeftIndex.setOnClickListener(this);
        fingerLeftMiddle.setOnClickListener(this);
        fingerLeftRing.setOnClickListener(this);
        fingerLeftPinky.setOnClickListener(this);
        fingerRightThumb.setOnClickListener(this);
        fingerRightIndex.setOnClickListener(this);
        fingerRightMiddle.setOnClickListener(this);
        fingerRightRing.setOnClickListener(this);
        fingerRightPinky.setOnClickListener(this);

        //Changing selected Image View
        fingerPrintImageDisplay = (ImageView) root.findViewById(R.id.fingerPrintImage);

        mButtonRegister = (Button) root.findViewById(R.id.buttonRegister);
        mButtonRegister.setOnClickListener(this);
        mButtonSaveCapture = (Button) root.findViewById(R.id.btnSavePrints);
        mButtonSaveCapture.setOnClickListener(this);

        mButtonClearUnsyncFingerPrint = (Button) root.findViewById(R.id.buttonClearUnsyncFingerPrint);
        mButtonClearUnsyncFingerPrint.setOnClickListener(this);
        //mButtonClearUnsyncFingerPrint.setVisibility(View.GONE);

        mImageViewFingerprint = (ImageView) root.findViewById(R.id.imageViewFingerprint);

        mNumFakeThresholds = new int[1];
        mDefaultFakeThreshold = new int[1];
        mFakeEngineReady =new boolean[1];



        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES* JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i=0; i<grayBuffer.length; ++i)
            grayBuffer[i] = Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES);
        mImageViewFingerprint.setImageBitmap(grayBitmap);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2)*(JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2)];
        for (int i=0; i<sintbuffer.length; ++i)
            sintbuffer[i] = Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2);
//            mImageViewRegister.setImageBitmap(grayBitmap);
//            mImageViewVerify.setImageBitmap(grayBitmap);
        mMaxTemplateSize = new int[1];



        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        sgfplib = new JSGFPLib(getContext(), (UsbManager)getContext().getSystemService(Context.USB_SERVICE));
        bSecuGenDeviceOpened = false;
        usbPermissionRequested = false;

        debugMessage("Starting Activity\n");
        debugMessage("JSGFPLib version: " + sgfplib.GetJSGFPLibVersion() + "\n");
        mAutoOnEnabled = false;
        //autoOn = new SGAutoOnEventNotifier(sgfplib, getContext());
        nCaptureModeN = 0;
        Log.d(TAG, "Exit onCreate()");

    }

    @Override
    public void onClick(View view) {
        if(view == mButtonSaveCapture){
            saveFingerPrints();
        } else if (view == this.mButtonRegister) {
            ToastUtil.success("Clicked on me");
            CapturePrint();
        } else if (view == this.mButtonClearUnsyncFingerPrint){
            deleteUnsyncedFingerPrint(Long.parseLong(patientId));
        }
        else {
            setViewItem(view);
        }
    }

    public void CapturePrint() {

        if (fingerPosition == null) {
            CustomDebug("Please select the finger position before capturing", false);
        } else {
            //DEBUG Log.d(TAG, "Clicked REGISTER");
            debugMessage("Clicked REGISTER\n");
            if (mRegisterImage != null)
                mRegisterImage = null;

            mRegisterImage = new byte[mImageWidth * mImageHeight];

            //long result = sgfplib.GetImage(mRegisterImage);
            long result = sgfplib.GetImageEx(mRegisterImage, IMAGE_CAPTURE_TIMEOUT_MS,IMAGE_CAPTURE_QUALITY);
            debugMessage("GetImage() returned:" + result);

            if (result != 0) {
                String errorMsg = biometricsUtil.getDeviceErrors((int) result);
                CustomDebug(errorMsg, false);
                return;
            }

            //mImageViewFingerprint.setImageBitmap(toGrayscale(mRegisterImage));
            mImageViewFingerprint.setImageBitmap(this.toGrayscale(mRegisterImage));
            result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);

            debugMessage("SetTemplateFormat() returned:" + result + "\n");

            int[] quality1 = new int[1];
            result = sgfplib.GetImageQuality(mImageWidth, mImageHeight, mRegisterImage, quality1);
            debugMessage("GetImageQuality() ret:" + result + "quality [" + quality1[0] + "]\n");

            SGFingerInfo fpInfo = new SGFingerInfo();
            fpInfo.FingerNumber = 1;
            fpInfo.ImageQuality = quality1[0];
            fpInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fpInfo.ViewNumber = 1;

            //Arrays.fill(mRegisterTemplate, (byte) 0);

            result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate);
            debugMessage("GetImageQuality() ret:" + result + "\n");

            //Save fingerprint
            boolean isGoodQuality = checkForQuality(fpInfo.ImageQuality);

            int[] size = new int[1];
            result = sgfplib.GetTemplateSize(mRegisterTemplate, size);
            debugMessage("GetTemplateSize() ret:" + result + " size [" + size[0] + "]\n");

            String template = Base64.encodeToString(mRegisterTemplate, Base64.DEFAULT);

            if (template != null && isGoodQuality) {
//
//                //add to dictionary
//                Biometrics theFinger = new Biometrics();
//
//                theFinger.setImageHeight(mImageHeight);
//                theFinger.setImageWidth(mImageWidth);
//                theFinger.setFingerPositions(fingerPosition);
//                theFinger.setCreator(1);
//                theFinger.setImageQuality(fpInfo.ImageQuality);
//                theFinger.setPatienId(Integer.parseInt(patientId));
//
//                theFinger.setTemplate(Base64.encodeToString(mRegisterTemplate, Base64.NO_WRAP));
//                theFinger.setImageDPI(mImageDPI);
//                theFinger.setSerialNumber(mDeviceSN);
//                theFinger.setImageByte(mRegisterTemplate);
//                theFinger.setSyncStatus(0);
//
//
//                //reject finger print if already capture for another finger. Accept and replace if this is the same finger
//                String previousCapture = biometricsUtil.CheckIfFingerAlreadyCaptured(theFinger.getTemplate(), patientFingerPrints);
//                if (previousCapture !=null && !previousCapture.isEmpty()) {
//                    CustomDebug("This finger has been captured before for "+ previousCapture, false);
//                } else {
//                    //save to temp list to be discard later
//                    patientFingerPrints.add(theFinger);
//
//                    //save to the database directly
//                    Long db_id = fingerPrintDAO.saveFingerPrint(theFinger);
//                    debugMessage(String.valueOf(db_id));
//                    fingerPrintCaptureCount += 1;
//
//                    //color the button and save locally
//                    colorCapturedButton(fingerPosition, android.R.color.holo_green_light, Typeface.BOLD);
//                }
            } else {
                //color warning. this capture is not counted
                colorCapturedButton(fingerPosition, android.R.color.holo_orange_light, Typeface.NORMAL);
            }
            //enable the save button when 6 fingers has been captured
            if (fingerPrintCaptureCount >= 6) {
                this.mButtonSaveCapture.setClickable(true);
                this.mButtonSaveCapture.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
        }
        mRegisterImage = null;
    }

    public void deleteUnsyncedFingerPrint(long patientId) {
        BiometricsDAO dao = new BiometricsDAO();
        dao.deletePrint(patientId);

        colorCapturedButton(FingerPositions.RightSmall, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.RightWedding, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.RightMiddle, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.RightIndex, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.RightThumb, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.LeftMiddle, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.LeftIndex, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.LeftSmall, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.LeftWedding, android.R.color.black, Typeface.NORMAL);
        colorCapturedButton(FingerPositions.LeftThumb, android.R.color.black, Typeface.NORMAL);

        patientFingerPrints = new ArrayList<>();
        CustomDebug("Fingerprints successfully cleared", false);
    }

    public void CheckIfAlreadyCapturedOnLocalDB(String patientId) {
        BiometricsDAO dao = new BiometricsDAO();
        List<Biometrics> pbs = dao.getAll(false, patientId);
        if(pbs !=null && pbs.size() > 0){
            CustomDebug("Some Finger Print already exit for this patient. You can capture more or clear the existing ones to start afresh", false);
            fingerPrintCaptureCount = pbs.size();
            //load in temp list
            patientFingerPrints.addAll(pbs);

            for (Biometrics item : pbs) {
                colorCapturedButton(item.getFingerPositions(), android.R.color.holo_green_light, Typeface.NORMAL);
            }
        }
        else{ //check if already sync
            pbs = dao.getAll(true, patientId);
            if(pbs !=null && pbs.size() > 0){
                CustomDebug("Finger print has been captured for this patient", true);
            }
        }
    }

    private boolean checkForQuality(int imageQuality) {
        if(imageQuality < IMAGE_CAPTURE_QUALITY){
            CustomDebug("Please re-capture this finger. The quality is low ("+imageQuality+" %).", false);
            return false;
        }
        return true;
    }

    private void saveFingerPrints() {

        //FingerPrintSyncService sync = new FingerPrintSyncService();
        ////sync.autoSyncFingerPrint();
        try {
            if (fingerPrintCaptureCount < 6) {
                CustomDebug("Please captured a minimum of 6 print before saving", false);
                return;
            }

            BiometricsDAO dao = new BiometricsDAO();

//        PatientBiometricDTO dto = new PatientBiometricDTO();
//        dto.setFingerPrintList(new ArrayList<>(patientFingerPrints.values()));


            if (NetworkUtils.isOnline() && NetworkUtils.hasNetwork() && patientUUID != null) {

                List<Biometrics> pbs = dao.getAll(false, patientId);
                BiometricsClass dto = new BiometricsClass();
                dto.setFingerPrintList(new ArrayList<>(pbs));
                dto.setPatientUUID(patientUUID);

//                new FingerPrintSyncService().startSync(dto, new GenericResponseCallbackListener<PatientBiometricSyncResponseModel>() {
//                    @Override
//                    public void onResponse(PatientBiometricSyncResponseModel obj) {
//                        if (obj != null && obj.getIsSuccessful()) {
//                            CustomDebug(obj.getErrorMessage(), false);
//
//                            Biometrics _temp = pbs.get(0);
//                            _temp.setSyncStatus(1);
//                            _temp.setTemplate("");
//                            dao.updatePatientFingerPrintSyncStatus(Long.valueOf(patientId), _temp);
//
//                            CustomDebug("Successfully saved to server.", true);
//                        }
//                    }
//
//                    @Override
//                    public void onErrorResponse(PatientBiometricSyncResponseModel errorMessage) {
//                        if (errorMessage != null) {
//                            CustomDebug(errorMessage.getErrorMessage(), false);
//                        }
//                        //already saved
//                        //dao.saveFingerPrint(dto.getFingerPrintList());
//                        CustomDebug("An error occurred while saving prints on the server.", true);
//                    }
//
//                    @Override
//                    public void onErrorResponse(String errorMessage) {
//                        CustomDebug(errorMessage, false);
//
//                        //save locally
//                        //already saved
//                        //dao.saveFingerPrint(dto.getFingerPrintList());
//                        CustomDebug("Finger Prints saved offline", true);
//                    }
//                });
            } else {
                //save locally
                //dao.saveFingerPrint(dto.getFingerPrintList()); --they are already saved
                CustomDebug("Saved offline", true);
            }
        } catch (Exception ex) {
            CustomDebug(ex.getMessage(), false);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //This broadcast receiver is necessary to get user permissions to access the attached USB device
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.d(TAG,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            //DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
                            debugMessage("USB BroadcastReceiver VID : " + device.getVendorId() + "\n");
                            debugMessage("USB BroadcastReceiver PID: " + device.getProductId() + "\n");
                        }
                        else
                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
                    }
                    else
                        Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //This message handler is used to access local resources not
    //accessible by SGFingerPresentCallback() because it is called by
    //a separate thread.

    private boolean bRegisterAutoOnMode;
    public Handler fingerDetectedHandler = new Handler(){
        // @Override
        public void handleMessage(Message msg) {
            //Handle the message
//            if (bRegisterAutoOnMode) {
//                bRegisterAutoOnMode = false;
//                RegisterFingerPrint();
//            }
//            else if (bVerifyAutoOnMode) {
//                bVerifyAutoOnMode = false;
//                VerifyFingerPrint();
//            }
//            else
//                CaptureFingerPrint();
//            if (mAutoOnEnabled) {
//                EnableControls();
//            }
        }
    };

    private void debugMessage(String message) {
        System.out.print(message);
    }
    public void colorCapturedButton(FingerPositions fingerPosition, int color, int typeface) {
//
        if (fingerPosition == FingerPositions.LeftThumb) {
            this.fingerLeftThumb.setTextColor(getResources().getColor(color));
            this.fingerLeftThumb.setTypeface(this.fingerLeftThumb.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.LeftIndex) {
            this.fingerLeftIndex.setTextColor(getResources().getColor(color));
            this.fingerLeftIndex.setTypeface(this.fingerLeftIndex.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.LeftMiddle) {
            this.fingerLeftMiddle.setTextColor(getResources().getColor(color));
            this.fingerLeftMiddle.setTypeface(this.fingerLeftMiddle.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.LeftWedding) {
            this.fingerLeftRing.setTextColor(getResources().getColor(color));
            this.fingerLeftRing.setTypeface(this.fingerLeftRing.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.LeftSmall) {
            this.fingerLeftPinky.setTextColor(getResources().getColor(color));
            this.fingerLeftPinky.setTypeface(this.fingerLeftPinky.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.RightThumb) {
            this.fingerRightThumb.setTextColor(getResources().getColor(color));
            this.fingerRightThumb.setTypeface(this.fingerRightThumb.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.RightIndex) {
            this.fingerRightIndex.setTextColor(getResources().getColor(color));
            this.fingerRightIndex.setTypeface(this.fingerRightIndex.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.RightMiddle) {
            this.fingerRightMiddle.setTextColor(getResources().getColor(color));
            this.fingerRightMiddle.setTypeface(this.fingerRightMiddle.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.RightWedding) {
            this.fingerRightRing.setTextColor(getResources().getColor(color));
            this.fingerRightRing.setTypeface(this.fingerRightRing.getTypeface(), typeface);
        } else if (fingerPosition == FingerPositions.RightSmall) {
            this.fingerRightPinky.setTextColor(getResources().getColor(color));
            this.fingerRightPinky.setTypeface(this.fingerRightPinky.getTypeface(), typeface);
        }
    }

    public void setViewItem(View v) {

        if (v == this.fingerLeftThumb) {
            fingerPosition = FingerPositions.LeftThumb;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_left_thumb);
        } else if (v == this.fingerLeftMiddle) {
            fingerPosition = FingerPositions.LeftMiddle;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_left_middle);
        } else if (v == this.fingerLeftIndex) {
            fingerPosition = FingerPositions.LeftIndex;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_left_index);
        } else if (v == this.fingerLeftRing) {
            fingerPosition = FingerPositions.LeftWedding;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_left_ring);
        } else if (v == this.fingerLeftPinky) {
            fingerPosition = FingerPositions.LeftSmall;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_left_pinky);
        } else if (v == this.fingerRightThumb) {
            fingerPosition = FingerPositions.RightThumb;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_right_thumb);
        } else if (v == this.fingerRightIndex) {
            fingerPosition = FingerPositions.RightIndex;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_right_index);
        } else if (v == this.fingerRightMiddle) {
            fingerPosition = FingerPositions.RightMiddle;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_right_middle);
        } else if (v == this.fingerRightRing) {
            fingerPosition = FingerPositions.RightWedding;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_right_ring);
        } else if (v == this.fingerRightPinky) {
            fingerPosition = FingerPositions.RightSmall;
            fingerPrintImageDisplay.setImageResource(R.drawable.finger_right_pinky);
        }
    }

    private void CustomDebug(String s, boolean finishOnOk) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LamisPlus.getInstance());
        dlgAlert.setMessage(s);
        dlgAlert.setTitle("Patient's Biometric Capture");
        dlgAlert.setPositiveButton("OK",
                (dialog, whichButton) -> {
                    if (finishOnOk) {
                        getActivity().finish();
                    }
                }
        );
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //Converts image to grayscale (NEW)
    public Bitmap toGrayscale(byte[] mImageBuffer, int width, int height)
    {
        byte[] Bits = new byte[mImageBuffer.length * 4];
        for (int i = 0; i < mImageBuffer.length; i++) {
            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
        }

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return bmpGrayscale;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //Converts image to grayscale (NEW)
    public Bitmap toGrayscale(byte[] mImageBuffer)
    {
        byte[] Bits = new byte[mImageBuffer.length * 4];
        for (int i = 0; i < mImageBuffer.length; i++) {
            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
        }

        Bitmap bmpGrayscale = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return bmpGrayscale;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //Converts image to grayscale (NEW)
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int y=0; y< height; ++y) {
            for (int x=0; x< width; ++x){
                int color = bmpOriginal.getPixel(x, y);
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;
                int gray = (r+g+b)/3;
                color = Color.rgb(gray, gray, gray);
                //color = Color.rgb(r/3, g/3, b/3);
                bmpGrayscale.setPixel(x, y, color);
            }
        }
        return bmpGrayscale;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //Converts image to binary (OLD)
    public Bitmap toBinary(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public void SGFingerPresentCallback (){
        autoOn.stop();
        fingerDetectedHandler.sendMessage(new Message());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onPause() {
        Log.d(TAG, "Enter onPause()");
        debugMessage("Enter onPause()\n");
        if (bSecuGenDeviceOpened)
        {
            autoOn.stop();
            sgfplib.CloseDevice();
            bSecuGenDeviceOpened = false;
        }
        requireActivity().unregisterReceiver(mUsbReceiver);
        mRegisterImage = null;
        mRegisterTemplate = null;
        mImageViewFingerprint.setImageBitmap(grayBitmap);
        super.onPause();
        Log.d(TAG, "Exit onPause()");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume(){
        Log.d(TAG, "Enter onResume()");
        debugMessage("Enter onResume()\n");
        super.onResume();
        requireActivity().registerReceiver(mUsbReceiver, filter);
        long error = sgfplib.Init( SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE){
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
            if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
                dlgAlert.setMessage("The attached fingerprint device is not supported on Android");
            else
                dlgAlert.setMessage("Fingerprint device initialization failed!");
            dlgAlert.setTitle("SecuGen Fingerprint SDK");
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int whichButton){
                            getActivity().finish();
                            return;
                        }
                    }
            );
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
        else {
            UsbDevice usbDevice = sgfplib.GetUsbDevice();
            if (usbDevice == null){
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
                dlgAlert.setMessage("SecuGen fingerprint sensor not found!");
                dlgAlert.setTitle("SecuGen Fingerprint SDK");
                dlgAlert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int whichButton){
                                getActivity().finish();
                                return;
                            }
                        }
                );
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            }
            else {
                boolean hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                if (!hasPermission) {
                    if (!usbPermissionRequested)
                    {
                        debugMessage("Requesting USB Permission\n");
                        //Log.d(TAG, "Call GetUsbManager().requestPermission()");
                        usbPermissionRequested = true;
                        sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
                    }
                    else
                    {
                        //wait up to 20 seconds for the system to grant USB permission
                        hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                        debugMessage("Waiting for USB Permission\n");
                        int i=0;
                        while ((hasPermission == false) && (i <= 40))
                        {
                            ++i;
                            hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //Log.d(TAG, "Waited " + i*50 + " milliseconds for USB permission");
                        }
                    }
                }
                if (hasPermission) {
                    debugMessage("Opening SecuGen Device\n");
                    error = sgfplib.OpenDevice(0);
                    debugMessage("OpenDevice() ret: " + error + "\n");
                    if (error == SGFDxErrorCode.SGFDX_ERROR_NONE)
                    {
                        bSecuGenDeviceOpened = true;
                        SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
                        error = sgfplib.GetDeviceInfo(deviceInfo);
                        debugMessage("GetDeviceInfo() ret: " + error + "\n");
                        mImageWidth = deviceInfo.imageWidth;
                        mImageHeight= deviceInfo.imageHeight;
                        mImageDPI = deviceInfo.imageDPI;
                        debugMessage("Image width: " + mImageWidth + "\n");
                        debugMessage("Image height: " + mImageHeight + "\n");
                        debugMessage("Image resolution: " + mImageDPI + "\n");
                        debugMessage("Serial Number: " + new String(deviceInfo.deviceSN()) + "\n");

                        error = sgfplib.FakeDetectionCheckEngineStatus(mFakeEngineReady);
                        debugMessage("Ret[" + error + "] Fake Engine Ready: " + mFakeEngineReady[0] + "\n");
                        if (mFakeEngineReady[0]) {
                            error = sgfplib.FakeDetectionGetNumberOfThresholds(mNumFakeThresholds);
                            debugMessage("Ret[" + error + "] Fake Thresholds: " + mNumFakeThresholds[0] + "\n");
                            if (error != SGFDxErrorCode.SGFDX_ERROR_NONE)
                                mNumFakeThresholds[0] = 1; //0=Off, 1=TouchChip
                            //this.mSeekBarFDLevel.setMax(mNumFakeThresholds[0]);

                            error = sgfplib.FakeDetectionGetDefaultThreshold(mDefaultFakeThreshold);
                            debugMessage("Ret[" + error + "] Default Fake Threshold: " + mDefaultFakeThreshold[0] + "\n");
                            //this.mTextViewFDLevel.setText("Fake Threshold (" + mDefaultFakeThreshold[0] + "/" + mNumFakeThresholds[0] + ")");
                            mFakeDetectionLevel = mDefaultFakeThreshold[0];

                            //error = this.sgfplib.SetFakeDetectionLevel(mFakeDetectionLevel);
                            //debugMessage("Ret[" + error + "] Set Fake Threshold: " + mFakeDetectionLevel + "\n");


                            double[] thresholdValue = new double[1];
                            error = sgfplib.FakeDetectionGetThresholdValue(thresholdValue);
                            debugMessage("Ret[" + error + "] Fake Threshold Value: " + thresholdValue[0] + "\n");
                        }
                        else {
                            mNumFakeThresholds[0] = 1;		//0=Off, 1=Touch Chip
                            mDefaultFakeThreshold[0] = 1; 	//Touch Chip Enabled
                            //this.mTextViewFDLevel.setText("Fake Threshold (" + mDefaultFakeThreshold[0] + "/" + mNumFakeThresholds[0] + ")");
                        }

                        sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
                        sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
                        debugMessage("TEMPLATE_FORMAT_ISO19794 SIZE: " + mMaxTemplateSize[0] + "\n");
                        mRegisterTemplate = new byte[(int)mMaxTemplateSize[0]];
                        //mVerifyTemplate = new byte[(int)mMaxTemplateSize[0]];
                        //EnableControls();
//                        boolean smartCaptureEnabled = this.mSwitchSmartCapture.isChecked();
//                        if (smartCaptureEnabled)
//                            sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte)1);
//                        else
//                            sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte)0);
                        if (mAutoOnEnabled){
                            autoOn.start();
                            //DisableControls();
                        }
                    }
                    else
                    {
                        debugMessage("Waiting for USB Permission\n");
                    }
                }
                //Thread thread = new Thread(this);
                //thread.start();
            }
        }
        Log.d(TAG, "Exit onResume()");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDestroy() {
        Log.d(TAG, "Enter onDestroy()");
        sgfplib.CloseDevice();
        mRegisterImage = null;
        //mVerifyImage = null;
        mRegisterTemplate = null;
        //mVerifyTemplate = null;
        sgfplib.Close();
        super.onDestroy();
        Log.d(TAG, "Exit onDestroy()");
    }



}
