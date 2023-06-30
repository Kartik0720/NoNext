package com.project.nonext.seller;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project.nonext.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SellerOrdersDetail extends AppCompatActivity  {
    RadioButton radioButtonConf,radioButtonPacked,radioButtonShip,radioButtonDelivered;
    RadioGroup radioGroup;
    TextView myOrdersProductDetailTitle,myOrdersProductDetailPrice;
    ImageView myOrdersProductDetailImageView;
    DocumentReference df;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String orderId,imageUri,status;
    EditText abc;

    private String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/ProgrammerWorld.pdf";
    private File file = new File(stringFilePath);

    //    View myOrdersViewOrderPlaced,myOrdersViewOrderConfirmed,myOrdersViewOrderPacked,myOrdersViewOrderShipped,myOrdersViewOrderDelivered;
//    View myOrdersHorizontalDelivered,myOrdersHorizontalShipped,myOrdersHorizontalPacked,myOrdersHorizontalConfirmed;
    TextView myOrdersTextShipAddress,myOrdersTextShipPincode,myOrdersTextBillPincode,myOrdersTextBillAddress,myOrdersTextPayment;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_orders_detail);

//        ActivityCompat.requestPermissions(SellerOrdersDetail.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Order Details");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myOrdersProductDetailTitle = findViewById(R.id.sellerOrdersProductDetailTitle);
        myOrdersProductDetailPrice = findViewById(R.id.sellerOrdersProductDetailPrice);
        myOrdersProductDetailImageView = findViewById(R.id.sellerOrdersProductDetailImageView);
        radioButtonConf = findViewById(R.id.radioButtonConfirmed);
        radioButtonPacked = findViewById(R.id.radioButtonPacked);
        radioButtonShip = findViewById(R.id.radioButtonShipped);
        radioButtonDelivered = findViewById(R.id.radioButtonDelivered);

        radioGroup = findViewById(R.id.radiogroup);
      /*  myOrdersViewOrderPlaced = findViewById(R.id.myOrdersViewOrderPlaced);
        myOrdersViewOrderConfirmed = findViewById(R.id.myOrdersViewOrderConfirmed);
        myOrdersViewOrderPacked = findViewById(R.id.myOrdersViewOrderPacked);
        myOrdersViewOrderShipped = findViewById(R.id.myOrdersViewOrderShipped);
        myOrdersViewOrderDelivered = findViewById(R.id.myOrdersViewOrderDelivered);
*/
        /*myOrdersHorizontalDelivered = findViewById(R.id.myOrdersHorizontalDelivered);
        myOrdersHorizontalShipped = findViewById(R.id.myOrdersHorizontalShipped);
        myOrdersHorizontalPacked = findViewById(R.id.myOrdersHorizontalPacked);
        myOrdersHorizontalConfirmed = findViewById(R.id.myOrdersHorizontalConfirmed);*/
        myOrdersTextShipAddress = findViewById(R.id.sellerOrdersTextShipAddress);
        myOrdersTextShipPincode = findViewById(R.id.sellerOrdersTextShipPincode);
        myOrdersTextBillPincode = findViewById(R.id.sellerOrdersTextBillPincode);
        myOrdersTextBillAddress = findViewById(R.id.sellerOrdersTextBillAddress);
        myOrdersTextPayment = findViewById(R.id.sellerOrdersTextPayment);

       /* radioGroup.setOnCheckedChangeListener(this);*/

        orderId = getIntent().getStringExtra("orderId");
        df = fStore.collection("Orders").document(orderId);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                myOrdersProductDetailTitle.setText(value.getString("title"));
                myOrdersProductDetailPrice.setText(value.getString("price"));
                myOrdersTextShipAddress.setText(value.getString("address"));
                myOrdersTextBillAddress.setText(value.getString("address"));
                myOrdersTextShipPincode.setText(value.getString("pincode"));
                myOrdersTextBillPincode.setText(value.getString("pincode"));
                imageUri = value.getString("url");
                Picasso.get().load(imageUri).error(R.drawable.logo).into(myOrdersProductDetailImageView);
                status = value.getString("orderstatus");
                Log.d("abc",status);

                    if(status.equals("confirmed")){
                   radioButtonConf.setChecked(true);
                }else if(status.equals("packed")){
                    radioButtonPacked.setChecked(true);
                }else if(status.equals("shipped")){
                    radioButtonShip.setChecked(true);
                } else if(status.equals("Delivered")){
                  radioButtonDelivered.setChecked(true);
                }else{

                    }
            }
        });
