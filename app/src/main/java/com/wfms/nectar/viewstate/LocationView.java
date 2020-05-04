package com.wfms.nectar.viewstate;


import com.wfms.nectar.jsonModelResponses.Fuel.LocationResponse;

public interface LocationView {

    void onGetLocationListSuccess(LocationResponse supplierResponse);
    void onGetlocationListFailure(String msg);
}
