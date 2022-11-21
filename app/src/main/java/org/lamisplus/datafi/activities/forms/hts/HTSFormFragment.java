package org.lamisplus.datafi.activities.forms.hts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientFragment;

public class HTSFormFragment extends LamisBaseFragment<HTSFormContract.Presenter> implements HTSFormContract.View{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hts_form, container, false);
        setHasOptionsMenu(true);
        if(root!=null){
//            initViews(root);
//            initDropDowns();
//            dropDownClickListeners();
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.submit_done_menu, menu);
    }

    public static HTSFormFragment newinstance(){
        return new HTSFormFragment();
    }

}
