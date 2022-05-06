package com.example.a499_android;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;

public class JoinGroup2 extends AppCompatActivity {
    private final String TAG = "JoinGroup2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference groups = db.collection("Groups");
    DocumentReference groupDocRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_groups_page_2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Join Group");
        Button finishJoinBtn = findViewById(R.id.finishJoinGroups);
        TextView info = findViewById(R.id.groupInfo);
        String groupName = "Group: " + getIntent().getExtras().getString("Group Name");
        String leaderName = "Leader: " + getIntent().getExtras().getString("Leader Name");
        String inviteCode = getIntent().getExtras().getString("Invite Code");
        String groupPassword = getIntent().getExtras().getString("Group Password");
        info.setText("Enter password to join the following group:\n" + groupName + "\n" + leaderName + " ");

        finishJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pWord = findViewById(R.id.joinGroupPassword);
                String password = pWord.getText().toString();
                groupDocRef = groups.document(inviteCode);

                checkPassword(new FirestoreCallback() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if(password.equals(groupPassword)){
                            groupDocRef.collection(password).document("Info")
                                    .update("members",
                                            FieldValue.arrayUnion(SaveSharedPreference.getUserName(JoinGroup2.this)))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(JoinGroup2.this, GroupsLandingPage.class);
                                            startActivity(intent);
                                        }
                                    });
                        } else {
                            Toast.makeText(JoinGroup2.this, "Incorrect Password!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //Intent intent = new Intent(JoinGroup2.this, GroupsLandingPage.class);
                //startActivity(intent);
            }
        });
    }

    private void checkPassword(FirestoreCallback firestoreCallback){
        groupDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallback.onSuccess(document);
                    } else {
                        Log.d(TAG, "No such document exists.");
                        firestoreCallback.onSuccess(document);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }
}
