package com.bpapps.matrixjavav2.model;

import androidx.annotation.NonNull;

import com.bpapps.matrixjavav2.model.datamodel.DataListCat;
import com.bpapps.matrixjavav2.model.datamodel.DataListObject;
import com.bpapps.matrixjavav2.model.datamodel.Result;
import com.bpapps.matrixjavav2.model.repository.Repository;

import java.util.List;

public class Model implements Repository.IOnDataConnectivityChangedListener, Repository.IDataLoadListener {
    private static Model sInstance = null;

    private Repository mRepository = Repository.getInstance();

    private IOnDataConnectivityChangedListener mConnectivityChangedCallBack = null;
    private IDataLoadListener mDataLoadCallback = null;

    private Model() {
    }

    public static Model getInstance() {
        if (sInstance == null) {
            sInstance = new Model();
        }

        return sInstance;
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (mConnectivityChangedCallBack != null) {
            mConnectivityChangedCallBack.onConnectivityChanged(isConnected);
        }
    }

    @Override
    public void onLoadSuccess(Result result) {
        if (mDataLoadCallback != null) {
            mDataLoadCallback.onLoadSuccess(result);
        }
    }

    @Override
    public void onLoadFailure(Throwable error) {
        if (mDataLoadCallback != null) {
            mDataLoadCallback.onLoadFailure(error);
        }
    }

    public boolean getConnectivityStatus() {
        return mRepository.getConnectivityStatus();
    }

    public List<DataListCat> getCategories() {
        return mRepository.getCategories();
    }

    public List<DataListObject> getItems() {
        return mRepository.getItems();
    }

    public void registerForConnectivityUpdateListener(@NonNull IOnDataConnectivityChangedListener callBack) {
        mRepository.registerForConnectivityUpdateListener(this);
        mConnectivityChangedCallBack = callBack;
    }

    public void unRegisterForConnectivityUpdateListener() {
        mRepository.unRegisterForConnectivityUpdateListener();
        mConnectivityChangedCallBack = null;
    }

    public void registerForDataLoadListener(IDataLoadListener callback) {
        mDataLoadCallback = callback;
        mRepository.registerForDataLoadListener(this);
    }

    public void unRegisterForDataLoadListener() {
        mDataLoadCallback = null;
        mRepository.unRegisterForDataLoadListener();
    }

    public List<DataListObject> getItemsByCategory(int categoryInd) {
        return mRepository.getItemsByCategory(categoryInd);
    }

    public String getCategoryTitle(int categoryInd) {
        return mRepository.getCategoryTitle(categoryInd);
    }


    public interface IOnDataConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);
    }

    public interface IDataLoadListener {
        void onLoadSuccess(Result result);

        void onLoadFailure(Throwable error);
    }
}
