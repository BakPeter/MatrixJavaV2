package com.bpapps.matrixjavav2.view.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bpapps.matrixjavav2.R;
import com.bpapps.matrixjavav2.model.datamodeldynamicui.DataSetHolder;
import com.bpapps.matrixjavav2.view.adapters.ItemsShowerAdapter;
import com.bpapps.matrixjavav2.view.snaphelpers.GravitySnapHelper;
import com.bpapps.matrixjavav2.viewmodel.MainViewViewModel;

public class MainViewFragment extends Fragment implements MainViewViewModel.IOnDataConnectivityChangedListener, ItemsShowerAdapter.IOnItemClickListener, MainViewViewModel.IDataLoadListener {
    private static final String TAG = "TAG.MainViewFragment";

    public static final int REQUEST_CONNECTIVITY_SETTINGS = 17;

    private MainViewViewModel mViewModel;

    protected AppCompatTextView tvCategory1;
    protected AppCompatTextView tvCategory2;
    protected AppCompatTextView tvCategory3;
    protected AppCompatTextView tvCategory4;
    protected AppCompatTextView tvCategory5;

    protected RecyclerView rvCategory1;
    protected RecyclerView rvCategory2;
    protected RecyclerView rvCategory3;
    protected RecyclerView rvCategory4;
    protected RecyclerView rvCategory5;
    protected ItemsShowerAdapter mAdapter1;
    protected ItemsShowerAdapter mAdapter2;
    protected ItemsShowerAdapter mAdapter3;
    protected ItemsShowerAdapter mAdapter4;
    protected ItemsShowerAdapter mAdapter5;

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
        return inflater.inflate(R.layout.main_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connectivityStatusChanged(mViewModel.getConnectivityStatus());

        tvCategory1 = view.findViewById(R.id.tvCategory1Name);
        tvCategory1.setText(mViewModel.getCategoryTitle(0));

        tvCategory2 = view.findViewById(R.id.tvCategory2Name);
        tvCategory2.setText(mViewModel.getCategoryTitle(1));

        tvCategory3 = view.findViewById(R.id.tvCategory3Name);
        tvCategory3.setText(mViewModel.getCategoryTitle(2));

        tvCategory4 = view.findViewById(R.id.tvCategory4Name);
        tvCategory4.setText(mViewModel.getCategoryTitle(3));

        tvCategory5 = view.findViewById(R.id.tvCategory5Name);
        tvCategory5.setText(mViewModel.getCategoryTitle(4));

        rvCategory1 = view.findViewById(R.id.rvCategory1);
        mAdapter1 = new ItemsShowerAdapter(mViewModel.getItemsByCategory(0), this);
        rvCategory1.setAdapter(mAdapter1);
        rvCategory1.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        rvCategory2 = view.findViewById(R.id.rvCategory2);
        mAdapter2 = new ItemsShowerAdapter(mViewModel.getItemsByCategory(1), this);
        rvCategory2.setAdapter(mAdapter2);
        rvCategory2.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        rvCategory3 = view.findViewById(R.id.rvCategory3);
        mAdapter3 = new ItemsShowerAdapter(mViewModel.getItemsByCategory(2), this);
        rvCategory3.setAdapter(mAdapter3);
        rvCategory3.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        rvCategory4 = view.findViewById(R.id.rvCategory4);
        mAdapter4 = new ItemsShowerAdapter(mViewModel.getItemsByCategory(3), this);
        rvCategory4.setAdapter(mAdapter4);
        rvCategory4.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        rvCategory5 = view.findViewById(R.id.rvCategory5);
        mAdapter5 = new ItemsShowerAdapter(mViewModel.getItemsByCategory(4), this);
        rvCategory5.setAdapter(mAdapter5);
        rvCategory5.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        new GravitySnapHelper(Gravity.START).attachToRecyclerView(rvCategory1);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(rvCategory2);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(rvCategory3);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(rvCategory4);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(rvCategory5);
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

//    @Override
//    public void onLoadSuccess(List<DataListCat> categories, HashMap<Integer, ArrayList<DataListObject>> items) {
//        tvCategory1.setText(categories.get(0).getCategoryTitle());
//        tvCategory2.setText(categories.get(1).getCategoryTitle());
//        tvCategory3.setText(categories.get(2).getCategoryTitle());
//        tvCategory4.setText(categories.get(3).getCategoryTitle());
//        tvCategory5.setText(categories.get(4).getCategoryTitle());
//
//        mAdapter1.changeDataSet(items.get(categories.get(0).getCatId()));
//        mAdapter2.changeDataSet(items.get(categories.get(1).getCatId()));
//        mAdapter3.changeDataSet(items.get(categories.get(2).getCatId()));
//        mAdapter4.changeDataSet(items.get(categories.get(3).getCatId()));
//        mAdapter5.changeDataSet(items.get(categories.get(4).getCatId()));
//    }


    @Override
    public void onLoadSuccess(DataSetHolder dataSet) {

    }

    @Override
    public void onLoadFailure(Throwable error) {
        Log.d(TAG, "onLoadFailure: " + error.getMessage());
    }
}