package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class BulletinPostAcc extends AppCompatActivity {

    private RecyclerView mPostlist;
    private DatabaseReference mDatabasePost;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mProcessLike = false;
    private FirebaseListAdapter<Message> adapter;
    private ActionBarDrawerToggle mToggle;
    private ImageView mNavProfImg;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TextView mNavUser;
    private String mTitle;
    private TextView mNavAcct_type;
    String get_IP_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletinpost);

        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(BulletinPostAcc.this, LogInAct.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }



                else {



                }
            }
        };



        final ProgressDialog progressDialog = new ProgressDialog(BulletinPostAcc.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();





        progressDialog.dismiss();

        mDatabasePost = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLike.keepSynced(true);
        mDatabaseUsers.keepSynced(true);
        mDatabasePost.keepSynced(true);

        mPostlist = (RecyclerView) findViewById(R.id.post_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mPostlist.setHasFixedSize(true);
        mPostlist.setLayoutManager(layoutManager);

        checkUserExist();

        mToolbar = (Toolbar) findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View navHeaderView = navigationView.getHeaderView(0);
        mNavProfImg = (ImageView) navHeaderView.findViewById(R.id.nav_prof_image);
        mNavUser = (TextView) navHeaderView.findViewById(R.id.nav_username);
        mNavAcct_type = (TextView) navHeaderView.findViewById(R.id.nav_acc_type);
        final Menu nav_Menu = navigationView.getMenu();

        mNavProfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent accprofIntent = new Intent(getApplicationContext(), Profile_Navigation2.class);
                startActivity(accprofIntent);



            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Users users = dataSnapshot.getValue(Users.class);
                    Glide.with(BulletinPostAcc.this).load(users.getProfImage()).into(mNavProfImg);
                    mNavUser.setText(users.getName());
                    mNavAcct_type.setText(users.getAccount_type());

                    //String get_IP_image = users.getProfImage().toString();
                    //   return get_IP_image;



/*---------------------------------------------------------------------------------------------*/

//
                    if (users.getAccount_type().equals("Treasurer")){


                        nav_Menu.findItem(R.id.fines).setVisible(true);

                        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.action_addpost);
                        fab.hide();
                        nav_Menu.findItem(R.id.promote).setVisible(false);

                        //   FloatingActionButton(R.id.action_addpost).setVisible(true);
                    }
                    else{


                        nav_Menu.findItem(R.id.fines).setVisible(false);
                        nav_Menu.findItem(R.id.promote).setVisible(true);
                    }

                    if (users.getAccount_type().equals("Student")){

                        nav_Menu.findItem(R.id.promote).setVisible(false);
                        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.action_addpost);
                        fab.hide();

                    }

/*----------------------------------------------------------------------------------------------------*/




/*----------------------------------------------------------------------------------------------------*/


                }
                else{
                    nav_Menu.findItem(R.id.promote).setVisible(true);
                }

            }

/*----------------------------------------------------------------------------------------------------*/



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                Intent navIntent = getIntent();




                FragmentManager fragmentManager = getSupportFragmentManager();

                if (id == R.id.bulletin_menu) {
                    // Handle the preference  action;
                    //  Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                    //   mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //   startActivity(mainIntent);
                } else if (id == R.id.messaging) {
                    // Handle the message action
                   Intent messageIntent = new Intent(BulletinPostAcc.this, Login_room.class);


                   // messageIntent.putExtra("get_IP_image", get_IP_image);
                   startActivity(messageIntent);
                }
                else if (id == R.id.about) {
                    // Handle the message action


                    Intent About = new Intent(BulletinPostAcc.this, About2.class);
                    startActivity(About);

                }
                else if (id == R.id.promote) {
                    // Handle the message action


                    Intent About = new Intent(BulletinPostAcc.this, students_list.class);
                    startActivity(About);

                }


                else if (id == R.id.fines) {
                    // Handle the message action


                    Intent finesIntent = new Intent(getApplicationContext(), students_list.class);
                     startActivity(finesIntent);

                }
                else if (id == R.id.acc_profile) {
                    Intent accprofIntent = new Intent(getApplicationContext(), Profile_Navigation2.class);
                    startActivity(accprofIntent);
                }
                else if (id == R.id.suggestions) {
                    String getAcct_type = mNavAcct_type.getText().toString();
                    if (getAcct_type.equals("Teacher")) {

                        Intent addsuggest = new Intent(BulletinPostAcc.this, Suggestion.class);
                        startActivity(addsuggest);
                    }
            else {
                        Intent addsuggest = new Intent(BulletinPostAcc.this, AddSuggestion.class);
                        startActivity(addsuggest);



                    }
                    }
                if(item.getItemId() == R.id.action_logout) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(BulletinPostAcc.this);
                    builder.setTitle("Log out?");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AuthUI.getInstance().signOut(BulletinPostAcc.this)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(BulletinPostAcc.this,"You have been signed out.",Toast.LENGTH_LONG).show();
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
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawerlayout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.action_addpost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BulletinPostAcc.this, PostAct.class));
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder> (
                Post.class,
                R.layout.post_row,
                PostViewHolder.class,
                mDatabasePost )
        {

            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
                final String post_key = getRef(position).getKey();
                    viewHolder.setUsername(model.getUsername());
                    viewHolder.setDateTime(model.getDateTime());
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setContent(model.getContent());
                    viewHolder.setLikeBtn(post_key);
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent SinglePostIntent = new Intent(BulletinPostAcc.this, SingleViewPost.class);
                            SinglePostIntent.putExtra("post_id", post_key);
                            startActivity(SinglePostIntent);
                        }
                    });

                    viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mProcessLike = true;
                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (mProcessLike) {
                                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mProcessLike = false;
                                        } else {
                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Random Value");
                                            mProcessLike = false;
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    });
            }
        };
        mPostlist.setAdapter(firebaseRecyclerAdapter);
    }

    private void checkUserExist() {
        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(user_id)) {
                        Intent setupIntent = new Intent(BulletinPostAcc.this, SetupAccount.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }




    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageButton mLikeBtn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        public PostViewHolder(View itemView) {



            super(itemView);
            mView = itemView;
            mLikeBtn = (ImageButton) mView.findViewById(R.id.like);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);
        }
        public void setTitle(String Title) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(Title);
        }
        public void setLikeBtn(final String post_key) {
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                        mLikeBtn.setImageResource(R.mipmap.ic_thumb_up_blue_24dp);
                    }
                    else {
                        mLikeBtn.setImageResource(R.mipmap.ic_thumb_up_gray_24dp);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        public void setContent(String Content) {
            TextView post_content = (TextView) mView.findViewById(R.id.post_content);
            post_content.setText(Content);
        }
        public void setUsername(String Username) {
            TextView post_username = (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(Username);
        }
        public void setDateTime(String DateTime) {
            TextView post_datetime = (TextView) mView.findViewById(R.id.post_datetime);
            post_datetime.setText(DateTime);
        }
        public void setImage(Context ctx, String Image) {

            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
                if (Image != null && ctx != null) {
                    post_image.setVisibility(View.VISIBLE);
                    Picasso.with(ctx).load(Image).into(post_image);
                } else if (Image == null) {
                    post_image.setVisibility(View.GONE);
                }
        }
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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){

            mDrawerLayout.closeDrawer(GravityCompat.START);

        }else{



            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Log out?");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    AuthUI.getInstance().signOut(BulletinPostAcc.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(BulletinPostAcc.this,"You have been signed out.",Toast.LENGTH_LONG).show();
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

