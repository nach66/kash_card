package com.example.admin.card;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class justshow extends AppCompatActivity {
    TextView it, xt;
    int iff;
    Button buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justshow);
        it = findViewById(R.id.title);
        xt = findViewById(R.id.text);
        buy = findViewById(R.id.buy);
        it.setText(getIntent().getStringExtra("tit"));
        xt.setText(getIntent().getStringExtra("te"));
        iff = getIntent().getIntExtra("tis", 0);
        if (iff == 1)
            buy.setVisibility(View.VISIBLE);
    }

    public void goTourl(View view) {
        String uf = getIntent().getStringExtra("ur");
        goToUrl (uf);
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
