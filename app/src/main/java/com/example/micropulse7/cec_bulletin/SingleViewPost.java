package com.example.micropulse7.cec_bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleViewPost extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase;

    private ImageView mPostSingleImage;
    private TextView mPostSingleTitle;
    private TextView mPostSingleContent;
    private TextView mPostSingleUsername;
    private TextView mPostSingleDateTime;
    private Button mSingleRemoveBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_view_post);

        //Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mAuth = FirebaseAuth.getInstance();
        mPost_key = getIntent().getExtras().getString("post_id");

        //Setting Values
        mPostSingleContent = (TextView) findViewById(R.id.single_postcontent);
        mPostSingleImage = (ImageView) findViewById(R.id.single_postimage);
        mPostSingleTitle = (TextView) findViewById(R.id.single_posttitle);
        mPostSingleUsername = (TextView) findViewById(R.id.single_postusername);
        mPostSingleDateTime = (TextView) findViewById(R.id.single_datetime);
        mSingleRemoveBtn = (Button) findViewById(R.id.single_RemoveBtn);


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Getting valiue from mPostKey
                String post_title = (String) dataSnapshot.child("Title").getValue();
                String post_content = (String) dataSnapshot.child("Content").getValue();
                String post_image = (String) dataSnapshot.child("Image").getValue();
                String post_username = (String) dataSnapshot.child("Username").getValue();
                String post_datetime = (String) dataSnapshot.child("DateTime").getValue();
                String post_uid = (String) dataSnapshot.child("UserID").getValue();

                mPostSingleTitle.setText(post_title);
                mPostSingleContent.setText(post_content);
                Picasso.with(SingleViewPost.this).load(post_image).into(mPostSingleImage);
                mPostSingleDateTime.setText(post_datetime);
                mPostSingleUsername.setText(post_username);

                if(mAuth.getCurrentUser().getUid().equals(post_uid)){
                    mSingleRemoveBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(mPost_key).removeValue();
                Intent mainIntent = new Intent (SingleViewPost.this, BulletinPostAcc.class);
                startActivity(mainIntent);
            }
        });

    }
}
