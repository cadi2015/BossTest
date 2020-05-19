package com.wp.cheez.fragment;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.cheez.R;
import com.wp.cheez.activity.InstallAppDetailActivity;
import com.wp.cheez.application.App;
import com.wp.cheez.utils.LogHelper;
import com.wp.cheez.utils.PackageUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cadi on 2017/4/14.
 */

public class FragmentInstallAppList extends Fragment {
    private static final String TAG = LogHelper.makeTag(FragmentInstallAppList.class);
    private View mRootView;
    private RecyclerView mRvInstallAppList;
    private List<ApplicationInfo> mInstallAppList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        PackageUtil packageUtil = PackageUtil.getInstance(getContext());
        List<ApplicationInfo> installAppList = packageUtil.getAllApplication();
        mInstallAppList = filterSystemAppAndUpdateApp(installAppList);
    }

    private List<ApplicationInfo> filterSystemAppAndUpdateApp(List<ApplicationInfo> list) {
        List<ApplicationInfo> filteredList = new ArrayList<>();
        Iterator<ApplicationInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            ApplicationInfo applicationInfo = iterator.next();
            if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0 || (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                continue;
            }
            filteredList.add(applicationInfo);
        }
        return filteredList;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_install_app_list, null);
        setupViews();
        return mRootView;
    }

    private void setupViews() {
        mRvInstallAppList = mRootView.findViewById(R.id.install_app_rv_list);
        mRvInstallAppList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvInstallAppList.setAdapter(new MyRecViewAdapter(mInstallAppList));
    }

    private class MyRecViewAdapter extends RecyclerView.Adapter<FragmentInstallAppList.MyRecViewHolder> {
        List<ApplicationInfo> installApkList;

        MyRecViewAdapter(List<ApplicationInfo> list) {
            installApkList = list;
        }

        @Override
        public int getItemCount() {
            return installApkList.size();
        }

        @Override
        public FragmentInstallAppList.MyRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyRecViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.layout_rv_item_with_app_list, parent,false));
        }

        @Override
        public void onBindViewHolder(FragmentInstallAppList.MyRecViewHolder holder, int position) {
            ApplicationInfo applicationInfo = installApkList.get(position);
            PackageManager pm = App.getAppContext().getPackageManager();
            String apkName = applicationInfo.loadLabel(pm).toString();
            String packageName = applicationInfo.packageName;
            Drawable apkIcon = applicationInfo.loadIcon(pm);
            holder.tvId.setText(String.valueOf(position + 1));
            holder.tvAppName.setText(apkName);
            holder.tvAppPackageName.setText(packageName);
            holder.ivAppIcon.setBackground(apkIcon);
        }
    }

    private static class MyRecViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        ImageView ivAppIcon;
        TextView tvAppName;
        TextView tvAppPackageName;
        Button btnCheck;

        MyRecViewHolder(View itemView) {
            super(itemView);
            setupViews();
        }
        private void setupViews() {
            tvId = itemView.findViewById(R.id.rv_item_tv_id);
            ivAppIcon = itemView.findViewById(R.id.rv_item_iv_app_icon);
            tvAppName = itemView.findViewById(R.id.rv_item_tv_app_name);
            tvAppPackageName = itemView.findViewById(R.id.rv_item_tv_app_type);
            btnCheck = itemView.findViewById(R.id.rv_item_btn_check);
            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick(View v)");
                    Log.d(TAG, "tvAppPackageName = " + tvAppPackageName.getText().toString());
                    Intent intent = new Intent(App.getAppContext(), InstallAppDetailActivity.class);
                    intent.putExtra("packageName", tvAppPackageName.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getAppContext().startActivity(intent);
                }
            });
        }
    }
}
