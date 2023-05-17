package com.example.a499_android;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CreateGroupSuccessPageAdmin extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_groups_success);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Group");
        Button createGroupFinishBtn = findViewById(R.id.finishCreateGroupsBtn);
        String inviteCode = getIntent().getExtras().getString("InviteCode");
        TextView inviteCodeDisplay = findViewById(R.id.createInviteCode);
        inviteCodeDisplay.setText(inviteCode);

        createGroupFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroupSuccessPageAdmin.this, AdminLanding.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
