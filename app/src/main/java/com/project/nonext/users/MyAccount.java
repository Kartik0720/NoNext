package com.project.nonext.users;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyAccount extends AppCompatActivity {
    private TextView userDetailFirstName,userDetailLastName,userDetailEmail,userDetailMobile,userDetailBirthdate,userDetailChangePassword;
    private ImageView editAll;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DocumentReference df;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button userDetailUpdateBtn;
    DatePickerDialog picker;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_my_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //FindViewById
        /*firstName = findViewById(R.id.firstNameTextView);
        lastName = findViewById(R.id.lastNameTextView);
        mobileNo = findViewById(R.id.mobileNoTextView);
        email = findViewById(R.id.profileEmailTextView);
        address = findViewById(R.id.profileAddressTextView);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        mobileNoEditText = findViewById(R.id.mobileNoEditText);
        emailEditText = findViewById(R.id.profileEmailEditText);
        addressEditText = findViewById(R.id.profileAddressEditText);
        editAll = findViewById(R.id.editAll);*/
        userDetailFirstName = findViewById(R.id.userDetailFirstName);
        userDetailLastName = findViewById(R.id. userDetailLastName);
        userDetailEmail = findViewById(R.id.userDetailEmail);
        userDetailMobile = findViewById(R.id.userDetailMobile);
        userDetailUpdateBtn = findViewById(R.id. userDetailUpdateBtn);
        userDetailChangePassword = findViewById(R.id.userDetailChangePassword);


        //getting Current User Data
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
       // df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
        df =  fStore.collection("Users").document(String.valueOf(user.getUid()));
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String firstNameDb = documentSnapshot.getString("userfirstname");
                    String lastNameDb = documentSnapshot.getString("userlastname");
                    String mobileDb = documentSnapshot.getString("usermobile");
                    String emailDb = documentSnapshot.getString("useremail");
                    userDetailFirstName.setText(firstNameDb);
                    userDetailLastName.setText(lastNameDb);
                    userDetailMobile.setText(mobileDb);
                    userDetailEmail.setText(emailDb);
                    if (documentSnapshot.getString("userbirthdate") != null) {
                        String birthdateDb = documentSnapshot.getString("userbirthdate");
                        userDetailBirthdate.setText(birthdateDb);
                    }
                }
            }
        });
        //Changing Password
        userDetailChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyAccount.this,ChangePasswordActivity.class));
            }
        });
        //Updating User Details
        userDetailUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               updatebtn();
            }
        });
       //Fetching Date from DatePickerDialog
        userDetailBirthdate = findViewById(R.id.userDetailBirthday);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        userDetailBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(MyAccount.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                        userDetailBirthdate.setText(dayofMonth +"/" + (month + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });
    }


    public void updatebtn(){
        FirebaseUser user = mAuth.getCurrentUser();
        String newUserDetailFirstName = userDetailFirstName.getText().toString();
        String newUserDetailLastName = userDetailLastName.getText().toString();
        String newUserDetailEmail = userDetailEmail.getText().toString();
        String newUserDetailMobile = userDetailMobile.getText().toString();
        String newUserDetailBirthdate = userDetailBirthdate.getText().toString();
       // df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
        df =  fStore.collection("Users").document(String.valueOf(user.getUid()));
        Map<String,String> save = new HashMap<>();
        save.put("userfirstname",newUserDetailFirstName);
        save.put("userlastname",newUserDetailLastName);
        save.put("useremail",newUserDetailEmail);
        save.put("usermobile",newUserDetailMobile);
        save.put("userbirthdate",newUserDetailBirthdate);
        df.set(save, SetOptions.merge())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MyAccount.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyAccount.this, "Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onPause() {
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }

    @Override
    protected void onResume() {

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onResume();
    }
}