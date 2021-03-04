package com.example.campnews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.spark.submitbutton.SubmitButton;

public class SetNotice extends AppCompatActivity {

    private EditText notice;
    private SubmitButton submitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notice);
        notice=findViewById(R.id.editNotice);
        submitButton=findViewById(R.id.update_notice_Btn);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(notice.getText().toString())){



                }
                else {

                }
            }
        });







    }
}