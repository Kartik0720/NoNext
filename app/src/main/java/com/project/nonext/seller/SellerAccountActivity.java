package com.project.nonext.seller;

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
import com.project.nonext.users.ChangePasswordActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SellerAccountActivity extends AppCompatActivity {
    private TextView sellerDetailFirstName,sellerDetailLastName,sellerDetailEmail,sellerDetailMobile,sellerDetailBirthdate,sellerDetailChangePassword;
    private ImageView editAll;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DocumentReference df;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button sellerDetailUpdateBtn;
    DatePickerDialog picker;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //FindViewById
        sellerDetailFirstName = findViewById(R.id.sellerDetailFirstName);
        sellerDetailLastName = findViewById(R.id. sellerDetailLastName);
        sellerDetailEmail = findViewById(R.id.sellerDetailEmail);
        sellerDetailMobile = findViewById(R.id.sellerDetailMobile);
        sellerDetailUpdateBtn = findViewById(R.id. sellerDetailUpdateBtn);
        sellerDetailChangePassword = findViewById(R.id.sellerDetailChangePassword);


        //getting Current seller Data
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
       // df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
        df = fStore.collection("Users").document(user.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String firstNameDb = documentSnapshot.getString("sellerfirstname");
                    String lastNameDb = documentSnapshot.getString("sellerlastname");
                    String mobileDb = documentSnapshot.getString("sellermobile");
                    String emailDb = documentSnapshot.getString("selleremail");

                    sellerDetailFirstName.setText(firstNameDb);
                    sellerDetailLastName.setText(lastNameDb);
                    sellerDetailMobile.setText(mobileDb);
                    sellerDetailEmail.setText(emailDb);
                    if (documentSnapshot.getString("sellerbirthdate") != null) {
                        String birthdateDb = documentSnapshot.getString("sellerbirthdate");
                        sellerDetailBirthdate.setText(birthdateDb);
                    }
                }
            }
        });
        //Changing Password
        sellerDetailChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerAccountActivity.this, ChangePasswordActivity.class));
            }
        });
        //Updating seller Details
        sellerDetailUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatebtn();
            }
        });
        //Fetching Date from DatePickerDialog
        sellerDetailBirthdate = findViewById(R.id.sellerDetailBirthday);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        sellerDetailBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(SellerAccountActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                        sellerDetailBirthdate.setText(dayofMonth +"/" + (month + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });
    }


    public void updatebtn(){
        FirebaseUser user = mAuth.getCurrentUser();
        String newsellerDetailFirstName = sellerDetailFirstName.getText().toString();
        String newsellerDetailLastName = sellerDetailLastName.getText().toString();
        String newsellerDetailEmail = sellerDetailEmail.getText().toString();
        String newsellerDetailMobile = sellerDetailMobile.getText().toString();
        String newsellerDetailBirthdate = sellerDetailBirthdate.getText().toString();
       // df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
        df = fStore.collection("Users").document(user.getUid());
        Map<String,String> save = new HashMap<>();
        save.put("sellerfirstname",newsellerDetailFirstName);
        save.put("sellerlastname",newsellerDetailLastName);
        save.put("selleremail",newsellerDetailEmail);
        save.put("sellermobile",newsellerDetailMobile);
        save.put("sellerbirthdate",newsellerDetailBirthdate);
        df.set(save, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SellerAccountActivity.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SellerAccountActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
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