//        myOrdersViewOrderPlaced.setBackgroundResource(R.drawable.order_status_remaining);
    }

   /* @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.radioButtonConfirmed:


                df = fStore.collection("Orders").document(orderId);
                Map<String,Object> map = new HashMap<>();
                map.put("orderstatus","confirmed");

                df.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Updated the Status");
                        Toast.makeText(getApplicationContext(), "Orders is Confirmed", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,"onFailure",e);
                            }
                        });

                break;
            case R.id.radioButtonPacked:
                df = fStore.collection("Orders").document(orderId);
                Map<String,Object> map1 = new HashMap<>();
                map1.put("orderstatus","packed");

                df.update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Updated the Status");
                        Toast.makeText(getApplicationContext(), "Orders is Packed", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,"onFailure",e);
                            }
                        });


                break;
            case R.id.radioButtonShipped:
                df = fStore.collection("Orders").document(orderId);
                Map<String,Object> map2 = new HashMap<>();
                map2.put("orderstatus","shipped");

                df.update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Updated the Status");
                        Toast.makeText(getApplicationContext(), "Orders is Ship", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,"onFailure",e);
                            }
                        });

                break;
            case R.id.radioButtonDelivere:
                df = fStore.collection("Orders").document(orderId);
                Map<String,Object> map4 = new HashMap<>();
                map4.put("orderstatus","Delivered");

                df.update(map4).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Updated the Status");
                        Toast.makeText(getApplicationContext(), "Orders is Packed", Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,"onFailure",e);
                            }
                        });


                break;
        }
    }*/
  /*  public void createMyPdf(View view){

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = abc .getText().toString();
        int x = 10, y=25;

        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }

        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/myPDFFile.pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
            myOrdersTextShipAddress.setText("ERROR");
        }

        myPdfDocument.close();
    }*/
    /*AMit*/
   /* public void createMyPdf(View view){
        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = abc.getText().toString();
        int x = 10, y=25;

        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }


        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/myPDFFile.pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));

        }
        catch (Exception e){
            e.printStackTrace();
            abc.setText("ERROR");

        }

        myPdfDocument.close();


    }*/
  public void Pdf1(View view) {
    if(radioButtonConf.isChecked()){

        df = fStore.collection("Orders").document(orderId);
        Map<String,Object> map = new HashMap<>();
        map.put("orderstatus","confirmed");

        df.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Updated the Status");
                Toast.makeText(getApplicationContext(), "Orders is Confirmed", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure",e);
                    }
                });

    }
    else if(radioButtonPacked.isChecked()){
        df = fStore.collection("Orders").document(orderId);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("orderstatus","packed");

        df.update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Updated the Status");
                Toast.makeText(getApplicationContext(), "Orders is Packed", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure",e);
                    }
                });

    }
    else if(radioButtonShip.isChecked()){
        df = fStore.collection("Orders").document(orderId);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("orderstatus","shipped");

        df.update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Updated the Status");
                Toast.makeText(getApplicationContext(), "Orders is Ship", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure",e);
                    }
                });
    }
    else if (radioButtonDelivered.isChecked()){
          df = fStore.collection("Orders").document(orderId);
          Map<String,Object> map4 = new HashMap<>();
          map4.put("orderstatus","Delivered");

          df.update(map4).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {
                  Log.d(TAG,"Updated the Status");
                  Toast.makeText(getApplicationContext(), "Orders is Delivered", Toast.LENGTH_LONG).show();
              }
          })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Log.e(TAG,"onFailure",e);
                      }
                  });

      }
    else {
        Toast.makeText(getApplicationContext(),"please select order status ",Toast.LENGTH_LONG).show();
    }

  }

}