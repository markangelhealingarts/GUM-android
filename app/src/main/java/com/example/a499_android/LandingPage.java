package com.example.a499_android;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.example.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.TimeOfDayOrBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.LogRecord;

import static com.example.a499_android.LoginActivity.loggedUserName;

public class LandingPage extends AppCompatActivity {

    public static final String EXTRA = "LandingPage EXTRA";
    private static final String TAG = "Current User Data";
    public static final String WSURVEYQ = "WSurveyQ";
    public static final String WSURVEYR = "WSurveyR";
    public static final String TYPE_SURVEY = "Weekly Survey";
    public static final String W_SURVEY_COUNT = "w_survey_count";
    public static final String W_SURVEY_Q = "w_survey_q";
    public static final String W_SURVEY_QC = "w_survey_qc";

    public static final String test = "Delete this later, this variable is for testing.";
    public static String fitnessLevel = "";
    public static String avatarName;
    String profile_path_total;
    public ArrayList<String> workoutList = new ArrayList<>();
    DocumentReference docRef;
    private boolean isAdmin = false;
    private boolean is_admin = true;
    TextView messageMark;
    MenuItem toAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome...");
//        actionBar.setDisplayShowTitleEnabled(false);
        CardView editSchedule = findViewById(R.id.editScheduleBtn);
        //CardView changeAvatar = findViewById(R.id.viewShopBtn);
        //Too lazy to remove, just making them inaccessible for now.
        //changeAvatar.setVisibility(View.INVISIBLE);
        //CardView startSurveyBtn = findViewById(R.id.startSurveyBtnC);
        //startSurveyBtn.setVisibility(View.INVISIBLE);
        //Originally called msgAdmin. Changed to groups per Marks request.
        CardView groups = findViewById(R.id.groupButton);
        // Start Exercise
        CardView startExerciseBtn = findViewById(R.id.startWorkoutBtn);
        CardView viewVideos = findViewById(R.id.videoDemosBtn);
        TextView displayedPoints = findViewById(R.id.pointDisplay);
        ImageView icon = findViewById(R.id.landingIcon);
        //ImageView profile_image = findViewById(R.id.avatarPicture);
        messageMark = findViewById(R.id.messageMarktxt);
//        TextView displayedUsername = findViewById(R.id.usernameDisplay);



        // NOTE: user info read from db will be hardcoded until login activity is done
        String uName = SaveSharedPreference.getUserName(LandingPage.this);

        makeNotificationChannel();

        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //This originally used currentUserName
        DocumentReference userNameRef = db.collection("Users").document(uName);
        docRef = db.collection("Users").document(uName);
        // Read User Info
        getUserInfo(new FirestoreCallback() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    Log.d(TAG, "Found User Data");
                    displayedPoints.setText(document.getData().get("Points").toString());
                    //set image when loaded in
                    profile_path_total = document.getData().get("AvatarUrl").toString();
                    avatarName = profile_path_total.substring(0, profile_path_total.length()-4);
                    //removes .png from string to find in path
                    int size_profile = profile_path_total.length();
                    int stop = size_profile - 4;
                    String profile_path_name = profile_path_total.substring(0,stop);
                    Context context = LandingPage.this;
                    int resourceId =getResourceId(profile_path_name, "drawable", context.getPackageName(),context);
                    //profile_image.setImageResource(resourceId);

                    actionBar.setTitle("Welcome, " + uName);
                    if (document.getData().get("IsAdmin") == null) {
                        is_admin = false;
                        isAdmin = true;
                        invalidateOptionsMenu();
                    }else{
                        messageMark.setText("Message Users");
                    }
                } else {
                    Toast.makeText(LandingPage.this, "Unable to Load User Data", Toast.LENGTH_SHORT).show();
                }
            }
        }, userNameRef);

        editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, UpdateSchedule.class);
                startActivity(intent);
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LandingPage.this);
                alert.setTitle("Copy the following link to learn more.");
                alert.setMessage("https://getupandmove.net/GUM/GUM_Orientation/index.html\n");
                alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.create().show();
            }
        });
