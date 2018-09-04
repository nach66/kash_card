package com.example.admin.card;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class odot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odot);
    }

    public void goTof(View view) {
        goToUrl ( "https://www.facebook.com/tozeretkash/");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
