package com.example.admin.card;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import android.content.ContentResolver; import android.content.Intent;
import android.net.Uri; import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle; import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button; import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class register extends AppCompatActivity {
    EditText tET, nET, bET, emailET, passwordET, confirmPasswordET;
    Button sendBtn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference mRef;
    String email;
    boolean p = false;

    ImageView i;
    private static final int GALLERY_INTENT = 2;
    private ProgressBar mpb;
    String chosenUri = null; //if null, not chosen yet, can't upload

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nET = (EditText) findViewById(R.id.naa);
        bET = (EditText) findViewById(R.id.date);
        tET = (EditText) findViewById(R.id.tz);
        emailET = (EditText) findViewById(R.id.email);
        passwordET = (EditText) findViewById(R.id.password);
        confirmPasswordET = (EditText) findViewById(R.id.confirmPassword);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");
        sendBtn = (Button) findViewById(R.id.save_user);
        sendBtn.setOnClickListener(loginListener);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            String confirmPassword = confirmPasswordET.getText().toString();
            final String date = bET.getText().toString();
            final String taz = tET.getText().toString();
            final String name = nET.getText().toString();
            if (email.isEmpty() || password.isEmpty()
                    || confirmPassword.isEmpty()
                    || name.isEmpty() || taz.isEmpty()
                    || date.isEmpty()) {
                emailET.setError("אנא מלא/י את כל הפרטים");
                return;
            }
            if (!password.equals(confirmPassword)) {
                    confirmPasswordET.setError("הסיסמאות לא תואמות");
                    return;
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                profile.You a = new profile.You();
                                a.tazz = taz;
                                a.namee = name;
                                a.bday = date;
                                a.uid = user.getUid();
                                a.email = user.getEmail();
                                a.send = false;
                                a.confirmed = false;
                                a.image = chosenUri;
                                DateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
                                a.createdate = timeFormat.format(new Date());

                                mRef.child(name).setValue(a);
                                Toast.makeText(register.this,
                                        "ברוך הבא " + name,
                                        Toast.LENGTH_LONG).show();
                                p= true;
                                if (p) {
                                    Intent yalla = new Intent(register.this, send_doc.class);
                                    yalla.putExtra("k", 0);
                                    startActivity(yalla);
                                }

                            } else {
                                Toast.makeText(register.this,
                                        "יצירת המשתמש נכשלה. ייתכן שקיים משתמש עם שם זהה, או שחלק מן הפרטים אינם נכונים",
                                        Toast.LENGTH_LONG).show();
                                Log.i("failed", task.getException().toString());
                            }
                        }
                    });
        }
    };

    public void upload(View view) {
        Intent yalla = new Intent(register.this, upload.class);
        startActivity(yalla);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            chosenUri = data.getStringExtra("uri");
        }
    }
}