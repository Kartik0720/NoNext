package com.project.nonext;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.users.FeedbackUser;
import com.project.nonext.users.HelpCentreActivity;
import com.project.nonext.users.HomeFragment;
import com.project.nonext.users.MenuCategories;
import com.project.nonext.users.MyCartActivity;
import com.project.nonext.users.MyOrdersActivity;
import com.project.nonext.users.MyWishList;
import com.project.nonext.users.OffersActivity;
import com.project.nonext.users.PrivacyPolicyActivity;
import com.project.nonext.users.ProfileFragment;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button signUpAndLogIn;
    BottomNavigationView bottomNavigationView;
    NavigationView menuView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DocumentReference df;
    CardView shoppingcart;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageView cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Loading specific Layout as per operators
        FirebaseUser user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_users_main);
     /*   cart=findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyCartActivity.class));
            }
        });*/
        //object
        HomeFragment homeFragment = new HomeFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        //Toolbar
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setToolbar(toolbar,"Title");
        /*setSupportActionBar(toolbar);*/
        //FindViewById
        menuView=(NavigationView)findViewById(R.id.navigationMenu);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        cart = findViewById(R.id.cart);
        shoppingcart = findViewById(R.id.shopping_card);
       /* drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/

        //Header Drawer
        View hView = menuView.getHeaderView(0);
        TextView hi = hView.findViewById(R.id.hi);
        TextView name_user = hView.findViewById(R.id.nav_name);
        TextView email_user = hView.findViewById(R.id.nav_email);
        signUpAndLogIn = hView.findViewById(R .id.signUpAndLogin);
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
        signUpAndLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));

            }
        });

        //drawer menu Items selection
        menuView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.drawer_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, profileFragment).commit();
                        menuView.getMenu().getItem(1).setChecked(true);
                        menuView.getMenu().getItem(0).setChecked(false);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_category:
                        Toast.makeText(getApplicationContext(), "Category Panel is Open", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MenuCategories.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_offer:
                        Toast.makeText(getApplicationContext(), "offers Panel is Open", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),OffersActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_orders:
                        Toast.makeText(getApplicationContext(), "orders Panel is Open", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MyOrdersActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_cart:
                        Toast.makeText(getApplicationContext(), "cart Panel is Open", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MyCartActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_help:
                        Toast.makeText(getApplicationContext(), "Help Panel is Open", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HelpCentreActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_wishlist:
                        Toast.makeText(MainActivity.this, "Wishlist", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MyWishList.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_feedback:
                        Toast.makeText(getApplicationContext(), "privacy Panel is Open", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), FeedbackUser.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_privacy_policy:
                        startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.drawer_logout:
                        mAuth.signOut();
                        Intent i = (new Intent(MainActivity.this,
                                LoginActivity.class));
                        startActivity(i);
//                        menuView.getMenu().getItem(10).setChecked(true);
//                        menuView.getMenu().getItem(0).setChecked(false);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        menuView.getMenu().getItem(0).setChecked(true);
        //bottomFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, homeFragment).addToBackStack(null).commit();
                        return true;
                    case R.id.person:
                        if(user!=null){
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, profileFragment).addToBackStack(null).commit();
                        }else{
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                        return true;
                }
                return false;
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MyCartActivity.class));
            }
        });
    }

    public void setToolbar(Toolbar toolbar,String title){
        AppCompatActivity actionBar = this;
        actionBar.setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout)actionBar.findViewById(R.id.drawer);
        toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(toolbar != null)
            toolbar.setTitle(title);
    }

    //What happens when You press Back Button
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("NO",null)
                    .show();
        }
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