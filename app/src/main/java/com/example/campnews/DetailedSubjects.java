package com.example.campnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailedSubjects extends AppCompatActivity {



    private FloatingActionButton uploadFiles,upLoadImage,uploadPdf;
    private boolean isOpen;
    private Animation rotateOpen,rotateClose;
    private TextView notice;
    private ImageView editNotice;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_subjects);

        uploadFiles=findViewById(R.id.uploadFiles);
        upLoadImage=findViewById(R.id.uploadImage);
        uploadPdf=findViewById(R.id.uploadPdf);
        rotateOpen=AnimationUtils.loadAnimation(DetailedSubjects.this,R.anim.rotate_open_anim);
        rotateClose=AnimationUtils.loadAnimation(DetailedSubjects.this,R.anim.rotate_anim_close);
        notice=findViewById(R.id.noticeTv);
        editNotice=findViewById(R.id.noticeEdit);




      isOpen=false;

      editNotice.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(DetailedSubjects.this,SetNotice.class));
          }
      });


      uploadFiles.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(isOpen){
                  upLoadImage.startAnimation(rotateClose);
                  uploadPdf.startAnimation(rotateClose);
                  isOpen=false;
              }
              else {
                  upLoadImage.startAnimation(rotateOpen);
                  uploadPdf.startAnimation(rotateOpen);

                  isOpen=true;

              }
          }
      });

      upLoadImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(DetailedSubjects.this,SelectFile.class));
          }
      });

      uploadPdf.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });






    }
}