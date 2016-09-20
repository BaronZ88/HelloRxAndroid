package com.baron.hellorxandroid.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baron.hellorxandroid.R;
import com.baron.hellorxandroid.model.AppInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author baron (baronzhang[at]anjuke[dot]com)
 */
public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private List<AppInfo> appInfoList;

    public AppListAdapter(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app_list, parent, false);
        return new ViewHolder(rootView);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setBackground(appInfoList.get(position).getAppIcon());
        holder.textView.setText(appInfoList.get(position).getAppName());
    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textView)
        TextView textView;
        @Bind(R.id.imageView)
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
