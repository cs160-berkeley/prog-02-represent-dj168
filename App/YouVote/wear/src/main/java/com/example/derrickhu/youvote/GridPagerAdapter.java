package com.example.derrickhu.youvote;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.List;

/**
 * Created by derrickhu on 3/1/16.
 */
public class GridPagerAdapter extends FragmentGridPagerAdapter{

    private final Context mContext;
    private List mRows;
    private String zipCode;

    public GridPagerAdapter(Context ctx, FragmentManager fm, String zip) {
        super(fm);
        mContext = ctx;
        zipCode = zip;
    }

    // A simple container for static data in each page
    private static class Page {
        // static resources
        String type;
        String name;
        String party;
        int portrait;

        String state;
        String district;
        String percent;
        String election;

        String zipCode;


        public Page(String type, String name, String party, int portrait, String state) {
            this.type = type;
            this.name = name;
            this.party = party;
            this.portrait = portrait;
            this.state = state;
        }

        public Page(String state, String district, String election, String percent, int portrait) {
            this.state = state;
            this.district = district;
            this.election = election;
            this.percent = percent;
            this.portrait = portrait;
        }
    }

    // Create a static set of pages in a 2D array
    private final Page[][] PAGES = {
            {new Page("Senator", "Dianne Feinstein", "Democrat", R.drawable.dianne, "Orange County,CA"),
            new Page("Senator", "Barbara Boxer", "Democrat", R.drawable.dianne, "Orange County,CA"),
            new Page("Representative", "Darrell Issa", "Republican", R.drawable.dianne, "Orange County,CA")},
            {new Page("Orange County,CA", "District 49", "2012 Presidential Election", "70% Obama 29% Romney", R.drawable.alamedacounty)
             }
        };

            // Override methods in FragmentGridPagerAdapter
    @Override
    public Fragment getFragment (int row, int col)
    {
        Page page = PAGES [row] [col];
        if (row == 0) {
            String type = page.type;
            String name = page.name;
            String party = page.party;
            String state = page.state + " - " + zipCode;
            int portrait = page.portrait;
            return RepFragment.newInstance(type, party, state, name, portrait);
        }
        else {
            String election = page.election;
            String state = page.state;
            String percent = page.percent;
            int portrait = page.portrait;
            return RepFragment.newInstance(election, state, zipCode, percent, portrait);
        }

    }

    @Override
    public int getRowCount (){
        return PAGES.length;
    }
    @Override
    public int getColumnCount (int rowNum)
            {
            return PAGES[rowNum].length;
            }
}
