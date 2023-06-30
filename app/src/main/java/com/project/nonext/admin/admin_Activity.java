package com.project.nonext.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.LoginActivity;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;
import com.project.nonext.seller.OrdersActivity;
import com.project.nonext.seller.ProductActivity;
import com.project.nonext.seller.ProfileActivity;
import com.project.nonext.seller.seller_add_product;
import com.project.nonext.users.OffersActivity;

public class admin_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    MaterialCardView addproduct;
    MaterialCardView mproduct;
    MaterialCardView vusers;
    MaterialCardView morders;
    MaterialCardView mpayment;
    MaterialCardView aoffers;
    MaterialCardView myproducts;
    MaterialCardView vfeedback;
    private Button signUpAndLogIn;
    private DocumentReference df;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        nav = (NavigationView) findViewById(R.id.admin_navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.admin_main_drawer);
        addproduct = findViewById(R.id.admin_add_product);
        mproduct = findViewById(R.id.admin_manage_products);
        vusers =findViewById(R.id.admin_view_users);
        morders =findViewById(R.id.adminManageOrdersMain);
        mpayment  =findViewById(R.id.admin_manage_payment);
        aoffers =findViewById(R.id.admin_apply_offers);
        myproducts =findViewById(R.id.admin_My_product);
        vfeedback =findViewById(R.id.admin_feedback);

        View hView = nav.getHeaderView(0);
        TextView hi = hView.findViewById(R.id.hi);
        TextView name_user = hView.findViewById(R.id.nav_name);
        TextView email_user = hView.findViewById(R.id.nav_email);
        signUpAndLogIn = hView.findViewById(R.id.signUpAndLogin);
        if(user!=null) {
            signUpAndLogIn.setVisibility(View.GONE);
            //df = fStore.collection("Operators").document("Users").collection(String.valueOf(user.getUid())).document("userregister");
            df = fStore.collection("Users").document(user.getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String name_nav_user = documentSnapshot.getString("userfirstname");
                        String email_nav_user = documentSnapshot.getString("useremail");
                        name_user.setText(name_nav_user);
                        email_user.setText(email_nav_user);
                    }
                }
            });
        }else{
            hi.setVisibility(View.INVISIBLE);
            name_user.setVisibility(View.INVISIBLE);
            email_user.setVisibility(View.INVISIBLE);
            signUpAndLogIn.setVisibility(View.VISIBLE);
        }


        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),seller_add_product.class));
            }
        });
        vusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewSellerAndUser.class));
            }
        });
        mproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageProducts.class));
            }
        });
        aoffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ApplyOffers.class));
            }
        });
        myproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductActivity.class));
            }
        });
        vfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ViewFeedback.class));
            }
        });
        morders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ViewAllOrders.class));
            }
        });
        mpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminManagePayment.class));
            }
        });

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.drawer_admin_home:
                        Toast.makeText(getApplicationContext(), "admin Home Panel is Open", Toast.LENGTH_LONG).show();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_admin_profile:
                        Toast.makeText(getApplicationContext(), "Admin product Panel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_admin_product:
                        Toast.makeText(getApplicationContext(), "Admin order Panel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_manage_product:
                        Toast.makeText(getApplicationContext(), "Admin manage Panel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ManageProducts.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_view_users:
                        Toast.makeText(getApplicationContext(), "Admin view user Panel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ViewSellerAndUser.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_manage_orders:
                        Toast.makeText(getApplicationContext(), "Admin manage orders Panel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ViewAllOrders.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_manage_payment:
                        Toast.makeText(getApplicationContext(), "Admin payment Panel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),AdminManagePayment.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_apply_offers:
                        Toast.makeText(getApplicationContext(), "offers Panel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), OffersActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_Adminmy_product:
                        Toast.makeText(getApplicationContext(), "Admin my product Panel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_admin_view_product:
                        Toast.makeText(getApplicationContext(), "admin FeedBackPanel is Open", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ViewFeedback.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_admin_logout:
                        mAuth.signOut();
                        Toast.makeText(getApplicationContext(), "Admin Signed Out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(admin_Activity.this, LoginActivity.class));

                }

                return true;
            }
        });
    }


//        adminLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                Toast.makeText(getApplicationContext(), "Admin Signed Out", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                finish();
//            }
//        }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton("NO",null)
                .show();
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