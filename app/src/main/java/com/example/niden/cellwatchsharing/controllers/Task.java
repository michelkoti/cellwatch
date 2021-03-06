package com.example.niden.cellwatchsharing.controllers;



import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.niden.cellwatchsharing.database.FirebaseUserEntity;
import com.example.niden.cellwatchsharing.database.TaskEntityDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static com.example.niden.cellwatchsharing.activities.TechnicianActivity.mUserKey;


/**
 * Created by niden on 16-Nov-17.
 */

public class Task  {
    private final String DIR_USER="users";
    private final String DIR_TASK="tasks";
    private String currentDateTimeString = String.valueOf(System.currentTimeMillis());
    public static String eachUserID;


    //Insert new task

    public void insertTask(EditText txTaskName, EditText txClass, EditText txAddress, EditText txDescription, EditText txSuburb, Spinner spinner, Spinner spinnerTech,Button btnStartDate,Button btnEndDate) {
        String strTaskName=txTaskName.getText().toString();
        String strClass =txClass.getText().toString();
        String strAdress = txAddress.getText().toString();
        String strDesc = txAddress.getText().toString();
        String strSuburb=txSuburb.getText().toString();
        String strStartDate = btnStartDate.getText().toString();
        String strEndDate = btnEndDate.getText().toString();
        String strSpinnerType = spinner.getSelectedItem().toString();
        String strSpinnerTech = spinnerTech.getSelectedItem().toString();
        FirebaseUserEntity  data = (FirebaseUserEntity)spinnerTech.getSelectedItem();
        eachUserID= data.getId();
        StringTokenizer tokens = new StringTokenizer(strSpinnerTech,"|");
        String name = tokens.nextToken();
        String uid = tokens.nextToken();

        FirebaseDatabase.getInstance()
                .getReference()
                .child(DIR_TASK)
                .push()
                .setValue(new TaskEntityDatabase(eachUserID,strTaskName,strClass, strAdress, strDesc,strSuburb
                        ,currentDateTimeString, strSpinnerType, name,"",strStartDate,strEndDate,"Pending"));
        txTaskName.setText("");
        txAddress.setText("");
        txDescription.setText("");
        txSuburb.setText("");
        txClass.setText("");
    }

    public void insertTaskForTheirOwn(EditText txTaskName, EditText txClass, EditText txAddress, EditText txDescription, EditText txSuburb, Spinner spinner, Spinner spinnerTech,Button btnStartDate,Button btnEndDate) {
        String strTaskName=txTaskName.getText().toString();
        String strClass =txClass.getText().toString();
        String strAdress = txAddress.getText().toString();
        String strDesc = txAddress.getText().toString();
        String strSuburb=txSuburb.getText().toString();
        String strStartDate = btnStartDate.getText().toString();
        String strEndDate = btnEndDate.getText().toString();
        String strSpinnerType = spinner.getSelectedItem().toString();
        String strEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        StringTokenizer tokens = new StringTokenizer(strEmail,"@");
        String name = tokens.nextToken();
        FirebaseDatabase.getInstance()
                .getReference()
                .child(DIR_TASK)
                .push()
                .setValue(new TaskEntityDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid(),strTaskName,strClass, strAdress, strDesc,strSuburb
                        ,currentDateTimeString, strSpinnerType, name,"",strStartDate,strEndDate,"Uncomplete"));
        txTaskName.setText("");
        txAddress.setText("");
        txDescription.setText("");
        txSuburb.setText("");
        txClass.setText("");
    }

    public void updateTask(String taskKey,EditText txComment,Spinner spinnerTaskStatus) {
        String strComment = txComment.getText().toString();
        String strTaskStatus = spinnerTaskStatus.getSelectedItem().toString();
        Map<String, Object> result = new HashMap<>();
        result.put("taskComment",strComment);
        result.put("taskStatus",strTaskStatus);
        FirebaseDatabase.getInstance()
                .getReference()
                .child(DIR_TASK)
                .child(taskKey)
                .updateChildren(result);
    }

//SHOW CONTENTS ON TASK DETAIL ACTIVITY
    public void displayTaskDetailForTechnician(final String taskKey, final EditText etTaskName, final EditText etClass, final EditText etDescription,
                                               final EditText etAddress, final EditText etSuburb, final EditText etComment ){
       DatabaseReference mDataReference = FirebaseDatabase.getInstance().getReference().child(DIR_TASK);
        mDataReference.child(taskKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //getting from firebase
                    TaskEntityDatabase taskEntityDatabase = dataSnapshot.getValue(TaskEntityDatabase.class);
                    String strTaskName = taskEntityDatabase.getTaskName();
                    String strDescription = taskEntityDatabase.getTaskDescription();
                    String strAddress = taskEntityDatabase.getTaskAddress();
                    String strClass = taskEntityDatabase.getTaskClass();
                    String strSuburb = taskEntityDatabase.getTaskSuburb();
                    String strComment = taskEntityDatabase.getTaskComment();
                    //set into edit text
                    etComment.setText(strComment);
                    etTaskName.setText(strTaskName);
                    etClass.setText(strClass);
                    etDescription.setText(strDescription);
                    etAddress.setText(strAddress);
                    etSuburb.setText(strSuburb);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    //SHOW CONTENTS ON TASK DETAIL ACTIVITY
    public void displayTaskDetailForAdmin(String taskKey, final EditText etTaskName, final EditText etClass, final EditText etDescription, final EditText etAddress, final EditText etSuburb, final EditText etTaskComment ){
        DatabaseReference mDataReference = FirebaseDatabase.getInstance().getReference(DIR_TASK);
        mDataReference.child(taskKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TaskEntityDatabase taskEntityDatabase = dataSnapshot.getValue(TaskEntityDatabase.class);
                    String strTaskName = taskEntityDatabase.getTaskName();
                    String strDescription = taskEntityDatabase.getTaskDescription();
                    String strAddress = taskEntityDatabase.getTaskAddress();
                    String strClass = taskEntityDatabase.getTaskClass();
                    String strSuburb = taskEntityDatabase.getTaskSuburb();
                    String strComment = taskEntityDatabase.getTaskComment();

                    etTaskComment.setText(strComment);
                    etTaskName.setText(strTaskName);
                    etClass.setText(strClass);
                    etDescription.setText(strDescription);
                    etAddress.setText(strAddress);
                    etSuburb.setText(strSuburb);
                    etTaskComment.setText(strComment);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }



}
