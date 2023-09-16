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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.activities.dashboard.DashboardFragment;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.BiometricsClass;
import org.lamisplus.datafi.classes.ContactPointClass;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Biometrics;
import org.lamisplus.datafi.models.BiometricsList;
import org.lamisplus.datafi.models.BiometricsRecapture;
import org.lamisplus.datafi.models.Elicitation;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.BiometricsUtil;
import org.lamisplus.datafi.utilities.DateUtils;
import org.lamisplus.datafi.utilities.FingerPositions;
import org.lamisplus.datafi.utilities.HashUtil;
import org.lamisplus.datafi.utilities.ImageUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.URLValidator;
import org.lamisplus.datafi.utilities.ViewUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import SecuGen.Driver.Constant;
import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGANSITemplateInfo;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxConstant;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFPImageInfo;
import SecuGen.FDxSDKPro.SGFingerInfo;
import SecuGen.FDxSDKPro.SGFingerPresentEvent;
import SecuGen.FDxSDKPro.SGISOTemplateInfo;
import SecuGen.FDxSDKPro.SGImpressionType;
import SecuGen.FDxSDKPro.SGWSQLib;

public class BiometricsFragment extends LamisBaseFragment<BiometricsContract.Presenter> implements BiometricsContract.View, Runnable, SGFingerPresentEvent, View.OnClickListener {

    private View root;

    private static final String TAG = "SecuGen USB";
    private static final int IMAGE_CAPTURE_TIMEOUT_MS = 10000;
    private static final int IMAGE_CAPTURE_QUALITY = 60; //The default value here is 50 so i changed it to 60

    private SparseArray<Bitmap> mBitmapCache;
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
    private List<Biometrics> patientFingerPrints;
    private List<String> temporalBiometricsSave = new ArrayList<>();
    private AutoCompleteTextView autoFingerprintSelect;

    String patientId = "";
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
    private Biometrics biometrics;
    private FloatingActionButton fabBiometricsCapture;
    private TextInputLayout edReasonTIL;
    private EditText edReason;
    List<BiometricsClass.BiometricsClassFingers> biometricsClassFingers;

    private ImageView imgViewLeftThumbFinger, imgViewLeftIndexFinger, imgViewLeftMiddleFinger, imgViewLeftRingFinger, imgViewLeftLittleFinger, imgViewRightThumbFinger, imgViewRightIndexFinger, imgViewRightMiddleFinger, imgViewRightRingFinger, imgViewRightLittleFinger;
    private TextView qualityViewLeftThumbFinger, qualityViewLeftIndexFinger, qualityViewLeftMiddleFinger, qualityViewLeftRingFinger, qualityViewLeftLittleFinger, qualityViewRightThumbFinger, qualityViewRightIndexFinger, qualityViewRightMiddleFinger, qualityViewRightRingFinger, qualityViewRightLittleFinger;

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


            biometrics = new Biometrics();
            biometricsClassFingers = new ArrayList<>();

