package com.android.settings.inputmethod;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.inputmethod.InputMethodAndSubtypeEnablerManagerCompat;

public class InputMethodAndSubtypePreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private PreferenceFragmentCompat mFragment;
    private InputMethodAndSubtypeEnablerManagerCompat mManager;
    private String mTargetImi;

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

    public InputMethodAndSubtypePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void initialize(PreferenceFragmentCompat preferenceFragmentCompat, String str) {
        this.mFragment = preferenceFragmentCompat;
        this.mTargetImi = str;
        this.mManager = new InputMethodAndSubtypeEnablerManagerCompat(preferenceFragmentCompat);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mManager.init(this.mFragment, this.mTargetImi, preferenceScreen);
    }

    public void onStart() {
        this.mManager.refresh(this.mContext, this.mFragment);
    }

    public void onStop() {
        this.mManager.save(this.mContext, this.mFragment);
    }
}
