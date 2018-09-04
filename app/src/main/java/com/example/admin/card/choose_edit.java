package com.example.admin.card;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class choose_edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_edit);
    }

    public void dis(View view) {
        Intent r = new Intent(choose_edit.this, edit_listim.class);
        r.putExtra("which", 4);
        startActivity(r);
    }

    public void cop(View view) {
        Intent r = new Intent(choose_edit.this, edit_listim.class);
        r.putExtra("which", 5);
        startActivity(r);
    }

    public void show(View view) {
        Intent r = new Intent(choose_edit.this, edit_listim.class);
        r.putExtra("which", 6);
        startActivity(r);
    }

    public boolean onOptionsItemSelected(MenuItem i){
        if (i.getItemId() == (int) R.id.menu_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(choose_edit.this,
                                    "You have been signed out.", Toast.LENGTH_LONG).show();
                            Intent yalla = new Intent(choose_edit.this, MainActivity.class);
                            startActivity(yalla);
                        }
                    });
        }

        if (i.getItemId() == (int) R.id.pl) {
            Intent r = new Intent(choose_edit.this, profile.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.im) {
            Intent r = new Intent(choose_edit.this, p_details.class);
            r.putExtra("k", 0);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.editor) {
            Intent r = new Intent(choose_edit.this, choose_edit.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.cht) {
            Intent r = new Intent(choose_edit.this, chat.class);
            startActivity(r);
        }
        if (i.getItemId() == (int) R.id.od) {
            Intent r = new Intent(choose_edit.this, odot.class);
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
