package com.palash.sampleapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palash.sampleapp.R;
import com.palash.sampleapp.entiry.ELItem;
import com.palash.sampleapp.utilities.LocalSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ajit on 22/8/16.
 */
public class OrderExpListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LocalSetting localSetting;
    private HashMap<String, ArrayList<ELItem>> elItemMainHashMapList;
    private List<String> elItemChildList;

    public OrderExpListAdapter(Context context, HashMap<String, ArrayList<ELItem>> elItemMainHashMapList, List<String> elItemChildList) {
        this.context = context;
        this.elItemMainHashMapList = elItemMainHashMapList;
        this.elItemChildList = elItemChildList;
        localSetting = new LocalSetting();
    }

    @Override
    public int getGroupCount() {
        return elItemMainHashMapList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return elItemMainHashMapList.get(elItemChildList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return elItemChildList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return elItemMainHashMapList.get(elItemChildList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public int getChildTypeCount() {
        return getGroupCount();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String groupText = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater lf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lf.inflate(R.layout.row_spinner_dropdown, parent, false);
        }

        TextView row_txt_description = (TextView) convertView.findViewById(R.id.row_txt_description);
        //ImageView group_indicator = (ImageView) convertView.findViewById(R.id.group_indicator);
        try {
            List<String> elHeaderData = Arrays.asList(groupText.split(",.,"));
            row_txt_description.setText("Order Number " + elHeaderData.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if (isExpanded) {
            group_indicator.setImageResource(R.drawable.ic_keyboard_arrow_up_blue_300_24dp);
        } else {
            group_indicator.setImageResource(R.drawable.ic_keyboard_arrow_down_blue_300_24dp);
        }*/
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ELItem elItem = (ELItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_item, null);
        }

        ImageView row_item_file_type = (ImageView) convertView.findViewById(R.id.row_item_file_type);
        TextView row_item_order_number = (TextView) convertView.findViewById(R.id.row_item_order_number);
        TextView row_item_date = (TextView) convertView.findViewById(R.id.row_item_date);
        TextView row_item_catelog_name = (TextView) convertView.findViewById(R.id.row_item_catelog_name);
        TextView row_item_file_name = (TextView) convertView.findViewById(R.id.row_item_file_name);
        TextView row_item_remark = (TextView) convertView.findViewById(R.id.row_item_remark);
        TextView row_item_view = (TextView) convertView.findViewById(R.id.row_item_view);
        TextView row_item_delete = (TextView) convertView.findViewById(R.id.row_item_delete);

        row_item_order_number.setText("Item ID : " + elItem.getItemID());
        row_item_date.setText(elItem.getItemAddedDate());
        row_item_catelog_name.setText(elItem.getCatalogName());
        row_item_file_name.setText(elItem.getAttachmentName());
        row_item_remark.setText(elItem.getItemRemark());

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}