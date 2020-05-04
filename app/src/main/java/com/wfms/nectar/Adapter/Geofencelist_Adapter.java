package com.wfms.nectar.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.suke.widget.SwitchButton;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeResponse;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.GetAlEmployePresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImplOut;
import com.wfms.nectar.presenter.presenterImpl.SignUpUpdatePresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpadminPresenterImpl;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.EmployeView;
import com.wfms.nectar.viewstate.SignUpView;
import com.wfms.nectar.viewstate.SignUpViewOut;
import com.wfms.nectar.viewstate.SignUpViewUpdate;
import com.wfms.nectar.viewstate.SignUpViewadminnotification;
import com.wfms.nectar.wfms.CreateGeofenceActivity;
import com.wfms.nectar.wfms.GeofenceMapActivity;
import com.wfms.nectar.wfms.MainActivity;
import com.wfms.nectar.wfms.R;
import com.wfms.nectar.wfms.SettingActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Nectar on 03-09-2018.
 */

public class Geofencelist_Adapter extends RecyclerView.Adapter<Geofencelist_Adapter.ViewHolder>implements SignUpView, EmployeView, SignUpViewOut, SignUpViewadminnotification, SignUpViewUpdate {
  //  SwitchButton geofence_enable;;
    boolean istrue = false;
    int geofenceposition=0;
    Context con;
    ArrayList<EmployeData> geofencelist,geofencelist1;
    EmployeData data;
    EditText geofence_address,long_test,lat_test,end_lat_test,end_long_test,end_geofence_address;
     Dialog dialog;
    AlertDialog alertDialog;
    public static double latitude=0.0,end_latitude=0.0;
    public static double longitude=0.0,end_longitude=0.0;
    public static String  geofencetype;
    public static float radius=0;
    ProgressBar geofence_progressbar;
    ArrayList<String> geofencearray=new ArrayList<>();
    ArrayList<String> unassign_geofencearray=new ArrayList<>();
    String[] geofence_area = { "Select Geofence Type","Circle","SingleLine"};
    Button assign_geofence,dassign_geofence;
    String geofenceid,employid,isnotication,employeename,area_value;
    ProgressDialog progressDialog;
    public boolean sucess=false;
    ArrayList<EmployeData> Geofencedata=new ArrayList();
    public boolean isassign=false;
    public boolean isdassign=false;
    ArrayList<EmployeData> assigngeofencedata=new ArrayList<>();
    public Geofencelist_Adapter(AppCompatActivity activity, ArrayList<EmployeData> employeData, ArrayList<EmployeData> geofencelist1, ArrayList<EmployeData> geofencelist, ProgressBar geofence_progressbar, Button assign_geofence, String employid, String employeename, Button dassign_geofence, ArrayList<EmployeData> geofencedata) {
        this.con = activity;
        this.geofencelist = geofencelist;
        this.geofencelist1 = geofencelist1;
        this.geofence_progressbar=geofence_progressbar;
        this.assign_geofence=  assign_geofence;
        this.employid=employid;
        this.employeename=employeename;
        this.dassign_geofence=  dassign_geofence;
        this.Geofencedata=geofencedata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.geofence_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmployeData geofenceData = geofencelist.get(position);

        if(PrefUtils.getKey(con, AppConstants.UserID).equalsIgnoreCase("1"))
        {
            if(PrefUtils.getKey(con, AppConstants.Isgeofencenotification)!=null)
            {
                if(PrefUtils.getKey(con, AppConstants.Isgeofencenotification).equals("1"))
                {
                    holder.geofence_enable.setVisibility(View.VISIBLE);


                }else
                {
                    holder. geofence_enable.setVisibility(View.GONE);
                }
            }else
            {
                holder.geofence_enable.setVisibility(View.VISIBLE);
            }

            holder.edit_geofence.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.VISIBLE);
        }
        else
        {
            holder. geofence_enable.setVisibility(View.GONE);
            holder.edit_geofence.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.GONE);

        }

        if(geofenceData.getIsgeonotification()!=null)
        {
            if(geofenceData.getIsgeonotification().equalsIgnoreCase("0"))
            {
                holder.geofence_enable.setChecked(false);
            }
            else
            {
                holder. geofence_enable.setChecked(true);
            }
        }
        holder.geofence_name.setText(geofenceData.getGeofencename());

        holder.geofence_location.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               geofencetype=geofenceData.getType();

           if(geofenceData.getGeofence_latitude()!=null&&geofenceData.getGeofence_longitude()!=null) {
               latitude = Double.parseDouble(geofenceData.getGeofence_latitude());
               longitude=Double.parseDouble(geofenceData.getGeofence_longitude());
               radius=Float.parseFloat(geofenceData.getRadius());
           }
        if(geofencetype.equalsIgnoreCase("SingleLine")) {
            if (geofenceData.getEnd_latitude() != null && geofenceData.getEnd_longitude() != null ) {
                end_latitude = Double.parseDouble(geofenceData.getEnd_latitude());
                end_longitude = Double.parseDouble(geofenceData.getEnd_longitude());
            }
        }
               Intent i=new Intent(con, GeofenceMapActivity.class);
               con.startActivity(i);
           }
       });

        holder.checkbox.setChecked(geofenceData.isSelected());
        holder.checkbox.setTag(position);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.checkbox.getTag();
                if (geofencelist.get(pos).isSelected()) {
                    geofencelist.get(pos).setSelected(false);
                    geofencearray.remove(geofenceData.getId());
                    for (int i=0;i<Geofencedata.size();i++)
                    {
                        if(geofenceData.getId().equalsIgnoreCase(Geofencedata.get(i).getGeofence_id()))
                        {
                            unassign_geofencearray.add(geofenceData.getId());
                        }
                    }
                } else {
                    geofencelist.get(pos).setSelected(true);
                    geofencearray.add(geofenceData.getId());

                }
            }
        });


        holder.geofence_enable.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                sucess=false;

                if( holder.geofence_enable.isChecked())
                {


                    initnotificationenableAPIResources(geofenceData.getId(),"on", "1");
                    isnotication="1";
                    if(sucess==true)
                    {
                        PrefUtils.storeKey(con, AppConstants.Geofenceenable,isnotication);
                    }

                }
                else
                {
                    initnotificationenableAPIResources(geofenceData.getId(),"off", "1");
                    isnotication="0";
                    if(sucess==false)
                    {
                        PrefUtils.storeKey(con, AppConstants.Geofenceenable,isnotication);
                    }

                }
            }
        });
        holder.edit_geofence.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           dialog = new Dialog(con);
           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          //dialog.setCancelable(false);
           dialog.setContentView(R.layout.add_new_geofence);
           EditText geofence_namme = (EditText) dialog.findViewById(R.id.geofence_namme);
           lat_test = (EditText) dialog.findViewById(R.id.lat_test);
           long_test = (EditText) dialog.findViewById(R.id.long_test);
           Button add_button = (Button) dialog.findViewById(R.id.add_button);
           Button remove_button = (Button) dialog.findViewById(R.id.remove_button);
           TextView geofence_text1=(TextView)dialog.findViewById(R.id.geofence_text1);
           EditText radius = (EditText) dialog.findViewById(R.id.radius);
           Button set_button=(Button)dialog.findViewById(R.id.set_button);
           Button end_set_button=(Button)dialog.findViewById(R.id.end_set_button);
           geofence_address = (EditText) dialog.findViewById(R.id.geofence_address);
           Spinner  area = (Spinner) dialog.findViewById(R.id.area);
           SwitchButton geofence_enable1 = (SwitchButton) dialog.findViewById(R.id.geofence_enable1);
           add_button.setText("Update");
           RelativeLayout geofence_layout = (RelativeLayout) dialog.findViewById(R.id.geofence_layout);
           RelativeLayout geofence_location = (RelativeLayout) dialog.findViewById(R.id.geofence_location);
           geofence_layout.setVisibility(View.GONE);
           remove_button.setVisibility(View.VISIBLE);
           Button cancel_dialog = (Button) dialog.findViewById(R.id.cancel_dialog);
           RelativeLayout end_location_layout=  (RelativeLayout) dialog.findViewById(R.id.end_location_layout);
           end_lat_test = (EditText) dialog.findViewById(R.id.end_lat_test);
           end_long_test = (EditText) dialog.findViewById(R.id.end_long_test);
           end_geofence_address = (EditText) dialog.findViewById(R.id.end_geofence_address);

           geofence_address.addTextChangedListener(new TextWatcher() {

               @Override
               public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                   if(geofence_address.getText().toString().length()==0)
                   {
                       lat_test.setText("");
                       long_test.setText("");
                   }
               }

               @Override
               public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

               }

               @Override
               public void afterTextChanged(Editable arg0) {

               }
           });

           end_geofence_address.addTextChangedListener(new TextWatcher() {

               @Override
               public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                   if(end_geofence_address.getText().toString().length()==0)
                   {
                       end_lat_test.setText("");
                       end_long_test.setText("");
                   }
               }

               @Override
               public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
               }

               @Override
               public void afterTextChanged(Editable arg0) {
               }
           });

           if (geofenceData.getType().equalsIgnoreCase("Select Geofence Type")) {

               geofenceposition = 0;
           } else if (geofenceData.getType().equalsIgnoreCase("Circle")) {

               geofenceposition = 1;
           } else if (geofenceData.getType().equalsIgnoreCase("SingleLine")) {

               geofenceposition = 2;
           }
           area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 istrue=true;
                 //  setid();
                   area_value = area.getSelectedItem().toString();
                   if(area_value.equalsIgnoreCase("SingleLine"))
                   {
                       end_location_layout.setVisibility(View.VISIBLE);
                   }
                   else
                   {

                       end_location_layout.setVisibility(View.GONE);
                   }
               }

               @Override
               public void onNothingSelected(AdapterView<?> adapterView) {

               }
           });
           ArrayAdapter aa = new ArrayAdapter(con,android.R.layout.simple_spinner_item,geofence_area);
           aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           //Setting the ArrayAdapter data on the Spinner
           area.setAdapter(aa);
           area.setSelection(geofenceposition);
           if(geofenceData.getType().equalsIgnoreCase("SingleLine"))
           {
               end_lat_test.setText(geofenceData.getEnd_latitude());
               end_long_test.setText(geofenceData.getEnd_longitude());
               end_geofence_address.setText(geofenceData.getEnd_geofence_address());
               end_location_layout.setVisibility(View.VISIBLE);
           }
           else
           {
               end_location_layout.setVisibility(View.GONE);
           }
           cancel_dialog.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();
               }
           });
           add_button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   if(geofence_namme.getText().toString().length()==0)
                   {
                       Toast.makeText(con,"Please enter geofence name",Toast.LENGTH_LONG).show();
                   }
                   else if(area_value.equalsIgnoreCase("Select Geofence Type"))
                   {
                       Toast.makeText(con,"Please select geofence type",Toast.LENGTH_LONG).show();
                   }
                   else if(lat_test.getText().toString().length()==0)
                   {
                       Toast.makeText(con,"Please enter latitude",Toast.LENGTH_LONG).show();
                   }
                   else if(lat_test.getText().toString().length()<5)
                   {
                       Toast.makeText(con,"Please enter corrrect latitude ",Toast.LENGTH_LONG).show();
                   }
                   else if(isValidLatLng(Double.parseDouble(lat_test.getText().toString()))==false)
                   {
                       Toast.makeText(con,"Please enter correct latitude  ",Toast.LENGTH_LONG).show();
                   }
                   else if (long_test.getText().toString().length()==0)
                   {
                       Toast.makeText(con,"Please enter longitude",Toast.LENGTH_LONG).show();
                   }
                   else if(lat_test.getText().toString().length()<5)
                   {
                       Toast.makeText(con,"Please enter corrrect longitude ",Toast.LENGTH_LONG).show();
                   }
                   else if(isValidLatLng1(Double.parseDouble(long_test.getText().toString()))==false)
                   {
                       Toast.makeText(con,"Please enter correct  longitude ",Toast.LENGTH_LONG).show();
                   }

                   else if (radius.getText().toString().length()==0)
                   {
                       Toast.makeText(con,"Please Enter Radius",Toast.LENGTH_LONG).show();
                   }
                   else if(radius.getText().toString().equalsIgnoreCase("0"))
                   {
                       Toast.makeText(con,"Please enter valid radius ",Toast.LENGTH_LONG).show();
                   }  else if(area_value.equalsIgnoreCase("SingleLine"))
                   {
                       if(end_lat_test.getText().toString().length()==0)
                       {
                           Toast.makeText(con,"Please enter destination latitude",Toast.LENGTH_LONG).show();
                       }
                       else if(end_lat_test.getText().toString().length()<5)
                       {
                           Toast.makeText(con,"Please enter corrrect destination latitude ",Toast.LENGTH_LONG).show();
                       }
                       else if(isValidLatLng(Double.parseDouble(end_lat_test.getText().toString()))==false)
                       {
                           Toast.makeText(con,"Please enter correct destination latitude  ",Toast.LENGTH_LONG).show();
                       }
                       else if (end_long_test.getText().toString().length()==0)
                       {
                           Toast.makeText(con,"Please enter destination longitude",Toast.LENGTH_LONG).show();
                       }
                       else if(end_long_test.getText().toString().length()<5)
                       {
                           Toast.makeText(con,"Please enter corrrect destination longitude ",Toast.LENGTH_LONG).show();
                       }
                       else if(isValidLatLng1(Double.parseDouble(end_long_test.getText().toString()))==false)
                       {
                           Toast.makeText(con,"Please enter correct destination longitude ",Toast.LENGTH_LONG).show();
                       }
                       else if (lat_test.getText().toString().equalsIgnoreCase(end_lat_test.getText().toString())||long_test.getText().toString().equalsIgnoreCase(end_long_test.getText().toString()))
                       {
                           Toast.makeText(con,"source and destination address should not be same",Toast.LENGTH_LONG).show();
                       }
                       else
                       {

                           if (NetworkUtil.isOnline(con)) {
                               progressDialog = new ProgressDialog(con, R.style.AppCompatAlertDialogStyle);
                               progressDialog.setMessage(con.getResources().getString(R.string.loading));
                               progressDialog.setCanceledOnTouchOutside(false);
                               progressDialog.show();
                               initsavegeofenceAPIResources(geofence_namme.getText().toString(), lat_test.getText().toString(), long_test.getText().toString(), radius.getText().toString(), geofence_address.getText().toString(), area_value,geofenceData.getId(),end_lat_test.getText().toString(),end_long_test.getText().toString(),end_geofence_address.getText().toString());
                           }else
                           {
                               Toast.makeText(con,"Please check internet connection",Toast.LENGTH_SHORT).show();
                           }
                       }
                   }
                   else {

                       if (NetworkUtil.isOnline(con)) {
                           progressDialog = new ProgressDialog(con, R.style.AppCompatAlertDialogStyle);
                           progressDialog.setMessage(con.getResources().getString(R.string.loading));
                           progressDialog.setCanceledOnTouchOutside(false);
                           progressDialog.show();
                           initsavegeofenceAPIResources(geofence_namme.getText().toString(), lat_test.getText().toString(), long_test.getText().toString(), radius.getText().toString(), geofence_address.getText().toString(), area_value,geofenceData.getId(),"0.0","0.0","");
                       }else
                       {
                           Toast.makeText(con,"Please check internet connection",Toast.LENGTH_SHORT).show();
                       }
               }}
           });


           remove_button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);

                   alertDialogBuilder.setMessage("Are you sure, you want to remove geofence");
                           alertDialogBuilder.setPositiveButton("yes",
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface arg0, int arg1) {
                                           if (NetworkUtil.isOnline(con)) {
                                               progressDialog = new ProgressDialog(con, R.style.AppCompatAlertDialogStyle);
                                               progressDialog.setMessage(con.getResources().getString(R.string.loading));
                                               progressDialog.setCanceledOnTouchOutside(false);
                                               progressDialog.show();
                                               initremoveeofenceAPIResources(geofenceData.getId());
                                           }else
                                           {
                                               Toast.makeText(con,"Please check internet connection",Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });

                   alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

                       public void onClick(DialogInterface dialog, int which) {
                          alertDialog.dismiss();
                       }
                   });

                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

               }
           });
           set_button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(geofence_address.getText().toString().length()>0)
                   {
                       String  Value = geofence_address.getText().toString();
                       getLocationFromAddress(con, Value);

                   }
                   else
                   {
                       Toast.makeText(con,"Please enter address",Toast.LENGTH_LONG).show();
                   }
               }
           });

           end_set_button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(end_geofence_address.getText().toString().length()>0)
                   {
                       String  Value = end_geofence_address.getText().toString();
                       getLocationFromAddress_destination(con, Value);

                   }
                   else
                   {
                       Toast.makeText(con,"Please enter address",Toast.LENGTH_LONG).show();
                   }
               }
           });

           geofence_namme.setText(geofenceData.getGeofencename());
           lat_test.setText(geofenceData.getGeofence_latitude());
           long_test.setText(geofenceData.getGeofence_longitude());
           radius.setText(geofenceData.getRadius());
           if(geofenceData.getAddress()!=null) {
               geofence_address.setText(geofenceData.getAddress());
           }



           geofence_location.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   lat_test.setText(""+ CreateGeofenceActivity.lat);
                   long_test.setText(""+ CreateGeofenceActivity.longi);
                   getAddress();
               }
           });
           end_location_layout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   end_lat_test.setText(""+CreateGeofenceActivity.lat);
                   end_long_test.setText(""+CreateGeofenceActivity.longi);
                   getAddressDestination();
               }
           });
           dialog.show();
       }
   });
        assign_geofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(unassign_geofencearray.size()>0)
             {
                 unassign_geofencearray.clear();
             }

                 if(employeename.equalsIgnoreCase("Select User"))
                {
                    Toast.makeText(con, "Please Select User", Toast.LENGTH_SHORT).show();
                }
                else if(employeename.equalsIgnoreCase("Admin"))
                {
                    Toast.makeText(con, "Admin can't assign geofence to himself", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(geofencearray.size()>0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < geofencearray.size(); i++) {
                            String geofenceid1 = geofencearray.get(i);

                            sb.append(geofenceid1).append(",");
                        }
                        geofenceid = sb.deleteCharAt(sb.length() - 1).toString();

                        if (NetworkUtil.isOnline(con)) {
                            isassign=true;
                            isdassign=false;
                            progressDialog = new ProgressDialog(con, R.style.AppCompatAlertDialogStyle);
                            progressDialog.setMessage(con.getResources().getString(R.string.loading));
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            initassigneofenceAPIResources(CreateGeofenceActivity.employid, PrefUtils.getKey(con, AppConstants.Clientid),geofenceid, PrefUtils.getKey(con, AppConstants.UserID));
                        } else {
                            Toast.makeText(con, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(con, "Please Select at least one geofence", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        dassign_geofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(geofencearray.size()>0)
                {
                    geofencearray.clear();
                }
                if(!employeename.equalsIgnoreCase("Select User"))
                {
                    if(unassign_geofencearray.size()>0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < unassign_geofencearray.size(); i++) {
                            String geofenceid1 = unassign_geofencearray.get(i);
                            sb.append(geofenceid1).append(",");
                        }
                        geofenceid = sb.deleteCharAt(sb.length() - 1).toString();
                      //  Log.d("geofenceid",geofenceid);
                        if (NetworkUtil.isOnline(con)) {
                            isassign=false;
                            isdassign=true;
                            progressDialog = new ProgressDialog(con, R.style.AppCompatAlertDialogStyle);
                            progressDialog.setMessage(con.getResources().getString(R.string.loading));
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            initdassigneofenceAPIResources(CreateGeofenceActivity.employid, PrefUtils.getKey(con, AppConstants.Clientid),geofenceid, PrefUtils.getKey(con, AppConstants.UserID));

                        } else {
                            Toast.makeText(con, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(con, "Please uncheck geofence which you want to remove from user", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(con, "Please Select User", Toast.LENGTH_SHORT).show();
                }}

        });
    }

    private void initnotificationenableAPIResources(String id, String status, String adminid) {
        SignUpUpdatePresenterImpl geofencePresenter = new SignUpUpdatePresenterImpl(this);
        geofencePresenter.callApi(AppConstants.SingleGeofenceNotification, id, status, adminid,PrefUtils.getKey(con,AppConstants.Api_Token));
    }
    private void initassigneofenceAPIResources(String employid, String clientid, String geofenceid, String userid) {
        SignUpadminPresenterImpl geofencePresenter = new SignUpadminPresenterImpl(this);
        geofencePresenter.callApi(AppConstants.AssignGeofence, employid,clientid,geofenceid,userid,PrefUtils.getKey(con,AppConstants.Api_Token));
    }
    private void initdassigneofenceAPIResources(String employid, String clientid, String geofenceid, String userid) {
        SignUpadminPresenterImpl geofencePresenter = new SignUpadminPresenterImpl(this);
        geofencePresenter.callApi(AppConstants.DAssignGeofence, employid,clientid,geofenceid,userid,PrefUtils.getKey(con,AppConstants.Api_Token));
    }
    private void initremoveeofenceAPIResources(String id) {
        SignUpPresenterImplOut geofencePresenter = new SignUpPresenterImplOut(this);
        geofencePresenter.callApi(AppConstants.RemoveGeofence, id,PrefUtils.getKey(con,AppConstants.Api_Token),PrefUtils.getKey(con,AppConstants.UserID));
    }

    private void initsavegeofenceAPIResources(String geofencename, String lat, String longi, String radius, String addreess,String area,String geofenceid,String end_lat, String end_longi, String end_address) {
        SignUpPresenterImpl geofencePresenter = new SignUpPresenterImpl(this);
        geofencePresenter.callApi(AppConstants.EditGeofence, geofencename, lat, longi,radius,addreess,area, PrefUtils.getKey(con, AppConstants.Clientid), PrefUtils.getKey(con, AppConstants.UserID),geofenceid,end_lat,end_longi,end_address,PrefUtils.getKey(con,AppConstants.Api_Token));
    }

    public boolean isValidLatLng(double lat){
        if(lat < -90 || lat > 90)
        {
            return false;
        }
        return true;
    }

    public boolean isValidLatLng1( double lng){
        if(lng < -180 || lng > 180)
        {
            return false;
        }
        return true;
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if(address.size()>0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
                lat_test.setText(""+location.getLatitude());
                long_test.setText(""+location.getLongitude());
            }
            else
            {
                Toast.makeText(con,"Please enter correct address",Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public LatLng getLocationFromAddress_destination(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if(address.size()>0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
                end_lat_test.setText(""+location.getLatitude());
                end_long_test.setText(""+location.getLongitude());
            }
            else
            {
                Toast.makeText(con,"Please enter correct address",Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    @Override
    public int getItemCount() {

        return geofencelist.size();
    }

    public Address getAddress1(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(con, Locale.getDefault());

        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses.size()>0)
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
    //get full address
    public void getAddressDestination() {

        Address locationAddress = getAddress1(CreateGeofenceActivity.lat, CreateGeofenceActivity.longi);
        // Address locationAddress=getAddress(-8.923255,13.194977);
        if (locationAddress != null) {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            String currentLocation;

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;

                Log.d("currentLocation", "" + address);



                end_geofence_address.setText(address);


                // Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();
            }

        }

    }
    //get full address
    public void getAddress() {

        Address locationAddress = getAddress1(CreateGeofenceActivity.lat, CreateGeofenceActivity.longi);
        // Address locationAddress=getAddress(-8.923255,13.194977);
        if (locationAddress != null) {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            String currentLocation;

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;

                Log.d("currentLocation", "" + address);
                geofence_address.setText(address);
                // Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();
            }

        }

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        dialog.dismiss();
        progressDialog.dismiss();

        getgeofencelist();
        Toast.makeText(con,"Geofence Updated successfully",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpFailure(String msg) {
        dialog.dismiss();
        progressDialog.dismiss();
        Toast.makeText(con,"Please try again",Toast.LENGTH_SHORT).show();
    }

    private void getgeofencelist() {
        if (NetworkUtil.isOnline(con)) {
            geofence_progressbar.setVisibility(View.VISIBLE);
            initGetgeofencetAPIResources(employid, PrefUtils.getKey(con, AppConstants.Clientid),"1");
        } else {
            Toast.makeText(con, con.getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
    private void initGetgeofencetAPIResources(String userid, String clientid,String adminid) {
        GetAlEmployePresenterImpl getAllfuelPresenter = new GetAlEmployePresenterImpl(this);
        getAllfuelPresenter.callApi(AppConstants.Geofence_List, userid,clientid,adminid,PrefUtils.getKey(con,AppConstants.Api_Token));
    }

    @Override

    public void onGetEmployeListSuccess(EmployeResponse employerResponse) {
        try {
            if (geofencelist1 == null) {
                geofencelist1 = new ArrayList<>();
                geofencelist = new ArrayList<>();
                Geofencedata=new ArrayList();
            }
            geofencelist1.clear();
            geofencelist.clear();
            Geofencedata.clear();
            geofence_progressbar.setVisibility(View.GONE);
            geofencelist1.addAll(employerResponse.getSupplierData());
            if(employerResponse.getGeofencedatarData()!=null)
            {
                Geofencedata.addAll(employerResponse.getGeofencedatarData());
            }

            if(geofencelist1.size()>0)
            {

                for (int i=geofencelist1.size()-1;i>=0;i--)
                {
                    EmployeData data=new EmployeData();
                    if(geofencelist1.get(i).getIsactive().equals("1")) {

                    String gg=geofencelist1.get(i).getGeofencename();
                   // data.setSelected(false);
                    data.setGeofencename(geofencelist1.get(i).getGeofencename());
                    data.setGeofence_latitude(geofencelist1.get(i).getGeofence_latitude());
                    data.setGeofence_longitude(geofencelist1.get(i).getGeofence_longitude());
                    data.setAddress(geofencelist1.get(i).getAddress());
                    data.setRadius(geofencelist1.get(i).getRadius());
                    data.setId(geofencelist1.get(i).getId());
                    data.setType(geofencelist1.get(i).getType());
                    data.setEnd_latitude(geofencelist1.get(i).getEnd_latitude());
                    data.setEnd_longitude(geofencelist1.get(i).getEnd_longitude());
                    data.setEnd_geofence_address(geofencelist1.get(i).getEnd_geofence_address());
                    data.setIsgeonotification(geofencelist1.get(i).getIsgeonotification());
                    Log.d("Geofencedata",""+Geofencedata.size());
                    int a=Geofencedata.size();
                    if(Geofencedata.size()>0)
                    {
                        for (int j=0;j<Geofencedata.size();j++)
                        {
                            String gid=geofencelist1.get(i).getId();
                           // Log.d("geofencelist1",""+geofencelist1.size());
                          //  Log.d("geofenceassign",""+Geofencedata.get(j).getGeofence_id());
                            if(gid.equalsIgnoreCase(Geofencedata.get(j).getGeofence_id())){
                                data.setAssigngeofence("true");
                                data.setSelected(true);
                               break;
                            }
                            else
                            {
                               data.setSelected(false);
                                data.setAssigngeofence("false");
                            }
                        }

                    }

                    geofencelist.add(data);
                }}

                if (geofencelist.size() > 0) {
                    Log.d("geofencelist", "" + geofencelist.size());
                    notifyDataSetChanged();
                    saveArray();
                } else {
                    notifyDataSetChanged();
                    Toast.makeText(con, "Geofence not found", Toast.LENGTH_LONG).show();
                }

            }


        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }

    }
    public  void saveArray()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("Status_size", geofencelist.size());
        for(int i=0;i<geofencelist.size();i++)
        {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_gname" + i, geofencelist.get(i).getGeofencename());
            mEdit1.putString("Status_lat" + i, geofencelist.get(i).getGeofence_latitude());
            mEdit1.putString("Status_long" + i, geofencelist.get(i).getGeofence_longitude());
            mEdit1.putString("Status_radius" + i, geofencelist.get(i).getRadius());
            mEdit1.putString("Status_address" + i, geofencelist.get(i).getAddress());
            mEdit1.putString("Status_geofenceid" + i, geofencelist.get(i).getId());
        }
        mEdit1.commit();

    }
    @Override
    public void onGetEmployeListListFailure(String msg) {
        geofence_progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onSignUpSuccessOut(SignUpResponse signUpResponse) {
        dialog.dismiss();
        progressDialog.dismiss();
        getgeofencelist();
        Toast.makeText(con,"Geofence Remove successfully",Toast.LENGTH_SHORT).show();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.checkbox.setChecked(false);
        super.onViewRecycled(holder);
    }


    @Override
    public void onSignUpFailureOut(String msg) {
        dialog.dismiss();
        progressDialog.dismiss();
        Toast.makeText(con,"Please try again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpSuccessadminnotification(SignUpResponse signUpResponse) {
        if(isassign==true)
        {
            if(geofencearray.size()>0) {
                geofencearray.clear();
            }
            Toast.makeText(con,"Geofence assign successfully",Toast.LENGTH_SHORT).show();
        } else if (isdassign==true)
        {
            if(unassign_geofencearray.size()>0) {
                unassign_geofencearray.clear();
            }
            Toast.makeText(con,"Geofence remove successfully",Toast.LENGTH_SHORT).show();
        }
        getgeofencelist();
        progressDialog.dismiss();
    }

    @Override
    public void onSignUpFailureadminnotification(String msg) {
        progressDialog.dismiss();
        Toast.makeText(con,"Please try again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpUpdateSuccess(SignUpResponse signUpResponse) {

          sucess=true;
        PrefUtils.storeKey(con, AppConstants.Geofenceenable,isnotication);

    }

    @Override
    public void onSignUpUpdateFailure(String msg) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * ButterKnife Code
         **/

        @BindView(R.id.geofence_name)
        TextView geofence_name;
        @BindView(R.id.edit_geofence)
        RelativeLayout edit_geofence;
        @BindView(R.id.geofence_location)
        RelativeLayout geofence_location;
       @BindView(R.id.geofence_enable_single)
        SwitchButton geofence_enable;
        @BindView(R.id.checkbox)
        CheckBox checkbox;

        /**
         * ButterKnife Code
         **/

        Dialog dialogview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
