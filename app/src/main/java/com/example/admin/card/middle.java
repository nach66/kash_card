package com.example.admin.card;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class middle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pres:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new lista()).addToBackStack(null)
                            .commit();
                    return true;
                case R.id.changes:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new listb()).addToBackStack(null)
                            .commit();
                    return true;
                case R.id.shows:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new listc()).addToBackStack(null)
                            .commit();
                    return true;
            }
            return false;
        }
    };

    public boolean onOptionsItemSelected(MenuItem i){
        if (i.getItemId() == (int) R.id.menu_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(middle.this,
                                    "You have been signed out.", Toast.LENGTH_LONG).show();
                            Intent yalla = new Intent(middle.this, MainActivity.class);
                            startActivity(yalla);
                        }
                    });
        }

        if (i.getItemId() == (int) R.id.pl) {
            Intent r = new Intent(middle.this, profile.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.im) {
            Intent r = new Intent(middle.this, p_details.class);
            r.putExtra("k", 0);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.editor) {
            Intent r = new Intent(middle.this, choose_edit.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.cht) {
            Intent r = new Intent(middle.this, chat.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.od) {
            Intent r = new Intent(middle.this, odot.class);
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