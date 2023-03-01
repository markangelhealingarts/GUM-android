package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddSurveyQuestions extends AppCompatActivity {
    // Objects  that are used to reference xml ids
    // this activity allows the admin user to select what type of question will be for the next question

    RadioGroup radioGroup;
    RadioButton b1, b2, b3;
    Button nxtQuestion;
    EditText questionText;
    public String TAG = "AddSurveyQuestions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_survey_questions);
        radioGroup = findViewById(R.id.radioQuestionsGroupA);
        questionText = findViewById(R.id.addQuestionEditTxt);
        nxtQuestion = findViewById(R.id.submitBtnA);
        b1 = findViewById(R.id.question1A);
        b2 = findViewById(R.id.question2A);
        b3 = findViewById(R.id.question3A);
        Log.d(TAG, AdminLanding.w_survey_count_list.toString() + "\n" + AdminLanding.w_survey_responses_list + "\n" + AdminLanding.w_survey_questions_list);
        nxtQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //no text
                if (questionText.getText().toString().equals("")) {
                    Toast.makeText(AddSurveyQuestions.this, "No question has been written!", Toast.LENGTH_SHORT).show();
                } else {
                    //if multiple choice is checked , an M will be appended to the question to ensure it is a multiple choice question, then the user
                    //will be taken to the add survey question mc activity to add the responses of each question
                    char type_question = isChecked();
                    if (type_question == 'M') {
                        String question = "M"+questionText.getText().toString();
                        AdminLanding.w_survey_questions_list.add(question);
                        startActivity(new Intent(AddSurveyQuestions.this, AddSurveyQuestionsMC.class));
                    }else if(type_question == 'O'){
                        // if a scale question is checked, an O10 will be appended to ensure the question is a multiple choice question
                        String question = "O10"+questionText.getText().toString();
                        AdminLanding.w_survey_questions_list.add(question);
                        //since it is a scale choice question, there are no responses, it can be left empty, and also we need to append a list of
                        // 10 zeros to indicate no data has been recorded
                        AdminLanding.w_survey_responses_list.add(" ");
                        AdminLanding.w_survey_count_list.add("0,0,0,0,0,0,0,0,0,0,");
                        AddSurveyQuestionsCount.count++;
                        if(AddSurveyQuestionsCount.count == AddSurveyQuestionsCount.questionCount){
                            Toast.makeText(AddSurveyQuestions.this, "Survey is finished!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddSurveyQuestions.this, PreviewNewSurvey.class));
                        }else{
                            startActivity(new Intent(AddSurveyQuestions.this, AddSurveyQuestions.class));
                        }
                    }else{
                        String question = "R"+questionText.getText().toString();
                        AdminLanding.w_survey_questions_list.add(question);
                        AdminLanding.w_survey_responses_list.add(" ");
                        AdminLanding.w_survey_count_list.add(" ");
                        AddSurveyQuestionsCount.count++;
                        // if it's a response question the count is empty because we don't need to record data on them
                        if(AddSurveyQuestionsCount.count == AddSurveyQuestionsCount.questionCount){
                            Toast.makeText(AddSurveyQuestions.this, "Survey is finished!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddSurveyQuestions.this, PreviewNewSurvey.class));
                        }else{
                            startActivity(new Intent(AddSurveyQuestions.this, AddSurveyQuestions.class));
                        }
                    }

                }
            }
        });

    }

    char isChecked() {
        //will check if user has selected an answer or not.
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(AddSurveyQuestions.this, "No answer has been selected", Toast.LENGTH_SHORT).show();
            return ' ';
        } else {
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
            char type_question_char = radioButton.getText().toString().charAt(0);
            return type_question_char;
        }
    }
}
//testing//////