            createViewObject(root);
            showDropDowns();
            CheckIfAlreadyCapturedOnLocalDB();

        }
        return root;
    }

    public static BiometricsFragment newInstance() {
        return new BiometricsFragment();
    }

    public void createViewObject(View root) {
        //Selecting Different Fingers
        //Declare the variables for the different fingers
        imgViewLeftThumbFinger = root.findViewById(R.id.imgViewLeftThumbFinger);
        imgViewLeftIndexFinger = root.findViewById(R.id.imgViewLeftIndexFinger);
        imgViewLeftMiddleFinger = root.findViewById(R.id.imgViewLeftMiddleFinger);
        imgViewLeftRingFinger = root.findViewById(R.id.imgViewLeftRingFinger);
        imgViewLeftLittleFinger = root.findViewById(R.id.imgViewLeftLittleFinger);
        imgViewRightThumbFinger = root.findViewById(R.id.imgViewRightThumbFinger);
        imgViewRightIndexFinger = root.findViewById(R.id.imgViewRightIndexFinger);
        imgViewRightMiddleFinger = root.findViewById(R.id.imgViewRightMiddleFinger);
        imgViewRightRingFinger = root.findViewById(R.id.imgViewRightRingFinger);
        imgViewRightLittleFinger = root.findViewById(R.id.imgViewRightLittleFinger);

        qualityViewLeftThumbFinger = root.findViewById(R.id.qualityViewLeftThumbFinger);
        qualityViewLeftIndexFinger = root.findViewById(R.id.qualityViewLeftIndexFinger);
        qualityViewLeftMiddleFinger = root.findViewById(R.id.qualityViewLeftMiddleFinger);
        qualityViewLeftRingFinger = root.findViewById(R.id.qualityViewLeftRingFinger);
        qualityViewLeftLittleFinger = root.findViewById(R.id.qualityViewLeftLittleFinger);
        qualityViewRightThumbFinger = root.findViewById(R.id.qualityViewRightThumbFinger);
        qualityViewRightIndexFinger = root.findViewById(R.id.qualityViewRightIndexFinger);
        qualityViewRightMiddleFinger = root.findViewById(R.id.qualityViewRightMiddleFinger);
        qualityViewRightRingFinger = root.findViewById(R.id.qualityViewRightRingFinger);
        qualityViewRightLittleFinger = root.findViewById(R.id.qualityViewRightLittleFinger);

        edReasonTIL = root.findViewById(R.id.edReasonTIL);
        edReason = root.findViewById(R.id.edReason);

        autoFingerprintSelect = root.findViewById(R.id.autoFingerprintSelect);
        mButtonRegister = root.findViewById(R.id.buttonRegister);
        mButtonSaveCapture = root.findViewById(R.id.btnSavePrints);
        mButtonClearUnsyncFingerPrint = (Button) root.findViewById(R.id.buttonClearUnsyncFingerPrint);
        mImageViewFingerprint = (ImageView) root.findViewById(R.id.imageViewFingerprint);
        fabBiometricsCapture = root.findViewById(R.id.fabBiometricsCapture);
        //Set onclick listener
        mButtonRegister.setOnClickListener(this);
        mButtonSaveCapture.setOnClickListener(this);
        fabBiometricsCapture.setOnClickListener(this);

        mButtonClearUnsyncFingerPrint.setOnClickListener(this);

        mNumFakeThresholds = new int[1];
        mDefaultFakeThreshold = new int[1];
        mFakeEngineReady = new boolean[1];


        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES * JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i = 0; i < grayBuffer.length; ++i)
            grayBuffer[i] = Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES);
        mImageViewFingerprint.setImageBitmap(grayBitmap);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2) * (JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2)];
        for (int i = 0; i < sintbuffer.length; ++i)
            sintbuffer[i] = Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2);
