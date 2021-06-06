package com.example.campnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spark.submitbutton.SubmitButton;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class AddPostDetails extends AppCompatActivity {
    private ImageView imageLoad;
    private EditText desc;
    private SubmitButton post;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db ;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private String text,descText;
    private boolean flag;
    private int STORAGE_PERMISSION_CODE = 1;
    private long postsCount;
    private long postsCount1;
    Date currentTime;
    private Button select;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_details);

        imageLoad=findViewById(R.id.postHold);
        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        desc=findViewById(R.id.postDesc);
        post=findViewById(R.id.post);
        select = findViewById(R.id.selectImage);
        currentTime = Calendar.getInstance().getTime();

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLoad.setVisibility(View.VISIBLE);
                    choosePost();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadPic();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            choosePost();
        }
        else {
            Toast.makeText(AddPostDetails.this, "Please provide permission", Toast.LENGTH_LONG).show();
        }
    }


        private void choosePost() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private void requestStoragePersmission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(AddPostDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(AddPostDetails.this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is required to access file")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddPostDetails.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }
        else {
            ActivityCompat.requestPermissions(AddPostDetails.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }


    }


        @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
          imageUri = data.getData();
            imageLoad.setImageURI(imageUri);
        }


    }
    private void uploadPic() {

        String time = currentTime.toString();
        String cutTime = time.substring(0,20);
        descText = desc.getText().toString().trim();

        db.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                           if(!value.isEmpty()){
                                                               postsCount=value.getDocumentChanges().size();
                                                           }
                                                       }
                                                   });

         ProgressDialog pd = new ProgressDialog(AddPostDetails.this);
        pd.setTitle("Uploading Image....");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();

        StorageReference riversRef = storageReference.child("posts/"+randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                Map<String,Object> note = new HashMap<>();
                                note.put("imageUrl",imageUrl);
                                note.put("name","Parth Darekar");
                                note.put("desc",descText );
                                note.put("count",postsCount);
                                note.put("time",cutTime);
                                db.collection("Posts").document("parth darekar"+postsCount).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(AddPostDetails.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(AddPostDetails.this,MainActivity.class));

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progressPercentage = (100.00* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("Percentage: "+(int) progressPercentage+"%");
            }
        });
    }

}