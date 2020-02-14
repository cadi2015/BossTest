package com.wp.cheez.fragment;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
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
        mRvInstallAppList = (RecyclerView) mRootView.findViewById(R.id.install_app_rv_list);
        mRvInstallAppList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvInstallAppList.setAdapter(new MyRecViewAdapter(mInstallAppList));
    }

    private class MyRecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<ApplicationInfo> installApkList;

        MyRecViewAdapter(List<ApplicationInfo> list) {
            super();
            installApkList = list;
        }

        @Override
        public int getItemCount() {
            return installApkList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyRecViewHolder myRecViewHolder = new MyRecViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.layout_rv_item_with_app_list, null));
            return myRecViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyRecViewHolder) {
                ApplicationInfo applicationInfo = installApkList.get(position);
                Log.d(TAG, "applicationInfo flags = " + applicationInfo.flags);
                Log.d(TAG, "applicationInfo flags = " + Integer.toBinaryString(applicationInfo.flags));
                Log.d(TAG, "applicationInfo FLAG_SYSTEM = " + Integer.toBinaryString(ApplicationInfo.FLAG_SYSTEM));
                Log.d(TAG, "applicationInfo FLAG_UPDATED_SYSTEM_APP = " + Integer.toBinaryString(ApplicationInfo.FLAG_UPDATED_SYSTEM_APP));
                Log.d(TAG, "\n");
                String apkName = applicationInfo.loadLabel(getContext().getPackageManager()).toString();
                String packageName = applicationInfo.packageName;
                Drawable apkIcon = applicationInfo.loadIcon(getContext().getPackageManager());
                MyRecViewHolder myRecViewHolder = (MyRecViewHolder) holder;
                myRecViewHolder.tvId.setText(String.valueOf(position + 1));
                myRecViewHolder.tvAppName.setText(apkName);
                myRecViewHolder.tvAppPackageName.setText(packageName);
                myRecViewHolder.ivAppIcon.setBackground(apkIcon);
            }
        }
    }

    private class MyRecViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        ImageView ivAppIcon;
        TextView tvAppName;
        TextView tvAppPackageName;
        Button btnCheck;

        public MyRecViewHolder(View itemView) {
            super(itemView);
            setupViews();
        }

        private void setupViews() {
            tvId = (TextView) itemView.findViewById(R.id.rv_item_tv_id);
            ivAppIcon = (ImageView) itemView.findViewById(R.id.rv_item_iv_app_icon);
            tvAppName = (TextView) itemView.findViewById(R.id.rv_item_tv_app_name);
            tvAppPackageName = (TextView) itemView.findViewById(R.id.rv_item_tv_app_type);
            btnCheck = (Button) itemView.findViewById(R.id.rv_item_btn_check);
            btnCheck.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick(View v)");
                    Log.d(TAG, "tvAppPackageName = " + tvAppPackageName.getText().toString());
                    Intent intent = new Intent(getContext(), InstallAppDetailActivity.class);
                    intent.putExtra("packageName", tvAppPackageName.getText().toString());
                    startActivity(intent);
                }
            });
        }
    }
}
