package com.example.shrikanthravi.outpassgenerator;

import android.animation.Animator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Student extends AppCompatActivity {

    int i;
    TextView Name;
    TextView OutpassLeft;
    ImageView Bell;
    TextView Notif;
    int Outpass;
     String acctname="";
     String OL="";
    String status="";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView img;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Bell = (ImageView) findViewById(R.id.bell);
        Notif = (TextView) findViewById(R.id.notif);
        Name = (TextView) findViewById(R.id.stud_name);
        OutpassLeft = (TextView) findViewById(R.id.OutpassLeft);

        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*acctname = (String)dataSnapshot.child("Name").getValue();
                String acctmail = (String)dataSnapshot.child("Email").getValue();
                OL = (String)dataSnapshot.child("OutpassLeft").getValue();
                Name.setText(acctname);
                OutpassLeft.setText(OL);
                try{

                }catch (NumberFormatException nfe){

                }*/
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                acctname = map.get("Name");
                OL = map.get("OutpassLeft");
                status=map.get("Request");
                Name.setText(acctname);
                OutpassLeft.setText(OL);
                final Animation animation = new AlphaAnimation(1, 0);
                animation.setDuration(1000);
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.REVERSE);
                if (status.equals("Granted")){
                    System.out.println("Outpass Granted");
                    Bell.setVisibility(View.VISIBLE);
                    Notif.setVisibility(View.VISIBLE);
                    Notif.setText("Outpass Granted!");
                    Bell.startAnimation(animation);
                }else{
                    if(status.equals("Pending")){
                        Bell.setVisibility(View.VISIBLE);
                        Notif.setVisibility(View.VISIBLE);
                        Notif.setText("Request sent!");
                        Bell.startAnimation(animation);
                    }else{
                        if(status.equals("Denied")){
                            Bell.setVisibility(View.VISIBLE);
                            Notif.setVisibility(View.VISIBLE);
                            Notif.setText("Meet in person");
                            Bell.startAnimation(animation);
                        }
                    }
                }
                try{
                    Outpass = Integer.parseInt(OL);

                }catch(NumberFormatException nfe){

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Student.super.onBackPressed();
                        Intent back = new Intent(Student.this, LoginActivity.class);
                        startActivity(back);
                        Student.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                        FirebaseAuth.getInstance().signOut();
                    }
                }).create().show();
    }

    public void buttonClicked(View view) {
        RadioButton but2 = (RadioButton)findViewById(R.id.radioButton2);
        if (but2.isChecked()){

            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.alert1, null);

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Reason");
            // this sets the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                }
            });

            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(status.equals("Granted") ){
                        Toast.makeText(getBaseContext(),"Outpass usage pending!",Toast.LENGTH_SHORT).show();
                    }else{
                        if(status.equals("Pending")){
                            Toast.makeText(getBaseContext(),"Previous request pending!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            EditText reason = (EditText) alertLayout.findViewById(R.id.alert1);
                            String r = reason.getText().toString();
                            Toast.makeText(getBaseContext(), "Request sent!", Toast.LENGTH_SHORT).show();
                            ref.child("Request").setValue("Pending");
                            ref.child("Reason").setValue(r);
                        }
                    }
                }
            });
            AlertDialog dialog = alert.create();
            //WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            //wmlp.gravity = Gravity.FILL_HORIZONTAL;
            //wmlp.x = wmlp.y= 100;


            //Bitmap fast=fastblur(map, 70);
            //final Drawable draw=new BitmapDrawable(getResources(),fast);
            //dialog.getWindow().setAttributes(wmlp);
            //dialog.getWindow().setBackgroundDrawable(draw);
            //dialog.setView(alertLayout, 0, 0, 0, 0);
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
        else{
            if(Outpass>0){
                if(status.equals("Granted")){
                    Toast.makeText(getBaseContext(),"Outpass usage pending!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(status.equals("Pending")){
                        Toast.makeText(getBaseContext(),"Previous request pending!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                      //  Outpass = Outpass - 1;
                      //  String o = Integer.toString(Outpass);
                      //  ref.child("OutpassLeft").setValue(o);
                        ref.child("Request").setValue("Granted");
                        Toast.makeText(getBaseContext(), "Outpass Granted!", Toast.LENGTH_SHORT).show();
                    }
                }

            }else{
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.alert, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Oops!");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getBaseContext(), "Request sent!", Toast.LENGTH_SHORT).show();
                        ref.child("Request").setValue("Pending");
                    }
                });
                AlertDialog dialog = alert.create();
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

        }

    }
    public void buttonClicked1(View view){
        LayoutInflater inflater = getLayoutInflater();
       final View alertLayout = inflater.inflate(R.layout.student_details, null);
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView Sname = (TextView) alertLayout.findViewById(R.id.Sname);
                TextView Sdept = (TextView) alertLayout.findViewById(R.id.Sdept);
                TextView Syear = (TextView) alertLayout.findViewById(R.id.Syear);
                TextView Sregno = (TextView) alertLayout.findViewById(R.id.Sregno);
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                acctname = map.get("Name");
                String dept=map.get("Department");
                String year=map.get("Year");
                String regno=map.get("Register number");
                Sname.setText(acctname);
                Sdept.setText(dept);
                Syear.setText(year);
                Sregno.setText(regno);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Details");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);
        alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        AlertDialog dialog = alert.create();
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
    public void buttonClicked2(View view){
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.student_image, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);
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
    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();


        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

}
