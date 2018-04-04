package com.palash.sampleapp.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.sampleapp.R;
import com.palash.sampleapp.adapter.CatalogListAdapter;
import com.palash.sampleapp.api.JsonObjectMapper;
import com.palash.sampleapp.api.WebServiceConsumer;
import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELCatalog;
import com.palash.sampleapp.entiry.ELItem;
import com.palash.sampleapp.entiry.ELLogin;
import com.palash.sampleapp.utilities.Constants;
import com.palash.sampleapp.utilities.LocalSetting;
import com.palash.sampleapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.LoginAdapter loginAdapterDB;
    private DatabaseAdapter.ItemAdapter itemAdapterDB;

    private ELLogin elLogin;
    private ELItem elItem;
    private ArrayList<ELLogin> elArrayLogin;
    private ArrayList<ELCatalog> elArrayListCatalog;

    private CatalogListAdapter catalogListAdapter;

    private MaterialSpinner add_item_catalog_name_spinner;
    private EditText add_item_pageno_edt;
    private EditText item_remark_edt;
    private TextView selected_file_name;
    private TextView add_file_image_btn;
    private TextView view_file_image_btn;
    private TextView delete_file_image_btn;

    private String SelectedCatalogID = "0";
    private String SelectedCatalogName = "";
    private String OrderNumber = "";
    private String AttachmentType = "0";
    private String CurrentTimeStamp = "0";
    private String AttachmentName = null;
    private byte[] AttachmentData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        init();
        InitView();
        SetAdapter();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        localSetting = new LocalSetting();
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        loginAdapterDB = databaseAdapter.new LoginAdapter();
        itemAdapterDB = databaseAdapter.new ItemAdapter();
        elLogin = new ELLogin();
        elItem = new ELItem();

        elArrayLogin = new ArrayList<>();
        elArrayLogin = loginAdapterDB.listAll();
        if (elArrayLogin != null && elArrayLogin.size() > 0) {
            elLogin = elArrayLogin.get(0);
        }
        OrderNumber = getIntent().getStringExtra("OrderNumber");
        CurrentTimeStamp = localSetting.currentTimeStamp();
    }

    private void InitView() {
        add_item_catalog_name_spinner = (MaterialSpinner) findViewById(R.id.add_item_catalog_name_spinner);
        add_item_pageno_edt = (EditText) findViewById(R.id.add_item_pageno_edt);
        item_remark_edt = (EditText) findViewById(R.id.item_remark_edt);
        add_file_image_btn = (TextView) findViewById(R.id.add_file_image_btn);
        view_file_image_btn = (TextView) findViewById(R.id.view_file_image_btn);
        delete_file_image_btn = (TextView) findViewById(R.id.delete_file_image_btn);
        selected_file_name = (TextView) findViewById(R.id.selected_file_name);

        add_file_image_btn.setOnClickListener(this);
        view_file_image_btn.setOnClickListener(this);
        delete_file_image_btn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_toolbar_save).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_toolbar_save:
                if (validate()) {
                    bindData();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validate() {
        if (SelectedCatalogID.equals("0")) {
            Toast.makeText(context, "Please add catalog", Toast.LENGTH_SHORT).show();
            return false;
        } else if (add_item_pageno_edt.getText().toString().equals("") && add_item_pageno_edt.getText().toString().length() == 0) {
            Toast.makeText(context, "Please add page number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (item_remark_edt.getText().toString().equals("") && item_remark_edt.getText().toString().length() == 0) {
            Toast.makeText(context, "Please add remark", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selected_file_name.getText().toString().equals("") && selected_file_name.getText().toString().length() == 0) {
            Toast.makeText(context, "Please add attachment", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void SetAdapter() {
        elArrayListCatalog = localSetting.returnCatalog();
        if (elArrayListCatalog != null && elArrayListCatalog.size() > 0) {
            catalogListAdapter = new CatalogListAdapter(context, elArrayListCatalog);
            add_item_catalog_name_spinner.setAdapter(catalogListAdapter);
            catalogListAdapter.notifyDataSetChanged();

            add_item_catalog_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int sCatalogPos = add_item_catalog_name_spinner.getSelectedItemPosition();
                    if (sCatalogPos == 0) {
                        SelectedCatalogID = "0";
                        SelectedCatalogName = "";
                    } else if (sCatalogPos > 0) {
                        sCatalogPos = sCatalogPos - 1;
                        SelectedCatalogID = elArrayListCatalog.get(sCatalogPos).getID();
                        SelectedCatalogName = elArrayListCatalog.get(sCatalogPos).getDescription();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void bindData() {
        elItem = new ELItem();
        elItem.setOrderNumber(OrderNumber);
        elItem.setItemID(CurrentTimeStamp);
        elItem.setCatalogID(SelectedCatalogID);
        elItem.setCatalogName(SelectedCatalogName);
        elItem.setPageNo(add_item_pageno_edt.getText().toString());
        elItem.setAttachmentName(selected_file_name.getText().toString());
        elItem.setAttachmentFullPath(AttachmentName);
        elItem.setAttachmentData(AttachmentData);
        elItem.setItemIsTempAdded(AttachmentType);
        elItem.setItemRemark(item_remark_edt.getText().toString());
        elItem.setItemAddedDate(localSetting.returnCurrentDate());
        elItem.setItemIsTempAdded("1");

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Do you really want to add this item!")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        new AddItemTask().execute();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_file_image_btn:
                selectImage();
                break;
            case R.id.view_file_image_btn:
                viewAttachment();
                break;
            case R.id.delete_file_image_btn:
                AttachmentType = "0";
                AttachmentName = null;
                AttachmentData = null;
                selected_file_name.setText(null);
                break;
        }
    }

    private void viewAttachment() {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(AttachmentName).substring(1));
        newIntent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),AttachmentName)), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    public class AddItemTask extends AsyncTask<Void, Void, String> {
        private JsonObjectMapper objectMapper;
        private WebServiceConsumer serviceConsumer;
        private Response response;
        private int responseCode = 0;
        private String jSonData;
        private TransparentProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = localSetting.showDialog(context);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = "";
            try {
                objectMapper = new JsonObjectMapper();
                serviceConsumer = new WebServiceConsumer(context, null, null);
                jSonData = objectMapper.unMap(elItem);
                response = serviceConsumer.POST(Constants.ADD_ITEM_URL, jSonData);

                if (response != null) {
                    responseString = response.body().string();
                    responseCode = response.code();
                    Log.d(Constants.TAG, "Response code:" + responseCode);
                    Log.d(Constants.TAG, "Response string:" + responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            localSetting.hideDialog(progressDialog);
            itemAdapterDB.create(elItem);
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(context.getResources().getString(R.string.app_name))
                    .setContentText("Item Added Successfully")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();

            super.onPostExecute(result);
        }
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    boolean isMarsh = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
                    if (isMarsh) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 2);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
                    if (isKitKat) {
                        Intent intent = new Intent();
                        intent.setType("*/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, 1);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    Uri selectedAttachment = data.getData();
                    String filePath = localSetting.fileNameFromURI(selectedAttachment, context);

                    if (localSetting.checkExtension(filePath)) {    // is image file selected
                        try {
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(selectedAttachment, filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            AttachmentName = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                            AttachmentData = localSetting.getBytes(selectedAttachment, context);
                            cursor.close();

                            selected_file_name.setText(CurrentTimeStamp + "." + localSetting.returnExtension(filePath));
                            AttachmentType = "1";  // for gallary image
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something wrong..", Toast.LENGTH_SHORT).show();
                        }
                    } else if (filePath.contains(".pdf")) {
                        try {
                            AttachmentName = localSetting.fileNameFromURI(selectedAttachment, context);
                            AttachmentData = localSetting.getBytes(selectedAttachment, context);

                            selected_file_name.setText(CurrentTimeStamp + "." + "pdf");
                            AttachmentType = "2";  // for gallary document
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, "You are selected wrong file format", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    byte[] byteArray = bytes.toByteArray();
                    try {
                        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
                        Uri selectedImage1 = Uri.parse(path);
                        AttachmentName = localSetting.fileNameFromURI(selectedImage1, context);
                        AttachmentData = localSetting.getBytes(selectedImage1, context);

                        selected_file_name.setText(CurrentTimeStamp + "." + "jpg");
                        AttachmentType = "3";  // for camera image
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Something wrong..", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
