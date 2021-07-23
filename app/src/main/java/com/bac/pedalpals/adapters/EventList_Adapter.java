package com.bac.pedalpals.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bac.pedalpals.R;
import com.bac.pedalpals.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventList_Adapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<Event> events;
    private int layout;
    private ArrayList<Event> filteredList;

    EventFilter eventfilater;


    public EventList_Adapter(Context context, ArrayList<Event> events, int layout) {
        this.context = context;
        this.events = events;
        filteredList =this.events;

        this.layout = layout;
    }

    public void setList(List<Event> list){
        events.clear();
        events.addAll(list);
        filteredList =this.events;
        sortMyByDate();
    }

    private void sortMyByDate() {
        //Do sorting of the list

        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event p1, Event p2) {
                //    return emp1.getFirstName().compareToIgnoreCase(emp2.getFirstName());
                return p1.getDate().compareToIgnoreCase(p2.getDate());
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
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
            viewHolder._img = (ImageView) list_row.findViewById(R.id.event_img);
            viewHolder.eventtype = (TextView) list_row.findViewById(R.id.event_type);
            viewHolder.date = (TextView) list_row.findViewById(R.id.event_date);
            viewHolder.location = (TextView) list_row.findViewById(R.id.event_location);

            viewHolder.desc = (TextView) list_row.findViewById(R.id.event_desc);
            list_row.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) list_row.getTag();
        }

        Event evts= events.get(position);

        viewHolder.desc.setText(evts.getDesc());
        viewHolder.location.setText(evts.getLocation());

        viewHolder.date.setText(evts.getDate());
        viewHolder.eventtype.setText(evts.getType());
        byte[] img_record = evts.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img_record,0,img_record.length);
        viewHolder._img.setImageBitmap(bitmap);
        return list_row;
    }

    @Override
    public Filter getFilter() {
        if (eventfilater == null) {
            eventfilater = new EventFilter();
        }

        return eventfilater;
    }


    private  class  ViewHolder{

        ImageView _img;
        TextView eventtype,desc, date,location;
    }


    private class EventFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<Event> tempList = new ArrayList<Event>();

                // search content in friend list
                for (Event event : events) {
                    if (event.getDesc().toLowerCase().contains(constraint.toString().toLowerCase())||event.getDate().toLowerCase().contains(constraint.toString().toLowerCase())||event.getLocation().toLowerCase().contains(constraint.toString().toLowerCase())||event.getType().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(event);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = events.size();
                filterResults.values = events;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Event>) results.values;
            notifyDataSetChanged();
        }
    }


}
