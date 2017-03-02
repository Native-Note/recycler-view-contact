package com.nativenote.recyclerview;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by imtiaz on 3/2/17.
 */

public class MainActivity extends AppCompatActivity {
    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;
    private TextView emptyView;
    private static final int PERMISSION_REQUEST_CONTACT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyView = (TextView) findViewById(R.id.empty_view);
        mAdapter = new ContactAdapter(MainActivity.this, contactList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // To show item divider, uncomment below line
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        checkPermission();
    }

    private void prepareData() {
        contactList.clear();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactList.add(new Contact(name, phoneNumber));
        }
        phones.close();

        if (contactList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepareData();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            checkPermission();
                        }
                    });
                    builder.show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_CONTACTS
                    },
                    PERMISSION_REQUEST_CONTACT);
        } else {
            prepareData();
        }
    }
}
