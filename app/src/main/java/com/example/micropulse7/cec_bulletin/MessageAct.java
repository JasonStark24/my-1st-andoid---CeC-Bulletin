package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class MessageAct extends AppCompatActivity {

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
        setContentView(R.layout.messagedisp);
        final ProgressDialog progressDialog = new ProgressDialog(MessageAct.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(MessageAct.this, LogInAct.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
        else {
            final String user_id = mAuth.getCurrentUser().getUid();
            mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Name");
            mDatabaseCurrentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  //  Users users = dataSnapshot.getValue(Users.class);

                    //String get_IP_image = users.getProfImage().toString();


                    progressDialog.dismiss();



                    Toast.makeText(MessageAct.this, "Welcome " +dataSnapshot.getValue(),Toast.LENGTH_LONG).show();
                    // Load chat room contents

                    String string = "This string will be printed by methodOne";
                    displayChatMessages(string);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }






        mToolbar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolbar);
        mDrawerLayout2 = (DrawerLayout) findViewById(R.id.message_drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout2, R.string.open, R.string.close);

        mDrawerLayout2.addDrawerListener(mToggle);
        mToggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);

        mNavProfImg = (ImageView) navHeaderView.findViewById(R.id.nav_prof_image);
        mNavUser = (TextView) navHeaderView.findViewById(R.id.nav_username);
        mNavAcct_type = (TextView) navHeaderView.findViewById(R.id.nav_acc_type);
        final Menu nav_Menu = navigationView.getMenu();

        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Users users = dataSnapshot.getValue(Users.class);
                    Glide.with(MessageAct.this).load(users.getProfImage()).into(mNavProfImg);
                    mNavUser.setText(users.getName());
                    mNavAcct_type.setText(users.getAccount_type());

/*---------------------------------------------------------------------------------------------*/


                    if (users.getAccount_type().equals("Treasurer")){


                        nav_Menu.findItem(R.id.fines).setVisible(true);
                        nav_Menu.findItem(R.id.promote).setVisible(false);


                    }
                    else{


                        nav_Menu.findItem(R.id.fines).setVisible(false);
                        nav_Menu.findItem(R.id.promote).setVisible(true);

                    }
                    if (users.getAccount_type().equals("Student")){

                        nav_Menu.findItem(R.id.promote).setVisible(false);

                    }
                    else{
                        nav_Menu.findItem(R.id.promote).setVisible(true);
                    }



/*----------------------------------------------------------------------------------------------------*/
                    Intent intent = getIntent();
                    String Room_name = intent.getStringExtra("Room_name");


                    final TextView Room_title = (TextView) findViewById(R.id.Room_title);
                    Room_title.setText(Room_name);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    //    Intent navIntent = getIntent();

    //   final String Student_status = navIntent.getStringExtra("Student_status");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (id == R.id.bulletin_menu) {
                    Intent mainIntent = new Intent(getApplicationContext(), BulletinPostAcc.class);

       //             mainIntent.putExtra("Student_status", Student_status);
                    startActivity(mainIntent);

                } else if (id == R.id.messaging) {
                    //No Intent on Message activity when Message activity is active..
                }


                else if (id == R.id.about) {
                    // Handle the message action


                    Intent About = new Intent(MessageAct.this, About2.class);
                    startActivity(About);

                }
                else if (id == R.id.acc_profile) {
                    Intent accprofIntent = new Intent(MessageAct.this, Profile_Navigation2.class);
                    startActivity(accprofIntent);
                }


                else if (id == R.id.suggestions) {
                    String getAcct_type = mNavAcct_type.getText().toString();
                    if (getAcct_type.equals("Teacher")) {

                        Intent addsuggest = new Intent(MessageAct.this, Suggestion.class);
                        startActivity(addsuggest);
                    }
                    else {
                        Intent addsuggest = new Intent(MessageAct.this, AddSuggestion.class);
                        startActivity(addsuggest);



                    }
                }

                else if (id == R.id.fines) {
                    // Handle the message action


                    Intent finesIntent = new Intent(getApplicationContext(), students_list.class);
                    startActivity(finesIntent);

                }


                if(item.getItemId() == R.id.action_logout) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MessageAct.this);
                    builder.setTitle("Log out?");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AuthUI.getInstance().signOut(MessageAct.this)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MessageAct.this,"You have been signed out.",Toast.LENGTH_LONG).show();
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




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            Intent intent = getIntent();
            String Room_name = intent.getStringExtra("Room_name");
            String Room_Security = intent.getStringExtra("Room_Security");

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
                            FirebaseDatabase.getInstance().getReference().child("Messages").child(Room_name).push().setValue(new Message(input,
                                    dataSnapshot.getValue().toString())
                            );
                            // Clear the input
                            inputmsg.setText("");
                        }
                        else{
                            Toast.makeText(MessageAct.this,"Error! Message is Empty.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


        });




    }
    private void displayChatMessages(String string) {


        Intent intent = getIntent();
        String Room_name = intent.getStringExtra("Room_name");
        String Room_Security = intent.getStringExtra("Room_Security");

        final ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Message>(this,
                Message.class,
                R.layout.message,
                FirebaseDatabase.getInstance().getReference().child("Messages").child(Room_name)) {
            @Override
            protected void populateView(View v, Message model, int position) {
                LinearLayout layot = (LinearLayout) v.findViewById(R.id.message_field);
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageName = (TextView) v.findViewById(R.id.message_email);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                TextView arrow = (TextView) v.findViewById(R.id.arrow_up);

                //  final ImageView image_profile = (ImageView) v.findViewById(R.id.profile);
                String name2 = model.getMessageEmail();
                String message2 = model.getMessageText();
       //      name3.equals("Jason Gales Calope");
            //   String profile = model.getimage_profile();
                //  Glide.with(MessageAct.this).load(Message.getProfImage()).into(messageprofile);
               // messageprofile.setBackground(Drawable.createFromPath(get_IP_image));
                messageText.setText(message2 );

//                messageText.setTextColor(Color.RED);
            //    messageText.setBackgroundResource(R.drawable.buttommessage);
                if (mNavUser.getText().toString().equals(name2)) {
//                    messageText.setBackgroundColor(Color.RED);
                    messageText.setBackgroundResource(R.drawable.buttommessage);

                    messageName.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                    messageName.setTextColor(Color.GRAY);
                    messageTime.setTextColor(Color.BLACK);
                    messageTime.setText(name2);
                    messageTime.setTypeface(null, Typeface.BOLD);

                    messageTime.setGravity(Gravity.END);
                    //int TEXT_ALIGNMENT_CENTER
                    arrow.setGravity(Gravity.END);
                    arrow.setTextColor(Color.parseColor("#10dfee"));
                    //arrow.setTextColor();
                }else{


                    messageName.setText(name2);
                    messageName.setTypeface(null, Typeface.BOLD);
                    messageName.setTextColor(Color.BLACK);
                    messageTime.setTextColor(Color.GRAY);
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

                    // messageText.setBackgroundResource(R.drawable.buttommessage);
                    messageText.setBackgroundColor(Color.GRAY);
                    messageText.setTextColor(Color.WHITE);
                    messageText.setGravity(Gravity.START);
                    arrow.setGravity(Gravity.START);
                    arrow.setTextColor(Color.GRAY);
                }

                /* if (!name2.equals("Jason Gales Calope")) {
//                    messageName.setVisibility(View.GONE);
                    messageText.setVisibility(View.GONE);
    //                messageTime.setVisibility(View.GONE);
      //              layot.setVisibility(View.GONE);
                }*/






            }

        };

        listOfMessages.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
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

                    AuthUI.getInstance().signOut(MessageAct.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MessageAct.this,"You have been signed out.",Toast.LENGTH_LONG).show();
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
