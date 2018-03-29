package com.palash.sampleapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palash.sampleapp.R;
import com.palash.sampleapp.entiry.ELCatalog;

import java.util.ArrayList;

/**
 * Created by ajits on 3/28/2018.
 */

public class CatalogListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<ELCatalog> elArrayListCatalog; // Values to be displayed

    public CatalogListAdapter(Context context, ArrayList<ELCatalog> elArrayListCatalog) {
        mContext = context;
        this.elArrayListCatalog = elArrayListCatalog;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return elArrayListCatalog.size();
    }

    @Override
    public ELCatalog getItem(int position) {
        return elArrayListCatalog.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_spinner_dropdown, null);
                holder.row_txt_id = (TextView) convertView.findViewById(R.id.row_txt_id);
                holder.row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.row_txt_id.setText(elArrayListCatalog.get(position).getID());
            holder.row_txt_description.setText(elArrayListCatalog.get(position).getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView row_txt_id;
        TextView row_txt_description;
    }
}
