package com.nuoda.coorlib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nuoda.coorlib.viewholder.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public abstract class CommonNoComparableAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> data;
    private final  int itemLayoutId;

    public CommonNoComparableAdapter(int itemLayoutId, List<T> data, Context context) {
        this.itemLayoutId = itemLayoutId;
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public abstract void convert(ViewHolder help, T item, int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=getViewHolder(convertView,parent);
        convert(viewHolder,data.get(position),position);
        return viewHolder.getConverView();
    }
    private ViewHolder getViewHolder(View convertView, ViewGroup parent) {
        return ViewHolder.getViewHolder(this.context, convertView, parent, this.itemLayoutId);
    }
}
