package com.bpapps.matrixjavav2.model.repository;

import androidx.annotation.NonNull;

import com.bpapps.matrixjavav2.apputils.ConnectivityHandler;

public class Repository implements ConnectivityHandler.IOnDataConnectivityChangedListener {
    private static Repository sInstance;

    private ConnectivityHandler mConnectivityHandler = new ConnectivityHandler();

    private IOnDataConnectivityChangedListener mConnectivityChangedCallBack = null;

    public static Repository getInstance() {
        if (sInstance == null) {
            sInstance = new Repository();
        }

        return sInstance;
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (mConnectivityChangedCallBack != null) {
            mConnectivityChangedCallBack.onConnectivityChanged(isConnected);
        }
    }

    public boolean getConnectivityStatus() {
        return mConnectivityHandler.getConnectivityStatus();
    }

    public interface IOnDataConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);
    }

    public void registerForConnectivityUpdateListener(@NonNull IOnDataConnectivityChangedListener callBack) {
        mConnectivityHandler.registerForConnectivityUpdateListener(this);
        mConnectivityChangedCallBack = callBack;
    }

    public void unRegisterForConnectivityUpdateListener() {
        mConnectivityHandler.unRegisterForConnectivityUpdateListener();
        mConnectivityChangedCallBack = null;
    }
}
