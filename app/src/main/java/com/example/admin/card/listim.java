package com.example.admin.card;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
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
import android.widget.BaseAdapter;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class listim extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mRef;
    String titl, tex, urmy;
    int k,p,i;
    TextView titi;
    Note no;
    GridView gridView;
    ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        initImageLoader();
        try {
            k = getIntent().getExtras().getInt("which");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        titi = findViewById(R.id.ti);
        gridView = findViewById(R.id.gridD);
        database = FirebaseDatabase.getInstance();
        notes = new ArrayList<>();

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
                notes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    notes.add(snapshot.getValue(Note.class));
                }
                gridView.setAdapter(new GridAdapter(listim.this, notes));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                no = notes.get(position);
                Intent adn = new Intent(listim.this, justshow.class);
                adn.putExtra("tit", no.title);
                adn.putExtra("te", no.content);
                adn.putExtra("im", no.imgId);
                startActivity(adn);
            }
        });
    }

    public void add(View view) {
        Intent adn = new Intent(listim.this, add.class);
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
                Note h = new Note();
                h.title = titl;
                h.content = tex;
                h.imgId = urmy;
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
    public class GridAdapter extends BaseAdapter {
        private ArrayList<Note> notess;
        private Context context;
        public GridAdapter(Context context,  ArrayList<Note> notess) {
            this.context = context;
            this.notess = notess;
        }
        @Override
        public int getCount() {
            return notess.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public Object getItem(int position) {
            return notess.get(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(
                        new GridView.LayoutParams(
                                290,
                                290));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }
            ImageLoader.getInstance().displayImage(notess.get(position).imgId, imageView);
            return imageView;
        }
    }

    private void initImageLoader() {
        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher_foreground)
                .showImageForEmptyUri(R.drawable.ic_launcher_background)
                .showImageOnFail(R.drawable.ic_launcher_background).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(displayOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public boolean onOptionsItemSelected(MenuItem i){
        if (i.getItemId() == (int) R.id.menu_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(listim.this,
                                    "You have been signed out.", Toast.LENGTH_LONG).show();
                            Intent yalla = new Intent(listim.this, MainActivity.class);
                            startActivity(yalla);
                        }
                    });
        }
        if (i.getItemId() == (int) R.id.pl) {
            Intent r = new Intent(listim.this, profile.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.im) {
            Intent r = new Intent(listim.this, p_details.class);
            r.putExtra("k", 0);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.editor) {
            Intent r = new Intent(listim.this, choose_edit.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.cht) {
            Intent r = new Intent(listim.this, chat.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.od) {
            Intent r = new Intent(listim.this, odot.class);
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