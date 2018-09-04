package com.example.admin.card;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class add extends AppCompatActivity {
    EditText editText;
    EditText editTit;
    EditText editu;
    Button s;
    String a,b,c, url, urim;
    int k, id,p;
    Intent resulti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        editTit = findViewById(R.id.title);
        editText = findViewById(R.id.text);
        editu = findViewById(R.id.url);

        try {
            k = getIntent().getExtras().getInt("key");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (k == 2) {
            urim = getIntent().getStringExtra("im");
            c = getIntent().getStringExtra("tit");
            editTit.setText(getIntent().getStringExtra("tit"));
            editText.setText(getIntent().getStringExtra("te"));
            editu.setText(getIntent().getStringExtra("ur"));
        }
    }

    public void save(View view) {
        a = editTit.getText().toString();
        url = editu.getText().toString();
        if (a.isEmpty()) {
            editTit.setError("Title can't be empty");
            return;
        }
        if (k==2){
            if (!a.equals(c)) {
                editTit.setError("אי אפשר לשנות את הכותרת. אם ברצונך להשתמש בכותרת אחרת, יש ליצור פריט חדש");
                editTit.setText(c);
                return;
            }
        }
        b = editText.getText().toString();
        resulti = new Intent();
        resulti.putExtra("ur", url);
        resulti.putExtra("uri", urim);
        resulti.putExtra("a", a);
        resulti.putExtra("b", b);
        setResult(RESULT_OK, resulti);
        finish();
    }

    public void pick(View view) {
        Intent yalla = new Intent(add.this, upload.class);
        yalla.putExtra("key", 444);
        startActivityForResult(yalla, 88);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 88 && resultCode == RESULT_OK) {
            urim = data.getStringExtra("uri");
        }
    }
}

