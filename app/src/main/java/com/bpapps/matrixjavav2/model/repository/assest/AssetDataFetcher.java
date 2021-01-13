package com.bpapps.matrixjavav2.model.repository.assest;

import android.os.Handler;
import android.os.Looper;

import com.bpapps.matrixjavav2.App;
import com.bpapps.matrixjavav2.model.datamodel.Result;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class AssetDataFetcher {

    private static final String JSON_FILE_NAME = "jsonObject.json";

    public void loadData(final IDataLoadListener dataLoadListenerCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = App.getAppContext().getAssets().open(JSON_FILE_NAME);
                    String json = new BufferedReader(new InputStreamReader(inputStream)).lines().parallel().collect(Collectors.joining(("\n")));

                    Gson gson = new Gson();
                    final Result result = gson.fromJson(json, Result.class);

                    if (dataLoadListenerCallback != null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                dataLoadListenerCallback.onLoadSuccess(result);
                            }
                        });
                    }
                } catch (IOException e) {
                    if (dataLoadListenerCallback != null) {
                        dataLoadListenerCallback.onFailure(e);
                    }
                }
            }
        }).start();
    }


    public interface IDataLoadListener {
        void onLoadSuccess(Result result);

        void onFailure(Throwable error);
    }
}