/*
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, SelectAvatar.class);
                startActivity(intent);
            }
        });

        startSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference usersList =  db.collection("Surveys").document("WSurveyUsers");
                usersList.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> list = new ArrayList<>();
                        boolean check_users_comp = true;
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG,data.toString() + " times size " + data.size());
                                Iterator it = data.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pair = (Map.Entry) it.next();
                                    if (pair.getKey().toString().equals("users_finished")) {
                                        list = (ArrayList<String>) document.get("users_finished");
                                    }
                                    it.remove(); // avoids a ConcurrentModificationException
                                }
                                for(int i =0; i< list.size(); i++){
                                    if(list.get(i).equals(loggedUserName)){
                                        check_users_comp= false;
                                        i = list.size();
                                    }
                                }
                                if(check_users_comp){
                                    Intent intent = new Intent(LandingPage.this, DetermineQuestionType.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LandingPage.this, "Looks like you've already completed the survey, please check again sometime next week.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });
*/
        // Start Activity
        startExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LandingPage.this, SelectWorkout.class);
                startActivity(intent);
            }
        });

        viewVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, VideoDemonstrations.class);
                startActivity(intent);
            }
        });

        //TODO remove this

        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminMsgList.fromAdmin = false;// used to make sure when login from admin to regular user, it does not do other queries.
                if(is_admin){
                    Log.d(TAG, "admin");
                    Intent intent = new Intent(LandingPage.this, AdminMsgList.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(LandingPage.this, GroupsLandingPage.class);
                    startActivity(intent);
                }
            }
        });

       if(FinishedExerciseActivity.fromNotification){
           DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   switch (which){
                       case DialogInterface.BUTTON_POSITIVE:
                           //Yes button clicked
                           //TODO
                           //get difficulty and current avatar
                           //get random exercise from difficulty
                           //construct video name

                           SelectWorkout.selectedWorkout = "session1_gentle_nature";    //Temporary for testing

                           Intent intent = new Intent(LandingPage.this, StartExercise.class);
                           startActivity(intent);
                           break;

                       case DialogInterface.BUTTON_NEGATIVE:
                           //No button clicked
                           FinishedExerciseActivity.fromNotification = false;
                           dialog.cancel();
                           break;
                   }
               }
           };

           androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(LandingPage.this);
           builder.setMessage("Start Scheduled Exercise? Saying no means you will miss out on points!").setPositiveButton("Yes", dialogClickListener)
                   .setNegativeButton("No", dialogClickListener).show();
       }

    }//End of onCreate()

    // Intent Factory
    public static Intent getIntent(Context context, String val){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(EXTRA, val);
        return intent;
    }
/*
    //maybe retrieve schedule and pass it through intent first
    public void startEditScheduleActivity(View view){
        Intent intent = SelectSchedule.getIntent(this, "");
        startActivity(intent);
    }
*/
    private void getUserInfo(FirestoreCallback firestoreCallback, DocumentReference currentUserName) {
        currentUserName.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallback.onSuccess(document);
                        Log.d(TAG, currentUserName.getId() + " username");
                        loggedUserName = currentUserName.getId();
                        CreateAccount.first_survey = false;

                        Map<String, Object> data = document.getData();// map to get workoutlist data
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
                            if(pair.getKey().toString().equals("Schedule")){ workoutList = (ArrayList<String>) document.get("Schedule"); }
                            if(pair.getKey().toString().equals("FitnessLvl")){ fitnessLevel = (String) document.get("FitnessLvl");}

                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d(TAG, "workout list: " + workoutList.toString());
                        Log.d(TAG, "Fitness Level: " + workoutList.toString());
                    }
                } else {
                    Log.d(TAG, "ERROR: ", task.getException());
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }

    private void makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("GUM", "GUM", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for GUM");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static int getResourceId(String pVariableName, String pResourcename, String pPackageName,Context context)
    {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    /**
     * The isAdmin if block is weird...
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_page, menu);

        if (isAdmin) {
            menu.findItem(R.id.to_admin).setVisible(false);
        } else {
            menu.findItem(R.id.to_admin).setVisible(true);
        }
        invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_item:
                SaveSharedPreference.clearUserName(LandingPage.this); //clears preference of username and anything else in there
                Intent toMainActivityIntent = new Intent(LandingPage.this, MainActivity.class);
                startActivity(toMainActivityIntent);
                break;
            case R.id.to_admin:
                startActivity(new Intent(LandingPage.this, AdminLanding.class));
                break;
            case R.id.settings_item:
                startActivity(new Intent(LandingPage.this, ChangeLevel.class));
                break;
            case R.id.gum_website:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://gum-website.herokuapp.com/"));
                startActivity(i);
                break;
            case R.id.contact:
                Intent j = new Intent(Intent.ACTION_VIEW);
                j.setData(Uri.parse("https://www.markangelhealingarts.com/contact.html"));
                startActivity(j);
                break;
            case R.id.about_mark:
                Intent m = new Intent(Intent.ACTION_VIEW);
                m.setData(Uri.parse("https://www.markangelhealingarts.com/about.html"));
                startActivity(m);
                break;
            case R.id.Donate:
                Intent s = new Intent(Intent.ACTION_VIEW);
                s.setData(Uri.parse("https://www.paypal.com/donate?token=a1QKv5G3IZ5uowCm1N_s7OUN3hxGZEHO8QEaSLTNEj8ofuoE95oDoweRV--ul1zKS_at90xDrPiyR2yG"));
                startActivity(s);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
