package com.palash.sampleapp.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.palash.sampleapp.R;
import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELItem;
import com.palash.sampleapp.utilities.LocalSetting;

import java.util.ArrayList;

public class ViewImageActivity extends AppCompatActivity {

    private Context context;
    private LocalSetting localSetting;
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.LoginAdapter loginAdapterDB;
    private DatabaseAdapter.ItemAdapter itemAdapterDB;

    private ELItem elItem;
    private ArrayList<ELItem> elItemArrayList;

    private ImageView display_image;

    private String ItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        init();
        InitView();

        ItemID = getIntent().getStringExtra("ItemID");
        setData();
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

        elItem = new ELItem();
    }

    private void InitView() {
        display_image = (ImageView) findViewById(R.id.display_image);
    }

    private void setData() {
        elItemArrayList = itemAdapterDB.listAllItemID(ItemID);
        if (elItemArrayList != null && elItemArrayList.size() > 0) {
            elItem = elItemArrayList.get(0);

            if (elItem.getAttachmentData() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(elItem.getAttachmentData(), 0, elItem.getAttachmentData().length);
                display_image.setImageBitmap(bmp);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
