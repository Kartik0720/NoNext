package com.project.nonext.admin;

import android.Manifest;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class ApplyOffers extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    TextInputEditText addtitle,exd;
    Button btnaddproduct;
    ImageButton imagebtn;
    ImageView ProdImgView;
    private String[] camerapermissions;
    private String[] storagepermissions;

    Uri SelectedImgUri;
    Task<Uri> prodUrl;
    Bitmap SelectedImgBitmap;
    Bitmap bitmapImage;
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_apply_offers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Apply Offers");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addtitle = findViewById(R.id.EdtAddOfferTitle);
        exd = findViewById(R.id.EdtAddExpires);
        btnaddproduct = findViewById(R.id.adminBtnAddOffer);
        imagebtn = findViewById(R.id.adminImgUplodBtn);
        ProdImgView = findViewById(R.id.adminImgAddProdView);


        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        // Select Option for camera OR gallery

        //PROFILE IMAGE
        storagepermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        camerapermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });

   /*     imagebtn.setOnClickListener(view -> SelectImage());*/

        //    upload data in FireStore

        btnaddproduct.setOnClickListener(v -> {

            if (SelectedImgUri != null) {
//                ImageUpload(SelectedImgUri);
                addProduct(SelectedImgUri);
            }
            else {
                Toast.makeText(this, "There is a Problem", Toast.LENGTH_SHORT).show();
            }

        });

    }
    private void chooseProfilePicture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyOffers.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pick_image,null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        ImageView imageViewAddCamera = dialogView.findViewById(R.id.PickImgCamera);
        ImageView imageViewAddGallery = dialogView.findViewById(R.id.PickImgPhotos);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        imageViewAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermission()) {
                    takePictureFromCamera();
                    alertDialog.cancel();
                }
            }
        });

        imageViewAddGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
                alertDialog.cancel();
            }
        });
    }
    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult(pickPhoto,1);
    }

    private void takePictureFromCamera(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePicture,2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK){
                    assert data != null;
                    SelectedImgUri = Objects.requireNonNull(data).getData();
                    ProdImgView.setImageURI(SelectedImgUri);

                  /*  Uri selectedImageUri = data.getData();
                    ProdImgView.setImageURI(selectedImageUri);*/

                }
                break;
            case 2:
                //camera
                if(resultCode == RESULT_OK){

                    Bundle bundle = data.getExtras();
                    bitmapImage = (Bitmap) bundle.get("data");
                    ProdImgView.setImageBitmap(bitmapImage);
                    SelectedImgUri=getImageUri(this,bitmapImage);




                  /*  assert data != null;
                    Bundle bundle = Objects.requireNonNull(data).getExtras();
                    SelectedImgBitmap = (Bitmap) bundle.get("Data");
                    ProdImgView.setImageBitmap(SelectedImgBitmap);*/
                }
        }
    }
    private boolean checkAndRequestPermission(){
        if(Build.VERSION.SDK_INT <=23){
            int cameraPermission = ActivityCompat.checkSelfPermission(ApplyOffers.this,Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(ApplyOffers.this,new String[]{Manifest.permission.CAMERA},20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePictureFromCamera();
        }
        else
            Toast.makeText(ApplyOffers.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ApplyOffers.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ApplyOffers.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
   /* void SelectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyOffers.this);
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
    */

    private void addProduct(Uri uri) {

        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        SelectedImgUri = uri;
                        Log.v("abc", SelectedImgUri.toString());

                        String offerCode = Objects.requireNonNull(addtitle.getText()).toString();
                        String date = Objects.requireNonNull(exd.getText()).toString();

//        String ProdUrl = SelectedImgUri.toString();

                        if (TextUtils.isEmpty(offerCode)) {
                            addtitle.setError("Enter Offer Code");
                            addtitle.requestFocus();
                        }
                        else if(TextUtils.isEmpty(date)) {
                            exd.setError("Enter Date");
                            exd.requestFocus();
                        }else {
                            FirebaseUser seller = mAuth.getCurrentUser();
                            assert seller != null;
                            Map<String, String> ProInfo = new HashMap<>();

                            ProInfo.put("adminId", seller.getUid());
                            ProInfo.put("offerCode", offerCode);
                            ProInfo.put("date",date);
                            ProInfo.put("url", SelectedImgUri.toString());
                            Log.v("pqr", SelectedImgUri.toString());
                            CollectionReference documentReference = fStore.collection("Offers");

                            documentReference.add(ProInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    documentReference.update("offersId", documentReference.getId());
                                    Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(ApplyOffers.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public Uri getImageUri(Context inContext, Bitmap bitmapImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitmapImage, "Title", null);
        return Uri.parse(path);
    }

}