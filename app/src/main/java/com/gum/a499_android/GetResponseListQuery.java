package com.gum.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class GetResponseListQuery  extends AppCompatActivity {

    // this activity is similar to determine question type, however since we have seperate collectins for first survey,
    // weekly survey, past weekly survey q, past weekly survey r, past weekly survey count, time etc we have to do queries for each one depending
    // on what the user selects.
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String TAG = "Get Response List";
    public static ArrayList<String> listSelected_responses = new ArrayList<>();
    public static ArrayList<String> listSelected_questions = new ArrayList<>();
    public static ArrayList<String> listSelected_questions_c = new ArrayList<>();
    public static int index_charts = 0;
    DocumentReference surveyTotal;
    DocumentReference pastResponses;
    DocumentReference pastQuestions;
    DocumentReference pastQuestionsL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_responses);
        if(!SelectASurvey.past_survey) {
            surveyTotal = db.collection("Surveys").document(SelectASurvey.document_responses_str);
            getWSurvey();
        }else{
            pastResponses = db.collection("Surveys").document(SelectASurvey.document_responses_str);
            pastQuestions = db.collection("Surveys").document(SelectASurvey.document_questions_str);
            pastQuestionsL = db.collection("Surveys").document(SelectASurvey.document_questions_answers);
            getPastResponses();
            getPastQuestions();
            getPastQuestionsL();
        }
    }
    void getWSurvey(){
        surveyTotal.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals(SelectASurvey.field_name_response)){ listSelected_responses = (ArrayList<String>) document.get(SelectASurvey.field_name_response); }
                            if(pair.getKey().toString().equals(SelectASurvey.field_name_questions)){ listSelected_questions = (ArrayList<String>) document.get(SelectASurvey.field_name_questions); }
                            if(pair.getKey().toString().equals(SelectASurvey.field_name_questions_list)){ listSelected_questions_c = (ArrayList<String>) document.get(SelectASurvey.field_name_questions_list); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d(TAG, listSelected_responses.toString() + "\n " + listSelected_questions.toString() + " \n " + listSelected_questions_c.toString());
                        char first_response = listSelected_questions.get(0).charAt(0);
                        Intent intent;
                        Log.d(TAG, first_response+"");
                        if(first_response == 'O'){
                            intent = new Intent(GetResponseListQuery.this, ViewResponseO.class);
                        }else if(first_response == 'R'){
                            intent = new Intent(GetResponseListQuery.this, ViewResponseR.class);
                        }else{
                            intent = new Intent(GetResponseListQuery.this, ViewResponses.class);
                        }
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return surveyList;
    }
    void getPastResponses(){
        pastResponses.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals(SelectASurvey.field_name_response)){ listSelected_responses = (ArrayList<String>) document.get(SelectASurvey.field_name_response); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return surveyList;
    }

    void getPastQuestions(){
        pastQuestions.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals(SelectASurvey.field_name_response)){ listSelected_questions = (ArrayList<String>) document.get(SelectASurvey.field_name_response); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return surveyList;
    }

    void getPastQuestionsL(){
        pastQuestionsL.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals(SelectASurvey.field_name_response)){ listSelected_questions_c = (ArrayList<String>) document.get(SelectASurvey.field_name_response); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d(TAG, listSelected_responses.toString() + "\n " + listSelected_questions.toString() + " \n " + listSelected_questions_c.toString());
                        char first_response = listSelected_questions.get(0).charAt(0);
                        Intent intent;
                        Log.d(TAG, first_response+"");
                        if(first_response == 'O'){
                            intent = new Intent(GetResponseListQuery.this, ViewResponseO.class);
                        }else if(first_response == 'R'){
                            intent = new Intent(GetResponseListQuery.this, ViewResponseR.class);
                        }else{
                            intent = new Intent(GetResponseListQuery.this, ViewResponses.class);
                        }
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return surveyList;
    }
}
