package com.android.settings.wifi.dpp;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.settings.core.InstrumentedFragment;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public abstract class WifiDppQrCodeBaseFragment extends InstrumentedFragment {
    private GlifLayout mGlifLayout;
    protected FooterButton mLeftButton;
    protected FooterButton mRightButton;
    protected TextView mSummary;

    /* access modifiers changed from: protected */
    public abstract boolean isFooterAvailable();

    public void onViewCreated(View view, Bundle bundle) {
        Class<FooterBarMixin> cls = FooterBarMixin.class;
        super.onViewCreated(view, bundle);
        this.mGlifLayout = (GlifLayout) view;
        this.mSummary = (TextView) view.findViewById(16908304);
        if (isFooterAvailable()) {
            this.mLeftButton = new FooterButton.Builder(getContext()).setButtonType(2).setTheme(2131952137).build();
            ((FooterBarMixin) this.mGlifLayout.getMixin(cls)).setSecondaryButton(this.mLeftButton);
            this.mRightButton = new FooterButton.Builder(getContext()).setButtonType(5).setTheme(2131952136).build();
            ((FooterBarMixin) this.mGlifLayout.getMixin(cls)).setPrimaryButton(this.mRightButton);
        }
        this.mGlifLayout.getHeaderTextView().setAccessibilityLiveRegion(1);
    }

    /* access modifiers changed from: protected */
    public void setHeaderIconImageResource(int i) {
        this.mGlifLayout.setIcon(getDrawable(i));
    }

    private Drawable getDrawable(int i) {
        try {
            return getContext().getDrawable(i);
        } catch (Resources.NotFoundException unused) {
            Log.e("WifiDppQrCodeBaseFragment", "Resource does not exist: " + i);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void setHeaderTitle(String str) {
        this.mGlifLayout.setHeaderText((CharSequence) str);
    }

    /* access modifiers changed from: protected */
    public void setHeaderTitle(int i, Object... objArr) {
        this.mGlifLayout.setHeaderText((CharSequence) getString(i, objArr));
    }

    /* access modifiers changed from: protected */
    public void setProgressBarShown(boolean z) {
        this.mGlifLayout.setProgressBarShown(z);
    }
}
