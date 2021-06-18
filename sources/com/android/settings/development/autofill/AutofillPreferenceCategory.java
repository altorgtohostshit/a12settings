package com.android.settings.development.autofill;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.autofill.AutofillManager;
import androidx.preference.PreferenceCategory;

public final class AutofillPreferenceCategory extends PreferenceCategory {
    private final ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private final ContentObserver mSettingsObserver;

    public AutofillPreferenceCategory(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mSettingsObserver = new ContentObserver(handler) {
            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onChange$0() {
                AutofillPreferenceCategory autofillPreferenceCategory = AutofillPreferenceCategory.this;
                autofillPreferenceCategory.notifyDependencyChange(autofillPreferenceCategory.shouldDisableDependents());
            }

            public void onChange(boolean z, Uri uri, int i) {
                AutofillPreferenceCategory.this.mHandler.postDelayed(new AutofillPreferenceCategory$1$$ExternalSyntheticLambda0(this), 2000);
            }
        };
        this.mContentResolver = context.getContentResolver();
    }

    public void onAttached() {
        super.onAttached();
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("autofill_service"), false, this.mSettingsObserver);
    }

    public void onDetached() {
        this.mContentResolver.unregisterContentObserver(this.mSettingsObserver);
        super.onDetached();
    }

    private boolean isAutofillEnabled() {
        AutofillManager autofillManager = (AutofillManager) getContext().getSystemService(AutofillManager.class);
        boolean z = autofillManager != null && autofillManager.isEnabled();
        Log.v("AutofillPreferenceCategory", "isAutofillEnabled(): " + z);
        return z;
    }

    public boolean shouldDisableDependents() {
        boolean z = !isAutofillEnabled();
        Log.v("AutofillPreferenceCategory", "shouldDisableDependents(): " + z);
        return z;
    }
}
