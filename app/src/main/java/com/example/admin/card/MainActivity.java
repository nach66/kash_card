package com.example.admin.card;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText emailET, passwordET;
    Button sendBtn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference mRef;
    ArrayList<profile.You> notes;
    profile.uListAdapter adapter;
    profile.You me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   Intent yalla = new Intent(MainActivity.this, upload.class);
      //  startActivity(yalla);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");
        notes = new ArrayList<>();
        adapter = new profile.uListAdapter(this, notes);

        if (firebaseAuth.getCurrentUser() != null)
                myref_users();
        else {
            setContentView(R.layout.activity_main);
            emailET = (EditText) findViewById(R.id.email);
            passwordET = (EditText) findViewById(R.id.password);
            sendBtn = (Button) findViewById(R.id.send);
            sendBtn.setOnClickListener(loginListener);
        }
    }

    private void closeKeyboard(View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public int daysBetween(Date d1, Date d2){
            return (int)( (d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
    }

    private void myref_users() {
        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().contains("8kashcard@gmail.com")
                && FirebaseAuth.getInstance().getCurrentUser().getUid().contains("uLHQ07ikeFVJUrlRZSTQHevuuNI3")) {
            Intent yalla = new Intent(MainActivity.this, middle.class);
            startActivity(yalla);
        }
        else if (FirebaseAuth.getInstance().getCurrentUser().getEmail().contains("nach666@gmail.com")
                && FirebaseAuth.getInstance().getCurrentUser().getUid().contains("OK7wrC8LHuNbCF4sPK83WHyWKFi1")) {
            Intent yalla = new Intent(MainActivity.this, middle.class);
            startActivity(yalla);
        }
        else {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notes.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        me = postSnapshot.getValue(profile.You.class);
                        if (user.getEmail().contains(me.email)
                                && user.getUid().contains(me.uid)) {

                            DateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                            String today = timeFormat.format(new Date());
                            Date date1 = null, date2 = null;
                            try {
                                date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(today);
                                date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(me.createdate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (!me.send && !me.confirmed) { //חדש לגמרי
                                Intent yalla = new Intent(MainActivity.this, send_doc.class);
                                yalla.putExtra("k", 0);
                                startActivity(yalla);
                            }
                            if (me.send && !me.confirmed && daysBetween(date1, date2) <= 350) { //שלח מסמכים וממתין
                                Intent yalla = new Intent(MainActivity.this, waiting.class);
                                startActivity(yalla);
                            }
                            if (me.send && me.confirmed
                                    && daysBetween(date1, date2) <= 320) { //פעיל רגיל
                                Intent yalla = new Intent(MainActivity.this, middle.class);
                                startActivity(yalla);
                            }

                            if (me.confirmed &&
                                    daysBetween(date1, date2) > 320
                                    && daysBetween(date1, date2) <= 350) {//עומד לפוג
                                Intent yalla = new Intent(MainActivity.this, send_doc.class);
                                yalla.putExtra("k", 79);
                                startActivity(yalla);
                            }

                            if (daysBetween(date1, date2) > 350) {//פג
                                Intent adn = new Intent(MainActivity.this, send_doc.class);
                                adn.putExtra("k", 100);
                                startActivity(adn);
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
        }
    }

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            if (email.isEmpty()) {
                emailET.setError("email can't be empty");
                return;
            }
            if (password.isEmpty()) {
                passwordET.setError("password can't be empty");
                return;
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                                        Toast.makeText(MainActivity.this,
                                                "logged in with user " + user.getEmail(),
                                                Toast.LENGTH_LONG).show();
                                        myref_users();

                                    } else {
                                        Log.i("failed", "cant log in");
                                        Toast.makeText(MainActivity.this, "Login failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
            closeKeyboard(v);
        }
    };

    public void register(View view) {
        Intent la = new Intent(MainActivity.this, register.class);
        startActivity(la);
    }

    public void forgeti(View view) {
        String email = emailET.getText().toString();
        if (email.isEmpty()) {
            emailET.setError("לאן לשלוח? נא לכתוב את המייל איתו נרשמת");
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
       // String emailAddress = "user@example.com";

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "הסיסמה נשלחה למייל",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}