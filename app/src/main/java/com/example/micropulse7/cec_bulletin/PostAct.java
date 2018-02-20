package com.example.micropulse7.cec_bulletin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.util.Date;

public class PostAct extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private EditText mPostContent;
    private Button mSubmitBtn;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgessDialog;
    private static final int GALLERY_INTENT =2;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mProgessDialog = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mPostTitle = (EditText) findViewById(R.id.post_TitleField);
        mPostContent = (EditText) findViewById(R.id.post_ContentField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_INTENT);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
               startPostingImage();
            }
        });
    }

    private void startPostingImage() {

        mProgessDialog.setMessage("Posting...");

        final String title_val = mPostTitle.getText().toString().trim();
        final String content_val = mPostContent.getText().toString().trim();

            if (!TextUtils.isEmpty (title_val)&& !TextUtils.isEmpty (content_val)){
                if(mImageUri != null) {
                    mProgessDialog.show();
                    StorageReference filepath = mStorage.child("Post_Image").child(mImageUri.getLastPathSegment());

                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            final DatabaseReference newPost = mDatabase.push();
                            final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    newPost.child("Title").setValue(title_val);
                                    newPost.child("Content").setValue(content_val);
                                    newPost.child("Image").setValue(downloadUrl.toString());
                                    newPost.child("UserID").setValue(mCurrentUser.getUid());
                                    newPost.child("DateTime").setValue(currentDateTimeString);
                                    newPost.child("Username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mProgessDialog.dismiss();
                        }
                    });
                    Intent Post = new Intent(getApplicationContext(), BulletinPostAcc.class);
                    startActivity(Post);
                }
                else if(mImageUri == null) {
                    mProgessDialog.show();
                    final DatabaseReference newPost = mDatabase.push();
                    final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("Title").setValue(title_val);
                            newPost.child("Content").setValue(content_val);
                            newPost.child("UserID").setValue(mCurrentUser.getUid());
                            newPost.child("DateTime").setValue(currentDateTimeString);
                            newPost.child("Username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                    }
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mProgessDialog.dismiss();

                    Intent Post = new Intent(getApplicationContext(), BulletinPostAcc.class);
                    startActivity(Post);
                }
        }
        else if(TextUtils.isEmpty (title_val)&& TextUtils.isEmpty (content_val)){
                //for toast.
                Toast.makeText(PostAct.this, "Error fields are empty!",Toast.LENGTH_SHORT).show();
            }

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,  data);

     //   if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
     //       mImageUri = data.getData();
     //       mSelectImage.setImageURI(mImageUri);
     //   }
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mImageUri = data.getData();
            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(6, 4)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mSelectImage.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
