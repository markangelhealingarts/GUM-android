package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Agreement extends AppCompatActivity {
    CheckBox checkBox;
    Button proceedBtn;
    boolean is_checked = false;

    //hi there
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement);
        checkBox = findViewById(R.id.checkBox);
        proceedBtn = findViewById(R.id.proceedBtn);
    }
    public void Check(View v)
    {
        // the agreement
        // Concatenation of the checked options in if
        // Check is used to check whether the person has agreed to the terms above or not
        if(checkBox.isChecked()) {
            is_checked = true;
            startActivity(new Intent(Agreement.this, CreateAccount.class));
        }else {
            Toast.makeText(this, "Check agreement to proceed.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
