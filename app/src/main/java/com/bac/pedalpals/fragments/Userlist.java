package com.bac.pedalpals.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.pedalpals.R;
import com.bac.pedalpals.adapters.UsersList_Adapter;
import com.bac.pedalpals.db.UserDatabase;
import com.bac.pedalpals.model.User;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Userlist extends Fragment{


    private SearchView searchView;
    private MenuItem searchMenuItem;
    ListView listView;
    ArrayList<User> users;
    UsersList_Adapter adapter = null;
    ImageView icon;
    static UserDatabase db_helper;
    private static final int REQUEST_CODE_GALLERY = 888;



    public Userlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);


        listView =(ListView) view.findViewById(R.id.listview);
        users = new ArrayList<>();
        adapter = new UsersList_Adapter(getActivity().getApplicationContext(), users, R.layout.users_list);

        listView.setAdapter(adapter);
        db_helper = new UserDatabase(getActivity());

        Cursor cursor = db_helper.getUser(
                "SELECT * FROM Users");

        users.clear();

        while (cursor.moveToNext()) {


            int id = cursor.getInt(0);
            String email = cursor.getString(1);
            String username = cursor.getString(2);
            int rank = cursor.getInt(4);

            User user = new User();
            user.setId(id);
            user.setReview(rank);
            user.setUsername(username);
            user.setEmail(email);

            users.add(user);

        }
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                CharSequence[] items = { "Review User"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Choose Action");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {


                        if (i == 0) {


                            Cursor c = db_helper.getUser("SELECT Id FROM Users" );
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
    private void RateApp(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.rate_pop_layout);
        dialog.setTitle("Rate User");



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

                    User user = new User();
                    user.setReview(Integer.parseInt(result.getText().toString()));
                    user.setId(position);

                    db_helper.reviewUser(user);

                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),"User Ranked",Toast.LENGTH_LONG).show();


                }catch (Exception ex){
                    ex.printStackTrace();
                }


                dialog.dismiss();

                UpdateRecordList();
            }
        });

        //   dialog.show();;


    }



    private void UpdateRecordList() {

        Cursor cursor= db_helper.getUser("SELECT * FROM " + UserDatabase.tbName);
        users.clear();
        while (cursor.moveToNext()){


            int id = cursor.getInt(0);
            String email = cursor.getString(1);
            String username = cursor.getString(2);
            int rank = cursor.getInt(4);

            User user = new User();
            user.setId(id);
            user.setReview(rank);
            user.setUsername(username);
            user.setEmail(email);

            users.add(user);

        }
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
