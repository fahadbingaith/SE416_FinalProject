package com.example.se416_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class firebase_select extends AppCompatActivity {
 EditText FirebaseSearchInput;
 ListView lv;
 ArrayList <User> users;
 private DatabaseReference mDatabase;
 private ValueEventListener ValListener;
 private int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_select);
        users = new ArrayList<>();
        lv=findViewById(R.id.selectSQlite);
        Button buttonSearch = findViewById(R.id.ButtonSearch);
        EditText firebaseSearchInput = findViewById(R.id.FirebaseSearchInput);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        BaseAdapter adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return users.size();
            }

            @Override
            public Object getItem(int position) {
                return users.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }



            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView txt= new TextView(firebase_select.this);
                txt.setTextSize(24);

                User u = users.get(position);


                txt.setText(u.getUserId()+"\n"+u.getFirstName()+"\n"+u.getLastName()+"\n"+u.getPhoneNumber()+"\n"+u.getEmailAddress());


                return txt;
            }
        };

        lv.setAdapter(adapter);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseSearchInput.getText().length() == 0){
                id = -1;
                }else {
                    id= Integer.valueOf(firebaseSearchInput.getText()+"");
                }

                mDatabase.removeEventListener(ValListener);
                ValListener = new ValListener();
                mDatabase.addValueEventListener(ValListener);
            }
        });
        ValListener = new ValListener();
        mDatabase.addValueEventListener(ValListener);

    }
class  ValListener implements ValueEventListener {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        try {
            users=snapshot.getValue(new GenericTypeIndicator<ArrayList<User>>() {});
        }catch (DatabaseException e ){
            users=new ArrayList<>();
            users.addAll(snapshot.getValue(new GenericTypeIndicator<HashMap<String,User>>() {}).values());
        }
        ArrayList<User> newList=new ArrayList();
        for(User u:users){
            if(u!=null){
                newList.add(u);
            }
        }
        users=newList;

        if(id==-1){
        }else{
         for (User u:users){
             if (id == u.userId){
                 users=new ArrayList<>();
                 users.add(u);
                 break;
             }
         }
        }
        ((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
}
