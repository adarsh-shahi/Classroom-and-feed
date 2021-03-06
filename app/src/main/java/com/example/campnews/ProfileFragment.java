package com.example.campnews;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private CircleImageView profile;
    private TextView fullname,dob,mail,id,enroll,roll;
    private FirebaseAuth mAuth;
    String Uid;
    String name;
    private FirebaseFirestore db ;
    private FloatingActionButton fab;
    private ProgressDialog loading;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private int STORAGE_PERMISSION_CODE = 1;
    private TextView personalEt;
    private CardView CollegeCardView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Uid=mAuth.getCurrentUser().getUid();
        profile=view.findViewById(R.id.profile_image);
        fullname=view.findViewById(R.id.fullName);
        dob=view.findViewById(R.id.dob);
        personalEt = view.findViewById(R.id.personalDetailsEt);
        mail=view.findViewById(R.id.mail);
        id=view.findViewById(R.id.collegeId);
        enroll=view.findViewById(R.id.enroll);
        roll=view.findViewById(R.id.rollNo);
        CollegeCardView = view.findViewById(R.id.collegeDetailsCard);
        fab=view.findViewById(R.id.fab);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        loading=new ProgressDialog(getActivity());




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
                showEditProfileDialog();

            }
        });
        return view;
    }

    private void showEditProfileDialog() {
        String options[]={"Edit Profile Photo","Log Out"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                   loading.setMessage("Updating Profile Photo");
                        chooseProfilePic();
                }
                else if(which==1){
                        LogOut();
                }
            }
        });
        builder.create().show();
    }

    private void LogOut() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(getActivity());

        // Set the message show for the Alert time
        builder.setMessage("Do you want to log out ?");

        // Set Alert Title
        builder.setTitle("Log Out");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                mAuth.signOut();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            chooseProfilePic();
        }
        else {
            Toast.makeText(getActivity(), "Please provide permission", Toast.LENGTH_LONG).show();
        }
    }

//    private void requestStoragePersmission() {
//        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Permission needed")
//                    .setMessage("This permission is required to access file")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).create().show();
//        }
//        else {
//            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
//        }
//
//
//    }

    private void chooseProfilePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            profile.setImageURI(imageUri);
            uploadPic();
        }


    }

    private void uploadPic() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading Image....");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();

        StorageReference riversRef = storageReference.child("images/"+name+"_"+randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                       riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               String imageUrl = uri.toString();
                               Map<String,Object> note = new HashMap<>();
                               note.put("profilepic",imageUrl);
                               db.collection("Users").document(mail.getText().toString()).update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       pd.dismiss();
                                       Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
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
                pd.setMessage(+(int) progressPercentage+"%");
            }
        });




    }

    @Override
            public void onStart() {
        super.onStart();
        String getMail = mAuth.getCurrentUser().getEmail();

        if (Uid.equals("xpeday1KUegBCyxthsznL5NSobl2")) {
            CollegeCardView.setVisibility(View.GONE);
            personalEt.setText("Details");
            fullname.setText("Name: Santoshi Shete");
            mail.setText("Mail: ajpteacher.campnews@gmail.com");
            dob.setText("Subject: Advanced Java Programming");

        } else {
            DocumentReference documentReference = db.collection("Users").document(getMail);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    dob.setText(value.getString("dob"));
                    mail.setText(value.getString("mail"));
                    id.setText(value.getString("collegecode"));
                    enroll.setText(value.getString("enroll"));
                    roll.setText(value.getString("roll"));
                    fullname.setText(value.getString("name"));
                    try {
                        Picasso.get().load(value.getString("profilepic")).placeholder(R.drawable.ic_default_profile1_blue).into(profile);
                    } catch (Exception e) {

                    }

                }
            });
        }
    }
}

