package com.bpapps.matrixjavav2.view.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bpapps.matrixjavav2.R;
import com.bpapps.matrixjavav2.model.datamodel.DataListCat;
import com.bpapps.matrixjavav2.model.datamodel.DataListObject;
import com.bpapps.matrixjavav2.model.datamodeldynamicui.DataSetHolder;
import com.bpapps.matrixjavav2.view.adapters.ItemsShowerAdapter;
import com.bpapps.matrixjavav2.view.adapters.ItemsShowerDynamicUIAdapter;
import com.bpapps.matrixjavav2.viewmodel.MainViewViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class MainViewDynamicUIFragment extends Fragment implements MainViewViewModel.IOnDataConnectivityChangedListener, ItemsShowerAdapter.IOnItemClickListener, MainViewViewModel.IDataLoadListener {
    private static final String TAG = "TAG.MainViewFragment";

    public static final int REQUEST_CONNECTIVITY_SETTINGS = 17;

    private MainViewViewModel mViewModel;

    private RecyclerView mRvDynamicUI;
    private ItemsShowerDynamicUIAdapter mAdapter;
    protected AlertDialog mDialogNoConnectivity = null;

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
        return inflater.inflate(R.layout.main_view_dynamic_u_i_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connectivityStatusChanged(mViewModel.getConnectivityStatus());

        mRvDynamicUI = view.findViewById(R.id.rvDynamicUI);
        mRvDynamicUI.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new ItemsShowerDynamicUIAdapter( new DataSetHolder(), this, requireContext());
        mRvDynamicUI.setAdapter(mAdapter);
    }

    private void connectivityStatusChanged(boolean isConnected) {
        if (!isConnected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("No internet connection!!!")
                    .setMessage("The needs an internet connection to run.\nPress 'SETTINGS' to go to internet settings and connect.\nPress 'FINISH' to exit the app.")
                    .setCancelable(false)
                    .setPositiveButton("CONNECT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Log.d(TAG, "go to internet settings");
                            startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), REQUEST_CONNECTIVITY_SETTINGS);
                        }
                    })
                    .setNegativeButton("FINISH", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            requireActivity().finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
//                            requireActivity().finishAndRemoveTask();
                            requireActivity().finishAndRemoveTask();
                            System.exit(0);
                        }
                    });

            mDialogNoConnectivity = builder.create();
            mDialogNoConnectivity.show();
        } else {
            if (mDialogNoConnectivity != null) {
                mDialogNoConnectivity.dismiss();
                mDialogNoConnectivity = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.registerForConnectivityUpdateListener(this);
        mViewModel.registerForDataLoadListener(this);
    }

    @Override
    public void onPause() {
        mViewModel.unRegisterForConnectivityUpdateListener();
        mViewModel.unRegisterForDataLoadListener();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        connectivityStatusChanged(isConnected);
//        Toast.makeText(requireContext(), "onConnectivityChanged() + " + (isConnected ? "connected" : "not connected"), Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onConnectivityChanged() + " + (isConnected ? "connected" : "not connected"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CONNECTIVITY_SETTINGS) {
            Log.d(TAG, "onActivityResult: requestCode == REQUEST_CONNECTIVITY_SETTINGS");
            connectivityStatusChanged(mViewModel.getConnectivityStatus());
        } else {
            Log.d(TAG, "onActivityResult: requestCode != REQUEST_CONNECTIVITY_SETTINGS");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(int id) {
        //TODO show chosen item
        Log.d(TAG, "onItemClick: id = " + id);
    }

    @Override
    public void onLoadSuccess(DataSetHolder dataSet) {
        mAdapter.changeDataSet(dataSet);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        Log.d(TAG, "onLoadFailure: " + error.getMessage());
    }
}