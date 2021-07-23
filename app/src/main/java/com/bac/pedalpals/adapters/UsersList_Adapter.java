package com.bac.pedalpals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bac.pedalpals.R;
import com.bac.pedalpals.model.Event;
import com.bac.pedalpals.model.User;

import java.util.ArrayList;

public class UsersList_Adapter extends BaseAdapter  {

    private Context context;
    private ArrayList<User> users;
    private int layout;




    public UsersList_Adapter(Context context, ArrayList<User> users, int layout) {
        this.context = context;
        this.users = users;

        this.layout = layout;
    }



    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list_row = convertView;
        ViewHolder viewHolder = new ViewHolder();

        if(list_row == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            list_row = inflater.inflate(layout, null);
            viewHolder.name = (TextView) list_row.findViewById(R.id.username);
            viewHolder.rank = (TextView) list_row.findViewById(R.id.rank);
            list_row.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) list_row.getTag();
        }

        User user= users.get(position);

        viewHolder.name.setText(user.getUsername());
        viewHolder.rank.setText(String.valueOf(user.getReview()));

        return list_row;
    }


    private  class  ViewHolder{

        TextView name, rank;
    }


}
