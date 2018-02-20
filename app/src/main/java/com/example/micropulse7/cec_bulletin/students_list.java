package com.example.micropulse7.cec_bulletin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micropulse7.cec_bulletin.models.Users;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class students_list extends AppCompatActivity {

    private DatabaseReference mDatabaseUsers;
    private ListView studentlist;
    private FirebaseListAdapter<Users> adapter2;
    private Button mAddFines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        studentlist = (ListView) findViewById(R.id.user_list);

        adapter2 = new FirebaseListAdapter<Users>(this,
                Users.class,
                R.layout.user_name_and_id,
                mDatabaseUsers){
            @Override
            protected void populateView(final View v, final Users model, int position) {
                final String uid_key = getRef(position).getKey();

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(students_list.this, "User id: " + uid_key, Toast.LENGTH_SHORT).show();


                      v.setOnLongClickListener(new View.OnLongClickListener() {
                          @Override
                          public boolean onLongClick(View view) {




                              AlertDialog.Builder builder = new AlertDialog.Builder(students_list.this);
                              builder.setTitle("Promotion is triggered by Long press");
                              builder.setMessage("Promote this Student?");
                              builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int which) {

                                      Intent promote = new Intent(students_list.this, Promote.class);
                                      promote.putExtra("stud_uid", uid_key);
                                      startActivity(promote);

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
                              return false;
                          }
                      });

                    }
                });

                mAddFines = (Button) v.findViewById(R.id.add_fines_btn);
                mAddFines.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent addfines2 = new Intent(students_list.this, AddFines.class);
                        addfines2.putExtra("stud_uid", uid_key);
                        startActivity(addfines2);
                    }
                });


                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String acct_type = dataSnapshot.child(uid_key).child("Account_type").getValue().toString();
                        if (acct_type.equals("Student") || acct_type.equals("Treasurer")) {

                            TextView stud_id = (TextView) v.findViewById(R.id.stud_list_id);
                            TextView stud_name = (TextView) v.findViewById(R.id.stud_list_name);

                            stud_name.setText(model.getName());
                            stud_id.setText(model.getIdNumber());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        studentlist.setAdapter(adapter2);




    }
}
