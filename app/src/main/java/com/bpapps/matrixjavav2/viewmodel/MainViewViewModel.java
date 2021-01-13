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
    private List<DataListCat> mCategories = null;
    private HashMap<Integer, ArrayList<DataListObject>> mItemsByCategories = null;

    private IOnDataConnectivityChangedListener mConnectivityChangedCallBack = null;
    private IDataLoadListener mDataLoadCallback = null;

    public List<DataListObject> getItemsByCategory(int categoryPosition) {
        if (mItemsByCategories == null) {
            mItemsByCategories = new HashMap<>();
        }

        List<DataListObject> items = mModel.getItems();
        if (items != null && items.size() > 0) {
            Integer currCategoryId;
            for (int i = 0; i < items.size(); i++) {
                currCategoryId = items.get(i).getCatId();

                if (!mItemsByCategories.containsKey(currCategoryId)) {
                    mItemsByCategories.put(currCategoryId, new ArrayList<DataListObject>());
                }

                mItemsByCategories.get(currCategoryId).add(items.get(i));
            }
        } else {
            return new ArrayList<>();
        }

        return mItemsByCategories.get(mCategories.get(categoryPosition).getCatId());
    }

    public String getCategoryTitle(int categoryInd) {
        if (mCategories == null) {
            List<DataListCat> categories = mModel.getCategories();
            if (categories != null) {
                mCategories = categories;
            }
        }

        String categoryTitle;
        if (mCategories != null && mCategories.size() != 0) {
            if (categoryInd < 0 || (categoryInd > mCategories.size() - 1)) {
                categoryTitle = App.getAppContext().getResources().getString(R.string.default_category_title) + " " + categoryInd;
            } else {
                categoryTitle = mCategories.get(categoryInd).getCategoryTitle();
            }
        } else {
            categoryTitle = App.getAppContext().getResources().getString(R.string.default_category_title) + " " + categoryInd;

//            categoryTitle = App.getAppContext().getResources().getString(R.string.default_category_title);
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
        updateCategories(result.getDataObject().getDataListCat());
        updateItems(result.getDataObject().getDataListObject());

        mDataLoadCallback.onLoadSuccess(mCategories, mItemsByCategories);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        if (mDataLoadCallback != null) {
            mDataLoadCallback.onLoadFailure(error);
        }
    }

    private void updateItems(List<DataListObject> items) {
        Integer currId;
        for (int i = 0; i < mCategories.size(); i++) {
            currId = mCategories.get(i).getCatId();
            if (mItemsByCategories.containsKey(currId)) {
                mItemsByCategories.clear();
            } else {
                mItemsByCategories.put(currId, new ArrayList<DataListObject>());
            }
        }

        for (DataListObject item :
                items) {
            if (!mItemsByCategories.containsKey(item.getCatId())) {
                mItemsByCategories.put(item.getCatId(), new ArrayList<DataListObject>());
            }

            mItemsByCategories.get(item.getCatId()).add(item);
        }
    }

    private void updateCategories(List<DataListCat> categories) {
        if (mCategories != null) {
            mCategories.clear();
            mCategories.addAll(categories);
        } else {
            mCategories = new ArrayList<>(categories);
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