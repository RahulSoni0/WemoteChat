package com.example.wemotechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    ListView discussions ;
    String Username;
    ArrayList<String> listofdiscussion = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    private DatabaseReference dbr ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        dbr = FirebaseDatabase.getInstance().getReference().getRoot();




   // for send notfication
        // add sdk ( Cloud Messaging )
        //get its instance and
        // make any topic so making it easy to filter target
   FirebaseMessaging.getInstance().subscribeToTopic("notifications");

    discussions = findViewById(R.id.lvDiscussionTopics);
    arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1, listofdiscussion);

     discussions.setAdapter(arrayAdapter);

     getUserName();

     dbr.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             Set<String> set = new HashSet<>();

             Iterator i = snapshot.getChildren().iterator();


             while(i.hasNext()){

                 set.add(((DataSnapshot)i.next()).getKey());

             }


             arrayAdapter.clear();
             arrayAdapter.addAll(set);
             arrayAdapter.notifyDataSetChanged();



         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });


     discussions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Intent i = new Intent(getApplicationContext(), Discussions.class);
             i.putExtra("selected_topic" , ((TextView)view).getText().toString());
             i.putExtra("user_name" , Username );
             startActivity(i);
         }
     });

    }


private void getUserName(){

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Enter user name");

    EditText username =  new EditText(this);

    builder.setView(username);
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Username = username.getText().toString();


        }
    });

    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            getUserName();

        }
    });
    builder.show();
}


}