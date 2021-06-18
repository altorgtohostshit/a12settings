package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.MainSwitchBar;

public class SettingsMainSwitchBar extends MainSwitchBar {
    private boolean mDisabledByAdmin;
    private RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private String mMetricsTag;
    private OnBeforeCheckedChangeListener mOnBeforeListener;
    private ImageView mRestrictedIcon;

    public interface OnBeforeCheckedChangeListener {
        boolean onBeforeCheckedChanged(Switch switchR, boolean z);
    }

    public SettingsMainSwitchBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public SettingsMainSwitchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SettingsMainSwitchBar(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SettingsMainSwitchBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        addOnSwitchChangeListener(new SettingsMainSwitchBar$$ExternalSyntheticLambda1(this));
        ImageView imageView = (ImageView) findViewById(R.id.restricted_icon);
        this.mRestrictedIcon = imageView;
        imageView.setOnClickListener(new SettingsMainSwitchBar$$ExternalSyntheticLambda0(this, context));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Switch switchR, boolean z) {
        logMetrics(z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Context context, View view) {
        if (this.mDisabledByAdmin) {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(context, this.mEnforcedAdmin);
            onRestrictedIconClick();
        }
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mEnforcedAdmin = enforcedAdmin;
        if (enforcedAdmin != null) {
            super.setEnabled(true);
            this.mDisabledByAdmin = true;
            this.mTextView.setEnabled(false);
            this.mSwitch.setEnabled(false);
            this.mSwitch.setVisibility(8);
            this.mRestrictedIcon.setVisibility(0);
            return;
        }
        this.mDisabledByAdmin = false;
        this.mSwitch.setVisibility(0);
        this.mRestrictedIcon.setVisibility(8);
        setEnabled(isEnabled());
    }

    public void setEnabled(boolean z) {
        if (!z || !this.mDisabledByAdmin) {
            super.setEnabled(z);
        } else {
            setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
        }
    }

    public boolean performClick() {
        return getDelegatingView().performClick();
    }

    /* access modifiers changed from: protected */
    public void onRestrictedIconClick() {
        MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeatureProvider;
        metricsFeatureProvider.action(0, 853, 0, this.mMetricsTag + "/switch_bar|restricted", 1);
    }

    public void setChecked(boolean z) {
        OnBeforeCheckedChangeListener onBeforeCheckedChangeListener = this.mOnBeforeListener;
        if (onBeforeCheckedChangeListener == null || !onBeforeCheckedChangeListener.onBeforeCheckedChanged(this.mSwitch, z)) {
            super.setChecked(z);
        }
    }

    public void setCheckedInternal(boolean z) {
        super.setChecked(z);
    }

    public void setOnBeforeCheckedChangeListener(OnBeforeCheckedChangeListener onBeforeCheckedChangeListener) {
        this.mOnBeforeListener = onBeforeCheckedChangeListener;
    }

    public void setMetricsTag(String str) {
        this.mMetricsTag = str;
    }

    private View getDelegatingView() {
        return this.mDisabledByAdmin ? this.mRestrictedIcon : this.mSwitch;
    }

    private void logMetrics(boolean z) {
        MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeatureProvider;
        metricsFeatureProvider.action(0, 853, 0, this.mMetricsTag + "/switch_bar", z ? 1 : 0);
    }
}
