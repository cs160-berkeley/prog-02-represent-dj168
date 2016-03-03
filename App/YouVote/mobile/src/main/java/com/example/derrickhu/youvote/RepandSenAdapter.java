package com.example.derrickhu.youvote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by derrickhu on 2/27/16.
 */
public class RepandSenAdapter extends BaseAdapter {

    private final Context context;
    private final int[] photos;
    private final String[] names;
    private final String[] parties;
    private final String[] websites;
    private final String[] emails;
    private final String[] tweets;

    public RepandSenAdapter(Context context, int[] photos, String[] names, String[] parties,String[] websites,String[] emails,String[] tweets) {

        this.context = context;
        this.photos = photos;
        this.names = names;
        this.parties = parties;
        this.websites = websites;
        this.emails = emails;
        this.tweets = tweets;
        }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.repandsen, parent, false);

        ImageView pictureView = (ImageView) repRow.findViewById(R.id.dianne);
        TextView nameView = (TextView) repRow.findViewById(R.id.senator);
        TextView partyView = (TextView) repRow.findViewById(R.id.party);
        TextView websiteView = (TextView) repRow.findViewById(R.id.website);
        TextView emailView = (TextView) repRow.findViewById(R.id.email);
        TextView tweetView = (TextView) repRow.findViewById(R.id.twitter);

        pictureView.setImageResource(photos[position]);
        nameView.setText(names[position]);
        partyView.setText(parties[position]);
        websiteView.setText(websites[position]);
        emailView.setText(emails[position]);
        tweetView.setText(tweets[position]);

        //Attaching onClickListener

        repRow.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("NAME", names[position]);
                context.startActivity(intent);
                //new intent
                //put extra
                //context.startACtivity
            }
        });

        return repRow;

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
        return names.length;
    }
}
