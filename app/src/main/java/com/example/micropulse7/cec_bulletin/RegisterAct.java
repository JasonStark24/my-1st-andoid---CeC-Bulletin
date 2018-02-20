package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAct extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText mPasswordField;
    private EditText mEmailField;
    //private EditText mNameField;
    // private TextView mLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.registerBtn);

        mPasswordField = (EditText) findViewById(R.id.Register_passwordField);
        mEmailField = (EditText) findViewById(R.id.Register_emailField);



        //mNameField = (EditText) findViewById(R.id.Register_nameField);
        // mLogin = (TextView) findViewById(R.id.loginhere_text);

        buttonRegister.setOnClickListener(this);
        // mLogin.setOnClickListener(this);




    /*--------------------------------------------------------------------------------------*/
        Intent intent = getIntent();
        final String id_number = intent.getStringExtra("id_number");
        final String Student_status = intent.getStringExtra("Student_status");
        final String First_name = intent.getStringExtra("First_name");
        final String Middle_nane = intent.getStringExtra("Middle_nane");
        final String Last_name = intent.getStringExtra("Last_name");
        final String Gender = intent.getStringExtra("Gender");
        final String Course = intent.getStringExtra("Course");
        final String Account_type = intent.getStringExtra("Account_type");
        final String App = intent.getStringExtra("App");
        final String HEmail = mEmailField.getText().toString();

/*---------------------------------------------------------------------------------------------------------------------------*/

        final TextView welcomeMessage = (TextView) findViewById(R.id.message);



        String setmessage = "hai "+ First_name+" " + Middle_nane+"  " +Last_name +" welcome to CeC Bulletin plss Sign Up your  Email and Password ";
        welcomeMessage.setText(setmessage);





    }


   /*---------------------------------------------------------------------------------------------------------------------------*/




    private void registeruser() {

        //final String name = mNameField.getText().toString().trim();
        final String email = mEmailField.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgressDialog.setMessage("Signing Up...");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Intent intent = getIntent();
                    final String id_number = intent.getStringExtra("id_number");
                    final String Student_status = intent.getStringExtra("Student_status");
                    final String First_name = intent.getStringExtra("First_name");
                    final String Middle_nane = intent.getStringExtra("Middle_nane");
                    final String Last_name = intent.getStringExtra("Last_name");
                    final String Gender = intent.getStringExtra("Gender");
                    final String Course = intent.getStringExtra("Course");
                    final String Account_type = intent.getStringExtra("Account_type");
                    final String App = intent.getStringExtra("App");
                    final String HEmail = mEmailField.getText().toString();



                    if(task.isSuccessful()){




                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);

                        //current_user_db.child("Name").setValue(name);
                        //current_user_db.child("Image").setValue("default");

                        mProgressDialog.dismiss();
                        Toast.makeText(RegisterAct.this,"Success! Now Setup your Profile Information!", Toast.LENGTH_SHORT).show();


                        Intent statuss = new Intent(RegisterAct.this, SetupAccount.class);
                        if (App.equals("Register")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAct.this);
                            builder.setMessage("ID is already register")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();

/*----------------------------------ProgressDialog------------------------------------------------------------------------*/
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {

                                            mProgressDialog.dismiss();
                                        }
                                    }, 3000);


/*---------------------------------End-of-ProgressDialog------------------------------------------------------------------------*/
                        }


                        else {

                            statuss.putExtra("HEmail", HEmail);
                            statuss.putExtra("id_number", id_number);
                            statuss.putExtra("Student_status", Student_status);
                            statuss.putExtra("First_name", First_name);
                            statuss.putExtra("Middle_nane", Middle_nane);
                            statuss.putExtra("Last_name", Last_name);
                            statuss.putExtra("Gender", Gender);
                            statuss.putExtra("Course", Course);
                            statuss.putExtra("Account_type", Account_type);
                            statuss.putExtra("App", App);

                            RegisterAct.this.startActivity(statuss);

                            //startActivity(new Intent(RegisterActivity.this, SetupAccActivity.class));
                            finish();




                        }




                    }
                    else{
                        mProgressDialog.hide();
                        Toast.makeText(RegisterAct.this,"Failed to Register!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(RegisterAct.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onClick(View view){
        if(view == buttonRegister){
            registeruser();
        }
        //  if(view == mLogin){
        //    startActivity(new Intent(RegisterActivity.this, LogInActivity.class) );
        //}
    }

}
