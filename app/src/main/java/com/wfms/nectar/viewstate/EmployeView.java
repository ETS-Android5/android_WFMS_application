package com.wfms.nectar.viewstate;


import com.wfms.nectar.jsonModelResponses.Fuel.EmployeResponse;

public interface EmployeView {

    void onGetEmployeListSuccess(EmployeResponse supplierResponse);
    void onGetEmployeListListFailure(String msg);
}
