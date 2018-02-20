package com.example.micropulse7.cec_bulletin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Login_room extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private ImageView mNavProfImg;
    private DatabaseReference mDatabaseUsers;

    private TextView mNavUser;
    private TextView mNavAcct_type;
    private FirebaseUser mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {







                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.register_room);



                final EditText Room = (EditText) dialog.findViewById(R.id.Room);

                final EditText RoomDescription = (EditText) dialog.findViewById(R.id.RoomDescription);

                final EditText RoomPassword = (EditText) dialog.findViewById(R.id.RoomPassword);

                Button btnDone = (Button) dialog.findViewById(R.id.Croom);
                Button Cancer = (Button) dialog.findViewById(R.id.Cancel);
                Cancer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();


                    }
                });

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        final ProgressDialog progressDialog = new ProgressDialog(Login_room.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Authenticating...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        final String Room_name = Room.getText().toString();
                        final String Room_password = RoomPassword.getText().toString();
                        final String Room_description = RoomDescription.getText().toString();


                        Response.Listener<String> responseListener = new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success){
                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {

                                                        progressDialog.dismiss();
                                                    }
                                                }, 3000);
                                        dialog.dismiss();

                                        Toast.makeText(Login_room.this,"Success.",Toast.LENGTH_LONG).show();


                                    }else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_room.this);
                                        builder.setMessage("Room created Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();

                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {

                                                        progressDialog.dismiss();
                                                    }
                                                }, 3000);


                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };


                        RoomRequest roomRequest = new RoomRequest(Room_name, Room_password, Room_description, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Login_room.this);
                        queue.add(roomRequest);



                    }
                });

                dialog.show();

            }
        });

final  EditText Room_name1 = (EditText) findViewById(R.id.R_name);
        final EditText Room_Security1 = (EditText) findViewById(R.id.R_Password);



        final Button R_Room= (Button) findViewById(R.id.Log_in);
        R_Room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String Room_name = Room_name1.getText().toString();
                final String Room_Security = Room_Security1.getText().toString();


                final ProgressDialog progressDialog = new ProgressDialog(Login_room.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();




                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {

                                String Room_num = jsonResponse.getString("Room_num");
                                String Room_name = jsonResponse.getString("Room_name");
                                String Std_id = jsonResponse.getString("Std_id");

                                String Std_name = jsonResponse.getString("Std_name");

                                String Room_info = jsonResponse.getString("Room_info");

                                String Room_Cdate = jsonResponse.getString("Room_Cdate");

                                String Room_Security = jsonResponse.getString("Room_Security");


                                Intent intent = new Intent(Login_room.this, MessageAct.class);
                                intent.putExtra("Room_num", Room_num);
                                intent.putExtra("Room_name", Room_name);
                                intent.putExtra("Std_id", Std_id);
                                intent.putExtra("Std_name", Std_name);
                                intent.putExtra("Room_info", Room_info);
                                intent.putExtra("Room_Cdate", Room_Cdate);
                                intent.putExtra("Room_Security", Room_Security);


                                Login_room.this.startActivity(intent);




                            } else {


                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {

                                                progressDialog.dismiss();
                                            }
                                        }, 3000);



                                AlertDialog.Builder builder = new AlertDialog.Builder(Login_room.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }






                    }
                };

                Log_in_room_request log_in_room_request = new Log_in_room_request(Room_name, Room_Security, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login_room.this);
                queue.add(log_in_room_request);








            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
                    Glide.with(Login_room.this).load(users.getProfImage()).into(mNavProfImg);
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
        getMenuInflater().inflate(R.menu.login_room, menu);
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

            //             mainIntent.putExtra("Student_status", Student_status);
            startActivity(mainIntent);
        } else if (id == R.id.messaging) {
            // Handle the message action

        }
        else if (id == R.id.about) {
            // Handle the message action


            Intent About = new Intent(Login_room.this, About2.class);
            startActivity(About);

        }

        else if (id == R.id.fines) {
            // Handle the message action


            Intent finesIntent = new Intent(getApplicationContext(), students_list.class);
            startActivity(finesIntent);

        }

        else if (id == R.id.promote) {
            // Handle the message action


            Intent About = new Intent(Login_room.this, students_list.class);
            startActivity(About);

        }
        else if (id == R.id.acc_profile) {
            Intent accprofIntent = new Intent(getApplicationContext(), Profile_Navigation2.class);
            startActivity(accprofIntent);
        }
        else if (id == R.id.suggestions) {
            Intent addsuggest = new Intent(Login_room.this, AddSuggestion.class);
            startActivity(addsuggest);
        }

        if(item.getItemId() == R.id.action_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Login_room.this);
            builder.setTitle("Log out?");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AuthUI.getInstance().signOut(Login_room.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Login_room.this,"You have been signed out.",Toast.LENGTH_LONG).show();
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
