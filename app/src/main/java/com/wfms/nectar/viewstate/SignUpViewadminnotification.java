package com.wfms.nectar.viewstate;


import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;

public interface SignUpViewadminnotification {
    public void onSignUpSuccessadminnotification(SignUpResponse signUpResponse);
    public void onSignUpFailureadminnotification(String msg);
}
