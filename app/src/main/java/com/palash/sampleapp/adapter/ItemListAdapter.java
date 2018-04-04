package com.palash.sampleapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.palash.sampleapp.R;
import com.palash.sampleapp.activity.LoginActivity;
import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELCatalog;
import com.palash.sampleapp.entiry.ELItem;
import com.palash.sampleapp.utilities.Constants;
import com.palash.sampleapp.utilities.LocalSetting;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by ajits on 3/28/2018.
 */

public class ItemListAdapter extends BaseAdapter {

    private Context context;
    private LocalSetting localSetting;
    private LayoutInflater inflater;
    private ArrayList<ELItem> elItemArrayList; // Values to be displayed
    private DatabaseContract databaseContract;
    private DatabaseAdapter databaseAdapter;
    private DatabaseAdapter.ItemAdapter itemAdapterDB;

    public ItemListAdapter(Context context, ArrayList<ELItem> elItemArrayList) {
        this.context = context;
        this.elItemArrayList = elItemArrayList;
        localSetting = new LocalSetting();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        databaseContract = new DatabaseContract(context);
        databaseAdapter = new DatabaseAdapter(databaseContract);
        itemAdapterDB = databaseAdapter.new ItemAdapter();
    }

    @Override
    public int getCount() {
        return elItemArrayList.size();
    }

    @Override
    public ELItem getItem(int position) {
        return elItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_item, null);
                holder.row_item_file_type = (ImageView) convertView.findViewById(R.id.row_item_file_type);
                holder.row_item_order_number = (TextView) convertView.findViewById(R.id.row_item_order_number);
                holder.row_item_date = (TextView) convertView.findViewById(R.id.row_item_date);
                holder.row_item_catelog_name = (TextView) convertView.findViewById(R.id.row_item_catelog_name);
                holder.row_item_file_name = (TextView) convertView.findViewById(R.id.row_item_file_name);
                holder.row_item_remark = (TextView) convertView.findViewById(R.id.row_item_remark);
                holder.row_item_view = (TextView) convertView.findViewById(R.id.row_item_view);
                holder.row_item_delete = (TextView) convertView.findViewById(R.id.row_item_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.row_item_order_number.setText("Item ID : " + elItemArrayList.get(position).getItemID());
            holder.row_item_date.setText(localSetting.convertDate(elItemArrayList.get(position).getItemAddedDate()));
            holder.row_item_catelog_name.setText(elItemArrayList.get(position).getCatalogName());
            holder.row_item_file_name.setText(elItemArrayList.get(position).getAttachmentName());
            holder.row_item_remark.setText(elItemArrayList.get(position).getItemRemark());

            holder.row_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ELItem elItem = elItemArrayList.get(position);
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Do you really want to delete this item!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    deleteItem(elItem, position);
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private void deleteItem(ELItem elItem, int Position) {
        if (elItem.getItemID() != null && elItem.getItemID().length() > 0) {
            itemAdapterDB.deleteItem(elItem.getItemID());
            elItemArrayList.remove(Position);
            notifyDataSetChanged();
            localSetting.showSuccessAlert(context, context.getResources().getString(R.string.app_name), "Item Deleted Successfully");
        }
    }

    private class ViewHolder {
        ImageView row_item_file_type;
        TextView row_item_order_number;
        TextView row_item_date;
        TextView row_item_catelog_name;
        TextView row_item_file_name;
        TextView row_item_remark;
        TextView row_item_view;
        TextView row_item_delete;
    }
}
