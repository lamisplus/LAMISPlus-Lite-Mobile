package org.lamisplus.datafi.activities.connectserver.findpatientserver;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.activities.patientprofile.PatientProfileActivity;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class FindPatientServerRecyclerViewAdapter extends RecyclerView.Adapter<FindPatientServerRecyclerViewAdapter.PatientViewHolder> {
    private FindPatientServerFragment mContext;
    private List<Person> mItems;
    private boolean multiSelect = false;
    private ArrayList<Person> selectedItems = new ArrayList<>();

    private androidx.appcompat.view.ActionMode.Callback actionModeCallbacks = new androidx.appcompat.view.ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            multiSelect = true;
            mode.getMenuInflater().inflate(R.menu.delete_multi_patient_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
            ((LamisBaseActivity) mContext.requireActivity()).showMultiDeletePatientDialog(selectedItems);
            return true;
        }

        @Override
        public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };

    public FindPatientServerRecyclerViewAdapter(FindPatientServerFragment context, List<Person> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_find_patients_server, parent, false);
        //FontsUtil.setFont((ViewGroup) itemView);
        return new PatientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        holder.update(mItems.get(position), position);
        final Person person = mItems.get(position);

        if (null != person.getIdentifier()) {
            String patientIdentifier = String.format(mContext.getResources().getString(R.string.patient_identifier),
                    person.getIdentifier());
            holder.mIdentifier.setText(person.getIdentifiers().getValue());
        }
        if (null != person.getFirstName()) {
            holder.mDisplayName.setText(person.getFirstName() + " " + person.getOtherName() + " " + person.getSurname());
            //holder.mDisplayName.setText(person.getAddresses().getCity());
        }
        String personGender = CodesetsDAO.findCodesetsDisplayById(person.getGenderId());
        if (null != personGender) {
            holder.mGender.setText(personGender);
        }

        List<Person> personDb = PersonDAO.getAllPatients();
        for (Person p : personDb) {
            if (p.getPersonId() == person.getPersonId()) {
                holder.downloadPatientButton.setVisibility(View.GONE);
            }else{
                holder.downloadPatientButton.setVisibility(View.VISIBLE);
            }
        }

        try {
            holder.mBirthDate.setText(person.getDateOfBirth());
        } catch (Exception e) {
            holder.mBirthDate.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class PatientViewHolder extends RecyclerView.ViewHolder {

        private CardView mRowLayout;
        private TextView mIdentifier;
        private TextView mDisplayName;
        private TextView mGender;
        private TextView mAge;
        private TextView mBirthDate;
        private Button downloadPatientButton;

        private ColorStateList cardBackgroundColor;

        public PatientViewHolder(View itemView) {
            super(itemView);
            mRowLayout = (CardView) itemView;
            mIdentifier = itemView.findViewById(R.id.syncedPatientIdentifier);
            mDisplayName = itemView.findViewById(R.id.syncedPatientDisplayName);
            mGender = itemView.findViewById(R.id.syncedPatientGender);
            mAge = itemView.findViewById(R.id.syncedPatientAge);
            mBirthDate = itemView.findViewById(R.id.syncedPatientBirthDate);
            downloadPatientButton = itemView.findViewById(R.id.downloadPatientButton);


            cardBackgroundColor = mRowLayout.getCardBackgroundColor();
        }

        void selectItem(Person item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    mRowLayout.setCardBackgroundColor(cardBackgroundColor);
                } else {
                    selectedItems.add(item);
                    mRowLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.selected_card));
                }
            }
        }

        void update(final Person value, int position) {
            if (selectedItems.contains(value)) {
                mRowLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.selected_card));
            } else {
                mRowLayout.setCardBackgroundColor(cardBackgroundColor);
            }
            itemView.setOnLongClickListener(view -> {
                ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                selectItem(value);
                return true;
            });

            Button download = itemView.findViewById(R.id.downloadPatientButton);
            download.setOnClickListener(view -> {
                if (!multiSelect) {
                    value.save();
                    mItems.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    ToastUtil.success("Patient Downloaded");
                } else {
                    Log.v("Baron", "Its true");
                    selectItem(value);
                }

            });
        }
    }
}
