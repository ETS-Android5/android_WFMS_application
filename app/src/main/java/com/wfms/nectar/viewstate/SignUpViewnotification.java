package com.wfms.nectar.viewstate;


import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;

public interface SignUpViewnotification {
    public void onSignUpSuccessnotification(SignUpResponse signUpResponse);
    public void onSignUpFailurenotification(String msg);
}
