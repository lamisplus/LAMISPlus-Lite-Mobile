package org.lamisplus.datafi.activities.biometrics.neurotech;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.support.v4.app.*;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NFPosition;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.standards.CBEFFBDBFormatIdentifiers;
import com.neurotec.biometrics.standards.CBEFFBiometricOrganizations;
import com.neurotec.biometrics.standards.FMRecord;
import com.neurotec.biometrics.view.NFingerView;
import com.neurotec.biometrics.view.NFingerViewBase.ShownImage;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceType;
import com.neurotec.devices.NFScanner;
import com.neurotec.images.NImage;
import com.neurotec.lang.NCore;
import com.neurotec.lang.NotActivatedException;
import com.neurotec.samples.licensing.LicensingManager;
import com.neurotec.samples.util.IOUtils;
import com.neurotec.samples.util.NImageUtils;
import com.neurotec.samples.util.ResourceUtils;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.classes.BiometricsClass;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.models.Biometrics;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.BiometricsUtil;
import org.lamisplus.datafi.utilities.FingerPositions;
import org.lamisplus.datafi.utilities.ImageUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FingerActivity extends BiometricActivity {

    // ===========================================================
    // Private static fields
    // ===========================================================

    private static final String TAG = FingerActivity.class.getSimpleName();
    private static final String BUNDLE_KEY_STATUS = "status";
    private static final String MODALITY_ASSET_DIRECTORY = "fingers";

    // ===========================================================
    // Private fields
    // ===========================================================

    private NFingerView mFingerView;
    private SparseArray<Bitmap> mBitmapCache;
    private Bitmap mDefaultBitmap;
    private TextView mStatus;
    private Map<String, NFPosition> mFingerPositions;
    private final int MODALITY_CODE_FINGER = 2;

    private FingerPositions fingerPosition = null;
    private List<Biometrics> patientFingerPrints;
    private List<String> temporalBiometricsSave = new ArrayList<>();

    String patientId = "";
    BiometricsUtil biometricsUtil;

    int fingerPrintCaptureCount = 0;

    List<BiometricsClass.BiometricsClassFingers> biometricsClassFingers = new ArrayList<>();

    private ImageView imgViewLeftThumbFinger, imgViewLeftIndexFinger, imgViewLeftMiddleFinger, imgViewLeftRingFinger, imgViewLeftLittleFinger, imgViewRightThumbFinger, imgViewRightIndexFinger, imgViewRightMiddleFinger, imgViewRightRingFinger, imgViewRightLittleFinger;

    private String patientID;

    private PendingIntent mPermissionIntent;
    private IntentFilter filter;
    // ===========================================================
    // Private methods
    // ===========================================================

    private void setFingerPosition() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(FingerActivity.this);
        builderSingle.setTitle("Select finger position");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FingerActivity.this, android.R.layout.select_dialog_singlechoice);

        arrayAdapter.add(toLowerCase(NFPosition.UNKNOWN.name()));

        arrayAdapter.add(toLowerCase(NFPosition.LEFT_LITTLE_FINGER.name()));
        arrayAdapter.add(toLowerCase(NFPosition.LEFT_RING_FINGER.name()));
        arrayAdapter.add(toLowerCase(NFPosition.LEFT_MIDDLE_FINGER.name()));
        arrayAdapter.add(toLowerCase(NFPosition.LEFT_INDEX_FINGER.name()));
        arrayAdapter.add(toLowerCase(NFPosition.LEFT_THUMB.name()));

        arrayAdapter.add(toLowerCase(NFPosition.RIGHT_LITTLE_FINGER.name()));
        arrayAdapter.add(toLowerCase(NFPosition.RIGHT_RING_FINGER.name()));
        arrayAdapter.add(toLowerCase(NFPosition.RIGHT_MIDDLE_FINGER.name()));
        arrayAdapter.add(toLowerCase(NFPosition.RIGHT_INDEX_FINGER.name()));
        arrayAdapter.add(toLowerCase(NFPosition.RIGHT_THUMB.name()));


        builderSingle.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                String strName = arrayAdapter.getItem(item);
                final String element = strName;
                subject.getTemplate().getFingers().getRecords().get(0).setPosition(mFingerPositions.get(element));
            }
        });
        builderSingle.show();
    }

    private NFScanner getScanner() {
        NDevice fingerDevice = null;
        for (NDevice device : client.getDeviceManager().getDevices()) {
            if (device.getDeviceType().contains(NDeviceType.FSCANNER)) {
                if (device.getId().equals(PreferenceManager.getDefaultSharedPreferences(this).getString(FingerPreferences.FINGER_CAPTURING_DEVICE, "None"))) {
                    return (NFScanner) device;
                } else if (fingerDevice == null) {
                    fingerDevice = device;
                }
            }
        }
        return (NFScanner) fingerDevice;
    }

    private NSubject createSubjectFromImage(Uri uri) {
        NSubject subject = null;
        try {
            NImage image = NImageUtils.fromUri(this, uri);
            subject = new NSubject();
            NFinger finger = new NFinger();
            finger.setImage(image);
            subject.getFingers().add(finger);
        } catch (Exception e) {
            Log.i(TAG, "Failed to load file as NImage");
        }
        return subject;
    }

    private NSubject createSubjectFromMemory(Uri uri) {
        NSubject subject = null;
        try {
            subject = NSubject.fromMemory(IOUtils.toByteBuffer(this, uri));
        } catch (Exception e) {
            Log.i(TAG, "Failed to load finger from file");
        }
        return subject;
    }

    // ===========================================================
    // Protected methods
    // ===========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USB_Permissions();
        Bundle patientBundle = savedInstanceState;
        if (patientBundle != null) {
            patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            patientBundle = getIntent().getExtras();
        }
        if (patientBundle != null) {
            patientID = patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        }
        try {

            PreferenceManager.setDefaultValues(this, R.xml.finger_preferences, false);
            LinearLayout layout = ((LinearLayout) findViewById(R.id.multimodal_biometric_layout));

            imgViewLeftThumbFinger = findViewById(R.id.imgViewLeftThumbFinger);
            imgViewLeftIndexFinger = findViewById(R.id.imgViewLeftIndexFinger);
            imgViewLeftMiddleFinger = findViewById(R.id.imgViewLeftMiddleFinger);
            imgViewLeftRingFinger = findViewById(R.id.imgViewLeftRingFinger);
            imgViewLeftLittleFinger = findViewById(R.id.imgViewLeftLittleFinger);
            imgViewRightThumbFinger = findViewById(R.id.imgViewRightThumbFinger);
            imgViewRightIndexFinger = findViewById(R.id.imgViewRightIndexFinger);
            imgViewRightMiddleFinger = findViewById(R.id.imgViewRightMiddleFinger);
            imgViewRightRingFinger = findViewById(R.id.imgViewRightRingFinger);
            imgViewRightLittleFinger = findViewById(R.id.imgViewRightLittleFinger);

            mFingerView = new NFingerView(this);
            layout.addView(mFingerView);

            mStatus = new TextView(this);
            mStatus.setText("Status");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            mStatus.setLayoutParams(params);
            layout.addView(mStatus);

            String[] fingerPositions = new String[]{"--SELECT FINGER", "Left Thumb Finger", "Left Index Finger", "Left Middle Finger",
                    "Left Ring Finger", "Left Little Finger", "Right Thumb Finger", "Right Index Finger", "Right Middle Finger", "Right Ring Finger", "Right Little Finger"};

            Spinner fingerSpinners = findViewById(R.id.fingerPositionSpinner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fingerPositions);
            fingerSpinners.setAdapter(spinnerArrayAdapter);

            mDefaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_finger);
            if (savedInstanceState == null) {
                NFinger finger = new NFinger();
                finger.setImage(NImage.fromBitmap(mDefaultBitmap));
                mFingerView.setFinger(finger);
            }
            Button add = (Button) findViewById(R.id.multimodal_button_add);
            add.setText("Add");
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedPosition = fingerSpinners.getSelectedItem().toString();
                    Integer itemPosition = fingerSpinners.getSelectedItemPosition();
                    try {
                        if (itemPosition > 0 && itemPosition != 0 && getCapturedStatus()) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                byte[] isoTemplate = subject.getTemplateBuffer(CBEFFBiometricOrganizations.ISO_IEC_JTC_1_SC_37_BIOMETRICS,
                                        CBEFFBDBFormatIdentifiers.ISO_IEC_JTC_1_SC_37_BIOMETRICS_FINGER_MINUTIAE_RECORD_FORMAT,
                                        FMRecord.VERSION_ISO_20).toByteArray();
                                String value = Base64.getEncoder().encodeToString(isoTemplate);
                                temporalBiometricsSave.add(value);
                                fingerPrintCaptureCount = temporalBiometricsSave.size();
                                fingerSpinners.setSelection(0);
                                //This function was created by me in the BiometricActivity to be able to indicate if the capture was successful then after which it is set back to false to prevent subsequent re-use of the already capture fingerprints
                                setCapturedStatus(false);
                                biometricsClassFingers.add(new BiometricsClass.BiometricsClassFingers(value, selectedPosition));
                                bindDrawableResources(getImageView(selectedPosition));
                            }
                        } else {
                            ToastUtil.error("Please select Finger Position");
                        }
                    } catch (NotActivatedException e) {
                        ToastUtil.error("Operation must be activated before capturing (" + e.getMessage() + ")");
                    }
                }
            });
            Button setPosition = (Button) findViewById(R.id.multimodal_button_save);
            setPosition.setText("Save");
            setPosition.setVisibility(View.VISIBLE);
            setPosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveFingerPrint();
                }
            });

        } catch (
                Exception e) {
            showError(e);
        }

    }

    private void saveFingerPrint() {
        if (fingerPrintCaptureCount < 6) {
            CustomDebug("Please capture a minimum of 6 prints before saving", false);
        } else {

            CustomDebug("Saved successfully", false);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String biometricsList = gson.toJson(biometricsClassFingers);
            Biometrics biometrics = new Biometrics();
            Integer pid = Integer.valueOf(patientID);
            biometrics.setPerson(pid);
            if (BiometricsDAO.getPatientId(pid) != null) {
                biometrics.setPatientId(BiometricsDAO.getPatientId(pid));
            }
            biometrics.setIso(true);
            biometrics.setDeviceName("Futronic");
            biometrics.setBiometricType("FINGERPRINT");
            biometrics.setCapturedBiometricsList(biometricsList);
            biometrics.setType("SUCCESS");
            biometrics.save();
        }
    }

    private void CustomDebug(String s, boolean finishOnOk) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(s);
        dlgAlert.setTitle("Patient's Biometric Capture");
        dlgAlert.setPositiveButton("OK",
                (dialog, whichButton) -> {
                    if (finishOnOk) {
                        finish();
                    }
                }
        );
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
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
        if (this != null && imageView != null) {
            createImageBitmap(drawableId, imageView.getLayoutParams());
            imageView.setImageBitmap(mBitmapCache.get(drawableId));
        }
    }

    private void createImageBitmap(Integer key, ViewGroup.LayoutParams layoutParams) {
        if (mBitmapCache.get(key) == null) {
            mBitmapCache.put(key, ImageUtils.decodeBitmapFromResource(getResources(), key,
                    layoutParams.width, layoutParams.height));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_KEY_STATUS, TextUtils.isEmpty(mStatus.getText()) ? "" : mStatus.getText().toString());
    }

    @Override
    protected List<String> getAdditionalComponents() {
        return additionalComponents();
    }

    @Override
    protected List<String> getMandatoryComponents() {
        return mandatoryComponents();
    }

    @Override
    protected Class<?> getPreferences() {
        return FingerPreferences.class;
    }

    @Override
    protected void updatePreferences(NBiometricClient client) {
        FingerPreferences.updateClient(client, this);
    }

    @Override
    protected boolean isCheckForDuplicates() {
        return FingerPreferences.isCheckForDuplicates(this);
    }

    @Override
    protected String getModalityAssetDirectory() {
        return MODALITY_ASSET_DIRECTORY;
    }

    @Override
    protected void onFileSelected(Uri uri) throws Exception {
        NSubject subject = null;
        mFingerView.setShownImage(FingerPreferences.isReturnBinarizedImage(this) ? ShownImage.RESULT : ShownImage.ORIGINAL);
        subject = createSubjectFromImage(uri);

        boolean isDetectLiveness = FingerPreferences.isDetectLiveness(this);
        client.setFingersDetectLiveness(isDetectLiveness);

        if (subject == null) {
            subject = createSubjectFromMemory(uri);
        }

        if (subject != null) {
            if (subject.getFingers() != null && subject.getFingers().get(0) != null) {
                mFingerView.setFinger(subject.getFingers().get(0));
            }
            extract(subject);
        } else {
            showInfo(R.string.msg_failed_to_load_image_or_standard);
        }
    }

    @Override
    protected void onStartCapturing() {
        NFScanner scanner = getScanner();
        if (scanner == null) {
            showError(R.string.msg_capturing_device_is_unavailable);
        } else {
            client.setFingerScanner(scanner);
            NSubject subject = new NSubject();
            NFinger finger = new NFinger();
            boolean isDetectLiveness = FingerPreferences.isDetectLiveness(this);
            client.setFingersDetectLiveness(isDetectLiveness);
            finger.addPropertyChangeListener(biometricPropertyChanged);
            mFingerView.setShownImage(FingerPreferences.isReturnBinarizedImage(this) ? ShownImage.RESULT : ShownImage.ORIGINAL);
            mFingerView.setFinger(finger);
            subject.getFingers().add(finger);
            capture(subject, null);
        }
    }

    @Override
    protected void onStatusChanged(final NBiometricStatus value) {
        runOnUiThread(() -> {
            mStatus.setText(value == null ? "" : ResourceUtils.getEnum(FingerActivity.this, value));
        });
    }

    public static List<String> mandatoryComponents() {
        return Arrays.asList(LicensingManager.LICENSE_FINGER_DETECTION,
                LicensingManager.LICENSE_FINGER_EXTRACTION,
                LicensingManager.LICENSE_FINGER_MATCHING,
                LicensingManager.LICENSE_FINGER_DEVICES_SCANNERS);
    }

    public static List<String> additionalComponents() {
        return Arrays.asList(LicensingManager.LICENSE_FINGER_WSQ,
                LicensingManager.LICENSE_FINGER_STANDARDS_FINGER_TEMPLATES,
                LicensingManager.LICENSE_FINGER_STANDARDS_FINGERS);
//			LicensingManager.LICENSE_FINGER_QUALITY_ASSESSMENT,
//			LicensingManager.LICENSE_FINGER_SEGMENTS_DETECTION);
    }

    public static String toLowerCase(String string) {
        StringBuilder sb = new StringBuilder();
        sb.append(string.substring(0, 1).toUpperCase());
        sb.append(string.substring(1).toLowerCase());
        return sb.toString().replaceAll("_", " ");
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, patientID);
        onSaveInstanceState(bundle);
        super.onBackPressed();
    }

    private void USB_Permissions() {
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        this.registerReceiver(mUsbReceiver, filter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Enter onPause()");
        this.unregisterReceiver(mUsbReceiver);
        Log.d(TAG, "Exit onPause()");
    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ToastUtil.success("Receiver called");
            String action = intent.getAction();
            //Log.d(TAG,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
//                            debugMessage("USB BroadcastReceiver VID : " + device.getVendorId() + "\n");
//                            debugMessage("USB BroadcastReceiver PID: " + device.getProductId() + "\n");
                        } else
                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
                    } else
                        Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };
}
