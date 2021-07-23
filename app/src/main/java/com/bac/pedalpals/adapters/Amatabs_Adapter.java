package com.bac.pedalpals.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bac.pedalpals.fragments.Userlist;
import com.bac.pedalpals.fragments.Eventslist;

public class Amatabs_Adapter extends FragmentPagerAdapter
{


    public Amatabs_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0://MyPlayListFragment
                Eventslist myEvents = new Eventslist();
                return  myEvents;

            case 1://contactsFragment
                Userlist usersFragment = new Userlist();
                return usersFragment;//contactsFragment
                default:
                    return  null;
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {


        switch (position){

            case 0:
                return  "ALL Events";

            case 1:
                return  "Users";

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {

        return 2;
    }
}
