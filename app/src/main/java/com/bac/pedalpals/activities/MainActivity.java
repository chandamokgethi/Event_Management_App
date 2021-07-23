package com.bac.pedalpals.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.pedalpals.R;
import com.bac.pedalpals.db.UserDatabase;
import com.bac.pedalpals.model.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btRegister;
    private TextView tvLogin;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button login;

    UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        login = findViewById(R.id.btnlogin);
        txtPassword = findViewById(R.id.password);
        txtUsername = findViewById(R.id.username);
        btRegister.setOnClickListener( this);
        db = new UserDatabase(this);

//        getSupportActionBar().setTitle("login");


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                boolean checkUser = db.checkUser(user);

                if(checkUser)
                {
                    Intent intent =new Intent(getApplicationContext(), EventHome.class);
                    startActivity(intent);
                }else
                    {
                        Toast.makeText(getApplicationContext(),"Account Not Found",Toast.LENGTH_LONG).show();
                    }


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v==btRegister){
            Intent intent   = new Intent(MainActivity.this, RegisterActivity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View, String>(tvLogin,"tvLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());
        }


    }
}
