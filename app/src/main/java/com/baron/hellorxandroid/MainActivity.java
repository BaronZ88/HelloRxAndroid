package com.baron.hellorxandroid;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baron.hellorxandroid.adapter.AppListAdapter;
import com.baron.hellorxandroid.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author baron (baronzhang[at]anjuke[dot]com)
 */
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.pull_down_srl)
    SwipeRefreshLayout mPullDownSRL;
    @Bind(R.id.app_list_rv)
    RecyclerView mAppListRV;

    private List<AppInfo> mAppInfoList;
    private AppListAdapter mAppListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAppListRV.setLayoutManager(linearLayoutManager);
        mAppInfoList = new ArrayList<>();
        mAppListAdapter = new AppListAdapter(mAppInfoList);
        mAppListRV.setAdapter(mAppListAdapter);

        mPullDownSRL.setOnRefreshListener(this);

        mPullDownSRL.post(new Runnable() {
            @Override
            public void run() {
                mPullDownSRL.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (mAppInfoList != null) {
            mAppInfoList.clear();
            mAppListAdapter.notifyDataSetChanged();
        }
        loadApp();
    }

    private void loadApp() {

        final PackageManager pm = MainActivity.this.getPackageManager();
        Observable.create(new Observable.OnSubscribe<ApplicationInfo>() {
                    @Override
                    public void call(Subscriber<? super ApplicationInfo> subscriber) {
                        List<ApplicationInfo> infoList = getApplicationInfoList(pm);
                        for (ApplicationInfo info : infoList) {
                            subscriber.onNext(info);
                        }
                        subscriber.onCompleted();
                    }
                }).filter(new Func1<ApplicationInfo, Boolean>() {
                    @Override
                    public Boolean call(ApplicationInfo applicationInfo) {
                        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0;
                    }
                }).map(new Func1<ApplicationInfo, AppInfo>() {

                    @Override
                    public AppInfo call(ApplicationInfo applicationInfo) {
                        AppInfo info = new AppInfo();
                        info.setAppIcon(applicationInfo.loadIcon(pm));
                        info.setAppName(applicationInfo.loadLabel(pm).toString());
                        return info;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        mAppListAdapter.notifyDataSetChanged();
                        mPullDownSRL.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPullDownSRL.setRefreshing(false);
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        mAppInfoList.add(appInfo);
                    }
                });
    }

    private List<ApplicationInfo> getApplicationInfoList(final PackageManager pm) {
        return pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}