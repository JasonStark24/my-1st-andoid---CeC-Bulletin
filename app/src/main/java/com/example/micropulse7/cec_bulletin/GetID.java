package com.example.micropulse7.cec_bulletin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GetID extends AppCompatActivity {


    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_id);


        final Button grab = (Button) findViewById(R.id.GrabID);


        grab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final EditText Eid = (EditText) findViewById(R.id.SId);

                final String id_number = Eid.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(GetID.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                Response.Listener<String> responseListener = new Response.Listener<String>(){


                    @Override
                    public void onResponse(String response) {


                        try{


                            final JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");






                            if (success) {
                                Intent intent = new Intent(GetID.this, RegisterAct.class);
                                String id_number = jsonResponse.getString("id_number");
                                String Student_status = jsonResponse.getString("Student_status");
                                String First_name = jsonResponse.getString("First_name");
                                String Middle_nane = jsonResponse.getString("Middle_nane");
                                String Last_name = jsonResponse.getString("Last_name");
                                String Gender = jsonResponse.getString("Gender");
                                String Course = jsonResponse.getString("Course");
                                String Account_type = jsonResponse.getString("Account_type");

                                String App = jsonResponse.getString("App");



                                if (App.equals("Register")){
                                     AlertDialog.Builder builder = new AlertDialog.Builder(GetID.this);
                                    builder.setMessage("ID is already register")
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

                                else {



                                    intent.putExtra("id_number", id_number);
                                    intent.putExtra("Student_status", Student_status);
                                    intent.putExtra("First_name", First_name);
                                    intent.putExtra("Middle_nane", Middle_nane);
                                    intent.putExtra("Last_name", Last_name);
                                    intent.putExtra("Gender", Gender);
                                    intent.putExtra("Course", Course);
                                    intent.putExtra("Account_type", Account_type);

                                    intent.putExtra("App", App);
                                    GetID.this.startActivity(intent);


                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {

                                                    progressDialog.dismiss();
                                                }
                                            }, 3000);



                                }


                            }
                            else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(GetID.this);
                                builder.setMessage("ID not Exist in School")
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


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };



                LoginRequest loginRequest = new LoginRequest(id_number, responseListener);
                RequestQueue queue = Volley.newRequestQueue(GetID.this);
                queue.add(loginRequest);

            }


        });
    }

}
