package com.example.admin.card;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class p_details extends AppCompatActivity {
    TextView nn, mm, ss, bb, cc, tt;
    int k;

    FirebaseDatabase database;
    DatabaseReference mRef;
    ArrayList<profile.You> notes;
    profile.uListAdapter adapter;
    profile.You me;
    String name,taz,bday,email;
    Button vv, xx;
  //  ImageView pic;
    String n, t, s, b, e,c,u;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            k = getIntent().getExtras().getInt("k");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (k == 99)
            setContentView(R.layout.activity_waiting);
        if (k == 0 || k == 1)
            setContentView(R.layout.activity_p_details);

     //   pic = findViewById(R.id.my_pic);
        vv = findViewById(R.id.ishur);
        xx = findViewById(R.id.bitul);
        database = FirebaseDatabase.getInstance();
        notes = new ArrayList<>();
        adapter = new profile.uListAdapter(this, notes);
        storageRef = FirebaseStorage.getInstance().getReference();
        next();
    }

    protected void next() {
        mRef = database.getReference("Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    me = postSnapshot.getValue(profile.You.class);
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().contains(me.email)
                            && FirebaseAuth.getInstance().getCurrentUser().getUid().contains(me.uid)) {
                        if (k == 0) { //עצמי
                            name = me.namee;
                            taz = "ת.ז: " + me.tazz;
                            bday = "תאריך לידה: " + me.bday;
                            email =  me.email;
                            doeverything();
                        }
                        if (k == 99) { //שלח מסמכים, מחכה לאישור, התאריך מתאפס
                            profile.You a = new profile.You();
                            a.tazz = me.tazz;
                            a.namee = me.namee;
                            a.bday = me.bday;
                            a.uid = me.uid;
                            a.email = me.email;
                            a.send = true;
                            a.confirmed = false;
                            DateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                            a.createdate = timeFormat.format(new Date());
                            mRef.child(me.namee).setValue(a);
                        }
                    }
                    notes.add(me);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        next2();
    }

    protected void next2() {
        if (k==1) { //הצגה ואישור המסמכים
             n = getIntent().getStringExtra("n");
             t = getIntent().getStringExtra("t");
             s = getIntent().getStringExtra("s");
             b = getIntent().getStringExtra("b");
             e = getIntent().getStringExtra("e");
             c = getIntent().getStringExtra("c");
             u = getIntent().getStringExtra("u");
             int status = getIntent().getIntExtra("STATUS", 0);

            name = n;
            taz = "ת.ז: " + t;
            bday = "תאריך לידה: " + b;
            email = e;
            cc = findViewById(R.id.c);
            String cday = "חבר/ה מאז: " + c;
            cc.setText(cday);

            if (status == 8)
                xx.setVisibility(View.VISIBLE);
            if (status == 0)
                vv.setVisibility(View.VISIBLE);
            vv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profile.You a = new profile.You();
                        a.tazz = t;
                        a.namee = n;
                        a.bday = b;
                        a.uid = u;
                        a.email = e;
                        a.send = true;
                        a.confirmed = true;
                        DateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                        a.createdate = timeFormat.format(new Date());
                        mRef.child(n).setValue(a);
                        Toast.makeText(p_details.this, "המשתמש הופעל", Toast.LENGTH_SHORT).show();
                    }
            });
            xx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profile.You a = new profile.You();
                        a.tazz = t;
                        a.namee = n;
                        a.bday = b;
                        a.uid = u;
                        a.email = e;
                        a.send = false;
                        a.confirmed = false;
                        DateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                        a.createdate = timeFormat.format(new Date());
                        mRef.child(n).setValue(a);
                        Toast.makeText(p_details.this, "המשתמש נחסם", Toast.LENGTH_SHORT).show();
                    }
            });
            doeverything();
        }
    }

    private void doeverything() {
        nn = findViewById(R.id.n);
        tt = findViewById(R.id.t);
        bb = findViewById(R.id.b);
        mm = findViewById(R.id.m);

        nn.setText(name);
        tt.setText(taz);
        bb.setText(bday);
        mm.setText(email);
    }

    public boolean onOptionsItemSelected(MenuItem i){
        if (i.getItemId() == (int) R.id.menu_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(p_details.this,
                                    "You have been signed out.", Toast.LENGTH_LONG).show();
                            Intent yalla = new Intent(p_details.this, MainActivity.class);
                            startActivity(yalla);
                        }
                    });
        }
        if (i.getItemId() == (int) R.id.od) {
            Intent r = new Intent(p_details.this, odot.class);
            startActivity(r);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (k==99)
            getMenuInflater().inflate(R.menu.bloked, menu);
        return true;
    }
}



