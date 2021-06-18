package com.google.android.settings.accessibility;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.accessibility.VibrationSettings;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.CustomDialogPreferenceCompat;

public class HapticsDialogPreference extends CustomDialogPreferenceCompat implements DialogInterface.OnClickListener {
    private Context mContext;
    private View mInfoIcon;
    private boolean mIsAvailable;
    private HapticsRingReceiverHelper mReceiver;
    private View mSummary;
    private View mTitle;

    public int getGestureDialogMessage() {
        return R.string.vibration_haptics_settings_not_available_summary;
    }

    public int getGestureDialogTitle() {
        return R.string.vibration_haptics_settings_not_available_title;
    }

    public int getSourceMetricsCategory() {
        return 1292;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
    }

    public HapticsDialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mContext = context;
        init();
    }

    public HapticsDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        init();
    }

    public HapticsDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        init();
    }

    public HapticsDialogPreference(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mTitle = preferenceViewHolder.findViewById(16908310);
        this.mSummary = preferenceViewHolder.findViewById(16908304);
        this.mInfoIcon = preferenceViewHolder.findViewById(R.id.info_button);
        updatePreference();
    }

    public void performClick() {
        if (isAvailable()) {
            performEnabledClick();
        } else {
            super.performClick();
        }
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        super.onPrepareDialogBuilder(builder, onClickListener);
        builder.setTitle(getGestureDialogTitle()).setMessage(getGestureDialogMessage()).setPositiveButton((int) R.string.okay, (DialogInterface.OnClickListener) this).setNegativeButton((CharSequence) "", (DialogInterface.OnClickListener) null);
    }

    /* access modifiers changed from: protected */
    public void updatePreference() {
        this.mIsAvailable = !this.mReceiver.isRingerModeSilent();
        View view = this.mTitle;
        if (view != null) {
            view.setEnabled(isAvailable());
        }
        View view2 = this.mSummary;
        if (view2 != null) {
            view2.setEnabled(isAvailable());
        }
        View view3 = this.mInfoIcon;
        if (view3 != null) {
            view3.setVisibility(isAvailable() ? 8 : 0);
        }
    }

    public String getDestination() {
        return VibrationSettings.class.getName();
    }

    private void performEnabledClick() {
        new SubSettingLauncher(this.mContext).setDestination(getDestination()).setSourceMetricsCategory(getSourceMetricsCategory()).launch();
    }

    /* access modifiers changed from: protected */
    public boolean isAvailable() {
        return this.mIsAvailable;
    }

    private void init() {
        setWidgetLayoutResource(R.layout.preference_widget_info);
        this.mReceiver = new HapticsRingReceiverHelper(this.mContext) {
            public void onChange() {
                HapticsDialogPreference.this.updatePreference();
            }
        };
    }

    public void onAttached() {
        super.onAttached();
        this.mReceiver.register(true);
    }

    public void onDetached() {
        super.onDetached();
        this.mReceiver.register(false);
    }
}
