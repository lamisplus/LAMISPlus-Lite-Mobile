package org.lamisplus.datafi.activities.preferences;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientActivity;
import org.lamisplus.datafi.activities.findpatient.FindPatientActivity;
import org.lamisplus.datafi.api.BearerApi;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.AccountDAO;
import org.lamisplus.datafi.models.Account;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ImageUtils;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.URLValidator;
import org.lamisplus.datafi.utilities.ViewUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrefrencesFragment extends LamisBaseFragment<PrefrencesContract.Presenter> implements PrefrencesContract.View, View.OnClickListener {

    private EditText edserverURL;
    private Button btnSaveServerURL;
    private Button btnSaveLocation;
    private TextView tvcurrentServerURL;
    private AutoCompleteTextView autoLocation;

    private LamisPlus  lamisPlus = LamisPlus.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_preferences, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            fillFields();
        }
        return root;
    }

    public static PrefrencesFragment newInstance() {
        return new PrefrencesFragment();
    }

    private void initiateFragmentViews(View root) {
        edserverURL = root.findViewById(R.id.edserverURL);
        btnSaveServerURL = root.findViewById(R.id.btnSaveServerURL);
        btnSaveLocation = root.findViewById(R.id.btnSaveLocation);
        tvcurrentServerURL = root.findViewById(R.id.tvcurrentServerURL);
        autoLocation = root.findViewById(R.id.autoLocation);
    }

    private void fillFields(){
        tvcurrentServerURL.setText(lamisPlus.getServerUrl());

        List<Account> accounts = AccountDAO.getAllAccounts();
        List<String> allAccounts = new ArrayList<>();
        for(Account acc : accounts){
            allAccounts.add(acc.getCurrentOrganisationUnitName());
        }
        LamisCustomHandler.showJson(allAccounts);
        ArrayAdapter<String> adapterLocations = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, allAccounts);
        autoLocation.setAdapter(adapterLocations);

        Account account = AccountDAO.getDefaultLocation();
        if(account != null) {
            autoLocation.setText(account.getCurrentOrganisationUnitName(), false);
        }
    }

    private void setListeners() {
        btnSaveServerURL.setOnClickListener(this);
        btnSaveLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveServerURL:
                String newURL = edserverURL.getText().toString();
                URLValidator.ValidationResult result = URLValidator.validate(newURL);
                if (StringUtils.isBlank(newURL) || newURL == null) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Please enter the correct URL");
                } else if (!result.isURLValid()) {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.ERROR, "Invalid URL entered");
                } else {
                    ToastUtil.showLongToast(getContext(), ToastUtil.ToastType.SUCCESS, "URL saved successfully " + result.getUrl());

                    Account account = AccountDAO.getUserDetails();
                    Account.load(Account.class, account.getId());
                    account.setServerUrl(result.getUrl());
                    account.save();

                    lamisPlus.setServerUrl(result.getUrl());

                    tvcurrentServerURL.setText(result.getUrl());
                    edserverURL.setText("");
                }

                break;
            case R.id.btnSaveLocation:
                String selectedFacility = ViewUtils.getInput(autoLocation);
                AccountDAO.setLocation(selectedFacility);
                ToastUtil.success("Location saved successfully as " + selectedFacility);
                break;
            default:

                break;
        }
    }


}
