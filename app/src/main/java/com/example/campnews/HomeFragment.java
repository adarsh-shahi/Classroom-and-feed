package com.example.campnews;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.annotation.Nullable;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference nameRef = db.collection("Posts");
    private NoteAdapter adapter;
    private FloatingActionButton addPost;
    private int STORAGE_PERMISSION_CODE = 1;


   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_home, container, false);
       Query query = nameRef.orderBy("count",Query.Direction.DESCENDING);
       FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
               .setQuery(query,Note.class)
               .build();

       adapter = new NoteAdapter(options);
       RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       recyclerView.setAdapter(adapter);

       addPost=view.findViewById(R.id.addPost);

       addPost.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showAddPOstType();
           }
       });




       return view;
    }

    private void requestStoragePersmission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("This permission is required to access file")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }


    }


    private void showAddPOstType() {
        String options[]={"Image Post","Text Post"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Post Type");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    String value = String.valueOf((which));
                    Intent intent = new Intent(getActivity(),AddPostDetails.class);
                    intent.putExtra("type",value);
                    startActivity(intent);

                }
                else if(which==1){
                    String value = String.valueOf((which));
                    Intent intent = new Intent(getActivity(),AddPostDetails.class);
                    intent.putExtra("type",value);
                    startActivity(intent);
                }
            }
        });
        builder.create().show();
    }









    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }
}