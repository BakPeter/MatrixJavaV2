package com.bpapps.matrixjavav2.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bpapps.matrixjavav2.R;
import com.bpapps.matrixjavav2.model.datamodel.DataListObject;

import java.util.ArrayList;
import java.util.List;

public class ItemsShowerAdapter extends RecyclerView.Adapter<ItemsShowerAdapter.ItemViewHolder> {

    private List<DataListObject> mDataSet;
    private IOnItemClickListener mOnItemClickListenerCallback;

    public ItemsShowerAdapter(List<DataListObject> dataSet, IOnItemClickListener onItemClickListenerCallback) {
        super();
        mDataSet = dataSet;
        mOnItemClickListenerCallback = onItemClickListenerCallback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_shower, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final DataListObject item = mDataSet.get(position);

        if (item.getImgBitmap() != null) {
            holder.mIvListItem.setImageBitmap(item.getImgBitmap());
        }
        holder.mTvTitle.setText(item.getTitle());
        holder.mTvSubTitle.setText(item.getsTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListenerCallback != null) {
                    mOnItemClickListenerCallback.onItemClick(item.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void changeDataSet(List<DataListObject> newDataSet) {
        mDataSet.clear();
        mDataSet.addAll(newDataSet);
        notifyDataSetChanged();
    }

    public interface IOnItemClickListener {
        void onItemClick(int id);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView mIvListItem;
        private AppCompatTextView mTvTitle;
        private AppCompatTextView mTvSubTitle;

        public ItemViewHolder(@NonNull View view) {
            super(view);

            mIvListItem = view.findViewById(R.id.ivListItem);
            mTvTitle = view.findViewById(R.id.tvListItemTitle);
            mTvSubTitle = view.findViewById(R.id.tvListItemSubTitle);
        }
    }
}