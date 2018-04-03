package com.palash.sampleapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palash.sampleapp.R;
import com.palash.sampleapp.activity.AddItemActivity;
import com.palash.sampleapp.activity.MainActivity;
import com.palash.sampleapp.adapter.ItemListAdapter;
import com.palash.sampleapp.api.JsonObjectMapper;
import com.palash.sampleapp.api.WebServiceConsumer;
import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELItem;
import com.palash.sampleapp.entiry.ELLogin;
import com.palash.sampleapp.entiry.ELOrder;
import com.palash.sampleapp.utilities.Constants;
import com.palash.sampleapp.utilities.LocalSetting;
import com.palash.sampleapp.utilities.TransparentProgressDialog;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
    private TextView order_number_txt;
    private TextView button_add_order;
    private ListView item_list;
    private LinearLayout item_no_data;

    private ItemListAdapter itemListAdapter;

    private String OrderNumber = "1001";

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
        order_number_txt = (TextView) view.findViewById(R.id.order_number_txt);
        button_add_new_item = (TextView) view.findViewById(R.id.button_add_new_item);
        button_add_order = (TextView) view.findViewById(R.id.button_add_order);
        item_list = (ListView) view.findViewById(R.id.item_list);
        item_no_data = (LinearLayout) view.findViewById(R.id.item_no_data);

        item_list.setVisibility(View.GONE);
        button_add_order.setVisibility(View.GONE);
        item_no_data.setVisibility(View.VISIBLE);
        button_add_new_item.setOnClickListener(this);
        button_add_order.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        OrderNumber = bundle.getString("OrderNumber");
        order_number_txt.setText("Order Number : " + OrderNumber);
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
            button_add_order.setVisibility(View.VISIBLE);
            item_no_data.setVisibility(View.GONE);

            itemListAdapter = new ItemListAdapter(context, elItemArrayList);
            item_list.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();
        } else {
            item_list.setVisibility(View.GONE);
            button_add_order.setVisibility(View.GONE);
            item_no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_new_item:
                startActivity(new Intent(context, AddItemActivity.class).putExtra("OrderNumber",OrderNumber));
                break;
            case R.id.button_add_order:
                bindList();
                break;
        }
    }

    private void bindList() {
        elOrder = new ELOrder();
        elOrder.setOrderNumber(OrderNumber);
        elOrder.setOrderAddedDate(localSetting.returnCurrentDate());

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Do you really want to add this order!")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        new AddOrderTask().execute();
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

    public class AddOrderTask extends AsyncTask<Void, Void, String> {
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
                jSonData = objectMapper.unMap(elOrder);
                response = serviceConsumer.POST(Constants.ADD_ORDER_URL, jSonData);

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
            orderAdapterDB.create(elOrder);
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(context.getResources().getString(R.string.app_name))
                    .setContentText("Order Added Successfully")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            button_add_order.setVisibility(View.GONE);
                            item_list.setVisibility(View.GONE);
                            item_no_data.setVisibility(View.VISIBLE);
                            itemAdapterDB.UpdateByOrderID(OrderNumber);
                            MainActivity main = new MainActivity();
                            main.ShowOrderList();
                        }
                    })
                    .show();

            super.onPostExecute(result);
        }
    }
}
