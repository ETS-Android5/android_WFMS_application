package com.wfms.nectar.interactor;

/**
 * Created by Abhishek on 27-Jul-17.
 */

public interface Interactor {
    void callApi(ApiInteractor apiInteractor, Object... args);
}
