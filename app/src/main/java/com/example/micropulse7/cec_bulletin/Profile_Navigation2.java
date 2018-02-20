package com.example.micropulse7.cec_bulletin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.micropulse7.cec_bulletin.models.Fines;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Navigation2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView mNavProfImg;
    private DatabaseReference mDatabaseUsers;
   // private String mUserkey = null;
   private DatabaseReference mDatabaseFines;
    private TextView mNavUser;
    private TextView mNavAcct_type;
    private FirebaseUser mAuth;
    private ImageView mProfimg;
    private TextView mProfname;
    private TextView mProfidnum;
    private TextView mProfcourse;
    private TextView fullname;
    private TextView mProfsex;
    private TextView id_number22;
    private TextView Course2;
    private TextView Email;
    private Button mProfUpdate;
    private DrawerLayout mDrawerLayout4;
    private ActionBarDrawerToggle mToggle;
    private ListView mFinesList;
    private FirebaseListAdapter<Fines> adapter3;
    private DatabaseReference mDatabasemyFines;
    private Button mRemoveFines;
    private Button mAddfines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__navigation2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseFines = FirebaseDatabase.getInstance().getReference().child("Fines").child(mAuth.getUid());
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mProfimg = (ImageView) findViewById(R.id.prof_img);
        mProfname = (TextView) findViewById(R.id.prof_name);
        mProfidnum = (TextView) findViewById(R.id.prof_idnum);
        mProfcourse = (TextView) findViewById(R.id.prof_course);
        fullname = (TextView) findViewById(R.id.fullname);
        mProfsex = (TextView) findViewById(R.id.prof_sex);
        id_number22 = (TextView) findViewById(R.id.id_number2);
        Course2 = (TextView) findViewById(R.id.Course);
        Email = (TextView) findViewById(R.id.Gmail);
        mProfUpdate = (Button) findViewById(R.id.prof_update);
        mFinesList = (ListView) findViewById(R.id.fines_list2);
        startupdatefineslist();


        mDatabaseUsers.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Users users = dataSnapshot.getValue(Users.class);
                    Glide.with(Profile_Navigation2.this).load(users.getProfImage()).into(mProfimg);
                    mProfname.setText(users.getName());
                    id_number22.setText(users.getIdNumber());
                    mProfcourse.setText(users.getCourse());
                    mProfsex.setText(users.getGender());
                    fullname.setText(users.getName());
                    Course2.setText(users.getCourse());
                    Email.setText(users.getAccount_type()
                    );

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mProfUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent setupIntent = new Intent(Profile_Navigation2.this, SetupAccount.class);
                startActivity(setupIntent);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        //mDatabaseFines = FirebaseDatabase.getInstance().getReference().child("Fines");

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
//        mDatabasemyFines = FirebaseDatabase.getInstance().getReference().child("Fines").child();

        View navHeaderView = navigationView.getHeaderView(0);
        mNavProfImg = (ImageView) navHeaderView.findViewById(R.id.nav_prof_image);
        mNavUser = (TextView) navHeaderView.findViewById(R.id.nav_username);
        mNavAcct_type = (TextView) navHeaderView.findViewById(R.id.nav_acc_type);
        final Menu nav_Menu = navigationView.getMenu();

        mDatabaseUsers.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Users users = dataSnapshot.getValue(Users.class);
                    Glide.with(Profile_Navigation2.this).load(users.getProfImage()).into(mNavProfImg);
                    mNavUser.setText(users.getName());
                    mNavAcct_type.setText(users.getAccount_type());

/*---------------------------------------------------------------------------------------------*/
                    if (users.getAccount_type().equals("Treasurer")){
                        nav_Menu.findItem(R.id.fines).setVisible(true);

                        nav_Menu.findItem(R.id.promote).setVisible(false);

                    }
                    else{
                        nav_Menu.findItem(R.id.promote).setVisible(true);

                        nav_Menu.findItem(R.id.fines).setVisible(false);


                    }


                    if (users.getAccount_type().equals("Student")){

                        nav_Menu.findItem(R.id.promote).setVisible(false);

                    }

/*----------------------------------------------------------------------------------------------------*/

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mNavProfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accprofIntent = new Intent(getApplicationContext(), Profile_Navigation2.class);
                startActivity(accprofIntent);

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void startupdatefineslist(){

        adapter3 = new FirebaseListAdapter<com.example.micropulse7.cec_bulletin.models.Fines>(this,
                com.example.micropulse7.cec_bulletin.models.Fines.class,
                R.layout.addfines_holder2,
                mDatabaseFines) {
            @Override
            protected void populateView(final View v, final Fines model, int position) {

                mDatabaseFines.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        TextView datetime = (TextView) v.findViewById(R.id.datetime_fines);
                        TextView username_fines = (TextView) v.findViewById(R.id.username_fines);
                        TextView program_fines = (TextView) v.findViewById(R.id.program_fines);
                        TextView fines_value = (TextView) v.findViewById(R.id.fines_value);

                        datetime.setText(model.getDateTime());
                        username_fines.setText(model.getUsername());
                        program_fines.setText(model.getFinesProgram());
                        fines_value.setText(model.getFinesValue());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mFinesList.setAdapter(adapter3);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile__navigation2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.bulletin_menu) {
            Intent mainIntent = new Intent(getApplicationContext(), BulletinPostAcc.class);
            startActivity(mainIntent);

        } else if (id == R.id.messaging) {
            Intent messageIntent = new Intent(Profile_Navigation2.this, Login_room.class);
            startActivity(messageIntent);
        }
        else if (id == R.id.about) {
            // Handle the message action
            Intent About = new Intent(Profile_Navigation2.this, About2.class);
            startActivity(About);

        }

        else if (id == R.id.promote) {
            // Handle the message action


            Intent About = new Intent(Profile_Navigation2.this, students_list.class);
            startActivity(About);

        }

        else if (id == R.id.fines) {
            // Handle the message action
            Intent finesIntent = new Intent(getApplicationContext(), students_list.class);
            startActivity(finesIntent);


        }
        else if (id == R.id.acc_profile) {
        }
        else if (id == R.id.suggestions) {
            String getAcct_type = mNavAcct_type.getText().toString();
            if (getAcct_type.equals("Teacher")) {

                Intent addsuggest = new Intent(Profile_Navigation2.this, Suggestion.class);
                startActivity(addsuggest);
            }
            else {
                Intent addsuggest = new Intent(Profile_Navigation2.this, AddSuggestion.class);
                startActivity(addsuggest);



            }
        }

        if(item.getItemId() == R.id.action_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Navigation2.this);
            builder.setTitle("Log out?");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AuthUI.getInstance().signOut(Profile_Navigation2.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Profile_Navigation2.this,"You have been signed out.",Toast.LENGTH_LONG).show();
                                    // Close activity
                                    finish();
                                }
                            });

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
