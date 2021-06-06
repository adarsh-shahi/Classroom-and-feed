package com.example.campnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DetailedSubjects extends AppCompatActivity {



    private FloatingActionButton uploadFiles,upLoadImage,uploadPdf;
    private boolean isOpen;
    private Animation rotateOpen,rotateClose;
    private TextView notice,subjectName,teachersName;
    private ImageView editNotice;
    private NoteFilesAdapter noteFilesAdapter;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference nameRef = db.collection("Notes");
    private long postsCount;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserId;
    Date currentTime;
    String pin;




    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_subjects);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        currentTime= Calendar.getInstance().getTime();

        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        Intent intent = getIntent();
        String sub = intent.getStringExtra("sub");
        String teacher = intent.getStringExtra("tea");

        Query query = nameRef.orderBy("count",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteFiles> options = new FirestoreRecyclerOptions.Builder<NoteFiles>()
                .setQuery(query,NoteFiles.class)
                .build();

        noteFilesAdapter = new NoteFilesAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_files);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedSubjects.this));
        recyclerView.setAdapter(noteFilesAdapter);

        subjectName=findViewById(R.id.subjectName);
        teachersName=findViewById(R.id.teachersName);

        uploadFiles=findViewById(R.id.uploadFiles);
        upLoadImage=findViewById(R.id.uploadImage);
        uploadPdf=findViewById(R.id.uploadPdf);
        rotateOpen=AnimationUtils.loadAnimation(DetailedSubjects.this,R.anim.rotate_open_anim);
        rotateClose=AnimationUtils.loadAnimation(DetailedSubjects.this,R.anim.rotate_anim_close);
        notice=findViewById(R.id.noticeTv);
        editNotice=findViewById(R.id.noticeEdit);


        subjectName.setText(sub);
        teachersName.setText(teacher);




      isOpen=false;

      db.collection("Notes").document("notice").addSnapshotListener(new EventListener<DocumentSnapshot>() {
          @Override
          public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               pin = value.getString("notice");
               notice.setText(pin);
          }
      });


      editNotice.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(DetailedSubjects.this,SetNotice.class));
          }
      });

        if(currentUserId.equals("xpeday1KUegBCyxthsznL5NSobl2")){
            uploadFiles.setVisibility(View.VISIBLE);
        }

        uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePost();
            }
        });

    }

    private void choosePost() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadPic();
        }
    }


    private void uploadPic() {
        String time = currentTime.toString();
        String cutTime = time.substring(0,20);

        db.collection("Notes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    postsCount=value.getDocumentChanges().size();
                }
            }
        });



        ProgressDialog pd = new ProgressDialog(DetailedSubjects.this);
        pd.setTitle("Uploading Image....");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();

        StorageReference riversRef = storageReference.child("notes/"+randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                Map<String,Object> note = new HashMap<>();
                                note.put("fileurl",imageUrl);
                                note.put("name","parth darekar"+postsCount);
                                note.put("count",postsCount);
                                note.put("time",cutTime);
                                db.collection("Notes").document("parth darekar"+postsCount).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(DetailedSubjects.this, "Image Uploaded", Toast.LENGTH_LONG).show();

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

    @Override
    public void onStop() {
        super.onStop();
        noteFilesAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        noteFilesAdapter.startListening();

    }


}