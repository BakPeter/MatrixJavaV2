package com.bpapps.matrixjavav2.view;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bpapps.matrixjavav2.R;
import com.bpapps.matrixjavav2.viewmodel.MainViewViewModel;

public class MainViewFragment extends Fragment implements MainViewViewModel.IOnDataConnectivityChangedListener {
    private static final String TAG = "TAG.MainViewFragment";

    private MainViewViewModel mViewModel;

    public static MainViewFragment newInstance() {
        return new MainViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MainViewViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isConnected = mViewModel.getConnectivityStatus();
        Toast.makeText(requireContext(), isConnected ? "connected" : "not connected", Toast.LENGTH_SHORT).show();
        Log.d(TAG, isConnected ? "connected" : "not connected");
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.registerForConnectivityUpdateListener(this);
    }

    @Override
    public void onStop() {
        mViewModel.unRegisterForConnectivityUpdateListener();

        super.onStop();
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        Toast.makeText(requireContext(), isConnected ? "connected" : "not connected", Toast.LENGTH_SHORT).show();
        Log.d(TAG, isConnected ? "connected" : "not connected");
    }
}