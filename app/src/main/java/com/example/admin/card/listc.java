package com.example.admin.card;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import android.app.Fragment;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class listc extends android.app.Fragment {
    FirebaseDatabase database;
    DatabaseReference mRef;
    String titl, tex, urmy;
    int k,p,i;
    TextView titi;
    listim.Note no;
    GridView gridView;
    ArrayList<listim.Note> notes;

    public listc() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.grid, container, false);
        titi = v.findViewById(R.id.ti);
        gridView = v.findViewById(R.id.gridD);
        database = FirebaseDatabase.getInstance();
        notes = new ArrayList<>();
        mRef = database.getReference("events");
        titi.setText("אירועים");
        initImageLoader();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    notes.add(snapshot.getValue(listim.Note.class));
                }
                gridView.setAdapter(new GridAdapter(getActivity(), notes));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                no = notes.get(position);
                Intent adn = new Intent(getActivity(), justshow.class);
                adn.putExtra("tit", no.title);
                adn.putExtra("te", no.content);
                adn.putExtra("im", no.imgId);
                adn.putExtra("tis", no.theris);
                adn.putExtra("ur", no.urlk);
                startActivity(adn);
            }
        });

        return v;
    }

    public void add(View view) {
        Intent adn = new Intent(getActivity(), add.class);
        adn.putExtra("key", 1);
        startActivityForResult(adn, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                titl = data.getStringExtra("a");
                tex = data.getStringExtra("b");
                urmy = data.getStringExtra("uri");
                listim.Note h = new listim.Note();
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

    private void initImageLoader() {
        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.load)
                .showImageForEmptyUri(R.mipmap.load)
                .showImageOnFail(R.mipmap.load).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(displayOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public class Note {
        public String title;
        public String content;
        public String date;
        public String imgId;
        public Note() {}
    }
    public class GridAdapter extends BaseAdapter {
        private ArrayList<listim.Note> notess;
        private Context context;
        public GridAdapter(Context context,  ArrayList<listim.Note> notess) {
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
                                300,
                                300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }
            ImageLoader.getInstance().displayImage(notess.get(position).imgId, imageView);
            return imageView;
        }
    }
}