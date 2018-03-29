package com.palash.sampleapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.palash.sampleapp.R;
import com.palash.sampleapp.activity.AddItemActivity;
import com.palash.sampleapp.utilities.LocalSetting;

public class AddOrderFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private LocalSetting localSetting;
    private TextView button_add_new_item;

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
        button_add_new_item.setOnClickListener(this);
    }

    private void initialize() {
        context = getActivity();
        localSetting = new LocalSetting();
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
