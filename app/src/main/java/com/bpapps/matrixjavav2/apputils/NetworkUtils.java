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
    private boolean mIsWifiConnected;
    private boolean mIsTransporterConnected;

    private IOnDataConnectivityChangedListener mCallBack;

    private ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            updateConnectivityStatus();
            if (mCallBack != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onConnectivityChanged(isConnected());
                    }
                });
            }
        }

        @Override
        public void onLost(@NonNull Network network) {
            updateConnectivityStatus();
            if (mCallBack != null && !isConnected()) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onConnectivityChanged(isConnected());
                    }
                });
            }
        }
    };


    public NetworkUtils(@NonNull Context context) {
        mContext = context;
        updateConnectivityStatus();
    }

//    public boolean isConnected(Network network) {
//        ConnectivityManager cm = ContextCompat.getSystemService(mContext, ConnectivityManager.class);
//
//        if (cm != null) {
//            NetworkCapabilities nc = cm.getNetworkCapabilities(network);
//
//            if (nc != null) {
//                if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    return true;
//                } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    return true;
//                } else return nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }

    private boolean updateConnectivityStatus() {
        ConnectivityManager cm = ContextCompat.getSystemService(mContext, ConnectivityManager.class);

        if (cm != null) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

            if (nc != null) {
                mIsTransporterConnected = nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                mIsWifiConnected = nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            } else {
                mIsTransporterConnected = false;
                mIsWifiConnected = false;
            }
        } else {
            mIsTransporterConnected = false;
            mIsWifiConnected = false;
        }

        return isConnected();
    }

    private boolean isConnected() {
        return  mIsTransporterConnected || mIsWifiConnected;
    }

    public boolean getConnectivityStatus() {
        return updateConnectivityStatus();
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