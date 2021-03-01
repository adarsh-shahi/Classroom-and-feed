package com.example.campnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.HashMap;
import java.util.Map;

public class SelectFile extends AppCompatActivity {

    private Button uploadFile, selectFile;
    private TextView fileName;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    Uri pdfUri;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);

        uploadFile=findViewById(R.id.uploadFile);
        selectFile=findViewById(R.id.selectFile);
        fileName=findViewById(R.id.fileName);

        storage=FirebaseStorage.getInstance();
        db=FirebaseFirestore.getInstance();

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    selectPDF();
            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri!=null) {
                    uploadFileMethod(pdfUri);
                }
                else {
                    Toast.makeText(SelectFile.this, "Select a file", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

//    private void requestStoragePersmission() {
//        if(ActivityCompat.shouldShowRequestPermissionRationale(SelectFile.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
//            new AlertDialog.Builder(SelectFile.this)
//                    .setTitle("Permission needed")
//                    .setMessage("This permission is required to access file")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(SelectFile.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//dialog.dismiss();
//                        }
//                    }).create().show();
//
//        }
//        else {
//            ActivityCompat.requestPermissions(SelectFile.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
//        }
//
//
//    }

    private void uploadFileMethod(Uri pdfUri) {

        String fileName = System.currentTimeMillis() + "";

        StorageReference storageReference = storage.getReference();
        storageReference.child("uploadsfile").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String fileUrl = uri.toString();
                                Map<String ,Object> note = new HashMap<>();
                                note.put("name","parth darekar");
                                note.put("file",fileUrl);
                                db.collection("Files").document("parth darekar").set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(SelectFile.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SelectFile.this,ChatFragment.class));
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

                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectPDF();
        }
        else {
            Toast.makeText(SelectFile.this, "Please provide permission", Toast.LENGTH_LONG).show();
        }
    }

    private void selectPDF() {

        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();

        } else {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }
}