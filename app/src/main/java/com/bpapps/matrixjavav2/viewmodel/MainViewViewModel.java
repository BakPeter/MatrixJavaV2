package com.bpapps.matrixjavav2.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.bpapps.matrixjavav2.App;
import com.bpapps.matrixjavav2.R;
import com.bpapps.matrixjavav2.model.Model;
import com.bpapps.matrixjavav2.model.datamodel.DataListCat;
import com.bpapps.matrixjavav2.model.datamodel.DataListObject;
import com.bpapps.matrixjavav2.model.datamodel.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainViewViewModel extends ViewModel implements Model.IOnDataConnectivityChangedListener, Model.IDataLoadListener {

    private Model mModel = Model.getInstance();

    private boolean mConnectivityStatus;

    private IOnDataConnectivityChangedListener mConnectivityChangedCallBack = null;
    private IDataLoadListener mDataLoadCallback = null;

    public List<DataListObject> getItemsByCategory(int categoryInd) {
        return mModel.getItemsByCategory(categoryInd);
    }

    public String getCategoryTitle(int categoryInd) {
        String categoryTitle = mModel.getCategoryTitle(categoryInd);

        if (categoryTitle == null) {
            categoryTitle = App.getAppContext().getResources().getString(R.string.default_category_title) + " " + categoryInd;
        }

        return categoryTitle;
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (mConnectivityStatus != isConnected) {
            mConnectivityStatus = isConnected;

            if (mConnectivityChangedCallBack != null) {
                mConnectivityChangedCallBack.onConnectivityChanged(mConnectivityStatus);
            }
        }
    }

    public boolean getConnectivityStatus() {
        mConnectivityStatus = mModel.getConnectivityStatus();
        return mConnectivityStatus;
    }

    @Override
    public void onLoadSuccess(Result result) {
        List<DataListCat> categories = result.getDataObject().getDataListCat();
        HashMap<Integer, ArrayList<DataListObject>> items = new HashMap<>(categories.size());

        for (DataListCat cat :
                categories) {
            items.put(cat.getCatId(), new ArrayList<DataListObject>());
        }

        for (DataListObject item :
                result.getDataObject().getDataListObject()) {
            items.get(item.getCatId()).add(item);
        }

        mDataLoadCallback.onLoadSuccess(categories, items);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        if (mDataLoadCallback != null) {
            mDataLoadCallback.onLoadFailure(error);
        }
    }

    public void registerForConnectivityUpdateListener(@NonNull IOnDataConnectivityChangedListener callBack) {
        mModel.registerForConnectivityUpdateListener(this);
        mConnectivityChangedCallBack = callBack;
    }

    public void unRegisterForConnectivityUpdateListener() {
        mModel.unRegisterForConnectivityUpdateListener();
        mConnectivityChangedCallBack = null;
    }

    public void registerForDataLoadListener(IDataLoadListener callback) {
        mDataLoadCallback = callback;
        mModel.registerForDataLoadListener(this);
    }

    public void unRegisterForDataLoadListener() {
        mDataLoadCallback = null;
        mModel.unRegisterForDataLoadListener();
    }

    public interface IOnDataConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);

    }

    public interface IDataLoadListener {
        void onLoadSuccess(List<DataListCat> categories, HashMap<Integer, ArrayList<DataListObject>> items);

        void onLoadFailure(Throwable error);
    }
}