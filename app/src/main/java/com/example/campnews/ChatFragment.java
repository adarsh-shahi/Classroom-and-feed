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


public class ChatFragment extends Fragment {

    private ImageView imageView;
    private CardView subject1;
    private TextView subject,teacherName;

    




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        subject1=view.findViewById(R.id.subject1);
        subject=view.findViewById(R.id.subjectName);
        teacherName=view.findViewById(R.id.teachersName);

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


        return view;
    }
}