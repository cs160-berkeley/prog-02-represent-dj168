package com.example.derrickhu.youvote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by derrickhu on 3/9/16.
 */
public class DetailedAdapter extends BaseAdapter {
    private final Context context;
    private String bioGuideId;
    private String[] committeesOrBills;
    private String party;

    public DetailedAdapter(Context context, String[] committeesOrBills, String party) {
        this.party = party;
        this.context = context;
        this.committeesOrBills = committeesOrBills;
    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View commsRow = inflater.inflate(R.layout.committees, parent, false);
        TextView commsView = (TextView) commsRow.findViewById(R.id.committees);
//        if (party.equals("R")) {
//            commsRow.setBackgroundColor(Color.parseColor("#990F12"));
//        }
//        if (party.equals("D")) {
//            commsRow.setBackgroundColor(Color.parseColor("#133269"));
//        }
//        if (party.equals("I")) {
//            commsRow.setBackgroundColor(Color.parseColor("#C5CAD4"));
//        }


        commsView.setText(committeesOrBills[position]);
        return commsRow;
    }



    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return committeesOrBills.length;
    }

}
