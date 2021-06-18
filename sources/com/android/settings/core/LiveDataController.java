package com.android.settings.core;

import android.content.Context;
import android.content.IntentFilter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.utils.ThreadUtils;

public abstract class LiveDataController extends BasePreferenceController {
    private MutableLiveData<CharSequence> mData = new MutableLiveData<>();
    private Preference mPreference;
    protected CharSequence mSummary;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public abstract CharSequence getSummaryTextInBackground();

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

    public LiveDataController(Context context, String str) {
        super(context, str);
        this.mSummary = context.getText(R.string.summary_placeholder);
    }

    public void initLifeCycleOwner(Fragment fragment) {
        this.mData.observe(fragment, new LiveDataController$$ExternalSyntheticLambda0(this));
        ThreadUtils.postOnBackgroundThread((Runnable) new LiveDataController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initLifeCycleOwner$0(CharSequence charSequence) {
        this.mSummary = charSequence;
        refreshSummary(this.mPreference);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initLifeCycleOwner$1() {
        this.mData.postValue(getSummaryTextInBackground());
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public CharSequence getSummary() {
        return this.mSummary;
    }
}
