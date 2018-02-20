package com.example.micropulse7.cec_bulletin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micropulse7.cec_bulletin.models.Fines;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class AddFines extends AppCompatActivity {

    private String mUserkey = null;
    //private boolean mProcessFines = false;
    private DatabaseReference mDatabaseFines;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabasemyFines;
    private Button mAddfines;
    private EditText mFinesprogram;
    private EditText mFinesval;
    private FirebaseListAdapter<Fines> adapter3;
    private Button mRemoveFines;
    private ListView mFinesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_fines);

        mDatabaseFines = FirebaseDatabase.getInstance().getReference().child("Fines");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserkey = getIntent().getExtras().getString("stud_uid");
        mDatabasemyFines = FirebaseDatabase.getInstance().getReference().child("Fines").child(mUserkey);

        mAddfines = (Button) findViewById(R.id.addFinesBtn);
        mFinesprogram = (EditText) findViewById(R.id.prgram_field);
        mFinesval = (EditText) findViewById(R.id.value_field);

        mFinesList = (ListView) findViewById(R.id.fines_list);




        startupdatefineslist();


        mAddfines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String fines_program = mFinesprogram.getText().toString().trim();
                final String fines_value = mFinesval.getText().toString().trim();

                if(!TextUtils.isEmpty (fines_program)&& !TextUtils.isEmpty (fines_value)){

                    final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    final DatabaseReference newFines = mDatabaseFines.child(mUserkey).push();
                    final DatabaseReference mDatabaseUsername = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserkey);

                    mDatabaseFines.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mDatabaseUsername.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    newFines.child("Username").setValue(dataSnapshot.child("Name").getValue());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            newFines.child("DateTime").setValue(currentDateTimeString);
                            newFines.child("FinesProgram").setValue(fines_program);
                            newFines.child("FinesValue").setValue(fines_value);
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    Toast.makeText(AddFines.this,
                            "Error! Empty fields.. Both fields should not be empty..",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void startupdatefineslist(){

        adapter3 = new FirebaseListAdapter<com.example.micropulse7.cec_bulletin.models.Fines>(this,
                com.example.micropulse7.cec_bulletin.models.Fines.class,
                R.layout.addfines_holder,
                mDatabasemyFines) {
            @Override
            protected void populateView(final View v, final Fines model, int position) {

                final String fines_key = getRef(position).getKey();

                mRemoveFines = (Button) v.findViewById(R.id.remove_finesBtn);

                mRemoveFines.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabasemyFines.child(fines_key).removeValue();

                    }
                });

                mDatabasemyFines.addValueEventListener(new ValueEventListener() {
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
}
