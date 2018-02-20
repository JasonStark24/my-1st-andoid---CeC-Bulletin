package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FinesProgram extends AppCompatActivity {

    private FirebaseListAdapter<Message> adapter;
    private EditText inputmsg;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseCurrentUser;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout2;
    private ActionBarDrawerToggle mToggle;
    private ImageView mNavProfImg;
    private TextView mNavUser;
    private TextView mNavAcct_type;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fines_program);
        final ProgressDialog progressDialog = new ProgressDialog(FinesProgram.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();



        final ListView nameP = (ListView) findViewById(R.id.Nprogram);



        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(FinesProgram.this, LogInAct.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
        else {
            final String user_id = mAuth.getCurrentUser().getUid();
            mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Name");
            mDatabaseCurrentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {



                    progressDialog.dismiss();



                    Toast.makeText(FinesProgram.this, "Welcome " +dataSnapshot.getValue(),Toast.LENGTH_LONG).show();
                    // Load chat room contents
                 //   displayProgram();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

/*------------------------------------------------------------------------------------------------------------------*/

        mToolbar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolbar);
        mDrawerLayout2 = (DrawerLayout) findViewById(R.id.message_drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout2, R.string.open, R.string.close);
        mDrawerLayout2.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);

        mNavProfImg = (ImageView) navHeaderView.findViewById(R.id.nav_prof_image);
        mNavUser = (TextView) navHeaderView.findViewById(R.id.nav_username);
        mNavAcct_type = (TextView) navHeaderView.findViewById(R.id.nav_acc_type);

        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Users users = dataSnapshot.getValue(Users.class);
                    Glide.with(FinesProgram.this).load(users.getProfImage()).into(mNavProfImg);
                    mNavUser.setText(users.getName());
                    mNavAcct_type.setText(users.getAccount_type());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

/*------------------------------------------------------------------------------------------------------------------*/


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (id == R.id.bulletin_menu) {
                    Intent mainIntent = new Intent(getApplicationContext(), BulletinPostAcc.class);
                    startActivity(mainIntent);

                } else if (id == R.id.messaging) {
                    //No Intent on Message activity when Message activity is active..
                }
                else if (id == R.id.acc_profile) {
                    Intent accprofIntent = new Intent(getApplicationContext(), BulletinPostAcc.class);
                    startActivity(accprofIntent);
                }
                if(item.getItemId() == R.id.action_logout) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FinesProgram.this);
                    builder.setTitle("Log out?");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AuthUI.getInstance().signOut(FinesProgram.this)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(FinesProgram.this,"You have been signed out.",Toast.LENGTH_LONG).show();
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
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.message_drawerlayout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

/*------------------------------------------------------------------------------------------------------------------*/


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Name");
                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        inputmsg = (EditText)findViewById(R.id.input);
                        String input = inputmsg.getText().toString();
                        if (!TextUtils.isEmpty(input)){
                            FirebaseDatabase.getInstance().getReference().child("Fines").push().setValue(new Message(input,
                                    dataSnapshot.getValue().toString())
                            );
                            // Clear the input
                            inputmsg.setText("");
                        }
                        else{
                            Toast.makeText(FinesProgram.this,"Error! getFines_Description is Empty.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });





/*------------------------------------------------------------------------------------------------------------------*/



/*------------------------------------------------------------------------------------------------------------------*/

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*---------------------------------------------------------------------------------------------------------*/


    @Override
    public void onBackPressed(){
        mDrawerLayout2 = (DrawerLayout) findViewById(R.id.message_drawerlayout);
        if (mDrawerLayout2.isDrawerOpen(GravityCompat.START)){

            mDrawerLayout2.closeDrawer(GravityCompat.START);

        }else{



            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Log out?");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    AuthUI.getInstance().signOut(FinesProgram.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(FinesProgram.this,"You have been signed out.",Toast.LENGTH_LONG).show();
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




    }
}
