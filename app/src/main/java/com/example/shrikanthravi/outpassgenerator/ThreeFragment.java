package com.example.shrikanthravi.outpassgenerator;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * Created by Shrikanth Ravi on 07-01-2017.
 */

public class ThreeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView lv;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> data =new ArrayList<String>();
    String acctname="";
    String OL="";
    String type="";
    String warden="Warden";
    String student="security";
    String key="";
    String log="";
    StringBuilder sb;
    SearchView searchView;
    int Outpass;
    MyArrayAdapter<String> adapter;
    DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("Users");
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot3:dataSnapshot.getChildren()){
                    acctname=(String)dataSnapshot3.child("Name").getValue();
                    type=(String)dataSnapshot3.child("type").getValue();
                    if(!type.equals(warden) && !type.equals(student))
                        if(!data.contains(acctname)){
                            data.add(acctname);
                        }
                    System.out.println(acctname);
                }
                //OL = map.get("OutpassLeft");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_three,container,false);
        searchView = (SearchView) view.findViewById(R.id.idsearch);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh3);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.Black);
        lv = (ListView) view.findViewById(R.id.Student_list);
        adapter = new MyArrayAdapter(getActivity(), R.layout.student_row,R.id.txt,data);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                final String Ssid = (String) lv.getItemAtPosition(position);
                // Toast.makeText(getActivity(),Ssid,Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                final View alertLayout = inflater.inflate(R.layout.log_details, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final TextView logtext=(TextView) alertLayout.findViewById(R.id.log);
                ref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String name=(String)dataSnapshot1.child("Name").getValue();
                            log=(String)dataSnapshot1.child("Log").getValue();
                            if(name.equals(Ssid)){
                                key = dataSnapshot1.getKey();
                                logtext.setText(log);
                            }

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);
                final AlertDialog aldialog = alert.create();
                aldialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override public void onShow(DialogInterface dialog) {
                        // Remember that ViewAnimationUtils will not work until api 21
                       /* Button btnPositive = aldialog.getButton(Dialog.BUTTON_POSITIVE);
                        btnPositive.setTextSize(20);
                        btnPositive.setTextColor(Color.BLACK);
                        Button btnNegative = aldialog.getButton(Dialog.BUTTON_NEGATIVE);
                        btnNegative.setTextSize(20);
                        btnNegative.setTextColor(Color.BLACK); */
                        int centerX = alertLayout.getWidth() / 2;
                        int centerY = alertLayout.getHeight() / 2;
                        // TODO Get startRadius from FAB
                        // TODO Also translate animate FAB to center of screen?
                        float startRadius = 20;
                        float endRadius = alertLayout.getHeight();
                        Animator animator = ViewAnimationUtils.createCircularReveal(alertLayout, centerX, centerY, startRadius, endRadius);
                        animator.setDuration(1000);
                        animator.start();
                    }
                });
                aldialog.show();
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                final String Ssid = (String) lv.getItemAtPosition(position);
                // Toast.makeText(getActivity(),Ssid,Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                final View alertLayout = inflater.inflate(R.layout.security_details, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                ImageView imageView = (ImageView) alertLayout.findViewById(R.id.SEimg);
                final TextView sdept=(TextView) alertLayout.findViewById(R.id.SESdept);
                final TextView sYear=(TextView) alertLayout.findViewById(R.id.SESyear);
                final TextView sName=(TextView) alertLayout.findViewById(R.id.SESname);
                final TextView sRegno=(TextView) alertLayout.findViewById(R.id.SESregno);
                imageView.setImageResource(R.drawable.stud1);
                ref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String name=(String)dataSnapshot1.child("Name").getValue();
                            String dept=(String)dataSnapshot1.child("Department").getValue();
                            String year=(String)dataSnapshot1.child("Year").getValue();
                            String regno=(String)dataSnapshot1.child("Register number").getValue();
                            String OL=(String)dataSnapshot1.child("OutpassLeft").getValue();
                            log=(String)dataSnapshot1.child("Log").getValue();
                            if(name.equals(Ssid)){
                                key = dataSnapshot1.getKey();
                                sName.setText(name);
                                sdept.setText(dept);
                                sYear.setText(year);
                                sRegno.setText(regno);
                                sb=new StringBuilder(log);

                                try{
                                    Outpass = Integer.parseInt(OL);

                                }catch(NumberFormatException nfe){

                                }
                            }

                        }
                        //OL = map.get("OutpassLeft");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);

                final AlertDialog aldialog = alert.create();


                aldialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override public void onShow(DialogInterface dialog) {
                        // Remember that ViewAnimationUtils will not work until api 21
                       /* Button btnPositive = aldialog.getButton(Dialog.BUTTON_POSITIVE);
                        btnPositive.setTextSize(20);
                        btnPositive.setTextColor(Color.BLACK);
                        Button btnNegative = aldialog.getButton(Dialog.BUTTON_NEGATIVE);
                        btnNegative.setTextSize(20);
                        btnNegative.setTextColor(Color.BLACK); */
                        int centerX = alertLayout.getWidth() / 2;
                        int centerY = alertLayout.getHeight() / 2;
                        // TODO Get startRadius from FAB
                        // TODO Also translate animate FAB to center of screen?
                        float startRadius = 20;
                        float endRadius = alertLayout.getHeight();
                        Animator animator = ViewAnimationUtils.createCircularReveal(alertLayout, centerX, centerY, startRadius, endRadius);
                        animator.setDuration(1000);
                        animator.start();
                    }
                });
                aldialog.show();


            }
        });

        lv.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //String text = newText;
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        adapter.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        adapter.notifyDataSetChanged();
        return view;
    }
    public class MyArrayAdapter<T> extends ArrayAdapter<T>
    {
        public MyArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = super.getView(position, convertView, parent);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
            final ImageView InOut = (ImageView) itemView.findViewById(R.id.InOut);
            final TextView sdept=(TextView) itemView.findViewById(R.id.list_sdept);
            final TextView sYear=(TextView) itemView.findViewById(R.id.list_syesr);
            final String selectedFromList =(String) (lv.getItemAtPosition(position));
            imageView.setImageResource(R.drawable.stud1);
            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String name=(String)dataSnapshot1.child("Name").getValue();
                        String dept=(String)dataSnapshot1.child("Department").getValue();
                        String year=(String)dataSnapshot1.child("Year").getValue();
                        String inout=(String)dataSnapshot1.child("InOut").getValue();
                        if(name.equals(selectedFromList)){
                            sdept.setText(dept);
                            sYear.setText(year);
                            if(inout.equals("In")){
                                InOut.setImageResource(R.drawable.greencircle);
                            }else{
                                InOut.setImageResource(R.drawable.redcircle);
                            }
                        }

                    }
                    //OL = map.get("OutpassLeft");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            return itemView;
        }
    }
    @Override
    public void onRefresh(){
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        ft3.detach(this).attach(this).commit();
    }
}