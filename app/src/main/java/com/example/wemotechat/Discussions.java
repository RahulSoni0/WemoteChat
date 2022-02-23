package com.example.wemotechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Discussions extends AppCompatActivity {

    ListView lv ;
    EditText et_msg ;
    Button btn_send ;

    ArrayList<String> listConversation = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    String UserName , SelectedTopic , user_msg_key ;

    private DatabaseReference dbr ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussions);

        lv = findViewById(R.id.listview);
        et_msg = findViewById(R.id.et_typemsg);
        btn_send = findViewById(R.id.button_send);

        Bundle extras = getIntent().getExtras();

        UserName = extras.getString("user_name");
       SelectedTopic = extras.getString("selected_topic");
       setTitle("Topic: " + SelectedTopic);

      arrayAdapter = new ArrayAdapter(this ,
              android.R.layout.simple_list_item_1 , listConversation);
        lv.setAdapter(arrayAdapter);
   dbr = FirebaseDatabase.getInstance().getReference().child(SelectedTopic);

    btn_send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Map<String , Object> map = new HashMap<>( );
            user_msg_key = dbr.push().getKey();

            dbr.updateChildren(map);


            DatabaseReference db2 = dbr.child(user_msg_key);
            Map<String, Object> map2 = new HashMap<>( );
            map2.put("msg" , et_msg.getText().toString() );
            map2.put("user" , UserName);
            db2.updateChildren(map2);

            et_msg.setText("");



        }
    });

    dbr.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            updateConversation(snapshot);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            updateConversation(snapshot);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });



    }

    private void updateConversation(DataSnapshot snapshot) {
       String msg , username , conversation ;

        Iterator i = snapshot.getChildren().iterator();
        while (i.hasNext()){

            msg = (String) ((DataSnapshot)i.next()).getValue();
            username = (String) ((DataSnapshot)i.next()).getValue();
            conversation = username + ": " + msg ;
            arrayAdapter.insert(conversation ,  0 );




        }


    }
}