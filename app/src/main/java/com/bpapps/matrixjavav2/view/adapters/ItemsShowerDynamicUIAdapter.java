package com.bpapps.matrixjavav2.view.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bpapps.matrixjavav2.R;
import com.bpapps.matrixjavav2.model.datamodel.DataListCat;
import com.bpapps.matrixjavav2.model.datamodeldynamicui.DataSetHolder;
import com.bpapps.matrixjavav2.view.snaphelpers.GravitySnapHelper;

public class ItemsShowerDynamicUIAdapter extends RecyclerView.Adapter<ItemsShowerDynamicUIAdapter.DynamicViewHolder> implements ItemsShowerAdapter.IOnItemClickListener {

    private DataSetHolder mDataSet;
    private ItemsShowerAdapter.IOnItemClickListener mOnItemClickListenerCallback;
    private Context mContext;

    public ItemsShowerDynamicUIAdapter(
            DataSetHolder mDataSet,
            ItemsShowerAdapter.IOnItemClickListener mOnItemClickListenerCallback,
            Context context) {
        this.mDataSet = mDataSet;
        this.mOnItemClickListenerCallback = mOnItemClickListenerCallback;
        this.mContext = context;
    }

    @NonNull
    @Override
    public DynamicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_dynamic_view, parent, false);

        return new DynamicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicViewHolder holder, int position) {
        DataListCat category = mDataSet.getCategories().get(position);
        holder.tvCategoryName.setText(category.getCategoryTitle());
        holder.rvDynamicUIItemsShower.setAdapter(new ItemsShowerAdapter(mDataSet.getItems(category), this));
        holder.rvDynamicUIItemsShower.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(holder.rvDynamicUIItemsShower);
    }

    @Override
    public int getItemCount() {
        return mDataSet.getCategories().size();
    }

    public void changeDataSet(DataSetHolder newDataSet) {
        mDataSet = newDataSet;
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int id) {
        if (mOnItemClickListenerCallback != null) {
            mOnItemClickListenerCallback.onItemClick(id);
        }
    }

    public class DynamicViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvCategoryName;
        private RecyclerView rvDynamicUIItemsShower;

        public DynamicViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategoryName = itemView.findViewById(R.id.tvDynamicUICategoryName);
            rvDynamicUIItemsShower = itemView.findViewById(R.id.rvDynamicUIItemsShower);
        }
    }

    public interface IOnItemClickListener {
        void onItemClick(int id);
    }
}
