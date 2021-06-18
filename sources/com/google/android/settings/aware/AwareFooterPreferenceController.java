package com.google.android.settings.aware;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.view.View;
import com.android.settings.R;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.utils.AnnotationSpan;

abstract class AwareFooterPreferenceController extends BasePreferenceController {
    public static final String TIPS_LINK = "tips_link";
    private final AwareFeatureProvider mFeatureProvider = FeatureFactory.getFactory(this.mContext).getAwareFeatureProvider();

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: package-private */
    public abstract int getText();

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AwareFooterPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return this.mFeatureProvider.isSupported(this.mContext) ? 0 : 3;
    }

    public CharSequence getSummary() {
        AnnotationSpan.LinkInfo linkInfo = getLinkInfo();
        AnnotationSpan.LinkInfo tipsLinkInfo = getTipsLinkInfo();
        CharSequence text = this.mContext.getText(getText());
        if (linkInfo != null) {
            text = AnnotationSpan.linkify(text, linkInfo);
        }
        if (tipsLinkInfo == null) {
            return text;
        }
        return AnnotationSpan.linkify(text, tipsLinkInfo);
    }

    private AnnotationSpan.LinkInfo getLinkInfo() {
        return new AnnotationSpan.LinkInfo("link", new AwareFooterPreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getLinkInfo$0(View view) {
        new SubSettingLauncher(this.mContext).setDestination(AwareSettings.class.getName()).setSourceMetricsCategory(getMetricsCategory()).launch();
    }

    /* access modifiers changed from: protected */
    public AnnotationSpan.LinkInfo getTipsLinkInfo() {
        return new AnnotationSpan.LinkInfo(TIPS_LINK, new AwareFooterPreferenceController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getTipsLinkInfo$1(View view) {
        this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.mContext.getString(R.string.tips_help_url_gesture))));
    }
}
