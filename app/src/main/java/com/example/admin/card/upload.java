package com.example.admin.card;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.IOException;
import java.util.Random;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.Random;
import static android.app.Activity.RESULT_OK;

import static android.app.Activity.RESULT_OK;

public class upload extends AppCompatActivity {
    ImageView img;
    Button btns, btn;
    Uri chosenUri = null; //if null, not chosen yet, can't upload
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressBar progressBar;
    int k;
    String mail, a, b;
    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        img = findViewById(R.id.img);
        img.setOnClickListener(imgClick);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(btnClick);
        progressBar = findViewById(R.id.progressBar);

        k = getIntent().getExtras().getInt("key");
        if (k == 666) {
            mail = getIntent().getExtras().getString("m");
            btns = findViewById(R.id.btn_save_img);
            btns.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
    };

    View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(chosenUri == null) {
                Toast.makeText(upload.this,
                        "לא נבחר קובץ.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            if (k== 666) {
                a = mail.replace("@","_");
                b = a.replace(".com","");
                ref = storage.getReference()
                        .child("Docs")
                        .child(b)
                        .child(""+System.currentTimeMillis());
//                        .child(new Random().nextLong() + "");
            }
            if (k== 444) {
                ref = storage.getReference()
                        .child("Logo")
                        .child(""+System.currentTimeMillis());
            }
            UploadTask uploadTask = ref.putFile(chosenUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 5000);
                    progressBar.setVisibility(View.GONE);
                        Toast.makeText(upload.this, "התמונה נשמרה", Toast.LENGTH_LONG).show();
                    if (k != 666) {
                        addUrlToDB(ref);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    img.setImageBitmap(null);
                    Toast.makeText(upload.this, "אוף", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double prgs = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) prgs);
                }
            });
        }
    };

    private void addUrlToDB(StorageReference ref) {
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                        Intent resulti = new Intent();
                        resulti.putExtra("uri", uri.toString());
                        setResult(RESULT_OK, resulti);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(upload.this, "סאמקק", Toast.LENGTH_LONG).show();
                }
            });
    }

    public void save_all_doc(View view) {
        if (k==666) {
            if (chosenUri != null) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(upload.this);
                alert1.setTitle("רגע!");
                alert1.setMessage("בטוח ששלחת את כל המסמכים הדרושים?");
                alert1.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent yalla = new Intent(upload.this, p_details.class);
                        yalla.putExtra("k", 99);
                        startActivity(yalla);
                    }
                });
                alert1.setNeutralButton("לא", null);
                alert1.create().show();

            } else Toast.makeText(upload.this, "לא נשלחו קבצים", Toast.LENGTH_LONG).show();
        }
        if (k==444)
            finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            chosenUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), chosenUri);
                img.setImageBitmap(bitmap);
                } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
