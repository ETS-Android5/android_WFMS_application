package com.wfms.nectar.viewstate;


import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;

public interface SignUpViewUpdate {
    public void onSignUpUpdateSuccess(SignUpResponse signUpResponse);
    public void onSignUpUpdateFailure(String msg);
}
