package com.example.campnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spark.submitbutton.SubmitButton;

import java.util.HashMap;
import java.util.Map;

public class SetNotice extends AppCompatActivity {

    private EditText notice;
    private SubmitButton submitButton;
    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notice);
        notice=findViewById(R.id.editNotice);
        submitButton=findViewById(R.id.update_notice_Btn);
        db=FirebaseFirestore.getInstance();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noticeEt = notice.getText().toString();
                if(!TextUtils.isEmpty(noticeEt)){
                    Toast.makeText(SetNotice.this, noticeEt, Toast.LENGTH_SHORT).show();


                    Map<String,Object> note = new HashMap<>();
                    note.put("notice",noticeEt);
                    db.collection("Notes").document("notice").set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(SetNotice.this, "Notice Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetNotice.this,DetailedSubjects.class));



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



                }
                else {
                    Toast.makeText(SetNotice.this, "Nothing updated", Toast.LENGTH_SHORT).show();


                }
            }
        });







    }
}