package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import java.util.ArrayList;
import java.util.List;

public class ColumbusAppRecyclerViewAdapter extends RecyclerView.Adapter<AppViewHolder> {
    private final List<ApplicationInfo> mApplicationInfos;
    private final Context mContext;
    private final int mIconSizePx;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final String mOpenAppValue;
    private int mSelectedPosition;
    private final List<List<ShortcutInfo>> mShortcutInfos;

    public ColumbusAppRecyclerViewAdapter(Context context, List<ApplicationInfo> list, List<List<ShortcutInfo>> list2, int i) {
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mApplicationInfos = list;
        this.mShortcutInfos = list2;
        this.mSelectedPosition = i;
        this.mIconSizePx = context.getResources().getDimensionPixelSize(R.dimen.columbus_app_icon_size);
        this.mOpenAppValue = context.getString(R.string.columbus_setting_action_launch_value);
    }

    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AppViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.columbus_app_list_radio_item, viewGroup, false));
    }

    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        ApplicationInfo applicationInfo = this.mApplicationInfos.get(i);
        List list = this.mShortcutInfos.get(i);
        PackageManager packageManager = this.mContext.getPackageManager();
        appViewHolder.setLabel(packageManager.getApplicationLabel(applicationInfo));
        Drawable applicationIcon = packageManager.getApplicationIcon(applicationInfo);
        int i2 = this.mIconSizePx;
        boolean z = false;
        applicationIcon.setBounds(0, 0, i2, i2);
        appViewHolder.setIcon(applicationIcon);
        appViewHolder.setOnClickListener(new ColumbusAppRecyclerViewAdapter$$ExternalSyntheticLambda0(this, i, applicationInfo));
        if (list == null || list.isEmpty()) {
            appViewHolder.setOnExtrasClickListener((View.OnClickListener) null);
        } else {
            appViewHolder.setOnExtrasClickListener(new ColumbusAppRecyclerViewAdapter$$ExternalSyntheticLambda1(this, applicationInfo, list));
        }
        if (this.mSelectedPosition == i) {
            z = true;
        }
        appViewHolder.setChecked(z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(int i, ApplicationInfo applicationInfo, View view) {
        int i2 = this.mSelectedPosition;
        this.mSelectedPosition = i;
        if (i2 >= 0 && i2 < getItemCount()) {
            notifyItemChanged(i2);
        }
        notifyItemChanged(i);
        Settings.Secure.putString(this.mContext.getContentResolver(), "columbus_action", this.mOpenAppValue);
        Settings.Secure.putString(this.mContext.getContentResolver(), "columbus_launch_app", applicationInfo.packageName);
        Settings.Secure.putString(this.mContext.getContentResolver(), "columbus_launch_app_shortcut", applicationInfo.packageName);
        this.mMetricsFeatureProvider.action(this.mContext, 1757, applicationInfo.packageName);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(ApplicationInfo applicationInfo, List list, View view) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(this.mContext, SubSettings.class);
        intent.putExtra(":settings:show_fragment", ColumbusGestureLaunchAppShortcutSettingsFragment.class.getName());
        intent.putExtra("columbus_launch_app_info", applicationInfo);
        intent.putParcelableArrayListExtra("columbus_app_shortcuts", new ArrayList(list));
        intent.putExtra(":settings:source_metrics", 1871);
        this.mContext.startActivity(intent);
    }

    public int getItemCount() {
        return this.mApplicationInfos.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        private final View mDivider;
        private final ImageView mExtrasIcon;
        private final RadioButton mRadioButton;

        AppViewHolder(View view) {
            super(view);
            this.mRadioButton = (RadioButton) view.findViewById(R.id.radio_button);
            this.mExtrasIcon = (ImageView) view.findViewById(R.id.extras_icon);
            this.mDivider = view.findViewById(R.id.divider);
        }

        /* access modifiers changed from: package-private */
        public void setLabel(CharSequence charSequence) {
            this.mRadioButton.setText(charSequence);
        }

        /* access modifiers changed from: package-private */
        public void setIcon(Drawable drawable) {
            this.mRadioButton.setCompoundDrawablesRelative(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
        }

        /* access modifiers changed from: package-private */
        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.mRadioButton.setOnClickListener(onClickListener);
        }

        /* access modifiers changed from: package-private */
        public void setOnExtrasClickListener(View.OnClickListener onClickListener) {
            this.mExtrasIcon.setOnClickListener(onClickListener);
            int i = onClickListener == null ? 8 : 0;
            this.mExtrasIcon.setVisibility(i);
            this.mDivider.setVisibility(i);
        }

        /* access modifiers changed from: package-private */
        public void setChecked(boolean z) {
            this.mRadioButton.setChecked(z);
        }
    }
}
