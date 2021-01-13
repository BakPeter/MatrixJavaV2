package com.bpapps.matrixjavav2.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.bpapps.matrixjavav2.model.Model;
import com.bpapps.matrixjavav2.model.repository.Repository;

public class MainViewViewModel extends ViewModel implements Model.IOnDataConnectivityChangedListener {

    private Model mModel = Model.getInstance();

    private IOnDataConnectivityChangedListener mConnectivityChangedCallBack = null;


    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (mConnectivityChangedCallBack != null) {
            mConnectivityChangedCallBack.onConnectivityChanged(isConnected);
        }
    }

    public boolean getConnectivityStatus() {
        return mModel.getConnectivityStatus();
    }

    public interface IOnDataConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);
    }

    public void registerForConnectivityUpdateListener(@NonNull IOnDataConnectivityChangedListener callBack) {
        mModel.registerForConnectivityUpdateListener(this);
        mConnectivityChangedCallBack = callBack;
    }

    public void unRegisterForConnectivityUpdateListener() {
        mModel.unRegisterForConnectivityUpdateListener();
        mConnectivityChangedCallBack = null;
    }
}