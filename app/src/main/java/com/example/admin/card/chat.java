package com.example.admin.card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chat extends AppCompatActivity {

    DatabaseReference messagesRef;
    ListView listView;
    ArrayList<Message> messages;
    EditText messageBox;
    MessagesListAdapter adapter;
    String k, k2;
    static boolean curses = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            messages = new ArrayList<>();
            adapter = new MessagesListAdapter(this, messages);
            listView = findViewById(R.id.list_m);
            listView.setAdapter(adapter);
            messageBox = findViewById(R.id.input);

            messagesRef = database.getReference("messages");
            messagesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    messages.clear();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Message m = postSnapshot.getValue(Message.class);
                        messages.add(m);
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message m = new Message();
                    String key = messagesRef.push().getKey();
                    m.message = messageBox.getText().toString();
                    m.date = new Date();

                    k = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    k2 = k.replace("@gmail.com", "");
                    m.user = k2;

                    messagesRef.child(key).setValue(m);
                    messageBox.setText("");
                    closeKeyboard(v);
                }
            });
        }
        private void closeKeyboard(View view){
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        public static class Message{
            public String message;
            public String user;
            public Date date;
            public Message(){}
        }
        public static class MessagesListAdapter extends ArrayAdapter<Message> {
            private ArrayList<Message> messages;
            String k2, k3;
            private static class ViewHolder {
                TextView messageTV;
                TextView userTV;
                TextView dateTV;
            }
            public MessagesListAdapter(Context context, ArrayList<Message> messages) {
                super(context, R.layout.item_in_list, messages);
                this.messages = messages;
            }

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Message message = getItem(position);
                ViewHolder viewHolder;

                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_in_list, parent, false);
                    viewHolder.messageTV = (TextView) convertView.findViewById(R.id.textt);
                    viewHolder.dateTV = (TextView) convertView.findViewById(R.id.timee);
                    viewHolder.userTV = (TextView) convertView.findViewById(R.id.userr);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                if (!curses) {

                    if (message.message.contains("מנתצת השלשלאות") ||
                            message.message.contains("מלכת ים העשב הגדול") ||
                            message.message.contains("הבלתי נשרפת") ||
                            message.message.contains("אם הדרקונים")) {
                        viewHolder.messageTV.setTextColor(Color.RED);
                        viewHolder.messageTV.setTypeface(null, Typeface.BOLD);
                    }else {
                        viewHolder.messageTV.setTextColor(Color.BLACK);
                        viewHolder.messageTV.setTypeface(null, Typeface.NORMAL);
                    }

                    k2 = message.message.replace("אבדה קדברה", "***");
                    k3 = k2.replace("ראש כרוב", "***");
                    k2 = k3.replace("קרושיו", "***");
                    k3 = k2.replace("כוסאמק", "***");
                }
                else {
                    if (message.message.contains("אבדה קדברה") ||
                            message.message.contains("ראש כרוב") ||
                            message.message.contains("קרושיו") ||
                            message.message.contains("כוסאמק")) {
                        viewHolder.messageTV.setTextColor(Color.RED);
                        viewHolder.messageTV.setTypeface(null, Typeface.BOLD);
                    }else {
                        viewHolder.messageTV.setTextColor(Color.BLACK);
                        viewHolder.messageTV.setTypeface(null, Typeface.NORMAL);
                    }

                    k2 = message.message.replace("מנתצת השלשלאות", "***");
                    k3 = k2.replace("מלכת ים העשב הגדול", "***");
                    k2 = k3.replace("אם הדרקונים", "***");
                    k3 = k2.replace("הבלתי נשרפת", "***");
                }

                viewHolder.messageTV.setText(k3);
                viewHolder.dateTV.setText(dateFormatter.format(message.date));
                viewHolder.userTV.setText(message.user);

                return convertView;
            }
    }
}