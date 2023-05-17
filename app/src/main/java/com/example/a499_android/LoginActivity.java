package com.example.a499_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    TextView username, password;
    Button loginButton;
    final String TAG = "";

    public static String checkAdmin = "";

    public static String loggedUserName = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = db.collection("Users");
    DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameInput = username.getText().toString().toLowerCase();
                String passwordInput = password.getText().toString();

                //Checking to see if it's Admin Mark.
                if(usernameInput.equals("markangelhealingarts@gmail.com") && (passwordInput.equals("4MAngelGum$"))){
                    userDocRef = users.document(usernameInput);
                    findUser(new FirestoreCallback() {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            Intent toAdminLanding = new Intent(LoginActivity.this, AdminLanding.class);
                            startActivity(toAdminLanding);
                        }
                    });
                }

               else if(usernameInput.length() != 0) {
                    userDocRef = users.document(usernameInput);
                    findUser(new FirestoreCallback() {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            if (document.exists()) {
                                String passwordValue = document.getString("Password").toString();
                                Log.d(TAG, String.valueOf(document));
                                if (passwordInput.equals(passwordValue)) {
                                    //sp.edit().putString("username", usernameInput).apply();
                                    //sp.edit().putInt("Points", Integer.parseInt(document.getString("Points"))).apply();
                                    Toast.makeText(LoginActivity.this, "Login Successful! Welcome " + usernameInput, Toast.LENGTH_SHORT).show();
                                    loggedUserName = usernameInput;
                                    SaveSharedPreference.setUserName(LoginActivity.this, usernameInput);
                                    Intent toLandingPage = new Intent(LoginActivity.this, LandingPage.class);
                                    startActivity(toLandingPage);
                                } else {
                                    Log.d(TAG, "Password did not match");
                                    Log.d(TAG, passwordInput);
                                    Toast.makeText(LoginActivity.this,
                                            "Incorrect Password",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Username not found",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Username cannot be blank", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }

    private void findUser(FirestoreCallback firestoreCallback) {
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallback.onSuccess(document);
                    } else {
                        Log.d(TAG, "No such document, This Username is available");
                        firestoreCallback.onSuccess(document);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void toSignUp(View view) {
        finish();
    }
}