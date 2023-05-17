package com.example.a499_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminLanding extends AppCompatActivity {
    // these lists are created incase the the user selects add a survey or wants to load previous survey data
    // this is also the landing page for the admin, this page can only be accessed by mark angel himself or you the developer
    CardView addEditTidbits, viewResponsesBtn, addSurveyBtn,addVideosBtn, resetSurvey, addGroupsBtn;
    ActionBar actionBar;
    public static ArrayList<String> w_survey_count_list = new ArrayList<>();
    public static ArrayList<String> w_survey_questions_list = new ArrayList<>();
    public static ArrayList<String> w_survey_responses_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);
        clearLists();
        actionBar = getSupportActionBar();
        actionBar.setTitle("Admin Landing Page");
        actionBar.setDisplayHomeAsUpEnabled(true);
        wiredUp();

        addEditTidbits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLanding.this, Tidbits.class));
            }
        });

        viewResponsesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLanding.this, LoadPreviousSurvey.class));
            }
        });

        addSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLanding.this, AddSurveyQuestionsCount.class ));
            }
        });

        //Groups addded button in the Admin Landing

        addGroupsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View group){
                startActivity(new Intent(AdminLanding.this, CreateGroupsAdmin.class));
            }
        });

        addVideosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLanding.this, AddVideos.class ));

            }
        });
        resetSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference usersList =  db.collection("Surveys").document("WSurveyUsers");
                ArrayList<String> restList = new ArrayList<>();
                Object list = restList;
                usersList.update("users_finished", list).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AdminLanding.this, "Users have been reset! users may retake survey again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void wiredUp() {
        addEditTidbits = findViewById(R.id.addEditTidbits);
        viewResponsesBtn = findViewById(R.id.viewResponsesBtn);
       // addSurveyBtn = findViewById(R.id.addSurveyBtn);
        addVideosBtn = findViewById(R.id.addVideosBtn);
        resetSurvey = findViewById(R.id.resetUsers);
    }
    private void clearLists(){
        // since all of these variables are static, we must clear them or set them to false ensuring to avoid any out of bounds errors or intenting to activities we shouldn't
        LoadPreviousSurvey.w_survey_list_names.clear();
        LoadPreviousSurvey.previous_times_list.clear();
        GetResponseListQuery.listSelected_questions.clear();
        GetResponseListQuery.listSelected_questions_c.clear();
        GetResponseListQuery.listSelected_responses.clear();
        SelectASurvey.survey_list_names.clear();
        SelectASurvey.past_survey= false;
        ViewResponseR.select_question= false;
        GetResponseListQuery.index_charts = 0;
        AddSurveyQuestionsCount.count =0;
        w_survey_questions_list.clear();
        w_survey_responses_list.clear();
        w_survey_count_list.clear();
    }
}