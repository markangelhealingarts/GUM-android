package com.gum.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GroupsLandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_landing_page);
        ActionBar actionBar = getSupportActionBar();
        Button createGroupBtn = findViewById(R.id.createGroupsBtn);
        Button joinGroupBtn = findViewById(R.id.joinGroupsBtn);
        Button currentGroupBtn = findViewById(R.id.currentGroupsBtn);

        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsLandingPage.this, CreateGroups.class);
                startActivity(intent);
            }
        });

        joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsLandingPage.this, JoinGroup.class);
                startActivity(intent);
            }
        });

        currentGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsLandingPage.this, ViewCurrentGroups.class);
                startActivity(intent);
            }
        });

    }
}
