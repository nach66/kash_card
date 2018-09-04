package com.example.admin.card;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class profile extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mRef;
    ListView listView;
    ArrayList<You> userim;
    uListAdapter adapter;
    TextView titi;
    private You me;
    int k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_only);

        database = FirebaseDatabase.getInstance();
        userim = new ArrayList<>();
        adapter = new uListAdapter(this, userim);
        listView = findViewById(R.id.listv);
        listView.setAdapter(adapter);
        titi = findViewById(R.id.ti);
        titi.setText("משתמשים רשומים");

        mRef = database.getReference("Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userim.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    me = postSnapshot.getValue(You.class);
                    userim.add(me);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        show();
    }

    private void show() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (k == 0) {
                    You no = userim.get(position);
                    Intent adn = new Intent(profile.this, p_details.class);
                    adn.putExtra("k", 1);
                    adn.putExtra("n", no.namee);
                    adn.putExtra("t", no.tazz);
                    adn.putExtra("b", no.bday);
                    adn.putExtra("e", no.email);
                    adn.putExtra("c", no.createdate);
                    adn.putExtra("u", no.uid);
                    adn.putExtra("i", no.image);
                    if (no.confirmed && no.send)
                        adn.putExtra("STATUS", 8);
                    else
                        adn.putExtra("STATUS", 0);
                    startActivity(adn);
                }
            }
        });
    }

    public static class You {
        public String tazz;
        public String uid;
        public String namee;
        public String bday;
        public String email;
        public String createdate;
        public boolean send;
        public boolean confirmed;
        public String image;
        public You() {}
    }
    public static class uListAdapter extends ArrayAdapter<You> {
        private ArrayList<You> notess;
        private static class ViewHolder {
            TextView na;
            TextView em;
        }
        public uListAdapter(Context context, ArrayList<You> notess) {
            super(context, R.layout.show_profile, notess);
            this.notess = notess;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            You m = getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new uListAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.show_profile, parent, false);
                viewHolder.na = (TextView) convertView.findViewById(R.id.titlee);
                viewHolder.em = (TextView) convertView.findViewById(R.id.emaill);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (!m.confirmed && m.send)
                convertView.setBackgroundColor(Color.GREEN);
            if (!m.send)
                convertView.setBackgroundColor(Color.RED);
            viewHolder.na.setText(m.namee);
            viewHolder.em.setText(m.email);
            return convertView;
        }
    }
}