package com.project.nonext.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.LoginActivity;
import com.project.nonext.MainActivity;
import com.project.nonext.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private FirebaseAuth mAuth;
    private MaterialCardView profileLogoutCardView,myAccount,settings,help,address,wishlist;
    private ImageButton editFirstName;
    private DocumentReference documentReference;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private TextView profileFirstName,profileLastName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String user_userName;
    private String user_UserEmail;
    private String user_userPass;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        pname.findViewById(R.id.profile_Name);
//        pemail.findViewById(R.id.profile_Email);
//        pmobile.findViewById(R.id.profile_Mobile);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        showAllDataUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        profileLogoutCardView= v.findViewById(R.id.profileLogoutCardView);
        myAccount = v.findViewById(R.id.myAccountCardView);
        address = v.findViewById(R.id.addressCardView);
//        settings = v.findViewById(R.id.settingsCardView);
        help = v.findViewById(R.id.ordersCardView);
        wishlist = v.findViewById(R.id.wishlistCardView);
        profileFirstName = v.findViewById(R.id.profileFirstName);
        profileLastName = v.findViewById(R.id.profileLastName);

        Toolbar toolbarFragment = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setToolbar(toolbarFragment,"Profile");

        /* setHasOptionsMenu(true);*/
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            logoutBtn.setVisibility(View.VISIBLE);
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//        }else{
//            showAllDataUser();
//        }
        /*showAllDataUser();*/
        showAllDataUser();
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddressActivity.class));
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MyAccount.class));
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MyOrdersActivity.class));
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MyWishList.class));
            }
        });
        profileLogoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = (new Intent(getActivity(),
                        LoginActivity.class));
                startActivity(i);
            }
        });
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            logoutBtn.setVisibility(View.VISIBLE);
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//        }else{
//            showAllDataUser();
//        }

    }
    private void showAllDataUser(){

        /*String registeredUserId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(registeredUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_userName = snapshot.child("UserName").getValue().toString();
                String user_userEmail = snapshot.child("UserEmail").getValue().toString();
                String user_userMobile = snapshot.child("UserMobile").getValue().toString();
                pname.setText(user_userName);
                pemail.setText(user_userEmail);
                pmobile.setText(user_userMobile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        //Displaying Data from Firestore
        FirebaseUser user = mAuth.getCurrentUser();
       /* documentReference = fStore.collection("Operators")
                .document("Users")
                .collection(user.getUid())
                .document("userregister");*/
        documentReference =  fStore.collection("Users").document(String.valueOf(user.getUid()));
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String user_userFirstName = documentSnapshot.getString("userfirstname");
                    String user_userLastName = documentSnapshot.getString("userlastname");
//                        String user_userMobile = documentSnapshot.getString("UserMobile");
//                        Log.v(TAG, user_userEmail);
                    profileFirstName.setText(user_userFirstName);
                    profileLastName.setText(user_userLastName);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.draw_menu,menu);
    }

}