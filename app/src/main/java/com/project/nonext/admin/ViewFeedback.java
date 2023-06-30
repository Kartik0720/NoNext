package com.project.nonext.admin;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

import java.util.ArrayList;
import java.util.Objects;

public class ViewFeedback extends AppCompatActivity {

    RecyclerView recview;
    ArrayList<feedbackModel> feedbackArrayList;
    FirebaseFirestore fStore;
    feedbackAdapter fbAdapter;
    ProgressBar feedbackProgressBar;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Feedback");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        feedbackProgressBar = findViewById(R.id.adminFbProgressBar);
        recview=(RecyclerView)findViewById(R.id.adminFbList);
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(this));

        fStore=FirebaseFirestore.getInstance();
        feedbackArrayList=new ArrayList<feedbackModel>();
        fbAdapter = new feedbackAdapter(feedbackArrayList);
        recview.setAdapter(fbAdapter);

        viewFeedback();
    }
    private void viewFeedback(){
        fStore.collection("feedback")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            feedbackProgressBar.setVisibility(View.GONE);
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                feedbackArrayList.add(dc.getDocument().toObject(feedbackModel.class));
                            }
                            fbAdapter.notifyDataSetChanged();
                            feedbackProgressBar.setVisibility(View.GONE);
                        }
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