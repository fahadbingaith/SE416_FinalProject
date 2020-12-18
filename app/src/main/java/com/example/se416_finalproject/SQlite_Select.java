package com.example.se416_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SQlite_Select extends AppCompatActivity {
ListView lv;
ArrayList <User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_qlite__select);
        lv=findViewById(R.id.selectSQlite);
        users = new ArrayList<>();
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
                TextView txt= new TextView(SQlite_Select.this);
                txt.setTextSize(24);

                User u = users.get(position);


                txt.setText(u.getUserId()+"\n"+u.getFirstName()+"\n"+u.getLastName()+"\n"+u.getPhoneNumber()+"\n"+u.getEmailAddress());


                return txt;
            }
        };

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u = users.get(position);
                Toast.makeText(SQlite_Select.this,u.firstName+" "+u.lastName, Toast.LENGTH_SHORT).show();
            }
        });
        Database_helper db = new Database_helper(this);

        Cursor c = db.selectAll();

        if (c!=null){
            do {

            User u = new User();

                System.out.println("select");
                u.setUserId(c.getInt(0));
               u.setFirstName(c.getString(1));
               u.setLastName(c.getString(2));
                u.setPhoneNumber(c.getString(3));
               u.setEmailAddress(c.getString(4));
                users.add(u) ;
            }while (c.moveToNext());
            adapter.notifyDataSetChanged();
        }
    }
}