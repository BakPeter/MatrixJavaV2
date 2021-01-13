package com.bpapps.matrixjavav2.model.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.bpapps.matrixjavav2.apputils.ConnectivityHandler;
import com.bpapps.matrixjavav2.model.datamodel.DataListCat;
import com.bpapps.matrixjavav2.model.datamodel.DataListObject;
import com.bpapps.matrixjavav2.model.datamodel.Result;
import com.bpapps.matrixjavav2.model.repository.assest.AssetDataFetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Repository implements ConnectivityHandler.IOnDataConnectivityChangedListener {
    private static final String TAG = "TAG.Repository";
    private static Repository sInstance;

    private Result mResult;
    private int mDownloadedItemsCount;

    private AssetDataFetcher mAssetDataFetcher = new AssetDataFetcher();
    private ConnectivityHandler mConnectivityHandler = new ConnectivityHandler();

    private IOnDataConnectivityChangedListener mConnectivityChangedCallBack = null;
    private IDataLoadListener mDataLoadCallBack = null;

    public static Repository getInstance() {
        if (sInstance == null) {
            sInstance = new Repository();
            sInstance.loadData();
        }

        return sInstance;
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            loadData();
        }

        if (mConnectivityChangedCallBack != null) {
            mConnectivityChangedCallBack.onConnectivityChanged(isConnected);
        }
    }

    public boolean getConnectivityStatus() {
        return mConnectivityHandler.getConnectivityStatus();
    }

    public List<DataListCat> getCategories() {
        if (mResult != null) {
            return mResult.getDataObject().getDataListCat();
        }
        return null;
    }

    public List<DataListObject> getItems() {
        if (mResult != null) {
            return mResult.getDataObject().getDataListObject();
        } else {
            return null;
        }
    }

    private void loadData() {
        if (mResult == null || mDownloadedItemsCount != mResult.getDataObject().getDataListObject().size()) {
            mAssetDataFetcher.loadData(new AssetDataFetcher.IDataLoadListener() {
                @Override
                public void onLoadSuccess(Result result) {
//                    Log.d(TAG, "onLoadSuccess: ");
                    mResult = result;
                    if (mConnectivityHandler.getConnectivityStatus()) {
                        downloadPictures();
                    } else {
                        if (mDataLoadCallBack != null) {
                            mDataLoadCallBack.onLoadSuccess(result);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable error) {
//                    Log.d(TAG, "onFailure: ");
                    if (mDataLoadCallBack != null) {
                        mDataLoadCallBack.onLoadFailure(error);
                    }
                }
            });
        }
    }

    private void downloadPictures() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<DataListObject> items = mResult.getDataObject().getDataListObject();
                for (int i = 0; i < items.size(); i++) {
                    final int currInd = i;
                    try {
                        URL url = new URL(items.get(i).getImageUrl());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(input);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                mResult.getDataObject().getDataListObject().get(currInd).setImgBitmap(bitmap);
                                mDownloadedItemsCount++;

                                if (currInd == items.size() - 1) {
                                    if (mDataLoadCallBack != null) {
                                        mDataLoadCallBack.onLoadSuccess(mResult);
                                    }
                                }
                            }
                        });
                    } catch (IOException error) {
                        if (!mConnectivityHandler.getConnectivityStatus()) {
                        }
                        if (currInd == items.size() - 1) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    mDataLoadCallBack.onLoadSuccess(mResult);
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    public void registerForDataLoadListener(IDataLoadListener callback) {
        mDataLoadCallBack = callback;
    }

    public void unRegisterForDataLoadListener() {
        mDataLoadCallBack = null;
    }

    public void registerForConnectivityUpdateListener(@NonNull IOnDataConnectivityChangedListener callBack) {
        mConnectivityHandler.registerForConnectivityUpdateListener(this);
        mConnectivityChangedCallBack = callBack;
    }

    public void unRegisterForConnectivityUpdateListener() {
        mConnectivityHandler.unRegisterForConnectivityUpdateListener();
        mConnectivityChangedCallBack = null;
    }

    public List<DataListObject> getItemsByCategory(int categoryInd) {
        List<DataListObject> retVal = new ArrayList<>();

        if (mResult != null) {
            int catId = mResult.getDataObject().getDataListCat().get(categoryInd).getCatId();

            for (DataListObject item :
                    mResult.getDataObject().getDataListObject()) {
                if (item.getCatId() == catId) {
                    retVal.add(item);
                }
            }
        }

        return retVal;
    }

    public String getCategoryTitle(int categoryInd) {
        String retVal = null;
        if (mResult != null) {
            retVal = mResult.getDataObject().getDataListCat().get(categoryInd).getCategoryTitle();
        }

        return retVal;
    }


    public interface IOnDataConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);
    }

    public interface IDataLoadListener {
        void onLoadSuccess(Result result);

        void onLoadFailure(Throwable error);
    }
}