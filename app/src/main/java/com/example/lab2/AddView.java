package com.example.lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class AddView extends AppCompatActivity {
    private EditText txtid,txtname, txtphone;
    private Button btnAdd,btnCancel;
    private ArrayList<Contact> data;
    private MainActivity main;
    private  Button btnImage;
    private ImageView imageView;
    private Uri selectedImageUri;

    private String imagePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view);
        txtid = findViewById(R.id.txtId);
        txtname = findViewById(R.id.txtName);
        txtphone = findViewById(R.id.txtPhone);
        btnAdd = findViewById(R.id.btnAdd);

        btnImage = findViewById(R.id.pickimage);
        imageView = findViewById(R.id.imageView);
        btnCancel = findViewById(R.id.btnCancel);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int id_up = bundle.getInt("Id");
            String name_up = bundle.getString("Name");
            String phone_up = bundle.getString("Phone");
            String img_path = bundle.getString("Image");


            txtid.setText(String.valueOf(id_up));
            txtname.setText(name_up);
            txtphone.setText(phone_up);

            Log.d("l", "onCreate: " + img_path);
//            Glide.with(AddView.this)
//                    .load(img_path)
//                    .into(imageView);
//            Bit map không sử dụng đc khi sử dụng nguồn ảnh từ gg photo
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(img_path);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id_new = Integer.parseInt(txtid.getText().toString());
                    String name_new = txtname.getText().toString();
                    String phone_new = txtphone.getText().toString();

                    // Kiểm tra xem các trường có rỗng không
                    if (name_new.isEmpty() || phone_new.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent intent = new Intent();
                        Bundle b = new Bundle();
                        b.putInt("Id", id_new);
                        b.putString("Name", name_new);
                        b.putString("Phone", phone_new);
                        if (selectedImageUri != null) {
                            b.putString("Image", selectedImageUri.toString());
                        }

                        intent.putExtras(b);
                        setResult(150, intent);
                        finish();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập số cho ID", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddView.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
//            Glide.with(this)
//                    .load(selectedImageUri)
//                    .into(imageView);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


}