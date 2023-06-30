package com.project.nonext.seller;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.nonext.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class seller_add_product extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    TextInputEditText addtitle,adddescription,addquantity,addprice,addhighlights;
    Button btnaddproduct;
    ImageButton imagebtn;
    AutoCompleteTextView addcategoryTxt;
    AutoCompleteTextView addtypeTxt;
    AutoCompleteTextView addbrandTxt;
    ImageView ProdImgView;

    Uri SelectedImgUri;
    Task<Uri> prodUrl;
    Bitmap SelectedImgBitmap;
    Bitmap bitmapImage;
    ProgressBar progressBar;

    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    final String[] items =  {"Electronics","Home Appliances","Fashion","Mobiles"};

    final String[] SubElectro = {"Laptop","Camera","Monitor","Smartwatch","Charger","Keyboards & Mouse"};
    final String[] BrandLap = {"HP","Lenovo","DELL","Acer","MI","MicroSoft","LG"};
    final String[] BrandCamera = {"Canon","GoPro","ABB","AKSHAR","ATIMUNA","Apeman"};
    final String[] BrandMonitor = {"DELL","acer","LG","HP","SAMSUNG","Lenovo","PHILIPS","EBRONICS","Gigabyte"};
    final String[] BrandSWatch = {"boAt","APPLE","Noise","realme","Fire-Boltt","SAMSUNG","GIONEE","SMART 4G","Gedlly"};
    final String[] BrandCharger ={"Zaap","Portronics","Mi","Oraimo Firefly","Amx"};
    final String[] BrandKeyMous = {"Logitech","Dell","HP","Rii Mini","Lenovo"};

    final String[] SubHome = {"Washing Machines","Refrigerators","Cooling Appliances","Kitchen Appliances"};
    final String[] BrandWashing = {"SAMSUNG","LG","Whirlpool","Panasonic","Croma","Sansui"};
    final String[] BrandRefri = {"SAMSUNG","Whirlpool","LG","Haier","Panasonic","Croma"};
    final String[] BrandCool ={"SAMSUNG","LG","Panasonic","Croma"};
    final String[] BrandKitchen ={"Prestige","Butterfly","Balzano","Pigeon","LG","Philips","Bajaj","	Samsung ","IFB","Whirlpool"};

    final String[] SubFashion = {"Men's Fashion","Women's Fashion","Baby & Kids"};
    final String[] BrandMen = {"Levi’s","Pepe Jeans","Tommy Hilfiger","Lee"," Wrangler"," Adidas","Allen Solly","Raymond","Turtle","Louis Philippe"};
    final String[] BrandWomen = {"BIBA","FABINDIA","ALLEN SOLLY","LEVIS","AND","AURELIA"," WESTSIDE","W for Women","H&M","ZARA"};
    final String[] BrandBaKi = {"Gini & Jony","Lilliput","Cucumber","Nino Bambino","Max","GAP","Mothercare","Little Kangaroos"};

    final String[] SubMobiles = {"Apple","Samsung","Mi","Infinix","Iqoo","Oneplus","Realme","Poco"};
    final String[] BrandMob = {"Apple","Samsung","Mi","Infinix","Iqoo","Oneplus","Realme","Poco"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add a Product");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnaddproduct = findViewById(R.id.BtnAddProduct);
        addtitle = findViewById(R.id.EdtAddProdTitle);
        adddescription = findViewById(R.id.EdtAddProdDesc);
        addcategoryTxt = findViewById(R.id.AutoTxtProdCategory);
        addtypeTxt = findViewById(R.id.AutoTxtProdSubCategory);
        addquantity = findViewById(R.id.EdtAddProdQuantity);
        addprice = findViewById(R.id.EdtAddProdPrice);
        imagebtn = findViewById(R.id.ImgUplodBtn);
        ProdImgView = findViewById(R.id.ImgAddProdView);
        addbrandTxt = findViewById(R.id.AutoTxtProdBrand);
        addhighlights = findViewById(R.id.EdtAddProdHighlights);
        progressBar = findViewById(R.id.addProductProgressBar);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        //addproduct dropdown menu
        ArrayAdapter<String> adaptermain = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,items);

//        1.Electronics
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,SubElectro);
        ArrayAdapter<String> adapter1_1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandLap);
        ArrayAdapter<String> adapter1_2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandCamera);
        ArrayAdapter<String> adapter1_3 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandMonitor);
        ArrayAdapter<String> adapter1_4 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandSWatch);
        ArrayAdapter<String> adapter1_5 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandCharger);
        ArrayAdapter<String> adapter1_6 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandKeyMous);

