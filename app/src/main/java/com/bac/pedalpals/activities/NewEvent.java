package com.bac.pedalpals.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.pedalpals.R;
import com.bac.pedalpals.db.EventDatabase;
import com.bac.pedalpals.fragments.Datepicker;
import com.bac.pedalpals.model.Event;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;

public class NewEvent extends AppCompatActivity implements AdapterView.OnItemClickListener, DatePickerDialog.OnDateSetListener {

    Spinner spinnerLocation,spinnerType;
    EditText eventDescription, contactNo,email;
    TextView tvDate;
    Button btn, DateP;
    ImageView eventImg;
    String location = "";
    String type = "";
    private static final int REQUEST_CODE_GALLERY = 999;

    EventDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//


        tvDate = findViewById(R.id.txtdate);
        DateP = findViewById(R.id.DateP);
        spinnerLocation = findViewById(R.id.txtLocation);
        spinnerType = findViewById(R.id.txttype);
        eventDescription = findViewById(R.id.txtdesc);
        contactNo = findViewById(R.id.txtContact);
        email = findViewById(R.id.txtemail);

        eventImg = findViewById(R.id.img);
        btn = findViewById(R.id.btnsave);

        db = new EventDatabase(this);

        eventImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        NewEvent.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });


        DateP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new Datepicker();
                dialogFragment.show(getFragmentManager(),"Date Picker");
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String desc = eventDescription.getText().toString().trim();
                String conNo = contactNo.getText().toString().trim();
                String _email = email.getText().toString().trim();

                String date = tvDate.getText().toString().trim();
                String location = String.valueOf(spinnerType.getSelectedItem());;
                String type = String.valueOf(spinnerType.getSelectedItem());;

                Event event = new Event();
                event.setDate(date);
                event.setDesc(desc);
                event.setEmail(_email);
                event.setLocation(location);
                event.setContact(conNo);
                event.setType(type);
                event.setImg(ImageViewToBlob(eventImg));

                long success = db.insert(event);

                if(success > 0)
                {
                    Toast.makeText(getApplicationContext(), "EVENT SAVED", Toast.LENGTH_LONG).show();
                }else
                    {
                        Toast.makeText(getApplicationContext(), "COULD NOT SAVE EVENT", Toast.LENGTH_LONG).show();

                    }


            }
        });

        ArrayAdapter <CharSequence> events =ArrayAdapter.createFromResource(this, R.array.eventTypes,android.R.layout.simple_spinner_item);
        events.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(events);


        ArrayAdapter <CharSequence> locations =ArrayAdapter.createFromResource(this, R.array.locations,android.R.layout.simple_spinner_item);
        locations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locations);

        String l = String.valueOf(spinnerType.getSelectedItem());





    }
    private byte[] ImageViewToBlob(ImageView image) {
        Bitmap bitmap =((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
        byte[] bytearray = stream.toByteArray();

        return  bytearray;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){

            Uri imguri = data.getData();

            eventImg.setImageURI(imguri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }else{
                Toast.makeText(getApplicationContext(), "Don't have Permission to access file location", Toast.LENGTH_LONG).show();
            }

            return;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(calendar.MONTH, month);
        calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        tvDate.setText(currentDateFormat);


    }
}
