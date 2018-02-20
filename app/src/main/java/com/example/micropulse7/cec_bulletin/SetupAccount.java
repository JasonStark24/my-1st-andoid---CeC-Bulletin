package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class SetupAccount extends AppCompatActivity {

    //Variable Declaration
    private ImageButton mSetupImgBtn;
    private TextView mNameField;
    public static Button mSubmitBtn;
    //  public static Button mUpdateBtn;
    private Uri mImageUri = null;
    private static final int GALLERY_REQUEST = 2;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private StorageReference mStorageImage;

    private ProgressDialog mProgress;
    //   private Spinner mSex;
    // private Spinner mSpinnerSetup_YearLevel;
    private Spinner mSpinnerSetupCourse;
    private EditText mIdNumber;
    //  private EditText mAddress;
    // private Button mBirthdayBtn;
    // private TextView mBirthday;
    private ArrayAdapter<CharSequence> adapter;
    private AdapterView<?> Parent;
    private int Position;
    static final int DATE_DIALOG_ID = 0;
    private int mYear,mMonth,mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_acc);

        //Setting values
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_Images");
        mProgress = new ProgressDialog(this);
        mSetupImgBtn = (ImageButton)findViewById(R.id.SetupImageBtn);
        final TextView mNameField = (TextView) findViewById(R.id.SetupNameField);

        //  mAddress = (EditText) findViewById(R.id.setup_Address);
        final EditText mIdNumber = (EditText) findViewById(R.id.Setup_IdNumber);
        final TextView mcourse = (TextView) findViewById(R.id.setup_Course2);
        final TextView msex = (TextView) findViewById(R.id.setup_Sex2);
        final TextView maccountype = (TextView) findViewById(R.id.Account_type);

        final TextView HHApp = (TextView) findViewById(R.id.HApp);




        //   mUpdateBtn = (Button) findViewById(R.id.SetupUpdateBtn);
        mSubmitBtn = (Button) findViewById(R.id.SetupSubmitBtn);
        // mBirthday =(TextView) findViewById(R.id.setup_Birthday);
        // mBirthdayBtn=(Button) findViewById(R.id.set_birthBtn);
        final Button gen = (Button) findViewById(R.id.Bgenerate);



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



        mIdNumber.setText(id_number);
        mNameField.setText(First_name+" "+Middle_nane+" "+Last_name);
        mcourse.setText(Course);
        msex.setText(Gender);
        maccountype.setText(Account_type);
        HHApp.setText(App);
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                getID();



            }
        });

        //Setup Profile Info Submit Button
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSetupAccount();
            }



            private void StartSetupAccount() {

                final String name = mNameField.getText().toString().trim();
                final String user_id = mAuth.getCurrentUser().getUid();
                //       final String course = mSpinnerSetupCourse.getSelectedItem().toString();
                //    final String year_level = mSpinnerSetup_YearLevel.getSelectedItem().toString();
                //  final String sex = mSex.getSelectedItem().toString();
                // final String address = mAddress.getText().toString();
                final String id_num = mIdNumber.getText().toString();
                final String course= mcourse.getText().toString();
                final String sex= msex.getText().toString();
                final String account= maccountype.getText().toString();

                // final String birthday = maccountype.getText().toString();



/*-----------------------------------------------------------------------------------------------------------------*/
                mProgress.setMessage("Finishing Setup!");
                mProgress.show();

                if(!TextUtils.isEmpty(name) && mImageUri != null){

                    StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());
                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            final EditText id_number23 = (EditText) findViewById(R.id.Setup_IdNumber);
                            final TextView HHapp3 = (TextView) findViewById(R.id.HApp);
                            String HHapp4 = HHapp3.getText().toString();

  /*----------------------------------------------------------------------------------------------------------------------*/
                            String downloadUri = taskSnapshot.getDownloadUrl().toString();
                            mDatabaseUsers.child(user_id).child("Name").setValue(name);
                            mDatabaseUsers.child(user_id).child("ProfImage").setValue(downloadUri);
                            mDatabaseUsers.child(user_id).child("IdNumber").setValue(id_num);
                            mDatabaseUsers.child(user_id).child("Gender").setValue(sex);
                            mDatabaseUsers.child(user_id).child("Course").setValue(course);

                            mDatabaseUsers.child(user_id).child("Account_type").setValue(account);

                            Intent intent = getIntent();
                            String HEmail = intent.getStringExtra("HEmail");
                            //    mDatabaseUsers.child(user_id).child("Course").setValue(course);
                            //   mDatabaseUsers.child(user_id).child("YearLevel").setValue(year_level);
                            // mDatabaseUsers.child(user_id).child("Sex").setValue(sex);
                            //    mDatabaseUsers.child(user_id).child("Address").setValue(address);
                            //   mDatabaseUsers.child(user_id).child("Birthday").setValue(birthday);

                            mProgress.dismiss();
                            Intent mainIntent = new Intent(SetupAccount.this, BulletinPostAcc.class);
                            mainIntent.putExtra("HEmail", HEmail);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();

  /*----------------------------------------------------------------------------------------------------------------------*/
                            String getHEmail1 = intent.getStringExtra("HEmail");

                            String id_number22 = id_number23.getText().toString();


                            if (HHapp4.equals("Not Register")){




 /*----------------------------------------------------------------------------------------------------------------------*/
                                Response.Listener<String> responseListener = new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String response) {



                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success){


                                                Toast.makeText(getApplicationContext(),
                                                        "Update success", Toast.LENGTH_SHORT).show();

                                                mProgress.dismiss();







                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),
                                                        "Update field in Hostinger", Toast.LENGTH_SHORT).show();

                                                mProgress.dismiss();
                                            }
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };


                                ConfirmRegister confirmRegister = new ConfirmRegister(id_number22, getHEmail1, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(SetupAccount.this);
                                queue.add(confirmRegister);



  /*----------------------------------------------------------------------------------------------------------------------*/



                            }

                            else {



                            }









                        }
                    });
                }






                if (mImageUri == null){
                    Toast.makeText(SetupAccount.this, "Error! Profile picture is needed. Choose a profile pic.", Toast.LENGTH_SHORT).show();
                }
            }






        });






        // setup profile image
        mSetupImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }





    private void getID() {



        final EditText Eid = (EditText) findViewById(R.id.Setup_IdNumber);
        final String id_number = Eid.getText().toString();
        final EditText mIdNumber = (EditText) findViewById(R.id.Setup_IdNumber);
        final TextView mname = (TextView) findViewById(R.id.SetupNameField);
        final TextView mcourse = (TextView) findViewById(R.id.setup_Course2);
        final TextView msex = (TextView) findViewById(R.id.setup_Sex2);
        final TextView maccountype = (TextView) findViewById(R.id.Account_type);
        final TextView HHApp2 = (TextView) findViewById(R.id.HApp);
        final ProgressDialog progressDialog = new ProgressDialog(SetupAccount.this);
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
                        String id_number = jsonResponse.getString("id_number");
                        String Student_status = jsonResponse.getString("Student_status");
                        String First_name = jsonResponse.getString("First_name");
                        String Middle_nane = jsonResponse.getString("Middle_nane");
                        String Last_name = jsonResponse.getString("Last_name");
                        String Gender = jsonResponse.getString("Gender");
                        String Course = jsonResponse.getString("Course");
                        String Account_type = jsonResponse.getString("Account_type");
                        String App = jsonResponse.getString("App");


                        if (App.equals("Installed")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(SetupAccount.this);
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

                            mIdNumber.setText(id_number);
                            mname.setText(First_name+" "+Middle_nane+" "+Last_name);

                            mcourse.setText(Course);
                            msex.setText(Gender);
                            maccountype.setText(Account_type);
                            HHApp2.setText(App);

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {

                                            progressDialog.dismiss();
                                        }
                                    }, 3000);

                        }

                    }
                    else {

                        Toast.makeText(SetupAccount.this, "ID not Exist .", Toast.LENGTH_SHORT).show();


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
        RequestQueue queue = Volley.newRequestQueue(SetupAccount.this);
        queue.add(loginRequest);

    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                mSetupImgBtn.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
