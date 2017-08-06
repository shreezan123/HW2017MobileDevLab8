package com.saryal.hw2017mobiledev.lab7;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // Get references to text views to display database data.
        final TextView rootTextView = v.findViewById(R.id.root_text_view);
        final TextView subTreeTextView = v.findViewById(R.id.subtree_text_view);

        // Add database code here.
        // set up reference to database root
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // add a ValueEventListener on the ROOT of the database, to update the rootTextView.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rootTextView.setText(dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                rootTextView.setText(databaseError.toString());
            }
        });

        // Get a reference to "fun_path" element underneath the root.
        DatabaseReference funPathReference = databaseReference.child("fun_path");
        // add a ValueEventListener on that path of the database, to update subTreeTextView.
        funPathReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subTreeTextView.setText(dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                subTreeTextView.setText(databaseError.toString());
            }
        });
        return v;

    }

}
