package com.example.micropulse7.cec_bulletin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInAct extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mRegister;
    private Button mLoginBtn;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mErrormsg;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        context = this;
        mProgressDialog = new ProgressDialog(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mLoginBtn = (Button) findViewById(R.id.LoginBtn);
        mErrormsg = (TextView) findViewById(R.id.error_message);
        final TextView register = (TextView) findViewById(R.id.registerhere);
      //  final Button prac = (Button) findViewById(R.id.practicelang);
        final ImageView up = (ImageView) findViewById(R.id.up);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.custom_dialog);

                TextView tvSampleText = (TextView) dialog.findViewById(R.id.custom_dialog_tv_text);
                tvSampleText.setText("CRISTAL e-COLLEGE");

                Button btnDone = (Button) dialog.findViewById(R.id.custom_dialog_btn_done);

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        /*

        prac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                startActivity(new Intent(LogInAct.this, Room.class) );



            }
        });*/

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                startActivity(new Intent(LogInAct.this, GetID.class) );




            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
/*--------------------------------------------------------------------------------------------------*/


                String email =  mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();

                if(!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)) {

                    mProgressDialog.setMessage("Signing In...");
                    mProgressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                //mProgressDialog.dismiss();
                                checkUserExist();

                            }
                            else {
                                mProgressDialog.dismiss();
                                mErrormsg.setText("Error! Can't sign in. Try again later.");
                            }
                        }
                    });
                }
                else if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    mErrormsg.setText("");
                    mErrormsg.setText("Error! Email and Password field should not be empty.");
                }

/*-----------------------------------------------------------------------------------------------------*/


            }
        });

        }
    //private void checklogin() {}

    private void checkUserExist() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String HEmail = mEmailField.getText().toString();
                if(dataSnapshot.hasChild(user_id)){
                    mProgressDialog.dismiss();
                    cheking();
        }
                else{
                    mProgressDialog.dismiss();
                    Intent setupIntent = new Intent(LogInAct.this, SetupAccount.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    setupIntent.putExtra("HEmail", HEmail);
                    startActivity(setupIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cheking() {
    //
        String HEmail = mEmailField.getText().toString();


        mProgressDialog.dismiss();


/*----------------------------------------------------------------------------------------------------------------------------*/

//separating if student or teacher
        // final EditText getEmail = (EditText) findViewById(R.id.emailField);

        final String sendEmail = mEmailField.getText().toString();


        final ProgressDialog progressDialog = new ProgressDialog(LogInAct.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Checking Account...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {

                    final JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");


                    if (success) {
                        String id_number = jsonResponse.getString("id_number");
                        String Student_status = jsonResponse.getString("Student_status");
                        String First_name = jsonResponse.getString("First_name");
                        String Middle_nane = jsonResponse.getString("Middle_nane");
                        String Last_name = jsonResponse.getString("Last_name");
                        String Gender = jsonResponse.getString("Gender");
                        String Course = jsonResponse.getString("Course");
                        String Account_type = jsonResponse.getString("Account_type");
                        String App = jsonResponse.getString("App");
                        String HEmail = mEmailField.getText().toString();





                        mProgressDialog.dismiss();
                        Intent navIntent = new Intent(LogInAct.this, BulletinPostAcc.class);
                        navIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        navIntent.putExtra("HEmail", HEmail);
                        navIntent.putExtra("Student_status", Student_status);
                        finish();
                        startActivity(navIntent);

/*
                        if (Account_type.equals("Student")) {


                        }


                   */
                   /*     else {
                            mProgressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LogInAct.this);
                            builder.setMessage("Teacher")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }

                   */

                    }




                    else {
                        mProgressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LogInAct.this);
                        builder.setMessage("Account not register")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        EmailRequest emailRequest = new EmailRequest(sendEmail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LogInAct.this);
        queue.add(emailRequest);
/*-----------------------------------------------------------------------------------------------------------------------------*/



    }


    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit?");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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
