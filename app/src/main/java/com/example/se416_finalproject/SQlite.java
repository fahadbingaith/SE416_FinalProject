package com.example.se416_finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SQlite extends AppCompatActivity {

    Database_helper database_helper;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_qlite);

        Weather_container.MakeWeatherImage(this);

        Button sqliteAdd = findViewById(R.id.sqliteAdd);
        Button sqliteUpdate = findViewById(R.id.sqliteUpdate);
        Button sqliteDelete = findViewById(R.id.sqliteDelete);
        Button sqliteSelect = findViewById(R.id.sqliteSelect);
        Button sqliteAddFirebase = findViewById((R.id.sqliteAddFirebase));
        Button sqliteListview = findViewById(R.id.sqliteListview);


        EditText sqliteID = findViewById(R.id.sqliteID);
        EditText sqliteFname = findViewById(R.id.sqliteFname);
        EditText sqliteLname = findViewById(R.id.sqliteLname);
        EditText sqlitePhone = findViewById(R.id.sqlitePhone);
        EditText sqliteEmail = findViewById(R.id.sqliteEmail);

        database_helper =new Database_helper(this);
        reference = FirebaseDatabase.getInstance().getReference("users");
        sqliteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sqliteFname.getText().length()==0 || sqliteID.getText().length()==0||sqliteLname.getText().length()==0 || sqlitePhone.getText().length()==0 || sqliteEmail.getText().length()==0){
                    Toast.makeText(SQlite.this,"Fill in missing fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                 database_helper.insert(Integer.valueOf(sqliteID.getText()+""),
                         sqlitePhone.getText()+"",
                        sqliteFname.getText()+"",
                        sqliteLname.getText()+"",
                        sqliteEmail.getText()+"");
            }
        });
        sqliteUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sqliteFname.getText().length()==0 || sqliteID.getText().length()==0||sqliteLname.getText().length()==0 || sqlitePhone.getText().length()==0 || sqliteEmail.getText().length()==0 ){
                    Toast.makeText(SQlite.this,"Please fill-in missing fields(including id)",Toast.LENGTH_SHORT).show();
                    return;
                }
                database_helper.update(Integer.valueOf(sqliteID.getText()+""),

                        sqliteFname.getText()+"",
                        sqliteLname.getText()+"",
                        sqlitePhone.getText()+"",
                        sqliteEmail.getText()+"");
            }
        });
        sqliteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sqliteID.getText().length()==0 ){
                    Toast.makeText(SQlite.this,"Please fill-in the id field",Toast.LENGTH_SHORT).show();
                    return;
                }
                database_helper.delete(Integer.valueOf(sqliteID.getText()+""));
            }
        });

        sqliteSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sqliteID.getText().length()==0 ){
                    Toast.makeText(SQlite.this,"Please fill-in the id field",Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor c=database_helper.getSpecificResult(Integer.valueOf(sqliteID.getText()+""));
                if (c!=null){
                    System.out.println("select");
                    sqliteID.setText(c.getInt(0)+"");
                    sqliteFname.setText(c.getString(1));
                    sqliteLname.setText(c.getString(2));
                    sqlitePhone.setText(c.getString(3));
                    sqliteEmail.setText(c.getString(4));

                }else{
                    Toast.makeText(SQlite.this,"No field with id="+sqliteID.getText()+" was found.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        sqliteAddFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=sqliteID.getText()+"";
                if (uid.isEmpty() ){
                    Toast.makeText(SQlite.this,"Please fill in index field for this operation",Toast.LENGTH_SHORT).show();
                    return;
                }
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childData:snapshot.getChildren()){
                            if (childData.child("userId").getValue(Integer.class)==Integer.valueOf(uid)){
                                User u=snapshot.child(childData.getKey()).getValue(User.class);
                                database_helper.insert(u);
                                Toast.makeText(SQlite.this,"Entry added successfully",Toast.LENGTH_SHORT).show();
                                return;

                            }
                        }
                        Toast.makeText(SQlite.this,"No user with this UID was found",Toast.LENGTH_SHORT).show();
                        return;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        sqliteListview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SQlite.this,SQlite_Select.class));
            }
        });

    }

}