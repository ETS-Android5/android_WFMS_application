package com.wfms.nectar.viewstate;


import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;

public interface SignUpView {
    public void onSignUpSuccess(SignUpResponse signUpResponse);
    public void onSignUpFailure(String msg);
}
