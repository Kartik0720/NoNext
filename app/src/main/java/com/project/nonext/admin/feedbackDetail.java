package com.project.nonext.admin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

public class feedbackDetail extends AppCompatActivity {
    TextView fbAdminUserNamedetail,fbadminMobiledetail,fbAdminEmaildetail,fbAdminFbdetail;
    DocumentReference df;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_feedback_detail);

        fbAdminEmaildetail=findViewById(R.id.fbAdminEmaildetail);
        fbadminMobiledetail=findViewById(R.id.fbadminMobiledetail);
        fbAdminUserNamedetail=findViewById(R.id.fbAdminUserNamedetail);
        fbAdminFbdetail=findViewById(R.id.fbAdminFbdetail);



     /*   df = fStore.collection("feedback").getParent();
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                fbAdminUserNamedetail.setText(value.getString("username"));
                fbadminMobiledetail.setText(value.getString("mobile"));
                fbAdminEmaildetail.setText(value.getString("email"));
                fbAdminFbdetail.setText(value.getString("feedback"));
            }
        });
*/
    }
}