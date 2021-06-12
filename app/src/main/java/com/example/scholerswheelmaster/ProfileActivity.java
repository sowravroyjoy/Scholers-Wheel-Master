package com.example.scholerswheelmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView pName, pNumber, pEmail;
    private CardView pLogout, pMap;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference user_db;
    private String userID;
    private ProgressDialog progressDialog;
   // private String mName;
  //  private String mPhone;
   // private String mEmail;

    //private User currentUser;
    //private boolean logout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pName = findViewById(R.id.p_name);
        pNumber = findViewById(R.id.p_phone);
        pEmail = findViewById(R.id.p_email);
        pLogout = findViewById(R.id.p_logout);
        pMap = findViewById(R.id.p_map);

        firebaseAuth = FirebaseAuth.getInstance();
       user_db = FirebaseDatabase.getInstance().getReference();
        //userID = firebaseAuth.getCurrentUser().getUid();
       // user_db = FirebaseDatabase.getInstance().getReference().child("User");


        pLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout = true;
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        pMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;

            }
        });


        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        if(fuser!=null) {
            String uid = firebaseAuth.getCurrentUser().getUid();

            user_db.child("user").child("Student").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if(dataSnapshot.exists()) {

                           pName.setText(currentUser.getName());
                           pNumber.setText(currentUser.getPhone());
                           pEmail.setText(currentUser.getEmail());

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            user_db.child("user").child("Driver").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if(dataSnapshot.exists()) {

                            pName.setText(currentUser.getName());
                            pNumber.setText(currentUser.getPhone());
                            pEmail.setText(currentUser.getEmail());

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

}



