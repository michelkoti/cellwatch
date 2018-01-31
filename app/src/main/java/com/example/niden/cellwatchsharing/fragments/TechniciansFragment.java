package com.example.niden.cellwatchsharing.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.niden.cellwatchsharing.R;
import com.example.niden.cellwatchsharing.activities.TechnicianActivity;

import com.example.niden.cellwatchsharing.database.FirebaseUserEntity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by niden on 16-Nov-17.
 */

public class TechniciansFragment extends Fragment {
    private DatabaseReference mRef;
    View myView;
    private Activity activity = getActivity();
    private FirebaseListAdapter<FirebaseUserEntity> mTechAdapter;
    private FirebaseUserEntity firebaseUserEntity = new FirebaseUserEntity();
    private ListView technicianList;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_technicians_layout, container, false);
        activity = getActivity();
        activity.setTitle(getString(R.string.toobar_technicians));
        setHasOptionsMenu(true);
        technicianList = (ListView) myView.findViewById(R.id.list_technician);
        displayFriendsList();


        return myView;

    }


    public void displayFriendsList() {
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        mTechAdapter = new FirebaseListAdapter<FirebaseUserEntity>(activity, FirebaseUserEntity.class,
                R.layout.item_technician, mRef) {

            @Override
            protected void populateView(View v, FirebaseUserEntity model, final int position) {
                final TextView name_user = (TextView) v.findViewById(R.id.txt_name);
                final ImageView profile_user = (ImageView) v.findViewById(R.id.technician_profile);
                mRef.child(mTechAdapter.getRef(position).getKey()).child("info").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        firebaseUserEntity = dataSnapshot.getValue(FirebaseUserEntity.class);
                        name_user.setText(firebaseUserEntity.getName());
                        String url = firebaseUserEntity.getProfile_url();
                        Picasso.with(activity).load(url)
                                .resize(110, 110).centerCrop()
                                .into(profile_user);
                        profile_user.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent(activity, TechnicianActivity.class);
                                myIntent.putExtra("key", getRef(position).getKey());
                                activity.startActivity(myIntent);
                            }
                        });

                    }                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        technicianList.setAdapter(mTechAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}