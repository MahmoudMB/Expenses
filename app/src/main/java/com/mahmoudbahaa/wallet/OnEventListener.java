package com.mahmoudbahaa.wallet;

import org.json.JSONException;

/**
 * Created by MahmoudBahaa on 19/01/2019.
 */


public interface OnEventListener<T> {

    public void onSuccess(T object) throws JSONException;
    public void onFailure(Exception e);

    public void FailureMessage(String m);

}
