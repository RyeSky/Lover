package com.skye.lover.view.exrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public abstract class ExRcyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected View customHeaderView = null;

    protected View customFooterView = null;

    protected AdapterView.OnItemClickListener mOnItemClickListener;

    protected AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    public abstract RecyclerView.ViewHolder createViewHolder();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == VIEW_TYPES.HEADER && customHeaderView != null) {
            return new BaseViewHolder(customHeaderView);
        } else if (viewType == VIEW_TYPES.FOOTER && customFooterView != null) {
            return new BaseViewHolder(customFooterView);
        } else {
            return createViewHolder();
        }
    }

    public int getHeaderCount() {
        return customHeaderView == null ? 0 : 1;
    }

    public int getFooterCount() {
        return customFooterView == null ? 0 : 1;
    }

    public abstract Object get(int position);

    @Override
    public abstract int getItemCount();

    @Override
    public int getItemViewType(int position) {
        if (customFooterView != null && position == getItemCount() - 1) {
            return VIEW_TYPES.FOOTER;
        } else if (customHeaderView != null && position == 0) {
            return VIEW_TYPES.HEADER;
        } else {
            if (customHeaderView != null) {
                return super.getItemViewType(position - 1);
            }
            return super.getItemViewType(position);
        }
    }

    public abstract void onBindCustomViewHolder(
            RecyclerView.ViewHolder viewHolder, int position);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,
                                 int position) {
        if ((customHeaderView != null && position == 0)
                || (customFooterView != null && position == getItemCount() - 1)) {
        } else {
            if (customHeaderView != null) {
                position--;
            }
            onBindCustomViewHolder(viewHolder, position);

            final int pos = position;
            if (mOnItemClickListener != null) {
                viewHolder.itemView
                        .setOnClickListener((View v) -> {
                            mOnItemClickListener.onItemClick(null,
                                    viewHolder.itemView, pos, pos);
                        });
            }
            if (mOnItemLongClickListener != null) {
                viewHolder.itemView
                        .setOnLongClickListener((View view) -> mOnItemLongClickListener
                                .onItemLongClick(null,
                                        viewHolder.itemView, pos, pos));
            }
        }
    }

    public class VIEW_TYPES {

        public static final int HEADER = 7;

        public static final int FOOTER = 8;
    }

    protected class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

}
