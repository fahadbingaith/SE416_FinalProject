package com.example.se416_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class firebase extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText firebaseIndex;
    EditText firebaseID;
    EditText firebaseFname;
    EditText firebaseLname;
    EditText firebasePhone;
    EditText firebaseEmail;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

         Weather_container.MakeWeatherImage(this);

         Button firebaseSelect= findViewById(R.id.sqliteSelect);
        Button firebaseAdd= findViewById(R.id.sqliteAdd);
        Button firebaseDelete= findViewById(R.id.sqliteDelete);
        Button firebaseUpdate= findViewById(R.id.sqliteUpdate);

        EditText firebaseIndex = findViewById(R.id.firebaseIndex);
        EditText firebaseID = findViewById(R.id.sqliteID);
        EditText firebaseFname = findViewById(R.id.sqliteFname);
        EditText firebaseLname = findViewById(R.id.sqliteLname);
        EditText firebasePhone = findViewById(R.id.sqlitePhone);
        EditText firebaseEmail = findViewById(R.id.sqliteEmail);


        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        firebaseSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(firebase.this,firebase_select.class));
            }
        });

        firebaseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseIndex.getText().length()==0 || firebaseID.getText().length()==0||firebaseFname.getText().length()==0 || firebaseLname.getText().length()==0 || firebasePhone.getText().length()==0||firebaseEmail.getText().length()==0){
                    Toast.makeText(firebase.this,"Empty inputs",Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference insertRef=mDatabase.child(firebaseIndex.getText()+"");

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(firebaseIndex.getText()+"")){
                            Toast.makeText(firebase.this,"Index "+firebaseIndex.getText()+" already exists.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        insertRef.child("emailAddress").setValue(firebaseEmail.getText()+"");
                        insertRef.child("firstName").setValue(firebaseFname.getText()+"");
                        insertRef.child("lastName").setValue(firebaseLname.getText()+"");
                        insertRef.child("phoneNumber").setValue(firebasePhone.getText()+"");
                        insertRef.child("userId").setValue(Integer.valueOf(firebaseID.getText()+""));
                        Toast.makeText(firebase.this,"Inserted successfully.",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        firebaseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseIndex.getText().length()==0){
                    Toast.makeText(firebase.this,"Empty index",Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference deleteref =mDatabase.child(firebaseIndex.getText()+"");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.hasChild(firebaseIndex.getText()+"")){
                        Toast.makeText(firebase.this,"There is no data with this index",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    deleteref.setValue(null);
                        Toast.makeText(firebase.this,"Deleted successfully",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }
        });

        firebaseUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseIndex.getText().length()==0){
                    Toast.makeText(firebase.this,"Empty index",Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference updateref=mDatabase.child(firebaseIndex.getText()+"");

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(firebaseIndex.getText()+"")){
                           Toast.makeText(firebase.this,"no data found",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (firebaseEmail.getText().length()!=0){
                            updateref.child("emailAddress").setValue(firebaseEmail.getText()+"");
                        }
                        if (firebaseFname.getText().length()!=0){
                            updateref.child("firstName").setValue(firebaseFname.getText()+"");
                        }
                        if (firebaseLname.getText().length()!=0){
                            updateref.child("lastName").setValue(firebaseLname.getText()+"");
                        }
                        if (firebasePhone.getText().length()!=0){
                            updateref.child("phoneNumber").setValue(firebasePhone.getText()+"");
                        }
                        if (firebaseID.getText().length()!=0){
                            updateref.child("userId").setValue(Integer.valueOf(firebaseID.getText()+""));
                        }
                        Toast.makeText(firebase.this,"Updated successfully.",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }

}

@IgnoreExtraProperties
class User {

    public int userId;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String emailAddress;

    public User(int userId, String firstName, String lastName, String phoneNumber, String emailAddress) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {

        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {

        this.emailAddress = emailAddress;
    }

    public User(){

    }


}