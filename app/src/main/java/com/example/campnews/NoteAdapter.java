package com.example.campnews;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteHolder> {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();




    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder noteHolder, int i, @NonNull final Note note) {

        noteHolder.description.setText(note.getDesc());
        noteHolder.adminName.setText(note.getName());

        if(TextUtils.isEmpty(note.getImageUrl())){
            noteHolder.adminView.setVisibility(View.VISIBLE);
            noteHolder.postView.setVisibility(View.GONE);
            noteHolder.post.setVisibility(View.GONE);
            noteHolder.description.setTextSize(20);
        }
        else {
            Picasso.get().load(note.getImageUrl()).into(noteHolder.post);
        }
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_feed,parent,false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView description;
        ImageView post;
        TextView adminName;
        View adminView,postView;



        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            description=itemView.findViewById(R.id.desc);
            post=itemView.findViewById(R.id.post);
            adminName=itemView.findViewById(R.id.adminName);
            adminView=itemView.findViewById(R.id.adminDivider);
            postView=itemView.findViewById(R.id.postDivider);

        }
    }
}
