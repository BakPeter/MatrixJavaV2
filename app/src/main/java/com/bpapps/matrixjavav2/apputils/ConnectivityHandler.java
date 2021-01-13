package com.bpapps.matrixjavav2.apputils;

import androidx.annotation.NonNull;

import com.bpapps.matrixjavav2.App;

public class ConnectivityHandler implements NetworkUtils.IOnDataConnectivityChangedListener {
    private static final String TAG = "TAG.ConnectivityHandler";

    private NetworkUtils mNetworkUtils = new NetworkUtils(App.getAppContext());
    private IOnDataConnectivityChangedListener mCallBack = null;

    public boolean getConnectivityStatus() {
        return mNetworkUtils.getConnectivityStatus();
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (mCallBack != null) {
            mCallBack.onConnectivityChanged(isConnected);
        }
    }


    public interface IOnDataConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);
    }

    public void registerForConnectivityUpdateListener(@NonNull IOnDataConnectivityChangedListener callBack) {
        mNetworkUtils.registerForConnectivityUpdateListener(this);
        mCallBack = callBack;
    }

    public void unRegisterForConnectivityUpdateListener() {
        mNetworkUtils.unRegisterForConnectivityUpdateListener();
        mCallBack = null;
    }
}