//            mImageViewRegister.setImageBitmap(grayBitmap);
//            mImageViewVerify.setImageBitmap(grayBitmap);
        mMaxTemplateSize = new int[1];


        //USB Permissions
        //mPermissionIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        mPermissionIntent = PendingIntent.getBroadcast(requireActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        sgfplib = new JSGFPLib(requireActivity(), (UsbManager) requireActivity().getSystemService(Context.USB_SERVICE));
        bSecuGenDeviceOpened = false;
        usbPermissionRequested = false;

        debugMessage("Starting Activity\n");
        debugMessage("JSGFPLib version: " + sgfplib.GetJSGFPLibVersion() + "\n");
        mAutoOnEnabled = false;
        autoOn = new SGAutoOnEventNotifier(sgfplib, this);
        nCaptureModeN = 0;
        Log.d(TAG, "Exit onCreate()");
    }

    private void showDropDowns() {
        String[] allFingers = getResources().getStringArray(R.array.fingerprints);

        ArrayAdapter<String> adapterAllFingers = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allFingers);
        autoFingerprintSelect.setAdapter(adapterAllFingers);
    }

    @Override
    public void onClick(View view) {
        if (view == mButtonSaveCapture) {
            saveFingerPrints();
        } else if (view == this.fabBiometricsCapture) {
            CapturePrint();
        } else if (view == this.mButtonClearUnsyncFingerPrint) {
            deleteUnsyncedFingerPrint();
        } else {
            setViewItem(view);
        }
    }

    public void CapturePrint() {

        if (StringUtils.isBlank(ViewUtils.getInput(autoFingerprintSelect))) {
            CustomDebug("Please select the finger position before capturing", false);
        } else {
            //DEBUG Log.d(TAG, "Clicked REGISTER");
            debugMessage("Clicked REGISTER\n");
            if (mRegisterImage != null)
                mRegisterImage = null;

            mRegisterImage = new byte[mImageWidth * mImageHeight];

            if (sgfplib != null) {
                long result = sgfplib.GetImageEx(mRegisterImage, IMAGE_CAPTURE_TIMEOUT_MS, IMAGE_CAPTURE_QUALITY);
                debugMessage("GetImage() returned:" + result);

                if (result != 0) {
                    if (biometricsUtil != null) {
                        String errorMsg = biometricsUtil.getDeviceErrors((int) result);
                        CustomDebug(errorMsg, false);
                    }
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

                Integer imageQuality = fpInfo.ImageQuality;

                String hashed = HashUtil.bcryptHash(mRegisterTemplate);

                int[] size = new int[1];
                result = sgfplib.GetTemplateSize(mRegisterTemplate, size);
                debugMessage("GetTemplateSize() ret:" + result + " size [" + size[0] + "]\n");

                String template = Base64.encodeToString(mRegisterTemplate, Base64.NO_WRAP);

                biometricsUtil = new BiometricsUtil(sgfplib);
                if (template != null && isGoodQuality) {
                    //add to dictionary
                    temporalBiometricsSave.add(template);
                    fingerPrintCaptureCount = temporalBiometricsSave.size();
                    biometrics.setSyncStatus(0);

                    String previousCapture = biometricsUtil.CheckIfFingerAlreadyCaptured(template, biometricsClassFingers);
                    if (previousCapture != null && !previousCapture.isEmpty()) {
                        CustomDebug("This finger has been captured before for " + previousCapture, false);
                    } else {
                        //save to temp list to be discard later
                        biometricsClassFingers.add(new BiometricsClass.BiometricsClassFingers(template, ViewUtils.getInput(autoFingerprintSelect), hashed, imageQuality));
                        bindDrawableResources(getImageView(ViewUtils.getInput(autoFingerprintSelect), imageQuality));

                    }
                } else {
                    //color warning. this capture is not counted
                    unbindDrawableResources(getImageView(ViewUtils.getInput(autoFingerprintSelect), imageQuality));
                }
                //enable the save button when 6 fingers has been captured
                if (fingerPrintCaptureCount >= 6) {
                    this.mButtonSaveCapture.setClickable(true);
                    this.mButtonSaveCapture.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
            }
        }
        mRegisterImage = null;
    }

    public void deleteUnsyncedFingerPrint() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BiometricsDAO.deletePrint(mPresenter.getPatientId());

                        unbindDrawableResources(imgViewLeftThumbFinger);
                        unbindDrawableResources(imgViewLeftIndexFinger);
                        unbindDrawableResources(imgViewLeftMiddleFinger);
                        unbindDrawableResources(imgViewLeftRingFinger);
                        unbindDrawableResources(imgViewLeftLittleFinger);
                        unbindDrawableResources(imgViewRightThumbFinger);
                        unbindDrawableResources(imgViewRightIndexFinger);
                        unbindDrawableResources(imgViewRightMiddleFinger);
                        unbindDrawableResources(imgViewRightRingFinger);
                        unbindDrawableResources(imgViewRightLittleFinger);
                        biometricsClassFingers.clear();
                        CustomDebug("Fingerprints successfully cleared", false);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void CheckIfAlreadyCapturedOnLocalDB() {
        if (!mPresenter.isClientRecapturing()) {
            if (mPresenter.getPatientId() != null) {
                List<BiometricsList> biometricsLists = BiometricsDAO.getFingerPrints(mPresenter.getPatientId());
                if (biometricsLists != null && biometricsLists.size() > 0) {
                    CustomDebug("Some Finger Print already exit for this patient. You can capture more or clear the existing ones to start afresh", false);

                    for (BiometricsList item : biometricsLists) {
                        biometricsClassFingers.add(new BiometricsClass.BiometricsClassFingers(item.getTemplate(), item.getTemplateType()));
                        getImageView(item.getTemplateType()).setImageDrawable(getResources().getDrawable(R.drawable.fingerprint_checked));
                    }
                } else { //check if already sync
                    if (BiometricsDAO.syncStatus(mPresenter.getPatientId()) != null && BiometricsDAO.syncStatus(mPresenter.getPatientId()) == 1) {
                        CustomDebug("Fingerprint has been captured and synced for this patient", true);
                    }
                }
            }
        } else {
            //Do nothing for now
        }
    }

    private boolean checkForQuality(int imageQuality) {
        if (imageQuality < IMAGE_CAPTURE_QUALITY) {
            CustomDebug("Please re-capture this finger. The quality is low (" + imageQuality + " %).", false);
            return false;
        }
        return true;
    }

    private void saveFingerPrints() {

        if (fingerPrintCaptureCount < 6) {
            CustomDebug("Please capture a minimum of 6 prints before saving", false);
        } else {
            if (!mPresenter.isClientRecapturing()) {
                CustomDebug("Saved successfully", false);
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String biometricsList = gson.toJson(biometricsClassFingers);
                Biometrics biometrics = new Biometrics();
                biometrics.setPerson(mPresenter.getPatientId());
                if (BiometricsDAO.getPatientId(mPresenter.getPatientId()) != null) {
                    biometrics.setPatientId(BiometricsDAO.getPatientId(mPresenter.getPatientId()));
                }
                biometrics.setIso(true);
                biometrics.setDeviceName("Secugen Mobile");
                biometrics.setBiometricType("FINGERPRINT");
                biometrics.setCapturedBiometricsList(biometricsList);
                biometrics.setType("SUCCESS");
                biometrics.setDateTime(DateUtils.currentDate());
                biometrics.save();
            } else {
                if (fingerPrintCaptureCount < 10 && StringUtils.isBlank(ViewUtils.getInput(edReason))) {
                    edReasonTIL.setVisibility(View.VISIBLE);
                    CustomDebug("Captured fingerprints is less than 10 for recapture. Enter the reason on the box shown", false);
                } else {
                    CustomDebug("Saved successfully", false);
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    String biometricsList = gson.toJson(biometricsClassFingers);
                    BiometricsRecapture biometricsRecapture = new BiometricsRecapture();
                    biometricsRecapture.setPerson(mPresenter.getPatientId());
                    if (BiometricsDAO.getPatientId(mPresenter.getPatientId()) != null) {
                        biometricsRecapture.setPatientId(BiometricsDAO.getPatientId(mPresenter.getPatientId()));
                    }
                    biometricsRecapture.setIso(true);
                    biometricsRecapture.setDeviceName("Secugen Mobile");
                    biometricsRecapture.setBiometricType("FINGERPRINT");
                    biometricsRecapture.setCapturedBiometricsList(biometricsList);
                    biometricsRecapture.setType("SUCCESS");
                    biometricsRecapture.setReason(ViewUtils.getInput(edReason));
                    biometricsRecapture.setDateTime(DateUtils.currentDate());
                    biometricsRecapture.save();
                }
            }
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
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
                            debugMessage("USB BroadcastReceiver VID : " + device.getVendorId() + "\n");
                            debugMessage("USB BroadcastReceiver PID: " + device.getProductId() + "\n");
                        } else
                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
                    } else
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
    public Handler fingerDetectedHandler = new Handler() {
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

    public void setViewItem(View v) {

    }

    private void CustomDebug(String s, boolean finishOnOk) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(requireActivity());
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
    public Bitmap toGrayscale(byte[] mImageBuffer, int width, int height) {
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
    public Bitmap toGrayscale(byte[] mImageBuffer) {
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
    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int color = bmpOriginal.getPixel(x, y);
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;
                int gray = (r + g + b) / 3;
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
    public Bitmap toBinary(Bitmap bmpOriginal) {
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

    public void SGFingerPresentCallback() {
        autoOn.stop();
        fingerDetectedHandler.sendMessage(new Message());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onPause() {
        Log.d(TAG, "Enter onPause()");
        debugMessage("Enter onPause()\n");
        if (bSecuGenDeviceOpened) {
            if (autoOn != null) {
                autoOn.stop();
                sgfplib.CloseDevice();
            }
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
    public void onResume() {
        Log.d(TAG, "Enter onResume()");
        debugMessage("Enter onResume()\n");
        super.onResume();
        //DisableControls();
        getActivity().registerReceiver(mUsbReceiver, filter);
        long error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(requireActivity());
            if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
                dlgAlert.setMessage("The attached fingerprint device is not supported on Android");
            else
                dlgAlert.setMessage("Fingerprint device initialization failed!");
            dlgAlert.setTitle("SecuGen Fingerprint SDK");
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            requireActivity().finish();
                            return;
                        }
                    }
            );
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        } else {
            UsbDevice usbDevice = sgfplib.GetUsbDevice();
            if (usbDevice == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(requireActivity());
                dlgAlert.setMessage("SecuGen fingerprint sensor not found!");
                dlgAlert.setTitle("SecuGen Fingerprint SDK");
                dlgAlert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                requireActivity().finish();
                                return;
                            }
                        }
                );
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            } else {
                boolean hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                if (!hasPermission) {
                    if (!usbPermissionRequested) {
                        debugMessage("Requesting USB Permission\n");
                        //Log.d(TAG, "Call GetUsbManager().requestPermission()");
                        usbPermissionRequested = true;
                        sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
                    } else {
                        //wait up to 20 seconds for the system to grant USB permission
                        hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                        debugMessage("Waiting for USB Permission\n");
                        int i = 0;
                        while ((hasPermission == false) && (i <= 40)) {
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
                    if (error == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        bSecuGenDeviceOpened = true;
                        SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
                        error = sgfplib.GetDeviceInfo(deviceInfo);
                        debugMessage("GetDeviceInfo() ret: " + error + "\n");
                        mImageWidth = deviceInfo.imageWidth;
                        mImageHeight = deviceInfo.imageHeight;
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
                        } else {
                            mNumFakeThresholds[0] = 1;        //0=Off, 1=Touch Chip
                            mDefaultFakeThreshold[0] = 1;    //Touch Chip Enabled
                            //this.mTextViewFDLevel.setText("Fake Threshold (" + mDefaultFakeThreshold[0] + "/" + mNumFakeThresholds[0] + ")");
                        }

                        sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
                        sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
                        debugMessage("TEMPLATE_FORMAT_ISO19794 SIZE: " + mMaxTemplateSize[0] + "\n");
                        mRegisterTemplate = new byte[(int) mMaxTemplateSize[0]];
//                        mVerifyTemplate = new byte[(int)mMaxTemplateSize[0]];
//                        EnableControls();
                        boolean smartCaptureEnabled = true;
                        if (smartCaptureEnabled)
                            sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 1);
                        else
                            sgfplib.WriteData(SGFDxConstant.WRITEDATA_COMMAND_ENABLE_SMART_CAPTURE, (byte) 0);
                        if (mAutoOnEnabled) {
                            autoOn.start();
                            //DisableControls();
                        }
                    } else {
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

    private ImageView getImageView(String fingerpos, Integer imageQuality) {
        ImageView mView = null;
        if (fingerpos.equals("Left Thumb Finger")) {
            mView = imgViewLeftThumbFinger;
            qualityViewLeftThumbFinger.setVisibility(View.VISIBLE);
            qualityViewLeftThumbFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Left Index Finger")) {
            mView = imgViewLeftIndexFinger;
            qualityViewLeftIndexFinger.setVisibility(View.VISIBLE);
            qualityViewLeftIndexFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Left Middle Finger")) {
            mView = imgViewLeftMiddleFinger;
            qualityViewLeftMiddleFinger.setVisibility(View.VISIBLE);
            qualityViewLeftMiddleFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Left Ring Finger")) {
            mView = imgViewLeftRingFinger;
            qualityViewLeftRingFinger.setVisibility(View.VISIBLE);
            qualityViewLeftRingFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Left Little Finger")) {
            mView = imgViewLeftLittleFinger;
            qualityViewLeftLittleFinger.setVisibility(View.VISIBLE);
            qualityViewLeftLittleFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Right Thumb Finger")) {
            mView = imgViewRightThumbFinger;
            qualityViewRightThumbFinger.setVisibility(View.VISIBLE);
            qualityViewRightThumbFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Right Index Finger")) {
            mView = imgViewRightIndexFinger;
            qualityViewRightIndexFinger.setVisibility(View.VISIBLE);
            qualityViewRightIndexFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Right Middle Finger")) {
            mView = imgViewRightMiddleFinger;
            qualityViewRightMiddleFinger.setVisibility(View.VISIBLE);
            qualityViewRightMiddleFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Right Ring Finger")) {
            mView = imgViewRightRingFinger;
            qualityViewRightRingFinger.setVisibility(View.VISIBLE);
            qualityViewRightRingFinger.setText(imageQuality + "");
        } else if (fingerpos.equals("Right Little Finger")) {
            mView = imgViewRightLittleFinger;
            qualityViewRightLittleFinger.setVisibility(View.VISIBLE);
            qualityViewRightLittleFinger.setText(imageQuality + "");
        }
        return mView;
    }

    private ImageView getImageView(String fingerpos) {
        ImageView mView = null;
        if (fingerpos.equals("Left Thumb Finger")) {
            mView = imgViewLeftThumbFinger;
        } else if (fingerpos.equals("Left Index Finger")) {
            mView = imgViewLeftIndexFinger;
        } else if (fingerpos.equals("Left Middle Finger")) {
            mView = imgViewLeftMiddleFinger;
        } else if (fingerpos.equals("Left Ring Finger")) {
            mView = imgViewLeftRingFinger;
        } else if (fingerpos.equals("Left Little Finger")) {
            mView = imgViewLeftLittleFinger;
        } else if (fingerpos.equals("Right Thumb Finger")) {
            mView = imgViewRightThumbFinger;
        } else if (fingerpos.equals("Right Index Finger")) {
            mView = imgViewRightIndexFinger;
        } else if (fingerpos.equals("Right Middle Finger")) {
            mView = imgViewRightMiddleFinger;
        } else if (fingerpos.equals("Right Ring Finger")) {
            mView = imgViewRightRingFinger;
        } else if (fingerpos.equals("Right Little Finger")) {
            mView = imgViewRightLittleFinger;
        }
        return mView;
    }

    public void bindDrawableResources(ImageView imageView) {
        bindDrawableResource(imageView, R.drawable.fingerprint_checked);
    }

    public void unbindDrawableResources(ImageView imageView) {
        bindDrawableResource(imageView, R.drawable.fingerprint);
    }

    /**
     * Binds drawable resource to ImageView
     *
     * @param imageView  ImageView to bind resource to
     * @param drawableId id of drawable resource (for example R.id.somePicture);
     */
    private void bindDrawableResource(ImageView imageView, int drawableId) {
        mBitmapCache = new SparseArray<>();
        if (getView() != null && imageView != null) {
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

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void run() {

        //Log.d(TAG, "Enter run()");
        //ByteBuffer buffer = ByteBuffer.allocate(1);
        //UsbRequest request = new UsbRequest();
        //request.initialize(mSGUsbInterface.getConnection(), mEndpointBulk);
        //byte status = -1;
        while (true) {


            // queue a request on the interrupt endpoint
            //request.queue(buffer, 1);
            // send poll status command
            //  sendCommand(COMMAND_STATUS);
            // wait for status event
            /*
            if (mSGUsbInterface.getConnection().requestWait() == request) {
                byte newStatus = buffer.get(0);
                if (newStatus != status) {
                    Log.d(TAG, "got status " + newStatus);
                    status = newStatus;
                    if ((status & COMMAND_FIRE) != 0) {
                        // stop firing
                        sendCommand(COMMAND_STOP);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            } else {
                Log.e(TAG, "requestWait failed, exiting");
                break;
            }
            */
        }
    }

}
