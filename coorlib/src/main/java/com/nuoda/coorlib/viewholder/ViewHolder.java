package com.nuoda.coorlib.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ViewHolder {
    private final SparseArray<View> viewSparseArray;
    private View converView;
    private View view;

    public ViewHolder(Context context, ViewGroup parent, int layoutId) {
        this.viewSparseArray = new SparseArray<>();
        converView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        converView.setTag(this);
    }

    public static ViewHolder getViewHolder(Context context, View converView, ViewGroup parent, int layoutId) {
        if (converView == null) {
            return new ViewHolder(context, parent, layoutId);
        }
        return (ViewHolder) converView.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = viewSparseArray.get(viewId);
        if (view == null) {
            view = converView.findViewById(viewId);
            viewSparseArray.append(viewId, view);
        }
        return (T) view;
    }

    public View getConverView() {
        return converView;
    }

    public ImageView setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return view;
    }

    public TextView setText(int viewId, String text) {
        TextView textView = (TextView) viewSparseArray.get(viewId);
        textView.setText(text);
        return textView;
    }

    public ImageView setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return view;
    }

}
