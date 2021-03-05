package com.example.campnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class NoteFilesAdapter extends FirestoreRecyclerAdapter<NoteFiles,NoteFilesAdapter.NoteHolder> {



    public NoteFilesAdapter(@NonNull FirestoreRecyclerOptions<NoteFiles> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteFilesAdapter.NoteHolder noteFilesHolder, int i, @NonNull NoteFiles noteFiles) {

        noteFilesHolder.time.setText(noteFiles.getTime());
        Picasso.get().load(noteFiles.getFileurl()).into(noteFilesHolder.imageNotes);
    }

    @NonNull
    @Override
    public NoteFilesAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_feed,parent,false);
        return new NoteHolder(v);
    }

     class NoteHolder extends RecyclerView.ViewHolder {

        TextView time;
        ImageView imageNotes;

         public NoteHolder(@NonNull View itemView) {
             super(itemView);

             time=itemView.findViewById(R.id.imageTime);
             imageNotes=itemView.findViewById(R.id.imageFile);

         }
     }
}
