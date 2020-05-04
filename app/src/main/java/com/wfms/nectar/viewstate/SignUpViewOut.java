package com.wfms.nectar.viewstate;


import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;

public interface SignUpViewOut {
    public void onSignUpSuccessOut(SignUpResponse signUpResponse);
    public void onSignUpFailureOut(String msg);
}
