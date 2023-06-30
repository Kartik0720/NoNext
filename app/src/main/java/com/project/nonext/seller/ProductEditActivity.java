package com.project.nonext.seller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductEditActivity extends AppCompatActivity {

    TextView UpDateProdTitle,UpDateProdDesc,UpDateProdPrice,UpDateProdQuantity,UpDateProdCategory,UpDateProdSubCategory,UpDateProdBrand,UpDateProdHighlights;
    Button btnUpDateProduct,btnDeleteProduct;
    ImageView imgUpDateProdView;
    ImageButton btnimgUpDate;
    String prodId;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    FirebaseFirestore fStore;
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    

    Uri SelectedImgUri;
    Bitmap SelectedImgBitmap;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_edit);

        prodId = getIntent().getStringExtra("uProdId");

        //SellerProdDetPrice.setText(getIntent().getStringExtra("uProdId"));

        UpDateProdTitle = findViewById(R.id.EdtUpDateProdTitle);
        UpDateProdDesc = findViewById(R.id.EdtUpDateProdDesc);
        UpDateProdPrice = findViewById(R.id.EdtUpDateProdPrice);
        UpDateProdQuantity = findViewById(R.id.EdtUpDateProdQuantity);
        UpDateProdCategory = findViewById(R.id.AutoTxtUpDateProdCategory);
        UpDateProdSubCategory = findViewById(R.id.AutoTxtUpDateProdSubCategory);
        UpDateProdBrand = findViewById(R.id.AutoTxtUpDateProdBrand);
        UpDateProdHighlights = findViewById(R.id.EdtUpDateProdHighlights);
        btnimgUpDate = findViewById(R.id.ImgUpDateBtn);
        btnUpDateProduct = findViewById(R.id.BtnUpDateProduct);
        btnDeleteProduct = findViewById(R.id.BtnDeleteProduct);
        imgUpDateProdView = findViewById(R.id.ImgUpDateProdView);

        fStore = FirebaseFirestore.getInstance();

        //Get Data from FireStore
        DocumentReference getProdData = fStore.collection("Products").document(prodId);

        getProdData.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                UpDateProdTitle.setText(Objects.requireNonNull(value).getString("title"));
                UpDateProdDesc.setText(value.getString("description"));
                UpDateProdPrice.setText(value.getString("prices"));
                UpDateProdQuantity.setText(value.getString("quantity"));
                UpDateProdCategory.setText(value.getString("categories"));
                UpDateProdSubCategory.setText(value.getString("sub-categories"));
                UpDateProdBrand.setText(value.getString("brand"));
                UpDateProdHighlights.setText(value.getString("highlights"));
                String uri = value.getString("url");
                Picasso.get().load(uri).error(R.drawable.logo).into(imgUpDateProdView);

            }
        });

//        UpDate Data
        btnUpDateProduct.setOnClickListener(view ->{
            if(SelectedImgUri != null) {
                upDateProduct(SelectedImgUri);
            }else{
                Toast.makeText(this, "There is a Problem", Toast.LENGTH_SHORT).show();
            }
        });

//        Delete Data
        btnDeleteProduct.setOnClickListener(view -> deleteProduct());

//        Update Image
        btnimgUpDate.setOnClickListener(view -> selectImage());

    }

    private void selectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductEditActivity.this);
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

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    private void tackPictureFromGallery() {
        Intent PickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(PickPhoto,2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    assert data != null;
                    Bundle bundle = Objects.requireNonNull(data).getExtras();
                    SelectedImgBitmap = (Bitmap) bundle.get("Data");
                    imgUpDateProdView.setImageBitmap(SelectedImgBitmap);

                }

            case 2:
                if(resultCode==RESULT_OK)
                {
                    assert data != null;
                    SelectedImgUri = Objects.requireNonNull(data).getData();
                    imgUpDateProdView.setImageURI(SelectedImgUri);
                }
                break;

        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private void tackPictureFromCamera(){

        Intent tackPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(tackPicture.resolveActivity(getPackageManager()) != null){
            startActivityForResult(tackPicture,1);
        }

    }

    private boolean checkAndRequestPermissions(){
        if(Build.VERSION.SDK_INT >= 23){
            int cameraPermission = ActivityCompat.checkSelfPermission(ProductEditActivity.this, Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(ProductEditActivity.this,new String[]{Manifest.permission.CAMERA},123);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            Toast.makeText(ProductEditActivity.this,"Permission Not Granted",Toast.LENGTH_LONG).show();

        }else{
            tackPictureFromCamera();

        }
    }


    private void deleteProduct() {
        DocumentReference deleteProductRef = fStore.collection("Products").document(prodId);

        deleteProductRef.update("delete","yes").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProductEditActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductEditActivity.this, "Failure", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void upDateProduct(Uri uri) {

        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        SelectedImgUri = uri;

                        String newTitle = UpDateProdTitle.getText().toString();
                        String newDesc = UpDateProdDesc.getText().toString();
                        String newCategory = UpDateProdCategory.getText().toString();
                        String newSubCategory = UpDateProdSubCategory.getText().toString();
                        String newBrand = UpDateProdBrand.getText().toString();
                        String newQui = UpDateProdQuantity.getText().toString();
                        String newPrice = UpDateProdPrice.getText().toString();
                        String newHighligts = UpDateProdHighlights.getText().toString();

                        DocumentReference upDateProductRef = fStore.collection("Products").document(prodId);

                        Map<String, String> UpDate = new HashMap<>();
                        UpDate.put("title", newTitle);
                        UpDate.put("description", newDesc);
                        UpDate.put("categories", newCategory);
                        UpDate.put("sub-categories", newSubCategory);
                        UpDate.put("brand", newBrand);
                        UpDate.put("quantity", newQui);
                        UpDate.put("prices", newPrice);
                        UpDate.put("url",SelectedImgUri.toString());
                        UpDate.put("highlights", newHighligts);

                        upDateProductRef
                                .set(UpDate, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProductEditActivity.this, "Data UpDate Successfully", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProductEditActivity.this, "UpDating Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductEditActivity.this, "UpDating Failed !!", Toast.LENGTH_SHORT).show();

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