package umn.ac.id.uastokobako;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TambahBarangDuls extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 1;
    EditText nama, harga, stok;
    Button btntambahstok, btnGaleri;
    ImageView fotoBarang;
    ProgressBar progressBar;
    Uri fotoUri;
    DatabaseReference dataRefSembako;
    StorageReference storageRefSembako;
    StorageTask uploadTask;
    Beras beras;
    long incrId;

    private String getFileExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//3
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            fotoUri = data.getData();//4
            Picasso.get().load(fotoUri).into(fotoBarang);//5
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang_duls);

        //==============DARI SINI===============\\
        nama = findViewById(R.id.editnama);
        harga = findViewById(R.id.editharga);
        stok = findViewById(R.id.editstok);
        btnGaleri = findViewById(R.id.buttonBukaGaleri);
        btntambahstok = findViewById(R.id.buttontambahkestok);
        fotoBarang = findViewById(R.id.imageUpload);
        progressBar= findViewById(R.id.progress_bar);
        beras = new Beras();
        dataRefSembako = FirebaseDatabase.getInstance().getReference().child("Beras");
        storageRefSembako = FirebaseStorage.getInstance().getReference().child("Beras");
        dataRefSembako.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    incrId=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btntambahstok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(TambahBarangDuls.this, "SABAR Cuk, masi uplot", Toast.LENGTH_LONG).show();
                }
                else {
                    if(fotoUri != null){
                        final StorageReference fileReference = storageRefSembako.child(System.currentTimeMillis() + "." + getFileExtention(fotoUri));

                        uploadTask = fileReference.putFile(fotoUri)//6
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setProgress(0);
                                            }
                                        }, 500);

                                        //update dari stack overflow
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                final String downloadUrl = uri.toString();
                                                int hargaM = Integer.parseInt(harga.getText().toString().trim());
                                                int stokM = Integer.parseInt(stok.getText().toString().trim());

                                                beras.setNamaBarang(nama.getText().toString().trim());
                                                beras.setHarga(hargaM);
                                                beras.setStok(stokM);
                                                //beras.setGambarUrl(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                                beras.setGambarUrl(downloadUrl);//7
                                                dataRefSembako.child(String.valueOf(incrId+1)).setValue(beras);//8

                                                if(uploadTask.isComplete()){
                                                    Intent intent2 = new Intent(TambahBarangDuls.this, MainActivity.class);
                                                    startActivity(intent2);
                                                    Toast.makeText(TambahBarangDuls.this, "Data Masuk Cuk", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        //sampai sini


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TambahBarangDuls.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                        progressBar.setProgress((int) progress);
                                    }
                                });

//                    int hargaM = Integer.parseInt(harga.getText().toString().trim());
//                    int stokM = Integer.parseInt(stok.getText().toString().trim());
//
//                    beras.setNamaBarang(nama.getText().toString().trim());
//                    beras.setHarga(hargaM);
//                    beras.setStok(stokM);
//                    beras.setGambarUrl();
//                    dataRefSembako.child(String.valueOf(incrId+1)).setValue(beras);
//
//                    Toast.makeText(TambahBarangDuls.this, "Data Masuk Cuk", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(TambahBarangDuls.this,"Upload foto dulu", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnGaleri.setOnClickListener(new View.OnClickListener() {//1
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);//2
            }
        });





        //==============SAMPE SINI===============\\
    }
}
