package com.project.nonext.users;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FeedbackUser extends AppCompatActivity {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextInputEditText UserNameFb, edtUserMobile, edtUserEmail, feedbackDetail;
    private Button btnUserFb;
    private ProgressBar loadingPB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Feedback From");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtUserEmail = findViewById(R.id.edtUserEmailFeedback);
        edtUserMobile = findViewById(R.id.edtUserMobileFeedback);
        UserNameFb = findViewById(R.id.edtFeedbackUserName);
        btnUserFb = findViewById(R.id.btnUserfeedback);
        feedbackDetail = findViewById(R.id.feedbackDetai);
        loadingPB = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
//        databaseReference = firebaseDatabase.getReference("Users");


        btnUserFb.setOnClickListener(view -> {
            createUser();
        });
    }

    private void createUser() {
        String UserMobile = Objects.requireNonNull(edtUserMobile.getText()).toString();
        String UserEmail = Objects.requireNonNull(edtUserEmail.getText()).toString();
        String UserName = Objects.requireNonNull(UserNameFb.getText()).toString();
        String fbDetail = Objects.requireNonNull(feedbackDetail.getText()).toString();

//        UserId =UserName;
        if (TextUtils.isEmpty(UserName)) {
            UserNameFb.setError("Please Enter Name");
            UserNameFb.requestFocus();
        } else if (TextUtils.isEmpty(UserMobile)) {
            edtUserMobile.setError("Please Enter Mobile Number");
            edtUserMobile.requestFocus();
        } else if (UserMobile.length() != 10) {
            edtUserMobile.setError("Please Enter valid Mobile Number");
            edtUserMobile.requestFocus();
        } else if (TextUtils.isEmpty(UserEmail)) {
            edtUserEmail.setError("Email can not be empty");
            edtUserEmail.requestFocus();
        } else if (TextUtils.isEmpty(fbDetail)) {
            feedbackDetail.setError("Please Enter Feedback Details");
            feedbackDetail.requestFocus();

        } else {
                databaseadd();
        }

    }

    private void databaseadd() {
        Map<String,String> items =new HashMap<>();
        items.put("username",UserNameFb.getText().toString().trim());
        items.put("mobile",edtUserMobile.getText().toString().trim());
        items.put("email",edtUserEmail.getText().toString().trim());
        items.put("feedback",feedbackDetail.getText().toString().trim());

        db.collection("feedback").add(items)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        UserNameFb.setText("");
                        edtUserMobile.setText("");
                        edtUserEmail.setText("");
                        feedbackDetail.setText("");
                        Toast.makeText(getApplicationContext(),"Feedback Send Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}