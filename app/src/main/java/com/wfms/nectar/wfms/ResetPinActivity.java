package com.wfms.nectar.wfms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.SignUpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nectar on 12-09-2018.
 */

public class ResetPinActivity extends AppCompatActivity implements SignUpView {
    /** ButterKnife Code **/
    @BindView(R.id.RtextInputEditTextoldpassword)
    EditText mRtextInputEditTextoldpassword;
    @BindView(R.id.RtextInputEditTextpassword)
    EditText mRtextInputEditTextpassword;
    @BindView(R.id.RtextInputEditTextconfirmpassword)
    EditText mRtextInputEditTextconfirmpassword;
    @BindView(R.id.submit_button)
    androidx.appcompat.widget.AppCompatButton msubmit_button;
    @BindView(R.id.Rusername_inputlayout)
    TextInputLayout Rusername_inputlayout;
    @BindView(R.id.Rpassword_inputlayout)
    TextInputLayout Rpassword_inputlayout;
    @BindView(R.id.back_layout)
    RelativeLayout back;
    @BindView(R.id.Rconfimrpassword_inputlayout)
    TextInputLayout Rconfimrpassword_inputlayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /** ButterKnife Code **/
    ProgressDialog dialog;
    String userid;
    Resources resources;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.resetpin_layout);
        ButterKnife.bind(this);
        resources=getResources();
        userid=PrefUtils.getKey(ResetPinActivity.this,AppConstants.UserID);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("Reset Password");
    }
    @OnClick(R.id.back_layout)
    public void onbackClick() {
        finish();
    }
    @OnClick(R.id.submit_button)
    public void onClick() {
       if (TextUtils.isEmpty(mRtextInputEditTextoldpassword.getText().toString()))
        {
            mRtextInputEditTextoldpassword.setError(resources.getString(R.string.enter_oldpassword));
            mRtextInputEditTextoldpassword.requestFocus();

        }
        else if (TextUtils.isEmpty(mRtextInputEditTextpassword.getText().toString()))
        {
            mRtextInputEditTextpassword.setError(resources.getString(R.string.enter_newpassword));
            mRtextInputEditTextpassword.requestFocus();

        }
        else if (TextUtils.isEmpty(mRtextInputEditTextconfirmpassword.getText().toString()))
        {
            mRtextInputEditTextconfirmpassword.setError(resources.getString(R.string.enter_password));
            mRtextInputEditTextconfirmpassword.requestFocus();

        }
        else if(!mRtextInputEditTextconfirmpassword.getText().toString().equals(mRtextInputEditTextpassword.getText().toString()))
        {
            mRtextInputEditTextconfirmpassword.setError(resources.getString(R.string.password_not_match));
            mRtextInputEditTextconfirmpassword.requestFocus();
        }
        else
        {
            submit();
        }

    }

    private void submit() {

        if (NetworkUtil.isOnline(ResetPinActivity.this)){
            dialog=new ProgressDialog(ResetPinActivity.this,R.style.AppCompatAlertDialogStyle);
            dialog.setMessage(resources.getString(R.string.loading));
            dialog.show();
            initLoginAPIResources(mRtextInputEditTextoldpassword.getText().toString(),mRtextInputEditTextpassword.getText().toString(),PrefUtils.getKey(ResetPinActivity.this,AppConstants.Clientid),PrefUtils.getKey(ResetPinActivity.this,AppConstants.Api_Token));

        }else {
            Toast.makeText(ResetPinActivity.this,resources.getString(R.string.internet_connection),Toast.LENGTH_SHORT).show();

        }
    }
    private void initLoginAPIResources(String name, String password,String clientid,String apitoken) {
        SignUpPresenterImpl loginPresenter = new SignUpPresenterImpl(this);
        loginPresenter.callApi(AppConstants.Reset_Pin, name,password,userid,clientid,apitoken);
    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        dialog.dismiss();
        Intent i = new Intent(ResetPinActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onSignUpFailure(String msg) {
        dialog.dismiss();
        Toast.makeText(ResetPinActivity.this,resources.getString(R.string.check_oldpassword),Toast.LENGTH_SHORT).show();
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent i= new Intent(ResetPinActivity.this,LoginActivity.class);
                startActivity(i);

                return true;
            case R.id.reset:
                Intent i1= new Intent(ResetPinActivity.this,ResetPinActivity.class);
                startActivity(i1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
