package com.wfms.nectar.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveData;
import com.wfms.nectar.wfms.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeofenceHistoryAdapter extends RecyclerView.Adapter<GeofenceHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<LeaveData> geofencelist;
    public GeofenceHistoryAdapter(Context applicationContext, ArrayList<LeaveData> geofencelist) {
        this.context=applicationContext;
        this.geofencelist=geofencelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.geofence_history_item, parent, false);
        return new GeofenceHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeofenceHistoryAdapter.ViewHolder holder, int position) {
        LeaveData data=geofencelist.get(position);

     if(data.getName()!=null)
     {
         holder.geohistory_username.setText("Name: "+data.getName());
     }
        if(data.getGeofencename()!=null)
        {
            holder.geohistory_geoname.setText("GeofenceName: "+data.getGeofencename());
        }
        if(data.getType()!=null)
        {
            if(data.getType().equals("1"))
            {
                holder.geohistory_type.setText("Type: Entry");
            } else if(data.getType().equals("2"))
            {
                holder.geohistory_type.setText("Type: Exit");
            }

        }
        if(data.getAddress()!=null)
        {
            holder.geohistory_address.setText("Address: "+data.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return geofencelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * ButterKnife Code
         **/

        @BindView(R.id.geohistory_username)
        TextView geohistory_username;
        @BindView(R.id.geohistory_geoname)
        TextView geohistory_geoname;
        @BindView(R.id.geohistory_type)
        TextView geohistory_type;
        @BindView(R.id.geohistory_address)
        TextView geohistory_address;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }
}
