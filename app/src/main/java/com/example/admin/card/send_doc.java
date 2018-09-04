package com.example.admin.card;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class send_doc extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAnalytics mFirebaseAnalytics;
    FirebaseDatabase database;
    DatabaseReference mRef;

    Button d;
    String email;
    int k;
    boolean p;
    TextView h, h1, h2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_doc);
        p = false;

        try {
            k = getIntent().getExtras().getInt("k");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        d = findViewById(R.id.skip);
        h = findViewById(R.id.h);
        h1 = findViewById(R.id.h1);
        h2 = findViewById(R.id.h2);
        database = FirebaseDatabase.getInstance();
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if (k==0){ //חדש, צריך לשלוח מסמכים
            h.setVisibility(View.VISIBLE);
        }
        if (k==79){ //חודש אחרון
            h1.setVisibility(View.VISIBLE);
            d.setVisibility(View.VISIBLE);
        }
        if (k==100){ //עברה שנה צריך לחדש
            h2.setVisibility(View.VISIBLE);
        }
    }

   public void updoc(View view) {
       String user = firebaseAuth.getCurrentUser().getEmail();
       Intent yalla = new Intent(send_doc.this, upload.class);
       yalla.putExtra("key", 666);
       yalla.putExtra("m", user);
       startActivity(yalla);
   }

    public boolean onOptionsItemSelected(MenuItem i){
        if (i.getItemId() == (int) R.id.menu_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Toast.makeText(send_doc.this, "You have been signed out.", Toast.LENGTH_LONG).show();
                            Intent yalla = new Intent(send_doc.this, MainActivity.class);
                            startActivity(yalla);
                        }
                    });
        }

        if (i.getItemId() == (int) R.id.pl) {
            Intent r = new Intent(send_doc.this, profile.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.im) {
            Intent r = new Intent(send_doc.this, p_details.class);
            r.putExtra("k", 0);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.editor) {
            Intent r = new Intent(send_doc.this, choose_edit.class);
            startActivity(r);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (k==79)
            getMenuInflater().inflate(R.menu.main_menu, menu);
        else
            getMenuInflater().inflate(R.menu.bloked, menu);
        return true;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pres:
                    Intent r = new Intent(send_doc.this, listim.class);
                    r.putExtra("which", 1);
                    startActivity(r);
                    return true;
                case R.id.changes:
                    Intent t = new Intent(send_doc.this, listim.class);
                    t.putExtra("which", 2);
                    startActivity(t);
                    return true;
                case R.id.shows:
                    Intent y = new Intent(send_doc.this, listim.class);
                    y.putExtra("which", 3);
                    startActivity(y);
                    return true;
            }
            return false;
        }

    };

    public void daleg(View view) {
        Intent yalla = new Intent(send_doc.this, middle.class);
        startActivity(yalla);
    }
}