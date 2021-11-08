package com.example.adminpanelapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddLecturer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView addLecturerImage;
    private EditText addLecturerName, addLecturerContact, addLecturerRole;
    private Spinner addLecturerCategory;
    private Button addLecturerButton;
    private FloatingActionButton addLecturerFAB;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private String category;
    private String name, contact, role, downloadUrl = "";

    private ProgressDialog pd;

    private StorageReference storageReference;
    private DatabaseReference reference, dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);

        addLecturerImage = findViewById(R.id.lecturerAddPhoto);
        addLecturerName = findViewById(R.id.lecturerTitleName);
        addLecturerContact = findViewById(R.id.lecturerTitleContact);
        addLecturerRole = findViewById(R.id.lecturerTitleRole);
        addLecturerCategory = findViewById(R.id.addLecturerCategory);
        //Spinner spinner = (Spinner) findViewById(R.id.addLecturerCategory);
        addLecturerButton = findViewById(R.id.addLecturerButton);
       // addLecturerFAB = findViewById(R.id.floatingActionButton);

        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Lecturer");
        storageReference = FirebaseStorage.getInstance().getReference();


        //Spinner spino = findViewById(R.id.addLecturerCategory);
        //spino.setOnItemSelectedListener(this);

        String[] items = new String[]{"Select Category", "Information Technology", "Information Systems", "Networking Systems"};
        addLecturerCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item));

        //spino.setAdapter(ad);

        addLecturerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addLecturerCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addLecturerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }

        });


         addLecturerButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 checkValidation();
             }
         });

    }

    private void checkValidation() {
        name = addLecturerName.getText().toString();
        contact = addLecturerContact.getText().toString();
        role = addLecturerRole.getText().toString();

        if(name.isEmpty()){
            addLecturerName.setError("Please Enter Required Information");
            addLecturerName.requestFocus();
        } else if(contact.isEmpty()) {
            addLecturerContact.setError("Please Enter Required Information");
            addLecturerContact.requestFocus();
        }   if(role.isEmpty()) {
            addLecturerRole.setError("Please Enter Required Information");
            addLecturerRole.requestFocus();
        }  else if(bitmap == null){
            insertData();
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        pd.setMessage("Uploading...");
        pd.show();

        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, boas);
        byte[] finalImage = boas.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Lecturers").child(finalImage + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(AddLecturer.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    insertData();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(AddLecturer.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertData() {
        dbReference = reference.child(category);
        final String uniquekey = dbReference.push().getKey();


        LecturerData lecturerData = new LecturerData(name, downloadUrl, contact, role, uniquekey);

        dbReference.child(uniquekey).setValue(lecturerData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddLecturer.this, "Lecturer Info Submitted/Updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddLecturer.this, "Something Went Wrong, Info Not Submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivity(pickImage);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        AddLecturer.super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addLecturerImage.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), category, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
