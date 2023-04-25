package com.gum.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PreviewNewSurvey extends AppCompatActivity {
    TextView previewSurveyTxt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference wSurveyQDoc;
    DocumentReference wSurveyRDoc;
    DocumentReference pastWSurveyQDoc;
    DocumentReference pastWSurveyRDoc;
    DocumentReference pastWSurveyQLDoc;
    DocumentReference times;
    Button confirmSurvey;
    String TAG = "Preview New Survey";
    private ArrayList<String> w_count = AdminLanding.w_survey_count_list;
    private ArrayList<String> w_questions = AdminLanding.w_survey_questions_list;
    private ArrayList<String> w_questions_choice = AdminLanding.w_survey_responses_list;
    private ArrayList<String> past_w_survey_count = new ArrayList<>();
    private ArrayList<String> past_w_survey_q = new ArrayList<>();
    private ArrayList<String> past_w_survey_qc = new ArrayList<>();
    private ArrayList<String> times_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_new_survey);

        previewSurveyTxt = findViewById(R.id.previewSurvey);
        confirmSurvey = findViewById(R.id.submitBtnAS);
        setTextView();

        // a list of queries must happen
        confirmSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewNewSurvey.this, "Survey is uploading...", Toast.LENGTH_SHORT).show();
                wSurveyQDoc = db.collection("Surveys").document("WSurveyQ");
                 wSurveyRDoc = db.collection("Surveys").document("WSurveyR");
                 pastWSurveyQDoc = db.collection("Surveys").document("PastWSurveyQ");
                 pastWSurveyRDoc = db.collection("Surveys").document("PastWSurveyR");
                 pastWSurveyQLDoc = db.collection("Surveys").document("PastWSurveyQL");
                 times = db.collection("Surveys").document("PastSurveyTimes");
                 start_queries();
            }
        });
    }

    private void setTextView(){
        String s = "";
        // view all questions for a survey
        for(int i =0; i < w_questions.size(); i++){
            int question_num = i+1;
            if(w_questions.get(i).charAt(0) == 'M'){
                s = s+  question_num + ". " + w_questions.get(i).substring(1) + "\n" + w_questions_choice.get(i) + "\n";
            }else if(w_questions.get(i).charAt(0) == 'O'){
                s = s+ question_num+ ". " + w_questions.get(i).substring(3) + "\n";
            }else{
                s =s + question_num + ". " + w_questions.get(i).substring(1) + "\n";
            }
        }
        previewSurveyTxt.setText(s);
    }
    //first will get all of the current survey data and store them into past survey data
    void start_queries(){
        wSurveyQDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("w_survey_count")){ past_w_survey_count = (ArrayList<String>) document.get("w_survey_count"); }
                            if(pair.getKey().toString().equals("w_survey_q")){ past_w_survey_q = (ArrayList<String>) document.get("w_survey_q"); }
                            if(pair.getKey().toString().equals("w_survey_qc")){ past_w_survey_qc = (ArrayList<String>) document.get("w_survey_qc"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        start_queries2();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    void start_queries2(){
        // a new field will be added into past survey containing the name and the number of the list
        // past survey data will now be put in that new field
        pastWSurveyQLDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        int size = data.size() +1;
                        String fieldName = "w_survey_"+size;
                        Object past_survey_count_obj = past_w_survey_count;
                        Object past_survey_questions_obj = past_w_survey_q;
                        Object past_survey_question_count_obj = past_w_survey_qc;
                        pastWSurveyQDoc.update(fieldName,past_survey_questions_obj);
                        pastWSurveyQLDoc.update(fieldName,past_survey_question_count_obj );
                        pastWSurveyRDoc.update(fieldName, past_survey_count_obj);
                        start_queries3();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    void start_queries3(){
        //new Data will now be in WSurveyQ
        Object w_count_obj = w_count;
        Object w_questions_obj = w_questions;
        Object w_questions_choice_t_obj = w_questions_choice;
        wSurveyQDoc.update("w_survey_count",w_count_obj);
        wSurveyQDoc.update("w_survey_q",w_questions_obj);
        wSurveyQDoc.update("w_survey_qc",w_questions_choice_t_obj);
        wSurveyRDoc.delete();
        Map<String, Object> newWSurveyR = new HashMap<>();
        db.collection("Surveys").document("WSurveyR").set(newWSurveyR).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        start_time_query();
                    }
                });
    }
    void start_time_query(){
        // will add a timestamp when the survey was archived
        times.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, data.toString());
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("times")){ times_list = (ArrayList<String>) document.get("times"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Date date = Calendar.getInstance().getTime();
                        date.getTime();
                        times_list.add(date.toString());
                        Object times_obj = times_list;
                        times.update("times", times_obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PreviewNewSurvey.this, "Survey Has been Successfully Uploaded.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PreviewNewSurvey.this, AdminLanding.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
