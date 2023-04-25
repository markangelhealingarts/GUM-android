package com.gum.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddSurveyQuestionsMC  extends AppCompatActivity {
    Button addAnswer, nextQuestion,clearList;
    EditText answersText;
    TextView list_answers;
    String TAG = "Add Survey MC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_survey_mc_question);
        addAnswer = findViewById(R.id.addResponseBtn);
        nextQuestion = findViewById(R.id.submitBtnA);
        clearList = findViewById(R.id.clearListResponseBtn);
        answersText = findViewById(R.id.addAnswersEditTxt);
        list_answers = findViewById(R.id.listAnswers);
        ArrayList<String> answersList = new ArrayList<>();
        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answersText.getText().toString().equals("")){
                    //empty text
                    Toast.makeText(AddSurveyQuestionsMC.this, "Add some text!", Toast.LENGTH_SHORT).show();
                }else {
                    //check if the string if formatted correctly no commas ',' are allowed in a question
                    String check_comma = answersText.getText().toString();
                    boolean no_comma = true;
                    for(int i =0; i < check_comma.length(); i++){
                        if(check_comma.charAt(i) == ','){
                            no_comma = false;
                            i = check_comma.length();
                        }
                    }
                    if(no_comma) {
                        // can only add up to four questions per mc question
                        if (answersList.size() < 4) {
                            answersList.add(answersText.getText().toString());
                            String text = "";
                            for (String s : answersList) {
                                text = text + s + " \n";
                            }
                            list_answers.setText(text);
                        } else {
                            Toast.makeText(AddSurveyQuestionsMC.this, "List is full!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(AddSurveyQuestionsMC.this, "There is a comma, please remove comma.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // determine how many questions there are from 2-4 get the list of responses and convert them into one string seperated by comma
                if(answersList.size() >=2 && answersList.size() <=4){
                    String responses = returnStr(answersList);
                    if(answersList.size() == 2){
                        AddSurveyQuestionsCount.count++;
                        AdminLanding.w_survey_responses_list.add(responses);
                        AdminLanding.w_survey_count_list.add("0,0,");
                        if(AddSurveyQuestionsCount.count == AddSurveyQuestionsCount.questionCount) {
                            startActivity(new Intent(AddSurveyQuestionsMC.this, PreviewNewSurvey.class));
                        }else{
                            startActivity(new Intent(AddSurveyQuestionsMC.this, AddSurveyQuestions.class));
                        }
                    }else if(answersList.size()==3){
                        AddSurveyQuestionsCount.count++;
                        AdminLanding.w_survey_responses_list.add(responses);
                        AdminLanding.w_survey_count_list.add("0,0,0,");
                        if(AddSurveyQuestionsCount.count == AddSurveyQuestionsCount.questionCount){
                            startActivity(new Intent(AddSurveyQuestionsMC.this, PreviewNewSurvey.class));
                        }else{
                            startActivity(new Intent(AddSurveyQuestionsMC.this, AddSurveyQuestions.class));
                        }
                    }else{
                        AddSurveyQuestionsCount.count++;
                        AdminLanding.w_survey_responses_list.add(responses);
                        AdminLanding.w_survey_count_list.add("0,0,0,0,");
                        if(AddSurveyQuestionsCount.count == AddSurveyQuestionsCount.questionCount){
                            startActivity(new Intent(AddSurveyQuestionsMC.this, PreviewNewSurvey.class));
                        }else{
                            startActivity(new Intent(AddSurveyQuestionsMC.this, AddSurveyQuestions.class));
                        }

                    }
                }
            }
        });

        //clears list
        clearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answersList.clear();
                list_answers.setText("");
            }
        });

    }

    public String returnStr(ArrayList<String> list){
        String str = "";
        for(int i =0; i < list.size(); i++){
            str = str + list.get(i) + ",";
        }
        Log.d(TAG, str);
        return str;
    }
}
