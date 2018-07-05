package com.example.shrikanthravi.outpassgenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
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

public class TwoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView glv;
    SwipeRefreshLayout oSwipeRefreshLayout;
    ArrayList<String> data2 =new ArrayList<String>();
    String acctname="";
    String OL="";
    String type="";
    String warden="Warden";
    String student="security";
    String req ="";
    String request="Granted";
    MyArrayAdapter<String> adapter2;
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Users");

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data2.clear();
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                    acctname=(String)dataSnapshot2.child("Name").getValue();
                    req =(String)dataSnapshot2.child("Request").getValue();
                    type=(String)dataSnapshot2.child("type").getValue();
                    if(!type.equals(warden) && !type.equals(student))
                        if(request.equals(req)){
                            if(!data2.contains(acctname)){
                                data2.add(acctname);
                                adapter2.notifyDataSetChanged();
                            }}
                    //System.out.println(acctname);
                }
               // adapter2.notifyDataSetChanged();
                //OL = map.get("OutpassLeft");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        View view = inflater.inflate(R.layout.fragment_two,container,false);
        oSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh3);
        oSwipeRefreshLayout.setOnRefreshListener(this);
        oSwipeRefreshLayout.setColorSchemeResources(R.color.Black);
        glv = (ListView) view.findViewById(R.id.granted_list);
        adapter2 = new MyArrayAdapter(getActivity(), R.layout.student_row,R.id.txt,data2);
        glv.setAdapter(adapter2);
        adapter2.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        adapter2.notifyDataSetChanged();
        glv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                String Ssid = (String) glv.getItemAtPosition(position);
                Toast.makeText(getActivity(),Ssid,Toast.LENGTH_SHORT).show();

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
            final String selectedFromList =(String) (glv.getItemAtPosition(position));
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