package com.project.nonext.seller;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;
import com.project.nonext.users.OffersActivity;
import com.project.nonext.users.PrivacyPolicyActivity;

public class seller_Activity extends AppCompatActivity
{
    BottomNavigationView seller_NavigationView;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    MaterialCardView productCardView;
    MaterialCardView orders;
    MaterialCardView offers;
    MaterialCardView sellerPro;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private Button signUpAndLogIn;
    private DocumentReference df;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        //object
//        HomeFragment homeFragment = new HomeFragment();
//        ProfileFragment profileFragment = new ProfileFragment();

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        productCardView = findViewById(R.id.productCardView);
        orders=findViewById(R.id.ordersCard);
        nav=(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.seller_drawer);
        FloatingActionButton fab = findViewById(R.id.floating_btn);
        offers=findViewById(R.id.offer);
        sellerPro=findViewById(R.id.profile);
//        seller_NavigationView = findViewById(R.id.seller_bottomNavigationView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),seller_add_product.class));
                Toast.makeText(getApplicationContext(),"you clicked on add product",Toast.LENGTH_SHORT).show();
            }
        });


        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Header View
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
                        String name_nav_user = documentSnapshot.getString("sellerfirstname");
                        String email_nav_user = documentSnapshot.getString("selleremail");
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


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.drawer_seller_home:
                        Toast.makeText(getApplicationContext(),"Home Panel is Open",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),seller_Activity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_seller_product:
                        Toast.makeText(getApplicationContext(),"product Panel is Open",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),ProductActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_seller_order:
                        Toast.makeText(getApplicationContext(),"order Panel is Open",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_seller_profile:
                        Toast.makeText(getApplicationContext(),"profile Panel is Open",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_seller_offer:
                        Toast.makeText(getApplicationContext(),"offer Panel is Open",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),OffersActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_seller_privacy:
                        Toast.makeText(getApplicationContext(),"privacy Panel is Open",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_seller_help:
                        Toast.makeText(getApplicationContext(),"help Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_seller_logout:
                        mAuth.signOut();
                        startActivity(new Intent(seller_Activity.this,seller_login.class));
                }

                return true;
            }
        });

        productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProductActivity.class));
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
            }
        });
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OffersActivity.class));
            }
        });
        sellerPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
        //bottomfragment
//        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new seller_HomeFragment()).commit();
//        seller_NavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                switch (menuItem.getItemId()) {
//
//                    case R.id.seller_home:
//                        System.out.println("seller home");
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, homeFragment).commit();
//                        return true;
//                    case R.id.seller_person:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, profileFragment).commit();
//                        return true;
//                }
//                return false;
//            }
//        });

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