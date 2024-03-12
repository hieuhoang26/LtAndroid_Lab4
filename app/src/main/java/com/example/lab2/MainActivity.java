package com.example.lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Contact> listContact;
    MyAdapter contactAdapter;
    ListView listViewContact;

    Button btnXoa, btnThem, btnSua;

    private MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        listContact = new ArrayList<>();
//        listContact.add(new Contact(1, "Mot", "34567", "", false));
//        listContact.add(new Contact(2, "Hai", "0987", "", false));
//        listContact.add(new Contact(3, "Ba", "56789", "", true));

        db = new MyDB(this,"ContactDB", null,1);
        db.addcontact(new Contact(1, "Mot", "34567", "", false));
        db.addcontact(new Contact(2, "Hai", "0987", "", false));
        db.addcontact(new Contact(3, "Ba", "56789", "", true));
//        db.updateContact(1,new Contact(1, "Mot", "34567", "", false));
//        db.updateContact(2,new Contact(2, "Hai", "0987", "", false));
//        db.updateContact(3,new Contact(3, "Ba", "56789", "", true));

        listContact = db.getAllContact();


        contactAdapter = new MyAdapter(listContact, this);
        listViewContact = findViewById(R.id.listview);

        listViewContact.setAdapter(contactAdapter);
        registerForContextMenu(listViewContact);

        listViewContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        //Xoá khi tích vào checkbox
        btnXoa = findViewById(R.id.btnXoa);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận xoá");
                builder.setMessage("Bạn có chắc muốn xoá không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < listContact.size(); ) {
                            if (listContact.get(i).isStatus() == true) {
                                listContact.remove(i);
                            } else i++;
                        }
                        contactAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Không", null);
                builder.create().show();
            }
        });

        //Thêm mới
        btnThem = findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddView.class);
                startActivityForResult(intent, 100);
            }
        });
        //Edit
        btnSua = findViewById(R.id.btnSua);
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact selectedContact = null;
                for (Contact contact : listContact) {
                    if (contact.isStatus()) {
                        selectedContact = contact;
                        break;
                    }
                }

                if (selectedContact != null) {
                    // Send intent to AddView for editing
                    Intent intent = new Intent(MainActivity.this, AddView.class);
                    intent.putExtra("Id", selectedContact.getId());
                    intent.putExtra("Name", selectedContact.getName());
                    intent.putExtra("Phone", selectedContact.getPhoneNumber());
                    intent.putExtra("Image", selectedContact.getImage());
                    startActivityForResult(intent, 200); // Use a different requestCode for editing
                } else {
                    // Display a message if no contact with status = true is found
                    Toast.makeText(MainActivity.this, "No contact selected for editing", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle b = data.getExtras();
        int id = b.getInt("Id");
        String name = b.getString("Name");
        String phone = b.getString("Phone");
        String image = b.getString("Image");
        Contact newcontact = new Contact(id, name, phone, image, false);
        if (requestCode == 100 && resultCode == 150) {
            //truong hop them
            listContact.add(newcontact);
            db.addcontact(newcontact);
            contactAdapter.notifyDataSetChanged();
        } else if (requestCode == 200) {
            int index = getIndexById(id);
            if (index != -1) {
                listContact.set(index, newcontact);
                contactAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Contact selectedContact = listContact.get(position);

        if (item.getItemId() == R.id.menu_call) {
            // Handle the "Call" option
            String phoneNumber = selectedContact.getPhoneNumber();
            if (!phoneNumber.isEmpty()) {
                // Use an Intent to initiate a phone call
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.web) {
            String websiteUrl = "https://www.google.com";
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
            startActivity(webIntent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }



    private int getIndexById(int id) {
        for (int i = 0; i < listContact.size(); i++) {
            if (listContact.get(i).getId() == id) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
}