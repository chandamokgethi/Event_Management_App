package com.bac.pedalpals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bac.pedalpals.R;
import com.bac.pedalpals.db.UserDatabase;
import com.bac.pedalpals.model.User;

public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    private EditText email, username,password,epassword;
    private Button register;
    private UserDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        email = findViewById(R.id.txtemail);
        username = findViewById(R.id.txtusername);
        password = findViewById(R.id.txtpassword);
        epassword = findViewById(R.id.txtrepassword);
        register = findViewById(R.id.btnregister);
        db = new UserDatabase(this);
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String _email = email.getText().toString().trim();
                 String _username = username.getText().toString().trim();
                 String _password = password.getText().toString().trim();
                 String _repass = epassword.getText().toString().trim();

                User user = new User();
                user.setEmail(_email);
                user.setUsername(_username);
                user.setPassword(_password);




                boolean validateEmail = ValidateEmail(_password,_repass);

                if(validateEmail)
                {
                    long sucess = db.registerUser(user);

                    if(sucess > 0)
                    {
                        Toast.makeText(getApplicationContext(),"Account Created Successful",Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                }
            }
        });
    }

    private boolean ValidateEmail(String p, String q){
        boolean success = false;

        if(!p.equals(q))
        {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();

        }else if  (email.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Input Email", Toast.LENGTH_LONG).show();


        }else if  (username.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Input Username", Toast.LENGTH_LONG).show();


        }else if(p.length() < 6)
        {
            Toast.makeText(getApplicationContext(), "Password length should be six char(s) ", Toast.LENGTH_LONG).show();

        }else{
            success = true;
        }

        return  success;
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
}