//        2.Home Appliances
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,SubHome);
        ArrayAdapter<String> adapter2_1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandWashing);
        ArrayAdapter<String> adapter2_2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandRefri);
        ArrayAdapter<String> adapter2_3 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandCool);
        ArrayAdapter<String> adapter2_4 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandKitchen);


//        3.Fashion
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,SubFashion);
        ArrayAdapter<String> adapter3_1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandMen);
        ArrayAdapter<String> adapter3_2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandWomen);
        ArrayAdapter<String> adapter3_3 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandBaKi);

//        4.Mobiles
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,SubMobiles);
        ArrayAdapter<String> adapter4_1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandMob);
        ArrayAdapter<String> adapter4_2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,BrandMob);


        addcategoryTxt.setAdapter(adaptermain);
        addcategoryTxt.setOnItemClickListener((parent, view, position, id) -> {
            switch (position){
                case 0:
                    addtypeTxt.setAdapter(adapter1);
                    addtypeTxt.setOnClickListener(view1 -> addtypeTxt.showDropDown());
                    addtypeTxt.setOnItemClickListener((adapterView, view101, i, l) -> {
                        switch (i){
                            case 0:
                                addbrandTxt.setAdapter(adapter1_1);
                                addbrandTxt.setOnClickListener(view14 -> addbrandTxt.showDropDown());
                                break;

                            case 1:
                                addbrandTxt.setAdapter(adapter1_2);
                                addbrandTxt.setOnClickListener(view15 -> addbrandTxt.showDropDown());
                                break;

                            case 2:
                                addbrandTxt.setAdapter(adapter1_3);
                                addbrandTxt.setOnClickListener(view16 -> addbrandTxt.showDropDown());
                                break;

                            case 3:
                                addbrandTxt.setAdapter(adapter1_4);
                                addbrandTxt.setOnClickListener(view17 -> addbrandTxt.showDropDown());
                                break;

                            case 4:
                                addbrandTxt.setAdapter(adapter1_5);
                                addbrandTxt.setOnClickListener(view18 -> addbrandTxt.showDropDown());
                                break;

                            case 5:
                                addbrandTxt.setAdapter(adapter1_6);
                                addbrandTxt.setOnClickListener(view19 -> addbrandTxt.showDropDown());
                                break;

                        }

                    });
                    break;
                case 1:
                    addtypeTxt.setAdapter(adapter2);
                    addtypeTxt.setOnClickListener(view118 -> addtypeTxt.showDropDown());
                    addtypeTxt.setOnItemClickListener((adapterView, view202, i, l) -> {
                        switch (i){

                            case 0:
                                addbrandTxt.setAdapter(adapter2_1);
                                addbrandTxt.setOnClickListener(view110 -> addbrandTxt.showDropDown());
                                break;

                            case 1:
                                addbrandTxt.setAdapter(adapter2_2);
                                addbrandTxt.setOnClickListener(view111 -> addbrandTxt.showDropDown());
                                break;

                            case 2:
                                addbrandTxt.setAdapter(adapter2_3);
                                addbrandTxt.setOnClickListener(view112 -> addbrandTxt.showDropDown());
                                break;

                            case 3:
                                addbrandTxt.setAdapter(adapter2_4);
                                addbrandTxt.setOnClickListener(view113 -> addbrandTxt.showDropDown());
                                break;

                        }
                    });
                    break;

                case 2:
                    addtypeTxt.setAdapter(adapter3);
                    addtypeTxt.setOnClickListener(view12 -> addtypeTxt.showDropDown());
                    addtypeTxt.setOnItemClickListener((adapterView, view203, i, l) -> {
                        switch (i){

                            case 0:
                                addbrandTxt.setAdapter(adapter3_1);
                                addbrandTxt.setOnClickListener(view114 -> addbrandTxt.showDropDown());
                                break;

                            case 1:
                                addbrandTxt.setAdapter(adapter3_2);
                                addbrandTxt.setOnClickListener(view115 -> addbrandTxt.showDropDown());
                                break;

                            case 2:
                                addbrandTxt.setAdapter(adapter3_3);
                                addbrandTxt.setOnClickListener(view116 -> addbrandTxt.showDropDown());
                                break;
                        }
                    });
                    break;

                case 3:
                    addtypeTxt.setAdapter(adapter4);
                    addtypeTxt.setOnClickListener(view13 -> addtypeTxt.showDropDown());
                    addtypeTxt.setOnItemClickListener((adapterView, view204, i, l) -> {
                        switch (i){

                            case 0:
                                addbrandTxt.setAdapter(adapter4_1);
                                addbrandTxt.setOnClickListener(view117 -> addbrandTxt.showDropDown());
                                break;

                            case 1:
                                addbrandTxt.setAdapter(adapter4_2);
                                addbrandTxt.setOnClickListener(view117 -> addbrandTxt.showDropDown());
                                break;

                        }
                    });
                    break;

            }

        });


        // Select Option for camera OR gallery

        imagebtn.setOnClickListener(view -> SelectImage());

        //    upload data in FireStore

        btnaddproduct.setOnClickListener(v -> {
            if(SelectedImgUri != null) {
//                ImageUpload(SelectedImgUri);
                progressBar.setVisibility(View.VISIBLE);
                addProduct(SelectedImgUri);
            }else{
                Toast.makeText(this, "There is a Problem", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void ImageUpload(Uri uri) {
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        SelectedImgUri = uri;
                        Log.v("abc",SelectedImgUri.toString());
                        Toast.makeText(seller_add_product.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(seller_add_product.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
       /* String title = Objects.requireNonNull(addtitle.getText()).toString();

        storageRef.child(title).putFile(SelectedImgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        prodUrl = taskSnapshot.getStorage().getDownloadUrl();
                        //prodUrl =  sRef.getDownloadUrl();

                    }
                });*/
//        storageRef.child(addtitle.toString()).putFile(SelectedImgUri);
//        prodUrl = storageRef.child(addtitle.toString()).getDownloadUrl();
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    void SelectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(seller_add_product.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pick_image,null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        ImageView PickCamera = dialogView.findViewById(R.id.PickImgCamera);
        ImageView PickPhotos = dialogView.findViewById(R.id.PickImgPhotos);

        final AlertDialog alertDialogSelectImg = builder.create();
        alertDialogSelectImg.show();

        PickCamera.setOnClickListener(view -> {
            if(checkAndRequestPermissions()) {
                tackPictureFromCamera();
                alertDialogSelectImg.cancel();
            }

        });
        PickPhotos.setOnClickListener(view -> {
            tackPictureFromGallery();
            alertDialogSelectImg.cancel();
        });
    }

    private void tackPictureFromGallery() {
        Intent PickPhoto = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(PickPhoto,1);

    }
    @SuppressLint("QueryPermissionsNeeded")
    private void tackPictureFromCamera(){

        Intent tackPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(tackPicture.resolveActivity(getPackageManager()) != null){
            startActivityForResult(tackPicture,2);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    SelectedImgUri = Objects.requireNonNull(data).getData();
                    ProdImgView.setImageURI(SelectedImgUri);

                  /*  Uri selectedImageUri = data.getData();
                    ProdImgView.setImageURI(selectedImageUri);*/

                }
                break;
            case 2:
                //camera
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    bitmapImage = (Bitmap) bundle.get("data");
                    ProdImgView.setImageBitmap(bitmapImage);
                    SelectedImgUri = getImageUri(this, bitmapImage);


               /* if(resultCode == RESULT_OK){
                    assert data != null;
                    Bundle bundle = Objects.requireNonNull(data).getExtras();
                    SelectedImgBitmap = (Bitmap) bundle.get("Data");
                    ProdImgView.setImageBitmap(SelectedImgBitmap);
                  *//*  assert data != null;
                    SelectedImgUri = Objects.requireNonNull(data).getData();
                    ProdImgView.setImageURI(SelectedImgUri);
*//*

                }

            case 2:
                if(resultCode==RESULT_OK)
                {
                    Bundle bundle = data.getExtras();
                    bitmapImage = (Bitmap) bundle.get("data");
                    ProdImgView.setImageBitmap(bitmapImage);
                    SelectedImgUri=getImageUri(this,bitmapImage);
                }
                break;
*/
                }
        }

    }



    private boolean checkAndRequestPermissions(){
        if(Build.VERSION.SDK_INT <= 23){
            int cameraPermission = ActivityCompat.checkSelfPermission(seller_add_product.this,Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(seller_add_product.this,new String[]{Manifest.permission.CAMERA},200);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(seller_add_product.this,"Permission Not Granted",Toast.LENGTH_LONG).show();

        }else{
            tackPictureFromCamera();

        }
    }

    private void addProduct(Uri uri) {

        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        SelectedImgUri = uri;
                        Log.v("abc",SelectedImgUri.toString());

                        String title = Objects.requireNonNull(addtitle.getText()).toString();
                        String desc = Objects.requireNonNull(adddescription.getText()).toString();
                        String category = Objects.requireNonNull(addcategoryTxt.getText()).toString();
                        String type = Objects.requireNonNull(addtypeTxt.getText()).toString();
                        String brand = Objects.requireNonNull(addbrandTxt.getText()).toString();
                        String qui = Objects.requireNonNull(addquantity.getText()).toString();
                        String price = Objects.requireNonNull(addprice.getText()).toString();
                        String highligts = Objects.requireNonNull(addhighlights.getText()).toString();
//        String ProdUrl = SelectedImgUri.toString();

                        if (TextUtils.isEmpty(title)){
                            addtitle.setError("Enter Title");
                            addtitle.requestFocus();
                        }else if(TextUtils.isEmpty(desc)){
                            adddescription.setError("Enter Description");
                            adddescription.requestFocus();
                        }else if(TextUtils.isEmpty(category)){
                            addcategoryTxt.setError("Select Category");
                            addcategoryTxt.requestFocus();
                        }else if(TextUtils.isEmpty(type)) {
                            addtypeTxt.setError("Select Sub-Category");
                            addtypeTxt.requestFocus();
                        }else if(TextUtils.isEmpty(brand)){
                            addbrandTxt.setError("Select brand");
                            addbrandTxt.requestFocus();
                        }else if(TextUtils.isEmpty(qui)){
                            addquantity.setError("Enter Quantity");
                            addquantity.requestFocus();
                        }else if(TextUtils.isEmpty(price)){
                            addprice.setError("Enter Price");
                            addprice.requestFocus();
                        }else{
                            FirebaseUser seller = mAuth.getCurrentUser();
                            assert seller != null;
                            Map<String,String> ProInfo = new HashMap<>();

                            ProInfo.put("sellerId",seller.getUid());
                            ProInfo.put("title",title);
                            ProInfo.put("description",desc);
                            ProInfo.put("categories",category);
                            ProInfo.put("sub-categories",type);
                            ProInfo.put("brand",brand);
                            ProInfo.put("quantity",qui);
                            ProInfo.put("prices",price);
                            ProInfo.put("url",SelectedImgUri.toString());
                            ProInfo.put("highlights",highligts);
                            ProInfo.put("delete","no");

                            Log.v("pqr",SelectedImgUri.toString());
                            CollectionReference documentReference = fStore.collection("Products");

                            documentReference.add(ProInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    documentReference.update("prodId",documentReference.getId());
                                    Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(seller_add_product.this,ProductActivity.class);
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(i);
                                    overridePendingTransition(0, 0);
                                }
                            });
//                            documentReference.add(ProInfo).addOnCompleteListener(task ->
//                                    Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_SHORT).show());
                        }
//                        Toast.makeText(seller_add_product.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(seller_add_product.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });

        /*String title = Objects.requireNonNull(addtitle.getText()).toString();
        String desc = Objects.requireNonNull(adddescription.getText()).toString();
        String category = Objects.requireNonNull(addcategoryTxt.getText()).toString();
        String type = Objects.requireNonNull(addtypeTxt.getText()).toString();
        String brand = Objects.requireNonNull(addbrandTxt.getText()).toString();
        String qui = Objects.requireNonNull(addquantity.getText()).toString();
        String price = Objects.requireNonNull(addprice.getText()).toString();
//        String ProdUrl = SelectedImgUri.toString();

        if (TextUtils.isEmpty(title)){
            addtitle.setError("Enter Title");
            addtitle.requestFocus();
        }else if(TextUtils.isEmpty(desc)){
            adddescription.setError("Enter Description");
            adddescription.requestFocus();
        }else if(TextUtils.isEmpty(category)){
            addcategoryTxt.setError("Select Category");
            addcategoryTxt.requestFocus();
        }else if(TextUtils.isEmpty(type)) {
            addtypeTxt.setError("Select Sub-Category");
            addtypeTxt.requestFocus();
        }else if(TextUtils.isEmpty(brand)){
            addbrandTxt.setError("Select brand");
            addbrandTxt.requestFocus();
        }else if(TextUtils.isEmpty(qui)){
            addquantity.setError("Enter Quantity");
            addquantity.requestFocus();
        }else if(TextUtils.isEmpty(price)){
            addprice.setError("Enter Price");
            addprice.requestFocus();
        }else{
            FirebaseUser seller = mAuth.getCurrentUser();
            assert seller != null;
            Map<String,String> ProInfo = new HashMap<>();

            ProInfo.put("sellerId",seller.getUid());
            ProInfo.put("title",title);
            ProInfo.put("description",desc);
            ProInfo.put("categories",category);
            ProInfo.put("sub-categories",type);
            ProInfo.put("brand",brand);
            ProInfo.put("quantity",qui);
            ProInfo.put("prices",price);
            ProInfo.put("url",SelectedImgUri.toString());
            Log.v("pqr",SelectedImgUri.toString());
            CollectionReference documentReference = fStore.collection("Products");

            documentReference.add(ProInfo).addOnCompleteListener(task ->
                    Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_SHORT).show());
        }*/

    }
    public Uri getImageUri(Context inContext, Bitmap bitmapImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitmapImage, "Title", null);
        return Uri.parse(path);
    }
}