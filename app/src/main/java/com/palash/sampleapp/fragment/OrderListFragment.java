package com.palash.sampleapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palash.sampleapp.R;
import com.palash.sampleapp.activity.AddItemActivity;
import com.palash.sampleapp.adapter.ItemListAdapter;
import com.palash.sampleapp.adapter.OrderExpListAdapter;
import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELItem;
import com.palash.sampleapp.entiry.ELLogin;
import com.palash.sampleapp.entiry.ELOrder;
import com.palash.sampleapp.utilities.LocalSetting;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderListFragment extends Fragment {

    private Context context;
    private LocalSetting localSetting;

    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.LoginAdapter loginAdapterDB;
    private DatabaseAdapter.ItemAdapter itemAdapterDB;
    private DatabaseAdapter.OrderAdapter orderAdapterDB;

    private ELLogin elLogin;
    private ELOrder elOrder;
    private ELItem elItem;
    private ArrayList<ELLogin> elArrayLogin;
    private ArrayList<ELOrder> elOrderArrayList;
    private ArrayList<ELItem> elItemArrayList;

    private ExpandableListView order_explist;
    private LinearLayout order_no_data;

    private OrderExpListAdapter orderExpListAdapter;

    public OrderListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_order_list_fragment, container, false);
        initialize();
        InitView(view);
        return view;
    }

    private void initialize() {
        context = getActivity();
        localSetting = new LocalSetting();

        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        loginAdapterDB = databaseAdapter.new LoginAdapter();
        itemAdapterDB = databaseAdapter.new ItemAdapter();
        orderAdapterDB = databaseAdapter.new OrderAdapter();
        elLogin = new ELLogin();
        elItem = new ELItem();

        elArrayLogin = new ArrayList<ELLogin>();
        elArrayLogin = loginAdapterDB.listAll();
        if (elArrayLogin != null && elArrayLogin.size() > 0) {
            elLogin = elArrayLogin.get(0);
        }
    }

    private void InitView(View view) {
        order_explist = (ExpandableListView) view.findViewById(R.id.order_explist);
        order_no_data = (LinearLayout) view.findViewById(R.id.order_no_data);

        order_explist.setVisibility(View.GONE);
        order_no_data.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        setOrderData();
    }

    private void setOrderData() {
        elOrderArrayList = orderAdapterDB.listAll();
        ArrayList<String> mListIDs = new ArrayList<>();
        if (elOrderArrayList != null && elOrderArrayList.size() > 0) {
            for (int i = 0; i < elOrderArrayList.size(); i++) {
                ELOrder elOrder = elOrderArrayList.get(i);
                String newID = elOrder.getOrderNumber();
                if (mListIDs.size() > 0) {
                    Boolean flagMatch = false;
                    for (int j = 0; j < mListIDs.size(); j++) {
                        String checkID = mListIDs.get(j);
                        if (checkID.equals(newID)) {
                            flagMatch = true;
                        }
                    }
                    if (flagMatch == false) {
                        mListIDs.add(elOrder.getOrderNumber());
                    }
                } else {
                    mListIDs.add(elOrder.getOrderNumber());
                }
            }

            if (mListIDs.size() > 0) {
                HashMap<String, ArrayList<ELItem>> elItemMainHashMapList = new HashMap<>();
                ArrayList<String> elItemChildList = new ArrayList<>();
                for (int k = 0; k < mListIDs.size(); k++) {
                    ArrayList<ELItem> elItemArrayList = itemAdapterDB.listAllID(mListIDs.get(k));
                    if (elItemArrayList.size() > 0) {
                        ELItem elItem = elItemArrayList.get(0);
                        String key = String.valueOf(k) + ",.," + elItem.getOrderNumber();
                        elItemMainHashMapList.put(key, elItemArrayList);
                        elItemChildList.add(key);
                    }
                }

                if (elItemMainHashMapList != null && elItemMainHashMapList.size() > 0) {
                    orderExpListAdapter = new OrderExpListAdapter(context, elItemMainHashMapList, elItemChildList);
                    order_explist.setAdapter(orderExpListAdapter);
                    orderExpListAdapter.notifyDataSetChanged();

                    order_no_data.setVisibility(View.GONE);
                    order_explist.setVisibility(View.VISIBLE);
                    order_explist.expandGroup(0);
                } else {
                    order_explist.setVisibility(View.GONE);
                    order_no_data.setVisibility(View.VISIBLE);
                }
            }
        } else {
            order_explist.setVisibility(View.GONE);
            order_no_data.setVisibility(View.VISIBLE);
        }
    }
}
