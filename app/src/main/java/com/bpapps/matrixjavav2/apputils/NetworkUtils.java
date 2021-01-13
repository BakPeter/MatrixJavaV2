package com.bpapps.matrixjavav2.apputils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bpapps.matrixjavav2.App;

public class NetworkUtils {
    private Context mContext;

    private IOnDataConnectivityChangedListener mCallBack;

    private ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            if (mCallBack != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onConnectivityChanged(true);
                    }
                });
            }
        }

        @Override
        public void onLost(@NonNull Network network) {
            if (mCallBack != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onConnectivityChanged(false);
                    }
                });
            }
        }
    };


    public NetworkUtils(@NonNull Context context) {
        mContext = context;
    }

    public boolean isConnected() {
        ConnectivityManager cm = ContextCompat.getSystemService(mContext, ConnectivityManager.class);

        if (cm != null) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

            if (nc != null) {
                if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true;
                } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                } else return nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public interface IOnDataConnectivityChangedListener {
        void onConnectivityChanged(boolean isConnected);
    }

    public void registerForConnectivityUpdateListener(@NonNull IOnDataConnectivityChangedListener callBack) {
        ConnectivityManager cm = ContextCompat.getSystemService(App.getAppContext(), ConnectivityManager.class);

        if (cm != null) {
            cm.registerNetworkCallback(new NetworkRequest.Builder().build(), mNetworkCallback);
        }
        mCallBack = callBack;
    }

    public void unRegisterForConnectivityUpdateListener() {
        ConnectivityManager cm = ContextCompat.getSystemService(App.getAppContext(), ConnectivityManager.class);
        if (cm != null) {
            cm.unregisterNetworkCallback(mNetworkCallback);
        }
        mCallBack = null;
    }
}