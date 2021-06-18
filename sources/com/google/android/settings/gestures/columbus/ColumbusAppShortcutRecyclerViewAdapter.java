package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import java.util.List;

public class ColumbusAppShortcutRecyclerViewAdapter extends RecyclerView.Adapter<AppViewHolder> {
    private final Context mContext;
    private final Drawable mIcon;
    private final int mIconSizePx;
    private final LauncherApps mLauncherApps;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final String mOpenAppValue;
    private final String mPackageName;
    private int mSelectedPosition;
    private final List<ShortcutInfo> mShortcutInfos;

    public ColumbusAppShortcutRecyclerViewAdapter(Context context, List<ShortcutInfo> list, String str, Drawable drawable, int i) {
        this.mContext = context;
        this.mShortcutInfos = list;
        this.mPackageName = str;
        this.mIcon = drawable;
        this.mSelectedPosition = i;
        this.mLauncherApps = (LauncherApps) context.getSystemService(LauncherApps.class);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mIconSizePx = context.getResources().getDimensionPixelSize(R.dimen.columbus_app_icon_size);
        this.mOpenAppValue = context.getString(R.string.columbus_setting_action_launch_value);
    }

    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AppViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.columbus_app_list_radio_item, viewGroup, false));
    }

    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        boolean z = false;
        if (i == 0) {
            appViewHolder.setLabel(this.mContext.getString(R.string.columbus_setting_action_launch_title));
            appViewHolder.setIcon(this.mIcon);
            appViewHolder.setOnClickListener(new ColumbusAppShortcutRecyclerViewAdapter$$ExternalSyntheticLambda0(this, i));
        } else {
            ShortcutInfo shortcutInfo = this.mShortcutInfos.get(i - 1);
            appViewHolder.setLabel(shortcutInfo.getLabel());
            Drawable shortcutIconDrawable = this.mLauncherApps.getShortcutIconDrawable(shortcutInfo, 160);
            int i2 = this.mIconSizePx;
            shortcutIconDrawable.setBounds(0, 0, i2, i2);
            appViewHolder.setIcon(shortcutIconDrawable);
            appViewHolder.setOnClickListener(new ColumbusAppShortcutRecyclerViewAdapter$$ExternalSyntheticLambda1(this, i, shortcutInfo));
        }
        if (this.mSelectedPosition == i) {
            z = true;
        }
        appViewHolder.setChecked(z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(int i, View view) {
        int i2 = this.mSelectedPosition;
        this.mSelectedPosition = i;
        if (i2 >= 0 && i2 < getItemCount()) {
            notifyItemChanged(i2);
        }
        notifyItemChanged(i);
        Settings.Secure.putString(this.mContext.getContentResolver(), "columbus_action", this.mOpenAppValue);
        Settings.Secure.putString(this.mContext.getContentResolver(), "columbus_launch_app", this.mPackageName);
        Settings.Secure.putString(this.mContext.getContentResolver(), "columbus_launch_app_shortcut", this.mPackageName);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(int i, ShortcutInfo shortcutInfo, View view) {
        int i2 = this.mSelectedPosition;
        if (i2 >= 0 && i2 < getItemCount()) {
            notifyItemChanged(this.mSelectedPosition);
        }
        this.mSelectedPosition = i;
        notifyItemChanged(i);
        Settings.Secure.putString(this.mContext.getContentResolver(), "columbus_launch_app", this.mPackageName);
        Settings.Secure.putString(this.mContext.getContentResolver(), "columbus_launch_app_shortcut", shortcutInfo.getId());
        this.mMetricsFeatureProvider.action(this.mContext, 1760, shortcutInfo.getId());
    }

    public int getItemCount() {
        return this.mShortcutInfos.size() + 1;
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        private final RadioButton mRadioButton;

        AppViewHolder(View view) {
            super(view);
            this.mRadioButton = (RadioButton) view.findViewById(R.id.radio_button);
        }

        /* access modifiers changed from: package-private */
        public void setLabel(CharSequence charSequence) {
            this.mRadioButton.setText(charSequence);
        }

        /* access modifiers changed from: package-private */
        public void setIcon(Drawable drawable) {
            this.mRadioButton.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
        }

        /* access modifiers changed from: package-private */
        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.mRadioButton.setOnClickListener(onClickListener);
        }

        /* access modifiers changed from: package-private */
        public void setChecked(boolean z) {
            this.mRadioButton.setChecked(z);
        }
    }
}
