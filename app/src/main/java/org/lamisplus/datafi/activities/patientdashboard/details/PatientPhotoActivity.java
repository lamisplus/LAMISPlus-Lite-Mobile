package org.lamisplus.datafi.activities.patientdashboard.details;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class PatientPhotoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_patient_photo);
//        byte[] photo = getIntent().getByteArrayExtra("photo");
//
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
//        Bitmap patientPhoto = BitmapFactory.decodeStream(inputStream);
//        ImageView patientImageView = findViewById(R.id.patientPhoto);
//        patientImageView.setImageBitmap(patientPhoto);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            String patientName = getIntent().getStringExtra("name");
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setTitle(patientName);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
