package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class Promote_Account extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog progress;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote__account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
final TextView exist = (TextView) findViewById(R.id.exist_treasurer);
        final TextView newT = (TextView) findViewById(R.id.new_treasurer);

        final Button Dpromote = (Button) findViewById(R.id.unpromote);
        final Button Npromote = (Button) findViewById(R.id.promote);

Npromote.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {







        final String Std_id = exist.getText().toString();
        final String promote = "Treasurer";
        final ProgressDialog progressDialog = new ProgressDialog(Promote_Account.this);
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


/*
                        AlertDialog.Builder builder = new AlertDialog.Builder(Promote_Account.this);
                        builder.setMessage(" Success")
                                .setNegativeButton("ok", null)
                                .create()
                                .show();
*/
firebaseupdateAccount();


                    } else {

                        progressDialog.dismiss();



                        AlertDialog.Builder builder = new AlertDialog.Builder(Promote_Account.this);
                        builder.setMessage(" Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }






            }
//---------------------------------------------------------update_firebase
            private void firebaseupdateAccount() {
                final  TextView idnum_newTres = (TextView) findViewById(R.id.new_treasurer);
                mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
                mDatabaseUsers.addChildEventListener(new ChildEventListener() {
                    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        if (dataSnapshot.child("Account_type").getValue().equals("Treasurer")) {


        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});




            }
        };

        Update_request update_request = new Update_request(Std_id, promote, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Promote_Account.this);
        queue.add(update_request);







    }
});


        /*--------------------------------------------------------------------------------------------------*/
        Dpromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String Std_id = exist.getText().toString();
                final String promote = "Student";
                final ProgressDialog progressDialog = new ProgressDialog(Promote_Account.this);
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



                                AlertDialog.Builder builder = new AlertDialog.Builder(Promote_Account.this);
                                builder.setMessage(" Success")
                                        .setNegativeButton("ok", null)
                                        .create()
                                        .show();


                            } else {

                                progressDialog.dismiss();



                                AlertDialog.Builder builder = new AlertDialog.Builder(Promote_Account.this);
                                builder.setMessage(" Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }






                    }
                };

                Update_request update_request = new Update_request(Std_id, promote, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Promote_Account.this);
                queue.add(update_request);





            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        getMenuInflater().inflate(R.menu.promote__account, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
