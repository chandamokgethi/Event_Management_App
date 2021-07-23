package com.bac.pedalpals.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.bac.pedalpals.adapters.EventList_Adapter;
import com.bac.pedalpals.R;
import com.bac.pedalpals.adapters.Amatabs_Adapter;

public class EventHome extends AppCompatActivity {



    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    Amatabs_Adapter tabsAccessAdapter;


    private SearchView searchView;
    private MenuItem searchMenuItem;
    EventList_Adapter adapter;


        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_event_home);

            toolbar = findViewById(R.id.main_page_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Home");
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));




            viewPager = findViewById(R.id.main_tabs_pages);

            tabsAccessAdapter = new Amatabs_Adapter(getSupportFragmentManager());

            viewPager.setAdapter(tabsAccessAdapter);



            tabLayout = findViewById(R.id.main_tabs);

            tabLayout.setupWithViewPager(viewPager);

            // tabLayout.setSelectedTabIndicatorColor();
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
            tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
            tabLayout.setTabTextColors(Color.parseColor("#d4d0d0"), Color.parseColor("#d4d0d0"));


        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);

        return true;
    }
    Intent  intent;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.newE:
                  intent = new Intent(getApplicationContext(), NewEvent.class);
                startActivity(intent);
                break;

            case R.id.logOut:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.refresh:
                recreate();
                default:
                    break;


        }
        return super.onOptionsItemSelected(item);
    }



}
