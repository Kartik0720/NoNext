package com.project.nonext.admin;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class userList extends AppCompatActivity
{
    RecyclerView recview;
    ArrayList<userModel> datalist;
    FirebaseFirestore fStore;
    AdminAdapter adminAdapter;
    private FirebaseAuth mAuth;
    Task<QuerySnapshot> df;
    ProgressBar progressBar;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_users);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All user");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.adminUsersProgressBar);
        recview=(RecyclerView)findViewById(R.id.adminUserList);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();
        adminAdapter = new AdminAdapter(datalist);
        recview.setAdapter(adminAdapter);

        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getUid();
//        FirebaseUser user = mAuth.getCurrentUser();
        fStore=FirebaseFirestore.getInstance();
//        df = fStore.collection("Operators").document("Users").collection(user).document("userregister").getParent().get()
     //  df =  fStore.collection("Users")/*.whereEqualTo("usertype","Users")*/.get()
                /*.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        DocumentSnapshot[] list = new DocumentSnapshot[0];
                        for (DocumentSnapshot d : list) {
                            userModel obj = d.toObject(userModel.class);
                            datalist.add(obj);
                        }
                        adminAdapter.notifyDataSetChanged();
                    }
                });*/

       /* df = fStore.collection("alluser").get()*/
//        df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister").get()


    /* df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister").getParent().get()*/
        progressBar.setVisibility(View.VISIBLE);
        df =  fStore.collection("Users").whereEqualTo("usertype","User").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            userModel obj = d.toObject(userModel.class);
                            datalist.add(obj);
                        }
                        adminAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
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