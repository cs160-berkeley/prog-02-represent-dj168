package com.example.derrickhu.youvote;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;




    private OnFragmentInteractionListener mListener;

    public RepFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepFragment newInstance(String param1, String param2, String param3, String param4, String param5, String param6) {
        RepFragment fragment = new RepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = (View) inflater.inflate(R.layout.fragment_rep, container, false);

        TextView repOr2012 = (TextView) v.findViewById(R.id.repOr2012);
        TextView zipCodeView = (TextView) v.findViewById(R.id.zipCode);

        repOr2012.setText(mParam1);
        TextView partyOrState = (TextView) v.findViewById(R.id.partyOrState);
        partyOrState.setText(mParam2);
        if (mParam6.length() == 5) {
            zipCodeView.setText("Swipe down for 2012 Presidential Election Results for " +  mParam6);
        }
        else {
            zipCodeView.setText(mParam6);
        }
        TextView stateOrDistrict = (TextView) v.findViewById(R.id.stateOrDistrict);
        if (mParam3 != "") {
            stateOrDistrict.setText("Obama: " + mParam3 + "%");
        }
        TextView nameOrPercent = (TextView) v.findViewById(R.id.nameOrPercent);
        if (mParam4 != "") {
            nameOrPercent.setText("Romney: " + mParam4 + "%");
        }

        //
        //zipCodeView.setText(mParam6);

//        ImageView pictureView = (ImageView) v.findViewById(R.id.portrait);
//        TextView repOr2012 = (TextView) v.findViewById(R.id.repOr2012);
//        TextView partyOrState = (TextView) v.findViewById(R.id.partyOrState);
//        TextView stateOrDistrict = (TextView) v.findViewById(R.id.stateOrDistrict);
//        TextView nameOrPercent = (TextView) v.findViewById(R.id.nameOrPercent);
//
//        repOr2012.setText(mParam1);
//        partyOrState.setText(mParam2);
//        stateOrDistrict.setText(mParam3);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent sendIntent = new Intent(context, WatchToPhoneService.class);
                sendIntent.putExtra("BIOGUIDEID", mParam5);
                context.startService(sendIntent);


            }
        });


    return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
