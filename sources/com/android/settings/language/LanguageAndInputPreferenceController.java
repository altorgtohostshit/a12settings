package com.android.settings.language;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class LanguageAndInputPreferenceController extends BasePreferenceController {
    private InputMethodManager mInputMethodManager = ((InputMethodManager) this.mContext.getSystemService(InputMethodManager.class));
    private PackageManager mPackageManager = this.mContext.getPackageManager();

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

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

    public LanguageAndInputPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "default_input_method");
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        String packageName = ComponentName.unflattenFromString(string).getPackageName();
        for (InputMethodInfo next : this.mInputMethodManager.getInputMethodList()) {
            if (TextUtils.equals(next.getPackageName(), packageName)) {
                return next.loadLabel(this.mPackageManager);
            }
        }
        return "";
    }
}
