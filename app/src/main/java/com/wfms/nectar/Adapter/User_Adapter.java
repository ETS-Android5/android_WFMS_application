package com.wfms.nectar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.wfms.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nectar on 10-09-2018.
 */

public class User_Adapter extends BaseAdapter {
    Context con;
    ArrayList<EmployeData> userlist;

    @BindView(R.id.user_name)

    TextView user_name;

    /** ButterKnife Code **/
    public User_Adapter(Context drawerActivity, ArrayList<EmployeData> userlist) {
        this.con=drawerActivity;
        this.userlist=userlist;

    }

    @Override
    public int getCount() {
//        Log.d("fuellist",""+Fuellist.size());
        return userlist.size();
    }

    @Override
    public Object getItem(int i) {
        return userlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View raw = LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem_layout,parent, false);
        ButterKnife.bind(this,raw);
        user_name.setText(userlist.get(position).getUsername1());
        return raw;
    }
    }

