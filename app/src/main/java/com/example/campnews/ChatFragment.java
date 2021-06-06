package com.example.campnews;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChatFragment extends Fragment {

    private ImageView imageView;
    private CardView subject1, subject2, subject3, subject4, subject5;
    private TextView subject,teacherName;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String uid ;

    




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        subject1=view.findViewById(R.id.subject1);
        subject2=view.findViewById(R.id.subject2);
        subject3=view.findViewById(R.id.subject3);
        subject4=view.findViewById(R.id.subject4);
        subject5=view.findViewById(R.id.subject5);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        subject=view.findViewById(R.id.subjectName);
        teacherName=view.findViewById(R.id.teachersName);

        if(uid.equals("xpeday1KUegBCyxthsznL5NSobl2"))
            startActivity(new Intent(getActivity(), DetailedSubjects.class));

        subject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = subject.getText().toString();
                String teacher = teacherName.getText().toString();

                Intent intent = new Intent(getActivity(),DetailedSubjects.class);
                intent.putExtra("sub",subjectName);
                intent.putExtra("tea",teacher);
                startActivity(intent);
            }
        });

        subject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = "Client Side Scripting Language";
                String teacher = "Varsha Upre";

                Intent intent = new Intent(getActivity(),DetailedSubjects.class);
                intent.putExtra("sub",subjectName);
                intent.putExtra("tea",teacher);
                startActivity(intent);
            }
        });

        subject3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = "Operating System";
                String teacher = "Anjali Khandagale";

                Intent intent = new Intent(getActivity(),DetailedSubjects.class);
                intent.putExtra("sub",subjectName);
                intent.putExtra("tea",teacher);
                startActivity(intent);
            }
        });

        subject4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName ="Entrepreneurship Development";
                String teacher = "Vishal Shetkar";

                Intent intent = new Intent(getActivity(),DetailedSubjects.class);
                intent.putExtra("sub",subjectName);
                intent.putExtra("tea",teacher);
                startActivity(intent);
            }
        });



        subject5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = "Environmental Studies";
                String teacher = "Deepa Yerolkar";

                Intent intent = new Intent(getActivity(),DetailedSubjects.class);
                intent.putExtra("sub",subjectName);
                intent.putExtra("tea",teacher);
                startActivity(intent);
            }
        });


        return view;
    }
}