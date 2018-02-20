package com.example.micropulse7.cec_bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileAct extends AppCompatActivity {

    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mAuth;
    private ImageView mProfimg;
    private TextView mProfname;
    private TextView mProfidnum;
    private TextView mProfcourse;
    private TextView mProfyearlvl;
    private TextView mProfsex;
    private TextView mProfdob;
    private TextView mProfaddress;
    private Button mProfUpdate;
    private DrawerLayout mDrawerLayout4;
    private ActionBarDrawerToggle mToggle;
   // public static Button mSubmitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileinfo);
/*
        mDrawerLayout4 = (DrawerLayout) findViewById(R.id.profileinfo);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout4, R.string.open, R.string.close);
        mDrawerLayout4.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/


        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mProfimg = (ImageView) findViewById(R.id.prof_img);
        mProfname = (TextView) findViewById(R.id.prof_name);
        mProfidnum = (TextView) findViewById(R.id.prof_idnum);
        mProfcourse = (TextView) findViewById(R.id.prof_course);
        mProfyearlvl = (TextView) findViewById(R.id.prof_yearlevel);
        mProfsex = (TextView) findViewById(R.id.prof_sex);
        mProfdob = (TextView) findViewById(R.id.prof_dob);
        mProfaddress = (TextView) findViewById(R.id.prof_address);
        mProfUpdate = (Button) findViewById(R.id.prof_update);


        mDatabaseUsers.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Users users = dataSnapshot.getValue(Users.class);
                    Glide.with(ProfileAct.this).load(users.getProfImage()).into(mProfimg);
                    mProfname.setText(users.getName());
                    mProfidnum.setText(users.getIdNumber());
                    mProfcourse.setText(users.getCourse());
                    mProfsex.setText(users.getGender());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




       mProfUpdate.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {
               Intent setupIntent = new Intent(ProfileAct.this, SetupAccount.class);
               startActivity(setupIntent);
           }
       });

    }


/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }*/
}
