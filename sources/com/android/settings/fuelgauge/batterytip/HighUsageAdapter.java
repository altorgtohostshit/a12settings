package com.android.settings.fuelgauge.batterytip;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.util.IconDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.Utils;
import java.util.List;

public class HighUsageAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final Context mContext;
    private final List<AppInfo> mHighUsageAppList;
    private final IconDrawableFactory mIconDrawableFactory;
    private final PackageManager mPackageManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView appIcon;
        public TextView appName;
        public TextView appTime;
        public View view;

        public ViewHolder(View view2) {
            super(view2);
            this.view = view2;
            this.appIcon = (ImageView) view2.findViewById(R.id.app_icon);
            this.appName = (TextView) view2.findViewById(R.id.app_name);
            this.appTime = (TextView) view2.findViewById(R.id.app_screen_time);
        }
    }

    public HighUsageAdapter(Context context, List<AppInfo> list) {
        this.mContext = context;
        this.mHighUsageAppList = list;
        this.mIconDrawableFactory = IconDrawableFactory.newInstance(context);
        this.mPackageManager = context.getPackageManager();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.app_high_usage_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AppInfo appInfo = this.mHighUsageAppList.get(i);
        viewHolder.appIcon.setImageDrawable(Utils.getBadgedIcon(this.mIconDrawableFactory, this.mPackageManager, appInfo.packageName, UserHandle.getUserId(appInfo.uid)));
        CharSequence applicationLabel = Utils.getApplicationLabel(this.mContext, appInfo.packageName);
        if (applicationLabel == null) {
            applicationLabel = appInfo.packageName;
        }
        viewHolder.appName.setText(applicationLabel);
    }

    public int getItemCount() {
        return this.mHighUsageAppList.size();
    }
}
