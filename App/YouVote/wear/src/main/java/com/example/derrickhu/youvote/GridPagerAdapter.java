package com.example.derrickhu.youvote;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by derrickhu on 3/1/16.
 */
public class GridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private String origData;
    private String zipCode;
    private String countyState;
    private String obamaVote;
    private ArrayList<SimpleRow> mPages;

    public GridPagerAdapter(Context context, FragmentManager fm, String data) {
        super(fm);
        mContext = context;
        origData = data;
        initPages();
    }
    private void initPages() {
        mPages = new ArrayList<SimpleRow>();
        SimpleRow row1 = new SimpleRow();
        SimpleRow row2 = new SimpleRow();
        JSONArray repsList = null;
        String[] firstData = origData.split("[!]+");
        String repData = firstData[0];
        zipCode = firstData[1];
        countyState = firstData[2];
        String[] countyStateArray = countyState.split("[,]+");
        String county = countyStateArray[0];
        String state = countyStateArray[1];

        obamaVote = firstData[3];
        String[] obamaArray = obamaVote.split("[;]+");
        String obama = obamaArray[0];
        String romney = obamaArray[1];
        try {
            JSONObject reps = new JSONObject(repData);
            repsList = reps.getJSONArray("results");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        for (int i = 0; i < repsList.length(); i++) {
            try {
                JSONObject repData1 = repsList.getJSONObject(i);
                String typePartyState = repData1.getString("title") + " (" + repData1.getString("party") + " - " + repData1.getString("state") + " )";
                String fullName = repData1.getString("first_name") + " " + repData1.getString("last_name");
                String bioGuideId = repData1.getString("bioguide_id");
                Page newPage = new Page(typePartyState, fullName, bioGuideId);
                row1.addPages(newPage);

            }
            catch (JSONException e) {
            }
        }

        for (int i = 0; i < repsList.length(); i++) {
            Page newPage = new Page(county, state, obama, romney);
            row2.addPages(newPage);
        }
        mPages.add(row1);
        mPages.add(row2);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Page page = ((SimpleRow)mPages.get(row)).getPages(col);
        if (row == 0) {
            String typePartyState = page.typePartyState;
            String name = page.name;
            String bioGuideId = page.bioGuideId;
            return RepFragment.newInstance(typePartyState, name,"", "", bioGuideId, zipCode);
        }
        else {
            String county = page.county;
            String state = page.state;
            String obama = page.obama;
            String romney = page.romney;
            String bioGuideId = page.bioGuideId;

            return RepFragment.newInstance(county, state, obama, romney, bioGuideId, "");
        }
    }



    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.get(row).size();
    }

    public class Page {
        String typePartyState;
        String name;
        String bioGuideId;
        int portrait;

        String state;
        String county;
        String obama;
        String romney;

        public Page(String typePartyState, String name, String bioGuideId) {
            this.typePartyState = typePartyState;
            this.name = name;
            this.bioGuideId = bioGuideId;

        }
        public Page(String county, String state, String obama, String romney) {
            this.state = state;
            this.county = county;
            this.obama = obama;
            this.romney = romney;
            //this.portrait = portrait;
        }
    }

    public class SimpleRow {
        ArrayList<Page> mPagesRow = new ArrayList<Page>();
        public void addPages(Page page) {
            mPagesRow.add(page);
        }
        public Page getPages(int index) {
            return mPagesRow.get(index);
        }
        public int size(){
            return mPagesRow.size();
        }
    }
}



