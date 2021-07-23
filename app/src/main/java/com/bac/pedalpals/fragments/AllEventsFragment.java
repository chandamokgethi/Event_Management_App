package com.bac.pedalpals.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.pedalpals.model.Event;
import com.bac.pedalpals.activities.EventActivity;
import com.bac.pedalpals.adapters.EventList_Adapter;
import com.bac.pedalpals.db.EventDatabase;
import com.bac.pedalpals.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllEventsFragment extends Fragment{



    ListView listView;
    ArrayList<Event> events;
    EventList_Adapter adapter = null;
    ImageView icon;
    static EventDatabase db_helper;
    private static final int REQUEST_CODE_GALLERY = 888;



    public AllEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);


        listView =(ListView) view.findViewById(R.id.listview);
        events = new ArrayList<>();
        adapter = new EventList_Adapter(getActivity().getApplicationContext(), events, R.layout.list_row);

        listView.setAdapter(adapter);
        db_helper = new EventDatabase(getActivity());

        Cursor cursor = db_helper.getEvent(
                "SELECT * FROM Event");

        events.clear();

        while (cursor.moveToNext()) {


            int id = cursor.getInt(0);
            String desc = cursor.getString(1);
            String type = cursor.getString(2);
            String location = cursor.getString(3);
            String date = cursor.getString(4);
            String contact = cursor.getString(5);
            String email = cursor.getString(6);
            byte[] img = cursor.getBlob(7);

            Event event = new Event();
            event.setId(id);
            event.setDesc(desc);
            event.setType(type);
            event.setLocation(location);
            event.setDate(date);
            event.setContact(contact);
            event.setEmail(email);
            event.setImg(img);



            events.add(event);

        }
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                CharSequence[] items = {"Update", "Delete", "Event Deails"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Choose Action");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        if (i == 0) {

                            Cursor c = db_helper.getEvent("SELECT Id FROM  " + db_helper.tbName);
                            ArrayList<Integer> ids = new ArrayList<>();

                            while (c.moveToNext()) {

                                ids.add(c.getInt(0));

                            }//show Update dialog

                            ShowDialog(getActivity(), ids.get(position));

                        }
                        if (i == 1) {

                            Cursor c = db_helper.getEvent("SELECT Id FROM " +  db_helper.tbName);
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));

                        }
                        if (i == 2) {

                            Cursor c = db_helper.getEvent("SELECT Id FROM " + db_helper.tbName);
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            OpenEventDetails(arrID.get(position));
                        }
                        if (i == 3) {


                            Cursor c = db_helper.getEvent("SELECT Id FROM "  + db_helper.tbName);
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            RateApp(getActivity(), arrID.get(position));

                        }


                    }
                });

                builder.show();

            }
        });



        return  view;
    }

    private void RateApp(Activity activity, Integer integer) {
    }

    private void OpenEventDetails(Integer integer) {

        Cursor cursor = db_helper.getEvent("SELECT * FROM "+db_helper.tbName + "  WHERE Id = " + integer);
        events.clear();


        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String desc = cursor.getString(1);
            String type = cursor.getString(2);
            String location = cursor.getString(3);
            String date = cursor.getString(4);
            String contact = cursor.getString(5);
            String email = cursor.getString(6);
            byte[] img = cursor.getBlob(7);

            Intent intent = new Intent(getContext(), EventActivity.class);
            intent.putExtra("desc", desc);
            intent.putExtra("type", type);
            intent.putExtra("location", location);
            intent.putExtra("date", date);
            intent.putExtra("img", img);
            intent.putExtra("email", email);
            intent.putExtra("contact", contact);


            startActivity(intent);
        }


    }

    private void showDialogDelete(final Integer integer) {


        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(getActivity());
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Event p = new Event();
                    p.setId(integer);
                    db_helper.delete(p);
                    Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                UpdateRecordList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();

    }

    private void ShowDialog(Activity activity, final Integer integer) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_pop_dialog);
        dialog.setTitle("Update Event Details");


        icon = (ImageView) dialog.findViewById(R.id.updateimg);

        final Spinner spinnerLocation =  dialog.findViewById(R.id.txtLocation);
        final Spinner spinnerType =  dialog.findViewById(R.id.event_type) ;


        final EditText desc = (EditText) dialog.findViewById(R.id.txtdesc);
        final EditText date = (EditText) dialog.findViewById(R.id.txtdate);
        final EditText contactNo = (EditText) dialog.findViewById(R.id.txtContact);
        final Button btnUpdate = (Button) dialog.findViewById(R.id.btnupdate);


        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.65);

        int height = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.45);

        dialog.getWindow().setLayout(width, height);

        dialog.show();


        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );

            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Event event = new Event();
                    event.setImg(ImageViewToBlob(icon));
                    event.setId(integer);
                    event.setDate(date.getText().toString());
                    event.setDesc(desc.getText().toString());
                    event.setContact(contactNo.getText().toString());
                    event.setLocation(String.valueOf(spinnerLocation.getSelectedItem()));
                    event.setType(String.valueOf(spinnerType.getSelectedItem()));
                    db_helper.update(event);

                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    UpdateRecordList();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                dialog.dismiss();
                UpdateRecordList();
            }
        });
    }
    private byte[] ImageViewToBlob(ImageView image) {
        Bitmap bitmap =((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
        byte[] bytearray = stream.toByteArray();

        return  bytearray;

    }

    private void UpdateRecordList() {

        Cursor cursor= db_helper.getEvent("SELECT * FROM " + db_helper.tbName);
        events.clear();
        while (cursor.moveToNext()){


            int id = cursor.getInt(0);
            String desc = cursor.getString(1);
            String type = cursor.getString(2);
            String location = cursor.getString(3);
            String date = cursor.getString(4);
            String contact = cursor.getString(5);

            byte[] img = cursor.getBlob(6);

            Event event = new Event();
            event.setId(id);
            event.setDesc(desc);
            event.setType(type);
            event.setLocation(location);
            event.setDate(date);
            event.setContact(contact);
            event.setImg(img);
            events.add(event);
        }
    }
    private void RateApp(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.rate_pop_layout);
        dialog.setTitle("Rate Painting");





        final RatingBar rate  = (RatingBar) dialog.findViewById(R.id.ratingBar);
        final TextView result= (TextView) dialog.findViewById(R.id.result);
        final Button ratebtn  = (Button) dialog.findViewById(R.id.ratebtn);



        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels * 0.95);

        int height = (int)(activity.getResources().getDisplayMetrics().widthPixels * 0.65);

        dialog.getWindow().setLayout(width,height);

        dialog.show();


        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar rtBar, float rating,boolean fromUser) {
                int r = (int) rating;
                result.setText(String.valueOf(r));
            }
        });



        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Event event = new Event();
                   // event(Float.parseFloat(result.getText().toString()));
                   // painting.setId(position);

                    // db_helper.(painting);

                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),"Update Record",Toast.LENGTH_LONG).show();
                    dialog.dismiss();


                }catch (Exception ex){
                    ex.printStackTrace();
                }



                UpdateRecordList();
            }
        });

        //   dialog.show();;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Don't have Permission to access file location", Toast.LENGTH_LONG).show();
            }

            return;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
