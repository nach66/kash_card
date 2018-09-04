package com.example.admin.card;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class edit_listim extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mRef;
    String titl, tex, urmy, ur;
    ListView listView;
    TextView titi;
    int k,p,i;
    ArrayList<Note> notes;
    sListAdapter adapter;
    Note no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listim);
        try {
            k = getIntent().getExtras().getInt("which");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        titi = findViewById(R.id.ti);
        database = FirebaseDatabase.getInstance();
        notes = new ArrayList<>();
        adapter = new sListAdapter(this, notes);
        listView = findViewById(R.id.listv);

        listView.setAdapter(adapter);

        if (k == 1 || k == 4) {
            mRef = database.getReference("discounts");
            titi.setText("הטבות");
        }
        if (k == 2 || k == 5) {
            mRef = database.getReference("coupons");
            titi.setText("קופונים");
        }
        if (k == 3 || k == 6) {
            mRef = database.getReference("events");
            titi.setText("אירועים");
        }
        liste();
    }
    protected void liste() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Note m = postSnapshot.getValue(Note.class);
                    notes.add(m);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                no = notes.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(edit_listim.this);
                alert.setTitle(no.title);
                alert.setMessage(no.content);
                alert.setNegativeButton("מחיקה", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int w) {
                        mRef.child(no.title).removeValue();
                    }
                });
                alert.setPositiveButton("עריכה", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int w) {
                        Intent adn = new Intent(edit_listim.this, add.class);
                        adn.putExtra("key", 2);
                        adn.putExtra("tit", no.title);
                        adn.putExtra("te", no.content);
                        adn.putExtra("im", no.imgId);
                        adn.putExtra("ur", no.urlk);
                        startActivityForResult(adn, 1);
                    }
                });
                alert.setNeutralButton("חזרה", null);
                alert.create().show();
            }
        });
    }
    public void add(View view) {
        Intent adn = new Intent(edit_listim.this, add.class);
        adn.putExtra("key", 1);
        startActivityForResult(adn, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                titl = data.getStringExtra("a");
                tex = data.getStringExtra("b");
                urmy = data.getStringExtra("uri");
                ur = data.getStringExtra("ur");
                Note h = new Note();
                h.title = titl;
                h.content = tex;
                h.imgId = urmy;
                if (!ur.isEmpty()) {
                    h.theris = 1;
                    h.urlk = ur;
                }
                else h.theris = 0;
                DateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                h.date = timeFormat.format(new Date());

                mRef.child(h.title).setValue(h);
            }
        }
    }

    public static class Note {
        public String title;
        public String content;
        public String date;
        public String imgId;
        public String urlk;
        public int theris;

        public Note() {}
    }
    public static class sListAdapter extends ArrayAdapter<Note> {
        private ArrayList<Note> notess;

        private static class ViewHolder {
            TextView titleTV;
            ImageView iM;
        }

        public sListAdapter(Context context, ArrayList<Note> notess) {
            super(context, R.layout.item, notess);
            this.notess = notess;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Note m = getItem(position);
            sListAdapter.ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item, parent, false);
                viewHolder.titleTV = (TextView) convertView.findViewById(R.id.title);
                viewHolder.iM = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

   //         ImageLoader.getInstance().displayImage(notess.get(position).imgId, viewHolder.iM);
            viewHolder.titleTV.setText(m.title);
            return convertView;
        }
    }

    public boolean onOptionsItemSelected(MenuItem i){
        if (i.getItemId() == (int) R.id.menu_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(edit_listim.this,
                                    "You have been signed out.", Toast.LENGTH_LONG).show();
                            Intent yalla = new Intent(edit_listim.this, MainActivity.class);
                            startActivity(yalla);
                        }
                    });
        }

        if (i.getItemId() == (int) R.id.od) {
            Intent r = new Intent(edit_listim.this, odot.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.pl) {
            Intent r = new Intent(edit_listim.this, profile.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.im) {
            Intent r = new Intent(edit_listim.this, p_details.class);
            r.putExtra("k", 0);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.editor) {
            Intent r = new Intent(edit_listim.this, choose_edit.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.cht) {
            Intent r = new Intent(edit_listim.this, chat.class);
            startActivity(r);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().contains("8kashcard@gmail.com")
                && FirebaseAuth.getInstance().getCurrentUser().getUid().contains("uLHQ07ikeFVJUrlRZSTQHevuuNI3"))
            getMenuInflater().inflate(R.menu.manager, menu);
        else if (FirebaseAuth.getInstance().getCurrentUser().getEmail().contains("nach666@gmail.com")
                && FirebaseAuth.getInstance().getCurrentUser().getUid().contains("OK7wrC8LHuNbCF4sPK83WHyWKFi1"))
            getMenuInflater().inflate(R.menu.manager, menu);
        else
            getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
