package com.bpapps.matrixjavav2.model;

import androidx.annotation.NonNull;

import com.bpapps.matrixjavav2.model.repository.Repository;

public class Model implements Repository.IOnDataConnectivityChangedListener {
    private static Model sInstance = null;

    private Repository mRepository = Repository.getInstance();

    private IOnDataConnectivityChangedListener mConnectivityChangedCallBack = null;

    public static Model getInstance() {
        if (sInstance == null) {
            sInstance = new Model();
        }

        return sInstance;
    }

    private Model() {
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (mConnectivityChangedCallBack != null) {
            mConnectivityChangedCallBack.onConnectivityChanged(isConnected);
        }
    }

    public boolean getConnectivityStatus() {
        return mRepository.getConnectivityStatus();
    }

    public interface IOnDataConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);
    }

    public void registerForConnectivityUpdateListener(@NonNull IOnDataConnectivityChangedListener callBack) {
        mRepository.registerForConnectivityUpdateListener(this);
        mConnectivityChangedCallBack = callBack;
    }

    public void unRegisterForConnectivityUpdateListener() {
        mRepository.unRegisterForConnectivityUpdateListener();
        mConnectivityChangedCallBack = null;
    }
}
