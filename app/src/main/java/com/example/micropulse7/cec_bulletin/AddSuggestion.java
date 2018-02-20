package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class AddSuggestion extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseSuggest;
    private ProgressDialog mProgessDialog;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private TextView mContentSuggestion;
    private Button mSendSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suggestion);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseSuggest = FirebaseDatabase.getInstance().getReference().child("Suggestion");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        mSendSuggestion = (Button) findViewById(R.id.addSuggestBtn);
        mContentSuggestion = (TextView) findViewById(R.id.suggestContent);
        mSendSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSuggestion();
            }

            private void sendSuggestion() {


              //  mProgessDialog.setMessage("Sending your suggestion");

                final String suggest_content = mContentSuggestion.getText().toString().trim();

                if(!TextUtils.isEmpty (suggest_content)){

                    final DatabaseReference newSuggestion = mDatabaseSuggest.push();
                    final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                 //   mProgessDialog.show();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newSuggestion.child("Content").setValue(suggest_content);
                            newSuggestion.child("UserID").setValue(mCurrentUser.getUid());
                            newSuggestion.child("DateTime").setValue(currentDateTimeString);
                            newSuggestion.child("Username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                    }
                                }
                            });
                         //   mProgessDialog.dismiss();

                            Toast.makeText(AddSuggestion.this, "Success! Suggestion is added.", Toast.LENGTH_SHORT).show();

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else if(TextUtils.isEmpty (suggest_content)){
                    //for toast.
                    Toast.makeText(AddSuggestion.this, "Error! No content to send!", Toast.LENGTH_SHORT).show();
                }

                //Intent suggestion = new Intent(AddSuggestion.this, Suggestion.class );
                //startActivity(suggestion);

            }
        });

    }
}
