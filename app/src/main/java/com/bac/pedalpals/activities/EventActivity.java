package com.bac.pedalpals.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bac.pedalpals.R;

public class EventActivity extends AppCompatActivity {

    TextView tvdesc, tvdate, tvtype, tvcontact, tvlocation, email;
    ImageView icon;
    Button btn;
    private static final int REQUEST_PHONE_CALL = 1;

    Button sendMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        tvdesc = findViewById(R.id.eventdesc);
        tvdate = findViewById(R.id.masterdate);
        tvtype = findViewById(R.id.mastereventtype);
        tvlocation = findViewById(R.id.masterEventName);
        email = findViewById(R.id.masteremail);
        tvcontact = findViewById(R.id.mastercontact);
        icon = findViewById(R.id.mastereventimage);

        sendMail = findViewById(R.id.mastersendMail);
        btn = findViewById(R.id.masterbutton);

        String desc = getIntent().getStringExtra("desc");
        String date = getIntent().getStringExtra("date");
        String type = getIntent().getStringExtra("type");
        String _email = getIntent().getStringExtra("email");
        String location = getIntent().getStringExtra("location");
        String contact = getIntent().getStringExtra("contact");
        byte[] image = getIntent().getByteArrayExtra("img");


        tvdesc.setText(desc);
        tvcontact.setText(contact);
        tvdate.setText(date);
        tvlocation.setText(location);
        tvtype.setText(type);
        email.setText(_email);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        icon.setImageBitmap(bitmap);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tvcontact.getText()));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(EventActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

                    return;
                }
                startActivity(callIntent);
            }
        });


        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmessagedialog(EventActivity.this);
            }
        });


    }


    public void showmessagedialog (Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.message);
        dialog.setTitle("Sending Email");


        final EditText message = (EditText) dialog.findViewById(R.id.message);
        final Button msgbutton = (Button) dialog.findViewById(R.id.msgbutton);


        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.75);

        int height = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.85);

        dialog.getWindow().setLayout(width, height);

        dialog.show();

        msgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = "PedalPals Cycling Club";
                String Message = message.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, email.getText().toString());
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, Message);

                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));
            }
        });




    }
}
