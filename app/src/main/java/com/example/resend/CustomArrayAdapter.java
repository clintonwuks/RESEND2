package com.example.resend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomArrayAdapter extends BaseAdapter {
    private static final String TAG = "APP_TEST";
    private Context context;
    private ArrayList<FriendItem> al_items;
    static class ViewHolder {
        public TextView fName;
        public TextView uName;
        public TextView uAcr;
    }

    public CustomArrayAdapter(Context c, ArrayList<FriendItem> al) {
        context = c;
        al_items = al;
    }
    // overridden method that will construct a View for the listview out ofthe
    // item at the given position
    public View getView(int position, View convert_view, ViewGroup parent) {
        // view holder to save us from requesting references to items over and overagain
        ViewHolder holder;
// if we do not have a view to recycle then inflate the layout and fix up theview holder
        if(convert_view == null) {
            holder = new ViewHolder();
// get access to the layout infaltor service
            LayoutInflater inflator = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// inflate the XML custom item layout into a view to which we can add data
            convert_view = inflator.inflate(R.layout.friends_item_layout, parent,
                    false);
// pull all the items from the XML so we can modify them
            holder.fName = convert_view.findViewById(R.id.fullName);
            holder.uName = convert_view.findViewById(R.id.username);
            holder.uAcr = convert_view.findViewById(R.id.acr);
// set the view holder as a tag on this convert view in case it needs tobe
// recycled
            convert_view.setTag(holder);
        }
        else {
            holder = (ViewHolder) convert_view.getTag();
        }
// set all the data on the fields before returning it
        holder.uAcr.setText(al_items.get(position).getAcr());
        holder.fName.setText(al_items.get(position).getFullName());
        holder.uName.setText(al_items.get(position).getUserName());
        Log.d("TAG", "full name " + al_items.get(position).getFullName());
        Log.d(TAG, "user name " + al_items.get(position).getUserName());
// return the constructed view
        return convert_view;

    }
    // overridden method that will tell the listview how many items of datathere is
    // to be displayed
    public int getCount() { return al_items.size(); }
    // returns the rowid of the item at the given position. Given that we areusing an
    // array list the rowid will be equal to the index of the item
    public long getItemId(int position) { return position; }
    // overridden method that will return the item at the given position inthe list
    public Object getItem(int position) { return al_items.get(position); }



}




