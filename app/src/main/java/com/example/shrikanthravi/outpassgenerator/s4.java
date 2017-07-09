package com.example.shrikanthravi.outpassgenerator;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;



public class s4 extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ListView glv4;
    SwipeRefreshLayout oSwipeRefreshLayout;
    ArrayList<String> data4 =new ArrayList<String>();
    String acctname="";
    String OL="";
    String type="";
    String Year="";
    String warden="Warden";
    String student="security";
    String req ="";
    String request="Granted";
    String key="";
    String log="";
    StringBuilder sb;
    int Outpass;
    String name1="";
    String mparent="";
    MyArrayAdapter<String> adapter4;
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Users");

    public s4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data4.clear();
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                    acctname=(String)dataSnapshot2.child("Name").getValue();
                    req =(String)dataSnapshot2.child("Request").getValue();
                    type=(String)dataSnapshot2.child("type").getValue();
                    Year=(String)dataSnapshot2.child("Year").getValue();
                    String syear="4th Year";
                    if(!type.equals(warden) && !type.equals(student))
                        if(Year.equals(syear)) {
                            if (request.equals(req)) {
                                if (!data4.contains(acctname)) {
                                    data4.add(acctname);
                                    adapter4.notifyDataSetChanged();
                                }
                            }
                        }
                    //System.out.println(acctname);
                }
                // adapter2.notifyDataSetChanged();
                //OL = map.get("OutpassLeft");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        View view = inflater.inflate(R.layout.activity_s4,container,false);
        oSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srefresh4);
        oSwipeRefreshLayout.setOnRefreshListener(this);
        oSwipeRefreshLayout.setColorSchemeResources(R.color.Black);
        glv4 = (ListView) view.findViewById(R.id.s4granted_list);
        adapter4 = new MyArrayAdapter(getActivity(), R.layout.student_row,R.id.txt,data4);
        glv4.setAdapter(adapter4);
        adapter4.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        adapter4.notifyDataSetChanged();
        glv4.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                final String Ssid = (String) glv4.getItemAtPosition(position);
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
                ref2.addValueEventListener(new ValueEventListener() {
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
                                name1 = name;
                                mparent = (String)dataSnapshot1.child("Mparent").getValue();
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
                alert.setNegativeButton("In", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        StringBuilder sb1 = new StringBuilder(formattedDate);
                        sb1.insert(0,"In time:   ");
                        sb.insert(0,"\n\n");
                        sb.insert(0,sb1.toString());
                        ref3.child("Log").setValue(sb.toString());
                        ref3.child("InOut").setValue("In");
                        ref3.child("Request").setValue(" ");
                        Toast.makeText(getActivity(), "In time Updated", Toast.LENGTH_SHORT).show();
                        String subject = "Outpass Intimation";
                        String message = "Dear Parent," +
                                "\n\nThis mail is to notify that your ward, " +name1+" has arrived at the campus after the utilisation of outpass." +
                                "\n\n\nThank You," +
                                "\nSSN College of Engineering" +
                                "\nKalavakkam" +
                                "\nChennai 603110";
                        String email = mparent;
                        //Creating SendMail object
                        SendMail sm1 = new SendMail(getActivity(), email, subject, message);

                        //Executing sendmail to send email
                        sm1.execute();
                    }
                });

                alert.setPositiveButton("Out", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
                        if(Outpass>0){
                            Outpass=Outpass-1;
                            String o = Integer.toString(Outpass);
                            ref3.child("OutpassLeft").setValue(o);
                        }
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        StringBuilder sb1 = new StringBuilder(formattedDate);
                        sb1.insert(0,"Out time:   ");
                        sb.insert(0,"\n\n");
                        sb.insert(0,sb1.toString());
                        ref3.child("Log").setValue(sb.toString());
                        ref3.child("InOut").setValue("Out");
                        Toast.makeText(getActivity(), "Out time Updated", Toast.LENGTH_SHORT).show();
                        String subject = "Outpass Intimation";
                        String message = "Dear Parent," +
                                "\n\nYour ward, " +name1+" has left the campus." +
                                "\n\n\nThank You," +
                                "\nSSN College of Engineering" +
                                "\nKalavakkam" +
                                "\nChennai 603110";
                        String email = mparent;
                        //Creating SendMail object
                        SendMail sm1 = new SendMail(getActivity(), email, subject, message);

                        //Executing sendmail to send email
                        sm1.execute();
                    }
                });
                final AlertDialog aldialog = alert.create();


                aldialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override public void onShow(DialogInterface dialog) {
                        // Remember that ViewAnimationUtils will not work until api 21
                        Button btnPositive = aldialog.getButton(Dialog.BUTTON_POSITIVE);
                        btnPositive.setTextSize(20);
                        btnPositive.setTextColor(Color.BLACK);
                        Button btnNegative = aldialog.getButton(Dialog.BUTTON_NEGATIVE);
                        btnNegative.setTextSize(20);
                        btnNegative.setTextColor(Color.BLACK);
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
            final String selectedFromList =(String) (glv4.getItemAtPosition(position));
            imageView.setImageResource(R.drawable.stud1);
            ref2.addValueEventListener(new ValueEventListener() {
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
        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
        ft2.detach(this).attach(this).commit();
    }


}
