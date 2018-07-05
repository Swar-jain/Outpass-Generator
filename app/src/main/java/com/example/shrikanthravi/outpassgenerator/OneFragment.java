package com.example.shrikanthravi.outpassgenerator;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Shrikanth Ravi on 07-01-2017.
 */


public class OneFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView rlv;
    SwipeRefreshLayout nSwipeRefreshLayout;
    MyArrayAdapter<String> adapter1;
    ArrayList<String> data1 = new ArrayList<String>();
    String acctname="";
    String OL="";
    String Request="Pending";
    String req="";
    String type="";
    String warden="Warden";
    String student="security";
    String mparent="";
    String key="";
    String name1="";
    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference rref;
    public OneFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one,container,false);
        data1.clear();
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    acctname=(String)dataSnapshot1.child("Name").getValue();
                    type=(String)dataSnapshot1.child("type").getValue();
                    req=(String)dataSnapshot1.child("Request").getValue();
                    if(!type.equals(warden) && !type.equals(student)){
                        if(Request.equals(req)){
                            if(!data1.contains(acctname)){
                                data1.add(acctname);
                                adapter1.notifyDataSetChanged();
                            }
                        }
                    }
                    //System.out.println(req);
                }
                //OL = map.get("OutpassLeft");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        nSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        nSwipeRefreshLayout.setOnRefreshListener(this);
        nSwipeRefreshLayout.setColorSchemeResources(R.color.Black);
        rlv = (ListView) view.findViewById(R.id.request_list);
        adapter1 = new MyArrayAdapter(getActivity(), R.layout.student_row,R.id.txt,data1);
        rlv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                final String Ssid = (String) rlv.getItemAtPosition(position);

               // Toast.makeText(getActivity(),Ssid,Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                final View alertLayout = inflater.inflate(R.layout.request_details, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                ImageView imageView = (ImageView) alertLayout.findViewById(R.id.rimg);
                final TextView sdept=(TextView) alertLayout.findViewById(R.id.list_rdept);
                final TextView sYear=(TextView) alertLayout.findViewById(R.id.list_ryesr);
                final TextView sName=(TextView) alertLayout.findViewById(R.id.rtxt);
                final TextView sReason=(TextView) alertLayout.findViewById(R.id.list_rreason);
                imageView.setImageResource(R.drawable.stud1);
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String name=(String)dataSnapshot1.child("Name").getValue();
                            String dept=(String)dataSnapshot1.child("Department").getValue();
                            String year=(String)dataSnapshot1.child("Year").getValue();
                            String reason=(String)dataSnapshot1.child("Reason").getValue();

                            if(name.equals(Ssid)){
                                name1 = name;
                                key = dataSnapshot1.getKey();
                                mparent = (String)dataSnapshot1.child("Mparent").getValue();
                                sName.setText(name);
                                sdept.setText(dept);
                                sYear.setText(year);
                                sReason.setText(reason);

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
                alert.setNegativeButton("Deny", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rref = FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("Request");
                        rref.setValue("Denied");
                        Toast.makeText(getActivity(), "Request denied!", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rref = FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("Request");
                        rref.setValue("Granted");
                        Toast.makeText(getActivity(), "Request accepted!", Toast.LENGTH_SHORT).show();
                        String subject = "Outpass Intimation";
                        String message = "Dear Parent," +
                                "\n\nOutpass has been issued to your ward, " +name1+" on request." +
                                "\n\n\nThank You," +
                                "\nSSN College of Engineering" +
                                "\nKalavakkam" +
                                "\nChennai 603110";
                        String email = mparent;
                        //Creating SendMail object
                        SendMail sm = new SendMail(getActivity(), email, subject, message);

                        //Executing sendmail to send email
                        sm.execute();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override public void onShow(DialogInterface dialog) {
                        // Remember that ViewAnimationUtils will not work until api 21

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
                dialog.show();


            }
        });

        rlv.setAdapter(adapter1);
        adapter1.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
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
            final TextView sdept=(TextView) itemView.findViewById(R.id.list_sdept);
            final TextView sYear=(TextView) itemView.findViewById(R.id.list_syesr);
            final String selectedFromList =(String) (rlv.getItemAtPosition(position));
            imageView.setImageResource(R.drawable.stud1);
            ref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                         String name=(String)dataSnapshot1.child("Name").getValue();
                        String dept=(String)dataSnapshot1.child("Department").getValue();
                        String year=(String)dataSnapshot1.child("Year").getValue();
                            if(name.equals(selectedFromList)){
                                sdept.setText(dept);
                                sYear.setText(year);
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

        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.detach(this).attach(this).commit();
    }


}
