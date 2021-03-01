package com.example.campnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailedSubjects extends AppCompatActivity {

    private FloatingActionButton uploadFiles;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_subjects);

        uploadFiles=findViewById(R.id.uploadFiles);



        uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailedSubjects.this,SelectFile.class));
            }
        });


    }
}