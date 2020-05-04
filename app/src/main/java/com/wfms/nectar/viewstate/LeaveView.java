package com.wfms.nectar.viewstate;


import com.wfms.nectar.jsonModelResponses.Fuel.LeaveResponse;

public interface LeaveView {

    void onGeLeaveListSuccess(LeaveResponse leaveResponse);
    void onGetLeaveListListFailure(String msg);
}
