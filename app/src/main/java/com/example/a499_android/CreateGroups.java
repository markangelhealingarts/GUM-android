package com.example.a499_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateGroups extends AppCompatActivity {
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_create_groups);
        setContentView(R.layout.paynow);

        }
public void open(View view){
        Intent m = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/paypalme/getupandmove?country.x=US&locale.x=en_US"));
        m.setData(Uri.parse("https://www.paypal.com/paypalme/getupandmove?country.x=US&locale.x=en_US"));
        startActivity(m);
        }






  /*  private final String TAG = "CreateGroups";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference groups = db.collection("Groups");
    DocumentReference groupDocRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_groups);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Group");
        Button submitCreatedGroup = findViewById(R.id.createNewGroupBtn);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        submitCreatedGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView gN = findViewById(R.id.createGroupName);
                TextView gP = findViewById(R.id.createGroupPassword);
                String groupName = gN.getText().toString();
                String groupPassword = gP.getText().toString();
                boolean nameCheck = checkName(groupName);
                boolean passwordCheck = checkPassword(groupPassword);
                if(nameCheck){
                    if(passwordCheck){
                        String inviteCode = inviteCodeGenerate();
                        groupDocRef = groups.document(inviteCode);
                        //groupDocRef = groups.document("262288");
                        checkInviteCode(new FirestoreCallback() {
                            @Override
                            public void onSuccess(DocumentSnapshot document) {
                                String uName = SaveSharedPreference.getUserName(CreateGroups.this);
                                if(document.exists()){
                                    //TODO
                                    //Add something here in case this randomly generated code already exists
                                    Toast.makeText(CreateGroups.this, "An Error Occurred. Please Try Again.", Toast.LENGTH_SHORT).show();
                                } else {
                                    TextView gPts = findViewById(R.id.createGroupPoints);
                                    String points = gPts.getText().toString();
                                    Map<String, Object> newGroup = new HashMap<>();
                                    newGroup.put("Group Name", groupName);
                                    newGroup.put("Group", inviteCode);
                                    newGroup.put("Password", groupPassword);
                                    newGroup.put("Point Goal", 0);
                                    newGroup.put("Leader Name", uName);
                                    newGroup.put("Members", Arrays.asList());
                                    newGroup.put("Point Goal", points);
                                    //newGroup.put("Members", Arrays.asList());
                                    //Add new group to database
                                    groups.document(inviteCode).set(newGroup)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(CreateGroups.this,
                                                    "Group has been successfully created",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                    Toast.makeText(CreateGroups.this,
                                                            "Something went wrong with the creating account " +
                                                                    "process",
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            });
                                    DocumentReference userDocReference = db.collection("Users")
                                            .document(uName);
                                    userDocReference.update("Groups", FieldValue.arrayUnion(inviteCode))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(CreateGroups.this, CreateGroupSuccessPage.class);
                                            intent.putExtra("InviteCode", inviteCode);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CreateGroups.this,
                                                    "Error. Could not properly save Group", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                    } else {
                        Toast.makeText(CreateGroups.this,
                                "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateGroups.this,
                            "Group name must be at least 5 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Checks to see if the group name is smaller than 5 characters.
    private boolean checkName(String groupName){
        if(groupName.length() < 1){
            return false;
        }

        return true;
    }

    private boolean checkPassword(String groupPassword){
        if(groupPassword.length() < 5){
            return false;
        }
        return true;
    }

    private String inviteCodeGenerate(){
        String inviteCode = "";

        for(short i = 0; i < 6; i++){
            inviteCode += String.valueOf(new Random().nextInt(9));
        }

        return inviteCode;
    }

    private void checkInviteCode(FirestoreCallback firestoreCallback){
        groupDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //These if statements work in tandem to ensure that this page works.
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallback.onSuccess(document);
                    } else {
                        Log.d(TAG, "No such document, This group is available");
                        firestoreCallback.onSuccess(document);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

   */

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }
}
