package com.palash.sampleapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palash.sampleapp.R;
import com.palash.sampleapp.activity.AddItemActivity;
import com.palash.sampleapp.adapter.ItemListAdapter;
import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELItem;
import com.palash.sampleapp.entiry.ELLogin;
import com.palash.sampleapp.entiry.ELOrder;
import com.palash.sampleapp.utilities.LocalSetting;

import java.util.ArrayList;

public class AddOrderFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;

    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.LoginAdapter loginAdapterDB;
    private DatabaseAdapter.OrderAdapter orderAdapterDB;
    private DatabaseAdapter.ItemAdapter itemAdapterDB;

    private ELLogin elLogin;
    private ELOrder elOrder;
    private ArrayList<ELLogin> elArrayLogin;
    private ArrayList<ELOrder> elOrderArrayList;
    private ArrayList<ELItem> elItemArrayList;

    private TextView button_add_new_item;
    private ListView item_list;
    private LinearLayout item_no_data;

    private ItemListAdapter itemListAdapter;

    public AddOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_order, container, false);
        initialize();
        InitView(view);
        return view;
    }

    private void InitView(View view) {
        button_add_new_item = (TextView) view.findViewById(R.id.button_add_new_item);
        item_list = (ListView) view.findViewById(R.id.item_list);
        item_no_data = (LinearLayout) view.findViewById(R.id.item_no_data);

        item_list.setVisibility(View.GONE);
        item_no_data.setVisibility(View.VISIBLE);
        button_add_new_item.setOnClickListener(this);
    }

    private void initialize() {
        context = getActivity();
        localSetting = new LocalSetting();

        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        loginAdapterDB = databaseAdapter.new LoginAdapter();
        orderAdapterDB = databaseAdapter.new OrderAdapter();
        itemAdapterDB = databaseAdapter.new ItemAdapter();
        elLogin = new ELLogin();

        elArrayLogin = new ArrayList<ELLogin>();
        elArrayLogin = loginAdapterDB.listAll();
        if (elArrayLogin != null && elArrayLogin.size() > 0) {
            elLogin = elArrayLogin.get(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setOrderData();
    }

    private void setOrderData() {
        elItemArrayList = itemAdapterDB.listAll();
        if (elItemArrayList != null && elItemArrayList.size() > 0) {
            item_list.setVisibility(View.VISIBLE);
            item_no_data.setVisibility(View.GONE);

            itemListAdapter = new ItemListAdapter(context, elItemArrayList);
            item_list.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();
        } else {
            item_list.setVisibility(View.GONE);
            item_no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_new_item:
                startActivity(new Intent(context, AddItemActivity.class));
                break;
        }
    }
}
