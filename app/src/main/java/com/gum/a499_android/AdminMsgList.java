package com.gum.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class AdminMsgList extends AppCompatActivity {

    // this is the admin message list, this list is used for two things, allows the admin to select who to message ,
    // or to select a user to view their response for a specific survey
    // there is a static variable that determines where this activity intented from
    ArrayList<String> usersList = new ArrayList<>();
    ArrayList<String> priorityList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usersDoc;
    TextView priorityText;
    public String TAG = "Admin Landing";
    public static String userNameSelected = "";
    public static boolean fromAdmin = false;
    Button finishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list_msg);
        finishBtn = findViewById(R.id.backBtnUserList);
        finishBtn.setVisibility(View.INVISIBLE);
        priorityText = findViewById(R.id.priorityList);
        if(ViewResponseR.select_question){
            //if this is from viewresponse, the priority list's unread messages list is INVISIBLE
            priorityText.setVisibility(View.INVISIBLE);
        }
        usersDoc = db.collection("Users_List").document("List");
        init_firebase();

    }

    //query to get the users
    void init_firebase(){
        usersDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("user_names")){ usersList = (ArrayList<String>) document.get("user_names"); }
                            if(pair.getKey().toString().equals("priority_list")){ priorityList = (ArrayList<String>) document.get("priority_list"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        RecyclerView rv = findViewById(R.id.users_list_recycler_view);
                        rv.setLayoutManager(new LinearLayoutManager(AdminMsgList.this));
                        rv.setAdapter(new Adapter());
                        if (priorityList.size() == 0) {
                            priorityText.setText("No Unread Messages");
                        }else {
                            String priority_str = "";
                            int i = 0;
                            for (String s : priorityList) {
                                priority_str = priority_str + priorityList.get(i) + "\n";
                                i++;
                            }
                            priorityText.setText("Unread Messages \n" + priority_str);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        if(ViewResponseR.select_question){
            finishBtn.setVisibility(View.VISIBLE);
        }
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class Adapter extends RecyclerView.Adapter<AdminMsgList.ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(AdminMsgList.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bind(usersList.get(position));
        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }

    }
    private class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.message_item, parent, false));
        }

        public void bind(String user) {
            TextView item = itemView.findViewById(R.id.message_id);
            item.setText(user);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fromAdmin = true;
                    userNameSelected = user;
                    Intent intent;
                    //depending on what activity it is, we can then see what the response was by setting the variable user to username selected
                    if(ViewResponseR.select_question){
                        intent = new Intent(AdminMsgList.this, ViewResponseUser.class);
                    }else {
                        intent = new Intent(AdminMsgList.this, MessageAdmin.class);
                    }
                    startActivity(intent);
                }
            });
        }
    }



}
