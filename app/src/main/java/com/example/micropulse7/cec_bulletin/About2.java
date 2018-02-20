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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class About2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView mNavProfImg;
    private DatabaseReference mDatabaseUsers;

    private TextView mNavUser;
    private TextView mNavAcct_type;
    private FirebaseUser mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

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
                    Glide.with(About2.this).load(users.getProfImage()).into(mNavProfImg);
                    mNavUser.setText(users.getName());
                    mNavAcct_type.setText(users.getAccount_type());





/*---------------------------------------------------------------------------------------------*/


                    if (users.getAccount_type().equals("Treasurer")){

                        nav_Menu.findItem(R.id.promote).setVisible(false);

                        nav_Menu.findItem(R.id.fines).setVisible(true);
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
        getMenuInflater().inflate(R.menu.about2, menu);
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
            Intent messageIntent = new Intent(About2.this, Login_room.class);
            startActivity(messageIntent);
        }
        else if (id == R.id.about) {
            // Handle the message action
            Intent About = new Intent(About2.this, About2.class);
            startActivity(About);

        }

        else if (id == R.id.fines) {
            // Handle the message action
            Intent finesIntent = new Intent(getApplicationContext(), students_list.class);
            startActivity(finesIntent);


        }


        else if (id == R.id.promote) {
            // Handle the message action


            Intent About = new Intent(About2.this, students_list.class);
            startActivity(About);

        }

        else if (id == R.id.acc_profile) {
            Intent accprofIntent = new Intent(getApplicationContext(), Profile_Navigation2.class);
            startActivity(accprofIntent);
        }
        if(item.getItemId() == R.id.action_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(About2.this);
            builder.setTitle("Log out?");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AuthUI.getInstance().signOut(About2.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(About2.this,"You have been signed out.",Toast.LENGTH_LONG).show();
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
